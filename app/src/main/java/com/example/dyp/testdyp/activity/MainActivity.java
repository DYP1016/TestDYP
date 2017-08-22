package com.example.dyp.testdyp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.dyp.testdyp.R;
import com.example.dyp.testdyp.adapter.ViewHolderAdapter;
import com.example.dyp.testdyp.view.MyListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private MyListView listView;
    private ViewHolderAdapter adapter;
    private List<String> mData = new ArrayList<>();
    private List<Class> mStartActivity = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
    }

    @Override
    protected void initView() {
//        listView = new MyListView(this,10);
        listView = (MyListView) findViewById(R.id.main_listview);
        adapter = new ViewHolderAdapter(this, mData, mStartActivity);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void initData() {
        mData.add(0, "RxJava Test");
        mData.add(1, "Recycler Test");
        for (int i=0;i<20;i++){
            mData.add(String.valueOf(i));
        }
        mStartActivity.add(0, RxJavaActivity.class);
        mStartActivity.add(1, RecyclerViewActivity.class);
    }

    @Override
    protected void initListener() {

    }
}
