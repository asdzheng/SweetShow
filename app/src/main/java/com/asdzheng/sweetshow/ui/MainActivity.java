package com.asdzheng.sweetshow.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.asdzheng.sweetshow.R;
import com.asdzheng.sweetshow.bean.NewChannelInfoDetailDto;
import com.asdzheng.sweetshow.bean.NewChannelInfoDto;
import com.asdzheng.sweetshow.bean.UserInfo;
import com.asdzheng.sweetshow.http.GsonRequest;
import com.asdzheng.sweetshow.http.UrlUtil;
import com.asdzheng.sweetshow.imageloaders.ImagePipelineConfigFactory;
import com.asdzheng.sweetshow.ui.adapter.PhotosAdapter;
import com.asdzheng.sweetshow.ui.view.waveswiperefreshlayout.WaveSwipeRefreshLayout;
import com.asdzheng.sweetshow.utils.MeasUtils;
import com.asdzheng.sweetshow.utils.StringUtil;
import com.asdzheng.sweetshow.utils.recyclerview.AspectRatioLayoutManager;
import com.asdzheng.sweetshow.utils.recyclerview.AspectRatioSpacingItemDecoration;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class MainActivity extends AppCompatActivity implements WaveSwipeRefreshLayout.OnRefreshListener {

    public static final String SEXY_CHANNEL = "/channel/1033563/senses";


    private RecyclerView mRecyclerView;
    private WaveSwipeRefreshLayout waveSwipeRefreshLayout;

    UserInfo info;

    RequestQueue queue;

    int page =1;

    List<NewChannelInfoDetailDto> list;

    private PhotosAdapter mPhotosAdapter;

    private String nextStr = SEXY_CHANNEL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fresco.initialize(this, ImagePipelineConfigFactory.getOkHttpImagePipelineConfig(this));

        waveSwipeRefreshLayout = (WaveSwipeRefreshLayout) findViewById(R.id.wave_layout);
        int homepage_refresh_spacing = 40;
        waveSwipeRefreshLayout.setProgressViewOffset(false, -homepage_refresh_spacing * 2, homepage_refresh_spacing);
        waveSwipeRefreshLayout.setOnRefreshListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.swipe_target);
//        swipeToLoadLayout = (SwipeToLoadLayout) findViewById(R.id.swipeToLoadLayout);

        queue = Volley.newRequestQueue(this);

        list = new ArrayList<>();

        setupRecyclerView();

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                waveSwipeRefreshLayout.setRefreshing(true);
                requestData(nextStr);
            }
        });

//        requestData(nextStr);
    }

    @NonNull
    private void requestData(final String next) {
        GsonRequest<NewChannelInfoDto> request = new GsonRequest<>(Request.Method.GET, UrlUtil.getBaseUrl(next), NewChannelInfoDto.class, new Response.Listener<NewChannelInfoDto>() {

                @Override
                public void onResponse(NewChannelInfoDto response) {
                    if(response.getData().getResults() != null) {
                        if(page == 1) {
                            mPhotosAdapter.clear();
                            mPhotosAdapter.bind(filterEmptyPhotos(response.getData().getResults()));
                        } else {
                            mPhotosAdapter.bind(filterEmptyPhotos(response.getData().getResults()));
                        }
                    }
                    nextStr = response.getData().getNext();
                    if(page == 1) {
                        waveSwipeRefreshLayout.setRefreshing(false);
                    } else {
                        waveSwipeRefreshLayout.setLoading(false);
                    }

                    page++;
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, "网络连接错误", Toast.LENGTH_SHORT).show();
                    Log.w("main", error.toString());
                    waveSwipeRefreshLayout.setRefreshing(false);
                    waveSwipeRefreshLayout.setLoading(false);

                }
            } );

        queue.add(request);
    }

    private List<NewChannelInfoDetailDto> filterEmptyPhotos(List<NewChannelInfoDetailDto> results) {
        List<NewChannelInfoDetailDto> infos = new ArrayList<>() ;
        for (NewChannelInfoDetailDto info : results) {
            if(StringUtil.isNotEmpty(info.photo)) {
                infos.add(info);
            }
        }
        return infos;
    }

    private void setupRecyclerView() {
        this.mPhotosAdapter = new PhotosAdapter(list,this);
//        AnimationAdapter animationAdapter = new AlphaInAnimationAdapter(mPhotosAdapter);
//        animationAdapter.setDuration(500);
        this.mRecyclerView.setAdapter(new ScaleInAnimationAdapter(mPhotosAdapter));
        final AspectRatioLayoutManager layoutManager = new AspectRatioLayoutManager(mPhotosAdapter);
        this.mRecyclerView.setLayoutManager(layoutManager);
        layoutManager.setMaxRowHeight(getResources().getDisplayMetrics().heightPixels / 3);
        this.mRecyclerView.addItemDecoration(new AspectRatioSpacingItemDecoration(MeasUtils.dpToPx(4.0f, this)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onRefresh() {
        page = 1;
        nextStr = SEXY_CHANNEL;
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
}
