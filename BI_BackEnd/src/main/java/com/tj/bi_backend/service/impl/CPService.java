package com.tj.bi_backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tj.bi_backend.entity.CategoryPopularity;
import com.tj.bi_backend.mapper.CPMapper;
import com.tj.bi_backend.service.ICPService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class CPService extends ServiceImpl<CPMapper, CategoryPopularity> implements ICPService {
    @Resource
    private CPMapper cpMapper;
}
