package com.deng.lbs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.SupportMapFragment;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.deng.lbs.fragment.RoutePlanFragment;

import java.util.List;

/**
 * 线路规划
 * Author: Created by deng on 2016/7/8.
 * E-mail: tx16bing@163.com
 */
public class RoutePlanActivity extends AppCompatActivity implements OnGetRoutePlanResultListener {

    private SupportMapFragment mapFragment;
    private RoutePlanFragment routePlanFragment;
    private BaiduMap baiduMap;
    private RoutePlanSearch routePlanSearch;
    private List<TransitRouteLine> lines;

    private EditText startEt;
    private EditText endEt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routeplan);
        getSupportFragment();
        initPOI();
        initView();
    }

    private void getSupportFragment() {
        this.mapFragment = new SupportMapFragment();
        this.routePlanFragment = new RoutePlanFragment();
        showFragment(this.mapFragment);
    }

    private void initPOI() {
        this.routePlanSearch = RoutePlanSearch.newInstance();
        this.routePlanSearch.setOnGetRoutePlanResultListener(this);

    }

    private void initView() {
        this.startEt = (EditText) findViewById(R.id.start);
        this.endEt = (EditText) findViewById(R.id.end);
    }

    /**
     * 公交搜索
     * @param btn
     */
    public void searchTran(View btn){
        showFragment(this.mapFragment);
        this.baiduMap = this.mapFragment.getBaiduMap();
        PlanNode from = PlanNode.withCityNameAndPlaceName("深圳", this.startEt.getText().toString());
        PlanNode to = PlanNode.withCityNameAndPlaceName("深圳", this.endEt.getText().toString());
        this.routePlanSearch.transitSearch(new TransitRoutePlanOption().from(from).city("深圳").to(to));
    }

    /**
     * 路线搜索
     */
    public void searchLines(View btn){
        this.routePlanFragment.setLines(this.lines);
        showFragment(this.routePlanFragment);
    }

    private void showFragment(Fragment fragment) {

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragmentByTag = fm.findFragmentByTag(fragment.getClass().getSimpleName());
        FragmentTransaction beginTransaction = fm.beginTransaction();

        if (fragmentByTag == null){
            // 如果为空，表示Activity中还没有添加该Fragment
            beginTransaction.add(R.id.frame_map, fragment, fragment.getClass().getSimpleName());
        }else{

            // 如果不为空， 表示已经添加在Activity的Fragment管理器中，从管理器中取出Fragment进行显示和隐藏
            List<Fragment> fragments = fm.getFragments();
            // 遍历管理器中的Fragment
            for (Fragment fragment1 : fragments){
                if (fragment1.getClass().getSimpleName().equals(fragment.getClass().getSimpleName())){
                    // 如果传入的Fragment等于容器的Fragment，那么显示
                    beginTransaction.show(fragment1);
                }else{
                    beginTransaction.hide(fragment1);
                }
            }
        }

        // 最后提交事务
        beginTransaction.commit();

    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
        // 表示步行路线
    }

    /**
     * 公交路线规划
     */
    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {
        // 表示公交线路
        this.lines = transitRouteResult.getRouteLines();
        for (TransitRouteLine transitRouteLine : lines){
            // 规划每一条线路
            TransitRouteOverlay routeOverlay = new TransitRouteOverlay(this.baiduMap);
            routeOverlay.setData(transitRouteLine);
            routeOverlay.addToMap();
            routeOverlay.zoomToSpan();
        }
    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {
        // 表示自驾路线
    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {
        // 表示自行车骑行路线
    }
}
