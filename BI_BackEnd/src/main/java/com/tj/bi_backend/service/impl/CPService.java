package com.tj.bi_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tj.bi_backend.entity.CategoryPopularity;
import com.tj.bi_backend.mapper.CPMapper;
import com.tj.bi_backend.service.ICPService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class CPService extends ServiceImpl<CPMapper, CategoryPopularity> implements ICPService {
    @Resource
    private CPMapper cpMapper;
    @Override
    public List<CategoryPopularity> getByTime(Date startTime, Date endTime){
        QueryWrapper<CategoryPopularity> wrapper = new QueryWrapper<>();
        wrapper
                .between("date", startTime, endTime)
                .orderByAsc("date");
        return list(wrapper);
    }
}
