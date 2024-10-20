package com.campusgram.article.controller;

import com.campusgram.article.entity.Article;
import com.campusgram.article.usecase.CreateArticleUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/articles")
@RequiredArgsConstructor
@Component
@Slf4j
public class ArticleController {

    private final CreateArticleUseCase createArticleUseCase;

    @PostMapping
    Mono<ArticleResponse> createArticle(
        @RequestBody CreateArticleRequest createArticleRequest,
        @RequestHeader("X-User-Id") String userId
    ){

        var input = new CreateArticleUseCase.Input(
                createArticleRequest.getTitle(),
                createArticleRequest.getContent(),
                userId,
                createArticleRequest.getThumbnailImageIds()
        );
        return createArticleUseCase.execute(input).map(this::fromEntity);
    }

    private ArticleResponse fromEntity(Article article) {
        return new ArticleResponse(
                article.getId(),
                article.getTitle(),
                article.getContent(),
                article.getCreatorId(),
                article.getThumbnails().stream().map(articleThumbnail -> new ThumbnailResponse(
                        articleThumbnail.getId(),
                        articleThumbnail.getUrl(),
                        articleThumbnail.getWidth(),
                        articleThumbnail.getHeight()
                )).toList()
        );
    }
}
