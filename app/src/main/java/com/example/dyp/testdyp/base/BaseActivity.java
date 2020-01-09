package com.example.dyp.testdyp.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.Toast;

import com.example.dyp.testdyp.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by dyp on 2017/8/22.
 */

public abstract class BaseActivity extends AppCompatActivity implements IActivity {
    public static final String BASE_INTENT = "BASE_INTENT";
    protected final String TAG = this.getClass().getSimpleName();
    protected ProgressDialog mProgressDialog;
    private Context context;

    private Unbinder unbinder;
    private Toast toast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        if (getLayoutId() > 0) {
            setContentView(getLayoutId());
            unbinder = ButterKnife.bind(this);
        }
        initView(savedInstanceState);
        setListener();
        processLogic();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null && unbinder != Unbinder.EMPTY) {
            unbinder.unbind();
            unbinder = null;
        }
        context = null;
    }

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

    /**
     * 页面跳转
     */
    public void startActivity(Class<? extends Activity> clazz) {
        startActivity(clazz, null);
    }

    public void startActivity(Class<? extends Activity> clazz, Bundle bundle) {
        startActivity(clazz, bundle, false);
    }

    public void startActivity(Class<? extends Activity> clazz, Bundle bundle, boolean isFinish) {
        Intent intent = new Intent();
        intent.setClass(this, clazz);
        if (bundle != null) {
            intent.putExtra(BASE_INTENT, bundle);
        }
        startActivity(intent);
        if (isFinish) {
            finish();
        }
    }

    public void showLoading() {
        if (null == this.mProgressDialog) {
            ProgressDialog progressDialog = new ProgressDialog(this, R.style.CustomDialog);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(true);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            this.mProgressDialog = progressDialog;
        }
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
            mProgressDialog.setContentView(R.layout.publico_custom_progress_dlg);
        }
    }

    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
