package com.changfeng.tcpdemo;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.changfeng.tcpdemo.widget.MultiScrollNumber;

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
    private TextView nameTextView;
    private MultiScrollNumber numScrollView;
    private MultiScrollNumber hourScrollView;
    private MultiScrollNumber minuteScrollView;
    private TextView timeSeparatorTextView;
    private TextView adTextView;

    private boolean isAdding = false;

    private int delay = 100; // 定时器延时，毫秒

    public void setDelay(int delay) {
        this.delay = delay;
    }


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
        numScrollView = (MultiScrollNumber) view.findViewById(R.id.scroll_number_num);
        hourScrollView = (MultiScrollNumber) view.findViewById(R.id.scroll_number_hour);
        minuteScrollView = (MultiScrollNumber) view.findViewById(R.id.scroll_number_minute);
        timeSeparatorTextView = (TextView) view.findViewById(R.id.text_view_time_separator);
        adTextView = (TextView) view.findViewById(R.id.text_view_ad);
    }

    public void start() {
        startTimer();
    }


    public void addBusInfo(BusInfo info) {
        isAdding = true;
        if (busInfoList == null) {
            busInfoList = new ArrayList<>();
        }
        if (lineName == null) {
            lineName = info.getLineName();
        }
        busInfoList.add(info);
        isAdding = false;
    }

    private void update() {
        if (busInfoList != null && !busInfoList.isEmpty()) {
            if (currentIndex >= busInfoList.size()) {
                currentIndex = 0;
            }
            BusInfo info = busInfoList.get(currentIndex);
            currentIndex++;
            nameTextView.setText(info.getLineName());
            numScrollView.setNumberFromLastVal(Integer.parseInt(info.getBusCustomiseNum().trim()), 4);
            hourScrollView.setNumberFromLastVal(Integer.parseInt(TimeUtil.hourMinuteFormat.format(info.getDepartureTime()).trim().substring(0, 2)), 2);
            minuteScrollView.setNumberFromLastVal(Integer.parseInt(TimeUtil.hourMinuteFormat.format(info.getDepartureTime()).trim().substring(3, 5)), 2);
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
        timer.schedule(timerTask, delay, 100);
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
        numScrollView.setTextColors(new int[]{color, color, color, color});
        hourScrollView.setTextColors(new int[]{color, color});
        minuteScrollView.setTextColors(new int[]{color, color});
        timeSeparatorTextView.setTextColor(color);
        adTextView.setTextColor(color);
    }

    public void setTextSize(int size) {
        nameTextView.setTextSize(size);
        numScrollView.setTextSize(size);
        hourScrollView.setTextSize(size);
        minuteScrollView.setTextSize(size);
        timeSeparatorTextView.setTextSize(size);
        adTextView.setTextSize(size);
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

}
