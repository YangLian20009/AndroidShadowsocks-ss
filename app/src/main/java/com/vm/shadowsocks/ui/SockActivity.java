package com.vm.shadowsocks.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.vm.shadowsocks.R;
import com.vm.shadowsocks.ui.fragments.SitesFragment;

/**
 * Created by Kirk-YL on 2018/5/26.
 */

public class SockActivity extends BaseFragmentActivity {

    private SitesFragment sitesFragment=new SitesFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sock);
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,sitesFragment).commitNowAllowingStateLoss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        sitesFragment.onActivityResult(requestCode,resultCode,data);
    }
}
