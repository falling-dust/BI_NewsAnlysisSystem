package com.tj.bi_backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tj.bi_backend.entity.NewsPopularity;

import java.util.List;

public interface INPService extends IService<NewsPopularity> {
    List<NewsPopularity> getByNewsId(String newsId);

    List<NewsPopularity> getPopNewsByCategory(String category);
}
