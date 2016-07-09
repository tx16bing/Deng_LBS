package com.deng.lbs;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.Text;
import com.baidu.navisdk.adapter.BNOuterLogUtil;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BaiduNaviManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 搜索导航页面
 * 百度导航引擎初始化必须要有一个文件“assets/BaiduNaviSDK_Resource_v1_0_0.png”目录
 * Author: Created by deng on 2016/7/9.
 * E-mail: tx16bing@163.com
 */
public class MapNaviMainActivity extends AppCompatActivity{

    // 起点
    private static final int MAP_NAVIGATION_START_REQUEST_CODE = 0;
    // 终点
    private static final int MAP_NAVIGATION_END_REQUEST_CODE = MAP_NAVIGATION_START_REQUEST_CODE + 1;
    // 返回结果
    public static final int MAP_NAVIGATION_RESULT_CODE = MAP_NAVIGATION_END_REQUEST_CODE + 1;
    // 当前导航资源缓存目录文件夹名称
    private static final String MAP_NAVIGATION_FOLDER_NAME = "MyBaiduNavi";

    private MyPlan startPlan;
    private MyPlan endPlan;
    private TextView tv_content_start;
    private TextView tv_content_end;
    private String sdcardDir;

    private Handler ttsHandler = new Handler() {
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
        };
    };
    private BNRoutePlanNode startPlanNode;
    private BNRoutePlanNode endPlanNode;

    public void showToastMsg(final String msg) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(MapNaviMainActivity.this, msg,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_main);
        if (initMapNaviDirs()) {
            initMap();
        }
        initView();
    }

    /**
     * 初始化百度地图导航缓存目录
     *
     * @return
     */
    private boolean initMapNaviDirs() {
        sdcardDir = null;
        if (Environment.getExternalStorageState().equalsIgnoreCase(
                Environment.MEDIA_MOUNTED)) {
            sdcardDir = Environment.getExternalStorageDirectory().toString();
        }
        if (TextUtils.isEmpty(sdcardDir)) {
            return false;
        }
        // 创建目录
        File file = new File(sdcardDir, MAP_NAVIGATION_FOLDER_NAME);
        if (!file.exists()) {
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
     * 初始化地图
     */
    private void initMap() {
        BNOuterLogUtil.setLogSwitcher(true);
        BaiduNaviManager.getInstance().init(this, sdcardDir,
                MAP_NAVIGATION_FOLDER_NAME, new BaiduNaviManager.NaviInitListener() {

                    private String authinfo;

                    @Override
                    public void onAuthResult(int status, String msg) {
                        if (0 == status) {
                            authinfo = "key校验成功!";
                        } else {
                            authinfo = "key校验失败, " + msg;
                        }
                        MapNaviMainActivity.this.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                Toast.makeText(MapNaviMainActivity.this,
                                        authinfo, Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    @Override
                    public void initSuccess() {
                        Toast.makeText(MapNaviMainActivity.this, "百度导航引擎初始化成功",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void initStart() {
                        Toast.makeText(MapNaviMainActivity.this, "百度导航引擎初始化开始",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void initFailed() {
                        Toast.makeText(MapNaviMainActivity.this, "百度导航引擎初始化失败",
                                Toast.LENGTH_SHORT).show();
                    }
                }, null, ttsHandler, null);
    }


    private void initView() {
        tv_content_start = (TextView) findViewById(R.id.tv_content_start);
        tv_content_start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startMapNaviSearchActivity(MAP_NAVIGATION_START_REQUEST_CODE,
                        "起点");
            }
        });
        tv_content_end = (TextView) findViewById(R.id.tv_content_end);
        tv_content_end.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startMapNaviSearchActivity(MAP_NAVIGATION_END_REQUEST_CODE,
                        "终点");
            }
        });
    }

    /**
     * 启动POI搜索页面
     *
     * @param requestCode
     * @param text
     */
    private void startMapNaviSearchActivity(int requestCode, String text) {
        Intent intent = new Intent(this, MapNaviSearchActivity.class);
        intent.putExtra("text", text);
        startActivityForResult(intent, requestCode);
    }

    /**
     * 公交线路搜索
     * @param view
     */
    public void searchTran(View view){
//        startActivity(new Intent(MapNaviMainActivity.this, MapNavigationMainActivity.class));
    }

     /**
     * 开始算路，发起导航
     *
     * @param bt
     */
    public void navigation(View bt) {
        if (TextUtils.isEmpty(tv_content_start.getText().toString()) && TextUtils.isEmpty(tv_content_end.getText().toString())){
            return;
        }

        if (TextUtils.isEmpty(tv_content_start.getText().toString()) && !TextUtils.isEmpty(tv_content_end.getText().toString())){
            Toast.makeText(this, "请输入起点", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!TextUtils.isEmpty(tv_content_start.getText().toString()) && TextUtils.isEmpty(tv_content_end.getText().toString())){
            Toast.makeText(this, "请输入终点", Toast.LENGTH_SHORT).show();
            return;
        }

        startPlanNode = new BNRoutePlanNode(
                startPlan.longitude, startPlan.latitude, startPlan.name, null);
        endPlanNode = new BNRoutePlanNode(endPlan.longitude,
                endPlan.latitude, endPlan.name, null);
        if (startPlanNode != null && endPlanNode != null) {
            List<BNRoutePlanNode> nodes = new ArrayList<BNRoutePlanNode>();
            nodes.add(startPlanNode);
            nodes.add(endPlanNode);
            // nodes:传入的算路节点，顺序是起点、途经点、终点，其中途经点最多三个
            // preference - 算路偏好（躲避拥堵、少走高速、高速优先、少收费、推荐）
            // isGPSNav - true表示真实GPS导航，false表示模拟导航
            // listener - 开始导航回调监听器，在该监听器里一般是进入导航过程页面
            BaiduNaviManager.getInstance().launchNavigator(this, nodes,
                    BaiduNaviManager.RoutePlanPreference.ROUTE_PLAN_MOD_RECOMMEND, true,
                    new BaiduNaviManager.RoutePlanListener() {

                        @Override
                        public void onRoutePlanFailed() {
                            Toast.makeText(MapNaviMainActivity.this, "算路失败",
                                    Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onJumpToNavigator() {
                            // 可以进入导航啦
                            Toast.makeText(MapNaviMainActivity.this, "可以开始导航了",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MapNaviMainActivity.this, MapNaviGuideActivity.class);
                            intent.putExtra("startPlanNode", startPlanNode);
                            startActivity(intent);
                        }
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case MAP_NAVIGATION_START_REQUEST_CODE:
                if (data != null){
                    startPlan = newPlan(data);
                    tv_content_start.setText(startPlan.name);
                }
                break;
            case MAP_NAVIGATION_END_REQUEST_CODE:
                if (data != null){
                    // 处理终点返回的信息
                    endPlan = newPlan(data);
                    tv_content_end.setText(endPlan.name);
                }
                break;

            default:
                break;
        }
    }

    private MyPlan newPlan(Intent data) {
        MyPlan plan = new MyPlan();
        plan.name = data.getStringExtra("name");
        plan.address = data.getStringExtra("address");
        plan.latitude = data.getDoubleExtra("latitude", 0);
        plan.longitude = data.getDoubleExtra("longitude", 0);
        return plan;
    }

    class MyPlan {
        // 地名
        public String name;
        // 地址
        public String address;
        // 纬度
        public double latitude;
        // 经度
        public double longitude;
    }

}
