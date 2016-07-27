package com.changfeng.tcpdemo;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by chang on 2016/7/25.
 */
public class BusInfoItemView extends LinearLayout {
    public static final String TAG = "BusInfoItemView";

    private LinearLayout mainLayout;
    private TextView timeTextView;
    private TextView nameTextView;
    private TextView numTextView;
    private TextView adTextView;

    private boolean isAdding = false;

    private int interval = 5;

    private int outDateSecondsTolerate = 30;

    private String lineName;

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getLineName() {
        return lineName;
    }

    private List<BusInfo> busInfoList = null;
    private int currentIndex = 0;


    private Context context;

    private Timer timer;
    private TimerTask timerTask;

    public BusInfoItemView(Context context) {
        this(context, null, 0);
    }

    public BusInfoItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BusInfoItemView(Context context, AttributeSet attrs, int styleAttrs) {
        super(context, attrs, styleAttrs);
        this.context = context;

        Log.i(TAG, "BusInfoItemView: ");

        LayoutInflater inflater = LayoutInflater.from(context);
        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.view_bus_info_item, this);
        mainLayout = (LinearLayout) view.findViewById(R.id.layout_bus_info_item);

        nameTextView = (TextView) view.findViewById(R.id.text_view_name);
        numTextView = (TextView) view.findViewById(R.id.text_view_num);
        timeTextView = (TextView) view.findViewById(R.id.text_view_time);
        adTextView = (TextView) view.findViewById(R.id.text_view_ad);

        startTimer();
    }


    public void addBusInfo(BusInfo info) {
        Log.i(TAG, "addBusInfo: ");
        isAdding = true;
        if (busInfoList == null) {
            busInfoList = new ArrayList<>();
        }
        if (lineName == null) {
            lineName = info.getLineName();
        }
//        deleteByNum(info.getBusCustomiseNum());
        Log.i(TAG, "addBusInfo: " + info.toString());
        busInfoList.add(info);
        isAdding = false;
    }

    private void update() {
        if (busInfoList != null && !busInfoList.isEmpty()) {
            Log.i(TAG, "update: " + lineName + " count:" + busInfoList.size());
            for (BusInfo busInfo : busInfoList) {
                Log.i(TAG, "update: " + busInfo.toString());
            }
            currentIndex++;
            if (currentIndex >= busInfoList.size()) {
                currentIndex = 0;
            }
            BusInfo info = busInfoList.get(currentIndex);
            nameTextView.setText(info.getLineName());
            numTextView.setText(info.getBusCustomiseNum());
            timeTextView.setText(TimeUtil.hourMinuteFormat.format(info.getDepartureTime()));
            mainLayout.setVisibility(VISIBLE);
            adTextView.setVisibility(INVISIBLE);
            deleteOneOutdatedInfo();
        } else {
            lineName = null;
            mainLayout.setVisibility(INVISIBLE);
            adTextView.setVisibility(VISIBLE);
        }
    }

    // 避免一下在删除所有的数据造成视觉体验不好
    private void deleteOneOutdatedInfo() {
        if (busInfoList != null && !busInfoList.isEmpty()) {
            for (BusInfo info : busInfoList) {
                if (System.currentTimeMillis() - info.getDepartureTime() > outDateSecondsTolerate * 1000) {
                    busInfoList.remove(info);
                    break;
                }
            }
        }
    }

    public void deleteInfo(BusInfo info) {
        if (busInfoList != null && !busInfoList.isEmpty()) {
            busInfoList.remove(info);
        }
    }

    private void deleteByNum(String num) {
        if (busInfoList != null && !busInfoList.isEmpty()) {
            for (BusInfo busInfo : busInfoList) {
                if (busInfo.getBusCustomiseNum().equals(num)) {
                    busInfoList.remove(busInfo);
                }
            }
        }
    }

    int count = 0;

    private void startTimer() {
        stopTimer();

        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (count++ > interval * 10 && !isAdding) {
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            update();
                            count = 0;
                        }
                    });
                }


            }
        };
        timer.schedule(timerTask, 500, 100);
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
        }
        if (timerTask != null) {
            timerTask.cancel();
        }
    }

    public void setAd(String ad) {
        adTextView.setText(ad);
    }

    public void setTextColor(int color) {
        nameTextView.setTextColor(color);
        numTextView.setTextColor(color);
        timeTextView.setTextColor(color);
        adTextView.setTextColor(color);
    }

    public void setTextSize(int size) {
        nameTextView.setTextSize(size);
        numTextView.setTextSize(size);
        timeTextView.setTextSize(size);
        adTextView.setTextSize(size);
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

}
