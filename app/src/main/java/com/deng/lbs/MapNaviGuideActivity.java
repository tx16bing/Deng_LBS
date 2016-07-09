package com.deng.lbs;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.baidu.navisdk.adapter.BNRouteGuideManager;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNaviSettingManager;

/**
 * 导航页面（不需要布局文件）， 百度地图会提供一个视图给你
 * Author: Created by deng on 2016/7/9.
 * E-mail: tx16bing@163.com
 */
public class MapNaviGuideActivity extends AppCompatActivity{

    private static final int MSG_SHOW = 1;
    private static final int MSG_HIDE = 2;
    private static final int MSG_RESET_NODE = 3;
    private Handler hd = null;

    private BNRoutePlanNode startPlanNode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//		createHandler();
        // 百度导航视图
        View view = BNRouteGuideManager.getInstance().onCreate(this,
                onNavigationListener);
        setContentView(view);

        // 获取导航的起点
        startPlanNode = (BNRoutePlanNode) getIntent().getSerializableExtra(
                "startPlanNode");
        initSetting();
    }

    //自定义导航图标
    private void createHandler() {
        if (hd == null) {
            hd = new Handler(getMainLooper()) {
                public void handleMessage(android.os.Message msg) {
                    if (msg.what == MSG_SHOW) {

                    } else if (msg.what == MSG_HIDE) {
                    } else if (msg.what == MSG_RESET_NODE) {
                    }
                };
            };
        }
    }

    /**
     * 配置地图导航参数
     */
    private void initSetting(){
        //BNaviSettingManager.DayNightMode－－日夜模式
        //DAY_NIGHT_MODE_AUTO(日夜模式 ：自动模式)
        //DAY_NIGHT_MODE_DAY(日夜模式 ：白天模式)
        //DAY_NIGHT_MODE_NIGHT(日夜模式 ：夜晚模式)
        BNaviSettingManager.setDayNightMode(BNaviSettingManager.DayNightMode.DAY_NIGHT_MODE_AUTO);
        //BNaviSettingManager.PowerSaveMode---省电模式
        //AUTO_MODE(省电模式：自动)---根据当前手机电量动态改变导航的模式－－－为了省电
        //DISABLE_MODE(省电模式：关闭模式)
        //ENABLE_MODE(省电模式：开启模式)
        BNaviSettingManager.setPowerSaveMode(BNaviSettingManager.PowerSaveMode.AUTO_MODE);
        //BNaviSettingManager.PreViewRoadCondition--全程路况显示
        //ROAD_CONDITION_BAR_SHOW_OFF---关闭路况
        //ROAD_CONDITION_BAR_SHOW_ON---显示路况
        BNaviSettingManager.setShowTotalRoadConditionBar(BNaviSettingManager.PreViewRoadCondition.ROAD_CONDITION_BAR_SHOW_ON);
        //BNaviSettingManager.RealRoadCondition---实时路况条设置
        BNaviSettingManager.setRealRoadCondition(BNaviSettingManager.RealRoadCondition.NAVI_ITS_ON);
        //BNaviSettingManager.VoiceMode－－－语音播报模式
        BNaviSettingManager.setVoiceMode(BNaviSettingManager.VoiceMode.Veteran);
    }

    @Override
    protected void onResume() {
        super.onResume();
        BNRouteGuideManager.getInstance().onResume();

        if (hd != null) {
            hd.sendEmptyMessageAtTime(MSG_SHOW, 2000);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        BNRouteGuideManager.getInstance().onPause();
    };

    @Override
    protected void onStop() {
        super.onStop();
        BNRouteGuideManager.getInstance().onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BNRouteGuideManager.getInstance().onDestroy();
    }

    @Override
    public void onBackPressed() {
        BNRouteGuideManager.getInstance().onBackPressed(false);
    }

    public void onConfigurationChanged(
            android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        BNRouteGuideManager.getInstance().onConfigurationChanged(newConfig);
    };

    private BNRouteGuideManager.OnNavigationListener onNavigationListener = new BNRouteGuideManager.OnNavigationListener() {

        @Override
        public void onNaviGuideEnd() {
            finish();
        }

        @Override
        public void notifyOtherAction(int arg0, int arg1, int arg2, Object arg3) {

        }
    };

}
