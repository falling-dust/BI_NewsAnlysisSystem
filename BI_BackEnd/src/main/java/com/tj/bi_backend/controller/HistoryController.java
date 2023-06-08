package com.tj.bi_backend.controller;

import com.tj.bi_backend.entity.CategoryInterest;
import com.tj.bi_backend.entity.CategoryPopularity;
import com.tj.bi_backend.entity.NewsPopularity;
import com.tj.bi_backend.entity.UserInterest;
import com.tj.bi_backend.result.Result;
import com.tj.bi_backend.service.ICIService;
import com.tj.bi_backend.service.ICPService;
import com.tj.bi_backend.service.INPService;
import com.tj.bi_backend.service.IUIService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/history")
public class HistoryController {
    @Resource
    private ICIService ciService;

    @Resource
    private ICPService cpService;

    @Resource
    private INPService npService;

    @Resource
    private IUIService uiService;

    @GetMapping("/news-popularity")
    public Result getNewsPopularityByNewsId(@RequestParam String newsId){
        List<NewsPopularity> npList = npService.getByNewsId(newsId);
        if(npList.isEmpty()){
            return Result.error();
        }
        return Result.success(npList);
    }

    @GetMapping("/category-popularity")
    public Result getCategoryPopularity(){
        List<CategoryPopularity> cpList = cpService.list();
        if(cpList.isEmpty()){
            return Result.error();
        }
        return Result.success(cpList);
    }

    @GetMapping("/user-interest")
    public Result getUserInterestByUserId(@RequestParam String userId){
        List<UserInterest> uiList = uiService.getByUserId(userId);
        if(uiList.isEmpty()){
            return Result.error();
        }
        return Result.success(uiList);
    }

    @GetMapping("/category-interest")
    public Result getUserInterestByUserId(){
        List<CategoryInterest> ciList = ciService.list();
        if(ciList.isEmpty()){
            return Result.error();
        }
        return Result.success(ciList);
    }

}
