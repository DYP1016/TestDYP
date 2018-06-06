package com.example.dyp.testdyp.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ListView;

import com.example.dyp.testdyp.R;
import com.example.dyp.testdyp.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WifiActivity extends BaseActivity {

    @BindView(R.id.lv_wifi)
    ListView lvWifi;
    @BindView(R.id.srl_main)
    SwipeRefreshLayout srlMain;

    /**
     * 加载页面layout的id
     */
    @Override
    public int getLayoutId() {
        return R.layout.activity_wifi;
    }

    /**
     * 初始化页面布局
     *
     * @param savedInstanceState 恢复数据(正常恢复)
     */
    @Override
    public void initView(Bundle savedInstanceState) {

    }

    /**
     * 设置各种事件的监听器
     */
    @Override
    public void setListener() {

    }

    /**
     * 业务逻辑处理，主要与后端交互
     */
    @Override
    public void processLogic() {

    }
}
