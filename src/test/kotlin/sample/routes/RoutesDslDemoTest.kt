package sample.routes

import org.junit.Before
import org.junit.Test
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import sample.domain.Message
import sample.handler.MessageHandler

class RoutesDslDemoTest {
    
    lateinit var client: WebTestClient

    @Before
    fun setUp(): Unit {
        this.client = WebTestClient.bindToRouterFunction(AppRoutes(MessageHandler()).apis()).build()
    }

    @Test
    fun testMessages() {
        this.client.post()
                .uri("/messages")
                .body(BodyInserters.fromObject(Message("1", "one")))
                .exchange()
                .expectStatus().isCreated
                .expectBody()
                .jsonPath("$.id")
                .isEqualTo(1)

        this.client.post()
                .uri("/messages")
                .body(BodyInserters.fromObject(Message("2", "two")))
                .exchange()
                .expectStatus().isCreated
                .expectBody()
                .jsonPath("$.id")
                .isEqualTo(2)

        this.client.get()
                .uri("/messages")
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$.[0].id")
                .isEqualTo(1)

        this.client.delete()
                .uri("/messages/1")
                .exchange()
                .expectStatus().isAccepted

        this.client.get()
                .uri("/messages")
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$.[0].id")
                .isEqualTo(2)
    }
}