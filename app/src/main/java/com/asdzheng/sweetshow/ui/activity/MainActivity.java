package com.asdzheng.sweetshow.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.asdzheng.sweetshow.R;
import com.asdzheng.sweetshow.bean.NewChannelInfoDetailDto;
import com.asdzheng.sweetshow.bean.NewChannelInfoDto;
import com.asdzheng.sweetshow.http.GsonRequest;
import com.asdzheng.sweetshow.http.UrlUtil;
import com.asdzheng.sweetshow.imageloaders.ShowImageLoader;
import com.asdzheng.sweetshow.ui.adapter.PhotosAdapter;
import com.asdzheng.sweetshow.ui.view.waveswiperefreshlayout.WaveSwipeRefreshLayout;
import com.asdzheng.sweetshow.utils.LogUtil;
import com.asdzheng.sweetshow.utils.MeasUtils;
import com.asdzheng.sweetshow.utils.StringUtil;
import com.asdzheng.sweetshow.utils.recyclerview.AspectRatioLayoutManager;
import com.asdzheng.sweetshow.utils.recyclerview.AspectRatioSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.AnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class MainActivity extends BaseActivity implements WaveSwipeRefreshLayout.OnRefreshListener {
    //SexyChannel
    public static final String SEXY_CHANNEL = "/channel/1033563/senses";

    public static final String BEAUTY_CHANNEL = "/channel/1015326/senses";
    @Bind(R.id.recycler_channel_view)
    RecyclerView recyclerChannelView;
    @Bind(R.id.wave_channel)
    WaveSwipeRefreshLayout waveChannel;

    RequestQueue queue;

    int page = 1;

    List<NewChannelInfoDetailDto> list;

    private PhotosAdapter mPhotosAdapter;

    private String nextStr = BEAUTY_CHANNEL;

    @NonNull
    private void requestData(final String next) {
        GsonRequest<NewChannelInfoDto> request = new GsonRequest<>(Request.Method.GET, UrlUtil.getBaseUrl(next), NewChannelInfoDto.class,
                new Response.Listener<NewChannelInfoDto>() {

                    @Override
                    public void onResponse(NewChannelInfoDto response) {
                        if (response.getData().getResults() != null) {
                            if (page == 1) {
                                mPhotosAdapter.clear();
                                mPhotosAdapter.bind(filterEmptyPhotos(response.getData().getResults()));
                            } else {
                                mPhotosAdapter.bind(filterEmptyPhotos(response.getData().getResults()));
                            }
                        }
                        nextStr = response.getData().getNext();
                        if (page == 1) {
                            waveChannel.setRefreshing(false);
                        } else {
                            waveChannel.setLoading(false);
                        }

                        page++;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "网络连接错误", Toast.LENGTH_SHORT).show();
                Log.w("main", error.toString());
                waveChannel.setRefreshing(false);
                waveChannel.setLoading(false);

            }
        });

        queue.add(request);
    }

    private List<NewChannelInfoDetailDto> filterEmptyPhotos(List<NewChannelInfoDetailDto> results) {
        List<NewChannelInfoDetailDto> infos = new ArrayList<>();
        for (NewChannelInfoDetailDto info : results) {
            if (StringUtil.isNotEmpty(info.photo)) {
                infos.add(info);
            }
        }
        return infos;
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.i("MAIN", "width" + getWindow().getDecorView().getWidth() + " | " + getWindow().getDecorView().getHeight());
    }

    private void setupRecyclerView() {
        this.mPhotosAdapter = new PhotosAdapter(list, this);

        AnimationAdapter animationAdapter = new AlphaInAnimationAdapter(mPhotosAdapter);
        animationAdapter.setDuration(1000);
        this.recyclerChannelView.setAdapter(new ScaleInAnimationAdapter(mPhotosAdapter));
        final AspectRatioLayoutManager layoutManager = new AspectRatioLayoutManager(mPhotosAdapter);
        this.recyclerChannelView.setLayoutManager(layoutManager);
        layoutManager.setMaxRowHeight(getResources().getDisplayMetrics().heightPixels / 3);
        this.recyclerChannelView.addItemDecoration(new AspectRatioSpacingItemDecoration(MeasUtils.dpToPx(4.0f, this)));

        recyclerChannelView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    ShowImageLoader.getSharedInstance().resumeTag(MainActivity.this);
                } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    ShowImageLoader.getSharedInstance().pauseTag(MainActivity.this);
                } else if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
                    ShowImageLoader.getSharedInstance().pauseTag(MainActivity.this);
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        page = 1;
        nextStr = BEAUTY_CHANNEL;
        requestData(nextStr);
    }

    @Override
    public void onLoad() {
        requestData(nextStr);
    }

    @Override
    public boolean canLoadMore() {
        return true;
    }

    @Override
    public boolean canRefresh() {
        return true;
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
        int homepage_refresh_spacing = 40;

        waveChannel.setProgressViewOffset(false, -homepage_refresh_spacing * 2, homepage_refresh_spacing);
        waveChannel.setOnRefreshListener(this);

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        queue = Volley.newRequestQueue(this);

        list = new ArrayList<>();

        setupRecyclerView();

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                waveChannel.setRefreshing(true);
                requestData(nextStr);
            }
        });

    }

}
