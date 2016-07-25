package com.changfeng.tcpdemo;

import android.content.Context;
import android.util.Log;

import java.io.UnsupportedEncodingException;

/**
 * Created by chang on 2016/7/25.
 */
public class BusInfoParser {
    private String TAG = "BusInfoParser";

    private Context context;

    private static final byte TYPE_ORDINAL = 0x01;
    private static final byte TYPE_LINE_NUM = 0x02;
    private static final byte TYPE_LINE_NAME = 0x03;
    private static final byte TYPE_SCHEDULES = 0x04;
    private static final byte TYPE_BUS_CUSTOMISE_NUM = 0x05;
    private static final byte TYPE_DEPARTURE_TIME = 0x06;
    private static final byte TYPE_DRIVER_NUM = 0x07;
    private static final byte TYPE_DRIVER_NAME = 0x08;
    private static final byte TYPE_TRIP_TYPE = 0x09;
    private static final byte TYPE_PRE_DEPARTURE_TIME = 0x0A;
    private static final byte TYPE_DEPARTURE_NUM = 0x0B;
    private static final byte TYPE_DISPATCH_NAME = 0x0C;
    private static final byte TYPE_TERMINAL_NAME = 0x0D;
    private static final byte TYPE_SINGLE_TRIP_TIME = 0x0E;


    private static final int MAX_FRAME_LEN = 1464; // 海信协议对整个包长进行限长，整包长度不超过1464字节，大于此限定长度的数据包进行丢弃处理

    private static final int STATE_NO_RECEIVE = 0;
    private static final int STATE_START = 1;
    private static final int STATE_RECEIVING = 2;
    private static final int STATE_RECEIVED = 3;


    private byte[] busInfoBytes = new byte[MAX_FRAME_LEN];

    private int pos;
    private int receiveState;

    private int len;
    private byte lenLow;
    private byte lenHigh;

    public BusInfoParser(Context context) {
        this.context = context;
    }

    private OnBusInfoListener onBusInfoListener;

    public void setOnBusInfoListener(OnBusInfoListener onBusInfoListener) {
        this.onBusInfoListener = onBusInfoListener;
    }

    public void read(byte[] bytes) {
        for (byte data : bytes) {
            processDo(data);
        }
    }

    private void processDo(byte data) {
        pos++;
        if (pos > MAX_FRAME_LEN || receiveState == STATE_NO_RECEIVE) {
            pos = 0;
            receiveState = STATE_NO_RECEIVE;
        }

        switch (receiveState) {
            case STATE_NO_RECEIVE:
                pos = 0;
                if (data == 0x7E) {
                    receiveState = STATE_START;
                    busInfoBytes[pos] = data;
                }
                break;
            case STATE_START:
                busInfoBytes[pos] = data;
                if (pos == 1) {
                    lenHigh = data;
                }
                if (pos == 2) {
                    lenLow = data;
                    len = ((lenHigh) << 8) + lenLow;

                    Log.i(TAG, "processDo: len: " + len);

                    if (len <= MAX_FRAME_LEN) {
                        receiveState = STATE_RECEIVING;
                    } else {
                        receiveState = STATE_NO_RECEIVE;
                    }
                }
                break;
            case STATE_RECEIVING:
                busInfoBytes[pos] = data;
                if (pos + 1 == len && data == 0x7F) {
                    Log.i(TAG, "processDo: pos:" + pos + " len:" + len);
                    parse(busInfoBytes);
                    receiveState = STATE_NO_RECEIVE;
                }
                break;


        }

    }

    private void parse(byte[] data) {
        Log.i(TAG, "parse: " + ((BaseActivity) context).toHex(data));

        int i = 0;

        // 包头标识
        if (data[i] != 0x7E) {
            Log.i(TAG, "parse: frame head not correct:" + String.format("%02x", data[i]));
            return;
        }
        i++;

        // 整包长度
        int len = ((data[i]) << 8) + data[i + 1];
        if (len > MAX_FRAME_LEN) {
            Log.i(TAG, "parse: len not correct:" + len);
            return;
        }
        i += 2;
        // 版本号
        int version = data[i];
        Log.i(TAG, "parse: version:" + String.format("%02x", version));
        i += 1;
        // 判断是否是发车排队信息
        if (data[i] != 0x59) {
            Log.i(TAG, "parse: frame type not correct:" + data[i]);
            return;
        }
        i++;
        // 包头段长度
        int packetHeadLen = ((data[i]) << 8) + data[i + 1];
        i += 2;
        // 跳过包头段
        i += packetHeadLen;

        Log.i(TAG, "parse: packet head len:" + packetHeadLen);

        String lineName = null;
        Long departureTime = null;
        String busCustomiseNum = null;

        while (true) {
            byte type = data[i];
            Log.i(TAG, "parse: type:" + String.format("%02x", type));
            switch (type) {
                case TYPE_ORDINAL:
                    int ordinal = (data[i + 2] << 8) + data[i + 3];
                    Log.i(TAG, "parse: ordinal:" + ordinal);
                    i += 4;
                    break;
                case TYPE_LINE_NUM:
                    int lineNum = (data[i + 2] << 24) + (data[i + 3] << 16) + (data[i + 4] << 8) + data[i + 5];
                    Log.i(TAG, "parse: lineNum:" + lineNum);
                    i += 6;
                    break;
                case TYPE_LINE_NAME:
                    int lineNameLen = data[i + 2];
                    try {
                        lineName = new String(data, i + 3, lineNameLen, "GBK");
                        Log.i(TAG, "parse: line name len:" + lineNameLen + " line name:" + lineName);
                    } catch (UnsupportedEncodingException e) {
                        Log.e(TAG, "parse: ", e);
                    }
                    i += lineNameLen + 3;
                    break;
                case TYPE_SCHEDULES:
                    int schedules = (data[i + 2] << 8) + data[i + 3];
                    Log.i(TAG, "parse: schedules:" + schedules);
                    i += 4;
                    break;
                case TYPE_BUS_CUSTOMISE_NUM:
                    int numLen = data[i + 2];
                    busCustomiseNum = new String(data, i + 3, numLen);
                    Log.i(TAG, "parse: bus customise num:" + busCustomiseNum);
                    i += numLen + 3;
                    break;
                case TYPE_DEPARTURE_TIME:
                    departureTime = (long) ((data[i + 2] << 24) + (data[i + 3] << 16) + (data[i + 4] << 16) + (data[i + 5] << 8) + (data[i + 6]));
                    Log.i(TAG, "parse: departure time:" + departureTime);
                    i += 6;
                    break;
                case TYPE_DRIVER_NUM:
                    int driverNumLen = data[i + 2];
                    String driverNum = new String(data, i + 3, driverNumLen);
                    Log.i(TAG, "parse: driver num:" + driverNum);
                    i += driverNumLen + 2;
                    break;
                case TYPE_DRIVER_NAME:
                    int driverNameLen = data[i + 2];
                    String driverName = new String(data, i + 3, driverNameLen);
                    Log.i(TAG, "parse: driver name:" + driverName);
                    i += driverNameLen + 2;
                    break;
                case TYPE_TRIP_TYPE:
                    byte tripType = data[i + 2];
                    Log.i(TAG, "parse: trip type:" + String.format("%02x", tripType));
                    i += 3;
                    break;
                case TYPE_PRE_DEPARTURE_TIME:
                    byte preDepartureTime = data[i + 2];
                    Log.i(TAG, "parse: pre departure time:" + String.format("%02x", preDepartureTime));
                    i += 3;
                    break;
                case TYPE_DEPARTURE_NUM:
                    int departureNumLen = data[i + 2];
                    String departureNum = new String(data, i + 3, departureNumLen);
                    Log.i(TAG, "parse: departure num:" + departureNum);
                    i += departureNumLen + 2;
                    break;
                case TYPE_DISPATCH_NAME:
                    int dispatchNameLen = data[i + 2];
                    String dispatchName = new String(data, i + 3, dispatchNameLen);
                    Log.i(TAG, "parse: dispatch name:" + dispatchName);
                    i += dispatchNameLen;
                    break;
                case TYPE_TERMINAL_NAME:
                    int terminalNameLen = data[i + 2];
                    String terminalName = new String(data, i + 3, terminalNameLen);
                    Log.i(TAG, "parse: terminalName:" + terminalName);
                    i += terminalNameLen;
                    break;
                case TYPE_SINGLE_TRIP_TIME:
                    int time = (data[i + 2] << 8) + data[i + 3];
                    Log.i(TAG, "parse: single trip time:" + time);
                    i += 4;
                    break;
                default:
                    break;
            }


            // FIXME: 2016/7/25 由于海信的数据问题，司机姓名长度不对，导致无法正确读取后边的数据，并且在读取到车辆自编号，所需数据已经全部读完，所以结束数据读取
            // 存在问题： 如果海信的子包顺序调整，可能导致循环无法退出
            if (lineName != null && departureTime != null && busCustomiseNum != null) {
                Log.i(TAG, "parse: time:" + departureTime + " name:" + lineName + " num:" + busCustomiseNum);
                if (onBusInfoListener != null) {
                    onBusInfoListener.onReceived(new BusInfo(departureTime, lineName, busCustomiseNum));
                }
                break;
            }
        }


    }

    public interface OnBusInfoListener {
        void onReceived(BusInfo busInfo);
    }
}
