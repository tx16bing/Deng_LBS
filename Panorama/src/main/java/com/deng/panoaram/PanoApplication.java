package com.deng.panoaram;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.baidu.lbsapi.BMapManager;
import com.baidu.lbsapi.MKGeneralListener;

/**
 * Author: Created by deng on 2016/7/11.
 * E-mail: tx16bing@163.com
 */
public class PanoApplication extends Application{

    private PanoApplication mContext;
    private BMapManager bMapManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        initMapManager();
    }

    public PanoApplication getInstance() {
        return mContext;
    }

    /**
     *  初始化全景图管理器
     */
    private void initMapManager() {
        if (bMapManager == null){
            bMapManager = new BMapManager(mContext);
            bMapManager.init(new MyGeneralListener(this));
        }
    }


    class MyGeneralListener implements MKGeneralListener {

        private Context context;

        public MyGeneralListener(Context context) {
            this.context = context;
        }

        @Override
        public void onGetPermissionState(int iError) {

            if (iError != 0){
                // 授权key错误
                Toast.makeText( this.context, "请在AndoridManifest.xml中输入正确的授权Key,并检查您的网络连接是否正常！error: " + iError,
                        Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText( this.context, "key 认证成功", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
