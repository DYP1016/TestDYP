package com.example.dyp.testdyp.activity;

import android.view.Gravity;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


/**
 * Created by dyp on 2017/8/22.
 */

public abstract class BaseActivity extends AppCompatActivity {
    private Toast toast;

    protected abstract void initView();
    protected abstract void initData();
    protected abstract void initListener();

    public void toast(String msg) {
        if (null == msg) {
            return;
        }
        if (null == toast)
            toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        else
            toast.setText(msg);
        toast.show();
    }
    public void toast(String msg, int margin) {
        if (null == msg) {
            return;
        }
        if (null == toast)
            toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        else
            toast.setText(msg);
        toast.setGravity(Gravity.BOTTOM, 0, margin);
        toast.show();
    }

    public void toastL(String msg) {
        if (null == msg) {
            return;
        }
        if (null == toast)
            toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        else
            toast.setText(msg);
        toast.show();
    }
}
