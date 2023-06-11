package com.tj.bi_backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tj.bi_backend.entity.Clicks;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public interface IClickService extends IService<Clicks> {
    List<Clicks> getByMulti(String userId, Date date);
}
