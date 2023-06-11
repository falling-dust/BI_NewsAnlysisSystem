package com.tj.bi_backend.controller;

import com.tj.bi_backend.result.Result;
import com.tj.bi_backend.service.ISQLLogService;
import com.tj.bi_backend.service.impl.SQLLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import com.tj.bi_backend.entity.SQLLogEntry;
import java.util.*;
import jakarta.annotation.Resource;

@RestController
@RequestMapping("/log")
public class SQLLogController {

    @Value("${log.path}")
    private String logPath;

    private static final String LOG_FILE_NAME = "/mysql_log.log";

    @Resource
    private ISQLLogService sqlLogService;

    @GetMapping("")
    public Result getSQLLogs() {
        List<SQLLogEntry> logEntries = sqlLogService.parseSQLLogFile(logPath+ LOG_FILE_NAME);
        if (logEntries.isEmpty()){
            return Result.error();
        }
        return Result.success(logEntries);
    }
}
