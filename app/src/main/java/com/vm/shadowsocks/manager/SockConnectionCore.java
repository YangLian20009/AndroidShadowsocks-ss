package com.vm.shadowsocks.manager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.vm.shadowsocks.R;
import com.vm.shadowsocks.app.SocksApp;
import com.vm.shadowsocks.core.AppProxyManager;
import com.vm.shadowsocks.core.LocalVpnService;
import com.vondear.rxtools.view.RxToast;

import java.util.Calendar;

/**
 * Created by Kirk-YL on 2018/5/27.
 */

public class SockConnectionCore {
    private static final SockConnectionCore ourInstance = new SockConnectionCore();
    private LocalVpnService.onStatusChangedListener mChangeListener=new LocalVpnService.onStatusChangedListener() {
        @Override
        public void onStatusChanged(String status, Boolean isRunning) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            String logString = String.format("[%1$02d:%2$02d:%3$02d] %4$s\n",
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    calendar.get(Calendar.SECOND),
                    status);
            mOnConnectionLog.onStatus(status,isRunning);
        }

        @Override
        public void onLogReceived(String logString) {
            mOnConnectionLog.onLog(logString);
        }
    };
    private String mSsSockUrl;
    private OnConnectionLog mOnConnectionLog = new OnConnectionLog() {

        @Override
        public void onLog(String log) {

        }

        @Override
        public void onStatus(String status, Boolean isRunning) {

        }
    };


    public static SockConnectionCore getInstance() {
        return ourInstance;
    }

    private SockConnectionCore() {
    }

    public void vpnPreProxy(){
        if (AppProxyManager.isLollipopOrAbove){
            new AppProxyManager(SocksApp.getApplication());
        }
    }

    public void vpnConnection(FragmentActivity context, String ssSockUrl, OnConnectionLog onConnectionLog) {
        this.mSsSockUrl = ssSockUrl;
        this.mOnConnectionLog = onConnectionLog;
        setProxyUrl(mSsSockUrl);
        Intent intent = LocalVpnService.prepare(context);
        if (intent == null) {
            startVPNService(context);
        }
        else {
            context.startActivityForResult(intent,200);
        }
    }

    void setProxyUrl(String ProxyUrl) {
        SharedPreferences preferences = SocksApp.getApplication().getSharedPreferences("shadowsocksProxyUrl", SocksApp.getApplication().MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("CONFIG_URL_KEY", ProxyUrl);
        editor.apply();
    }

    public void vpnDisConnection() {
        LocalVpnService.removeOnStatusChangedListener(mChangeListener);
        boolean isAllowConnect = false;
        SocksApp.setSockVpnAllow(isAllowConnect);
        LocalVpnService.IsRunning = isAllowConnect;
    }


    boolean isSSUrl(String url){
        return url.startsWith("ss://");
    }

    boolean isValidUrl(String url) {
        try {
            if (url == null || url.isEmpty())
                return false;

            if (url.startsWith("ss://")) {//file path
                return true;
            } else { //url
                Uri uri = Uri.parse(url);
                if (!"http".equals(uri.getScheme()) && !"https".equals(uri.getScheme()))
                    return false;
                if (uri.getHost() == null)
                    return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    private void startVPNService(Context context) {
        if (!isSSUrl(mSsSockUrl)) {
            RxToast.showToast(R.string.err_invalid_url);
            return;
        }
        mOnConnectionLog.onLog("starting...");
        LocalVpnService.ProxyUrl = mSsSockUrl;
        LocalVpnService.addOnStatusChangedListener(mChangeListener);
        SocksApp.getApplication().startService(new Intent(context, LocalVpnService.class));
    }

    private void stopVPNService(){
        SocksApp.getApplication().stopService(new Intent(SocksApp.getApplication(), LocalVpnService.class));
    }


    public static interface OnConnectionLog {
        void onLog(String log);
        void onStatus(String status, Boolean isRunning);
    }


//    public static class SockConst{
//        public static final String SS_SOCK_URL="ss://YWVzLTI1Ni1jZmI6WUxzbm9hbncxMzIxNTlANDUuMzIuMTE0LjQ5OjExMjk";
//    }
}
