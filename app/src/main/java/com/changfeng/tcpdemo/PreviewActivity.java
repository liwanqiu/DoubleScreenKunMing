package com.changfeng.tcpdemo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ${似水流年} on 2016/6/30.
 */
public class PreviewActivity extends BaseActivity {
    private static final String TAG = "PreviewActivity";
//    private Intent previewParmeterIntent;

    //下面是使用JAVA代码动态实现布局
    private LinearLayout previewItemLayout;
    private TextView currentTimeText, companyInfo;
    private LinkedList<CustomItemView> itemViewsList;
    private final float WEIGHT = 2.0f;
    private int colorSelect;
    private TimerTask updateTimeTask;
    private Timer updateTimeTimer;
    private String currentTime;

    private String ad;//广告内容
    private int adFontSize;//广告字体大小
    private int itemCount;//每屏幕item个数
    private int busInfoFontSize;//发车信息字体大小
    private int timeFontSize; //公司信息字体大小

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setBackgroundDrawableResource(R.color.bgColor);


        setContentView(R.layout.activity_preview);
        initValues();
        initMainLayout();
        initUI();
        initUIContent();

        updateTimeTimer = new Timer();
        updateTimeTask = new TimerTask() {
            @Override
            public void run() {
                SimpleDateFormat startBus = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                currentTime = startBus.format(new Date(System.currentTimeMillis()));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        currentTimeText.setText(currentTime);
                    }
                });
            }
        };
        updateTimeTimer.schedule(updateTimeTask, 500, 500);
    }

    private void initValues() {
        itemViewsList = new LinkedList<>();
        colorSelect = 0;


        SharedPreferences preferences = getSharedPreferences
                (SharedPref.name, MODE_PRIVATE);

        itemCount = preferences.getInt(SharedPref.ITEM_COUNT, Constants.DEFAULT_ITEM_COUNT);
        busInfoFontSize = preferences.getInt(SharedPref.BUS_INFO_FONT_SIZE, Constants.DEFAULT_BUS_INFO_FONT_SIZE);
        ad = preferences.getString(SharedPref.AD, Constants.DEFAULT_AD);
        adFontSize = preferences.getInt(SharedPref.AD_FONT_SIZE, Constants.DEFAULT_AD_FONT_SIZE);
        timeFontSize = preferences.getInt(SharedPref.TIME_FONT_SIZE, Constants.DEFAULT_TIME_FONT_SIZE);


        Log.i(TAG, "initValues() 每屏个数：" + itemCount + " 车辆信息显示字体大小： " +
                busInfoFontSize +
                " 广告内容：  " +
                ad + " 广告字体大小：" + adFontSize);


    }

    private void initMainLayout() {
        previewItemLayout = (LinearLayout) findViewById(R.id.preview_item_layout_ID);
        currentTimeText = (TextView) findViewById(R.id.text_view_current_time);
        companyInfo = (TextView) findViewById(R.id.companyInfomation_ID);
    }

    private void addItems() {
        //设置每个item的布局参数
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
        lp.weight = WEIGHT;
        int bgColor;
        int fontColor;

        for (int i = 0; i < itemCount; i++) {
            //初始化item的view
            CustomItemView newCustomItem = new CustomItemView(PreviewActivity.this);
            if (i % 2 == 0) {
                bgColor = ContextCompat.getColor(this, R.color.itemBgColor);
                fontColor = ContextCompat.getColor(this, R.color.itemFontColor);
            } else {
                bgColor = ContextCompat.getColor(this, R.color.itemAnotherBgColor);
                fontColor = ContextCompat.getColor(this, R.color.itemAnotherFontColor);
            }
            newCustomItem.setBackgroundColor(bgColor);
            newCustomItem.setNormalFontColor(fontColor);
            itemViewsList.add(newCustomItem);
            previewItemLayout.addView(newCustomItem, lp);
        }
    }

    private void initUI() {
        Log.i(TAG, "initUI开始执行 item num:" + itemCount);
        addItems();
    }

    private void initUIContent() {
        String busLine = "61路";
        String busNum = "4307";
        String time = "15:30";
        String adContent = "助人为乐，服务大家！";
        companyInfo.setTextSize(timeFontSize);
        currentTimeText.setTextSize(timeFontSize * 0.7f);
        if (itemCount >= 2) {
            for (int i = 0; i < itemCount; i++) {
                CustomItemView view = itemViewsList.get(i);
                Log.i(TAG, "itemList个数：" + itemViewsList.size());
                if (i == itemCount - 1) {
                    if (this.ad.equals("")) {
                        view.setAdvertise(adContent);
                        view.setAdvertiseFont(adFontSize);
                    } else {
                        Log.i(TAG, "item的最后一个显示广告，广告内容：" + this.ad + "  " + adFontSize);
                        view.setAdvertise(this.ad);
                        view.setAdvertiseFont(adFontSize);

                    }
                } else {
                    view.setNormalText(busLine, busNum, time);
                    view.setNormalFontSize(busInfoFontSize);
                }

            }
        } else {
            Log.i(TAG, "item个数为一个，显示广告" + busInfoFontSize + "  " + busLine + busNum + time);
            itemViewsList.getFirst().setNormalFontSize(busInfoFontSize);
            itemViewsList.getFirst().setNormalText(busLine, busNum, time);
//            itemViewsList.getFirst().setNormalFontColor(ContextCompat.getColor(PreviewActivity
//                    .this,R.color.itemFontColor));
//            itemViewsList.getFirst().setBackgroundColor(ContextCompat.getColor(PreviewActivity
//                    .this,R.color.itemBgColor));
        }
    }

}
