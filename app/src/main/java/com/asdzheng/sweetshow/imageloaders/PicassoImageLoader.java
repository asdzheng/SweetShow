package com.asdzheng.sweetshow.imageloaders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.util.ArrayMap;
import android.widget.ImageView;

import com.asdzheng.sweetshow.ui.view.ChannelImageView;
import com.asdzheng.sweetshow.utils.ConfigConstants;
import com.asdzheng.sweetshow.utils.LogUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by asdzheng on 2015/12/26.
 */
public class PicassoImageLoader implements ImageLoader {

    @Override
    public void configure(final Context context) {
        Picasso.setSingletonInstance(new Picasso.Builder(context.getApplicationContext()).
                downloader(new OkHttpDownloader(context.getApplicationContext(), ConfigConstants.MAX_DISK_CACHE_SIZE)).
                memoryCache(new LruCache(ConfigConstants.MAX_MEMORY_CACHE_SIZE)).defaultBitmapConfig(Bitmap.Config.RGB_565).
                build());

    }

    @Override
    public void load(final Context context, final String s, final ImageView imageView) {
        if (imageView instanceof ChannelImageView) {
            ChannelImageView channelImageView = (ChannelImageView) imageView;

//            Picasso.with(context).load(s).tag(context).into(imageView);
//            LogUtil.w("PicassoImageLoader", "photoView.getSize() " + channelImageView.getWidth() + " | " + channelImageView.getHeight());

            Picasso.with(context).load(s).tag(context).resize(channelImageView.getWidth(), channelImageView.getHeight()).into(imageView);

//            else {
//                Picasso.with(context).load(s).tag(context).into(imageView);
//            }

        } else {
            Picasso.with(context).load(s).tag(context).into(imageView);
        }
    }

    @Override
    public void load(final Context context, final String s, final ImageView imageView, @DrawableRes final int n) {
        Picasso.with(context).load(s).tag(context).fit().placeholder(n).into(imageView);

    }

    @Override
    public void prefetch(final Context context, final String... array) {
        for (int length = array.length, i = 0; i < length; ++i) {
            Picasso.with(context).load(array[i]).fetch();
        }
    }

    @Override
    public void fetchImageForSize(Context context, final ShowImageLoader.AspectRatios ratios, final String... imageUrls) {
        final ArrayMap<String, Double> ratiosMap = new ArrayMap<>();
        for (int length = imageUrls.length, i = 0; i < length; ++i) {
            final String url = imageUrls[i];
            Picasso.with(context).load(url).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    LogUtil.i("imageloader", from.toString());
                    if (bitmap.getWidth() >= 1 && bitmap.getHeight() >= 1) {
                        ratiosMap.put(url, (double) (bitmap.getWidth() / bitmap.getHeight()));
                    } else {
                        ratiosMap.put(url, (double) 1);
                    }

                    if (ratiosMap.size() == imageUrls.length) {
                        ratios.getAspectRatios(ratiosMap);
                    }

                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    LogUtil.i("imageloader", "fail");

                    ratiosMap.put(url, (double) 1);
                    if (ratiosMap.size() == imageUrls.length) {
                        ratios.getAspectRatios(ratiosMap);
                    }
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
        }
    }

    @Override
    public void pause(Context context) {
        Picasso.with(context).pauseTag(context);
    }

    @Override
    public void resume(Context context) {
        Picasso.with(context).resumeTag(context);
    }

    @Override
    public void load(Context context, String s, ImageTarget target) {
        Picasso.with(context).load(s).into(target);
    }

    @Override
    public void load(Context context, String s, ImageView target, Callback callback) {
        Picasso.with(context).load(s).tag(context).into(target, callback);
    }

    @Override
    public void cancelRequest(Context context) {
        Picasso.with(context).cancelTag(context);
    }
}
