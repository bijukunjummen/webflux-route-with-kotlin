package sample

import org.junit.Test
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import reactor.test.scheduler.VirtualTimeScheduler
import java.time.Duration

class VirtualTimeTest {

    @Test
    fun testATimer() {

        VirtualTimeScheduler.getOrSet()
        val flux = Flux
                .interval(Duration.ofSeconds(4), Duration.ofSeconds(3))
                .take(3)


        StepVerifier.withVirtualTime({ flux })
                .expectSubscription()
                .thenAwait(Duration.ofSeconds(4))
                .expectNext(0)
                .thenAwait(Duration.ofSeconds(3))
                .expectNext(1)
                .thenAwait(Duration.ofSeconds(3))
                .expectNext(2)
                .verifyComplete()


    }
}