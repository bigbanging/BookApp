package com.bigbang.bookapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.bigbang.bookapp.adapter.BookListAdapter;
import com.bigbang.bookapp.bean.BookBean;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Author: litte
 * Created on 2018/8/10  17:57
 * Project Name:BookApp
 * Package Name:com.bigbang.bookapp
 * Description: 数据列表页面
 * Copyright (c) 2018, gwl All Rights Reserved.
 */
public class MainActivity extends AppCompatActivity{

    private static final String TAG = "MainActivity";
    private ListView mListView;
    private List<BookBean.DataBean> mList = new ArrayList<>();
    BookListAdapter adapter;
    private List<BookBean.DataBean> mData = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = findViewById(R.id.book_list);
//        mList = new ArrayList<>();

//        adapter = new BookListAdapter(this, mList);
        String url = "http://www.imooc.com/api/teacher?type=10";
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
//                Log.i(TAG, "onSuccess: result"+result);
               /* try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray data = jsonObject.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject object = (JSONObject) data.get(i);
                        String bookname = object.getString("bookname");
                        BookBean.DataBean dataBean = new BookBean.DataBean();
                        dataBean.setBookname(bookname);
                        mList.add(dataBean);
                    }
                    mListView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
                Gson gson = new Gson();
                BookBean bookBean = gson.fromJson(result, BookBean.class);
                mData = bookBean.getData();
                adapter = new BookListAdapter(MainActivity.this, mData);
                mListView.setAdapter(adapter);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(MainActivity.this, "数据加载失败，请稍后再试", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "onFailure: "+"statusCode:"+statusCode+"responseBody:"+responseBody);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

    public static void Start(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }
}
