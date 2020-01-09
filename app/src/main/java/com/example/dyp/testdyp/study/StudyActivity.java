package com.example.dyp.testdyp.study;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.dyp.testdyp.R;
import com.example.dyp.testdyp.base.BaseActivity;

import butterknife.BindView;

public class StudyActivity extends BaseActivity {
    @BindView(R.id.lv_main)
    ListView lvMain;

    private final static String[] ITEMS = {
            "View的事件传递"
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_study;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void setListener() {
        lvMain.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ITEMS));
        lvMain.setOnItemClickListener((parent, view, position, id) -> {
            switch (position){
                case 0:
                    startActivity(StudyViewActivity.class);
                    break;
            }
        });
    }

    @Override
    public void processLogic() {

    }
}
