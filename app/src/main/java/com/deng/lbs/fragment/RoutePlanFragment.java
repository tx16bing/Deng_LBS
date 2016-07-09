package com.deng.lbs.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mapapi.map.Text;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.deng.lbs.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 线路规划的路线
 * Author: Created by deng on 2016/7/8.
 * E-mail: tx16bing@163.com
 */
public class RoutePlanFragment extends BaseFragment{

    private final List<TransitRouteLine> lines;
    private final List<String> lineStrList;

    private RoutePlanAdapter planAdapter;
    private ListView listView;

    public RoutePlanFragment() {
        this.lines = new ArrayList<TransitRouteLine>();
        this.lineStrList = new ArrayList<String>();
    }

    /**
     * 设置路线
     * @param lines
     */
    public void setLines(List<TransitRouteLine> lines){
        this.lines.clear();
        this.lines.addAll(lines);
        this.lineStrList.clear();

        // 使用StringBuffer拼接每个路段
        StringBuffer sb = new StringBuffer();
        // 遍历路线
        int size = lines.size();
        for (int i = 0; i < size; i++){
            sb.append("线路").append(i+1).append("\r\n");
            // 获取途径过程
            TransitRouteLine transitRouteLine = this.lines.get(i);
            // 获取当前线路路段列表
            List<TransitRouteLine.TransitStep> allStep = transitRouteLine.getAllStep();
            for (TransitRouteLine.TransitStep transitStep : allStep){
                // 拼接路段
                sb.append(transitStep.getInstructions()).append("-->");
            }
            // 删除多余箭头(一共三个符号-->，所以需要执行三次)
            sb.deleteCharAt(sb.length()-1);
            sb.deleteCharAt(sb.length()-1);
            sb.deleteCharAt(sb.length()-1);
            sb.append("\r\n");
            sb.append("大约需要").append(transitRouteLine.getDuration()/60).append("分钟");
            this.lineStrList.add(sb.toString());
            sb.delete(0, sb.length());
        }

    }

    @Override
    public int getContentView() {
        return R.layout.fragment_routeplan;
    }

    @Override
    public void initContentView(View contentView) {
        this.listView = (ListView) contentView.findViewById(R.id.lv_routeplan);
    }

    @Override
    public void initData() {

        if (this.planAdapter == null){
            this.planAdapter = new RoutePlanAdapter();
            this.listView.setAdapter(this.planAdapter);
        }else{
            refreshData();
        }

    }

    @Override
    public void refreshData() {
        this.planAdapter.notifyDataSetChanged();
    }

    class RoutePlanAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return lineStrList.size();
        }

        @Override
        public Object getItem(int position) {
            return lineStrList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            if (convertView == null){
                holder = new ViewHolder();
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_routeplan, parent, false);
                holder.routePlan = (TextView) convertView.findViewById(R.id.tv_route_plan);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            holder.routePlan.setText(lineStrList.get(position));

            return convertView;
        }

        class ViewHolder {
            TextView routePlan;
        }
    }

}
