package com.tj.bi_backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tj.bi_backend.entity.CategoryPopularity;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public interface ICPService extends IService<CategoryPopularity> {
    List<CategoryPopularity> getByTime(Timestamp startTime, Timestamp endTime);
}
