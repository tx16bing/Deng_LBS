package com.deng.lbs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.SupportMapFragment;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;

/**
 * POI搜索附近
 * Author: Created by deng on 2016/7/8.
 * E-mail: tx16bing@163.com
 */
public class POISearchNearByActivity extends AppCompatActivity implements
        BDLocationListener, OnGetPoiSearchResultListener {

    private PoiSearch mPoiSearch;
    private EditText et_city;
    private EditText et_searchkey;
    private SupportMapFragment mapFragment;
    private BaiduMap baiduMap;
    private PoiOverlay poiOverlay;

    private int pageNum = 0;
    private LocationClient locationClient;
    private BDLocation location;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_poisearch_nearby);
        getMapFragment();
        initLocation();
        initPOI();
        initView();
    }

    private void getMapFragment() {
        this.mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        this.baiduMap = this.mapFragment.getBaiduMap();
    }

    /**
     * 初始化定位信息
     */
    private void initLocation() {
        this.locationClient = new LocationClient(getApplicationContext());
        this.locationClient.registerLocationListener(this);

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

        //  开启定位
        this.locationClient.start();

        this.baiduMap.setMyLocationEnabled(true);
    }

    /**
     * 初始化poi搜索
     */
    private void initPOI() {
        this.mPoiSearch = PoiSearch.newInstance();
        this.mPoiSearch.setOnGetPoiSearchResultListener(this);
    }

    private void initView() {
        this.et_searchkey = (EditText) findViewById(R.id.et_search_key);
    }

    /**
     * 搜索附近
     *
     * @param bt
     */
    public void searchNearBy(View bt) {
        searchNearBy(0);
    }

    public void poiSearch(View btn) {
        this.mPoiSearch.searchInCity((new PoiCitySearchOption())
                .city(this.et_city.getText().toString())
                .keyword(this.et_searchkey.getText().toString()).pageNum(this.pageNum));
    }

    /**
     * 下一组数据
     * @param btn
     */
    public void nextPagePoi(View btn) {
        this.pageNum++;
        searchNearBy(this.pageNum);
    }

    /**
     * 点击搜索附近
     * @param pageNum
     */
    private void searchNearBy(int pageNum) {
        // 清空图层
        this.baiduMap.clear();
        // 检索的配置参数类
        PoiNearbySearchOption nearbySearchOption = new PoiNearbySearchOption();
        // 检索关键字
        nearbySearchOption.keyword(this.et_searchkey.getText().toString());
        // 以当前位置为中心点，进行周边检索
        nearbySearchOption.location(new LatLng(this.location.getLatitude(), this.location
                .getLongitude()));
        // 设置检索的半径范围(m为单位)
        nearbySearchOption.radius(1000);
        //设置当前数据是第几页
        nearbySearchOption.pageNum(pageNum);
        //页大小（默认为10条数据）
        nearbySearchOption.pageCapacity(10);
        //附近检索
        this.mPoiSearch.searchNearby(nearbySearchOption);
    }

    /**
     * 定位成功回调该方法
     * @param location
     */
    @Override
    public void onReceiveLocation(BDLocation location) {
        if (location == null || this.baiduMap == null) {
            return;
        }
        this.location = location;
        Toast.makeText(this, location.getAddrStr(), Toast.LENGTH_LONG).show();

        // 定位成功之后，更新地图显示
        MyLocationData.Builder builder = new MyLocationData.Builder();
        // 设置定位数据的精度信息，单位：米
        builder.accuracy(1000 * 2);
        // 设置定位数据的方向信息
        builder.direction(50);
        // 纬度
        builder.latitude(location.getLatitude());
        // 经度
        builder.longitude(location.getLongitude());

        MyLocationData build = builder.build();
        // 设置当前定位图层所需要的数据
        this.baiduMap.setMyLocationData(build);

        // 设置视图模式
        MyLocationConfiguration configuration = new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.NORMAL, true, null);
        this.baiduMap.setMyLocationConfigeration(configuration);

        // 更新图层位置
        this.baiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(new LatLng(
                location.getLatitude(), location.getLongitude())), 300);
    }

    /**
     * poi搜索成功回调
     * @param result
     */
    @Override
    public void onGetPoiResult(PoiResult result) {

        this.baiduMap.clear();
        this.poiOverlay = new PoiOverlay(this.baiduMap);
        // 绑定图层点击事件
        this.baiduMap.setOnMarkerClickListener(this.poiOverlay);
        //根据result.getAllPoi().get(0).uid向服务器匹配数据
        //提示：将客户端所有的UID拼接成为字符串上传到服务器
        // 绑定POI检索数据
        this.poiOverlay.setData(result);
        // 添加到图层
        this.poiOverlay.addToMap();
        // 自适应
        this.poiOverlay.zoomToSpan();

        // 第一种方案
        // 第二种方案
        // 点击POI点，弹出地址
        this.poiOverlay.setOnPoiClickListener(new PoiOverlay.OnPoiClickListener() {

            @Override
            public boolean onPoiClick(PoiResult poiResult, int i) {
                // 返回的结果
                // 两种方案
                // 第一种：定义全局的poiOverlay
                // 第二种方案

                // 获取当前点击的POI
                PoiInfo poiInfo = poiResult.getAllPoi().get(i);

                Button button = new Button(POISearchNearByActivity.this);
                button.setText(poiInfo.name + "\r\n" + "地址：" + poiInfo.address
                        + "\r\n" + "电话:" + poiInfo.phoneNum);
                InfoWindow infoWindow = new InfoWindow(button,
                        poiInfo.location, -80);
                baiduMap.showInfoWindow(infoWindow);
                return false;
            }
        });
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }

    /**
     * 这个是第二种方案
     *
     * @author Dream
     *
     */
    class MyPoiOverlay extends PoiOverlay {

        public MyPoiOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public boolean onPoiClick(int i) {
            getPoiResult();
            return super.onPoiClick(i);
        }

    }
}
