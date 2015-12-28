package com.asdzheng.sweetshow.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
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
import com.asdzheng.sweetshow.ui.adapter.PhotosAdapter;
import com.asdzheng.sweetshow.utils.MeasUtils;
import com.asdzheng.sweetshow.utils.recyclerview.AspectRatioLayoutManager;
import com.asdzheng.sweetshow.utils.recyclerview.AspectRatioSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private RecyclerView mRecyclerView;

    UserInfo info;

    RequestQueue queue;

    List<NewChannelInfoDetailDto> list;

    private PhotosAdapter mPhotosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);

        queue = Volley.newRequestQueue(this);

        list = new ArrayList<>();

        setupRecyclerView();

        GsonRequest<NewChannelInfoDto> request = new GsonRequest<>(Request.Method.GET, "http://v2.same.com/channel/1033563/senses", NewChannelInfoDto.class, new Response.Listener<NewChannelInfoDto>() {

            @Override
            public void onResponse(NewChannelInfoDto response) {
                if(response.getData().getResults() != null) {
                    mPhotosAdapter.bind(response.getData().getResults());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.w("main", error.toString());
            }
        } );

        queue.add(request);


    }

    private void setupRecyclerView() {
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
}
