package com.changfeng.tcpdemo;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by chang on 2016/7/25.
 */
public class BusInfoView {
    public static final String TAG = "BusInfoView";

    private Context context;
    private LinearLayout layout;

    private List<BusInfoItemView> itemViews = new ArrayList<>();

//    private List<BusInfo> busInfoList;

    private List<LineInfo> lineInfoList;

    private Timer timer;
    private TimerTask timerTask;

    public BusInfoView(Context context, LinearLayout layout) {
        this.context = context;
        this.layout = layout;

        SharedPreferences preferences = context.getSharedPreferences(SharedPref.name, Context.MODE_PRIVATE);
        int itemNum = preferences.getInt(SharedPref.ITEM_NUM, Constants.DEFAULT_ITEM_NUM);
        int itemInterval = preferences.getInt(SharedPref.BUS_INFO_SCROLL_INTERVAL, Constants.DEFAULT_BUS_INFO_SCROLL_INTERVAL);
        int textSize = preferences.getInt(SharedPref.BUS_INFO_TEXT_SIZE, Constants.DEFAULT_BUS_INFO_TEXT_SIZE);

        int bgColor = ContextCompat.getColor(context, R.color.itemBgColor);
        int anotherBgColor = ContextCompat.getColor(context, R.color.itemAnotherBgColor);
        int itemLayoutBackgroundColor = ContextCompat.getColor(context,R.color.itemLayoutBackgroundColor);


        int fontColor = ContextCompat.getColor(context, R.color.itemFontColor);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        lp.weight = 1;
        for (int i = 0; i < itemNum; i++) {
            BusInfoItemView view = new BusInfoItemView(context);
            if (i % 2 == 0) {
                view.setBackground(ContextCompat.getDrawable(context,R.drawable.item_background_4));
//                view.setBackgroundColor(bgColor);
//                view.setItemLayoutBackgroundColor(itemLayoutBackgroundColor);
//                view.setPadding(0,5,0,5);
            } else {
                view.setBackground(ContextCompat.getDrawable(context, R.drawable.item_background_5));
//                view.setBackgroundColor(anotherBgColor);
//                view.setItemLayoutBackgroundColor(anotherBgColor);
            }
            view.setTextColor(fontColor);
            view.setTextSize(textSize);

            view.setInterval(itemInterval);
            view.setDelay((i + 1) * 500);

            itemViews.add(view);
            layout.addView(view, lp);
        }

        startTimer();

    }

    public void addBusInfo(BusInfo info) {
        if (lineInfoList == null) {
            lineInfoList = new ArrayList<>();
        }

        boolean added = false;
        for (LineInfo lineInfo : lineInfoList) {
            if (lineInfo.getLineName().equals(info.getLineName())) {
                lineInfo.addBusInfo(info);
                added = true;
            }
        }

        if (!added) {
            LineInfo lineInfo = new LineInfo();
            lineInfo.addBusInfo(info);
            lineInfoList.add(lineInfo);
        }

//        if (busInfoList == null) {
//            busInfoList = new ArrayList<>();
//        }
//
//        // 删除已经存在同一自编号的记录
//        for (BusInfo busInfo : busInfoList) {
//            if (info.getBusCustomiseNum().equals(busInfo.getBusCustomiseNum())) {
//                busInfoList.remove(busInfo);
//                for (BusInfoItemView view : itemViews) {
//                    view.deleteInfo(busInfo);
//                }
//                break;
//            }
//        }
//
//        boolean found = false;
//        boolean added = false;
//        for (BusInfoItemView view : itemViews) {
//            if (view.getLineName() != null && view.getLineName().equals(info.getLineName())) {
//                view.addBusInfo(info);
//                found = true;
//                added = true;
//            }
//        }
//
//        if (!found) {
//            for (BusInfoItemView view : itemViews) {
//                if (view.getLineName() == null) {
//                    view.addBusInfo(info);
//                    added = true;
//                    break;
//                }
//            }
//        }
//
//        if (added) {
//            busInfoList.add(info);
//        } else {
//            // TODO: 2016/7/27  当Item 数目小于 线路数目，处理
//        }


    }

    public void close() {
        stopTimer();
    }

    private Runnable updateViewRunnable = new Runnable() {
        @Override
        public void run() {
            if (lineInfoList == null || lineInfoList.isEmpty()) {
                return;
            }

            LineInfo lineInfo = getNextLineInfo();

            for (int i = 0; i < itemViews.size(); ++i) {
                if (i < lineInfo.getBusInfoList().size()) {
                    itemViews.get(i).setBusInfo(lineInfo.getBusInfoList().get(i));
                } else {
                    itemViews.get(i).clearWithLineName(lineInfo.getLineName());

                }

            }


            for (int i = 0; i < lineInfo.getBusInfoList().size(); ++i) {
                if (i > itemViews.size() - 1) {
                    break;
                }

                itemViews.get(i).setBusInfo(lineInfo.getBusInfoList().get(i));
            }
        }
    };


    private int currentLineInfoIndex = 0;

    private LineInfo getNextLineInfo() {
        if (currentLineInfoIndex > lineInfoList.size() - 1) {
            currentLineInfoIndex = 0;
        }
        return lineInfoList.get(currentLineInfoIndex++);
    }

    public void startTimer() {
        stopTimer();
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Log.i(TAG, "run: ");
                ((Activity)context).runOnUiThread(
                        updateViewRunnable
                );
            }
        };
        SharedPreferences sharedPref = context.getSharedPreferences(SharedPref.name, Context.MODE_PRIVATE);
        int interval = sharedPref.getInt(SharedPref.LINE_INTERVAL, Constants.DEFAULT_LINE_INTERVAL);
        timer.schedule(timerTask, 1000, interval * 1000);

    }

    public void stopTimer() {
        if (timer != null) {
            timer.cancel();
        }
        if (timerTask != null) {
            timerTask.cancel();
        }
    }


}
