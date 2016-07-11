package com.deng.lbs;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;

/**
 * 自定义Application 初始化百度地图相关sdk
 * Author: Created by deng on 2016/7/8.
 * E-mail: tx16bing@163.com
 */
public class LBSApplication extends Application {

    private LBSApplication mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;

        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //如果需要在Activity中初始化注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());

    }


    public LBSApplication getInstance() {
        return mContext;
    }


}
