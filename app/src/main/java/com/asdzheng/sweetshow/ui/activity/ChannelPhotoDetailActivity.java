package com.asdzheng.sweetshow.ui.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.asdzheng.sweetshow.R;
import com.asdzheng.sweetshow.utils.recyclerview.Size;
import com.asdzheng.sweetshow.utils.transition.ActivityTransitionExitHelper;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by Administrator on 2016-1-4.
 */
public class ChannelPhotoDetailActivity extends Activity {

    private ImageView imageView;

    private ActivityTransitionExitHelper transitionExitHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        imageView = (ImageView) findViewById(R.id.iv_channel_photo_detail);

        Size size = (Size) getIntent().getSerializableExtra("size");
        String photo = getIntent().getStringExtra("photo");
//        imageView.setLayoutParams(new RelativeLayout.LayoutParams(size.getWidth(), size.getHeight()));

        transitionExitHelper = ActivityTransitionExitHelper.with(getIntent())
                .toView(imageView).background(findViewById(R.id.rl_photo_detail)).start(savedInstanceState);

        Picasso.with(this).load(photo).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                imageView.setImageBitmap(bitmap);
                findViewById(R.id.mp_photo_detail).setVisibility(View.GONE);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                findViewById(R.id.mp_photo_detail).setVisibility(View.GONE);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        transitionExitHelper.runExitAnimation(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }
}
