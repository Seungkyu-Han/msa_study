package com.campusgram.article.publisher;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArticleEventPublisherImpl implements ArticleEventPublisher {

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    @SneakyThrows
    public void publish(ArticleEvent articleEvent) {
        var message = objectMapper.writeValueAsString(articleEvent);

        kafkaTemplate.send("articles", message);
    }
}
