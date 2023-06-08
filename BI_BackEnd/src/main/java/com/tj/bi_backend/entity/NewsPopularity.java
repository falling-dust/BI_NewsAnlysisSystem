package com.tj.bi_backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author mapleleaf
 * @date 2023年06月08日19:04
 */
@Data
public class NewsPopularity {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String newsId;
    private Timestamp date;
    private Integer clickTimes;
}
