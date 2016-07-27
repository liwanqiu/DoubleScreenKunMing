package com.changfeng.tcpdemo;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chang on 2016/7/25.
 */
public class BusInfoView {
    public static final String TAG = "BusInfoView";

    private Context context;
    private LinearLayout layout;

    private List<BusInfoItemView> itemViews = new ArrayList<>();

    private List<BusInfo> busInfoList;

    public BusInfoView(Context context, LinearLayout layout) {
        this.context = context;
        this.layout = layout;

        SharedPreferences preferences = context.getSharedPreferences(SharedPref.name, Context.MODE_PRIVATE);
        String ad = preferences.getString(SharedPref.AD, Constants.DEFAULT_AD);
        int adFontSize = preferences.getInt(SharedPref.AD_FONT_SIZE, Constants.DEFAULT_AD_FONT_SIZE);
        int itemCount = preferences.getInt(SharedPref.ITEM_COUNT, Constants.DEFAULT_ITEM_COUNT);
        int itemInterval = preferences.getInt(SharedPref.ITEM_INTERVAL, Constants.DEFAULT_ITEM_INTERVAL);
        int busFontSize = preferences.getInt(SharedPref.BUS_INFO_FONT_SIZE, Constants.DEFAULT_BUS_INFO_FONT_SIZE);

        int bgColor = ContextCompat.getColor(context, R.color.itemBgColor);
        int anotherBgColor = ContextCompat.getColor(context, R.color.itemAnotherBgColor);

        int fontColor = ContextCompat.getColor(context, R.color.itemFontColor);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        lp.weight = 1;
        for (int i = 0; i < itemCount; i++) {
            BusInfoItemView view = new BusInfoItemView(context);
            if (i % 2 == 0) {
                view.setBackgroundColor(bgColor);
            } else {
                view.setBackgroundColor(anotherBgColor);
            }
            view.setTextColor(fontColor);
            view.setTextSize(busFontSize);

            view.setInterval(itemInterval);
            view.setPadding(5, 2, 5, 2);

            view.setDelay((i + 1) * 500);

            view.setAd(ad);
            itemViews.add(view);
            layout.addView(view, lp);
            view.start();

        }

    }

    public void addBusInfo(BusInfo info) {

//         // test
//        if (!info.getLineName().equals("84路")) {
//            return;
//        }
//

        if (busInfoList == null) {
            busInfoList = new ArrayList<>();
        }

        // 删除已经存在同一自编号的记录
        for (BusInfo busInfo : busInfoList) {
            if (info.getBusCustomiseNum().equals(busInfo.getBusCustomiseNum())) {
                busInfoList.remove(busInfo);
                for (BusInfoItemView view : itemViews) {
                    view.deleteInfo(busInfo);
                }
                break;
            }
        }

        boolean found = false;
        boolean added = false;
        for (BusInfoItemView view : itemViews) {
            if (view.getLineName() != null && view.getLineName().equals(info.getLineName())) {
                view.addBusInfo(info);
                found = true;
                added = true;
            }
        }

        if (!found) {
            for (BusInfoItemView view : itemViews) {
                if (view.getLineName() == null) {
                    view.addBusInfo(info);
                    added = true;
                    break;
                }
            }
        }

        if (added) {
            busInfoList.add(info);
        } else {
            // TODO: 2016/7/27  当Item 数目小于 线路数目，处理
        }


    }


}
