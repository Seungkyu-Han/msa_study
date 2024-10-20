package com.campusgram.article.entity;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Article {
    private final String id;
    private final String title;
    private final String content;
    private final List<ArticleThumbnail> thumbnails;
    private final String creatorId;

}
