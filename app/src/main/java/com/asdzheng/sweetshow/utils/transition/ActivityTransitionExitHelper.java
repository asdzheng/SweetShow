package com.asdzheng.sweetshow.utils.transition;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.asdzheng.sweetshow.utils.LogUtil;
import com.asdzheng.sweetshow.utils.recyclerview.Size;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by Bruce Too
 * On 9/26/15.
 * At 15:13
 * ActivityTransitionExit
 * Don`t forget add transparent theme in target activity
 * <style name="Transparent">
 * <item name="android:windowNoTitle">true</item>
 * <item name="android:windowIsTranslucent">true</item>
 * <item name="android:windowBackground">@android:color/transparent</item>
 * </style>
 */
public class ActivityTransitionExitHelper {

    private final String TAG = this.getClass().getSimpleName();

    private final DecelerateInterpolator decelerator = new DecelerateInterpolator();
    private final AccelerateInterpolator accelerator = new AccelerateInterpolator();
    private static final int DEFUALT_ANIM_DURATION = 300;
    private static final int SCALE_ANIM_DURATION = 500;

    private Intent fromIntent;//intent from pre activity
    private View toView;//target view show in this activity
    private View background; //root view of this activity
    private ColorDrawable bgDrawable; //background color
    private float leftDelta;
    private float topDelta;
    private float widthDelta;
    private float heightDelta;

    private int thumbnailTop;
    private int thumbnailLeft;
    private int thumbnailWidth;
    private int thumbnailHeight;

    private int animDuration = DEFUALT_ANIM_DURATION;

    public boolean isStarting = true;

    public ActivityTransitionExitHelper(Intent fromIntent) {
        this.fromIntent = fromIntent;
    }

    public static ActivityTransitionExitHelper with(Intent intent) {
        return new ActivityTransitionExitHelper(intent);
    }

    /**
     * add target view
     *
     * @param toView
     * @return
     */
    public ActivityTransitionExitHelper toView(View toView) {
        this.toView = toView;
        return this;
    }

    /**
     * add root view of this layout
     *
     * @param background
     * @return
     */
    public ActivityTransitionExitHelper background(View background) {
        this.background = background;
        return this;
    }

    /**
     * @param savedInstanceState if savedInstanceState != null
     *                           we don`t have to perform the transition animation
     */
    public ActivityTransitionExitHelper start(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            thumbnailTop = fromIntent.getIntExtra(ActivityTransitionEnterHelper.PRE_NAME + ".top", 0);
            thumbnailLeft = fromIntent.getIntExtra(ActivityTransitionEnterHelper.PRE_NAME + ".left", 0);
            thumbnailWidth = fromIntent.getIntExtra(ActivityTransitionEnterHelper.PRE_NAME + ".width", 0);
            thumbnailHeight = fromIntent.getIntExtra(ActivityTransitionEnterHelper.PRE_NAME + ".height", 0);

            final Size size = (Size) fromIntent.getSerializableExtra("size");

            bgDrawable = new ColorDrawable(Color.BLACK);
            background.setBackground(bgDrawable);
            toView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    //remove default
                    toView.getViewTreeObserver().removeOnPreDrawListener(this);
                    int viewLocation[] = new int[2];
                    toView.getLocationOnScreen(viewLocation);
                    leftDelta = thumbnailLeft - toView.getLeft();
                    topDelta = thumbnailTop - toView.getTop();
                    //Note: widthDelta must be float
                    widthDelta = (float) thumbnailWidth / toView.getWidth();
                    heightDelta = (float) thumbnailHeight / size.getHeight();

                    topDelta = topDelta - ((toView.getHeight() - size.getHeight()) / 2) * heightDelta;

                    LogUtil.i(TAG, "thumbnailWidth " + thumbnailWidth + " | thumbnailHeight " +
                            thumbnailHeight + "| width " + toView.getWidth() + " | size.getHeight()" + size.getHeight() + " topDelta = " + topDelta);

                    runEnterAnimation();
                    return true;
                }
            });
        }
        return this;
    }

    private void runEnterAnimation() {
        isStarting = true;
        toView.setPivotX(0);
        toView.setPivotY(0); //axis
        toView.setScaleX(widthDelta);
        toView.setScaleY(heightDelta);
        toView.setTranslationX(leftDelta);
        toView.setTranslationY(topDelta);

        toView.animate().translationX(0).translationY(0)
                .scaleX(1).scaleY(1).setDuration(DEFUALT_ANIM_DURATION)
                .setInterpolator(accelerator).withEndAction(new Runnable() {
            @Override
            public void run() {
                LogUtil.w(TAG, "end start");
                isStarting = false;
            }
        }).start();

        ObjectAnimator bgAnim = ObjectAnimator.ofInt(bgDrawable, "alpha", 0, 255);
        bgAnim.setInterpolator(accelerator);
        bgAnim.setDuration(DEFUALT_ANIM_DURATION);
        bgAnim.start();

    }

    public void runExitAnimation(final Runnable exit) {

        PhotoView photoView = (PhotoView) toView;
//        if (photoView.getScale() > 1) {
//            LogUtil.i(TAG, "photoView width  " + photoView.getDisplayRect().width() + " | photoView Height " +
//                    photoView.getDisplayRect().height() + "| toView.width " + toView.getWidth() + " | size.getHeight()" + toView.getHeight() + " | scale " + photoView.getScale());
//
////            if(photoView.getDisplayRect().height() < toView.getHeight()) {
////                heightDelta = thumbnailHeight / photoView.getDisplayRect().height() ;
////
//////                topDelta = thumbnailTop - toView.getTop();
//////                topDelta = topDelta - ((toView.getHeight() - photoView.getDisplayRect().height()) / 2) * heightDelta;
////            } else {
//////                topDelta = thumbnailTop - toView.getTop();
////
////                heightDelta = thumbnailHeight / (float)toView.getHeight();
//////            }
////            animDuration = animDuration +  (int)(animDuration * (photoView.getScale()-1)) / 3;
////            photoView.setScale(1.0f, true);
//        }

        LogUtil.i(TAG, " photoView Height " +
                photoView.getDisplayRect().height() + " | scale " + photoView.getScale());

        photoView.setZoomTransitionDuration(300);
        photoView.setScale(1.0f, true);

        //targetApi 16
        toView.animate().translationX(leftDelta).translationY(topDelta)
                .scaleX(widthDelta).scaleY(heightDelta)
                .setInterpolator(decelerator)
                .setDuration(animDuration)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        background.setVisibility(View.GONE);
                        toView.setVisibility(View.GONE); //let background and target view invisible
                        exit.run();
                    }
                }).start();

//        //animate color drawable of background
        ObjectAnimator bgAnim = ObjectAnimator.ofInt(bgDrawable, "alpha", 0);
        bgAnim.setInterpolator(decelerator);
        bgAnim.setDuration(animDuration);
        bgAnim.start();
    }
}
