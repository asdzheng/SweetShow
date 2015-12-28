package com.asdzheng.sweetshow.utils;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by asdzheng on 2015/12/28.
 */
public class MeasUtils {
    public static int dpToPx(final float n, final Context context) {
        return (int) TypedValue.applyDimension(1, n, context.getResources().getDisplayMetrics());
    }

    public static int pxToDp(final int n, final Context context) {
        return (int)TypedValue.applyDimension(0, (float)n, context.getResources().getDisplayMetrics());
    }
}
