package com.asdzheng.sweetshow.utils.recyclerview;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.asdzheng.sweetshow.utils.LogUtil;

/**
 * Created by asdzheng on 2015/12/26.
 */
public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration
{
    public static final boolean DEFAULT_EDGE_INCLUSION = true;
    public static final int DEFAULT_SPACING = 32;
    private boolean mIncludeEdge;
    private int mSpacing;
    int count;

    /**
     * 现在只支持垂直排列
     * @param mSpacing
     * @param count
     */
    public GridSpacingItemDecoration(int mSpacing, int count) {
        this.mSpacing = mSpacing;
        this.count = count;
    }

    @Override
    public void getItemOffsets(final Rect rect, final View view, final RecyclerView recyclerView, final RecyclerView.State state) {
//        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
         int itemCount = recyclerView.getAdapter().getItemCount();
         int childAdapterPosition = recyclerView.getChildAdapterPosition(view);

        LogUtil.i("GridSpacingItemDecoration", "childAdapterPosition" + childAdapterPosition);

        childAdapterPosition = childAdapterPosition + 1;
//        int n;
//        if (linearLayoutManager.getOrientation() == 0) {
//            n = 1;
//        }
//        else {
//            n = 0;
//        }
//        int n2;
//        if (this.mIncludeEdge) {
//            n2 = this.mSpacing - childAdapterPosition * this.mSpacing / itemCount;
//        }
//        else {
//            n2 = childAdapterPosition * this.mSpacing / itemCount;
//        }
//        int n3;
//        if (this.mIncludeEdge) {
//            n3 = (childAdapterPosition + 1) * this.mSpacing / itemCount;
//        }
//        else {
//            n3 = this.mSpacing - (childAdapterPosition + 1) * this.mSpacing / itemCount;
//        }
        if(childAdapterPosition % count == 1 || childAdapterPosition % count != count - 1) {
            rect.right = mSpacing;
        }

        rect.left = 0;
        rect.bottom = mSpacing;
        rect.top = 0;

    }
}
