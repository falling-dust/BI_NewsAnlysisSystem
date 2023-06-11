package com.tj.bi_backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tj.bi_backend.entity.RealTimeUserInterest;

import java.util.List;

public interface IRIUIService extends IService<RealTimeUserInterest> {
    List<RealTimeUserInterest> getByUserId(String userId);
}
