package com.changfeng.tcpdemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ${似水流年} on 2016/7/14.
 */
public class SettingsActivity extends BaseActivity {
    private static final String TAG = "SettingsActivity";

    @BindView(R.id.spinner_item_num)
    Spinner itemNumSpinner;
    @BindView(R.id.spinner_bus_info_item_text_size)
    Spinner busInfoItemTextSizeSpinner;
    @BindView(R.id.edit_text_line_interval)
    EditText lineIntervalEditText;
    @BindView(R.id.spinner_bus_info_scroll_interval)
    Spinner busInfoScrollIntervalSpinner;
    @BindView(R.id.spinner_title_text_size)
    Spinner titleTextSizeSpinner;
    @BindView(R.id.edit_text_title)
    EditText titleEditText;
    @BindView(R.id.edit_text_ad)
    EditText adEditText;
    @BindView(R.id.edit_text_device_id)
    EditText deviceIdEditText;
    @BindView(R.id.edit_text_server)
    EditText serverEditText;
    @BindView(R.id.edit_text_server_port)
    EditText serverPortEditText;
    @BindView(R.id.text_edit_weather_interval)
    EditText weatherEditText;

    @OnClick(R.id.button_suggestion)
    void toSuggestion(View view) {
        startActivity(new Intent(SettingsActivity.this, SuggestionActivity.class));
    }

    @OnClick(R.id.button_preview)
    void toPreview(View view) {
        if (!isSettingsLegal()) {
            return;
        }
        savedParameter();
        Intent intent = new Intent(SettingsActivity.this, BusInfoActivity.class);
        intent.putExtra(BusInfoActivity.EMULATE, true);
        startActivity(intent);
    }

    @OnClick(R.id.button_ok)
    void toOk(View view) {
        if (!isSettingsLegal()) {
            return;
        }
        savedParameter();
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setBackgroundDrawableResource(R.color.preActivityBg);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        SharedPreferences sharedPreferences = getSharedPreferences
                (SharedPref.name, MODE_PRIVATE);
        int itemNum = sharedPreferences.getInt(SharedPref.ITEM_NUM, Constants.DEFAULT_ITEM_NUM);

        int busInfoTextSize = sharedPreferences.getInt(SharedPref.BUS_INFO_TEXT_SIZE,
                Constants.DEFAULT_BUS_INFO_TEXT_SIZE);
        int lineInterval = sharedPreferences.getInt(SharedPref.LINE_INTERVAL, Constants.DEFAULT_LINE_INTERVAL);
        lineIntervalEditText.setText(String.valueOf(lineInterval));

        int busInfoScrollInterval = sharedPreferences.getInt(SharedPref.BUS_INFO_SCROLL_INTERVAL, Constants.DEFAULT_BUS_INFO_SCROLL_INTERVAL);

        String ad = sharedPreferences.getString(SharedPref.AD, Constants.DEFAULT_AD);

        String deviceId = sharedPreferences.getString(SharedPref.DEVICE_ID,
                Constants.DEFAULT_DEVICE_ID);
        int serverPort = sharedPreferences.getInt(SharedPref.SERVER_PORT, Constants.DEFAULT_SERVER_PORT);
        String serverAddress = sharedPreferences.getString(SharedPref.SERVER_ADDRESS, Constants.DEFAULT_SERVER_ADDRESS);

        String title = sharedPreferences.getString(SharedPref.TITLE, Constants.DEFAULT_TITLE);
        int titleTextSize = sharedPreferences.getInt(SharedPref.TITLE_TEXT_SIZE, Constants.DEFAULT_TITLE_TEXT_SIZE);

        int weatherInterval = sharedPreferences.getInt(SharedPref.WEATHER_INTERVAL, Constants.DEFAULT_WEATHER_INTERVAL);

        titleEditText.setText(title);
        adEditText.setText(ad);
        deviceIdEditText.setText(deviceId);
        serverPortEditText.setText(String.valueOf(serverPort));
        serverEditText.setText(serverAddress);
        weatherEditText.setText(String.valueOf(weatherInterval));

        //首先初始化路线个数
        ArrayAdapter<Integer> itemNumAdapter = new ArrayAdapter<>(this, R.layout.spinner_layout, Constants.itemCountList);
        itemNumAdapter.setDropDownViewResource(R.layout.spinner_layout);
        itemNumSpinner.setAdapter(itemNumAdapter);
        itemNumSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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
        busInfoScrollIntervalSpinner.setAdapter(itemScrollCircleAdapter);
        busInfoScrollIntervalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        busInfoScrollIntervalSpinner.setSelection(Constants.itemScrollIntervalList.indexOf(busInfoScrollInterval));

        //发车信息的字体大小
        ArrayAdapter<Integer> fontSizeAdapter = new ArrayAdapter<>(this, R.layout
                .spinner_layout,
                Constants.busInfoFontSize);
        fontSizeAdapter.setDropDownViewResource(R.layout.spinner_layout);
        busInfoItemTextSizeSpinner.setAdapter(fontSizeAdapter);
        busInfoItemTextSizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        busInfoItemTextSizeSpinner.setSelection(Constants.busInfoFontSize.indexOf(busInfoTextSize));

        // 标题字体大小
        ArrayAdapter<Integer> timeFontSizeAdapter = new ArrayAdapter<>(this, R.layout
                .spinner_layout,
                Constants.timeFontSizeList);
        timeFontSizeAdapter.setDropDownViewResource(R.layout.spinner_layout);
        titleTextSizeSpinner.setAdapter(timeFontSizeAdapter);
        titleTextSizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        titleTextSizeSpinner.setSelection(Constants.timeFontSizeList.indexOf(titleTextSize));
    }

    private boolean isSettingsLegal() {
        if (deviceIdEditText.getText().toString().trim().isEmpty()) {
            showToast(R.string.message_device_id_not_correct);
            return false;
        }
        if (Integer.parseInt(serverPortEditText.getText().toString()) > 65535) {
            showToast(R.string.message_port_not_correct);
            return false;
        }
        if (Integer.parseInt(weatherEditText.getText().toString()) <= 0) {
            showToast(R.string.message_weather_interval_not_correct);
            return false;
        }
        return true;
    }

    private void savedParameter() {
        SharedPreferences.Editor editor = getSharedPreferences(SharedPref.name, MODE_PRIVATE).edit();

        editor.putString(SharedPref.TITLE, titleEditText.getText().toString());

        editor.putInt(SharedPref.ITEM_NUM, Integer.parseInt(itemNumSpinner.getSelectedItem()
                .toString()));

        editor.putInt(SharedPref.BUS_INFO_SCROLL_INTERVAL, Integer.parseInt(busInfoScrollIntervalSpinner
                .getSelectedItem().toString()));

        editor.putInt(SharedPref.BUS_INFO_TEXT_SIZE, Integer.parseInt(busInfoItemTextSizeSpinner
                .getSelectedItem().toString()));

        editor.putInt(SharedPref.LINE_INTERVAL, Integer.parseInt(lineIntervalEditText.getText().toString()));

        editor.putInt(SharedPref.TITLE_TEXT_SIZE, Integer.parseInt(titleTextSizeSpinner
                .getSelectedItem().toString()));

        editor.putString(SharedPref.AD, adEditText.getText().toString());
        editor.putString(SharedPref.DEVICE_ID, (deviceIdEditText.getText().toString()));
        editor.putString(SharedPref.SERVER_ADDRESS, serverEditText.getText().toString());
        editor.putInt(SharedPref.SERVER_PORT, Integer.parseInt(serverPortEditText.getText()
                .toString()));
        editor.putInt(SharedPref.WEATHER_INTERVAL, Integer.parseInt(weatherEditText.getText().toString()));
        editor.apply();
    }

}
