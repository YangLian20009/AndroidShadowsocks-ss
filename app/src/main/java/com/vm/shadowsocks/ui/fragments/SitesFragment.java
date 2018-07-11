package com.vm.shadowsocks.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.vm.shadowsocks.R;
import com.vm.shadowsocks.app.SocksApp;
import com.vm.shadowsocks.manager.SockConnectionCore;
import com.vm.shadowsocks.ui.WebActivity;
import com.vm.shadowsocks.utils.SockLogger;
import com.vondear.rxtools.view.RxToast;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Kirk-YL on 2018/5/26.
 */

public class SitesFragment extends BaseFragment implements View.OnClickListener{

    private View mView;
    private EditText etSsLink;
    private Button btSsConn;
    private EditText etWebLink;
    private Button btWebConn;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_sites, container, false);
        }
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SockConnectionCore.getInstance().vpnPreProxy();
        etSsLink=mView.findViewById(R.id.etSsLink);
        btSsConn=mView.findViewById(R.id.btSsConn);
        etWebLink=mView.findViewById(R.id.etWebLink);
        btWebConn=mView.findViewById(R.id.btWebConn);

        btSsConn.setOnClickListener(this);
        btWebConn.setOnClickListener(this);
        btWebConn.setClickable(false);
        btWebConn.setBackgroundColor(getResources().getColor(R.color.gray));
    }

    @Override
    public void onClick(View v) {
        if(v==btSsConn){
            if (!SocksApp.getSockVpnAllow()) {
                new MaterialDialog.Builder(getActivity())
                        .title("是否允许翻墙访问")
                        .content("注：此软用于提供访问国外查询资料工具使用为主，禁止使用此软件做违法犯罪等行为！！！")
                        .positiveText("确定")
                        .negativeText("取消")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                                conn();
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .canceledOnTouchOutside(false)
                        .show();
            }
        }
        else if(v==btWebConn){
            Intent intent=new Intent(getActivity(),WebActivity.class);
            intent.putExtra("URL",etWebLink.getText().toString());
            startActivity(intent);
        }
    }


    private void conn(){
        SockConnectionCore.getInstance().vpnConnection(getActivity(),etSsLink.getText().toString(), new SockConnectionCore.OnConnectionLog() {
            @Override
            public void onLog(String log) {
                Log.d("XXLOG","log="+log);
                SockLogger.log("log="+log);
            }

            @Override
            public void onStatus(String status, Boolean isRunning) {
                Log.d("XXLOG","log= state="+status );
                SockLogger.log("state="+status+",isRunning="+isRunning);
                RxToast.showToast(isRunning ? "连接成功，可以访问国外网站" : "连接失败，请重新连接");
                SocksApp.setSockVpnAllow(isRunning);
                onSockStatus(isRunning);
            }
        });
    }

    private void onSockStatus(boolean isRunning){
        if(isRunning){
            btWebConn.setClickable(true);
            btWebConn.setBackgroundColor(getResources().getColor(R.color.red));
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        SockConnectionCore.getInstance().vpnDisConnection();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
            if (resultCode == RESULT_OK) {
                conn();
            }
            return;
        }
    }
}
