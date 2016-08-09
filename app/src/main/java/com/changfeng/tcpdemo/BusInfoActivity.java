package com.changfeng.tcpdemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.os.EnvironmentCompat;
import android.util.Log;
import android.view.textservice.TextInfo;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.changfeng.tcpdemo.bean.Suggestion;
import com.changfeng.tcpdemo.interf.OnWeatherChangedListener;
import com.changfeng.tcpdemo.socketclient.helper.SocketResponsePacket;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chang on 2016/7/25.
 */
public class BusInfoActivity extends BaseActivity implements OnWeatherSuggestListener {
    public static final String TAG = "BusInfoActivity";

    @BindView(R.id.text_view_city)
    TextView cityTextView;
    @BindView(R.id.text_view_quality)
    TextView qualityTextView;
    @BindView(R.id.text_view_pm25)
    TextView pm25TextView;
    @BindView(R.id.text_view_pm10)
    TextView pm10TextView;
    @BindView(R.id.text_view_cond)
    TextView condTextView;
    @BindView(R.id.text_view_max_temperature)
    TextView maxTemperatureTextView;
    @BindView(R.id.text_view_min_temperature)
    TextView minTemperatureTextView;
    @BindView(R.id.text_view_wind)
    TextView windTextView;
    @BindView(R.id.text_view_suggestion)
    TextView suggestionTextView;

    private Gson gson = new GsonBuilder().serializeNulls().create();
    private Suggestion weatherSuggestion;


    // 是否使用模拟数据
    public static final String EMULATE = "bus_info_activity_emulate";


    @BindView(R.id.layout_bus_info)
    LinearLayout busInfoLayout;

    private BusInfoView busInfoView;

    @BindView(R.id.text_view_title)
    TextView titleTextView;
    @BindView(R.id.text_view_time)
    TextView timeTextView;
    @BindView(R.id.text_view_tcp_state)
    TextView tcpStateTextView;

    private int tcpConnectedColor;
    private int tcpDisconnectedColor;
    private int tcpConnectingColor;

    public Timer updateTimeTimer;
    public TimerTask updateTimeTimerTask;

    private String serverAddress;
    private int serverPort;
    private String deviceId;

    private boolean isEmulate = false;
    private Timer emulateTimer;
    private TimerTask emulateTimerTask;
    private List<BusInfo> emulateBusInfoList;


    @Override
    public String onSuggest(String weather) {
        // TODO: 2016/8/9
        if (weatherSuggestion == null) {
            try {
                String suggestionFileName = Environment.getExternalStorageDirectory() + File.separator + Constants.SUGGESTION_FILE_NAME;
                weatherSuggestion = gson.fromJson(FileUtils.getStringFromFile(suggestionFileName), Suggestion.class);
            } catch (Exception e) {
                Log.e(TAG, "onSuggest: ", e);
                return "";
            }
        }

        for (Suggestion.SuggestionBean s : weatherSuggestion.getSuggestion()) {
            if (s.getWeather().equals(weather)) {
                return s.getSuggestion();
            }
        }
        return "";
    }

    private SocketClient socketClient;
    private int reconnectInterval = 10;
    private SocketClient.SocketDelegate socketDelegate = new SocketClient.SocketDelegate() {
        @Override
        public void onConnected(SocketClient client) {
            Log.i(TAG, "onConnected: " + client.getRemoteIP() + " " + client.getRemotePort());
            showToast(R.string.server_connected, client.getRemoteIP(), client.getRemotePort());
            // 登录
            socketClient.send(generateLoginData());
        }

        @Override
        public void onDisconnected(SocketClient client) {
            Log.i(TAG, "onDisconnected: " + client.getRemoteIP() + " " + client.getRemotePort());
            showToast(R.string.server_disconnected, client.getRemoteIP(), client.getRemotePort());
            handler.postDelayed(reconnectRunnable, reconnectInterval * 1000);
        }

        @Override
        public void onResponse(SocketClient client, @NonNull SocketResponsePacket responsePacket) {
            final byte[] data = responsePacket.getData();
            String dataText = toHex(data);
            Log.i(TAG, "onResponse: " + dataText);
            parser.read(data);
        }
    };

    Runnable reconnectRunnable = new Runnable() {
        @Override
        public void run() {
            connect();
        }
    };

    private void connect() {
        Log.i(TAG, "connect: ");
        showToast(R.string.server_connecting, serverAddress, serverPort);
        socketClient.connect();
    }


    private BusInfoParser parser;

    private OnWeatherChangedListener weatherChangedListener = new OnWeatherChangedListener() {

        @Override
        public void onCityChanged(String city) {
            Log.i(TAG, "onCityChanged: " + city);
            cityTextView.setText(city);
        }

        @Override
        public void onQualityChanged(String quality) {
            Log.i(TAG, "onQualityChanged: " + quality);
            qualityTextView.setText(getString(R.string.air_quality, quality));
        }

        @Override
        public void onPm25Changed(String pm25) {
            Log.i(TAG, "onPm25Changed: " + pm25);
            pm25TextView.setText(getString(R.string.pm_25, pm25));
        }

        @Override
        public void onPm10Changed(String pm10) {
            Log.i(TAG, "onPm10Changed: " + pm10);
            pm10TextView.setText(getString(R.string.pm_10, pm10));
        }

        @Override
        public void onCondChanged(String day, String night) {
            Log.i(TAG, "onCondChanged: " + day + " " + night);
            condTextView.setText(getString(R.string.cond, day, night));
        }

        @Override
        public void onMaxTemperatureChanged(String t) {
            Log.i(TAG, "onMaxTemperatureChanged: " + t);
            maxTemperatureTextView.setText(getString(R.string.max_temperature, t));

        }

        @Override
        public void onMinTemperatureChanged(String t) {
            Log.i(TAG, "onMinTemperatureChanged: " + t);
            minTemperatureTextView.setText(getString(R.string.min_temperature, t));
        }

        @Override
        public void onWindChanged(String wind) {
            Log.i(TAG, "onWindChanged: " + wind);
            windTextView.setText(getString(R.string.wind, wind));
        }

        @Override
        public void onSuggestion(String suggestion) {
            Log.i(TAG, "onSuggestion: " + suggestion);


            suggestionTextView.setText(suggestion);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_info);
        ButterKnife.bind(this);


        SharedPreferences preferences = getSharedPreferences(SharedPref.name, MODE_PRIVATE);
        String title = preferences.getString(SharedPref.TITLE, Constants.DEFAULT_TITLE);

        serverAddress = preferences.getString(SharedPref.SERVER_ADDRESS, Constants.DEFAULT_SERVER_ADDRESS);
        serverPort = preferences.getInt(SharedPref.SERVER_PORT, Constants.DEFAULT_SERVER_PORT);
        int titleTextSize = preferences.getInt(SharedPref.TITLE_TEXT_SIZE, Constants.DEFAULT_TITLE_TEXT_SIZE);

        deviceId = preferences.getString(SharedPref.DEVICE_ID,
                Constants.DEFAULT_DEVICE_ID);
        int fetchWeatherInterval = preferences.getInt(SharedPref.WEATHER_INTERVAL, Constants.DEFAULT_WEATHER_INTERVAL);

        titleTextView.setText(title);
        titleTextView.setTextSize(titleTextSize);
        timeTextView.setTextSize(titleTextSize);
        busInfoView = new BusInfoView(this, busInfoLayout
        );

        int weatherTextSize = titleTextSize / 2;
        cityTextView.setTextSize(weatherTextSize);
        qualityTextView.setTextSize(weatherTextSize);
        pm10TextView.setTextSize(weatherTextSize);
        pm25TextView.setTextSize(weatherTextSize);
        condTextView.setTextSize(weatherTextSize);
        maxTemperatureTextView.setTextSize(weatherTextSize);
        minTemperatureTextView.setTextSize(weatherTextSize);
        windTextView.setTextSize(weatherTextSize);
        suggestionTextView.setTextSize(weatherTextSize);

        tcpConnectedColor = ContextCompat.getColor(this, R.color.tcp_connected);
        tcpConnectingColor = ContextCompat.getColor(this, R.color.tcp_connecting);
        tcpDisconnectedColor = ContextCompat.getColor(this, R.color.tcp_disconnected);
        tcpStateTextView.setBackgroundColor(tcpDisconnectedColor);

        socketClient = new SocketClient(serverAddress, serverPort);
        socketClient.getHeartBeatHelper().setRemoteNoReplyAliveTimeout(180 * 1000);

        parser = new BusInfoParser(this);
        parser.setOnBusInfoListener(new BusInfoParser.OnBusInfoListener() {
            @Override
            public void onReceived(BusInfo busInfo) {
                Log.i(TAG, "onReceived: bus info:" + busInfo.toString());
                // 屏蔽测试数据
                if (!busInfo.getBusCustomiseNum().isEmpty()) {
                    busInfoView.addBusInfo(busInfo);
                }


            }
        });


        Intent intent = getIntent();
        if (intent.hasExtra(EMULATE)) {
            try {
                isEmulate = intent.getBooleanExtra(EMULATE, false);
            } catch (Exception e) {
                Log.e(TAG, "onCreate: ", e);
            }
        }

        if (isEmulate) {
            emulateBusInfoList = generateBusInfoList();
        }

        cityTextView.setText(getString(R.string.city));
        WeatherManager.getInstance(this).registerWeatherChangeListener(weatherChangedListener);
        WeatherManager.getInstance(this).setCityName(getString(R.string.city_en));
        WeatherManager.getInstance(this).setFetchWeatherInterval(fetchWeatherInterval);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WeatherManager.getInstance(this).removeWeatherListener(weatherChangedListener);
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "onPause: ");
        super.onPause();

        stopTimer();
        if (isEmulate) {
            stopEmulateTimer();
        }
        handler.removeCallbacks(reconnectRunnable);
        if (socketClient != null) {
            socketClient.removeSocketDelegate(socketDelegate);
            socketClient.disconnect();
        }
        parser.stop();

        WeatherManager.getInstance(this).stopWeatherFetchingTimer();
    }


    @Override
    protected void onResume() {
        Log.i(TAG, "onResume: ");
        super.onResume();
        startTimer();
        if (isEmulate) {
            startEmulateTimer();
        }

//        parser.start();
        if (!isEmulate) {
            socketClient.registerSocketDelegate(socketDelegate);
            connect();
        }

        WeatherManager.getInstance(this).startWeatherFetchingTimer();
    }

    public void startTimer() {
        stopTimer();
        updateTimeTimer = new Timer();
        updateTimeTimerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        timeTextView.setText(TimeUtil.dateFormat.format(new Date()));
                        if (socketClient != null) {
                            if (socketClient.isConnected()) {
                                tcpStateTextView.setBackgroundColor(tcpConnectedColor);
                            } else if (socketClient.isConnecting()) {
                                tcpStateTextView.setBackgroundColor(tcpConnectingColor);
                            } else if (socketClient.isDisconnected()) {
                                tcpStateTextView.setBackgroundColor(tcpDisconnectedColor);
                            }
                        }
                    }
                });
            }
        };
        updateTimeTimer.schedule(updateTimeTimerTask, 500, 500);
    }

    public void stopTimer() {
        if (updateTimeTimer != null) {
            updateTimeTimer.cancel();
        }
        if (updateTimeTimerTask != null) {
            updateTimeTimerTask.cancel();
        }
    }

    private int currentEmulatePosition = 0;

    public void startEmulateTimer() {
        stopEmulateTimer();
        currentEmulatePosition = 0;
        emulateTimer = new Timer();
        emulateTimerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        busInfoView.addBusInfo(emulateBusInfoList.get(currentEmulatePosition));
                        currentEmulatePosition++;
                        if (currentEmulatePosition >= emulateBusInfoList.size()) {
                            currentEmulatePosition = 0;
                        }
                    }
                });
            }
        };
        emulateTimer.schedule(emulateTimerTask, 100, 200);
    }

    public void stopEmulateTimer() {
        if (emulateTimer != null) {
            emulateTimer.cancel();
        }
        if (emulateTimerTask != null) {
            emulateTimerTask.cancel();
        }
    }

    private List<BusInfo> generateBusInfoList() {
        List<BusInfo> l = new ArrayList<>();
        l.add(new BusInfo(1469084160000L, "114路", "5759"));
        l.add(new BusInfo(1469082960000L, "84路", "0122"));
        l.add(new BusInfo(1469083080000L, "74路", "5431"));
        l.add(new BusInfo(1469083440000L, "84路", "7389"));
        l.add(new BusInfo(1469084160000L, "114路", "5760"));
        l.add(new BusInfo(1469083200000L, "84路", "7385"));
        l.add(new BusInfo(1469084160000L, "114路", "5125"));
        l.add(new BusInfo(1469083440000L, "74路", "5426"));
        l.add(new BusInfo(1469084160000L, "114路", "5126"));
        l.add(new BusInfo(1469084160000L, "114路", "5061"));

        return l;
    }

    private byte[] generateLoginData() {

        byte[] outputByte = new byte[38];
        //1.固定开始部分
        outputByte[0] = 0x7E;
        //总长度
        outputByte[1] = 0x00;
        outputByte[2] = 0x26;
        outputByte[3] = 0x01;
        //消息帧类型
        outputByte[4] = 0x01;
        //包头段长度
        outputByte[5] = 0x00;
        outputByte[6] = 0x15;
        //2.包头段
        //原类型
        outputByte[7] = 0x02;
        outputByte[8] = 0x03;
        outputByte[9] = 0x0E;
        //身份标识，原地址
        outputByte[10] = 0x03;
        outputByte[11] = 0x01;
        int id = Integer.parseInt(deviceId.trim());
        byte firstByteID = (byte) ((id >> 24) & 0xFF);
        byte secondByteID = (byte) ((id >> 16) & 0xFF);
        byte thirdByteID = (byte) ((id >> 8) & 0xFF);
        byte fourByteID = (byte) (id & 0xFF);
        outputByte[12] = firstByteID;
        outputByte[13] = secondByteID;
        outputByte[14] = thirdByteID;
        outputByte[15] = fourByteID;

        outputByte[16] = 0x07;
        outputByte[17] = 0x03;
        outputByte[18] = 0x04;
        //回应字段
        outputByte[19] = 0x06;
        outputByte[20] = 0x03;
        outputByte[21] = 0x04;
        //时间戳
        outputByte[22] = 0x09;
        outputByte[23] = 0x01;
        long timeStamp = System.currentTimeMillis() / 1000;

        byte firstByte = (byte) ((timeStamp >> 24) & 0xFF);
        byte secondByte = (byte) ((timeStamp >> 16) & 0xFF);
        byte thirdByte = (byte) ((timeStamp >> 8) & 0xFF);
        byte fourByte = (byte) (timeStamp & 0xFF);

        outputByte[24] = firstByte;
        outputByte[25] = secondByte;
        outputByte[26] = thirdByte;
        outputByte[27] = fourByte;
        //3.包体段
        //身份标识
        outputByte[28] = 0x02;
        outputByte[29] = 0x01;
        outputByte[30] = firstByteID;
        outputByte[31] = secondByteID;
        outputByte[32] = thirdByteID;
        outputByte[33] = fourByteID;
        //登录原因
        outputByte[34] = 0x03;
        outputByte[35] = 0x03;
        outputByte[36] = 0x02;
        //4.包尾
        outputByte[37] = 0x7F;
        return outputByte;

    }
}
