package com.campusgram.article.repository;

import com.campusgram.article.common.image.ImageClient;
import com.campusgram.article.entity.Article;
import com.campusgram.article.entity.ArticleThumbnail;
import com.campusgram.article.repository.mongo.document.ArticleDocument;
import com.campusgram.article.repository.mongo.repository.ArticleMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ArticleRepositoryImpl implements ArticleRepository {

    private final ArticleMongoRepository articleMongoRepository;
    private final ImageClient imageClient;

    @Override
    public Mono<Article> save(Article article) {
        var documentToSave = fromEntity(article);

        return articleMongoRepository.save(documentToSave)
                .flatMap(
                        articleDocument -> this.getThumbnailsByIds(articleDocument.getThumbnailImageIds())
                                .collectList()
                                .map(thumbnails -> fromDocument(articleDocument, thumbnails))
                );
    }

    private ArticleDocument fromEntity(Article article) {
        return new ArticleDocument(
                article.getTitle(),
                article.getContent(),
                article.getThumbnails().stream()
                        .map(ArticleThumbnail::getId)
                        .collect(Collectors.toList()),
                article.getCreatorId()
        );
    }

    private Article fromDocument(
            ArticleDocument articleDocument,
            List<ArticleThumbnail> thumbnails
    ) {

        return Article.builder()
                .id(articleDocument.getId().toHexString())
                .title(articleDocument.getTitle())
                .content(articleDocument.getContent())
                .thumbnails(thumbnails)
                .creatorId(articleDocument.getCreatorId())
                .build();
    }

    private Flux<ArticleThumbnail> getThumbnailsByIds(List<String> imageIds){
        return imageClient.getImagesByIds(imageIds)
                .map(
                        imageResponse -> new ArticleThumbnail(
                                imageResponse.getId().toString(),
                                imageResponse.getUrl(),
                                imageResponse.getWidth(),
                                imageResponse.getHeight()
                        )
                );
    }
}
