package com.asdzheng.sweetshow.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.asdzheng.sweetshow.utils.MeasUtils;
import com.asdzheng.sweetshow.utils.recyclerview.AspectRatioLayoutManager;
import com.asdzheng.sweetshow.utils.recyclerview.AspectRatioSpacingItemDecoration;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerArrayAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener{


    private EasyRecyclerView mRecyclerView;

    UserInfo info;

    RequestQueue queue;

    List<NewChannelInfoDetailDto> list;

    private PhotosAdapter mPhotosAdapter;


    private String channel = "/channel/1033563/senses";

    private String nextStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (EasyRecyclerView) findViewById(R.id.recycler);

        queue = Volley.newRequestQueue(this);

        list = new ArrayList<>();

        setupRecyclerView();

        GsonRequest<NewChannelInfoDto> request = requestData("");

        queue.add(request);


    }

    @NonNull
    private GsonRequest<NewChannelInfoDto> requestData(String next) {
        return new GsonRequest<>(Request.Method.GET, UrlUtil.getBaseUrl(channel, next), NewChannelInfoDto.class, new Response.Listener<NewChannelInfoDto>() {

                @Override
                public void onResponse(NewChannelInfoDto response) {
                    if(response.getData().getResults() != null) {
                        mPhotosAdapter.addAll(response.getData().getResults());
                    }

                    nextStr = response.getData().getNext();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.w("main", error.toString());
                }
            } );
    }

    private void setupRecyclerView() {
        this.mPhotosAdapter = new PhotosAdapter(this);
        this.mRecyclerView.setAdapter(mPhotosAdapter);
        final AspectRatioLayoutManager layoutManager = new AspectRatioLayoutManager(mPhotosAdapter);
        this.mRecyclerView.setLayoutManager(layoutManager);
        mPhotosAdapter.setMore(R.layout.view_more, this);
        mPhotosAdapter.setNoMore(R.layout.view_nomore);
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
        requestData("");
    }
}
