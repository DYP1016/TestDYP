package com.example.dyp.testdyp.base;

import android.os.Bundle;
import android.os.PersistableBundle;

/**
 * IActivity 接口
 */
public interface IActivity {

    /**
     * 加载页面layout的id
     */
    int getLayoutId();

    /**
     * 初始化页面布局
     *
     * @param savedInstanceState 恢复数据(正常恢复)
     */
    void initView(Bundle savedInstanceState);

    /**
     * 设置各种事件的监听器
     */
    void setListener();

    /**
     * 业务逻辑处理，主要与后端交互
     */
    void processLogic();
}
