package com.tj.bi_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tj.bi_backend.entity.UserInterest;
import com.tj.bi_backend.mapper.UIMapper;
import com.tj.bi_backend.service.IUIService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UIService extends ServiceImpl<UIMapper, UserInterest> implements IUIService {
    @Resource
    private UIMapper uiMapper;
    @Override
    public List<UserInterest> getByUserId(String userId){
        QueryWrapper<UserInterest> wrapper = new QueryWrapper<>();
        wrapper
                .eq("user_id", userId)
                .orderByAsc("date");
        return list(wrapper);
    }
}
