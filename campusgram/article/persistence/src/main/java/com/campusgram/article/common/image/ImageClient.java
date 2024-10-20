package com.campusgram.article.common.image;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;

@RequiredArgsConstructor
@Component
public class ImageClient {
    private final WebClient imageWebClient;

    private final ReactiveCircuitBreakerFactory circuitBreakerFactory;

    private final static ImageResponse defaultImage = new ImageResponse(
            0L, "", 0, 0
    );

    public Flux<ImageResponse> getImagesByIds(List<String> imageIds){
        String param = String.join(",", imageIds);
        var flux =  imageWebClient.get()
                .uri("/api/images?imageIds=" + param)
                .retrieve()
                .bodyToFlux(ImageResponse.class);


        return flux.transform(it ->
                circuitBreakerFactory.create("image").run(it, e ->
                        Flux.create(emitter -> {
                            for (String imageId : imageIds) {
                                var defaultImage = new ImageResponse(
                                        Long.parseLong(imageId),
                                        "http://grizz.com/images/default",
                                        100,
                                        100);
                                emitter.next(defaultImage);
                            }
                            emitter.complete();
                        }))
        );
    }

}
