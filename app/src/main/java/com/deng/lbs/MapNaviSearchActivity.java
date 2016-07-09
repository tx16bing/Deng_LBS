package com.deng.lbs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;

import java.util.List;

/**
 * 发起线路搜索页面
 * Author: Created by deng on 2016/7/9.
 * E-mail: tx16bing@163.com
 */
public class MapNaviSearchActivity extends AppCompatActivity implements
        OnGetPoiSearchResultListener, AdapterView.OnItemClickListener, View.OnClickListener {

    private PoiSearch poiSearch;
    private EditText et_search_key;
    private List<PoiInfo> allPoi;
    private ListView lv_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_search);
        initPOISearch();
        initView();
    }

    private void initPOISearch() {
        poiSearch = PoiSearch.newInstance();
        poiSearch.setOnGetPoiSearchResultListener(this);
    }

    private void initView() {
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getIntent().getStringExtra("text"));
        et_search_key = (EditText) findViewById(R.id.et_search_key);
        findViewById(R.id.bt_search).setOnClickListener(this);
        lv_address = (ListView) findViewById(R.id.lv_address);
        lv_address.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // 点击按钮要进行POI搜索
        // 发起POI搜索
        poiSearch.searchInCity(new PoiCitySearchOption().city("深圳市")
                .keyword(et_search_key.getText().toString()).pageNum(0));
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult arg0) {

    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult arg0) {

    }

    @Override
    public void onGetPoiResult(PoiResult result) {
        allPoi = result.getAllPoi();
        lv_address.setAdapter(new POISearchAdapter());
    }

    class POISearchAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return allPoi.size();
        }

        @Override
        public Object getItem(int position) {
            return allPoi.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(
                    R.layout.item_map_navigation_search, parent, false);
            TextView textView = (TextView) convertView
                    .findViewById(R.id.tv_name);
            textView.setText(allPoi.get(position).name);
            return convertView;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        // 点击某一个Item，获取地理信息返回算路页面
        PoiInfo poiInfo = allPoi.get(position);
        Intent data = new Intent();
        data.putExtra("name", poiInfo.name);
        data.putExtra("address", poiInfo.address);
        data.putExtra("latitude", poiInfo.location.latitude);
        data.putExtra("longitude", poiInfo.location.longitude);
        setResult(MapNaviMainActivity.MAP_NAVIGATION_RESULT_CODE, data);
        finish();
    }

}
