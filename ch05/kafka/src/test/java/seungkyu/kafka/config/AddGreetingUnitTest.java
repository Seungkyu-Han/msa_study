package seungkyu.kafka.config;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class AddGreetingUnitTest {

    StreamFunctionConfig streamFunctionConfig = new StreamFunctionConfig();

    @Test
    void addGreeting() {
        var nameFlux = Flux.just("Hello", "Han", "seungkyu");

        var addGreetingFlux = streamFunctionConfig.addGreeting().apply(nameFlux);

        StepVerifier.create(addGreetingFlux)
                .expectNext("Hello Hello!")
                .expectNext("Hello Han!")
                .expectNext("Hello seungkyu!")
                .verifyComplete();
    }
}
