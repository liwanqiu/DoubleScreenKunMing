package com.changfeng.tcpdemo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Interpolator;
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
public class PreviewActivity extends Activity {
    private static final String TAG = "PreviewActivity";
    private Intent previewParmeterIntent;

    //下面是使用JAVA代码动态实现布局
    private LinearLayout  previewItemLayout;
    private TextView  currentTimeText,companyInfo;
    private LinkedList<CustomItemView>  itemViewsList;
    private final  float WEIGHT  =  2.0f;
    private int colorSelect;
    private TimerTask updateTimeTask;
    private Timer  updateTimeTimer;
    private String currentTime;

    private String advertiseContent;//广告内容
    private  int  adFontSize;//广告字体大小
    private  int itemNum ;//每屏幕item个数
    private int busInfoFontSize;//发车信息字体大小
    private int companyInfoFontSize ; //公司信息字体大小

      @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setBackgroundDrawableResource(R.color.mainActivityBg);


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
                  currentTime = startBus.format(new Date(System.currentTimeMillis() ));
                  runOnUiThread(new Runnable() {
                      @Override
                      public void run() {
                          currentTimeText.setText(currentTime);
                      }
                  });
              }
          };
          updateTimeTimer.schedule(updateTimeTask,500,500);
    }
    private  void initValues(){
        if (previewParmeterIntent == null){
            previewParmeterIntent = getIntent();
        }
        itemViewsList = new LinkedList<>();
        colorSelect = 0;


        SharedPreferences preferences = getSharedPreferences
                (getString(R.string.parameterSaved), MODE_PRIVATE);


//        //获取到传递过来的设置参数
//        itemNum = Integer.parseInt(previewParmeterIntent.getStringExtra(getString( R.string
//                .item_number)));
//        busInfoFontSize = Integer.parseInt(previewParmeterIntent.getStringExtra(getString(R
//                .string.bus_info_font_size)));
//        advertiseContent = previewParmeterIntent.getStringExtra(getString(R.string.ad_content));
//        adFontSize = Integer.parseInt(previewParmeterIntent.getStringExtra(getString(R.string
//                .ad_font_size)));
//        companyInfoFontSize = Integer.parseInt(previewParmeterIntent.getStringExtra(getString(R
//                .string.time_font_size)));


        itemNum = preferences.getInt(getString(R.string.item_number), Constants.ITEM_NUM);
        busInfoFontSize = preferences.getInt(getString(R.string.bus_info_font_size), Constants.BUS_INFO_FONT_SIZE);
        advertiseContent = preferences.getString(getString(R.string.ad_content), Constants.AD_CONTENT);
        adFontSize = preferences.getInt(getString(R.string.ad_font_size), Constants.AD_FONT_SIZE);
        companyInfoFontSize = preferences.getInt(getString(R.string.time_font_size), Constants.TIME_FONT_SIZE);


        Log.i(TAG,"initValues函数执行！   传递过来的参数：" +"每屏个数"+  itemNum +" 车辆信息显示字体大小 " +
                busInfoFontSize + "" +
                " " +
                "广告内容：  " +
                advertiseContent+ "" + " 广告字体大小：" +      adFontSize);


    }
    private void initMainLayout() {
        previewItemLayout = (LinearLayout) findViewById(R.id.preview_item_layout_ID);
        currentTimeText = (TextView)findViewById(R.id.currentTime_ID);
        companyInfo = (TextView)findViewById(R.id.companyInfomation_ID);
//        mainLayout.setWeightSum(itemNum*WEIGHT + WEIGHT/2);
    }
    private void addItem() {
        //设置每个item的布局参数
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams .MATCH_PARENT, 0);
        lp.weight= WEIGHT;
        //初始化item的view
        CustomItemView newCustomItem = new CustomItemView(PreviewActivity.this);
        if(colorSelect%2 == 0 ){
            newCustomItem.setBackgroundColor(ContextCompat.getColor(PreviewActivity.this,R.color
                    .itemBgColor));
            newCustomItem.setNormalFontColor(ContextCompat.getColor(PreviewActivity.this,R.color
                    .itemFontColor));
        }else{
            newCustomItem.setBackgroundColor(ContextCompat.getColor(PreviewActivity.this,R.color
                    .itemAnotherBgColor));
            newCustomItem.setNormalFontColor(ContextCompat.getColor(PreviewActivity.this,R.color
                    .itemAnotherFontColor));
        }
        colorSelect++;
       itemViewsList.add(newCustomItem);
        previewItemLayout.addView(newCustomItem,lp);
    }
    private void initUI(){
        Log.i(TAG,"initUI开始执行");
        Log.i(TAG,"itemNUM:" + itemNum);
            for (int i =0 ; i < itemNum;i++){
                addItem();
            }
    }
    private  void initUIContent(){
         String  busLine = "61路";
        String  busNum = "4307";
        String  time  = "15:30";
        String  adContent = "助人为乐，服务大家！";
        companyInfo.setTextSize(companyInfoFontSize);
        currentTimeText.setTextSize(companyInfoFontSize*0.7f);
        if (itemNum >=2){
            for (int i=0;i< itemNum;i++){
                CustomItemView  view = itemViewsList.get(i);
                Log.i(TAG,"itemList个数：" + itemViewsList.size());
                if (i == itemNum-1){
                    if (  advertiseContent.equals("")){
                        view.setAdvertise(adContent);
                        view.setAdvertiseFont(adFontSize);
//                        view.setBackgroundColor(ContextCompat.getColor(PreviewActivity.this,R
//                                .color.itemBgColor));
//                        view.setNormalFontColor(ContextCompat.getColor(PreviewActivity.this,R
//                                .color.itemFontColor));
                    }else{
                        Log.i(TAG,"item的最后一个显示广告，广告内容：" + advertiseContent + "  " + adFontSize);
                        view.setAdvertise(advertiseContent);
                        view.setAdvertiseFont(adFontSize);

                    }
                }else {
                    view.setNormalText(busLine,busNum,time);
                    view.setNormalFontSize(busInfoFontSize);
                }

            }
        }else{
            Log.i(TAG,"item个数为一个，显示广告" + busInfoFontSize  + "  " + busLine + busNum +time);
            itemViewsList.getFirst().setNormalFontSize(busInfoFontSize);
            itemViewsList.getFirst().setNormalText(busLine,busNum,time);
//            itemViewsList.getFirst().setNormalFontColor(ContextCompat.getColor(PreviewActivity
//                    .this,R.color.itemFontColor));
//            itemViewsList.getFirst().setBackgroundColor(ContextCompat.getColor(PreviewActivity
//                    .this,R.color.itemBgColor));
        }
    }
    @Override
    protected void onResume(){
        super.onResume();
    }
    @Override
    protected void onStop(){
        super.onStop();
        Log.i(TAG," onstop函数执行");
    }
    @Override
    protected  void onPause(){
        super.onPause();
        Log.i(TAG," onPause函数执行");
        previewParmeterIntent = null;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"onDestroy执行");

    }
}
