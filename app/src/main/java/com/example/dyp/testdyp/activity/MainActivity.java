package com.example.dyp.testdyp.activity;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;

import com.dyp.util.LogUtil;
import com.example.dyp.testdyp.R;
import com.example.dyp.testdyp.adapter.ViewHolderAdapter;
import com.example.dyp.testdyp.base.BaseActivity;
import com.example.dyp.testdyp.dbflow.DbFlowActivity;
import com.example.dyp.testdyp.lottie.LottieActivity;
import com.example.dyp.testdyp.recycle.RecyclerView2Activity;
import com.example.dyp.testdyp.study.StudyActivity;
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
        LogUtil.e("start");
        super.onCreate(savedInstanceState, persistentState);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.e("start");
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
        LogUtil.e("测试");
        mData.add("RxJava Test");
        mStartActivity.add(RxJavaActivity.class);

        mData.add("Recycler Test");
        mStartActivity.add(RecyclerViewActivity.class);

        mData.add("WIFI 切换");
        mStartActivity.add(WifiActivity.class);

        mData.add("DbFlow Test");
        mStartActivity.add(DbFlowActivity.class);

        mData.add("学习");
        mStartActivity.add(StudyActivity.class);

        mData.add("翻页RecyclerView");
        mStartActivity.add(RecyclerView2Activity.class);

        mData.add("Lottie");
        mStartActivity.add(LottieActivity.class);
    }

    /**
     * 业务逻辑处理，主要与后端交互
     */
    @Override
    public void processLogic() {

    }
}
