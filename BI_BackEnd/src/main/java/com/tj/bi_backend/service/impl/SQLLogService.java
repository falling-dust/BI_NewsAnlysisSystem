package com.tj.bi_backend.service.impl;

import com.tj.bi_backend.entity.SQLLogEntry;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tj.bi_backend.service.ISQLLogService;
import org.springframework.stereotype.Service;

@Service
public class SQLLogService implements ISQLLogService {
    @Override
    public List<SQLLogEntry> parseSQLLogFile(String filePath) {
        System.out.println(filePath);
        List<SQLLogEntry> logEntries = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            SQLLogEntry currentEntry = null;

            while ((line = reader.readLine()) != null) {
                SQLLogEntry entry = parseLogEntry(line);

                if (entry != null) {
                    if (currentEntry != null) {
                        logEntries.add(currentEntry);
                    }
                    currentEntry = entry;
                } else if (currentEntry != null) {
                    currentEntry.setSql(currentEntry.getSql() + " " + line.trim());
                }
            }

            if (currentEntry != null) {
                logEntries.add(currentEntry);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return logEntries;
    }

    private SQLLogEntry parseLogEntry(String line) {
        Pattern pattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}");
        Matcher matcher = pattern.matcher(line);

        if (matcher.find() && line.contains("Preparing: ")) {
            String timestamp = matcher.group();
            String sql = line.substring(matcher.end()).trim();
            // 只截取 "Preparing: " 之后的部分
            sql = sql.substring(sql.indexOf("Preparing: ") + "Preparing: ".length()).trim();
            return new SQLLogEntry(timestamp, sql);
        }

        return null;
    }
}
