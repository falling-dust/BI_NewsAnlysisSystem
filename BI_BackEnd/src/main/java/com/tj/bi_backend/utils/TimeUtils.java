package com.tj.bi_backend.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class TimeUtils {
    public static String timeTransfer(Timestamp timeStamp){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String sd = sdf.format(timeStamp);      // 时间戳转换成时间
        return sd;
    }
}
