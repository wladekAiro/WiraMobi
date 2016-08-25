package com.example.wladek.wira.utils;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by wladek on 8/15/16.
 */
public class WiraApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
