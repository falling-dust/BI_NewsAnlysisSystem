package com.tj.bi_backend.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Clicks {
    private String userId;
    private String newsId;
    private Integer dwellTime;
    private Timestamp exposureTime;
}
