package com.deng.lbs.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Fragment基类
 * Author: Created by deng on 2016/7/8.
 * E-mail: tx16bing@163.com
 */
public abstract class BaseFragment extends Fragment{

    //我们自己的Fragment要缓存视图
    private View contentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (this.contentView == null){
            this.contentView = inflater.inflate(getContentView(), container, false);
            initContentView(this.contentView);
            initData();
        }

        //  判断Fragment对应Activity是否存在这个视图
        ViewGroup parent = (ViewGroup) this.contentView.getParent();
        if (parent != null){
            // 如果存在，那么就移除，重新添加，这样的方式我们就可以缓存视图
            parent.removeView(this.contentView);
        }
        return this.contentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }

    public abstract int getContentView();
    public abstract void initContentView(View contentView);
    public abstract void initData();
    public abstract void refreshData();
}
