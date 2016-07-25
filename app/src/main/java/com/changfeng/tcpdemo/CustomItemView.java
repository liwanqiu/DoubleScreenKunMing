package com.changfeng.tcpdemo;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by ${似水流年} on 2016/6/30.
 */
public class CustomItemView extends LinearLayout {
    private static final String TAG = "自定义的CustomItemView类:";
    private String busInfo = null;
    //自定义空间中的2个textview，四个autotext
    private LinearLayout mainInfoLayout;
    private AutoScrollTextView first_auto, second_auto, third_auto, four_auto;
    private TextView text_1, text_2, text_3, text_4;
    private View itemView;
    //默认字体，字体大小
    private static final Typeface typefaceDefault = Typeface.SANS_SERIF;

    public CustomItemView(Context context) {
        this(context, null);
    }

    public CustomItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomItemView(Context context, AttributeSet attrs, int styleAttrs) {
        super(context, attrs, styleAttrs);
        LayoutInflater inflater = LayoutInflater.from(context);
        itemView = inflater.inflate(R.layout.custom_item_view_layout, this);

        mainInfoLayout = (LinearLayout) itemView.findViewById(R.id.mainInfo_layout_ID);
        text_1 = (TextView) itemView.findViewById(R.id.text_1_ID);
        text_2 = (TextView) itemView.findViewById(R.id.text_2_ID);
        text_3 = (TextView) itemView.findViewById(R.id.text_3_ID);
        text_4 = (TextView) itemView.findViewById(R.id.text_4_ID);

//        first_auto = (AutoScrollTextView)itemView.findViewById(R.id.first_autoText_ID);
//        second_auto = (AutoScrollTextView)itemView.findViewById(R.id.second_autoText_ID);
//        third_auto = (AutoScrollTextView)itemView.findViewById(R.id.third_autoText_ID);
//        four_auto = (AutoScrollTextView)itemView.findViewById(R.id.four_autoText_ID);

        text_1.setTypeface(CustomItemView.typefaceDefault);
//        text_1.setTextSize(R.dimen.bus_info_font_size);

        text_2.setTypeface(CustomItemView.typefaceDefault);

        text_3.setTypeface(CustomItemView.typefaceDefault);
//        text_3.setTextSize(R.dimen.bus_info_font_size);

        text_4.setTypeface(CustomItemView.typefaceDefault);
        text_4.setTextColor(ContextCompat.getColor(context, R.color.advertiseFontColor));
//        text_4.setTextSize(R.dimen.ad_font_size_default);

//        setNumber(8,5,2,9);
//        setMode(AutoScrollTextView.Mode.UP);
//        setTextColor(ContextCompat.getColor(context,R.color.normalFontCorlor));
//        setTextSize(R.dimen.bus_info_font_size);
    }

    public boolean setBusLine(String line) {
        if (busInfo == null) {
            busInfo = line;
            return true;
        } else {
            return false;
        }
    }

    public String getBusLine() {
        return busInfo;
    }

    /**
     * 以下是主要显示内容的相关设置
     */
//设置将要发车的车辆信息
    public void setWillStartText(String str1, String str2, String str3) {
        text_1.setText(str1);
        text_2.setText(str2);
        text_3.setText(str3);
//        setTargetNumber(str2);
//        mainInfoLayout.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.willStartBgCorlor));
//        text_1.setTextColor(ContextCompat.getColor(getContext(),R.color.willStartFontCorlor));
//        text_3.setTextColor(ContextCompat.getColor(getContext(),R.color.willStartFontCorlor));
//        setTextColor(R.color.willStartFontCorlor);


        if (mainInfoLayout.getVisibility() != View.VISIBLE) {
            text_4.setVisibility(View.GONE);
            mainInfoLayout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 接收到最新数据，相关设置
     */
    public void setLastText(String str1, String str2, String str3) {
        text_1.setText(str1);
        text_2.setText(str2);

        text_3.setText(str3);
//        setTargetNumber(str2);

//        mainInfoLayout.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.lastInputBgColor));
//        text_1.setTextColor(ContextCompat.getColor(getContext(),R.color.lastInputFontColor));
//        text_3.setTextColor(ContextCompat.getColor(getContext(),R.color.lastInputFontColor));
//        setTextColor(R.color.lastInputFontColor);
        if (mainInfoLayout.getVisibility() != View.VISIBLE) {
            text_4.setVisibility(View.GONE);
            mainInfoLayout.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 正常显示车辆信息的相关设置
     */
//显示内容
    public void setNormalText(String str1, String str2, String str3) {
        text_1.setText(str1);
        text_3.setText(str3);
        text_2.setText(str2);

//        setTargetNumber(str2);
//            mainInfoLayout.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.normalBgCorlor));
//            text_1.setTextColor(ContextCompat.getColor(getContext(),R.color.normalFontCorlor));
//            text_3.setTextColor(ContextCompat.getColor(getContext(),R.color.normalFontCorlor));
//            setTextColor(R.color.normalFontCorlor);

        if (mainInfoLayout.getVisibility() != View.VISIBLE) {
            text_4.setVisibility(View.GONE);
            mainInfoLayout.setVisibility(View.VISIBLE);
        }

    }

    //设置字体大小
    public void setNormalFontSize(int fontSize) {

        text_1.setTextSize(fontSize);
        text_3.setTextSize(fontSize);
        text_2.setTextSize(fontSize);


//        setTextSize(fontSize);
//            first_auto.setTextSize(fontSize);
//            second_auto.setTextSize(fontSize);
//            third_auto.setTextSize(fontSize);
//            four_auto.setTextSize(fontSize);
    }

    // 设置字体颜色
    public void setNormalFontColor(int resID) {
        text_1.setTextColor(resID);
        text_3.setTextColor(resID);
        text_2.setTextColor(resID);
//        setTextColor(resID);
    }

    //设置背景颜色
    private void setNormalColor(int resID) {
        itemView.setBackgroundColor(resID);
    }

    /**
     * 以下是关于宣传用语部分的
     */

//设置宣传语
    public void setAdvertise(String advertiseText) {
        text_4.setText(advertiseText);
//            text_4.setTextColor(ContextCompat.getColor(getContext(),R.color.advertiseFontCorlor));
//            text_4.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.advertiseBgCorlor));
        if (text_4.getVisibility() != View.VISIBLE) {
            mainInfoLayout.setVisibility(View.GONE);
            text_4.setVisibility(View.VISIBLE);
        }
    }

    //设置宣传语字体大小
    public void setAdvertiseFont(int fontSize) {

        text_4.setTextSize(fontSize);
    }

    //设置宣传语字体颜色
    private void setAdvertiseFontColor(int resID) {
        text_4.setTextColor(resID);
    }

    //设置宣传语item背景颜色
    private void setAdvertiseBg(int resID) {
        text_4.setBackgroundColor(resID);
    }

    /**
     * 设置车辆编号自动更新的相关参数
     */
    //设置车辆编号自动更新
//设置初始的数字
    public void setNumber(int first, int second, int third, int four) {
        first_auto.setNumber(first);
        second_auto.setNumber(second);
        third_auto.setNumber(third);
        four_auto.setNumber(four);
    }

    //设置动画结束时候的数字
    private void setTargetNumber(String input) {

        char first_char = input.trim().charAt(0);
        char second_char = input.trim().charAt(1);
        char third_char = input.trim().charAt(2);
        char four_char = input.trim().charAt(3);
        Log.i(TAG, "车辆编号分成四位数字后为：" + first_char + "  " + second_char + "   " + third_char + "   "
                + four_char);
        int first_int = Integer.parseInt(String.valueOf(first_char));
        int second_int = Integer.parseInt(String.valueOf(second_char));
        int third_int = Integer.parseInt(String.valueOf(third_char));
        int four_int = Integer.parseInt(String.valueOf(four_char));

        first_auto.setTargetNumber(first_int);
        second_auto.setTargetNumber(second_int);
        third_auto.setTargetNumber(third_int);
        four_auto.setTargetNumber(four_int);

    }

    //设置动画模式
    public void setMode(AutoScrollTextView.Mode mode) {
        first_auto.setMode(mode);
        second_auto.setMode(mode);
        third_auto.setMode(mode);
        four_auto.setMode(mode);
    }

    //设置字体大小
    public void setTextSize(int textSize) {
        first_auto.setTextSize(textSize);
        second_auto.setTextSize(textSize);
        third_auto.setTextSize(textSize);
        four_auto.setTextSize(textSize);
    }

    //设置字体颜色
    public void setTextColor(int color) {
        first_auto.setTextColor(color);
        second_auto.setTextColor(color);
        third_auto.setTextColor(color);
        four_auto.setTextColor(color);
    }

    //设置动画速度
    public void setSpeed(float speed) {
        first_auto.setSpeed(speed);
        second_auto.setSpeed(speed);
        third_auto.setSpeed(speed);
        four_auto.setSpeed(speed);
    }

}
