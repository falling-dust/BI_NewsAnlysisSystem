package com.tj.bi_backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tj.bi_backend.entity.News;
import com.tj.bi_backend.mapper.NewsMapper;
import com.tj.bi_backend.service.INewsService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class NewsService extends ServiceImpl<NewsMapper, News> implements INewsService {
    @Resource
    NewsMapper newsMapper;
}
