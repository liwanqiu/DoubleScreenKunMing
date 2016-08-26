package com.changfeng.tcpdemo;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chang on 2016/8/12.
 */
public class SplashActivity extends BaseActivity {

    public static final int DELAY = 4;

    @OnClick(R.id.button_settings)
    void toSettings(View view) {
        redirectTo(InitActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(this, R.layout.activity_splash, null);
        setContentView(view);
        ButterKnife.bind(this);
//        showAnimation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(runnable, DELAY * 1000);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            startBusInfoActivity();
        }
    };

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        startBusInfoActivity();
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    public void startBusInfoActivity() {
        redirectTo(BusInfoActivity.class);
        finish();
    }

//    private void showAnimation() {
//        Animation a = new AlphaAnimation(0.0f, 1.0f);
//        a.setDuration(DELAY * 1000);
//        splashImageView.setAnimation(a);
//
//        a.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                startBusInfoActivity();
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//    }

}
