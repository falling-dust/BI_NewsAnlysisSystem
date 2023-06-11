package com.tj.bi_backend.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ClickHistory {
    private String userId;
    private String newsId;
    private Integer dwellTime;
    private Timestamp exposureTime;
    private Timestamp exposureDate;
}
