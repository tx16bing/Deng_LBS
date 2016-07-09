package com.deng.lbs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
 * poi搜索地点界面
 * Author: Created by deng on 2016/7/8.
 * E-mail: tx16bing@163.com
 */
public class MapNavigationSearchActivity extends AppCompatActivity implements OnGetPoiSearchResultListener {

    private TextView titleTv;
    private EditText searchKeyEt;
    private PoiSearch mPoiSearch;
    private List<PoiInfo> allAddr;
    private ListView addressLv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_search);
        initPOI();
        initView();

    }

    private void initPOI() {
        this.mPoiSearch = PoiSearch.newInstance();
        this.mPoiSearch.setOnGetPoiSearchResultListener(this);
    }

    private void initView() {
        String hint = getIntent().getStringExtra("hint");
        this.titleTv = (TextView) findViewById(R.id.tv_title);
        this.titleTv.setText(hint);
        this.searchKeyEt = (EditText) findViewById(R.id.et_search_key);
        this.addressLv = (ListView) findViewById(R.id.lv_address);
    }

    /**
     * 点击搜索
     * @param bt
     */
    public void search(View bt) {
        this.mPoiSearch.searchInCity((new PoiCitySearchOption()).city("深圳市")
                .keyword(this.searchKeyEt.getText().toString()).pageNum(0));
    }

    /**
     * 搜索成功回调
     * @param poiResult
     */
    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        this.allAddr = poiResult.getAllPoi();
        if (this.allAddr == null){
            return;
        }
        this.addressLv.setAdapter(new POIAdapter(this));
        this.addressLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PoiInfo poiInfo = allAddr.get(position);
                Intent data = new Intent();
                MapNavigationMainActivity.MyPlan plan = new MapNavigationMainActivity.MyPlan();
                plan.name = poiInfo.name;
                plan.address = poiInfo.address;
                plan.latitude = poiInfo.location.latitude;
                plan.longitude = poiInfo.location.longitude;
                data.putExtra("plan", plan);
                setResult(MapNavigationMainActivity.RESULT_CODE_LOCATION, data);
                finish();
            }
        });

    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }

    class POIAdapter extends BaseAdapter {

        private Context context;

        public POIAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return allAddr.size();
        }

        @Override
        public Object getItem(int position) {
            return allAddr.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.item_map_navigation_search, parent,false);
            PoiInfo poiAddrInfo = allAddr.get(position);
            TextView textView = (TextView) convertView.findViewById(R.id.tv_name);
            textView.setText(poiAddrInfo.name);
            return convertView;
        }

    }
}
