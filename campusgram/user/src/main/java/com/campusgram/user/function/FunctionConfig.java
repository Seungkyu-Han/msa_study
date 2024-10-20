package com.campusgram.user.function;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
@EnableKafka
@RequiredArgsConstructor
public class FunctionConfig {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
//
//    @KafkaListener(topics = "articles", groupId = "group2")
//    public Function<Flux<String>, Flux<String>> followerNoti(ObjectMapper objectMapper) {
//
//
//        return input -> input.flatMap(rawEvent -> {
//            try {
//                var event = objectMapper.readValue(rawEvent, ArticleCreatedEvent.class);
//                System.out.println(event);
//                var creatorId = event.getCreatorId();
//                var followerIds = getFollowerIds(creatorId);
//
//                var notiMessages = getSendNotiMessages(objectMapper, creatorId, followerIds);
//                return Flux.fromIterable(notiMessages);
//            } catch (JsonProcessingException e) {
//                return Flux.error(new RuntimeException(e));
//            }
//        });
//    }
//
//    private List<String> getFollowerIds(String creatorId) {
//        return List.of(
//                creatorId + "1",
//                creatorId + "2",
//                creatorId + "3"
//        );
//    }
//
//    private List<String> getSendNotiMessages(
//            ObjectMapper objectMapper,
//            String creatorId,
//            List<String> followerIds) {
//        var notiMessage = creatorId + "님이 소식을 작성했어요!";
//
//        return followerIds.stream()
//                .map(followerId -> {
//                    var message = new SendNotiMessage(followerId, notiMessage);
//                    try {
//                        return objectMapper.writeValueAsString(message);
//                    } catch (JsonProcessingException e) {
//                        throw new RuntimeException(e);
//                    }
//                })
//                .collect(Collectors.toList());
//    }


    @KafkaListener(topics = "articles", groupId = "group2")
    public void listen(String rawEvent) {
        try {
            // Print the raw event received from the articles topic
            System.out.println("Received Message: " + rawEvent);

            // Deserialize the event
            var event = objectMapper().readValue(rawEvent, ArticleCreatedEvent.class);
            System.out.println(event);

            var creatorId = event.getCreatorId();
            var followerIds = getFollowerIds(creatorId);
            var notiMessages = getSendNotiMessages(objectMapper(), creatorId, followerIds);

            // Print the notification messages
            notiMessages.forEach(System.out::println);

            // Optionally, send the notification messages to another Kafka topic
            notiMessages.forEach(message -> kafkaTemplate.send("notifications", message));
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // Print the stack trace for easier debugging
        }
    }

    private List<String> getFollowerIds(String creatorId) {
        return List.of(
                creatorId + "1",
                creatorId + "2",
                creatorId + "3"
        );
    }

    private List<String> getSendNotiMessages(
            ObjectMapper objectMapper,
            String creatorId,
            List<String> followerIds) {
        var notiMessage = creatorId + "님이 소식을 작성했어요!";

        return followerIds.stream()
                .map(followerId -> {
                    var message = new SendNotiMessage(followerId, notiMessage);
                    try {
                        return objectMapper.writeValueAsString(message);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }
}
