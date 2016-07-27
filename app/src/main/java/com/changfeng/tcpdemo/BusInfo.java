package com.changfeng.tcpdemo;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chang on 2016/7/25.
 */
public class BusInfo {
    private Long departureTime;
    private String lineName;
    private String busCustomiseNum;

    public BusInfo(Long time, String name, String num) {
        departureTime = time;
        lineName = name;
        busCustomiseNum = num;
    }

    public void setBusCustomiseNum(String busCustomiseNum) {
        this.busCustomiseNum = busCustomiseNum;
    }

    public Long getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Long departureTime) {
        this.departureTime = departureTime;
    }

    public String getBusCustomiseNum() {
        return busCustomiseNum;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getLineName() {
        return lineName;
    }

    @Override
    public String toString() {
        return departureTime + " " + busCustomiseNum + " " + lineName;
    }

    public String toHumanString() {
        return TimeUtil.dateFormat.format(new Date(departureTime)) + " " + busCustomiseNum + " " + lineName;
    }
}
