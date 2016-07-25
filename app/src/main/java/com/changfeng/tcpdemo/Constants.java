package com.changfeng.tcpdemo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chang on 2016/7/23.
 */
public class Constants {
    public static final String DEFAULT_AD = "服务大家，快乐你我！";
    public static final int DEFAULT_AD_FONT_SIZE = 80;
    public static final int DEFAULT_ITEM_COUNT = 5;
    public static final int DEFAULT_ITEM_INTERVAL = 6;
    public static final int DEFAULT_BUS_INFO_FONT_SIZE = 80;
    public static final int DEFAULT_TIME_FONT_SIZE = 30;
    public static final String DEFAULT_DEVICE_ID = "871997";
    public static final int DEFAULT_SERVER_PORT = 50000;
    public static final String DEFAULT_SERVER_ADDRESS = "100.20.176.13"; // 移动IP 100.20.176.13   电信IP 172.25.40.31

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
