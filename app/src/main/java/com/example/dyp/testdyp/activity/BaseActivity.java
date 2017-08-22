package com.example.dyp.testdyp.activity;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by dyp on 2017/8/22.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected abstract void initView();
    protected abstract void initData();
    protected abstract void initListener();
}
