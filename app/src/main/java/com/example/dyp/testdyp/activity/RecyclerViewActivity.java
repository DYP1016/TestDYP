package com.example.dyp.testdyp.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.dyp.testdyp.R;
import com.example.dyp.testdyp.adapter.TestAdapter;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<String > dataset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        initView();
        initData();

        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new TestAdapter(this,dataset);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new com.example.dyp.testdyp.utils.DividerItemDecoration(this,
                com.example.dyp.testdyp.utils.DividerItemDecoration.VERTICAL_LIST));
    }


    @Override
    protected void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    }

    @Override
    protected void initData() {
        dataset = new ArrayList<>();
        for (int i =0; i <= 50 ; i++){
            dataset.add("item "+i);
        }
    }

    @Override
    protected void initListener() {

    }
}
