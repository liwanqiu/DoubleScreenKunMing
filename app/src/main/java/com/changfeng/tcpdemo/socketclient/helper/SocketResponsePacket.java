package com.changfeng.tcpdemo.socketclient.helper;

import java.util.Arrays;

/**
 * SocketResponsePacket
 * AndroidSocketClient <com.vilyever.socketclient>
 * Created by vilyever on 2016/4/11.
 * Feature:
 */
//服务器返回的数据，以包的形式
public class SocketResponsePacket {
    final SocketResponsePacket self = this;

    
    /* Constructors */
    public SocketResponsePacket(byte[] data, String message) {
        this.data = data;
        this.message = message;
    }

    
    /* Public Methods */
    public boolean isMatch(String message) {
        if (getMessage() == null) {
            return false;
        }
        return getMessage().equals(message);
    }

    //判断是否匹配
    public boolean isMatch(byte[] bytes) {
        return Arrays.equals(getData(), bytes);
    }

    /* Properties */
    /**
     * bytes data
     */
    private final byte[] data;
    public byte[] getData() {
        return this.data;
    }

    private final String message;
    public String getMessage() {
        return this.message;
    }


    /* Overrides */
     
     
    /* Delegates */
     
     
    /* Private Methods */
    
}