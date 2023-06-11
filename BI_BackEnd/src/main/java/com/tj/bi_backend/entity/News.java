package com.tj.bi_backend.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class News {
    @TableId(value = "news_id")
    private String newsId;
    private String category;
    private String topic;
    private String headline;
    private String newsBody;
}
