package com.tj.bi_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tj.bi_backend.entity.Clicks;
import com.tj.bi_backend.mapper.ClickMapper;
import com.tj.bi_backend.service.IClickService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ClickService extends ServiceImpl<ClickMapper, Clicks> implements IClickService {
    @Resource
    ClickMapper clickMapper;

    @Override
    public List<Clicks> getByMulti(String userId, Date date){
        QueryWrapper<Clicks> wrapper = new QueryWrapper<>();
        wrapper
                .eq("user_id", userId)
                .eq("date", date);

        return list(wrapper);
    }
}
