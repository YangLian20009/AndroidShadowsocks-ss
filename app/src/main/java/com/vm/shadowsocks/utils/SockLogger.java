package com.vm.shadowsocks.utils;

import com.orhanobut.logger.Logger;
import com.vm.shadowsocks.BuildConfig;

/**
 * Created by Kirk-YL on 2018/5/27.
 */

public class SockLogger {

    public static void log(Object o) {
        if (o == null) return;
//        if (BuildConfig.DEBUG) Logger.d("SockLog", o.toString());
        Logger.d("SockLog", o.toString());
    }
}
