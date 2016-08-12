package com.changfeng.tcpdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

/**
 * Created by chang on 2016/8/9.
 */
public class MountedReceiver extends BroadcastReceiver {

    private static final String TAG = "MountedReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive: " + intent.getDataString() + " " + intent.getAction());

        int index = intent.getDataString().indexOf("///");
        String mountPath = intent.getDataString().substring(index + 2);
        if (intent.getAction().equals(Intent.ACTION_MEDIA_MOUNTED)) {
            if (getElapsedTime() < 50000) {
                return;
            }
            String src = mountPath + File.separator + Constants.SUGGESTION_FILE_NAME;

            if (!new File(src).exists()) {
                return;
            }

            String dst = Environment.getExternalStorageDirectory() + File.separator + Constants.SUGGESTION_FILE_NAME;
            try {
                FileUtils.copyFileUsingFileChannels(new File(src), new File(dst));
                Toast.makeText(context, context.getString(R.string.message_copy_file_success), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Log.e(TAG, "onReceive: ", e);
                Toast.makeText(context, context.getString(R.string.message_copy_file_failed, src), Toast.LENGTH_LONG).show();
            }
        }
    }

    private long getElapsedTime() {
        long ut = SystemClock.elapsedRealtime();
        if (ut == 0) {
            ut = 1;
        }
        return ut;
    }
}
