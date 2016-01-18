package com.asdzheng.sweetshow.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.asdzheng.sweetshow.R;
import com.asdzheng.sweetshow.bean.ChannelBean;
import com.asdzheng.sweetshow.imageloaders.ShowImageLoader;
import com.asdzheng.sweetshow.utils.LogUtil;
import com.asdzheng.sweetshow.utils.MeasUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016-1-18.
 */
public class DiscoverAdapter extends RecyclerView.Adapter<DiscoverAdapter.DiscoverHolder> {


    private List<ChannelBean> channels;
    private Context context;

    public DiscoverAdapter(Context context, List<ChannelBean> channels) {
        this.channels = channels;
        this.context = context;
    }

    @Override
    public DiscoverHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View channelView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_discover, null);
        return new DiscoverHolder(channelView);
    }

    @Override
    public int getItemCount() {
        return channels.size();
    }

    @Override
    public void onBindViewHolder(DiscoverHolder holder, int position) {
        LogUtil.i("DiscoverAdapter", position + "");
        holder.tvDiscover.setText(channels.get(position).getChannelName());
        ShowImageLoader.getSharedInstance().load(context, channels.get(position).getChannelPhoto(), holder.ivDiscover);
    }

    class DiscoverHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_discover)
        ImageView ivDiscover;
        @Bind(R.id.tv_discover)
        TextView tvDiscover;
        public DiscoverHolder(View itemView) {
            super(itemView);
            int width = (MeasUtils.getDisplayWidth() - MeasUtils.dpToPx(4, context)) / 2;
            itemView.setLayoutParams(new ViewGroup.LayoutParams(width, width));
            ButterKnife.bind(this, itemView);
        }
    }
}
