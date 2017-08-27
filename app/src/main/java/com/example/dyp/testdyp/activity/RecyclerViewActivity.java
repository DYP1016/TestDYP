package com.example.dyp.testdyp.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.dyp.testdyp.R;
import com.example.dyp.testdyp.adapter.StagggeredAdapter;
import com.example.dyp.testdyp.adapter.TestAdapter;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private TestAdapter adapter;
    private StagggeredAdapter staggeredGridAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<String > dataset;

    private static final int LIST_ADD = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        initView();
        initData();
        initView();
        initListener();

        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        //添加水平分割线
//        recyclerView.addItemDecoration(new com.example.dyp.testdyp.utils.DividerItemDecoration(this,
//                com.example.dyp.testdyp.utils.DividerItemDecoration.VERTICAL_LIST));


    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case LIST_ADD:
                    adapter.addData(1);
                    staggeredGridAdapter.addData(1);
                    break;
                default:
                    break;
            }
            return false;
        }
    });

    public void addTest(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.sendEmptyMessage(LIST_ADD);

                }
            }
        }).start();
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
        adapter = new TestAdapter(this,dataset);
        staggeredGridAdapter = new StagggeredAdapter(this,dataset);

    }

    @Override
    protected void initListener() {
        adapter.setOnItemClickListener(new TestAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                toast("点击了 "+position);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                adapter.removeData(position);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
//        menu.add(Menu.NONE,0,0,"add");
//        menu.add(Menu.NONE,1,0,"remove");
        getMenuInflater().inflate(R.menu.recycler_view_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.recycler_menu_listView:
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                break;
            case R.id.recycler_menu_gridView:
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new GridLayoutManager(this,3));
                break;
            case R.id.recycler_menu_sta_gridView:
                recyclerView.setAdapter(staggeredGridAdapter);
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3,
                        StaggeredGridLayoutManager.VERTICAL));
                break;
            case R.id.recycler_menu_add:
                adapter.addData(1);
                staggeredGridAdapter.addData(1);
                break;
            case R.id.recycler_menu_remove:
                adapter.removeData(1);
                staggeredGridAdapter.removeData(1);
            default:
                break;
        }

        return super.onOptionsItemSelected(item);

    }
}
