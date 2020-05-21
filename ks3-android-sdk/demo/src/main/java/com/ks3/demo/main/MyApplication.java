package com.ks3.demo.main;

import android.app.Application;

import com.blankj.utilcode.util.Utils;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
    }
}
