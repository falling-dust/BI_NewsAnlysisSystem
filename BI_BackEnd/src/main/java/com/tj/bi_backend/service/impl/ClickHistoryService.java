package com.tj.bi_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tj.bi_backend.entity.ClickHistory;
import com.tj.bi_backend.mapper.ClickHistoryMapper;
import com.tj.bi_backend.service.IClickHistoryService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ClickHistoryService extends ServiceImpl<ClickHistoryMapper, ClickHistory> implements IClickHistoryService {
    @Resource
    ClickHistoryMapper clickMapper;

    @Override
    public List<ClickHistory> getByMulti(String userId, Date date){
        QueryWrapper<ClickHistory> wrapper = new QueryWrapper<>();
        wrapper
                .eq("user_id", userId)
                .eq("exposure_date", date);

        return list(wrapper);
    }
}
