package com.tj.bi_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SQLLogEntry {
    private String timestamp;
    private String sql;
}
