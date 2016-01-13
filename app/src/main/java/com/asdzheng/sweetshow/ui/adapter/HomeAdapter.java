package com.asdzheng.sweetshow.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.asdzheng.sweetshow.R;
import com.asdzheng.sweetshow.bean.NewChannelInfoDetailDto;
import com.asdzheng.sweetshow.imageloaders.ShowImageLoader;
import com.asdzheng.sweetshow.utils.MeasUtils;
import com.asdzheng.sweetshow.utils.StringUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016-1-12.
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.CardViewHolder> {


    private Context mContext;
    private List<NewChannelInfoDetailDto> infos;

    public HomeAdapter(Context context, List<NewChannelInfoDetailDto> mPhotos) {
        mContext = context;
        infos = mPhotos;
    }

    public void bind(@NonNull final List<NewChannelInfoDetailDto> mPhotos) {
        this.infos.addAll(mPhotos);
        notifyDataSetChanged();
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.adapter_home, parent, false);
        return new CardViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        float width = MeasUtils.getDisplayWidth();
        float height = (float) (width / StringUtil.getAspectRadioFromUrl(infos.get(position).photo));
        holder.ivHome.setLayoutParams(new RelativeLayout.LayoutParams((int) width, (int) height));

        ShowImageLoader.getSharedInstance().load(mContext, infos.get(position).photo, holder.ivHome);
    }

    @Override
    public int getItemCount() {
        return infos.size();
    }

    static class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @Bind(R.id.iv_home)
        ImageView ivHome;
        @Bind(R.id.iv_home_love)
        ImageView ivHomeLove;

        public CardViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            ivHome.setOnClickListener(this);
            ivHomeLove.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }

    public void clear() {
        infos.clear();
        notifyDataSetChanged();
    }
}
