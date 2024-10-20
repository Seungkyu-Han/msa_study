package com.campusgram.article.common.image;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageResponse {

    private Long id;
    private String url;
    private Integer width;
    private Integer height;
}
