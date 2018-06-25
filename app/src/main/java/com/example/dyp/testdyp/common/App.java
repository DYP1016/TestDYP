package com.example.dyp.testdyp.common;

import android.app.Application;

import com.example.dyp.testdyp.dbflow.SQLCipherHelperImpl;
import com.example.dyp.testdyp.dbflow.TestDatabase;
import com.example.dyp.testdyp.utils.WifiUtil;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.raizlabs.android.dbflow.config.DatabaseConfig;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.database.DatabaseHelperListener;
import com.raizlabs.android.dbflow.structure.database.OpenHelper;

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
        FlowConfig flowConfig = FlowConfig.builder(this)
                .addDatabaseConfig(new DatabaseConfig.Builder(TestDatabase.class)
                        .openHelper(new DatabaseConfig.OpenHelperCreator() {
                            @Override
                            public OpenHelper createHelper(DatabaseDefinition databaseDefinition, DatabaseHelperListener helperListener) {
                                return new SQLCipherHelperImpl(databaseDefinition, helperListener);
                            }
                        })
                        .build())
                .build();
        FlowManager.init(flowConfig);
    }
}
