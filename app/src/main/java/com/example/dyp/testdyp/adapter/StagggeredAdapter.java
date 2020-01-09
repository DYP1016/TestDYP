package com.example.dyp.testdyp.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dyp.testdyp.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by dyp on 2017/8/23.
 */

public class StagggeredAdapter extends RecyclerView.Adapter<StagggeredAdapter.MyViewHolder>{
    private Context context;
    private List<String> datas;
    private List<Integer> itemHighs;
    private LayoutInflater inflater;

    public StagggeredAdapter(Context context,List<String> datas){
        this.context = context;
        this.datas = datas;
        this.inflater = LayoutInflater.from(context);

        itemHighs = new ArrayList<>();
        for (int i=0;i<datas.size();i++){
            itemHighs.add((int)(100 + Math.random()*300));
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_item_view,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.textView.setText(datas.get(position));
        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
        params.height = itemHighs.get(position);
        holder.itemView.setLayoutParams(params);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    //添加一个元素
    public void addData(int pos){
        datas.add(pos,"insert one");
        notifyItemInserted(pos);
    }

    //删除一个元素
    public void removeData(int pos){
        datas.remove(pos);
        notifyItemRemoved(pos);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.recycler_view_text);
        }
    }
}
