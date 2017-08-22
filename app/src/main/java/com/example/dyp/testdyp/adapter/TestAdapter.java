package com.example.dyp.testdyp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dyp.testdyp.R;

import java.util.List;

/**
 * Recycler 用
 * Created by dyp on 2017/8/22.
 */

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.MyViewHolder> {
    private Context context;
    private List<String> dataset;
    private LayoutInflater inflater;

    public TestAdapter(Context context,List<String> dataset){
        this.context = context;
        this.dataset = dataset;
        this.inflater = LayoutInflater.from(context);
    }

    //将布局转化为view并传递给viewHolder
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_item_view,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    //建立ViewHolder与视图中数据的关联
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.textView.setText(dataset.get(position));
    }

    //获取item的数目
    @Override
    public int getItemCount() {
        return dataset.size();
    }

    //自定义ViewHolder，持有item所有的控件
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.recycler_view_text);
        }
    }
}
