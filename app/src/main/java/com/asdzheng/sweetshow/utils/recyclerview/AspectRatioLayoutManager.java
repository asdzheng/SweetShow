package com.asdzheng.sweetshow.utils.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

/**
 * Created by asdzheng on 2015/12/26.
 */
public class AspectRatioLayoutManager extends RecyclerView.LayoutManager
{
    private static final String TAG;
    private int mFirstVisiblePosition;
    private int mFirstVisibleRow;
    private boolean mForceClearOffsets;
    private AspectRatioLayoutSizeCalculator mSizeCalculator;

    static {
        TAG = AspectRatioLayoutManager.class.getSimpleName();
    }

    public AspectRatioLayoutManager(final AspectRatioLayoutSizeCalculator.SizeCalculatorDelegate sizeCalculatorDelegate) {
        this.mSizeCalculator = new AspectRatioLayoutSizeCalculator(sizeCalculatorDelegate);
    }

    private int getContentHeight() {
        return ((RecyclerView.LayoutManager)this).getHeight() - ((RecyclerView.LayoutManager)this).getPaddingTop() - ((RecyclerView.LayoutManager)this).getPaddingBottom();
    }

    private int getContentWidth() {
        return ((RecyclerView.LayoutManager)this).getWidth() - ((RecyclerView.LayoutManager)this).getPaddingLeft() - ((RecyclerView.LayoutManager)this).getPaddingRight();
    }

    private int preFillGrid(final Direction direction, final int n, final int n2, final RecyclerView.Recycler recycler, final RecyclerView.State state) {
        final int firstChildPositionForRow = this.mSizeCalculator.getFirstChildPositionForRow(this.mFirstVisibleRow);
        final SparseArray sparseArray = new SparseArray(((RecyclerView.LayoutManager)this).getChildCount());
        final int paddingLeft = ((RecyclerView.LayoutManager)this).getPaddingLeft();
        int decoratedTop = n2 + ((RecyclerView.LayoutManager)this).getPaddingTop();
        if (((RecyclerView.LayoutManager)this).getChildCount() != 0) {
            decoratedTop = ((RecyclerView.LayoutManager)this).getDecoratedTop(((RecyclerView.LayoutManager)this).getChildAt(0));
            if (this.mFirstVisiblePosition != firstChildPositionForRow) {
                switch (direction) {
                    case UP:
                        decoratedTop -= this.mSizeCalculator.sizeForChildAtPosition(-1 + this.mFirstVisiblePosition).getHeight();
                        break;
                    case DOWN:
                        decoratedTop += this.mSizeCalculator.sizeForChildAtPosition(this.mFirstVisiblePosition).getHeight();
                        break;
                }
            }
            for (int i = 0; i < ((RecyclerView.LayoutManager)this).getChildCount(); ++i) {
                sparseArray.put(i + this.mFirstVisiblePosition, (Object)((RecyclerView.LayoutManager)this).getChildAt(i));
            }
            for (int j = 0; j < sparseArray.size(); ++j) {
                ((RecyclerView.LayoutManager)this).detachView((View)sparseArray.valueAt(j));
            }
        }
        this.mFirstVisiblePosition = firstChildPositionForRow;
        int n3 = paddingLeft;
        int n4 = decoratedTop;
        for (int mFirstVisiblePosition = this.mFirstVisiblePosition; mFirstVisiblePosition >= 0 && mFirstVisiblePosition < state.getItemCount(); ++mFirstVisiblePosition) {
            final Size sizeForChildAtPosition = this.mSizeCalculator.sizeForChildAtPosition(mFirstVisiblePosition);
            if (n3 + sizeForChildAtPosition.getWidth() > this.getContentWidth()) {
                n3 = paddingLeft;
                n4 += this.mSizeCalculator.sizeForChildAtPosition(mFirstVisiblePosition - 1).getHeight();
            }
            int n5 = 0;
            switch (direction) {
                default:
                    if (n4 >= this.getContentHeight()) {
                        n5 = 1;
                        break;
                    }
                    n5 = 0;
                    break;
                case DOWN:
                    if (n4 >= n + this.getContentHeight()) {
                        n5 = 1;
                    }
                    else {
                        n5 = 0;
                    }
                    break;
            }
            if (n5 != 0) {
                break;
            }
            final View view = (View)sparseArray.get(mFirstVisiblePosition);
            if (view == null) {
                final View viewForPosition = recycler.getViewForPosition(mFirstVisiblePosition);
                ((RecyclerView.LayoutManager)this).addView(viewForPosition);
                ((RecyclerView.LayoutManager)this).measureChildWithMargins(viewForPosition, 0, 0);
                ((RecyclerView.LayoutManager)this).layoutDecorated(viewForPosition, n3, n4, n3 + sizeForChildAtPosition.getWidth(), n4 + sizeForChildAtPosition.getHeight());
            }
            else {
                ((RecyclerView.LayoutManager)this).attachView(view);
                sparseArray.remove(mFirstVisiblePosition);
            }
            n3 += sizeForChildAtPosition.getWidth();
        }
        for (int k = 0; k < sparseArray.size(); ++k) {
            recycler.recycleView((View)sparseArray.valueAt(k));
        }
        final int childCount = ((RecyclerView.LayoutManager)this).getChildCount();
        int bottom = 0;
        if (childCount > 0) {
            bottom = ((RecyclerView.LayoutManager)this).getChildAt(-1 + ((RecyclerView.LayoutManager)this).getChildCount()).getBottom();
        }
        return bottom;
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }

    public int findFirstVisibleItemPosition() {
        return this.mFirstVisiblePosition;
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(-2, -2);
    }

    public AspectRatioLayoutSizeCalculator getSizeCalculator() {
        return this.mSizeCalculator;
    }

    @Override
    public void onAdapterChanged(final RecyclerView.Adapter adapter, final RecyclerView.Adapter adapter2) {
        ((RecyclerView.LayoutManager)this).removeAllViews();
        this.mSizeCalculator.reset();
    }

    @Override
    public void onLayoutChildren(final RecyclerView.Recycler recycler, final RecyclerView.State state) {
        if (((RecyclerView.LayoutManager)this).getItemCount() == 0) {
            ((RecyclerView.LayoutManager)this).detachAndScrapAttachedViews(recycler);
            return;
        }
        this.mSizeCalculator.setContentWidth(this.getContentWidth());
        this.mSizeCalculator.reset();
        int decoratedTop;
        if (((RecyclerView.LayoutManager)this).getChildCount() == 0) {
            this.mFirstVisiblePosition = 0;
            this.mFirstVisibleRow = 0;
            decoratedTop = 0;
        }
        else {
            final View child = ((RecyclerView.LayoutManager)this).getChildAt(0);
            if (this.mForceClearOffsets) {
                this.mForceClearOffsets = false;
                decoratedTop = 0;
            }
            else {
                decoratedTop = ((RecyclerView.LayoutManager)this).getDecoratedTop(child);
            }
        }
        ((RecyclerView.LayoutManager)this).detachAndScrapAttachedViews(recycler);
        this.preFillGrid(Direction.NONE, 0, decoratedTop, recycler, state);
    }

    @Override
    public void scrollToPosition(final int n) {
        if (n >= ((RecyclerView.LayoutManager)this).getItemCount()) {
            Log.w(AspectRatioLayoutManager.TAG, String.format("Cannot scroll to %d, item count is %d", n, ((RecyclerView.LayoutManager)this).getItemCount()));
            return;
        }
        this.mForceClearOffsets = true;
        this.mFirstVisibleRow = this.mSizeCalculator.getRowForChildPosition(n);
        this.mFirstVisiblePosition = this.mSizeCalculator.getFirstChildPositionForRow(this.mFirstVisibleRow);
        ((RecyclerView.LayoutManager)this).requestLayout();
    }

    @Override
    public int scrollVerticallyBy(final int n, final RecyclerView.Recycler recycler, final RecyclerView.State state) {
        if (((RecyclerView.LayoutManager)this).getChildCount() == 0 || n == 0) {
            return 0;
        }
        final View child = ((RecyclerView.LayoutManager)this).getChildAt(0);
        final View child2 = ((RecyclerView.LayoutManager)this).getChildAt(-1 + ((RecyclerView.LayoutManager)this).getChildCount());
        int n2 = this.getContentHeight();
        if (n > 0) {
            boolean b;
            if (1 + this.mFirstVisiblePosition + ((RecyclerView.LayoutManager)this).getChildCount() >= ((RecyclerView.LayoutManager)this).getItemCount()) {
                b = true;
            }
            else {
                b = false;
            }
            if (b && n2 <= this.getContentHeight()) {
                this.preFillGrid(Direction.DOWN, Math.abs(n), 0, recycler, state);
                n2 = ((RecyclerView.LayoutManager)this).getDecoratedBottom(((RecyclerView.LayoutManager)this).getChildAt(-1 + ((RecyclerView.LayoutManager)this).getChildCount())) - this.getContentHeight();
            }
            else if (((RecyclerView.LayoutManager)this).getDecoratedBottom(child) - n <= 0) {
                ++this.mFirstVisibleRow;
                n2 = this.preFillGrid(Direction.DOWN, Math.abs(n), 0, recycler, state);
            }
            else if (((RecyclerView.LayoutManager)this).getDecoratedBottom(child2) - n < this.getContentHeight()) {
                n2 = this.preFillGrid(Direction.DOWN, Math.abs(n), 0, recycler, state);
            }
        }
        else if (this.mFirstVisibleRow == 0 && ((RecyclerView.LayoutManager)this).getDecoratedTop(child) - n >= 0) {
            n2 = -((RecyclerView.LayoutManager)this).getDecoratedTop(child);
        }
        else if (((RecyclerView.LayoutManager)this).getDecoratedTop(child) - n >= 0) {
            --this.mFirstVisibleRow;
            n2 = this.preFillGrid(Direction.UP, Math.abs(n), 0, recycler, state);
        }
        else if (((RecyclerView.LayoutManager)this).getDecoratedTop(child2) - n > this.getContentHeight()) {
            n2 = this.preFillGrid(Direction.UP, Math.abs(n), 0, recycler, state);
        }
        int n3;
        if (Math.abs(n) > n2) {
            n3 = n2 * (int)Math.signum(n);
        }
        else {
            n3 = n;
        }
        ((RecyclerView.LayoutManager)this).offsetChildrenVertical(-n3);
        return n3;
    }

    public void setMaxRowHeight(final int maxRowHeight) {
        this.mSizeCalculator.setMaxRowHeight(maxRowHeight);
    }

    private enum Direction
    {
        DOWN,
        NONE,
        UP;
    }
}
