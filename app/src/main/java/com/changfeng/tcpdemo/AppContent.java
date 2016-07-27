package com.changfeng.tcpdemo;

import android.app.Application;

import com.baidu.apistore.sdk.ApiStoreSDK;

/**
 * Created by chang on 2016/7/27.
 */
public class AppContent extends Application {

    @Override
    public void onCreate() {
        ApiStoreSDK.init(this, "13685222d884690365fc51dea456aac3");
        super.onCreate();
    }
}
