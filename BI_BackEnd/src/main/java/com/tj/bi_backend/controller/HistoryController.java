package com.tj.bi_backend.controller;

import com.tj.bi_backend.entity.*;
import com.tj.bi_backend.entity.DTO.ClickShowDTO;
import com.tj.bi_backend.entity.DTO.InterestDTO;
import com.tj.bi_backend.entity.DTO.ClicksMutilSearchDTO;
import com.tj.bi_backend.entity.DTO.PopularityDTO;
import com.tj.bi_backend.result.Result;
import com.tj.bi_backend.service.*;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import java.util.*;

import static com.tj.bi_backend.utils.TimeUtils.stringToDate;
import static com.tj.bi_backend.utils.TimeUtils.timestampToString;

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

    @Resource
    private INewsService newsService;

    @Resource
    private IClickHistoryService clickService;

    @GetMapping("/news-popularity")
    public Result getNewsPopularityByNewsId(@RequestParam String newsId){
        List<NewsPopularity> npList = npService.getByNewsId(newsId);  //根据新闻id获取数据
        if(npList.isEmpty()){
            return Result.error();
        }

        List<PopularityDTO> resultList = new ArrayList<>();  //将数据处理为前端需要的格式
        for(NewsPopularity np : npList){
            PopularityDTO npdto = new PopularityDTO();
            npdto.setDate(timestampToString(np.getDate()));
            npdto.setClickTimes(np.getClickTimes());
            resultList.add(npdto);
        }
        return Result.success(resultList);
    }

    @GetMapping("/category-popularity")
    public Result getCategoryPopularity(
            @RequestParam("startTime") String startTime,
            @RequestParam("endTime") String endTime
    ){
        List<CategoryPopularity> cpList = cpService.getByTime(stringToDate(startTime), stringToDate(endTime));  //获取所有的类别兴趣度数据
        if(cpList.isEmpty()){
            return Result.error();
        }

        Map<String, Integer> resultList = new HashMap<>();  //将数据处理为前端需要的格式，此处用字典存储.前面为category，后面为总流行度
        for(CategoryPopularity cp : cpList){
            String category = cp.getCategory();  //判断类型是否在字典的关键词里，如果在则直接插入，如果没有则新建列表后插入
            if(resultList.containsKey(category)){
                Integer tmp = resultList.get(category);
                tmp += cp.getClickTimes();
                resultList.replace(category, tmp);
            }
            else{
                resultList.put(category, cp.getClickTimes());
            }
        }

        return Result.success(resultList);
    }

    @GetMapping("/user-interest")
    public Result getUserInterestByUserId(@RequestParam String userId){
        List<UserInterest> uiList = uiService.getByUserId(userId);
        if(uiList.isEmpty()){
            return Result.error();
        }

        Map<String, List<InterestDTO>> resultList = new HashMap<>();  //将数据处理为前端需要的格式，此处用字典存储
        for(UserInterest ui : uiList){
            InterestDTO tmp = new InterestDTO();  //新建要插入的数据
            tmp.setDate(timestampToString(ui.getDate()));
            tmp.setInterestClicks(ui.getInterestClicks());
            String category = ui.getCategory();  //判断类型是否在字典的关键词里，如果在则直接插入，如果没有则新建列表后插入
            if(resultList.containsKey(category)){
                resultList.get(category).add(tmp);
            }
            else{
                List<InterestDTO> tmpList = new ArrayList<>();
                tmpList.add(tmp);
                resultList.put(category, tmpList);
            }
        }
        return Result.success(resultList);
    }

    @GetMapping("/category-interest")
    public Result getCategoryInterest() {
        List<CategoryInterest> ciList = ciService.list();
        if (ciList.isEmpty()) {
            return Result.error();
        }

        Map<String, List<InterestDTO>> resultList = new HashMap<>();  //将数据处理为前端需要的格式，此处用字典存储
        for (CategoryInterest ci : ciList) {
            InterestDTO tmp = new InterestDTO();  //新建要插入的数据
            tmp.setDate(timestampToString(ci.getDate()));
            tmp.setInterestClicks(ci.getInterestClicks());

            String category = ci.getCategory();  //判断类型是否在字典的关键词里，如果在则直接插入，如果没有则新建列表后插入
            if (resultList.containsKey(category)) {
                resultList.get(category).add(tmp);
            } else {
                List<InterestDTO> tmpList = new ArrayList<>();
                tmpList.add(tmp);
                resultList.put(category, tmpList);
            }
        }
        return Result.success(resultList);
    }

    @PostMapping("/multi-clicks")
    public Result getClickHistory(@RequestBody ClicksMutilSearchDTO clicksMutilSearchDTO) {
        System.out.println(clicksMutilSearchDTO);
        String userId = clicksMutilSearchDTO.getUserId();
        String date = clicksMutilSearchDTO.getDate();
        System.out.println(userId);
        System.out.println(date);
        List<ClickHistory> clicksList = clickService.getByMulti(userId, stringToDate(date));
        List<ClickShowDTO> resList = new ArrayList<>();

        for(ClickHistory c : clicksList){
            News news = newsService.getByNewsId(c.getNewsId());
            if(clicksMutilSearchDTO.getCategory().isEmpty() || news.getCategory().equals(clicksMutilSearchDTO.getCategory())){
                int titleLength = news.getHeadline().length();
                if(titleLength >= clicksMutilSearchDTO.getMinTitle() && titleLength <= clicksMutilSearchDTO.getMaxTitle()){
                    int contentLength = news.getNewsBody().length();
                    if(contentLength >= clicksMutilSearchDTO.getMinContent() && contentLength <= clicksMutilSearchDTO.getMaxContent()){
                        ClickShowDTO clickShowDTO = new ClickShowDTO();
                        clickShowDTO.setDate(timestampToString(c.getExposureTime()));
                        clickShowDTO.setTopic(news.getTopic());
                        clickShowDTO.setTitle(news.getHeadline());
                        clickShowDTO.setContent(news.getNewsBody().substring(0, 100));
                        clickShowDTO.setUserId(c.getUserId());

                        resList.add(clickShowDTO);
                    }
                }
            }
        }

        return Result.success(resList);
    }
}
