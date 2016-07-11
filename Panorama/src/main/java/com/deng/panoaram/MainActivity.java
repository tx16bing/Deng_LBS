package com.deng.panoaram;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * 全景图显示控制列表
 * Author: Created by deng on 2016/7/11.
 * E-mail: tx16bing@163.com
 */
public class MainActivity extends AppCompatActivity {

    // 根据pid方式显示全景图
    public static final int PANORAMA_PID = 0;

    // 根据全景图控制
    public static final int PANORAMA_CONTROLLER = 1;

    // 显示全景图图层
    public static final int PANORAMA_OVERLAY = 2;

    // 显示全景图内置相册
    public static final int PANORAMA_ALBUM = 3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void panorama(View bt) {
        startPanoramaActivity(PANORAMA_PID);
    }

    public void panoramaController(View bt) {
        startPanoramaActivity(PANORAMA_CONTROLLER);
    }

    public void panoramaOverlay(View bt) {
        startPanoramaActivity(PANORAMA_OVERLAY);
    }

    public void panoramaAlbum(View bt) {
        startPanoramaActivity(PANORAMA_ALBUM);
    }

    private void startPanoramaActivity(int type) {
        Intent intent = new Intent(this, PanoramaActivity.class);
        intent.putExtra("type", type);
        startActivity(intent);
    }
}
