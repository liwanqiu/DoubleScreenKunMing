package com.changfeng.tcpdemo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

public class InitActivity extends Activity {
    //定时显示
    private TimerTask countDownTask;
    private Timer countDownTimer;
    private int countDown;

    private TextView deviceIdTextView;
    private TextView serverAddressTextView;
    private TextView serverPortTextView;

    private TextView countDownText;

    //    //设置intent
//    private Intent  parameterIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setBackgroundDrawableResource(R.color.preActivityBg);
        setContentView(R.layout.activity_init);

        deviceIdTextView = (TextView) findViewById(R.id.device_id_text_view);
        serverAddressTextView = (TextView) findViewById(R.id.server_address_text_view);
        serverPortTextView = (TextView) findViewById(R.id.server_port_text_view);

        countDownText = (TextView) findViewById(R.id.countDown_text_ID);

        //设置参数按钮
        Button setParameter = (Button) findViewById(R.id.settings_btn_ID);
        setParameter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTimer();
                startActivity(new Intent(InitActivity.this, SetParameterActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTimer();
        SharedPreferences preferences = getSharedPreferences(getString(R.string.parameterSaved), MODE_PRIVATE);

        deviceIdTextView.setText(getString(R.string.device_id, preferences.getString(getString(R.string.device_ID),
                Constants.DEVICE_ID_NUMBER)));
        serverPortTextView.setText(getString(R.string.server_port, preferences.getInt(getString(R.string.port_number), Constants.PORT_NUMBER)));
        serverAddressTextView.setText(getString(R.string.server_address, preferences.getString(getString(R.string.service_IP), Constants.SERVICE_IP)));
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopTimer();
    }

    private void startTimer() {
        countDown = 8;
        if (countDownTimer == null) {
            countDownTimer = new Timer();
        }
        if (countDownTask == null) {
            countDownTask = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            countDownText.setText(countDown + "秒后进入主界面");
                            if (--countDown <= 0) {
                                startActivity(new Intent(InitActivity.this, MainActivity.class));
                            }
                        }
                    });

                }
            };
        }
        countDownTimer.schedule(countDownTask, 100, 1000);
    }

    private void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        if (countDownTask != null) {
            countDownTask.cancel();
            countDownTask = null;
        }
    }
}
