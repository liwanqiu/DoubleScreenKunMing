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

/**
 * Created by ${似水流年} on 2016/7/14.
 */
public class SetParameterActivity extends BaseActivity {
    private static final String TAG = "SetParameterActivity";
    //关于几个设置选项spinner的全变量
    private Spinner itemNumSpinner, busInfoFontSizeSpinner, itemScrollCircle, adFontSizeSpinner,
            timeFontSizeSpinner;
    private EditText advertiseEdit;
    private EditText deviceIDEdit, serviceIPEdit, portNumberEdit;

    //设置一些缺省值
    private String adContent;//广告内容
    private int adFontSize;//广告字体大小
    private int itemNum;//每屏幕item个数
    private int itemCircle;//滚动周期
    private int busInfoFontSize;//发车信息字体大小
    private int timeFontSize;


    private String serverAddress;//服务器地址
    private int port;//端口号
    private String deviceId;//设备ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setBackgroundDrawableResource(R.color.preActivityBg);
        setContentView(R.layout.activity_setparameter);

        findViewID();
        initValues();
        initSpinner();
    }

    private void findViewID() {
        deviceIDEdit = (EditText) findViewById(R.id.device_ID_ID);
        serviceIPEdit = (EditText) findViewById(R.id.service_IP_ID);
        portNumberEdit = (EditText) findViewById(R.id.port_number_ID);
        advertiseEdit = (EditText) findViewById(R.id.advertise_ID);

        itemNumSpinner = (Spinner) findViewById(R.id.item_num_spinner_ID);
        itemScrollCircle = (Spinner) findViewById(R.id.item_scroll_cycle_spinner_ID);
        busInfoFontSizeSpinner = (Spinner) findViewById(R.id.item_font_size_spinner_ID);
        adFontSizeSpinner = (Spinner) findViewById(R.id.advertise_font_size_spinner_ID);
        timeFontSizeSpinner = (Spinner) findViewById(R.id.time_size_spinner_ID);

//预览设置按钮
        Button previewSetting = (Button) findViewById(R.id.preview_btn_ID);
        previewSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savedParameter();
                startActivity(new Intent(SetParameterActivity.this, PreviewActivity.class));
            }
        });
//完成设置按钮
        Button finishSetting = (Button) findViewById(R.id.finishSetting_btn_ID);
        finishSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deviceIDEdit.getText().toString().trim().isEmpty()) {
                    showToast(R.string.message_device_id_not_correct);
                    return;
                }
                if (Integer.parseInt(portNumberEdit.getText().toString()) > 65535) {
                    showToast(R.string.message_port_not_correct);
                    return;
                }
                savedParameter();
                startActivity(new Intent(SetParameterActivity.this, MainActivity.class));
            }
        });
    }

    private void initValues() {
        SharedPreferences sharedPreferences = getSharedPreferences
                (getString(R.string.parameterSaved), MODE_PRIVATE);
        itemNum = sharedPreferences.getInt(getString(R.string.item_number), Constants.DEFAULT_ITEM_NUM);

        busInfoFontSize = sharedPreferences.getInt(getString(R.string.bus_info_font_size),
                Constants.DEFAULT_BUS_INFO_FONT_SIZE);
        itemCircle = sharedPreferences.getInt(getString(R.string.item_circle), Constants.DEFAULT_ITEM_CIRCLE);

        adFontSize = sharedPreferences.getInt(getString(R.string.ad_font_size), Constants.DEFAULT_AD_FONT_SIZE);
        adContent = sharedPreferences.getString(getString(R.string.ad_content), Constants.DEFAULT_AD_CONTENT);

        deviceId = sharedPreferences.getString(getString(R.string.device_ID),
                Constants.DEFAULT_DEVICE_ID);
        port = sharedPreferences.getInt(SharedPref.SERVER_PORT, Constants.DEFAULT_SERVER_PORT);
        serverAddress = sharedPreferences.getString(SharedPref.SERVER_ADDRESS, Constants.DEFAULT_SERVER_ADDRESS);

        timeFontSize = sharedPreferences.getInt(getString(R.string.time_font_size), Constants.DEFAULT_TIME_FONT_SIZE);

        deviceIDEdit.setText(deviceId);
        portNumberEdit.setText(String.valueOf(port));
        serviceIPEdit.setText(serverAddress);
        advertiseEdit.setText(adContent);
        Log.i(TAG, "initValues() 设备ID：" + deviceId + " 端口号：" + port + "   ip:  " +
                serverAddress + "  广告内容：" +
                adContent);
    }

    private void savedParameter() {
        SharedPreferences myShared = getSharedPreferences(getString(R.string.parameterSaved), Activity
                .MODE_PRIVATE);
        SharedPreferences.Editor editor = myShared.edit();
        editor.putInt(getString(R.string.item_number), Integer.parseInt(itemNumSpinner.getSelectedItem()
                .toString()));

        editor.putInt(getString(R.string.item_circle), Integer.parseInt(itemScrollCircle
                .getSelectedItem().toString()));

        editor.putInt(getString(R.string.bus_info_font_size), Integer.parseInt(busInfoFontSizeSpinner
                .getSelectedItem().toString()));

        editor.putInt(getString(R.string.time_font_size), Integer.parseInt(timeFontSizeSpinner
                .getSelectedItem().toString()));
        editor.putInt(getString(R.string.ad_font_size), Integer.parseInt(adFontSizeSpinner
                .getSelectedItem().toString()));


        editor.putString(getString(R.string.ad_content), advertiseEdit.getText().toString());
        editor.putString(getString(R.string.device_ID), (deviceIDEdit.getText().toString()));
        editor.putString(SharedPref.SERVER_ADDRESS, serviceIPEdit.getText().toString());
        editor.putInt(SharedPref.SERVER_PORT, Integer.parseInt(portNumberEdit.getText()
                .toString()));
        editor.apply();
    }

    private void initSpinner() {

        //首先初始化路线个数
        ArrayAdapter<Integer> itemNumAdapter = new ArrayAdapter<>(this, R.layout.spinner_layout, Constants.itemCountList);
        itemNumAdapter.setDropDownViewResource(R.layout.spinner_layout);
        itemNumSpinner.setAdapter(itemNumAdapter);
        itemNumSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showToast("每屏显示线路个数：" + itemNumSpinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        itemNumSpinner.setSelection(Constants.itemCountList.indexOf(itemNum));

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
        itemScrollCircle.setSelection(Constants.itemScrollIntervalList.indexOf(itemCircle));

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
