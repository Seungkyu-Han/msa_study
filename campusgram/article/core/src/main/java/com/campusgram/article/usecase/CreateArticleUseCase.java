package com.campusgram.article.usecase;

import com.campusgram.article.entity.Article;
import com.campusgram.article.entity.ArticleThumbnail;
import com.campusgram.article.entity.ArticleThumbnailIdOnly;
import com.campusgram.article.publisher.ArticleCreatedEvent;
import com.campusgram.article.publisher.ArticleEventPublisher;
import com.campusgram.article.repository.ArticleRepository;
import jakarta.inject.Named;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Named
@RequiredArgsConstructor
public class CreateArticleUseCase {

    private final ArticleEventPublisher articleEventPublisher;
    private final ArticleRepository articleRepository;

    @Data
    public static class Input{
        private final String title;
        private final String content;
        private final String creatorId;
        private final List<String> thumbnailImageIds;
    }

    public Mono<Article> execute(Input input){

        List<ArticleThumbnail> thumbnails = input.thumbnailImageIds.stream()
                .map(ArticleThumbnailIdOnly::new)
                .collect(Collectors.toList());

        var newArticle = Article.builder()
                .title(input.title)
                .content(input.content)
                .thumbnails(thumbnails)
                .creatorId(input.creatorId)
                .build();

        return articleRepository.save(newArticle)
                .doOnNext(article -> {
                    var event = new ArticleCreatedEvent(article.getId(), article.getCreatorId());
                    articleEventPublisher.publish(event);
                });
    }


}
