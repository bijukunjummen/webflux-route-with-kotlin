package sample.handler

import sample.domain.Message
import org.springframework.http.HttpStatus.*
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters.fromObject
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class MessageHandler {

    val map = linkedMapOf<String, Message>()

    fun getMessages(req: ServerRequest): Mono<ServerResponse> =
            ServerResponse.ok().body(fromObject(map.values))


    fun getMessage(req: ServerRequest): Mono<ServerResponse> =
            Mono.just(map[req.pathVariable("id")]).flatMap { v -> ServerResponse.ok().body(fromObject(v)) }

    
    fun addMessage(req: ServerRequest): Mono<ServerResponse> {
        return req.bodyToMono(Message::class.java).flatMap { m -> 
            map[m.id] = m
            ServerResponse.status(CREATED).body(fromObject(m))
        }
    }

    fun updateMessage(req: ServerRequest): Mono<ServerResponse> {
        return req.bodyToMono(Message::class.java).flatMap { m ->
            val id = req.pathVariable("id")
            m.id = id
            map[id] = m
            ServerResponse.status(ACCEPTED).body(fromObject(m))
        }
    }
    
    fun deleteMessage(req: ServerRequest): Mono<ServerResponse> {
        return Mono.just(req.pathVariable("id")).flatMap { id -> 
            map.remove(id)
            ServerResponse.status(ACCEPTED).build()
        }
    }
}