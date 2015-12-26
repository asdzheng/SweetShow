package com.asdzheng.sweetshow;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by asdzheng on 2015/12/10.
 */
public class MyApplication extends Application {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        ImageLoaderConfiguration configuration =  ImageLoaderConfiguration.createDefault(this);
        ImageLoader.getInstance().init(configuration);

        context = this.getApplicationContext();
    }
}
