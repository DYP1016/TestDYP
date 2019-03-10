package com.example.dyp.testdyp.dbflow;


import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

@Database(name = TestDatabase.NAME, version = TestDatabase.VERSION)
public class TestDatabase {
    public static final String NAME = "TestDatabase";

    public static final int VERSION = 1;

    public static List<Device> getDeviceList() {
        return SQLite.select().from(Device.class).queryList();
    }

    public static Device getDevice(String uid) {
        return SQLite.select().from(Device.class).where(Device_Table._uid.eq(uid)).querySingle();
    }

    public static void deleteAllDevice(){
        SQLite.delete(Device.class).execute();
    }
}

