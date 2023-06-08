package com.tj.bi_backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tj.bi_backend.entity.UserInterest;

import java.util.List;

public interface IUIService extends IService<UserInterest> {
    List<UserInterest> getByUserId(String userId);
}
