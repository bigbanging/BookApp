package com.bigbang.bookapp;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.lang.ref.WeakReference;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int INT_CODE = 1001;
    public static final int DELAY_MILLIS = 1000;
    private TextView tv_countDown;
    private MyHandler mMyHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        setContentView(R.layout.activity_splash);
        initView();
    }

    private void initView() {
        tv_countDown = findViewById(R.id.tv_countDown);
        mMyHandler = new MyHandler(this);
        Message message = Message.obtain();
        message.what = INT_CODE;
        message.arg1 = 3000;
        mMyHandler.sendMessage(message);
        tv_countDown.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_countDown:
//                jumpActivity();
                MainActivity.Start(SplashActivity.this);
                finish();
                mMyHandler.removeMessages(INT_CODE);
                break;
                default:break;
        }
    }

    private void jumpActivity() {
        startActivity(new Intent(this,MainActivity.class));
    }

    private static class MyHandler extends Handler {
        private WeakReference<SplashActivity> mWeakReference;
        MyHandler(SplashActivity activity) {
            mWeakReference = new WeakReference<SplashActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            SplashActivity activity = mWeakReference.get();
            if (msg.what == INT_CODE) {
                int time = msg.arg1;
                if (activity != null) {
                    // 更新UI
                    activity.tv_countDown.setText(time/1000+activity.getString(R.string.tv_splash));
                    //倒计时更新数据
                    Message message = Message.obtain();
                    message.what = INT_CODE;
                    message.arg1 = time - DELAY_MILLIS;
                    if (time > 0) {
                        sendMessageDelayed(message, DELAY_MILLIS);
                    } else {
                        //跳转主页
                        activity.jumpActivity();
                        activity.finish();
                    }
                }
            }
        }
    }
}
