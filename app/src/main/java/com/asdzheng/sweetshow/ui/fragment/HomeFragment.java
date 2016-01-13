package com.asdzheng.sweetshow.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.asdzheng.sweetshow.ui.adapter.HomeAdapter;
import com.asdzheng.sweetshow.ui.view.waveswiperefreshlayout.WaveSwipeRefreshLayout;
import com.asdzheng.sweetshow.utils.MeasUtils;
import com.asdzheng.sweetshow.utils.StringUtil;
import com.asdzheng.sweetshow.utils.recyclerview.LinearSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016-1-11.
 */
public class HomeFragment extends Fragment implements WaveSwipeRefreshLayout.OnRefreshListener{


    @butterknife.Bind(R.id.recycler_home_view)
    android.support.v7.widget.RecyclerView recyclerHomeView;
    @butterknife.Bind(R.id.wave_home)
    WaveSwipeRefreshLayout waveHome;

    HomeAdapter homeAdapter;

    int page = 1;

    RequestQueue queue;
    List<NewChannelInfoDetailDto> list;
    private String nextStr = UrlUtil.BEAUTY_CHANNEL;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        queue = Volley.newRequestQueue(getContext());
        list = new ArrayList<>();
        setupRecyclerView();

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                waveHome.setRefreshing(true);
                requestData(nextStr);
            }
        });

        int homepage_refresh_spacing = 40;

        waveHome.setProgressViewOffset(false, -homepage_refresh_spacing * 2, homepage_refresh_spacing);
        waveHome.setOnRefreshListener(this);
        waveHome.setCanRefresh(true);
    }

    @NonNull
    private void requestData(final String next) {
        GsonRequest<NewChannelInfoDto> request = new GsonRequest<>(Request.Method.GET, UrlUtil.getBaseUrl(next), NewChannelInfoDto.class,
                new Response.Listener<NewChannelInfoDto>() {

                    @Override
                    public void onResponse(NewChannelInfoDto response) {
                        if (response.getData().getResults() != null) {
                            if (page == 1) {
                                homeAdapter.clear();
                                homeAdapter.bind(filterEmptyPhotos(response.getData().getResults()));
                            } else {
                                homeAdapter.bind(filterEmptyPhotos(response.getData().getResults()));
                            }
                        }
                        nextStr = response.getData().getNext();
                        if (page == 1) {
                            waveHome.setRefreshing(false);
                        } else {
                            waveHome.setLoading(false);
                        }

                        page++;

                        if(homeAdapter.getItemCount() > 0) {
                            waveHome.setCanLoadMore(true);
                        } else {
                            waveHome.setCanLoadMore(false);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if(homeAdapter.getItemCount() == 0) {
                    waveHome.setCanLoadMore(false);
                }

                Toast.makeText(getContext(), "网络连接错误", Toast.LENGTH_SHORT).show();
                Log.w("main", error.toString());
                waveHome.setRefreshing(false);
                waveHome.setLoading(false);

            }
        });
        request.setTag(this);
        queue.add(request);
    }

    private void setupRecyclerView() {
        this.homeAdapter = new HomeAdapter(getContext(), list);
        this.recyclerHomeView.setAdapter(homeAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        this.recyclerHomeView.setLayoutManager(layoutManager);
//        layoutManager.setMaxRowHeight(getResources().getDisplayMetrics().heightPixels / 3);
        this.recyclerHomeView.addItemDecoration(new LinearSpacingItemDecoration(MeasUtils.dpToPx(4.0f, getContext())));

        recyclerHomeView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    ShowImageLoader.getSharedInstance().resumeTag(getContext());
                } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    ShowImageLoader.getSharedInstance().pauseTag(getContext());
                } else if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
                    ShowImageLoader.getSharedInstance().pauseTag(getContext());
                }
            }
        });
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
    public void onRefresh() {
        page = 1;
        nextStr = UrlUtil.BEAUTY_CHANNEL;
        requestData(nextStr);
    }

    @Override
    public void onLoad() {
        requestData(nextStr);
    }
}
