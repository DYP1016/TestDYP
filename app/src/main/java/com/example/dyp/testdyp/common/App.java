package com.example.dyp.testdyp.common;

import android.app.Application;

import com.example.dyp.testdyp.utils.WifiUtil;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.raizlabs.android.dbflow.config.FlowManager;

public class App extends Application {
    private static App app;

    public static App getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        Logger.addLogAdapter(new AndroidLogAdapter());
        WifiUtil.getInstance().init(this);
        FlowManager.init(this);
    }
}
