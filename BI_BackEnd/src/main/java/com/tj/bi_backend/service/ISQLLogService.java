package com.tj.bi_backend.service;

import com.tj.bi_backend.entity.SQLLogEntry;
import java.util.List;

public interface ISQLLogService {
    List<SQLLogEntry> parseSQLLogFile(String filePath);
}

