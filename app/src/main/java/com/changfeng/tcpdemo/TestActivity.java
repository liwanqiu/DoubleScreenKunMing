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

    private String deviceId;//设备ID

    private TextView textView;

    private SocketClient socketClient;
    private SocketClient.SocketDelegate socketDelegate = new SocketClient.SocketDelegate() {
        @Override
        public void onConnected(SocketClient client) {
            Log.i(TAG, "onConnected: " + client.getRemoteIP() + " " + client.getRemotePort());
            addDebugInfo(getString(R.string.server_connected, client.getRemoteIP(), client.getRemotePort()));
            client.send(generateLoginData());
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
            addDebugInfo(dataText);

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
        int serverPort = preferences.getInt(SharedPref.SERVER_PORT, Constants.DEFAULT_SERVER_PORT);
        deviceId = preferences.getString(SharedPref.DEVICE_ID,
                Constants.DEFAULT_DEVICE_ID);


        socketClient = new SocketClient(serverAddress, serverPort);
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
        Log.i(TAG, "dataTosend函数中的设备ID：" + deviceId);
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
        Log.i(TAG, "sendTodata中时间戳：" + timeStamp);

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
