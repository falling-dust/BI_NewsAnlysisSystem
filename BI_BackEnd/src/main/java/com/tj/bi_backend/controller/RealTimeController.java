package com.tj.bi_backend.controller;

import com.tj.bi_backend.result.Result;
import com.tj.bi_backend.utils.WebSocketUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/real-time")
public class RealTimeController {
    @PostMapping ("/set-user")
    public Result setTargetUserId(@RequestParam String userId){
        WebSocketUtils.setTargetUserId(userId);
        return Result.success();
    }
}
