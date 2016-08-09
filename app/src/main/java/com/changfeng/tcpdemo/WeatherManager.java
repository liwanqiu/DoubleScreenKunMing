package com.changfeng.tcpdemo;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.apistore.sdk.ApiCallBack;
import com.baidu.apistore.sdk.ApiStoreSDK;
import com.baidu.apistore.sdk.network.Parameters;
import com.changfeng.tcpdemo.bean.Weather;
import com.changfeng.tcpdemo.interf.OnWeatherChangedListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by chang on 2016/7/27.
 */
public class WeatherManager {
    public static final String TAG = "WeatherManager";
    private static WeatherManager ourInstance = new WeatherManager();

    private Context context;

    public static WeatherManager getInstance(Context context) {
        ourInstance.context = context;
        return ourInstance;
    }

    private List<OnWeatherChangedListener> listeners;

    public void registerWeatherChangeListener(OnWeatherChangedListener listener) {
        if (listeners == null) {
            listeners = new ArrayList<>();
        }
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeWeatherListener(OnWeatherChangedListener listener) {
        if (listeners != null) {
            listeners.remove(listener);
        }
    }

    private String cityName;

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityName() {
        return cityName;
    }

    private WeatherManager() {
    }

    private Timer timer;
    private TimerTask timerTask;
    private int fetchWeatherInterval = 1; // minute

    public void setFetchWeatherInterval(int fetchWeatherInterval) {
        this.fetchWeatherInterval = fetchWeatherInterval;
    }

    public int getFetchWeatherInterval() {
        return fetchWeatherInterval;
    }

    public void startWeatherFetchingTimer() {
        stopWeatherFetchingTimer();
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                getWeather(cityName);
            }
        };
        timer.schedule(timerTask, 500, fetchWeatherInterval * 60 * 1000);
    }

    public void stopWeatherFetchingTimer() {
        if (timer != null) {
            timer.cancel();
        }
        if (timerTask != null) {
            timerTask.cancel();
        }
    }

    private Gson gson = new GsonBuilder().serializeNulls().create();

    private void getWeather(String cityName) {
        if (TextUtils.isEmpty(cityName)) {
            return;
        }
        Parameters para = new Parameters();
        para.put("city", cityName);
        ApiStoreSDK.execute("http://apis.baidu.com/heweather/weather/free",
                ApiStoreSDK.GET,
                para,
                new ApiCallBack() {
                    @Override
                    public void onSuccess(int status, String responseString) {
                        Log.i("sdkdemo", "onSuccess() " + responseString);
//                        mTextView.setText(responseString);
                        Weather weather = gson.fromJson(responseString.replace("HeWeather data service 3.0", "weather"), Weather.class);
//                        Log.i(TAG, "onSuccess: " + gson.toJson(weather));
                        if (weather != null && listeners != null) {
                            try {
                                Weather.WeatherBean w = weather.getWeather().get(0);
                                Weather.WeatherBean.AqiBean aqi = w.getAqi();
                                Weather.WeatherBean.BasicBean basic = w.getBasic();
                                Weather.WeatherBean.DailyForecastBean.CondBean cond = w.getDaily_forecast().get(0).getCond();
                                Weather.WeatherBean.DailyForecastBean.TmpBean tmp = w.getDaily_forecast().get(0).getTmp();
                                Weather.WeatherBean.DailyForecastBean.WindBean wind = w.getDaily_forecast().get(0).getWind();

                                for (OnWeatherChangedListener listener : listeners) {
                                    listener.onCityChanged(basic.getCity());
                                    listener.onQualityChanged(aqi.getCity().getQlty());
                                    listener.onPm25Changed(aqi.getCity().getPm25());
                                    listener.onPm10Changed(aqi.getCity().getPm10());
                                    listener.onCondChanged(cond.getTxt_d(), cond.getTxt_n());
                                    listener.onMaxTemperatureChanged(tmp.getMax());
                                    listener.onMinTemperatureChanged(tmp.getMin());
                                    listener.onWindChanged(wind.getDir() + " " + wind.getSc());
                                    listener.onSuggestion(((OnWeatherSuggestListener) context).onSuggest(cond.getTxt_d(), cond.getTxt_n()));
                                }

                            } catch (Exception e) {
                                Log.e(TAG, "onSuccess: ", e);
                            }

                        }
                    }

                    @Override
                    public void onComplete() {
                        Log.i("sdkdemo", "onComplete");
                    }

                    @Override
                    public void onError(int status, String responseString, Exception e) {
                        Log.i("sdkdemo", "onError, status: " + status);
                        Log.i("sdkdemo", "errMsg: " + (e == null ? "" : e.getMessage()));
//                        mTextView.setText(getStackTrace(e));
                    }

                });

    }


}
