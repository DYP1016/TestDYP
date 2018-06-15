package com.example.dyp.testdyp.wifi.connect;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dyp.testdyp.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class WifiListAdapter extends BaseAdapter {

    protected Context mContext;
    protected List<ScanResult> mDatas;
    private LayoutInflater inflater;

    public WifiListAdapter(Context mContext, List<ScanResult> mDatas) {

        this.mContext = mContext;
        this.mDatas = mDatas;
        this.inflater = LayoutInflater.from(mContext);
        sortByLevel((ArrayList<ScanResult>) mDatas);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 定义ViewHolder
        ViewHolder viewHolder;
        if (convertView == null) {
            // convertView = mContext.
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_wifi_info, null);
            viewHolder.tvSSID = (TextView) convertView
                    .findViewById(R.id.tvSSID);
            viewHolder.tvSign = (TextView) convertView
                    .findViewById(R.id.tvSign);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvSSID.setText(mDatas.get(position).SSID);
        viewHolder.tvSign.setText(String.valueOf(Math.abs(mDatas.get(position).level)));
        return convertView;
    }

    // 根据wifi的强弱排序
    private void sortByLevel(ArrayList<ScanResult> list) {

        Collections.sort(list, new Comparator<ScanResult>() {

            @Override
            public int compare(ScanResult lhs, ScanResult rhs) {
                return rhs.level - lhs.level;
            }
        });
    }

    public void notifyData(List<ScanResult> mDatas) {
        this.mDatas = mDatas;
        sortByLevel((ArrayList<ScanResult>) mDatas);
        notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView tvSSID, tvSign;
    }

}
