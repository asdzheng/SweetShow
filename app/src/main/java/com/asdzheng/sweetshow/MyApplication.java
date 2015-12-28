package com.asdzheng.sweetshow;

import android.app.Application;
import android.content.Context;

/**
 * Created by asdzheng on 2015/12/10.
 */
public class MyApplication extends Application {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this.getApplicationContext();
    }
}
