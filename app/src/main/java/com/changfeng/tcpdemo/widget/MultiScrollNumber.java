package com.changfeng.tcpdemo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.ColorRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;

import com.changfeng.tcpdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuhaojie on 2016/7/19 20:39.
 */
public class MultiScrollNumber extends LinearLayout {

    private static final String TAG = "MultiScrollNumber";

    private Context mContext;
    private List<Integer> mTargetNumbers = new ArrayList<>();
    private List<Integer> mPrimaryNumbers = new ArrayList<>();
    private List<ScrollNumber> mScrollNumbers = new ArrayList<>();
    private int mTextSize = 130;

    private int lastVal = 0;

    private int[] mTextColors = new int[]{Color.BLUE};
    private Interpolator mInterpolator = new AccelerateDecelerateInterpolator();
    private String mFontFileName;

    public MultiScrollNumber(Context context) {
        this(context, null);
    }

    public MultiScrollNumber(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiScrollNumber(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.MultiScrollNumber);
        int primaryNumber = typedArray.getInteger(R.styleable.MultiScrollNumber_primary_number, 0);
        int targetNumber = typedArray.getInteger(R.styleable.MultiScrollNumber_target_number, 0);
        int numberSize = typedArray.getInteger(R.styleable.MultiScrollNumber_number_size, 130);

        setNumber(primaryNumber, targetNumber);
        setTextSize(numberSize);



        typedArray.recycle();

        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);


    }

    public void setNumber(int val) {
        resetView();

        int number = val;
        while (number > 0) {
            int i = number % 10;
            mTargetNumbers.add(i);
            number /= 10;
        }

        for (int i = mTargetNumbers.size() - 1; i >= 0; i--) {
            ScrollNumber scrollNumber = new ScrollNumber(mContext);
            scrollNumber.setTextColor(mTextColors[i % mTextColors.length]);
            scrollNumber.setTextSize(mTextSize);
            scrollNumber.setInterpolator(mInterpolator);
            if (!TextUtils.isEmpty(mFontFileName))
                scrollNumber.setTextFont(mFontFileName);
            scrollNumber.setNumber(0, mTargetNumbers.get(i), i * 10);
            mScrollNumbers.add(scrollNumber);
            addView(scrollNumber);
        }
        lastVal = val;
    }

    public void setNumber(int val, int count, boolean forceFlag) {
        resetView();
        int number = val;
        while (number > 0) {
            int i = number % 10;
            mTargetNumbers.add(i);
            number /= 10;
        }

        if (forceFlag) {
            while (mTargetNumbers.size() < count) {
                mTargetNumbers.add(0);
            }
        }


        for (int i = mTargetNumbers.size() - 1; i >= 0; i--) {
            ScrollNumber scrollNumber = new ScrollNumber(mContext);
            scrollNumber.setTextColor(mTextColors[i % mTextColors.length]);
            scrollNumber.setTextSize(mTextSize);
            scrollNumber.setInterpolator(mInterpolator);
            if (!TextUtils.isEmpty(mFontFileName))
                scrollNumber.setTextFont(mFontFileName);
            scrollNumber.setNumber(0, mTargetNumbers.get(i), i * 10);
            mScrollNumbers.add(scrollNumber);
            addView(scrollNumber);
        }

        lastVal = val;
    }

    private void resetView() {
        mTargetNumbers.clear();
        mPrimaryNumbers.clear();
    }


    public void setNumber(int from, int to) {
        if (to < from)
            throw new UnsupportedOperationException("'to' value must > 'from' value");

        resetView();
        // operate to
        int number = to, count = 0;
        while (number > 0) {
            int i = number % 10;
            mTargetNumbers.add(i);
            number /= 10;
            count++;
        }
        number = from;
        while (count > 0) {
            int i = number % 10;
            mPrimaryNumbers.add(i);
            number /= 10;
            count--;
        }

        for (int i = mTargetNumbers.size() - 1; i >= 0; i--) {
            ScrollNumber scrollNumber = new ScrollNumber(mContext);
            scrollNumber.setTextColor(mTextColors[i % mTextColors.length]);
            scrollNumber.setTextSize(mTextSize);
            if (!TextUtils.isEmpty(mFontFileName))
                scrollNumber.setTextFont(mFontFileName);
            scrollNumber.setNumber(mPrimaryNumbers.get(i), mTargetNumbers.get(i), i * 10);
            mScrollNumbers.add(scrollNumber);
            addView(scrollNumber);
        }

        lastVal = to;

    }

    public void setNumberFromLastVal(int val, int count) {
        setNumber(lastVal, val, count, true);
    }

    public void setNumber(int from, int to, int count, boolean forceflag) {
        resetView();
        // operate to
        int number = to;
        while (number > 0) {
            int i = number % 10;
            mTargetNumbers.add(i);
            number /= 10;
        }
        while (mTargetNumbers.size() < count) {
            mTargetNumbers.add(0);
        }
        // operate from
        number = from;
        while (number > 0) {
            int i = number % 10;
            mPrimaryNumbers.add(i);
            number /= 10;
        }

        while (mPrimaryNumbers.size() < count) {
            mPrimaryNumbers.add(0);
        }

        while (mScrollNumbers.size() < count) {
            ScrollNumber scrollNumber = new ScrollNumber(mContext);
            scrollNumber.setTextColor(mTextColors[0]);
            scrollNumber.setTextSize(mTextSize);
            scrollNumber.setShadowLayer(2.0f, 3, 3, Color.BLACK);
            if (!TextUtils.isEmpty(mFontFileName))
                scrollNumber.setTextFont(mFontFileName);
            mScrollNumbers.add(scrollNumber);
            addView(scrollNumber);
        }


        for (int i = mTargetNumbers.size() - 1; i >= 0; i--) {
//            ScrollNumber scrollNumber = new ScrollNumber(mContext);
//            scrollNumber.setTextColor(mTextColors[i % mTextColors.length]);
//            scrollNumber.setTextSize(mTextSize);
//            if (!TextUtils.isEmpty(mFontFileName))
//                scrollNumber.setTextFont(mFontFileName);
////            Log.i(TAG, "setNumber: from:" + mPrimaryNumbers.get(i) + " to:" + mTargetNumbers.get(i));
            mScrollNumbers.get(mTargetNumbers.size() - 1 - i).setNumber(mPrimaryNumbers.get(i), mTargetNumbers.get(i), i * 100);
//            mScrollNumbers.add(scrollNumber);
//            addView(scrollNumber);
        }

        lastVal = to;
//        mPrimaryNumbers = mTargetNumbers;

    }

    public void setTextColors(@ColorRes int[] textColors) {
        if (textColors == null || textColors.length == 0)
            throw new IllegalArgumentException("color array couldn't be empty!");
        mTextColors = textColors;
        for (int i = mScrollNumbers.size() - 1; i >= 0; i--) {
            ScrollNumber scrollNumber = mScrollNumbers.get(i);
            scrollNumber.setTextColor(mTextColors[i % mTextColors.length]);
        }
    }


    public void setTextSize(int textSize) {
        if (textSize <= 0) throw new IllegalArgumentException("text size must > 0!");
        mTextSize = textSize;
        for (ScrollNumber s : mScrollNumbers) {
            s.setTextSize(textSize);
        }
    }

    public void setInterpolator(Interpolator interpolator) {
        if (interpolator == null)
            throw new IllegalArgumentException("interpolator couldn't be null");
        mInterpolator = interpolator;
        for (ScrollNumber s : mScrollNumbers) {
            s.setInterpolator(interpolator);
        }
    }

    public void setTextFont(String fileName) {
        if (TextUtils.isEmpty(fileName)) throw new IllegalArgumentException("file name is null");
        mFontFileName = fileName;
        for (ScrollNumber s : mScrollNumbers) {
            s.setTextFont(fileName);
        }
    }


}
