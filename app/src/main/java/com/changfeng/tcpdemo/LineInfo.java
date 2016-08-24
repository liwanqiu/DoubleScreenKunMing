package com.changfeng.tcpdemo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chang on 2016-08-24.
 */
public class LineInfo {

    private String lineName;
    private List<BusInfo> busInfoList = new ArrayList<>();

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getLineName() {
        return lineName;
    }

    public void setBusInfoList(List<BusInfo> busInfoList) {
        this.busInfoList = busInfoList;
    }

    public List<BusInfo> getBusInfoList() {
        return busInfoList;
    }

    public void addBusInfo(BusInfo busInfo) {
        lineName = busInfo.getLineName();

        BusInfo a = null;
        for (BusInfo b : busInfoList) {
            if (b.getBusCustomiseNum().equals(busInfo.getBusCustomiseNum())) {
                a = b;
            }
        }
        if (a != null) {
            busInfoList.remove(a);
        }
        busInfoList.add(busInfo);
    }
}
