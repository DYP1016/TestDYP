package com.example.dyp.testdyp.lottie;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.dyp.testdyp.R;

public class LottieActivity extends AppCompatActivity {
    private static final String[] item = {
            "开始",
            "停止",
            "暂停",
            "恢复",
            "示例: 开关",
            "示例: 加载框",
            "示例: 城市",
            "示例: 对讲",
            "示例: 渐变色背景",
            "示例: 二维码扫描",
            "示例: 鲸鱼",
            "示例: 加载框2",
            "示例: 头像",
            "示例: 特效",
            "示例: WIFI",
    };

    private LottieAnimationView lavTest;
    private ListView lvControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottie);

        lavTest = findViewById(R.id.lav_test);
        lvControl = findViewById(R.id.lv_control);

        lavTest.setImageAssetsFolder("lottie/images");

        lvControl.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, item));
        lvControl.setOnItemClickListener((parent, view, position, id) -> {
            switch (item[position]) {
                case "开始":
                    lavTest.playAnimation();
                    break;
                case "停止":
                    lavTest.cancelAnimation();
                    break;
                case "暂停":
                    lavTest.pauseAnimation();
                    break;
                case "恢复":
                    lavTest.resumeAnimation();
                    break;
                case "示例: 加载框":
                    loadRaw(R.raw.loading_dialogue);
                    break;
                case "示例: 城市":
                    loadRaw(R.raw.smart_city_blue_lines);
                    break;
                case "示例: 对讲":
                    loadRaw(R.raw.voice);
                    break;
                case "示例: 头像":
                    loadRaw(R.raw.flowing_avatar);
                    break;
                case "示例: 渐变色背景":
                    loadRaw(R.raw.gradient_animated_background);
                    break;
                case "示例: 二维码扫描":
                    loadRaw(R.raw.scan_qr_code_success);
                    break;
                case "示例: 鲸鱼":
                    loadRaw(R.raw.empty_status);
                    break;
                case "示例: 加载框2":
                    loadRaw(R.raw.spinner_loading);
                    break;
                case "示例: 特效":
                    loadRaw(R.raw.rejection);
                    break;
                case "示例: WIFI":
                    loadRaw(R.raw.no_internet_connection);
                    break;
                case "示例: 开关":
                    loadRaw(R.raw.onoff_switch);
                    break;
            }
        });
    }

    private void loadRaw(int raw) {
        lavTest.setAnimation(raw);
        lavTest.playAnimation();
    }
}
