package com.deng.lbs;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.Text;
import com.baidu.navisdk.adapter.BNOuterLogUtil;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNaviSettingManager;
import com.baidu.navisdk.adapter.BaiduNaviManager;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 搜索导航
 * Author: Created by deng on 2016/7/8.
 * E-mail: tx16bing@163.com
 */
public class MapNavigationMainActivity extends AppCompatActivity{

    public static final int RESULT_CODE_LOCATION = 0;
    public static final int REQUEST_CODE_START_LOCATION = 1;
    public static final int REQUEST_CODE_END_LOCATION = 2;

    private TextView startTv;
    private TextView endTv;
    private EditText et_city;

    private String mSDCardPath = null;
    private String authinfo = null;
    private static final String APP_FOLDER_NAME = "DengBdSDK";

    public static final String ROUTE_PLAN_NODE = "routePlanNode";
    public static final String SHOW_CUSTOM_ITEM = "showCustomItem";
    public static final String RESET_END_NODE = "resetEndNode";
    public static final String VOID_MODE = "voidMode";

    private MyPlan startPlan;
    private MyPlan endPlan;

    /**
     * 内部TTS播报状态回传handler
     */
    private Handler ttsHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int type = msg.what;
            switch (type) {
                case BaiduNaviManager.TTSPlayMsgType.PLAY_START_MSG: {
                    showToastMsg("Handler : TTS play start");
                    break;
                }
                case BaiduNaviManager.TTSPlayMsgType.PLAY_END_MSG: {
                    showToastMsg("Handler : TTS play end");
                    break;
                }
                default:
                    break;
            }
        }
    };

    public void showToastMsg(final String msg) {
        MapNavigationMainActivity.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(MapNavigationMainActivity.this, msg,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_main);
        if (initDirs()){
            initMap();
        }
        initView();
    }

    private void initView() {
        this.startTv = (TextView) findViewById(R.id.tv_content_start);
        this.endTv = (TextView) findViewById(R.id.tv_content_end);

        this.startTv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                poiSearch(REQUEST_CODE_START_LOCATION, "起点: ");
            }
        });
        this.endTv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                poiSearch(REQUEST_CODE_END_LOCATION, "终点: ");
            }
        });
    }

    // 跳转到搜索界面进行搜索
    private void poiSearch(int requestCode, String hint) {

        Intent intent = new Intent(this, MapNavigationSearchActivity.class);
        intent.putExtra("hint", hint);
        startActivityForResult(intent, requestCode);

    }

    /**
     * 导航需要缓存离线文件，因此这里需要在本地有一个文件目录保存信息
     * @return
     */
    private boolean initDirs(){
        this.mSDCardPath = getSdcardDir();
        if (this.mSDCardPath == null){
            return false;
        }
        File file = new File(this.mSDCardPath, APP_FOLDER_NAME);
        if (!file.exists()){
            try {
                file.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    /**
     * 读取sdcard状态（是否挂载或者是否存在）
     * @return
     */
    private String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)){
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }

    /**
     * 百度导航初始化
     */
    private void initMap() {

        BNOuterLogUtil.setLogSwitcher(true);
        BaiduNaviManager.getInstance().init(this, this.mSDCardPath,APP_FOLDER_NAME,
        new BaiduNaviManager.NaviInitListener(){

            @Override
            public void onAuthResult(int status, String msg) {
                if (0 == status) {
                    authinfo = "key校验成功!";
                } else {
                    authinfo = "key校验失败, " + msg;
                }
                MapNavigationMainActivity.this
                        .runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                Toast.makeText(
                                        MapNavigationMainActivity.this,
                                        authinfo, Toast.LENGTH_LONG)
                                        .show();
                            }
                        });
            }

            @Override
            public void initStart() {
                Toast.makeText(MapNavigationMainActivity.this,
                        "百度导航引擎初始化开始", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void initSuccess() {
                Toast.makeText(MapNavigationMainActivity.this,
                        "百度导航引擎初始化成功", Toast.LENGTH_SHORT).show();
                initSetting();
            }

            @Override
            public void initFailed() {
                Toast.makeText(MapNavigationMainActivity.this,
                        "百度导航引擎初始化失败", Toast.LENGTH_SHORT).show();
            }
        }, null, ttsHandler, null);

    }

    /**
     * 初始化导航信息
     */
    private void initSetting() {
        BNaviSettingManager
                .setDayNightMode(BNaviSettingManager.DayNightMode.DAY_NIGHT_MODE_DAY);
        BNaviSettingManager
                .setShowTotalRoadConditionBar(BNaviSettingManager.PreViewRoadCondition.ROAD_CONDITION_BAR_SHOW_ON);
        BNaviSettingManager.setVoiceMode(BNaviSettingManager.VoiceMode.Veteran);
        BNaviSettingManager
                .setPowerSaveMode(BNaviSettingManager.PowerSaveMode.DISABLE_MODE);
        BNaviSettingManager
                .setRealRoadCondition(BNaviSettingManager.RealRoadCondition.NAVI_ITS_ON);
    }

    /**
     * 公交线路搜索
     * @param view
     */
    public void searchTran(View view){

    }

    /**
     * 点击开始导航
     *
     * @param view
     */
    public void navigation(View view) {
        if (this.startPlan == null || this.endPlan == null) {
            Toast.makeText(this, "请输入起点和终点！", Toast.LENGTH_LONG).show();
            return;
        }
        BNRoutePlanNode sNode = new BNRoutePlanNode(startPlan.longitude,
                startPlan.latitude, startPlan.name, null, BNRoutePlanNode.CoordinateType.BD09LL);
        BNRoutePlanNode eNode = new BNRoutePlanNode(endPlan.longitude,
                endPlan.latitude, endPlan.name, null, BNRoutePlanNode.CoordinateType.BD09LL);
        if (sNode != null && eNode != null) {
            List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
            list.add(sNode);
            list.add(eNode);

            BaiduNaviManager
                    .getInstance()
                    .launchNavigator(
                            this,
                            list,
                            BaiduNaviManager.RoutePlanPreference.ROUTE_PLAN_MOD_RECOMMEND,
                            true, new DemoRoutePlanListener(sNode));
        }
    }

    public class DemoRoutePlanListener implements BaiduNaviManager.RoutePlanListener {

        private BNRoutePlanNode mBNRoutePlanNode = null;

        public DemoRoutePlanListener(BNRoutePlanNode node) {
            mBNRoutePlanNode = node;
        }

        @Override
        public void onJumpToNavigator() {
			/*
			 * 设置途径点以及resetEndNode会回调该接口
			 */

            Intent intent = new Intent(MapNavigationMainActivity.this,
                    MapNavigationGuideActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ROUTE_PLAN_NODE,
                    (BNRoutePlanNode) mBNRoutePlanNode);
            intent.putExtras(bundle);
            startActivity(intent);
        }

        @Override
        public void onRoutePlanFailed() {
            Toast.makeText(MapNavigationMainActivity.this, "算路失败",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_START_LOCATION
                && resultCode == RESULT_CODE_LOCATION) {
            // 起点
            this.startPlan = (MyPlan) data.getSerializableExtra("plan");
            this.startTv.setText(startPlan.name);
        } else if (requestCode == REQUEST_CODE_END_LOCATION
                && resultCode == RESULT_CODE_LOCATION) {
            // 终点
            this.endPlan = (MyPlan) data.getSerializableExtra("plan");
            this.endTv.setText(endPlan.name);
        }
    }

    public static class MyPlan implements Serializable{
        private static final long serialVersionUID = 1L;
        public String name;
        public String address;
        public double latitude;
        public double longitude;
    }
}
