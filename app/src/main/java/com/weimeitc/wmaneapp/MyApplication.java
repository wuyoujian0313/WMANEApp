package com.weimeitc.wmaneapp;

import android.app.Application;
import android.content.Context;

/**
 * Created by wuyoujian on 17/3/19.
 */

public class MyApplication extends Application {
    private  static Context context;
    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
