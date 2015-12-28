package com.asdzheng.sweetshow.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.asdzheng.sweetshow.imageloaders.ShowImageLoader;

/**
 * Created by asdzheng on 2015/12/28.
 */
public class PhotoView extends ImageView
{
    private String mPhoto;

    public PhotoView(final Context context) {
        super(context);
        this.init(null, 0);
    }

    public PhotoView(final Context context, final AttributeSet set) {
        super(context, set);
        this.init(set, 0);
    }

    public PhotoView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.init(set, n);
    }

    private void init(final AttributeSet set, final int n) {
        this.setScaleType(ImageView.ScaleType.FIT_XY);
        this.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        this.setBackgroundColor(Color.GRAY);
    }

//    public void bind(final S mPhoto) {
//        this.mPhoto = mPhoto;
//       ShowImageLoader.getSharedInstance().loadImageForPhotoAtSize(mPhoto, 31, this);
//    }

    public void bind(final String s) {
        ShowImageLoader.getSharedInstance().load(s, this);
    }

    public String toString() {
        return this.mPhoto;
    }
}
