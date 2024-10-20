package com.campusgram.article.controller;

import com.campusgram.article.entity.Article;
import com.campusgram.article.entity.ArticleThumbnail;
import com.campusgram.article.entity.ArticleThumbnailIdOnly;
import com.campusgram.article.usecase.CreateArticleUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Slf4j
@WebFluxTest(
        controllers = {ArticleController.class}
)
@AutoConfigureWebTestClient
class ArticleControllerTest {

    @MockBean
    CreateArticleUseCase mockCreateArticleUseCase;

    @Autowired
    private WebTestClient webClient;
    @Test
    void contextLoad(){}

    @Test
    @SneakyThrows
    void happyCase(){
        //given
        var newArticleId = "abcd";
        var title = "title1";
        var content = "content1";
        var creatorUserId = "4321";
        var thumbnailImageIds = List.of("1", "2", "3");
        var thumbnails = thumbnailImageIds.stream()
                .map(it -> (ArticleThumbnail)new ArticleThumbnailIdOnly(it))
                .toList();
        var articleToReturn =
                Article.builder()
                        .id(newArticleId)
                        .title(title)
                        .content(content)
                        .thumbnails(thumbnails)
                        .creatorId(creatorUserId)
                        .build();

        when(mockCreateArticleUseCase.execute(any()))
                .thenReturn(Mono.just(articleToReturn));

        //when
        var body = CreateArticleRequest.builder()
                .title(title)
                .thumbnailImageIds(thumbnailImageIds)
                .content(content)
                .build();

        var result = webClient.post()
                .uri("/api/v1/articles")
                .bodyValue(body)
                .header( "Content-Type", "application/json")
                .header("X-User-Id", creatorUserId)
                .exchange()
                .expectBody()
                .jsonPath("$.id").isEqualTo(newArticleId).returnResult();

        log.info(result.toString());
    }
}