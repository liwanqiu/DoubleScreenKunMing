package com.changfeng.tcpdemo;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Created by chang on 2016/7/26.
 */
public class TimeUtil {

    public static SimpleDateFormat dateFormat;
    public static SimpleDateFormat hourMinuteFormat;

    static {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss E");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));

        hourMinuteFormat = new SimpleDateFormat("HH:mm");
        hourMinuteFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
    }
}

