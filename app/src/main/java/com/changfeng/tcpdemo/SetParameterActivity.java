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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by ${似水流年} on 2016/7/14.
 */
public class SetParameterActivity extends Activity {
    private static final String TAG = "SetParameter类：";
    private static final long serialVersionUID = 777888999L;
    //关于几个设置选项spinner的全变量
    private Spinner itemNumSpinner, fontSizeSpinner, itemScrollCircle, advertiseFontSizeSpinner,
            timeFontSizeSpinner;
    private EditText advertiseEdit;
    private EditText deviceIDEdit, serviceIPEdit, portNumberEdit;
    //全局的toast
    private Toast toast;
    //布局
    private LinearLayout showCountDownLayout, settingLayout;
    //设置一些缺省值
    private String advertiseContent;//广告内容
    private int adFontSize;//广告字体大小
    private int itemNum;//每屏幕item个数
    private int itemCircle;//滚动周期
    private int busInfoFontSize;//发车信息字体大小
    private int timeFontSize;

    private String serviceIP;//服务器地址
    private int portNumber;//端口号
    private String deviceIDNum;//设备ID

    private int itemNumPosition;
    private int busInfoFontPosition;
    private int adFontsizePosition;
    private int itemCirclePosition;
    private int timeFontSizePosition;


//    private Intent parameterIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setBackgroundDrawableResource(R.color.preActivityBg);
        setContentView(R.layout.activity_setparameter);

        findViewID();
        initSpinner();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initValues();
    }

    //    @Override
//    protected void onRestart() {
//        super.onRestart();
////        initValues();
////        //显示保存的参数
////        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.parameterSaved), MODE_PRIVATE);
////        itemNum = sharedPreferences.getInt(getString(R.string.item_number), Constants.ITEM_NUM);
////        busInfoFontSize = sharedPreferences.getInt(getString(R.string.bus_info_font_size),
////                Constants.BUS_INFO_FONT_SIZE);
////        itemCircle = sharedPreferences.getInt(getString(R.string.item_circle),  Constants.ITEM_CIRCLE);
////        adFontSize = sharedPreferences.getInt(getString(R.string.ad_font_size),  Constants.AD_FONT_SIZE);
////        advertiseContent = sharedPreferences.getString(getString(R.string.ad_content),  Constants.AD_CONTENT);
////
////        deviceIDNum = sharedPreferences.getString(getString(R.string.device_ID),
////                Constants.DEVICE_ID_NUMBER);
////        portNumber = sharedPreferences.getInt(getString(R.string.port_number),  Constants.PORT_NUMBER);
////        serviceIP = sharedPreferences.getString(getString(R.string.service_IP),  Constants.SERVICE_IP);
////
////        itemNumPosition = sharedPreferences.getInt(getString(R.string.item_num_positon),  Constants.ITEM_NUM_POSITION);
////        busInfoFontPosition = sharedPreferences.getInt(getString(R.string.bus_info_font_position)
////                ,  Constants.BUS_INFO_FONT_POSITION);
////        adFontsizePosition = sharedPreferences.getInt(getString(R.string.ad_font_position)
////                ,  Constants.AD_FONT_POSITION);
////        itemCirclePosition = sharedPreferences.getInt(getString(R.string.item_circle_position),
////                Constants.ITEM_CIRCLR_POSITION);
////
////        timeFontSizePosition = sharedPreferences.getInt(getString(R.string.time_font_size_position),
////                Constants.TIME_FONT_SIZE_POSITION);
////        timeFontSize = sharedPreferences.getInt(getString(R.string.time_font_size),  Constants.TIME_FONT_SIZE);
////
////
////        Log.i(TAG, "设置界面中restart保存的各种position：" + itemNumPosition + "  " + itemCirclePosition + " " +
////                " " +
////                busInfoFontPosition + "   " + adFontsizePosition);
////
////        itemNumSpinner.setSelection(itemNumPosition, true);
////        fontSizeSpinner.setSelection(busInfoFontPosition, true);
////        itemScrollCircle.setSelection(itemCirclePosition, true);
////        advertiseFontSizeSpinner.setSelection(adFontsizePosition, true);
////        timeFontSizeSpinner.setSelection(timeFontSizePosition, true);
////
////
////        deviceIDEdit.setText(deviceIDNum);
////        portNumberEdit.setText(String.valueOf(portNumber));
////        serviceIPEdit.setText(serviceIP);
////        advertiseEdit.setText(advertiseContent);
////
////        Log.i(TAG, "OnRestart中保存的设备ID：" + deviceIDNum + "端口号：" + portNumber + "   ip:  " +
////                serviceIP + "  广告内容：" +
////                advertiseContent);
//    }

    private void findViewID() {
        deviceIDEdit = (EditText) findViewById(R.id.device_ID_ID);
        serviceIPEdit = (EditText) findViewById(R.id.service_IP_ID);
        portNumberEdit = (EditText) findViewById(R.id.port_number_ID);
        advertiseEdit = (EditText) findViewById(R.id.advertise_ID);

        itemNumSpinner = (Spinner) findViewById(R.id.item_num_spinner_ID);
        itemScrollCircle = (Spinner) findViewById(R.id.item_scroll_cycle_spinner_ID);
        fontSizeSpinner = (Spinner) findViewById(R.id.item_font_size_spinner_ID);
        advertiseFontSizeSpinner = (Spinner) findViewById(R.id.advertise_font_size_spinner_ID);
        timeFontSizeSpinner = (Spinner) findViewById(R.id.time_size_spinner_ID);

//预览设置按钮
        Button previewSetting = (Button) findViewById(R.id.preview_btn_ID);
        previewSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (parameterIntent != null) {
//                    parameterIntent = null;
//                    parameterIntent = new Intent(SetParameterActivity.this, PreviewActivity.class);
//                } else {
//                    parameterIntent = new Intent(SetParameterActivity.this, PreviewActivity.class);
//                }
//                parameterIntent.putExtra(getString(R.string.item_number),
//                        itemNumSpinner.getSelectedItem().toString());
//                parameterIntent.putExtra(getString(R.string.bus_info_font_size),
//                        fontSizeSpinner.getSelectedItem().toString());
//                parameterIntent.putExtra(getString(R.string.ad_font_size),
//                        advertiseFontSizeSpinner.getSelectedItem().toString());
//                parameterIntent.putExtra(getString(R.string.time_font_size), timeFontSizeSpinner
//                        .getSelectedItem().toString());
//                parameterIntent.putExtra(getString(R.string.ad_content), advertiseEdit.getText().toString());
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
                    showToast("设备ID不能为空！");
                    return;
                }
                if (Integer.parseInt(portNumberEdit.getText().toString()) > 65535) {
                    showToast("端口号输入有误，应该小于65535！");
                    return;
                }
//                if (parameterIntent != null) {
//                    parameterIntent = null;
//                    parameterIntent = new Intent(SetParameterActivity.this, MainActivity.class);
//                } else {
//                    parameterIntent = new Intent(SetParameterActivity.this, MainActivity.class);
//                }
                savedParameter();
                startActivity(new Intent(SetParameterActivity.this, MainActivity.class));
            }
        });
    }

    private void initValues() {
        SharedPreferences sharedPreferences = getSharedPreferences
                (getString(R.string.parameterSaved), MODE_PRIVATE);
        itemNum = sharedPreferences.getInt(getString(R.string.item_number), Constants.ITEM_NUM);
        busInfoFontSize = sharedPreferences.getInt(getString(R.string.bus_info_font_size),
                Constants.BUS_INFO_FONT_SIZE);
        itemCircle = sharedPreferences.getInt(getString(R.string.item_circle), Constants.ITEM_CIRCLE);

        adFontSize = sharedPreferences.getInt(getString(R.string.ad_font_size), Constants.AD_FONT_SIZE);
        advertiseContent = sharedPreferences.getString(getString(R.string.ad_content), Constants.AD_CONTENT);

        deviceIDNum = sharedPreferences.getString(getString(R.string.device_ID),
                Constants.DEVICE_ID_NUMBER);
        portNumber = sharedPreferences.getInt(getString(R.string.port_number), Constants.PORT_NUMBER);
        serviceIP = sharedPreferences.getString(getString(R.string.service_IP), Constants.SERVICE_IP);

        itemNumPosition = sharedPreferences.getInt(getString(R.string.item_num_positon), Constants.ITEM_NUM_POSITION);
        busInfoFontPosition = sharedPreferences.getInt(getString(R.string.bus_info_font_position)
                , Constants.BUS_INFO_FONT_POSITION);
        adFontsizePosition = sharedPreferences.getInt(getString(R.string.ad_font_position)
                , Constants.AD_FONT_POSITION);
        itemCirclePosition = sharedPreferences.getInt(getString(R.string.item_circle_position),
                Constants.ITEM_CIRCLR_POSITION);

        timeFontSizePosition = sharedPreferences.getInt(getString(R.string.time_font_size_position),
                Constants.TIME_FONT_SIZE_POSITION);
        timeFontSize = sharedPreferences.getInt(getString(R.string.time_font_size), Constants.TIME_FONT_SIZE);

        itemNumSpinner.setSelection(itemNumPosition, true);
        fontSizeSpinner.setSelection(busInfoFontPosition, true);
        itemScrollCircle.setSelection(itemCirclePosition, true);
        advertiseFontSizeSpinner.setSelection(adFontsizePosition, true);
        timeFontSizeSpinner.setSelection(timeFontSizePosition, true);

        Log.i(TAG, "设置界面中initValues保存的各种position：" + itemNumPosition + "  " + itemCirclePosition
                + " " +
                " " +
                busInfoFontPosition + "   " + adFontsizePosition);
        deviceIDEdit.setText(deviceIDNum);
        portNumberEdit.setText(String.valueOf(portNumber));
        serviceIPEdit.setText(serviceIP);
        advertiseEdit.setText(advertiseContent);
        Log.i(TAG, "initValues函数中保存的设备ID：" + deviceIDNum + "端口号：" + portNumber + "   ip:  " +
                serviceIP + "  广告内容：" +
                advertiseContent);
    }

    private void savedParameter() {
        SharedPreferences myShared = getSharedPreferences(getString(R.string.parameterSaved), Activity
                .MODE_PRIVATE);
        SharedPreferences.Editor editor = myShared.edit();
        editor.putInt(getString(R.string.item_number), Integer.parseInt(itemNumSpinner.getSelectedItem()
                .toString()));
        editor.putInt(getString(R.string.item_num_positon), itemNumSpinner.getSelectedItemPosition());

        editor.putInt(getString(R.string.item_circle), Integer.parseInt(itemScrollCircle
                .getSelectedItem().toString()));
        editor.putInt(getString(R.string.item_circle_position), itemScrollCircle
                .getSelectedItemPosition());

        editor.putInt(getString(R.string.bus_info_font_size), Integer.parseInt(fontSizeSpinner
                .getSelectedItem().toString()));
        editor.putInt(getString(R.string.bus_info_font_position), fontSizeSpinner
                .getSelectedItemPosition());

        editor.putInt(getString(R.string.time_font_size), Integer.parseInt(timeFontSizeSpinner
                .getSelectedItem().toString()));
        editor.putInt(getString(R.string.time_font_size_position), timeFontSizeSpinner
                .getSelectedItemPosition());
        editor.putInt(getString(R.string.ad_font_size), Integer.parseInt(advertiseFontSizeSpinner
                .getSelectedItem().toString()));
        editor.putInt(getString(R.string.ad_font_position), advertiseFontSizeSpinner
                .getSelectedItemPosition());


        Log.i(TAG, "savedParameter 中保存的：" + myShared.getInt(getString(R.string.item_num_positon), 0));

        editor.putString(getString(R.string.ad_content), advertiseEdit.getText().toString());
        editor.putString(getString(R.string.device_ID), (deviceIDEdit.getText().toString()));
        editor.putString(getString(R.string.service_IP), serviceIPEdit.getText().toString());
        editor.putInt(getString(R.string.port_number), Integer.parseInt(portNumberEdit.getText()
                .toString()));
//        editor.commit();
        editor.apply();
    }

    private void initSpinner() {
//首先初始化路线个数
        String[] itemNumStr = new String[10];
        for (int i = 0; i < itemNumStr.length; i++) {
            itemNumStr[i] = String.valueOf(i + 1);
        }
        ArrayAdapter<String> itemNumAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, itemNumStr);
        itemNumAdapter.setDropDownViewResource(R.layout.spinner_layout);
        itemNumSpinner.setAdapter(itemNumAdapter);
        itemNumSpinner.setVisibility(View.VISIBLE);
        itemNumSpinner.setSelection(0, true);
        itemNumSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showToast("您设置的是每屏显示" + itemNumSpinner.getSelectedItem().toString() + "条信息！");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//初始化每条信息滚动周期
        String[] itemScrollCircleStr = new String[10];
        for (int i = 0; i < itemScrollCircleStr.length; i++) {
            itemScrollCircleStr[i] = String.valueOf((i + 5));
        }
        ArrayAdapter<String> itemScrollCircleAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout,
                itemScrollCircleStr);
        itemScrollCircleAdapter.setDropDownViewResource(R.layout.spinner_layout);
        itemScrollCircle.setAdapter(itemScrollCircleAdapter);
        itemScrollCircle.setVisibility(View.VISIBLE);
        itemScrollCircle.setSelection(0, true);
        itemScrollCircle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showToast("您设置的是每" + itemScrollCircle.getSelectedItem().toString() + "滚动一条！");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//发车信息的字体大小
        String[] fontSizeStr = new String[30];
        for (int i = 0; i < fontSizeStr.length; i++) {
            fontSizeStr[i] = String.valueOf((i + 4) * 5);
        }
        ArrayAdapter<String> fontSizeAdapter = new ArrayAdapter<String>(this, R.layout
                .spinner_layout,
                fontSizeStr);
        fontSizeAdapter.setDropDownViewResource(R.layout.spinner_layout);
        fontSizeSpinner.setAdapter(fontSizeAdapter);
        fontSizeSpinner.setVisibility(View.VISIBLE);
        fontSizeSpinner.setSelection(0, true);
        fontSizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showToast("您设置的字体大小：" + fontSizeSpinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//公司信息字体大小
        String[] timeFontSizeStr = new String[15];
        for (int i = 0; i < timeFontSizeStr.length; i++) {
            timeFontSizeStr[i] = String.valueOf((i + 1) * 5);
        }
        ArrayAdapter<String> timeFontSizeAdapter = new ArrayAdapter<String>(this, R.layout
                .spinner_layout,
                timeFontSizeStr);
        timeFontSizeAdapter.setDropDownViewResource(R.layout.spinner_layout);
        timeFontSizeSpinner.setAdapter(timeFontSizeAdapter);
        timeFontSizeSpinner.setVisibility(View.VISIBLE);
        timeFontSizeSpinner.setSelection(0, true);
        timeFontSizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showToast("您设置的字体大小：" + timeFontSizeSpinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//广告内容字体大小
        String[] advertiseFontSizeStr = new String[30];
        for (int i = 0; i < advertiseFontSizeStr.length; i++) {
            advertiseFontSizeStr[i] = String.valueOf((i + 4) * 5);
        }
        ArrayAdapter<String> advertiseFontAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout,
                advertiseFontSizeStr);
        advertiseFontAdapter.setDropDownViewResource(R.layout.spinner_layout);
        advertiseFontSizeSpinner.setAdapter(advertiseFontAdapter);
        advertiseFontSizeSpinner.setVisibility(View.VISIBLE);
        advertiseFontSizeSpinner.setSelection(0, true);
        advertiseFontSizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showToast("您设置的广告字体大小是：" + advertiseFontSizeSpinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void showToast(String text) {
        if (toast == null) {
            toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        toast.show();
    }
}
