package com.changfeng.tcpdemo;

import android.content.Context;
import android.view.ViewGroup;
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

    private LinearLayout layout;
    private TextView timeTextView;
    private TextView nameTextView;
    private TextView numTextView;
    private TextView adTextView;

    private List<BusInfo> busInfoList = null;

    private Context context;

    private Timer timer;
    private TimerTask timerTask;

    public BusInfoItemView(Context context, LinearLayout layout) {
        super(context);
        this.context = context;
        this.layout = layout;
        LinearLayout.LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        lp.weight = 1;
        nameTextView = new TextView(context);
        layout.addView(nameTextView, lp);
        numTextView = new TextView(context);
        layout.addView(numTextView, lp);
        timeTextView = new TextView(context);
        layout.addView(timeTextView, lp);
    }

    public void addBusInfo(BusInfo info) {
        if (busInfoList == null) {
            busInfoList = new ArrayList<>();
        }
        busInfoList.add(info);
    }

    private void update() {

    }

    private void startTimer() {
        stopTimer();
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                update();
            }
        };
        timer.schedule(timerTask, 500, 5000);
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
        }
        if (timerTask != null) {
            timerTask.cancel();
        }
    }


}
