package com.deng.lbs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Overlay;

/**
 * 需要注意的一点是：AS 2.1.2版本在测试百度导航时候回存在编译问题，这个是AS的一个问题
 * 解决方法是：把AS的版本换回2.1.0，经过测试时没有问题的
 * Author: Created by deng on 2016/7/8.
 * E-mail: tx16bing@163.com
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private MapView mapView;
    private BaiduMap mBaiduMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }

    private void initView() {

        findViewById(R.id.init_btn).setOnClickListener(this);
        findViewById(R.id.overlay_btn).setOnClickListener(this);
        findViewById(R.id.geometry_btn).setOnClickListener(this);
        findViewById(R.id.poi_btn).setOnClickListener(this);
        findViewById(R.id.route_btn).setOnClickListener(this);
        findViewById(R.id.location_btn).setOnClickListener(this);
        findViewById(R.id.nearby_poi_btn).setOnClickListener(this);
        findViewById(R.id.navigation_btn).setOnClickListener(this);
        findViewById(R.id.panorama_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.init_btn:
                startActivity(new Intent(MainActivity.this, InitActivity.class));
                break;

            case R.id.overlay_btn:
                startActivity(new Intent(MainActivity.this, OverlayActivity.class));
                break;

            case R.id.geometry_btn:
                startActivity(new Intent(MainActivity.this, GeometryActivity.class));
                break;

            case R.id.poi_btn:
                startActivity(new Intent(MainActivity.this, POISearchActivity.class));
                break;

            case R.id.route_btn:
                startActivity(new Intent(MainActivity.this, RoutePlanActivity.class));
                break;

            case R.id.location_btn:
                startActivity(new Intent(MainActivity.this, LocationActivity.class));
                break;

            case R.id.nearby_poi_btn:
                startActivity(new Intent(MainActivity.this, POISearchNearByActivity.class));
                break;

            case R.id.navigation_btn:
//                startActivity(new Intent(MainActivity.this, MapNavigationMainActivity.class));
                startActivity(new Intent(MainActivity.this, MapNaviMainActivity.class));
                break;

            default:
                break;
        }

    }
}
