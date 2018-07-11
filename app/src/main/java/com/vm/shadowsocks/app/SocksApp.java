package com.vm.shadowsocks.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.vondear.rxtools.RxTool;

/**
 * Created by Kirk-YL on 2018/5/24.
 */

public class SocksApp extends MultiDexApplication {

    private static SocksApp mSockApp;
    private static boolean mSockVpnAllow = false;

    public static SocksApp getApplication() {
        return mSockApp;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mSockApp = this;
        RxTool.init(this);
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    public static boolean getSockVpnAllow() {
        return mSockVpnAllow;
    }

    public static void setSockVpnAllow(boolean mSockVpnAllow) {
        SocksApp.mSockVpnAllow = mSockVpnAllow;
    }
}
