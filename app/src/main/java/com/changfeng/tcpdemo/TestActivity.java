package com.changfeng.tcpdemo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.TextView;

import com.changfeng.tcpdemo.socketclient.helper.SocketResponsePacket;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by chang on 2016/7/25.
 */
public class TestActivity extends BaseActivity {
    private static final String TAG = "TestActivity";
    private List<String> infoList = new ArrayList<>();

    private BusInfoParser parser;

    private int reconnectInterval = 30;

    private TextView textView;

    private SocketClient socketClient;
    private SocketClient.SocketDelegate socketDelegate = new SocketClient.SocketDelegate() {
        @Override
        public void onConnected(SocketClient client) {
            Log.i(TAG, "onConnected: " + client.getRemoteIP() + " " + client.getRemotePort());
            addDebugInfo(getString(R.string.server_connected, client.getRemoteIP(), client.getRemotePort()));

        }

        @Override
        public void onDisconnected(SocketClient client) {
            Log.i(TAG, "onDisconnected: " + client.getRemoteIP() + " " + client.getRemotePort());
            addDebugInfo(getString(R.string.server_disconnected, client.getRemoteIP(), client.getRemotePort()));
            handler.postDelayed(reconnectRunnable, reconnectInterval * 1000);
        }

        @Override
        public void onResponse(SocketClient client, @NonNull SocketResponsePacket responsePacket) {
            byte[] data = responsePacket.getData();
            String dataText = toHex(data);
            Log.i(TAG, "onResponse: " + dataText);
//            addDebugInfo(dataText);

            parser.read(data);


        }
    };

    Runnable reconnectRunnable = new Runnable() {
        @Override
        public void run() {
            connect();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        textView = (TextView) findViewById(R.id.test_text_view);

        SharedPreferences preferences = getSharedPreferences(SharedPref.name, MODE_PRIVATE);
        String serverAddress = preferences.getString(SharedPref.SERVER_ADDRESS, Constants.DEFAULT_SERVER_ADDRESS);
        int port = preferences.getInt(SharedPref.SERVER_PORT, Constants.DEFAULT_SERVER_PORT);

        socketClient = new SocketClient(serverAddress, port);
        socketClient.getHeartBeatHelper().setRemoteNoReplyAliveTimeout(60 * 1000);

        parser = new BusInfoParser(this);
        parser.setOnBusInfoListener(new BusInfoParser.OnBusInfoListener() {
            @Override
            public void onReceived(BusInfo busInfo) {
                Log.i(TAG, "onReceived: bus info:" + busInfo.toString());
                addDebugInfo(busInfo.toHumanString());
            }
        });
    }

    private void connect() {
        Log.i(TAG, "connect: ");
        socketClient.connect();
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "onPause: ");
        super.onPause();
        handler.removeCallbacks(reconnectRunnable);
        socketClient.removeSocketDelegate(socketDelegate);
        socketClient.disconnect();
    }


    @Override
    protected void onResume() {
        Log.i(TAG, "onResume: ");
        super.onResume();
        socketClient.registerSocketDelegate(socketDelegate);
        connect();
    }

    public void addDebugInfo(String info) {
        String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
        infoList.add(time + " " + info);
        StringBuilder stringBuilder = new StringBuilder();
        for (String text : infoList) {
            stringBuilder.append(text).append('\n');
        }
        textView.setText(stringBuilder.toString());
    }

}
