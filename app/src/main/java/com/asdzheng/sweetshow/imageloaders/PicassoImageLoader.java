package com.asdzheng.sweetshow.imageloaders;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

/**
 * Created by asdzheng on 2015/12/26.
 */
public class PicassoImageLoader implements ImageLoader {

    private static final long PICASSO_DISK_CACHE_SIZE = 104857600;

    @Override
    public void configure(final Context context) {
        Picasso.setSingletonInstance(new Picasso.Builder(context.getApplicationContext()).downloader(new OkHttpDownloader(context.getApplicationContext(), PICASSO_DISK_CACHE_SIZE)).build());
    }

    @Override
    public void load(final Context context, final String s, final ImageView imageView) {
        Picasso.with(context).load(s).into(imageView);
    }

    @Override
    public void load(final Context context, final String s, final ImageView imageView, @DrawableRes final int n) {
        Picasso.with(context).load(s).placeholder(n).into(imageView);
    }

    @Override
    public void prefetch(final Context context, final String... array) {
        for (int length = array.length, i = 0; i < length; ++i) {
            Picasso.with(context).load(array[i]).fetch();
        }
    }
}
