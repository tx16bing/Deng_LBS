package com.deng.lbs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.SupportMapFragment;
import com.baidu.mapapi.model.LatLng;

/**
 * 百度地图定位
 * Author: Created by deng on 2016/7/8.
 * E-mail: tx16bing@163.com
 */
public class LocationActivity extends AppCompatActivity implements BDLocationListener {

    private LocationClient locationClient;
    private BaiduMap baiduMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        initMapFragment();
        initLocation();
    }

    private void initMapFragment() {

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        this.baiduMap = mapFragment.getBaiduMap();

    }

    private void initLocation() {
        this.locationClient = new LocationClient(getApplicationContext());
        this.locationClient.registerLocationListener(this);

        // 设置定位条件
        LocationClientOption clientOption = new LocationClientOption();
        // 定位模式 分为高精度定位模式 低功耗定位模式 仅设备定位模式
        // 高精度定位模式：这种定位模式下，会同时使用网络定位和GPS定位，优先返回最高精度的定位结果；
        // 低功耗定位模式：这种定位模式下，不会使用GPS，只会使用网络定位（Wi-Fi和基站定位）
        // 仅用设备定位模式：这种定位模式下，不需要连接网络，只使用GPS进行定位，这种模式下不支持室内环境的定位
        clientOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        // coorType - 取值有3个：
        // 返回国测局经纬度坐标系：gcj02
        // 返回百度墨卡托坐标系 ：bd09
        // 返回百度经纬度坐标系 ：bd09ll
        clientOption.setCoorType("bd09ll");
        // 设置扫描间隔，单位是毫秒 当<1000(1s)时，定时定位无效
        clientOption.setScanSpan(0);
        // 可选，设置是否需要地址信息，默认不需要
        clientOption.setIsNeedAddress(true);
        // 可选，默认false,设置是否使用gps
        clientOption.setOpenGps(true);
        // 可选，默认false，设置是否当gps有效时按照1S 1次频率输出GPS结果
        clientOption.setLocationNotify(true);
        // 设置是否需要返回位置语义化信息，可以在BDLocation.getLocationDescribe()中得到数据，
        // ex:"在天安门附近"， 可以用作地址信息的补充
        clientOption.setIsNeedLocationDescribe(true);
        // //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        clientOption.setIgnoreKillProcess(true);
        // 可选，默认false，设置是否收集CRASH信息，默认收集
        clientOption.SetIgnoreCacheException(false);
        // 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        clientOption.setEnableSimulateGps(false);

        this.locationClient.setLocOption(clientOption);
        // 开始定位
        this.locationClient.start();

        this.baiduMap.setMyLocationEnabled(true);
    }

    /**
     * 定位成功回调该方法
     * @param bdLocation
     */
    @Override
    public void onReceiveLocation(BDLocation bdLocation) {

        if (bdLocation == null || this.baiduMap == null) {
            return;
        }
        Toast.makeText(this, bdLocation.getAddrStr(), Toast.LENGTH_LONG).show();

        // 定位成功之后，更新地图显示
        MyLocationData.Builder builder = new MyLocationData.Builder();
        // 设置定位数据的精度信息，单位：米
        builder.accuracy(1000 * 2);
        // 设置定位数据的方向信息
        builder.direction(50);
        // 纬度
        builder.latitude(bdLocation.getLatitude());
        // 经度
        builder.longitude(bdLocation.getLongitude());

        MyLocationData build = builder.build();

        // 设置当前定位图层所需要的数据
        this.baiduMap.setMyLocationData(build);

        // 设置视图模式
        MyLocationConfiguration configuration = new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.FOLLOWING, true, null);
        this.baiduMap.setMyLocationConfigeration(configuration);

        // 更新图层位置
        this.baiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(new LatLng(
                bdLocation.getLatitude(), bdLocation.getLongitude())), 300);

    }
}
