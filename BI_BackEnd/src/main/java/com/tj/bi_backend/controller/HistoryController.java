package com.tj.bi_backend.controller;

import com.tj.bi_backend.entity.CategoryInterest;
import com.tj.bi_backend.entity.CategoryPopularity;
import com.tj.bi_backend.entity.DTO.CPDTO;
import com.tj.bi_backend.entity.DTO.NPDTO;
import com.tj.bi_backend.entity.NewsPopularity;
import com.tj.bi_backend.entity.UserInterest;
import com.tj.bi_backend.result.Result;
import com.tj.bi_backend.service.ICIService;
import com.tj.bi_backend.service.ICPService;
import com.tj.bi_backend.service.INPService;
import com.tj.bi_backend.service.IUIService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.tj.bi_backend.utils.TimeUtils.timeTransfer;

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
        List<NewsPopularity> npList = npService.getByNewsId(newsId);  //根据新闻id获取数据
        if(npList.isEmpty()){
            return Result.error();
        }

        List<NPDTO> resultList = new ArrayList<>();  //将数据处理为前端需要的格式
        for(NewsPopularity np : npList){
            NPDTO npdto = new NPDTO();
            npdto.setDate(timeTransfer(np.getDate()));
            npdto.setClickTimes(np.getClickTimes());
            resultList.add(npdto);
        }
        return Result.success(resultList);
    }

    @GetMapping("/category-popularity")
    public Result getCategoryPopularity(
            @RequestParam("startTime") Date startTime,
            @RequestParam("endTime") Date endTime
    ){
        List<CategoryPopularity> cpList = cpService.getByTime(startTime, endTime);  //获取所有的类别兴趣度数据
        if(cpList.isEmpty()){
            return Result.error();
        }

        List<CPDTO> resultList = new ArrayList<>();  //将数据处理为前端需要的格式
        for(CategoryPopularity cp : cpList){
            CPDTO cpdto = new CPDTO();
            cpdto.setCategory(cp.getCategory());
            cpdto.setDate(timeTransfer(cp.getDate()));
            cpdto.setClickTimes(cp.getClickTimes());
            resultList.add(cpdto);
        }

        return Result.success(resultList);
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
