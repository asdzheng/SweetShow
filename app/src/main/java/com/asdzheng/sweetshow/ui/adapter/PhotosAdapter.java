package com.asdzheng.sweetshow.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.view.View;
import android.view.ViewGroup;

import com.asdzheng.sweetshow.bean.NewChannelInfoDetailDto;
import com.asdzheng.sweetshow.ui.activity.ChannelPhotoDetailActivity;
import com.asdzheng.sweetshow.ui.view.ChannelImageView;
import com.asdzheng.sweetshow.utils.MeasUtils;
import com.asdzheng.sweetshow.utils.StringUtil;
import com.asdzheng.sweetshow.utils.recyclerview.AspectRatioLayoutSizeCalculator;
import com.asdzheng.sweetshow.utils.recyclerview.Size;
import com.asdzheng.sweetshow.utils.transition.ActivityTransitionEnterHelper;

import java.util.List;

/**
 * Created by asdzheng on 2015/12/28.
 */
public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.PhotoViewHolder> implements AspectRatioLayoutSizeCalculator.SizeCalculatorDelegate {
    private List<NewChannelInfoDetailDto> mPhotos;

    private ArrayMap<String, Double> photoAspectRatios;

    private Context mContext;

    public PhotosAdapter(List<NewChannelInfoDetailDto> mPhotos, Context context) {
        this.mPhotos = mPhotos;
        this.mContext = context;
    }

    @Override
    public double aspectRatioForIndex(final int n) {
        if (n < getItemCount()) {
            final NewChannelInfoDetailDto info = mPhotos.get(n);
            double ratio = StringUtil.getAspectRadioFromUrl(info.photo);
            return ratio;
        }
        return 1.0;
    }

    public void bind(@NonNull final List<NewChannelInfoDetailDto> mPhotos) {
        this.mPhotos.addAll(mPhotos);

        notifyDataSetChanged();
    }

    public void clear() {
        mPhotos.clear();
        notifyDataSetChanged();
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PhotoViewHolder(new ChannelImageView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {
        ((ChannelImageView) holder.itemView).bind(mPhotos.get(position).photo);
        holder.itemView.setTag(mPhotos.get(position).photo);
    }

    @Override
    public int getItemCount() {
        return this.mPhotos.size();
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder {
        public PhotoViewHolder(final View view) {
            super(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    scaleUpAnimation(v);
                }
            });
        }

    }

    public ArrayMap<String, Double> getPhotoAspectRatios() {
        return photoAspectRatios;
    }

    public void setPhotoAspectRatios(ArrayMap<String, Double> photoAspectRatios) {
        this.photoAspectRatios = photoAspectRatios;
    }

    private void scaleUpAnimation(View view) {
        Activity context = (Activity) view.getContext();
        //因为宽在大图中一直是全屏，高预估为宽放大的比例
        Size detailSize = new Size(MeasUtils.getDisplayWidth(context), (int)(view.getHeight() *
            (float)MeasUtils.getDisplayWidth(context) / view.getWidth()));

//        LogUtil.w("Adapter", "Size = " + detailSize + " | getHeight = " + view.getHeight() + " scale =" +
//                Float.parseFloat(String.format("%.2f",(float)MeasUtils.getDisplayWidth(context) / view.getWidth())));

        Bundle bundle = new Bundle();
        bundle.putSerializable("size", detailSize);
        bundle.putString("photo", view.getTag().toString());

        ActivityTransitionEnterHelper.with(context).fromView(view).
                bundle(bundle).start(ChannelPhotoDetailActivity.class);
    }

}
