package com.example.dyp.testdyp.dbflow;

import com.example.dyp.testdyp.utils.LogUtil;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.sqlcipher.SQLCipherOpenHelper;
import com.raizlabs.android.dbflow.structure.database.DatabaseHelperListener;

import java.util.Arrays;

public class SQLCipherHelperImpl extends SQLCipherOpenHelper {
    public SQLCipherHelperImpl(DatabaseDefinition databaseDefinition, DatabaseHelperListener listener) {
        super(databaseDefinition, listener);
    }

    /**
     * @return The SQLCipher secret for opening this database.
     */
    @Override
    protected String getCipherSecret() {
        byte[] key = new byte[32];
        Arrays.fill(key, (byte) 0);
        String input = new String(key);
        LogUtil.i("key = " + Arrays.toString(key));
        LogUtil.i("input = " + input);
        return input;
    }
}
