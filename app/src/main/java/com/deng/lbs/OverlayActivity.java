package com.deng.lbs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

/**
 * 图层，覆盖物
 * Author: Created by deng on 2016/7/8.
 * E-mail: tx16bing@163.com
 */
public class OverlayActivity extends AppCompatActivity implements OnGetGeoCoderResultListener {

    private MapView mapView;
    private BaiduMap baiduMap;
    private GeoCoder geoCoder;
    private Marker marker;

    private EditText longitude;
    private EditText latitude;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overlay);
        initView();
    }

    private void initView() {
        this.mapView = (MapView) findViewById(R.id.map_view);
        this.baiduMap = this.mapView.getMap();
        this.geoCoder = GeoCoder.newInstance();
        this.geoCoder.setOnGetGeoCodeResultListener(this);

        this.longitude = (EditText) findViewById(R.id.longitude);
        this.latitude = (EditText) findViewById(R.id.latitude);
    }

    /**
     * 在指定位置添加图层覆盖物
     * @param btn
     */
    public void location(View btn){

        // 获取经纬度
        String lon = this.longitude.getText().toString().trim();
        String lat = this.latitude.getText().toString().trim();

        Toast.makeText(this, "lon : " + lon + " lat: " + lat, Toast.LENGTH_SHORT).show();

        // 指定经纬度
        LatLng latLng = new LatLng(Double.parseDouble(lon), Double.parseDouble(lat));

        // 创建覆盖物
        MarkerOptions markerOptions = new MarkerOptions();
        // 指定覆盖物的位置
        markerOptions.position(latLng);

        // 创建图标对象
        BitmapDescriptor fromResource = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
        // 给图层绑定图标
        markerOptions.icon(fromResource);
        // 设置级别关系
        markerOptions.zIndex(9);
        // 设置允许拖拽覆盖物
        markerOptions.draggable(true);

        // 绑定覆盖物
        this.baiduMap.addOverlay(markerOptions);

        // 点击某个覆盖物然后回调
//		baiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
//
//			private boolean isShow;
//			private InfoWindow infoWindow;
//
//			@Override
//			public boolean onMarkerClick(Marker mk) {
//				marker = mk;
//				//反Geo获取地理信息
//				geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(mk.getPosition()));
//				return true;
//			}
//		});

        // 拖拽监听
        this.baiduMap.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {

            }

            @Override
            public void onMarkerDragStart(Marker marker) {

            }
        });

    }

    //获取这个地理位置
    //反Geo编码－－－根据经纬度获取地理信息(获取地理位置)
    //Geo编码－－根据地理信息获取经纬度

    public void delete(View btn){
        this.baiduMap.clear();
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
        // 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        this.mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        this.mapView.onDestroy();
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {

        Button button = new Button(this);
        button.setBackgroundResource(R.drawable.popup);

        // 获取地理信息
        button.setText(result.getAddress());

        //给我们的InfoWindow添加点击监听有两种方案
        //第一种方案：直接给我们View添加
        //第二种方案：给InfoWindow添加点击监听
        //推测：百度地图源码
//		InfoWindow infoWindow = new InfoWindow(button , marker.getPosition(), -80);

        // 创建图标对象
        BitmapDescriptor fromResource = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
        InfoWindow infoWindow = new InfoWindow(fromResource, this.marker.getPosition(), -80,  new InfoWindow.OnInfoWindowClickListener(){

            @Override
            public void onInfoWindowClick() {
                // 点击信息窗口的处理事件
            }
        });

        //第一种方式：给Button添加监听也可以
        //第二种方式：给InfoWindow添加监听

        this.baiduMap.showInfoWindow(infoWindow);

    }
}
