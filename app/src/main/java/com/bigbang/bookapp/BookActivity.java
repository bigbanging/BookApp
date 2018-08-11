package com.bigbang.bookapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.TextView;

import com.bigbang.bookapp.view.BookPageBezierHelper;
import com.bigbang.bookapp.view.BookPageView;

import java.io.IOException;

/**
 * Author: litte
 * Created on 2018/8/11  17:35
 * Project Name:BookApp
 * Package Name:com.bigbang.bookapp
 * Description: TODO
 * Copyright (c) 2018, gwl All Rights Reserved.
 */
public class BookActivity extends AppCompatActivity {

    public static final String FILE_PATH = "file_path";
    private BookPageView mBookPageView;
    private TextView mTv_progress;
    private BookPageBezierHelper mBookPageBezierHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // fullScreen
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_book);
        // init view
        mBookPageView = findViewById(R.id.bookPageView);
        mTv_progress = findViewById(R.id.tv_progress);
        //get width height
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        mBookPageBezierHelper = new BookPageBezierHelper(width, height);
        mBookPageView.setBookPageBezierHelper(mBookPageBezierHelper);

        Bitmap currentBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Bitmap nextBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mBookPageView.setBitmaps(currentBitmap,nextBitmap);
        //设置自定义图片
//        mBookPageBezierHelper.setUseBg(true);
        mBookPageBezierHelper.setBackground(this,R.drawable.book_bg);
        //显示阅读进度
        mBookPageBezierHelper.setOnProgressChangedListener(new BookPageBezierHelper.OnProgressChangedListener() {
            @Override
            public void setProgress(int currentLength, int totalLength) {
                mTv_progress.setText(currentLength/totalLength+"%");
            }
        });
        Intent intent = getIntent();
        if (intent != null) {
            String filePath = intent.getStringExtra(FILE_PATH);
            if (!TextUtils.isEmpty(filePath)) {
                try {
                    mBookPageBezierHelper.openBook(filePath);
                    mBookPageBezierHelper.draw(new Canvas(currentBitmap));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else {
                // TODO can not find book path
            }
        } else {
            // TODO can not find book path
        }
    }

    public static void start(Context context, String filePath) {
        Intent intent = new Intent(context, BookActivity.class);
        intent.putExtra(FILE_PATH, filePath);
        context.startActivity(intent);
    }
}
