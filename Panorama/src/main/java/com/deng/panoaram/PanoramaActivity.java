package com.deng.panoaram;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.baidu.lbsapi.panoramaview.ImageMarker;
import com.baidu.lbsapi.panoramaview.OnTabMarkListener;
import com.baidu.lbsapi.panoramaview.PanoramaView;
import com.baidu.lbsapi.panoramaview.TextMarker;
import com.baidu.lbsapi.tools.Point;
import com.baidu.pano.platform.plugin.indooralbum.IndoorAlbumPlugin;

/**
 * 全景图显示
 * Author: Created by deng on 2016/7/11.
 * E-mail: tx16bing@163.com
 */
public class PanoramaActivity extends AppCompatActivity{

    private int type;
    private PanoramaView mPanoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panorama);
        this.type = getIntent().getIntExtra("type", MainActivity.PANORAMA_PID);
        initPanoramaView();
        initPanoramaType(this.type);
    }

    private void initPanoramaView() {
        this.mPanoView = (PanoramaView) findViewById(R.id.panorama);
    }

    /**
     * 根据不同的type进行不同的操作
     * @param type
     */
    private void initPanoramaType(int type) {

        switch (type){
            case MainActivity.PANORAMA_PID:
                panoramaTypePid();
                break;
            case MainActivity.PANORAMA_CONTROLLER:
                panoramaController();
                break;
            case MainActivity.PANORAMA_OVERLAY:
//                panoramaOverlay();
//                findViewById(R.id.bt_add_marker).setVisibility(View.VISIBLE);
                break;
            case MainActivity.PANORAMA_ALBUM:
                panoramaAlbum();
                break;

            default:
                break;
        }

    }

    /**
     * 内置相册
     */
    private void panoramaAlbum(){
        //初始化相册
        IndoorAlbumPlugin.getInstance().init();
        this.mPanoView.setPanoramaImageLevel(PanoramaView.ImageDefinition.ImageDefinitionHigh);
        this.mPanoView.setPanoramaByUid("28e700f15aae5418085cb3a7", PanoramaView.PANOTYPE_INTERIOR);
        //显示相册
        this.mPanoView.setIndoorAlbumVisible();
    }

    /**
     * 根据pid方式显示全景图
     */
    private void panoramaTypePid() {
        this.mPanoView.setPanorama("0100220000130817164838355J5");
    }

    /**
     * 设置全景图不同的参数和属性
     */
    private void panoramaController() {
        this.mPanoView.setPanoramaPitch(0);
        // 设置全景图的缩放级别，默认缩放级别为2级
        // 缩放级别一共分为5级，分别为1-5，随着级别的增大清晰度不断提高
        this.mPanoView.setPanoramaZoomLevel(5);
        this.mPanoView.setPanoramaZoomLevel(5);
        //设置全景图片的显示级别
        //根据枚举类ImageDefinition来设置清晰级别
        //较低清晰度 ImageDefinationLow
        //中等清晰度 ImageDefinationMiddle
        //较高清晰度 ImageDefinationHigh
        this.mPanoView.setPanoramaImageLevel(PanoramaView.ImageDefinition.ImageDefinitionHigh);
        //true显示街景箭头；false不显示街景箭头
        this.mPanoView.setShowTopoLink(true);
        this.mPanoView.setArrowTextureByBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.street_arrow));
        this.mPanoView.setPanorama("0100220000130817164838355J5");
    }

    /**
     * 添加图层
     */
    private void panoramaOverlay() {
        this.mPanoView.setPanoramaImageLevel(PanoramaView.ImageDefinition.ImageDefinitionHigh);
        this.mPanoView.setPanorama("0100220000130817164838355J5");
    }

    public void addImageMarker(View bt){
        this.mPanoView.removeAllMarker();

        this.mPanoView.setShowTopoLink(true);
        this.mPanoView.setArrowTextureByBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.street_arrow));

        ImageMarker imageMarker = new ImageMarker();
        //覆盖物经纬度坐标
        imageMarker.setMarkerPosition(new Point(116.427116, 39.929718));
        //覆盖物的高度
        imageMarker.setMarkerHeight(50);
        imageMarker.setMarker(getResources().getDrawable(R.drawable.icon_marka));
        imageMarker.setOnTabMarkListener(new OnTabMarkListener() {

            @Override
            public void onTab() {
                Toast.makeText(PanoramaActivity.this, "点击了覆盖物", Toast.LENGTH_LONG).show();
            }
        });

        this.mPanoView.addMarker(imageMarker);
    }

    public void addTextMarker(View bt){
        TextMarker textMarker = new TextMarker();
        textMarker.setMarkerPosition(new Point(116.409766, 39.911808));
        textMarker.setText("妹子帮我做的，今天晚上吃了一顿美餐！");
        textMarker.setFontSize(14);
        textMarker.setFontColor(Color.GREEN);
        textMarker.setPadding(10, 20, 15, 25);
        textMarker.setMarkerHeight(50);
        textMarker.setBgColor(Color.BLUE);
        this.mPanoView.addMarker(textMarker);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.mPanoView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.mPanoView.onResume();
    }

    @Override
    protected void onDestroy() {
        this.mPanoView.destroy();
        super.onDestroy();
    }
}
