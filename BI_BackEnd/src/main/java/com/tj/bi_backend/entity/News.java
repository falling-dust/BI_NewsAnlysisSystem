package com.tj.bi_backend.entity;

import lombok.Data;

@Data
public class News {
    private String newsId;
    private String category;
    private String topic;
    private String headline;
    private String newsBody;
}
