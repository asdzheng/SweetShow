package com.asdzheng.sweetshow.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.view.View;
import android.view.ViewGroup;

import com.asdzheng.sweetshow.bean.NewChannelInfoDetailDto;
import com.asdzheng.sweetshow.imageloaders.ShowImageLoader;
import com.asdzheng.sweetshow.ui.view.PhotoView;
import com.asdzheng.sweetshow.utils.StringUtil;
import com.asdzheng.sweetshow.utils.recyclerview.AspectRatioLayoutSizeCalculator;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.List;

/**
 * Created by asdzheng on 2015/12/28.
 */
public class PhotosAdapter extends RecyclerArrayAdapter<NewChannelInfoDetailDto> implements AspectRatioLayoutSizeCalculator.SizeCalculatorDelegate {
    protected static final int INVALID_ITEM_POSITION = -1;
    private List<NewChannelInfoDetailDto> mPhotos;


    private ArrayMap<String, Double> photoAspectRatios;

    public PhotosAdapter(Context context) {
        super(context);
    }

//    public PhotosAdapter() {
//        this();
//    }

//    public PhotosAdapter(List<NewChannelInfoDetailDto> mPhotos) {
//        this.mPhotos = mPhotos;
//    }

    private void prefetchPhotos(@NonNull final List<String> list) {
        ShowImageLoader.getSharedInstance().prefetch(list);
    }

    @Override
    public double aspectRatioForIndex(final int n) {

        if (n < getCount()) {
            final NewChannelInfoDetailDto info = getItem(n);
            double ratio = StringUtil.getAspectRadioFromUrl(info.photo);
            return ratio;
        }
        return 1.0;
    }

    public void bind(@NonNull final List<NewChannelInfoDetailDto> mPhotos) {
        this.mPhotos = mPhotos;

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

    public void bindNext(@NonNull final List<NewChannelInfoDetailDto> list) {
        final int size = this.mPhotos.size();
        this.mPhotos.addAll(list);
        ((RecyclerView.Adapter) this).notifyItemRangeInserted(size, list.size());
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new PhotoViewHolder(new PhotoView(getContext()));
    }

//    @Override
//    public int getItemCount() {
//        return this.mPhotos.size();
//    }

//    public int getPositionForPhoto(final int n) {
//        int n2 = -1;
//        for (int i = 0; i < this.mPhotos.size(); ++i) {
//            if (this.mPhotos.get(i).getId() == n) {
//                n2 = i;
//                break;
//            }
//        }
//        return n2;
//    }

//    @Override
//    public void onBindViewHolder(final PhotoViewHolder photoViewHolder, final int n) {
//        ((PhotoView) photoViewHolder.itemView).bind(this.mPhotos.get(n).photo);
//    }

//    @Override
//    public PhotoViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
//        return new PhotoViewHolder(new PhotoView(viewGroup.getContext()));
//    }

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

    public class PhotoViewHolder extends BaseViewHolder<NewChannelInfoDetailDto>
    {

        PhotoView photo;


        public PhotoViewHolder(final View view) {
            super(view);
            photo = (PhotoView)view;
        }

        @Override
        public void setData(NewChannelInfoDetailDto data) {
            super.setData(data);

            photo.bind(data.photo);
        }
    }

    public ArrayMap<String, Double> getPhotoAspectRatios() {
        return photoAspectRatios;
    }

    public void setPhotoAspectRatios(ArrayMap<String, Double> photoAspectRatios) {
        this.photoAspectRatios = photoAspectRatios;
    }
}
