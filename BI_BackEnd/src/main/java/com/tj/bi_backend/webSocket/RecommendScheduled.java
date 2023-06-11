package com.tj.bi_backend.webSocket;

import com.alibaba.fastjson.JSON;
import com.tj.bi_backend.entity.NewsPopularity;
import com.tj.bi_backend.entity.RealTimeUserInterest;
import com.tj.bi_backend.entity.UserInterest;
import com.tj.bi_backend.service.IRIUIService;
import com.tj.bi_backend.service.impl.NPService;
import com.tj.bi_backend.service.impl.NewsService;
import com.tj.bi_backend.utils.WebSocketUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
@Component
public class RecommendScheduled {

    @Autowired
    private IRIUIService iriuiService;

    @Autowired
    private NPService npService;

    @Autowired
    private NewsService newsService;

    /**
     * 每3秒执行一次
     */
    @Scheduled(cron = "0/3 * * * * ? ") //我这里暂时不需要运行这条定时任务，所以将注解注释了，朋友们运行时记得放开注释啊
    public void nowOnline() {
        System.err.println("*********   首页定时任务执行   **************");

        CopyOnWriteArraySet<WebSocketUtils> webSocketSet = WebSocketUtils.getWebSocketSet();  //获取客户端信息

        List<String> resList = new ArrayList<>();

        WebSocketUtils.setTargetUserId("U153189");

        System.out.println(WebSocketUtils.getTargetUserId());

        if(!WebSocketUtils.getTargetUserId().isEmpty()){  //如果尚未设置目标用户，则返回列表为空
            List<RealTimeUserInterest> uiList = iriuiService.getByUserId(WebSocketUtils.getTargetUserId());  //获取该用户的兴趣清单并倒序，让最新的兴趣点击放在前面

            List<String> ciList = new ArrayList<>();
            for (int i = 0; i < 2 && i < uiList.size(); i++) {  //获取用户感兴趣的前两种新闻类别
                ciList.add(uiList.get(i).getCategory());
            }

            for(String category : ciList){
                List<NewsPopularity> npList = npService.getPopNewsByCategory(category);
                for(int i = 0; i < 3 && i < npList.size(); i++){
                    resList.add(npList.get(i).getNewsId());
                }
            }
        }

        webSocketSet.forEach(c -> {
            try {
                c.sendMessage(JSON.toJSONString(resList));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        System.err.println("/n 首页定时任务完成.......");
    }

}