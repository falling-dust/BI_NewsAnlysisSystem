package com.tj.bi_backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tj.bi_backend.entity.CategoryInterest;
import com.tj.bi_backend.mapper.CIMapper;
import com.tj.bi_backend.service.ICIService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class CIService extends ServiceImpl<CIMapper, CategoryInterest> implements ICIService {
    @Resource
    private CIMapper ciMapper;

}
