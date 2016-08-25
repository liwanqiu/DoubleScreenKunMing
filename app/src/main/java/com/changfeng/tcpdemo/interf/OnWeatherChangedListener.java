package com.changfeng.tcpdemo.interf;

/**
 * Created by chang on 2016/7/27.
 */
public interface OnWeatherChangedListener {

    void onCityChanged(String city);

    void onQualityChanged(String quality);

    void onPm25Changed(String pm25);

    void onPm10Changed(String pm10);

    void onCondChanged(String day, String night);

    void onTemperatureChanged(String max, String min);

    void onWindChanged(String wind);

    void onWeatherChanged(String weather);

    void onSuggestion(String suggestion);


}
