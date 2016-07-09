package com.deng.lbs;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * 绘制几何图形覆盖物
 * Author: Created by deng on 2016/7/8.
 * E-mail: tx16bing@163.com
 */
public class GeometryActivity extends AppCompatActivity{

    private MapView mapView;
    private BaiduMap baiduMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geomtry);
        initView();
    }

    private void initView() {

        this.mapView = (MapView) findViewById(R.id.map_view);
        this.baiduMap = this.mapView.getMap();

    }

    /**
     * 清除
     * @param btn
     */
    public void clear(View btn){
        this.baiduMap.clear();
    }

    /**
     * 重置
     * @param btn
     */
    public void reset(View btn){

       //定义多边形的五个顶点
        LatLng pt1 = new LatLng(39.93923, 116.357428);
        LatLng pt2 = new LatLng(39.91923, 116.327428);
        LatLng pt3 = new LatLng(39.89923, 116.347428);
        LatLng pt4 = new LatLng(39.89923, 116.367428);
        LatLng pt5 = new LatLng(39.91923, 116.387428);
        List<LatLng> latLngs = new ArrayList<LatLng>();
        latLngs.add(pt1);
        latLngs.add(pt2);
        latLngs.add(pt3);
        latLngs.add(pt4);
        latLngs.add(pt5);

        // 创建多边形覆盖物
        PolygonOptions polygonOptions = new PolygonOptions();
        // 构建图形路径
        polygonOptions.points(latLngs);
        //设置多边形覆盖物的图形路径的宽度（边框的默认宽度是5）
        polygonOptions.stroke(new Stroke(3, Color.RED));
		//指定多边形覆盖物的填充颜色--透明
        polygonOptions.fillColor(Color.TRANSPARENT);
        this.baiduMap.addOverlay(polygonOptions);

        //提醒：一定要多观察，多看开发平台的常见问题
        //画圆
//        LatLng pt4 = new LatLng(39.89923, 116.367428);
//        //创建圆形覆盖物
//        CircleOptions circleOptions = new CircleOptions();
//        //设置圆心坐标
//        circleOptions.center(pt4);
//
//        //设置圆半径
//        circleOptions.radius(2 * 1000);
//        //设置边框
//        circleOptions.stroke(new Stroke(5, Color.BLUE));
//        //设置填充颜色
//        circleOptions.fillColor(Color.TRANSPARENT);
//        //绑定圆形覆盖物
//        baiduMap.addOverlay(circleOptions);

    }

    @Override
    protected void onResume() {
        super.onResume();
        // 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mapView.onDestroy();
    }

}
