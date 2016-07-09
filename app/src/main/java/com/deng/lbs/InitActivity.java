package com.deng.lbs;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;

/**
 * 百度地图集成
 * Author: Created by deng on 2016/7/8.
 * E-mail: tx16bing@163.com
 */
public class InitActivity extends AppCompatActivity {


    private MapView mapView;
    private BaiduMap mBaiduMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        initMapView();

    }

    private void initMapView() {
        // 获取地图控件引用
        this.mapView = (MapView) findViewById(R.id.bmapView);
        this.mBaiduMap = this.mapView.getMap();
    }

    // 以下几个方法是各种图层的显示

    /**
     * 普通地图
     * @param v
     */
    public void normal(View v){
        this.mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NONE);
    }

    /**
     * 卫星地图
     * @param v
     */
    public void satellite(View v){
        this.mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
    }

    /**
     * 交通地图
     * @param v
     */
    public void traffic(View v){
        this.mBaiduMap.setTrafficEnabled(true);
    }

    /**
     * 热力地图
     * @param v
     */
    public void heatmap(View v){
        this.mBaiduMap.setBaiduHeatMapEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        this.mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        this.mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        this.mapView.onDestroy();
    }

}
