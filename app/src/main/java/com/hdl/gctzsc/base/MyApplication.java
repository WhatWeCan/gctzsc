package com.hdl.gctzsc.base;

import android.app.Application;
import android.content.Context;

import cn.bmob.v3.Bmob;

/**
 * 自定义MyApplication
 */

public class MyApplication extends Application {

    private static Context mAppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = this;
        Bmob.initialize(this, "8ecc3741f43a54cf8e62b10092f7eb4c");
    }

    /**
     * 提供ApplicationContext
     *
     * @return
     */
    public static Context getAppContext() {
        return mAppContext;
    }

}
