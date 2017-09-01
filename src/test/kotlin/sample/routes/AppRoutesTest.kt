package sample.routes

import org.junit.Test
import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.bodyFromPublisher
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.ServerResponse.status
import org.springframework.web.reactive.function.server.router
import sample.domain.Message

class AppRoutesTest {

    @Test
    fun testSimpleGet() {
        val routerFunction = router {
            GET("/isokay", { _ -> ok().build() })
            GET("/isokay2")({ _ -> ok().build() })
            "/isokay3" { _ -> ok().build() }
        }

        val client = WebTestClient.bindToRouterFunction(routerFunction).build()

        client.get()
                .uri("/isokay")
                .exchange()
                .expectStatus().isOk

        client.get()
                .uri("/isokay2")
                .exchange()
                .expectStatus().isOk

        client.get()
                .uri("/isokay3")
                .exchange()
                .expectStatus().isOk
    }

    @Test
    fun testNested() {
        val routerFunction = router {
            ("/api" and accept(MediaType.APPLICATION_JSON) and contentType(MediaType.APPLICATION_JSON)).nest {
                POST("/create", { req ->
                    status(HttpStatus.CREATED).body(bodyFromPublisher(req.bodyToMono(Message::class.java)))
                })
                PUT("/update", { req ->
                    status(HttpStatus.ACCEPTED).body(bodyFromPublisher(req.bodyToMono(Message::class.java)))
                })
            }
        }

        val client = WebTestClient.bindToRouterFunction(routerFunction).build()

        client.post()
                .uri("/api/create")
                .body(BodyInserters.fromObject(Message("1", "one")))
                .exchange()
                .expectStatus().isCreated
                .expectBody()
                .jsonPath("$.payload")
                .isEqualTo("one")

        client.put()
                .uri("/api/update")
                .body(BodyInserters.fromObject(Message("1", "one")))
                .exchange()
                .expectStatus().isAccepted
                .expectBody()
                .jsonPath("$.payload")
                .isEqualTo("one")
    }

    @Test
    fun testResources() {
        val routerFunction = router {
            resources("/someresource/**",
                    ClassPathResource("/someresource/response.txt"))
        }

        val client = WebTestClient.bindToRouterFunction(routerFunction).build()

        client.get()
                .uri("/someresource/response.txt")
                .exchange()
                .expectStatus().isOk

    }
}