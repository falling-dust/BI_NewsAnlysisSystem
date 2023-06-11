package com.tj.bi_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tj.bi_backend.entity.NewsPopularity;
import com.tj.bi_backend.mapper.NPMapper;
import com.tj.bi_backend.service.INPService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NPService extends ServiceImpl<NPMapper, NewsPopularity> implements INPService {
    @Resource
    private NPMapper npMapper;

    @Override
    public List<NewsPopularity> getByNewsId(String newsId){
        QueryWrapper<NewsPopularity> wrapper = new QueryWrapper<>();
        wrapper
                .eq("news_id", newsId)
                .orderByAsc("date");

        return list(wrapper);
    }

    @Override
    public List<NewsPopularity> getPopNewsByCategory(String category){
        QueryWrapper<NewsPopularity> wrapper = new QueryWrapper<>();
        wrapper
                .eq("category", category)
                .orderByDesc("date")
                .orderByDesc("click_times");

        return list(wrapper);
    }
}
