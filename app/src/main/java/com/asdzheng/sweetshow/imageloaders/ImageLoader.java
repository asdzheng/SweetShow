package com.asdzheng.sweetshow.imageloaders;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import java.io.IOException;

/**
 * Created by asdzheng on 2015/12/26.
 */
public interface ImageLoader {
    void configure(Context context);

    void load(Context context, String imageUrl, ImageView imageView);

    void load(Context context, String imageUrl, ImageView imageView, @DrawableRes int drawable);

    void prefetch(Context context, String... imageUrls);

    void fetchImageForSize(Context context, final ShowImageLoader.AspectRatios ratios, final String... imageUrls);

    void pause(Context context);

    void resume(Context context);

    Bitmap getBitmapFromCache(Context context, String key) ;
}
