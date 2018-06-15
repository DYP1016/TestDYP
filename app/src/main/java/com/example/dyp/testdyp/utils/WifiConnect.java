package com.example.dyp.testdyp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.SystemClock;

import java.util.List;

public class WifiConnect {

    private WifiManager mWifiManager;

    private boolean currentWifiIsControl;

    public WifiConnect(Context context) {
        mWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    public int checkState() {
        return mWifiManager.getWifiState();
    }

    public void startScan() {
        mWifiManager.startScan();
    }

    public WifiInfo getNowWifiConnect() {
        return mWifiManager.getConnectionInfo();
    }

    public String getCurrentSSID(Context context) {
        WifiInfo wifiInfo = ((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).getConnectionInfo();
        return wifiInfo.getSSID().substring(1, wifiInfo.getSSID().length() - 1);
    }

    public boolean connectSaved(String ssid) {
        WifiConfiguration tempConfig = searchExistedSameWifiConfig(ssid);
        if (tempConfig != null) {
            LogUtil.i("connect saved ssid " + ssid);
            return mWifiManager.enableNetwork(tempConfig.networkId, true);
        }
        LogUtil.i("can not find saved ssid " + ssid);
        return false;
    }

    public boolean connect(String findSSID, String password, WifiCipherType paramWifiCipherType, boolean isSaved) {
        while (mWifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING) {
            try {
                // 为了避免程序一直while循环，让它睡个100毫秒检测……
                Thread.sleep(100);
            } catch (InterruptedException ie) {
            }
        }

        WifiConfiguration wifiConfig = CreateWifiInfo(findSSID, password, paramWifiCipherType);
        if (wifiConfig == null) {
            LogUtil.d("wifiConfig is null!");
            return false;
        }
        WifiConfiguration tempConfig = searchExistedSameWifiConfig(findSSID);
        if (tempConfig != null) {
            LogUtil.i("this wifi was added");
            if (Build.VERSION.SDK_INT < 23) {
                mWifiManager.removeNetwork(tempConfig.networkId);
            } else {
                currentWifiIsControl = mWifiManager.removeNetwork(tempConfig.networkId);
                LogUtil.i("is my wifi : " + currentWifiIsControl);
                //若不是本App控制的WIFI且有密码，直接返回失败
                return (isSaved || paramWifiCipherType == WifiCipherType.WIFICIPHER_NOPASS) && connect(tempConfig.networkId);
            }
        } else {
            currentWifiIsControl = true;
        }

        int netID = mWifiManager.addNetwork(wifiConfig);
        if (netID != -1) {
            currentWifiIsControl = true;
            return connect(netID);
        }
        return false;
    }

    public boolean isCurrentWifiIsControl() {
        return currentWifiIsControl;
    }

    //To do : 参考controlcam的，与http://www.cnblogs.com/zhuqiang/p/3566686.html实例中的方法有些区别；
    private WifiConfiguration CreateWifiInfo(String findSSID, String password,
                                             WifiCipherType paramWifiCipherType) {
        WifiConfiguration localWifiConfiguration = new WifiConfiguration();
        localWifiConfiguration.allowedAuthAlgorithms.clear();
        localWifiConfiguration.allowedGroupCiphers.clear();
        localWifiConfiguration.allowedKeyManagement.clear();
        localWifiConfiguration.allowedPairwiseCiphers.clear();
        localWifiConfiguration.allowedProtocols.clear();
        localWifiConfiguration.SSID = ("\"" + findSSID + "\"");
        if (paramWifiCipherType == WifiCipherType.WIFICIPHER_NOPASS) {
            localWifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
//            localWifiConfiguration.wepKeys[0] = "\"\"";
//            localWifiConfiguration.wepTxKeyIndex = 0;
        } else if (paramWifiCipherType == WifiCipherType.WIFICIPHER_WEP) {
            localWifiConfiguration.preSharedKey = ("\"" + password + "\"");
            localWifiConfiguration.hiddenSSID = true;
            localWifiConfiguration.allowedAuthAlgorithms.set(1);
            localWifiConfiguration.allowedGroupCiphers.set(3);
            localWifiConfiguration.allowedGroupCiphers.set(2);
            localWifiConfiguration.allowedGroupCiphers.set(0);
            localWifiConfiguration.allowedGroupCiphers.set(1);
            localWifiConfiguration.allowedKeyManagement.set(0);
            localWifiConfiguration.wepTxKeyIndex = 0;
        } else if (paramWifiCipherType == WifiCipherType.WIFICIPHER_WPA) {
            localWifiConfiguration.preSharedKey = ("\"" + password + "\"");
            localWifiConfiguration.hiddenSSID = false;
            localWifiConfiguration.allowedKeyManagement.set(1);
        }
        return localWifiConfiguration;
    }

    private WifiConfiguration searchExistedSameWifiConfig(String findSSID) {
        List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();
        if (null != existingConfigs) {
            for (WifiConfiguration existingConfig : existingConfigs) {
                LogUtil.d(existingConfig.SSID);
                if (existingConfig.SSID.equals("\"" + findSSID + "\"")) {
                    return existingConfig;
                }
            }
        }

        return null;
    }

    private void stopOtherConnect() {
        List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();
        if (existingConfigs == null) {
            LogUtil.e("stop other connect fail");
            return;
        }
        for (WifiConfiguration configuration : existingConfigs) {
            mWifiManager.disableNetwork(configuration.networkId);
        }
    }

    private boolean openWifi() {
        boolean bool = false;
        if (!mWifiManager.isWifiEnabled()) {
            bool = mWifiManager.setWifiEnabled(true);
        }
        return bool;
    }

    public static WifiCipherType getSecurity(ScanResult paramScanResult) {
        if (paramScanResult == null) {
            return WifiCipherType.WIFICIPHER_INVALID;
        } else if (paramScanResult.capabilities.contains("WEP")) {
            return WifiCipherType.WIFICIPHER_WEP;
        } else if (paramScanResult.capabilities.contains("PSK")) {
            return WifiCipherType.WIFICIPHER_WPA;
        }
        return WifiCipherType.WIFICIPHER_NOPASS;
    }

    public static enum WifiCipherType {
        WIFICIPHER_WEP, WIFICIPHER_WPA, WIFICIPHER_NOPASS, WIFICIPHER_INVALID;
        /*static
        {
	      WifiCipherType WIFICIPHER_NOPASS = new WifiCipherType("WIFICIPHER_NOPASS", 2);
	      WifiCipherType WIFICIPHER_INVALID = new WifiCipherType("WIFICIPHER_INVALID", 3);
	      WifiCipherType[] arrayOfWifiCipherType = new WifiCipherType[4];
	      arrayOfWifiCipherType[0] = WIFICIPHER_WEP;
	      arrayOfWifiCipherType[1] = WIFICIPHER_WPA;
	      arrayOfWifiCipherType[2] = WIFICIPHER_NOPASS;
	      arrayOfWifiCipherType[3] = WIFICIPHER_INVALID;
	    }*/
    }

    private boolean connect(int networkId) {
        return mWifiManager.enableNetwork(networkId, true);
    }
}
