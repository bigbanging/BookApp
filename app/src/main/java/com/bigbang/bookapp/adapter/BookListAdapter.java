package com.bigbang.bookapp.adapter;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bigbang.bookapp.BookActivity;
import com.bigbang.bookapp.R;
import com.bigbang.bookapp.bean.BookBean;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import java.io.File;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Author: litte
 * Created on 2018/8/11  11:57
 * Project Name:BookApp
 * Package Name:com.bigbang.bookapp
 * Description: 适配器
 * Copyright (c) 2018, gwl All Rights Reserved.
 */
public class BookListAdapter extends BaseAdapter {

    private Context mContext;
    private List<BookBean.DataBean> mList;
    private LayoutInflater mInflater;
    private String mPathname;
    private File mFile;

    public BookListAdapter(Context context, List<BookBean.DataBean> list) {
        mContext = context;
        mList = list;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.book_list_item, parent,false);
            holder.tv_name = convertView.findViewById(R.id.tv_book_name);
            holder.btn_read = convertView.findViewById(R.id.btn_open);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //设置
        final BookBean.DataBean dataBean = mList.get(position);
        holder.tv_name.setText(dataBean.getBookname());
//        holder.btn_read.setText("点击下载");
        final ViewHolder finalHolder = holder;
        mPathname = Environment.getExternalStorageDirectory() + "/imooc/" + dataBean.getBookname() + ".txt";
        mFile = new File(mPathname);
        holder.btn_read.setText(mFile.exists()?"点击打开":"点击下载");

        holder.btn_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //下载书籍
                AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                asyncHttpClient.addHeader("Accept-Encoding","identity");
                if (mFile.exists()){
                    // 已经下载完成
//                    finalHolder.btn_read.setText("点击打开");
                    //打开书籍
                    BookActivity.start(mContext,mPathname);
                }else {
                    asyncHttpClient.get(dataBean.getBookfile(), new FileAsyncHttpResponseHandler(mFile) {
                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                            Toast.makeText(mContext, "下载失败", Toast.LENGTH_SHORT).show();
                            finalHolder.btn_read.setText("下载失败");
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, File file) {
                            finalHolder.btn_read.setText("点击打开");
                        }

                        @Override
                        public void onProgress(long bytesWritten, long totalSize) {
                            super.onProgress(bytesWritten, totalSize);
                            finalHolder.btn_read.setText(String.valueOf(bytesWritten * 100 / totalSize) + "%");
                        }
                    });
                }
            }
        });
        return convertView;
    }

    private class ViewHolder {
        private TextView tv_name;
        private Button btn_read;
    }
}
