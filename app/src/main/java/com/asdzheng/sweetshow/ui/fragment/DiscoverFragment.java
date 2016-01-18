package com.asdzheng.sweetshow.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asdzheng.sweetshow.R;
import com.asdzheng.sweetshow.bean.ChannelBean;
import com.asdzheng.sweetshow.ui.adapter.DiscoverAdapter;
import com.asdzheng.sweetshow.utils.MeasUtils;
import com.asdzheng.sweetshow.utils.recyclerview.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016-1-11.
 */
public class DiscoverFragment extends Fragment {


    @Bind(R.id.rv_discover)
    RecyclerView rvDiscover;

    DiscoverAdapter adapter;

    List<ChannelBean> channels;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initList();
        setUpRecyclerView();
    }

    private void initList() {
        ChannelBean channel = new ChannelBean();
        channel.setChannelName("性感");
        channel.setChannelPhoto("http://h.hiphotos.baidu.com/baike/w%3D268/sign=c229e77a27a446237ecaa264a0227246/b7003af33a87e9509a216e6c17385343fbf2b4f8.jpg");

        ChannelBean channel1 = new ChannelBean();
        channel.setChannelName("美女");
        channel.setChannelPhoto("http://f.hiphotos.baidu.com/baike/c0%3Dbaike150%2C5%2C5%2C150%2C50/sign=d5412780ad4bd11310c0bf603bc6cf6a/2f738bd4b31c8701bc5f7115277f9e2f0608ff5c.jpg");

        ChannelBean channe2 = new ChannelBean();
        channel.setChannelName("美景");
        channel.setChannelPhoto("http://h.hiphotos.baidu.com/baike/w%3D268/sign=c229e77a27a446237ecaa264a0227246/b7003af33a87e9509a216e6c17385343fbf2b4f8.jpg");

        ChannelBean channe3 = new ChannelBean();
        channel.setChannelName("建筑");
        channel.setChannelPhoto("http://h.hiphotos.baidu.com/baike/w%3D268/sign=c229e77a27a446237ecaa264a0227246/b7003af33a87e9509a216e6c17385343fbf2b4f8.jpg");

        ChannelBean channe4 = new ChannelBean();
        channel.setChannelName("建筑");
        channel.setChannelPhoto("http://h.hiphotos.baidu.com/baike/w%3D268/sign=c229e77a27a446237ecaa264a0227246/b7003af33a87e9509a216e6c17385343fbf2b4f8.jpg");

        ChannelBean channe5 = new ChannelBean();
        channel.setChannelName("建筑");
        channel.setChannelPhoto("http://h.hiphotos.baidu.com/baike/w%3D268/sign=c229e77a27a446237ecaa264a0227246/b7003af33a87e9509a216e6c17385343fbf2b4f8.jpg");

        ChannelBean channe6 = new ChannelBean();
        channel.setChannelName("建筑");
        channel.setChannelPhoto("http://h.hiphotos.baidu.com/baike/w%3D268/sign=c229e77a27a446237ecaa264a0227246/b7003af33a87e9509a216e6c17385343fbf2b4f8.jpg");

        ChannelBean channe7 = new ChannelBean();
        channel.setChannelName("建筑");
        channel.setChannelPhoto("http://h.hiphotos.baidu.com/baike/w%3D268/sign=c229e77a27a446237ecaa264a0227246/b7003af33a87e9509a216e6c17385343fbf2b4f8.jpg");


        channels = new ArrayList<>();
        channels.add(channel);
        channels.add(channel);
        channels.add(channel);
        channels.add(channel);
        channels.add(channel);
        channels.add(channel);
        channels.add(channel);
        channels.add(channel);
    }

    private void setUpRecyclerView() {
        adapter = new DiscoverAdapter(getActivity(), channels);
        rvDiscover.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        rvDiscover.setLayoutManager(layoutManager);
//        rvDiscover.addItemDecoration(new RecyclerView.ItemDecoration() {
//        });
        rvDiscover.addItemDecoration(new GridSpacingItemDecoration(MeasUtils.dpToPx(4, getContext()), 2));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
