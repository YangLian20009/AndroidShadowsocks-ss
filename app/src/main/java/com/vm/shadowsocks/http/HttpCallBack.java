package com.vm.shadowsocks.http;

/**
 * Created by Kirk-YL on 2018/5/26.
 */

public abstract class HttpCallBack<T> {

    public abstract void onSuccess(T data);

    public abstract void onFail(String msg);
}
