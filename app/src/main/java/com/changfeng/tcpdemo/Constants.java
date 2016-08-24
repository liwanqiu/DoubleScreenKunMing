package com.changfeng.tcpdemo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chang on 2016/7/23.
 */
public class Constants {

    public static final String SUGGESTION_FILE_NAME = "suggestion.json";

    public static final String DEFAULT_AD = "服务大家，快乐你我他！";
    public static final int DEFAULT_ITEM_NUM = 5;
    public static final int DEFAULT_BUS_INFO_SCROLL_INTERVAL = 6;
    public static final String DEFAULT_TITLE = "昆明公交总公司站点发车信息屏";
    public static final int DEFAULT_BUS_INFO_TEXT_SIZE = 80;
    public static final int DEFAULT_LINE_INTERVAL = 10;
    public static final int DEFAULT_TITLE_TEXT_SIZE = 30;
    public static final String DEFAULT_DEVICE_ID = "871997";
    public static final int DEFAULT_SERVER_PORT = 50000;
    public static final String DEFAULT_SERVER_ADDRESS = "100.20.176.13"; // 移动IP 100.20.176.13   电信IP 172.25.40.31

    public static final int DEFAULT_WEATHER_INTERVAL = 10; // 获取天气事件间隔, 分钟

    public static List<Integer> itemCountList;
    public static List<Integer> itemScrollIntervalList;
    public static List<Integer> busInfoFontSize;
    public static List<Integer> timeFontSizeList;
    public static List<Integer> adFontSizeList;

    static {
        itemCountList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            itemCountList.add(i + 1);
        }

        itemScrollIntervalList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            itemScrollIntervalList.add(i + 5);
        }

        busInfoFontSize = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            busInfoFontSize.add((i + 4) * 5);
        }

        timeFontSizeList = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            timeFontSizeList.add((i + 4) * 5);
        }

        adFontSizeList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            adFontSizeList.add((i + 4) * 5);
        }
    }

}
