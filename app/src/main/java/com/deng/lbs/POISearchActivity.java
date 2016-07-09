package com.deng.lbs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.SupportMapFragment;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;

/**
 * POI搜索
 * Author: Created by deng on 2016/7/8.
 * E-mail: tx16bing@163.com
 */
public class POISearchActivity extends AppCompatActivity implements OnGetPoiSearchResultListener {

    private SupportMapFragment mapFragment;
    private PoiSearch poiSearch;
    private BaiduMap baiduMap;
    private PoiOverlay poiOverlay;

    private EditText cityEt;
    private EditText keyEt;
    private int pageNum = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poisearch);
        getMapFragment();
        initPOI();
        initView();
    }

    private void getMapFragment() {

        this.mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        this.baiduMap = mapFragment.getBaiduMap();

    }

    private void initPOI() {

        this.poiSearch = PoiSearch.newInstance();
        this.poiSearch.setOnGetPoiSearchResultListener(this);

    }

    private void initView() {

        this.cityEt = (EditText) findViewById(R.id.et_city);
        this.keyEt = (EditText) findViewById(R.id.et_searchkey);

    }

    /**
     * 进行poi搜索
     * @param btn
     */
    public void poiSearch(View btn){
        this.poiSearch.searchInCity(new PoiCitySearchOption().city(this.cityEt.getText().toString())
        .keyword(this.keyEt.getText().toString())
        .pageNum(this.pageNum));
    }

    public void nextPagePoi(View btn){
        this.pageNum++;
        this.poiSearch.searchInCity(new PoiCitySearchOption().city(this.cityEt.getText().toString())
                .keyword(this.keyEt.getText().toString())
                .pageNum(this.pageNum));
    }

    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        this.baiduMap.clear();
        this.poiOverlay = new PoiOverlay(this.baiduMap);
        // 绑定图层点击事件
        this.baiduMap.setOnMarkerClickListener(this.poiOverlay);
        this.poiOverlay.setData(poiResult);
        // 添加到图层
        this.poiOverlay.addToMap();
        // 自适应
        this.poiOverlay.zoomToSpan();

        //第一种方案
        //第二种方案
        //点击POI点，弹出地址
        this.poiOverlay.setOnPoiClickListener(new PoiOverlay.OnPoiClickListener() {
            @Override
            public boolean onPoiClick(PoiResult poiResult, int i) {
                //返回的结果,两种方案
                //第一种：定义全局的poiOverlay
                //第二种方案

                //获取当前点击的POI
                PoiInfo poiInfo = poiResult.getAllPoi().get(i);

                Button button = new Button(POISearchActivity.this);
                button.setText(poiInfo.name + "\r\n" + "地址：" + poiInfo.address + "\r\n" + "电话：" + poiInfo.phoneNum);
                InfoWindow infoWindow = new InfoWindow(button, poiInfo.location, -80);
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
     */
    class MyPoiOverlay extends PoiOverlay{
        /**
         * 构造函数
         *
         * @param baiduMap 该 PoiOverlay 引用的 BaiduMap 对象
         */
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
