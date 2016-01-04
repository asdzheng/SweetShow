package com.asdzheng.sweetshow.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.view.View;
import android.view.ViewGroup;

import com.asdzheng.sweetshow.bean.NewChannelInfoDetailDto;
import com.asdzheng.sweetshow.imageloaders.ShowImageLoader;
import com.asdzheng.sweetshow.ui.activity.ChannelPhotoDetailActivity;
import com.asdzheng.sweetshow.ui.view.PhotoView;
import com.asdzheng.sweetshow.utils.MeasUtils;
import com.asdzheng.sweetshow.utils.StringUtil;
import com.asdzheng.sweetshow.utils.recyclerview.AspectRatioLayoutSizeCalculator;
import com.asdzheng.sweetshow.utils.recyclerview.Size;

import java.util.List;

/**
 * Created by asdzheng on 2015/12/28.
 */
public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.PhotoViewHolder> implements AspectRatioLayoutSizeCalculator.SizeCalculatorDelegate {
    protected static final int INVALID_ITEM_POSITION = -1;
    private List<NewChannelInfoDetailDto> mPhotos;

    private ArrayMap<String, Double> photoAspectRatios;

    ViewGroup.LayoutParams params;

    private int num = 0;

    public PhotosAdapter(List<NewChannelInfoDetailDto> mPhotos, Context context) {
        this.mPhotos = mPhotos;
        params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }


    private void prefetchPhotos(@NonNull final List<String> list) {
        ShowImageLoader.getSharedInstance().prefetch(list);

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

//        String[] urls = new String[mPhotos.size()];
//        for (int i = 0; i < mPhotos.size(); i++) {
//            urls[i] = mPhotos.get(i).photo;
//        }

//        ShowImageLoader.getSharedInstance().fetchImageForSize(new ShowImageLoader.AspectRatios() {
//
//            @Override
//            public void getAspectRatios(ArrayMap<String, Double> ratios) {
//                photoAspectRatios = ratios;
//
//            }
//        }, urls);
        notifyDataSetChanged();
    }

    public void clear() {
        mPhotos.clear();
        notifyDataSetChanged();
    }

    public void bindNext(@NonNull final List<NewChannelInfoDetailDto> list) {
        final int size = this.mPhotos.size();
        this.mPhotos.addAll(list);
        ((RecyclerView.Adapter) this).notifyItemRangeInserted(size, list.size());
    }


    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        num ++ ;
//        LogUtil.w("photoadapter", "new SimpleDraweeView num = " + num);

        return new PhotoViewHolder(new PhotoView(parent.getContext()));
//        SimpleDraweeView draweeView = new SimpleDraweeView(parent.getContext());
//        GenericDraweeHierarchyBuilder builder =
//                new GenericDraweeHierarchyBuilder(parent.getResources());
//        GenericDraweeHierarchy hierarchy  = builder
//                .setFadeDuration(300)
//                .setPlaceholderImage(Drawables.sPlaceholderDrawable)
//                .setFailureImage(Drawables.sErrorDrawable)
//                .build();
//        draweeView.setHierarchy(hierarchy);
//        draweeView.setLayoutParams(params);
//        return new PhotoViewHolder(draweeView);

    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {
//        SimpleDraweeView draweeView = ((SimpleDraweeView) holder.itemView);
//        draweeView.setImageURI(Uri.parse(mPhotos.get(position).photo));
        ((PhotoView) holder.itemView).bind(mPhotos.get(position).photo);
        holder.itemView.setTag(mPhotos.get(position).photo);
    }

    @Override
    public int getItemCount() {
        return this.mPhotos.size();
    }


//    public void setOnPhotoClickListener(final OnPhotoClickListener mOnPhotoClickListener) {
//        this.mOnPhotoClickListener = mOnPhotoClickListener;
//    }
//
//    public void setOnPhotoLongPressListener(final OnPhotoLongPressListener mOnPhotoLongPressListener) {
//        this.mOnPhotoLongPressListener = mOnPhotoLongPressListener;
//    }

//    public interface OnPhotoClickListener
//    {
//        void onPhotoClick(View p0, Photo p1, int p2);
//    }
//
//    public interface OnPhotoLongPressListener
//    {
//        void onPhotoLongPressListener(View p0, Photo p1, int p2);
//    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder {
        public PhotoViewHolder(final View view) {
            super(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    scaleUpAnimation(v);
//                    Intent intent = new Intent(context, ChannelPhotoDetailActivity.class);
//                    intent.putExtra("photo", v.getTag().toString());
//                    context.startActivity(intent);
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
        Context context = view.getContext();

        Size detailSize = new Size(MeasUtils.getDisplayWidth(context), view.getHeight() *
                (MeasUtils.getDisplayWidth(context) / view.getWidth()));

        //让新的Activity从一个小的范围扩大到全屏
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeScaleUpAnimation(view, detailSize.getWidth()//The View that the new activity is animating from
                        , detailSize.getHeight(), //拉伸开始的坐标
                        0, 0);//拉伸开始的区域大小，这里用（0，0）表示从无到全屏

        Intent intent = new Intent(context, ChannelPhotoDetailActivity.class);
        intent.putExtra("photo", view.getTag().toString());
        intent.putExtra("size", detailSize);
        ActivityCompat.startActivity((Activity) context, intent, options.toBundle());
    }

}
