package com.example.dyp.testdyp.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import com.example.dyp.testdyp.R;
import com.example.dyp.testdyp.adapter.ViewHolderAdapter;
import com.example.dyp.testdyp.base.BaseActivity;
import com.example.dyp.testdyp.dbflow.DbFlowActivity;
import com.example.dyp.testdyp.utils.LogUtils;
import com.example.dyp.testdyp.view.MyListView;
import com.example.dyp.testdyp.wifi.connect.WifiActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity {
    @BindView(R.id.mlv_main)
    MyListView mlvMain;
    private ViewHolderAdapter adapter;
    private List<String> mData = new ArrayList<>();
    private List<Class> mStartActivity = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        LogUtils.e("start");
        super.onCreate(savedInstanceState, persistentState);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.e("start");
    }

    /**
     * 加载页面layout的id
     */
    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    /**
     * 初始化页面布局
     *
     * @param savedInstanceState 恢复数据(正常恢复)
     */
    @Override
    public void initView(Bundle savedInstanceState) {
        adapter = new ViewHolderAdapter(this, mData, mStartActivity);
        mlvMain.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    /**
     * 设置各种事件的监听器
     */
    @Override
    public void setListener() {
        LogUtils.e("测试");
        mData.add("RxJava Test");
        mStartActivity.add(RxJavaActivity.class);

        mData.add("Recycler Test");
        mStartActivity.add(RecyclerViewActivity.class);

        mData.add("WIFI 切换");
        mStartActivity.add(WifiActivity.class);

        mData.add("DbFlow Test");
        mStartActivity.add(DbFlowActivity.class);
    }

    /**
     * 业务逻辑处理，主要与后端交互
     */
    @Override
    public void processLogic() {

    }
}
