package com.asdzheng.sweetshow.utils.transition;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Bruce Too
 * On 9/26/15.
 * At 15:13
 * ActivityTransitionExitHelper和 ActivityTransitionEnterHelper
 * 实现了Activity直接切换的时候自定义view动画
 */
public class ActivityTransitionEnterHelper {

    public final static String PRE_NAME = "ActivityTransitionEnterHelper";
    private final Activity activity;
    private View fromView;//the view where u click
    private String imgUrl;// the resource url of imageView etc..
    private Bundle bundle;

    public ActivityTransitionEnterHelper(Activity activity) {
        this.activity = activity;
    }

    public static ActivityTransitionEnterHelper with(Activity activity) {
        return new ActivityTransitionEnterHelper(activity);
    }

    public ActivityTransitionEnterHelper fromView(View fromView) {
        this.fromView = fromView;
        return this;
    }

    public ActivityTransitionEnterHelper imageUrl(String imgUrl) {
        this.imgUrl = imgUrl;
        return this;
    }

    public ActivityTransitionEnterHelper bundle(Bundle bundle) {
        this.bundle = bundle;
        return this;
    }

    public void start(Class target) {

        Intent intent = new Intent(activity, target);
        int[] screenLocation = new int[2];
        fromView.getLocationOnScreen(screenLocation);
        intent.putExtra(PRE_NAME + ".left", screenLocation[0]).
                putExtra(PRE_NAME + ".top", screenLocation[1]).
                putExtra(PRE_NAME + ".width", fromView.getWidth()).
                putExtra(PRE_NAME + ".height", fromView.getHeight()).
                putExtra(PRE_NAME + ".imageUrl", imgUrl);
        if(bundle != null) {
            intent.putExtras(bundle);
        }

        activity.startActivity(intent);
        activity.overridePendingTransition(0, 0);
    }

}
