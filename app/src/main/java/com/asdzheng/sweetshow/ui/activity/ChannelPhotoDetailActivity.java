package com.asdzheng.sweetshow.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.asdzheng.sweetshow.R;
import com.asdzheng.sweetshow.imageloaders.ShowImageLoader;
import com.asdzheng.sweetshow.utils.recyclerview.Size;

/**
 * Created by Administrator on 2016-1-4.
 */
public class ChannelPhotoDetailActivity extends Activity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        imageView = (ImageView) findViewById(R.id.iv_channel_photo_detail);

        Size size = (Size) getIntent().getSerializableExtra("size");
        imageView.setLayoutParams(new RelativeLayout.LayoutParams(size.getWidth(), size.getHeight()));

        ShowImageLoader.getSharedInstance().load(getIntent().getStringExtra("photo"), imageView);
    }
}
