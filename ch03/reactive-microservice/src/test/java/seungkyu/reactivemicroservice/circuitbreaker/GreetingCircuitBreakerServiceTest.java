package seungkyu.reactivemicroservice.circuitbreaker;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.test.StepVerifier;

import java.time.Duration;

import static org.mockito.Mockito.*;


@Slf4j
@Import(TestCircuitBreakerConfig.class)
@AutoConfigureReactiveCircuitBreaker
@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = GreetingCircuitBreakerService.class
)
class GreetingCircuitBreakerServiceTest {
    @Autowired
    private GreetingCircuitBreakerService greetingService;

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @SpyBean
    private Greeter spyGreeter;

    private final String successMessage = "Hello seungkyu!";
    private final String fallbackMessage = "Hello world!";

    @Test
    void greetingNoDelay(){
        var mono = greetingService.greeting("seungkyu", 0L);

        StepVerifier.create(mono)
                .expectNext(successMessage)
                .verifyComplete();

        verify(spyGreeter).generate("seungkyu");
    }
    @Test
    void greetingDelay5000AndWait1s() {
        // when, then
        StepVerifier.withVirtualTime(() ->
                        greetingService.greeting("seungkyu", 5000L))
                .thenAwait(Duration.ofSeconds(1))
                .expectNext(fallbackMessage)
                .verifyComplete();

        verify(spyGreeter, times(0)).generate("seungkyu");
    }

    @Test
    void greetingDelay5000AndWait5s(){
        StepVerifier.withVirtualTime(() -> greetingService.greeting("seungkyu", 5000L))
                .thenAwait(Duration.ofSeconds(5))
                .expectNext(fallbackMessage)
                .verifyComplete();


        verify(spyGreeter, never()).generate("seungkyu");
    }

    @Test
    void greetingToThrowException(){
        StepVerifier.create(
                greetingService.greetingWithException("seungkyu")
        ).expectNext(fallbackMessage)
                .verifyComplete();
    }

    @Test
    void makeCircuitBreakerOpen(){
        for(int i = 0; i < 4; i++){
            StepVerifier.create(
                    greetingService.greetingWithId("mini", "seungkyu", 0L)
            ).expectNext(successMessage)
                    .verifyComplete();
        }

        for(int i = 0; i < 2; i++){
            StepVerifier.withVirtualTime(() ->
                    greetingService.greetingWithId("mini", "seungkyu", 5000L))
                    .thenAwait(Duration.ofSeconds(2))
                    .expectNext(fallbackMessage)
                    .verifyComplete();
        }

        for(int i = 0; i < 100; i++){
            StepVerifier.create(
                    greetingService.greetingWithId("mini", "seungkyu", 0L)
            ).expectNext(fallbackMessage)
                    .verifyComplete();
        }

        verify(spyGreeter, times(4)).generate("seungkyu");
    }

    @Test
    void makeCircuitBreakerHalfOpenManually(){
        for(int i = 0; i < 4; i++){
            StepVerifier.withVirtualTime(() ->
                    greetingService.greetingWithId("mini", "seungkyu", 5000L)
            ).thenAwait(Duration.ofSeconds(2))
                    .expectNext(fallbackMessage)
                    .verifyComplete();
        }

        var miniCb = circuitBreakerRegistry.circuitBreaker("mini");
        log.info("change state to half-open manually");
        miniCb.transitionToHalfOpenState();

        var state = circuitBreakerRegistry.circuitBreaker("mini").getState();
        Assertions.assertEquals(CircuitBreaker.State.HALF_OPEN, state);
        StepVerifier.create(
                greetingService.greetingWithId("mini", "seungkyu", 0L)
        ).expectNext(successMessage)
                .verifyComplete();
    }

    @Test
    @SneakyThrows
    void makeCircuitBreakerHalfOpenAutomatically(){
        for(int i = 0; i < 4; i++){
            StepVerifier.withVirtualTime(() ->
                            greetingService.greetingWithId("autoHalf", "seungkyu", 5000L)
                    ).thenAwait(Duration.ofSeconds(2))
                    .expectNext(fallbackMessage)
                    .verifyComplete();
        }

        log.info("wait 6000ms");
        Thread.sleep(6000);

        var state = circuitBreakerRegistry.circuitBreaker("autoHalf").getState();
        Assertions.assertEquals(CircuitBreaker.State.HALF_OPEN, state);
        StepVerifier.create(
                        greetingService.greetingWithId("autoHalf", "seungkyu", 0L)
                ).expectNext(successMessage)
                .verifyComplete();
    }


    @Test
    @SneakyThrows
    void makeCircuitBreakerHalfOpenToClose(){

        var total = 6;
        var failed = 2;

        for(int i = 0; i < 4; i++){
            StepVerifier.withVirtualTime(() ->
                            greetingService.greetingWithId("halfOpen", "seungkyu", 5000L)
                    ).thenAwait(Duration.ofSeconds(2))
                    .expectNext(fallbackMessage)
                    .verifyComplete();
        }

        log.info("wait 6000ms");
        Thread.sleep(6000);

        for(int i = 0; i < total - failed; i++){
            StepVerifier.create(
                    greetingService.greetingWithId("halfOpen", "seungkyu", 0L)
            ).expectNext(successMessage)
                    .verifyComplete();
        }

        for(int i = 0; i < failed; i++){
            StepVerifier.create(
                            greetingService.greetingWithId("halfOpen", "seungkyu", 5000L)
                    )
                    .thenAwait(Duration.ofSeconds(2))
                    .expectNext(fallbackMessage)
                    .verifyComplete();
        }

        var state = circuitBreakerRegistry.circuitBreaker("halfOpen").getState();

        Assertions.assertEquals(CircuitBreaker.State.CLOSED, state);
    }
}
