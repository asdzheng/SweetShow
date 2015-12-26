package com.asdzheng.sweetshow.imageloaders;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

/**
 * Created by asdzheng on 2015/12/26.
 */
public interface ImageLoader {
    void configure(Context context);

    void load(Context context, String imageUrl, ImageView imageView);

    void load(Context context, String imageUrl, ImageView imageView, @DrawableRes int drawable);

    void prefetch(Context context, String... imageUrls);
}
