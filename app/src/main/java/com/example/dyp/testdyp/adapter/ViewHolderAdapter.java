package com.example.dyp.testdyp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dyp.testdyp.R;
import com.example.dyp.testdyp.activity.RxJavaActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * main页面用
 * Created by dyp on 2017/8/20.
 */

public class ViewHolderAdapter extends BaseAdapter {
    private List<String> mData;
    private List<Class<RxJavaActivity>> mStartActivity = new ArrayList<>();
    private LayoutInflater inflater;
    private Context context;

    public ViewHolderAdapter(Context context, List<String> mData, List<Class<RxJavaActivity>> mStartActivity) {
        this.mData = mData;
        this.mStartActivity = mStartActivity;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.simple_list_view, null);
            holder.title = (TextView) convertView.findViewById(R.id.list_text_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.title.setText(mData.get(position));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    context.startActivity(new Intent(context, mStartActivity.get(position)));
                }catch (Exception e){
                    Toast.makeText(context, "该Activity不存在", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return convertView;
    }

    public final class ViewHolder {
        public TextView title;
    }
}
