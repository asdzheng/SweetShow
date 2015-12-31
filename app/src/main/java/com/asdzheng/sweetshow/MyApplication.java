package com.asdzheng.sweetshow;

import android.app.Application;
import android.content.Context;

import com.asdzheng.sweetshow.ui.drawable.Drawables;

/**
 * Created by asdzheng on 2015/12/10.
 */
public class MyApplication extends Application {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this.getApplicationContext();
        Drawables.init(getResources());
    }
}
