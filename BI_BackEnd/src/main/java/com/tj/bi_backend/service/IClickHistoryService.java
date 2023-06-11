package com.tj.bi_backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tj.bi_backend.entity.ClickHistory;

import java.util.Date;
import java.util.List;

public interface IClickHistoryService extends IService<ClickHistory> {
    List<ClickHistory> getByMulti(String userId, Date date);
}
