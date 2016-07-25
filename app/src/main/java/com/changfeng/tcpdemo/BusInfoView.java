package com.changfeng.tcpdemo;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
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

            view.setAd(ad);
            itemViews.add(view);
            layout.addView(view, lp);

        }

    }

    public void addBusInfo(BusInfo info) {
        boolean found = false;
        for (BusInfoItemView view : itemViews) {
            if (view.getLineName() != null && view.getLineName().equals(info.getLineName())) {
                view.addBusInfo(info);
                found = true;
            }
        }

        if (!found) {
            for (BusInfoItemView view : itemViews) {
                if (view.getLineName() == null) {
                    view.addBusInfo(info);
                    break;
                }
            }
        }
    }


}
