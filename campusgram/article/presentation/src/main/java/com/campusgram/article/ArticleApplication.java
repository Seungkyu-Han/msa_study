package com.campusgram.article;

import io.github.resilience4j.circuitbreaker.autoconfigure.CircuitBreakerAutoConfiguration;
import io.github.resilience4j.timelimiter.autoconfigure.TimeLimiterAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JAutoConfiguration;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JAutoConfiguration;

import javax.swing.*;
import java.util.function.Function;

@SpringBootApplication
@ImportAutoConfiguration(
        classes = {
                ReactiveResilience4JAutoConfiguration.class,
                Resilience4JAutoConfiguration.class,
                CircuitBreakerAutoConfiguration.class,
                TimeLimiterAutoConfiguration.class,
        }
)
public class ArticleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArticleApplication.class, args);
    }
}

