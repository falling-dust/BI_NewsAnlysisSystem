package com.tj.bi_backend.entity.DTO;

import lombok.Data;

@Data
public class ClicksMutilSearchDTO {
    private String date;
    private String category;
    private String userId;
    private Integer maxTitle;
    private Integer minTitle;
    private Integer maxContent;
    private Integer minContent;
}
