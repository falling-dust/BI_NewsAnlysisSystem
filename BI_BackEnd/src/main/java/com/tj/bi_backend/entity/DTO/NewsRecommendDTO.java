package com.tj.bi_backend.entity.DTO;

import lombok.Data;

@Data
public class NewsRecommendDTO {
    private String newsId;
    private String topic;
    private String title;
    private String content;
}
