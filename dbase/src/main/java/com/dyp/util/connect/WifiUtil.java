package com.dyp.util.connect;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.dyp.util.LogUtil;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * WIFI配置工具类
 * 用于连接指定WIFI
 * Created by dyp on 2018/1/26.
 */

public class WifiUtil {
    public static final int LOAD_SUCCESS = 0;
    public static final int LOAD_FAIL = 1;
    private static final int TIME_OUT = 10;
    private static final String WIFI_STATE_COMPLETED = "COMPLETED"; //手机连接上WIFI
    private static final String WIFI_STATE_DISCONNECTED = "DISCONNECTED"; //手机断开了WIFI
    private static final String WIFI_STATE_INVALID_DISCONNECTED = "00:00:00:00:00:00"; //无效的状态

    private String targetSSID; //连接的目标WIFI
    private Disposable disposable; //连接设备超时用
    private WifiConnect wifiConnect;
    private WifiManager wifiManager;
    private volatile boolean isCheckConnect = false;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private CallBack callBack;
    private WifiInfo mWifiInfo; // 连接设备之前的WiFi信息；
    private BroadcastReceiver mReceiver;
    private Context context;
    private ConnectivityManager connectivityManager;

    public static final int CONNECT_FAIL_TIME_OUT = -1;
    public static final int CONNECT_FAIL_NOT_BELONG = -2;

    private WifiUtil() {
    }

    public static WifiUtil getInstance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static final WifiUtil instance = new WifiUtil();
    }

    public void init(Context context) {
        wifiConnect = new WifiConnect(context.getApplicationContext());
        wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    public void stopConnect() {
        compositeDisposable.clear();
        if (context != null) {
            context.unregisterReceiver(mReceiver);
            context = null;
        }
    }

    public void connectTargetWifi(Context context, final ScanResult scanResult, final String password, final boolean isSaved, CallBack callBack) {
        WifiConnect.WifiCipherType wifiCipherType = WifiConnect.getSecurity(scanResult);
        String ssid = GetRealSsid(scanResult.SSID);
        connectTargetWifi(context, ssid, password, wifiCipherType, isSaved, callBack);
    }

    public void connectTargetWifi(Context context, final String ssid, final String password, final boolean isSaved, CallBack callBack) {
        // TODO: 2018/1/26 当前判断加密方式为密码为空时为未加密,密码不为空时为WPA2加密
        WifiConnect.WifiCipherType type;
        if (TextUtils.isEmpty(password)) {
            type = WifiConnect.WifiCipherType.WIFICIPHER_NOPASS;
        } else {
            type = WifiConnect.WifiCipherType.WIFICIPHER_WPA;
        }
        connectTargetWifi(context, ssid, password, type, isSaved, callBack);
    }

    public void connectTargetWifi(Context context, final String ssid, final String password, final WifiConnect.WifiCipherType wifiCipherType, final boolean isSaved, CallBack callBack) {
        this.context = context;
        this.callBack = callBack;
        this.targetSSID = ssid;
        this.isCheckConnect = false;
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        LogUtil.i("切换WIFI, 目标SSID: " + ssid + " 加密方式: " + wifiCipherType.name());

        initReceiver();
        //超时设置
        Observable.timer(TIME_OUT, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        checkConnect();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        //开始连接WIFI
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {

                Boolean mIsDevWifiConnected = false;
                for (int TIMES_NUM = 0; TIMES_NUM < 10; TIMES_NUM++) {
                    LogUtil.d("connect Times =" + TIMES_NUM);
                    if (isSaved) {
                        mIsDevWifiConnected = wifiConnect.connectSaved(ssid);
                    } else {
                        mIsDevWifiConnected = wifiConnect.connect(ssid, password, wifiCipherType, isSaved);
                    }
                    LogUtil.d("Phone connect wifi success(true) or not(false) :"
                            + mIsDevWifiConnected);
                    if (mIsDevWifiConnected) {
                        e.onNext(true);
                        e.onComplete();
                        return;
                    }
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                }
                e.onNext(false);
                e.onComplete();
            }
        })
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            if (mWifiInfo != null && mWifiInfo.getSSID() != null && (GetRealSsid(mWifiInfo.getSSID()).equals(targetSSID) && //连接的WIFI是目标WIFI
                                    mWifiInfo.getSupplicantState().toString().equals(WIFI_STATE_COMPLETED))) { //当前已经连接上了WIFI)
                                checkConnect();
                            }
                        } else {
                            checkConnect();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        checkConnect();
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void checkConnect() {
        LogUtil.i("checkConnect");
        if (isCheckConnect) {
            LogUtil.i("fail : checked");
            return;
        }
        isCheckConnect = true;
        if (disposable != null) {
            disposable.dispose();
            disposable = null;
        }

        if (TextUtils.isEmpty(targetSSID) || mWifiInfo == null) {
            LogUtil.i("fail : target is null");
            myHandler.sendEmptyMessage(LOAD_FAIL);
            return;
        }

        LogUtil.i("target : " + targetSSID + " current : " + GetRealSsid(mWifiInfo.getSSID()));
        if (GetRealSsid(mWifiInfo.getSSID()).equals(targetSSID) && //连接的WIFI是目标WIFI
                mWifiInfo.getSupplicantState().toString().equals(WIFI_STATE_COMPLETED)) { //当前已经连接上了WIFI
            myHandler.sendEmptyMessage(LOAD_SUCCESS);
            return;
        }
        myHandler.sendEmptyMessage(LOAD_FAIL);
    }

    private void initReceiver() {
        LogUtil.i("initReceiver");
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mWifiInfo = wifiManager.getConnectionInfo();
                if (null != mWifiInfo && null != mWifiInfo.getSSID()) {
                    String info = "getSSID()=" + mWifiInfo.getSSID() + "\n"
                            + "getBSSID()=" + mWifiInfo.getBSSID() + "\n"
                            + "getHiddenSSID()=" + mWifiInfo.getHiddenSSID() + "\n"
                            + "getLinkSpeed()=" + mWifiInfo.getLinkSpeed() + "\n"
                            + "getMacAddress()=" + mWifiInfo.getMacAddress() + "\n"
                            + "getNetworkId()=" + mWifiInfo.getNetworkId() + "\n"
                            + "getRssi()=" + mWifiInfo.getRssi() + "\n"
                            + "getSupplicantState()=" + mWifiInfo.getSupplicantState() + "\n"
                            + "getDetailedStateOf()=" + WifiInfo.getDetailedStateOf(mWifiInfo.getSupplicantState());
                    LogUtil.i(info);

                    if (mWifiInfo.getBSSID() == null || mWifiInfo.getBSSID().equals(WIFI_STATE_INVALID_DISCONNECTED)) {
                        return;
                    }

                    if (!GetRealSsid(mWifiInfo.getSSID()).equals(targetSSID)) {
                        return;
                    }

                    if (!mWifiInfo.getSupplicantState().toString().equals(WIFI_STATE_COMPLETED)) {
                        return;
                    }

                    if (WifiInfo.getDetailedStateOf(mWifiInfo.getSupplicantState()).toString().equals(WIFI_STATE_DISCONNECTED)) {
                        return;
                    }

                    checkConnect();
                }
            }

        };

        //="android.net.wifi.STATE_CHANGE"  监听wifi状态的变化
        IntentFilter filter = new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        context.registerReceiver(mReceiver, filter);
    }

    // 根据Android的版本判断获取到的SSID是否有双引号
    public static String GetRealSsid(String ssid) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            // android系统 >= 4.2.2，获取到的ssid有双引号，
            // android系统 < 4.2.2,获取到的ssid没有双引号
            if (ssid.startsWith("\"") && ssid.endsWith("\"")) {
                ssid = ssid.substring(1, ssid.length() - 1);
            }
        }
        LogUtil.d("ssid = " + ssid);
        return ssid;
    }

    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        private final WeakReference<WifiUtil> weakReference;

        private MyHandler(WifiUtil instance) {
            weakReference = new WeakReference<WifiUtil>(instance);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            WifiUtil instance = weakReference.get();
            if (instance != null) {
                instance.stopConnect();
                switch (msg.what) {
                    case LOAD_SUCCESS:
                        instance.callBack.onConnect();
                        break;
                    case LOAD_FAIL:
                        instance.callBack.onDisConnect(
                                instance.wifiConnect.isCurrentWifiIsControl() ? CONNECT_FAIL_TIME_OUT : CONNECT_FAIL_NOT_BELONG
                        );
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public interface CallBack {
        //连接目标WIFI成功
        void onConnect();

        //连接目标WIFI失败
        void onDisConnect(int code);
    }
}
