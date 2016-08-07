package com.changfeng.tcpdemo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ${似水流年} on 2016/7/14.
 */
public class SetParameterActivity extends BaseActivity {
    private static final String TAG = "SetParameterActivity";
    //关于几个设置选项spinner的全变量
    private Spinner itemCountSpinner, busInfoFontSizeSpinner, itemScrollCircle, adFontSizeSpinner,
            timeFontSizeSpinner;
    private EditText adEdit;
    private EditText deviceIdEdit, serviceAddressEdit, serverPortEdit;

    @BindView(R.id.text_edit_weather_interval)
    EditText weatherEditText;

    //设置一些缺省值
    private String ad;//广告内容
    private int adFontSize;//广告字体大小
    private int itemCount;//每屏幕item个数
    private int itemInterval;//滚动周期
    private int busInfoFontSize;//发车信息字体大小
    private int timeFontSize;
    private int weatherInterval;


    private String serverAddress;//服务器地址
    private int serverPort;//端口号
    private String deviceId;//设备ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setBackgroundDrawableResource(R.color.preActivityBg);
        setContentView(R.layout.activity_setparameter);
        ButterKnife.bind(this);

        findViewID();
        initValues();
        initSpinner();
    }

    private boolean isSettingsLegal() {
        if (deviceIdEdit.getText().toString().trim().isEmpty()) {
            showToast(R.string.message_device_id_not_correct);
            return false;
        }
        if (Integer.parseInt(serverPortEdit.getText().toString()) > 65535) {
            showToast(R.string.message_port_not_correct);
            return false;
        }
        if (Integer.parseInt(weatherEditText.getText().toString()) <= 0) {
            showToast(R.string.message_weather_interval_not_correct);
            return false;
        }
        return true;
    }

    private void findViewID() {
        deviceIdEdit = (EditText) findViewById(R.id.device_ID_ID);
        serviceAddressEdit = (EditText) findViewById(R.id.service_IP_ID);
        serverPortEdit = (EditText) findViewById(R.id.port_number_ID);
        adEdit = (EditText) findViewById(R.id.advertise_ID);

        itemCountSpinner = (Spinner) findViewById(R.id.item_num_spinner_ID);
        itemScrollCircle = (Spinner) findViewById(R.id.item_scroll_cycle_spinner_ID);
        busInfoFontSizeSpinner = (Spinner) findViewById(R.id.item_font_size_spinner_ID);
        adFontSizeSpinner = (Spinner) findViewById(R.id.advertise_font_size_spinner_ID);
        timeFontSizeSpinner = (Spinner) findViewById(R.id.time_size_spinner_ID);

//预览设置按钮
        Button previewSetting = (Button) findViewById(R.id.preview_btn_ID);
        previewSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSettingsLegal()) {
                    return;
                }
                savedParameter();
                Intent intent = new Intent(SetParameterActivity.this, BusInfoActivity.class);
                intent.putExtra(BusInfoActivity.EMULATE, true);
                startActivity(intent);
            }
        });
//完成设置按钮
        Button finishSetting = (Button) findViewById(R.id.finishSetting_btn_ID);
        finishSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSettingsLegal()) {
                    return;
                }
                savedParameter();
                finish();
            }
        });
    }

    private void initValues() {
        SharedPreferences sharedPreferences = getSharedPreferences
                (SharedPref.name, MODE_PRIVATE);
        itemCount = sharedPreferences.getInt(SharedPref.ITEM_COUNT, Constants.DEFAULT_ITEM_COUNT);

        busInfoFontSize = sharedPreferences.getInt(SharedPref.BUS_INFO_FONT_SIZE,
                Constants.DEFAULT_BUS_INFO_FONT_SIZE);
        itemInterval = sharedPreferences.getInt(SharedPref.ITEM_INTERVAL, Constants.DEFAULT_ITEM_INTERVAL);

        adFontSize = sharedPreferences.getInt(SharedPref.AD_FONT_SIZE, Constants.DEFAULT_AD_FONT_SIZE);
        ad = sharedPreferences.getString(SharedPref.AD, Constants.DEFAULT_AD);

        deviceId = sharedPreferences.getString(SharedPref.DEVICE_ID,
                Constants.DEFAULT_DEVICE_ID);
        serverPort = sharedPreferences.getInt(SharedPref.SERVER_PORT, Constants.DEFAULT_SERVER_PORT);
        serverAddress = sharedPreferences.getString(SharedPref.SERVER_ADDRESS, Constants.DEFAULT_SERVER_ADDRESS);

        timeFontSize = sharedPreferences.getInt(SharedPref.TIME_FONT_SIZE, Constants.DEFAULT_TIME_FONT_SIZE);

        weatherInterval = sharedPreferences.getInt(SharedPref.WEATHER_INTERVAL, Constants.DEFAULT_WEATHER_INTERVAL);


        deviceIdEdit.setText(deviceId);
        serverPortEdit.setText(String.valueOf(serverPort));
        serviceAddressEdit.setText(serverAddress);
        weatherEditText.setText(String.valueOf(weatherInterval));
        adEdit.setText(ad);
    }

    private void savedParameter() {
        SharedPreferences myShared = getSharedPreferences(SharedPref.name, Activity
                .MODE_PRIVATE);
        SharedPreferences.Editor editor = myShared.edit();
        editor.putInt(SharedPref.ITEM_COUNT, Integer.parseInt(itemCountSpinner.getSelectedItem()
                .toString()));

        editor.putInt(SharedPref.ITEM_INTERVAL, Integer.parseInt(itemScrollCircle
                .getSelectedItem().toString()));

        editor.putInt(SharedPref.BUS_INFO_FONT_SIZE, Integer.parseInt(busInfoFontSizeSpinner
                .getSelectedItem().toString()));

        editor.putInt(SharedPref.TIME_FONT_SIZE, Integer.parseInt(timeFontSizeSpinner
                .getSelectedItem().toString()));
        editor.putInt(SharedPref.AD_FONT_SIZE, Integer.parseInt(adFontSizeSpinner
                .getSelectedItem().toString()));


        editor.putString(SharedPref.AD, adEdit.getText().toString());
        editor.putString(SharedPref.DEVICE_ID, (deviceIdEdit.getText().toString()));
        editor.putString(SharedPref.SERVER_ADDRESS, serviceAddressEdit.getText().toString());
        editor.putInt(SharedPref.SERVER_PORT, Integer.parseInt(serverPortEdit.getText()
                .toString()));
        editor.putInt(SharedPref.WEATHER_INTERVAL, Integer.parseInt(weatherEditText.getText().toString()));
        editor.apply();
    }

    private void initSpinner() {

        //首先初始化路线个数
        ArrayAdapter<Integer> itemNumAdapter = new ArrayAdapter<>(this, R.layout.spinner_layout, Constants.itemCountList);
        itemNumAdapter.setDropDownViewResource(R.layout.spinner_layout);
        itemCountSpinner.setAdapter(itemNumAdapter);
        itemCountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showToast("每屏显示线路个数：" + itemCountSpinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        itemCountSpinner.setSelection(Constants.itemCountList.indexOf(itemCount));

        //初始化每条信息滚动周期
        ArrayAdapter<Integer> itemScrollCircleAdapter = new ArrayAdapter<>(this, R.layout.spinner_layout,
                Constants.itemScrollIntervalList);
        itemScrollCircleAdapter.setDropDownViewResource(R.layout.spinner_layout);
        itemScrollCircle.setAdapter(itemScrollCircleAdapter);
        itemScrollCircle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showToast("信息滚动周期：" + itemScrollCircle.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        itemScrollCircle.setSelection(Constants.itemScrollIntervalList.indexOf(itemInterval));

        //发车信息的字体大小
        ArrayAdapter<Integer> fontSizeAdapter = new ArrayAdapter<>(this, R.layout
                .spinner_layout,
                Constants.busInfoFontSize);
        fontSizeAdapter.setDropDownViewResource(R.layout.spinner_layout);
        busInfoFontSizeSpinner.setAdapter(fontSizeAdapter);
        busInfoFontSizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showToast("发车信息字体大小：" + busInfoFontSizeSpinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        busInfoFontSizeSpinner.setSelection(Constants.busInfoFontSize.indexOf(busInfoFontSize));

        //公司信息字体大小
        ArrayAdapter<Integer> timeFontSizeAdapter = new ArrayAdapter<>(this, R.layout
                .spinner_layout,
                Constants.timeFontSizeList);
        timeFontSizeAdapter.setDropDownViewResource(R.layout.spinner_layout);
        timeFontSizeSpinner.setAdapter(timeFontSizeAdapter);
        timeFontSizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showToast("公司信息字体大小：" + timeFontSizeSpinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        timeFontSizeSpinner.setSelection(Constants.timeFontSizeList.indexOf(timeFontSize));

        //广告内容字体大小
        ArrayAdapter<Integer> advertiseFontAdapter = new ArrayAdapter<>(this, R.layout.spinner_layout,
                Constants.adFontSizeList);
        advertiseFontAdapter.setDropDownViewResource(R.layout.spinner_layout);
        adFontSizeSpinner.setAdapter(advertiseFontAdapter);
        adFontSizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showToast("广告字体大小：" + adFontSizeSpinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        adFontSizeSpinner.setSelection(Constants.adFontSizeList.indexOf(adFontSize));
    }


}
