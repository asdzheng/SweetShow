package com.asdzheng.sweetshow.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

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
import com.asdzheng.sweetshow.ui.adapter.PhotosAdapter;
import com.asdzheng.sweetshow.ui.view.swipelayout.OnLoadMoreListener;
import com.asdzheng.sweetshow.ui.view.swipelayout.OnRefreshListener;
import com.asdzheng.sweetshow.ui.view.swipelayout.SwipeToLoadLayout;
import com.asdzheng.sweetshow.utils.MeasUtils;
import com.asdzheng.sweetshow.utils.recyclerview.AspectRatioLayoutManager;
import com.asdzheng.sweetshow.utils.recyclerview.AspectRatioSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnRefreshListener, OnLoadMoreListener {

    public static final String SEXY_CHANNEL = "/channel/1033563/senses";


    private RecyclerView mRecyclerView;
    private SwipeToLoadLayout swipeToLoadLayout;

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
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.swipe_target);
        swipeToLoadLayout = (SwipeToLoadLayout) findViewById(R.id.swipeToLoadLayout);

        queue = Volley.newRequestQueue(this);

        list = new ArrayList<>();

        setupRecyclerView();

        swipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setRefreshing(true);
            }
        });

        requestData(nextStr);
    }

    @NonNull
    private void requestData(final String next) {
        GsonRequest<NewChannelInfoDto> request = new GsonRequest<>(Request.Method.GET, UrlUtil.getBaseUrl(next), NewChannelInfoDto.class, new Response.Listener<NewChannelInfoDto>() {

                @Override
                public void onResponse(NewChannelInfoDto response) {
                    if(response.getData().getResults() != null) {
                        if(page == 1) {
                            mPhotosAdapter.clear();
                            mPhotosAdapter.bind(response.getData().getResults());
                        } else {
                            mPhotosAdapter.bind(response.getData().getResults());
                        }
                    }
                    nextStr = response.getData().getNext();
                    if(page == 1) {
                        swipeToLoadLayout.setRefreshing(false);
                    } else {
                        swipeToLoadLayout.setLoadingMore(false);
                    }

                    page++;
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.w("main", error.toString());
                    swipeToLoadLayout.setRefreshing(false);
                    swipeToLoadLayout.setLoadingMore(false);

                }
            } );

        queue.add(request);
    }

    private void setupRecyclerView() {
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);

        this.mPhotosAdapter = new PhotosAdapter(list);
        this.mRecyclerView.setAdapter(mPhotosAdapter);
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
    public void onLoadMore() {
        requestData(nextStr);
    }

    @Override
    public void onRefresh() {
        page = 1;
        nextStr = SEXY_CHANNEL;
        requestData(nextStr);
    }
}
