package com.example.dyp.testdyp.study;

import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

import com.dyp.util.LogUtil;
import com.example.dyp.testdyp.R;
import com.example.dyp.testdyp.base.BaseActivity;

import butterknife.BindView;

public class StudyViewActivity extends BaseActivity {
    @BindView(R.id.tv_test)
    TextView tvTest;

    @Override
    public int getLayoutId() {
        return R.layout.activity_study_view;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void setListener() {
        tvTest.setOnTouchListener((v, event) -> {
            LogUtil.i("onTouch: " + event.getAction());
            return false;
        });
    }

    @Override
    public void processLogic() {

    }
}
