package com.tj.bi_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tj.bi_backend.entity.RealTimeUserInterest;
import com.tj.bi_backend.mapper.RIUIMapper;
import com.tj.bi_backend.service.IRIUIService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RIUIService extends ServiceImpl<RIUIMapper, RealTimeUserInterest> implements IRIUIService {
    @Resource
    private RIUIMapper riuiMapper;
    @Override
    public List<RealTimeUserInterest> getByUserId(String userId){
        QueryWrapper<RealTimeUserInterest> wrapper = new QueryWrapper<>();
        wrapper
                .eq("user_id", userId)
                .orderByDesc("time_period");
        return list(wrapper);
    }
}
