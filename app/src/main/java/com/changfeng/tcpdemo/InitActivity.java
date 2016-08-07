package com.changfeng.tcpdemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class InitActivity extends BaseActivity {
    private static final String TAG = "InitActivity";
    //定时显示
    private TimerTask countDownTask;
    private Timer countDownTimer;
    private int countDown;

    private TextView versionTextView;
    private TextView deviceIdTextView;
    private TextView serverAddressTextView;
    private TextView serverPortTextView;

    private TextView countDownText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setBackgroundDrawableResource(R.color.preActivityBg);
        setContentView(R.layout.activity_init);

        versionTextView = (TextView) findViewById(R.id.version_text_view);
        deviceIdTextView = (TextView) findViewById(R.id.device_id_text_view);
        serverAddressTextView = (TextView) findViewById(R.id.server_address_text_view);
        serverPortTextView = (TextView) findViewById(R.id.server_port_text_view);

        countDownText = (TextView) findViewById(R.id.countDown_text_ID);

        //设置按钮
        Button setParameter = (Button) findViewById(R.id.button_settings);
        setParameter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSetParameterActivity();
            }
        });

        // 测试按钮
        Button button = (Button) findViewById(R.id.button_test);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTestActivity();
            }
        });
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "onResume: ");
        super.onResume();
        startTimer();

        versionTextView.setText(getString(R.string.version, BuildConfig.VERSION_NAME));
        SharedPreferences preferences = getSharedPreferences(SharedPref.name, MODE_PRIVATE);
        deviceIdTextView.setText(getString(R.string.device_id, preferences.getString(SharedPref.DEVICE_ID,
                Constants.DEFAULT_DEVICE_ID)));
        serverPortTextView.setText(getString(R.string.server_port, preferences.getInt(SharedPref.SERVER_PORT, Constants.DEFAULT_SERVER_PORT)));
        serverAddressTextView.setText(getString(R.string.server_address, preferences.getString(SharedPref.SERVER_ADDRESS, Constants.DEFAULT_SERVER_ADDRESS)));
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "onPause: ");
        super.onPause();
        stopTimer();
    }

    private void startTimer() {
        Log.i(TAG, "startTimer: ");
        countDown = 8;
        countDownTimer = new Timer();
        countDownTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        countDownText.setText(countDown + "秒后进入主界面");
                        if (--countDown <= 0) {
                            startMainActivity();
                        }
                    }
                });

            }
        };
        countDownTimer.schedule(countDownTask, 100, 1000);
    }

    private void startTestActivity() {
        Log.i(TAG, "startTestActivity: ");
        startActivity(new Intent(this, TestActivity.class));
    }

    private void startSetParameterActivity() {
        Log.i(TAG, "startSetParameterActivity: ");
        startActivity(new Intent(this, SetParameterActivity.class));
    }

    private void startMainActivity() {
        Log.i(TAG, "startMainActivity: ");
//        startActivity(new Intent(this, MainActivity.class));
        startActivity(new Intent(this, BusInfoActivity.class));
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        startMainActivity();
        return true;
    }

    private void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (countDownTask != null) {
            countDownTask.cancel();
        }
    }
}
