package com.changfeng.tcpdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.SystemClock;
import android.util.Log;

import java.io.File;

/**关于该类：
 * 创建时间：/2016/7/19  14:10
 * @author : 似水流年
 * @info : 所属包名：com.changfeng.tcpdemo
 * @description :
 *
 */

public class AutoUpdateApp extends BroadcastReceiver{
    private static  final  String  TAG = "AutoUpdateApp自动更新APP类：";
    private  static  final  String  APK_FILE_NAME = "/kunMing.apk";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }
        Log.i(TAG, intent.getDataString() + "   " + intent.getAction());

        if (intent.getAction().equalsIgnoreCase(Intent.ACTION_MEDIA_MOUNTED)) {
            String str = intent.getDataString();
            //获取路径
//            String  storagePath = intent.getData().getPath();
                        String storagePath = str.substring(str.indexOf(":"), str.length()).replace("///", "/");


            Log.e("获取到的路径：" , storagePath);
//            String  apkFilePath =  Environment.getExternalStorageDirectory() + "/" + context
//                    .getPackageName() + "/" +
//                    APK_FILE_NAME;

            String  apkFilePath = storagePath +APK_FILE_NAME;
            if (SystemClock.elapsedRealtime() < 1000 * 50) {
                return;
            }
            Intent installApk = new Intent();
            installApk.setAction(Intent.ACTION_VIEW);
            installApk.setDataAndType(Uri.fromFile(new File(apkFilePath)),"application/and.android.package-archive");
            context.startActivity(installApk);



        }
    }


}
