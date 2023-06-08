package com.tj.bi_backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class CategoryPopularity {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String category;
    private Timestamp date;
    private Integer clickTimes;
}
