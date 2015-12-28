package com.asdzheng.sweetshow.imageloaders;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.util.ArrayMap;
import android.widget.ImageView;

import com.asdzheng.sweetshow.MyApplication;

import java.util.List;

/**
 * Created by asdzheng on 2015/12/26.
 */
public class ShowImageLoader {

    private static volatile ShowImageLoader sInstance;
    private Context mContext;
    private ImageLoader mImageLoader;

    private ShowImageLoader(final Context mContext, final ImageLoader mImageLoader) {
        this.mContext = mContext;
        (this.mImageLoader = mImageLoader).configure(mContext);
    }

    public static ShowImageLoader getSharedInstance() {
        synchronized (ShowImageLoader.class) {
            if (ShowImageLoader.sInstance == null) {
                ShowImageLoader.sInstance = new ShowImageLoader(MyApplication.context, new PicassoImageLoader());
            }
            return ShowImageLoader.sInstance;
        }
    }

    public void load(final String s, final ImageView imageView) {
        this.mImageLoader.load(this.mContext, s, imageView);
    }

    public void load(final String s, final ImageView imageView, @DrawableRes final int n) {
        this.mImageLoader.load(this.mContext, s, imageView, n);
    }

    public void fetchImageForSize(final ShowImageLoader.AspectRatios ratios, final String... imageUrls) {
        this.mImageLoader.fetchImageForSize(mContext, ratios, imageUrls);
    }

//    public void loadImageForPhotoAtSize(final Photo photo, final int n, final ImageView imageView) {
//        this.load(photo.getImageUrlForSize(n), imageView);
//    }
//
//    public void loadImageForPhotoAtSize(final Photo photo, final int n, final ImageView imageView, @DrawableRes final int n2) {
//        this.load(photo.getImageUrlForSize(n), imageView, n2);
//    }

    public void prefetch(final List<String> list) {
        this.mImageLoader.prefetch(this.mContext, (String[])list.toArray(new String[list.size()]));
    }

    public void prefetch(final String... array) {
        this.mImageLoader.prefetch(this.mContext, array);
    }

//    public void prefetchImageForPhotosAtSize(final int n, final List<Photo> list) {
//        final String[] array = new String[list.size()];
//        for (int i = 0; i < list.size(); ++i) {
//            array[i] = list.get(i).getImageUrlForSize(n);
//        }
//        this.mImageLoader.prefetch(this.mContext, array);
//    }
//
//    public void prefetchImageForPhotosAtSize(final int n, final Photo... array) {
//        final String[] array2 = new String[array.length];
//        for (int i = 0; i < array.length; ++i) {
//            array2[i] = array[i].getImageUrlForSize(n);
//        }
//        this.mImageLoader.prefetch(this.mContext, array2);
//    }

    public void setImageLoader(final ImageLoader mImageLoader) {
        this.mImageLoader = mImageLoader;
    }

    public interface AspectRatios {
        void getAspectRatios(ArrayMap<String, Double> ratios);
    }
}
