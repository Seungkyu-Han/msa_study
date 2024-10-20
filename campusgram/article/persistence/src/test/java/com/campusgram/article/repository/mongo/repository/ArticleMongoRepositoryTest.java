package com.campusgram.article.repository.mongo.repository;

import com.campusgram.article.repository.mongo.document.ArticleDocument;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@DataMongoTest
class ArticleMongoRepositoryTest {
    @Container
    static MongoDBContainer mongoDBContainer =
            new MongoDBContainer("mongo:5.0.19")
                    .withEnv("MONGO_INITDB_DATABASE", "seungkyu")
                    .withExposedPorts(27017)
                    .withSharding();

    @Autowired
    private ArticleMongoRepository articleMongoRepository;

    @DynamicPropertySource
    static void configureProperties(
            DynamicPropertyRegistry registry
    ){
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Test
    void saveArticle(){
        var title = "title1";
        var content = "content1";
        var creatorId = "4321";
        var thumbnailImageIds = List.of("1", "2", "3");

        var articleToSave = new ArticleDocument(
                title, content, thumbnailImageIds, creatorId
        );

        var result = articleMongoRepository.save(articleToSave);

        StepVerifier.create(result)
                .assertNext(
                        articleDocument -> {
                            assertNotNull(articleDocument.getId());
                        }
                ).verifyComplete();
    }
}