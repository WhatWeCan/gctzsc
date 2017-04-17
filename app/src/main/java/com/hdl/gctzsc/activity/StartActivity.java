package com.hdl.gctzsc.activity;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.hdl.gctzsc.R;

/**
 * 欢迎界面
 */
public class StartActivity extends AppCompatActivity {
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //无title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //全屏
        setContentView(R.layout.activity_start);
        initView();
    }

//    /**
//     * 初始化短信sdk
//     */
//    private void initSMSSDK() {
//        SMSSDK.initSDK(this, "119e746d33b9f", "e0313795a8c5cc3334339a0a8f14b2a8");
//    }

    private void initView() {
        LinearLayout llWelcome = (LinearLayout) findViewById(R.id.activity_startActivity);
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("alpha",
                0.4f, 1f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleX", 1f,
                1.1f);
        PropertyValuesHolder pvhZ = PropertyValuesHolder.ofFloat("scaleY", 1f,
                1.1f);
        ObjectAnimator.ofPropertyValuesHolder(llWelcome, pvhX, pvhY, pvhZ).setDuration(1400).start();

        //隔1.5秒跳转到主页
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1500);
    }
}
