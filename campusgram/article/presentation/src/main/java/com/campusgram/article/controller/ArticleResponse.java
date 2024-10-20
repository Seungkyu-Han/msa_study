package com.campusgram.article.controller;

import lombok.Data;

import java.util.List;

@Data
public class ArticleResponse {

    private final String id;
    private final String title;
    private final String content;
    private final String creatorId;
    private final List<ThumbnailResponse> thumbnails;
}
