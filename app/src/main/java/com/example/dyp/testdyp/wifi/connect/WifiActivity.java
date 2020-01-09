package com.example.dyp.testdyp.wifi.connect;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.dyp.testdyp.R;
import com.example.dyp.testdyp.base.BaseActivity;
import com.dyp.util.LogUtil;
import com.dyp.util.connect.WifiConnect;
import com.dyp.util.connect.WifiUtil;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class WifiActivity extends BaseActivity {

    @BindView(R.id.lv_wifi)
    ListView lvWifi;
    @BindView(R.id.srl_main)
    SwipeRefreshLayout srlMain;
    @BindView(R.id.et_ssid)
    EditText etSsid;
    @BindView(R.id.bt_direct)
    Button btDirect;

    private List<ScanResult> mDatas;
    private WifiListAdapter mAdapter;
    private WifiUtil.CallBack callBack;

    /**
     * 加载页面layout的id
     */
    @Override
    public int getLayoutId() {
        return R.layout.activity_wifi;
    }

    @OnClick({R.id.bt_direct})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_direct:
                String ssid = etSsid.getText().toString();
                if (TextUtils.isEmpty(ssid)) {
                    toast("SSID不能为空");
                } else {
                    showPasswordDialog(etSsid.getText().toString(), null);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 初始化页面布局
     *
     * @param savedInstanceState 恢复数据(正常恢复)
     */
    @Override
    public void initView(Bundle savedInstanceState) {
        setTitle("WifiConnect");
    }

    /**
     * 设置各种事件的监听器
     */
    @Override
    public void setListener() {
        srlMain.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryWifiList();
            }
        });
        lvWifi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ScanResult scanResult = mDatas.get(position);
                WifiConnect.WifiCipherType wifiCipherType = WifiConnect.getSecurity(scanResult);
                if (wifiCipherType == WifiConnect.WifiCipherType.WIFICIPHER_NOPASS) {
                    toastL("连接: ssid=" + WifiUtil.GetRealSsid(scanResult.SSID));
                    showLoading();
                    WifiUtil.getInstance().connectTargetWifi(WifiActivity.this, scanResult, "", false, callBack);
                } else {
                    showPasswordDialog(scanResult.SSID, scanResult);
                }
            }
        });
        callBack = new WifiUtil.CallBack() {
            @Override
            public void onConnect() {
                hideLoading();
                toast("连接目标WIFI成功");
            }

            @Override
            public void onDisConnect(int code) {
                hideLoading();
                String info;
                switch (code) {
                    case WifiUtil.CONNECT_FAIL_NOT_BELONG:
                        info = "该WIFI不属于本App";
                        break;
                    case WifiUtil.CONNECT_FAIL_TIME_OUT:
                        info = "连接超时(可能密码错误)";
                        break;
                    default:
                        info = "连接失败(未知)";
                        break;
                }
                toast("连接目标WIFI失败: " + info);
            }
        };
    }

    /**
     * 业务逻辑处理，主要与后端交互
     */
    @Override
    public void processLogic() {
        mDatas = new ArrayList<>();
        mAdapter = new WifiListAdapter(this, mDatas);
        lvWifi.setAdapter(mAdapter);

        queryWifiList();

        test();
    }

    private void queryWifiList() {
        srlMain.setRefreshing(true);
        Observable.timer(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        srlMain.setRefreshing(false);
                        if (mDatas == null) {
                            return;
                        }
                        LogUtil.i("mDatas size = " + mDatas.size());
                        mDatas = getWifiListDatas();
                        mAdapter.notifyData(mDatas);
                    }

                    @Override
                    public void onError(Throwable e) {
                        toast("错误: " + e.getMessage());
                        srlMain.setRefreshing(false);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private List<ScanResult> getWifiListDatas() {
        WifiManager mWifiManager = (WifiManager) this.getApplication().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        return mWifiManager != null ? mWifiManager.getScanResults() : null;
    }

    private void showPasswordDialog(final String ssid, @Nullable final ScanResult scanResult) {
        final EditText etPassword = new EditText(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(ssid)
                .setMessage("请输入密码")
                .setView(etPassword)
                .setPositiveButton("连接", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showLoading();
                        String password = etPassword.getText().toString();
                        toastL("连接: ssid=" + ssid + " password=" + password);
                        if (scanResult != null) {
                            WifiUtil.getInstance().connectTargetWifi(WifiActivity.this, scanResult,
                                    password, false, callBack);
                        } else {
                            WifiUtil.getInstance().connectTargetWifi(WifiActivity.this, ssid,
                                    password, false, callBack);
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setCancelable(false)
                .show();
        dialog.show();
    }

    private void test() {
        LogUtil.e("start");
        try {
            DatagramSocket socket = new DatagramSocket();
            socket.setSoTimeout(20000);
            socket.setBroadcast(true);
            socket.bind(new InetSocketAddress(5001));

            byte[] buffer = new byte[200];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            socket.receive(packet);

            LogUtil.e("ret = " + packet.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
