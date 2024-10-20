package com.campusgram.article.usecase;

import com.campusgram.article.entity.Article;
import com.campusgram.article.entity.ArticleThumbnail;
import com.campusgram.article.entity.ArticleThumbnailIdOnly;
import com.campusgram.article.repository.ArticleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateArticleUseCaseTest {

    @InjectMocks
    CreateArticleUseCase createArticleUseCase;

    @Mock
    ArticleRepository articleRepository;

    @Test
    void happyCase(){
        var newArticleId = "1";
        var title = "title1";
        var content = "content1";
        var creatorId = "4321";
        var thumbnailImageIds = List.of("1", "2", "3");

        var savedArticle = Article.builder()
                .id(newArticleId)
                .title(title)
                .content(content)
                .creatorId(creatorId)
                .thumbnails(
                        thumbnailImageIds.stream()
                                .map(ArticleThumbnailIdOnly::new)
                                .collect(Collectors.toList())
                )
                .creatorId(creatorId)
                .build();

        when(articleRepository.save(any()))
                .thenReturn(Mono.just(savedArticle));

        var input = new CreateArticleUseCase.Input(
                title, content, creatorId, thumbnailImageIds
        );

        var result = createArticleUseCase.execute(input);

        StepVerifier.create(result)
                .assertNext(
                        article -> {
                            assertEquals(newArticleId, article.getId());
                            assertEquals(title, article.getTitle());
                            assertEquals(content, article.getContent());
                            assertEquals(creatorId, article.getCreatorId());
                            var imageIds = article.getThumbnails().stream().map(ArticleThumbnail::getId).toList();
                            assertIterableEquals(thumbnailImageIds, imageIds);
                        }
                )
                .verifyComplete();
    }

}