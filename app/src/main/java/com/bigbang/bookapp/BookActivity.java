package com.bigbang.bookapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigbang.bookapp.view.BookPageBezierHelper;
import com.bigbang.bookapp.view.BookPageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: litte
 * Created on 2018/8/11  17:35
 * Project Name:BookApp
 * Package Name:com.bigbang.bookapp
 * Description: 阅读视图
 * Copyright (c) 2018, gwl All Rights Reserved.
 */
public class BookActivity extends AppCompatActivity {

    public static final String FILE_PATH = "file_path";
    private BookPageView mBookPageView;
    private TextView mTv_progress;
    private BookPageBezierHelper mBookPageBezierHelper;
    private RelativeLayout mRelativeLayout;
    private RecyclerView mRecyclerView;

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
        mRelativeLayout = findViewById(R.id.setting);
        mRecyclerView = findViewById(R.id.recyclerView);

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
        mBookPageView.setOnUserNeedSettingListener(new BookPageView.OnUserNeedSettingListener() {
            @Override
            public void onUserNeedSetting() {
                mRelativeLayout.setVisibility(mRelativeLayout.getVisibility() == View.VISIBLE ? View.GONE:View.VISIBLE);
            }
        });
        List<String> mData = new ArrayList<>();
        mData.add("添加书签");
        mData.add("读取书签");
        mData.add("设置背景");
        mData.add("语音朗读");
        mData.add("跳转进度");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(new MyRecyclerViewAdapter(this,mData));
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

    private class MyRecyclerViewAdapter extends RecyclerView.Adapter {
        private Context mContext;
        private List<String> mData = new ArrayList<>();
        MyRecyclerViewAdapter(Context context, List<String> mData) {
            mContext = context;
            this.mData = mData;
        }
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            TextView textView = new TextView(mContext);
            return new ViewHolder(textView);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            TextView textView = (TextView) holder.itemView;
            textView.setWidth(240);
            textView.setHeight(180);
            textView.setTextSize(26);
            textView.setTextColor(Color.RED);
            textView.setGravity(Gravity.CENTER);
            textView.setText(mData.get(position));
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private TextView mTextView;
            public ViewHolder(TextView itemView) {
                super(itemView);
                mTextView = itemView;
            }
        }
    }
}
