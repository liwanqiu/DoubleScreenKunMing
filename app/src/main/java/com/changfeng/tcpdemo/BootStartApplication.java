package com.changfeng.tcpdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
/**
 * Created by ${似水流年} on 2016/7/7.
 */
public class BootStartApplication extends BroadcastReceiver{
      static final String ACTION = "android.intent.action.BOOT_COMPLETED";
    @Override
    public void onReceive(Context context, Intent  intent){
        if (intent.getAction().equals(ACTION)){
            Intent  myIntent = new Intent(context,InitActivity.class);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(myIntent);
        }
    }

}
