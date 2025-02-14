package com.campusgram.article.controller;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CreateArticleRequest {
    private final String title;
    private final String content;
    private final List<String> thumbnailImageIds;
}
