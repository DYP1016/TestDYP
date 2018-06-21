package com.example.dyp.testdyp.dbflow;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.dyp.testdyp.R;
import com.example.dyp.testdyp.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class DbFlowActivity extends BaseActivity {

    @BindView(R.id.lv_device)
    ListView lvDevice;
    @BindView(R.id.lv_control)
    ListView lvControl;

    private String[] controls = new String[]{
            "增加一个设备",
            "删除一个设备",
            "删除全部设备"
    };

    private List<Device> deviceList = new ArrayList<>();
    private DeviceAdapter deviceAdapter;

    /**
     * 加载页面layout的id
     */
    @Override
    public int getLayoutId() {
        return R.layout.activity_db_flow;
    }

    /**
     * 初始化页面布局
     *
     * @param savedInstanceState 恢复数据(正常恢复)
     */
    @Override
    public void initView(Bundle savedInstanceState) {

    }

    /**
     * 设置各种事件的监听器
     */
    @Override
    public void setListener() {
        lvControl.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, controls));
        deviceAdapter = new DeviceAdapter(this, deviceList);
        lvDevice.setAdapter(deviceAdapter);

        lvControl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: //增加设备
                        deviceAdd();
                        break;
                    case 1: //删除一个设备
                        deviceDelete();
                        break;
                    case 2:
                        deviceAllDelete();
                        break;
                }
            }
        });

        lvDevice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Device device = deviceList.get(position);
                final EditText etName = new EditText(DbFlowActivity.this);
                etName.setText(device.getName());
                AlertDialog.Builder builder = new AlertDialog.Builder(DbFlowActivity.this);
                builder.setTitle("修改设备名")
                        .setView(etName)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deviceModifyName(device, etName.getText().toString());
                            }
                        });
                builder.show();
            }
        });
    }

    /**
     * 业务逻辑处理，主要与后端交互
     */
    @Override
    public void processLogic() {
        queryDeviceList();
    }

    private void queryDeviceList() {
        deviceList.clear();
        deviceList.addAll(TestDatabase.getDeviceList());
        deviceAdapter.notifyDataSetChanged();
    }

    private void deviceAdd() {
        Device device = new Device();
        device.setName("Device" + (deviceList.size() + 1));
        device.setUid("UID000" + (deviceList.size() + 1));
        device.setPassword("1234");
        device.setUsername("admin");
        device.save();

        queryDeviceList();
    }

    private void deviceDelete() {
        if (deviceList.size() == 0) {
            toast("无设备");
            return;
        }
        //删除第一个设备
        deviceList.get(0).delete();

        queryDeviceList();
    }

    private void deviceAllDelete() {
        if (deviceList.size() == 0) {
            toast("无设备");
            return;
        }
        TestDatabase.deleteAllDevice();

        queryDeviceList();
    }

    private void deviceModifyName(Device device, String name) {
        device.setName(name);
        device.update();

        queryDeviceList();
    }
}
