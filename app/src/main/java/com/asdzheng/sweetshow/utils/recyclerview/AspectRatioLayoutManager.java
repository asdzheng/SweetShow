package com.asdzheng.sweetshow.utils.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

import com.asdzheng.sweetshow.utils.LogUtil;

/**
 * Created by asdzheng on 2015/12/26.
 */
public class AspectRatioLayoutManager extends RecyclerView.LayoutManager {
    private static final String TAG;
    //第一个可见的position
    private int mFirstVisiblePosition;
    //最后一个可见的position
    private int mLastVisiblePosition;

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
        return getHeight() - getPaddingTop() - getPaddingBottom();
    }

    private int getContentWidth() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }

    private int preFillGrid(final Direction direction, final int dy, final int n2, final RecyclerView.Recycler recycler, final RecyclerView.State state) {
        final int firstChildPositionForRow = this.mSizeCalculator.getFirstChildPositionForRow(this.mFirstVisibleRow);
//        LogUtil.i(TAG, "mFirstVisibleRow = " + mFirstVisibleRow  + " | firstChildPositionForRow = " + firstChildPositionForRow+ "| mFirstVisiblePosition = " + mFirstVisiblePosition) ;
        final SparseArray sparseArray = new SparseArray(getChildCount());
        final int paddingLeft = getPaddingLeft();
        int decoratedTop = n2 + getPaddingTop();
        if (getChildCount() != 0) {
            decoratedTop = getDecoratedTop(getChildAt(0));
            if (this.mFirstVisiblePosition != firstChildPositionForRow) {
                switch (direction) {
                    case UP:
                        decoratedTop -= this.mSizeCalculator.sizeForChildAtPosition(-1 + this.mFirstVisiblePosition).getHeight();
                        LogUtil.i(TAG, "UP = " + decoratedTop);
                        break;
                    case DOWN:
                        decoratedTop += this.mSizeCalculator.sizeForChildAtPosition(this.mFirstVisiblePosition).getHeight();
                        LogUtil.i(TAG, "DOWN = " + decoratedTop);
                        break;
                }
            }
            for (int i = 0; i < getChildCount(); ++i) {
                sparseArray.put(i + this.mFirstVisiblePosition, (Object) getChildAt(i));
            }
            for (int j = 0; j < sparseArray.size(); ++j) {
                detachView((View) sparseArray.valueAt(j));
            }
        }
        this.mFirstVisiblePosition = firstChildPositionForRow;
        int childPaddingLeft = paddingLeft;
        int childPaddingTop = decoratedTop;

        for (int mFirstVisiblePosition = this.mFirstVisiblePosition; mFirstVisiblePosition >= 0 && mFirstVisiblePosition < state.getItemCount(); ++mFirstVisiblePosition) {
            final Size sizeForChildAtPosition = this.mSizeCalculator.sizeForChildAtPosition(mFirstVisiblePosition);
            //是否加上下一个view就超过屏幕的宽度
            if (childPaddingLeft + sizeForChildAtPosition.getWidth() > this.getContentWidth()) {
                childPaddingLeft = paddingLeft;
                childPaddingTop += this.mSizeCalculator.sizeForChildAtPosition(mFirstVisiblePosition - 1).getHeight();
            }
            int n5 = 0;
            switch (direction) {
                default:
                    if (childPaddingTop >= this.getContentHeight()) {
                        n5 = 1;
                        break;
                    }
                    n5 = 0;
                    break;
                case DOWN:
                    if (childPaddingTop >= dy + this.getContentHeight()) {
                        n5 = 1;
                    } else {
                        n5 = 0;
                    }
                    break;
            }

//            LogUtil.i(TAG, "childPaddingTop = " + childPaddingTop + " | dy = " + dy + " | getContentHeight = " + getContentHeight() + " | n5 = " + n5);

            if (n5 != 0) {
                break;
            }
            final View view = (View) sparseArray.get(mFirstVisiblePosition);
            if (view == null) {
                final View viewForPosition = recycler.getViewForPosition(mFirstVisiblePosition);
//               LogUtil.i(TAG, "view == null mFirstVisiblePosition = " + mFirstVisiblePosition + " | sizeForChildAtPosition = " + sizeForChildAtPosition );
                addView(viewForPosition);
                measureChildWithMargins(viewForPosition, 0, 0);
                layoutDecorated(viewForPosition, childPaddingLeft, childPaddingTop, childPaddingLeft + sizeForChildAtPosition.getWidth(), childPaddingTop + sizeForChildAtPosition.getHeight());
            } else {
//                LogUtil.i(TAG, "view != null mFirstVisiblePosition = " + mFirstVisiblePosition + " | sizeForChildAtPosition = " + sizeForChildAtPosition );
                attachView(view);
                sparseArray.remove(mFirstVisiblePosition);
            }
            childPaddingLeft += sizeForChildAtPosition.getWidth();
        }
        for (int k = 0; k < sparseArray.size(); ++k) {
            recycler.recycleView((View) sparseArray.valueAt(k));
        }
        final int childCount = getChildCount();
        int bottom = 0;
        if (childCount > 0) {
            bottom = getChildAt(-1 + getChildCount()).getBottom();
        }

        mLastVisiblePosition = mFirstVisiblePosition + childCount - 1;

        return bottom;
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }

    public int findFirstVisibleItemPosition() {
        return this.mFirstVisiblePosition;
    }

    public int getmLastVisiblePosition() {
        return mLastVisiblePosition;
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT);
    }

    public AspectRatioLayoutSizeCalculator getSizeCalculator() {
        return this.mSizeCalculator;
    }

    @Override
    public void onAdapterChanged(final RecyclerView.Adapter adapter, final RecyclerView.Adapter adapter2) {
        removeAllViews();
        this.mSizeCalculator.reset();
    }

    @Override
    public void onLayoutChildren(final RecyclerView.Recycler recycler, final RecyclerView.State state) {
        if (getItemCount() == 0) {
            detachAndScrapAttachedViews(recycler);
            return;
        }
        this.mSizeCalculator.setContentWidth(this.getContentWidth());
        this.mSizeCalculator.reset();
        int decoratedTop;
        if (getChildCount() == 0) {
            this.mFirstVisiblePosition = 0;
            this.mFirstVisibleRow = 0;
            decoratedTop = 0;
        } else {
            final View child = getChildAt(0);
            if (this.mForceClearOffsets) {
                this.mForceClearOffsets = false;
                decoratedTop = 0;
            } else {
                decoratedTop = getDecoratedTop(child);
            }
        }
        detachAndScrapAttachedViews(recycler);
        this.preFillGrid(Direction.NONE, 0, decoratedTop, recycler, state);
    }

    @Override
    public void scrollToPosition(final int n) {
        if (n >= getItemCount()) {
            Log.w(AspectRatioLayoutManager.TAG, String.format("Cannot scroll to %d, item count is %d", n, getItemCount()));
            return;
        }
        this.mForceClearOffsets = true;
        this.mFirstVisibleRow = this.mSizeCalculator.getRowForChildPosition(n);
        this.mFirstVisiblePosition = this.mSizeCalculator.getFirstChildPositionForRow(this.mFirstVisibleRow);
        requestLayout();
    }

    /**
     * Scroll vertically by dy pixels in screen coordinates and return the distance traveled.
     * The default implementation does nothing and returns 0.
     *
     * @param dy       distance to scroll in pixels. Y increases as scroll position
     *                 approaches the bottom.
     * @param recycler Recycler to use for fetching potentially cached views for a
     *                 position
     * @param state    Transient state of RecyclerView
     * @return The actual distance scrolled. The return value will be negative if dy was
     * negative and scrolling proceeeded in that direction.
     * <code>Math.abs(result)</code> may be less than dy if a boundary was reached.
     */
    @Override
    public int scrollVerticallyBy(final int dy, final RecyclerView.Recycler recycler, final RecyclerView.State state) {
        if (getChildCount() == 0 || dy == 0) {
            return 0;
        }
        final View child = getChildAt(0);
        final View child2 = getChildAt(-1 + getChildCount());
        int n2 = this.getContentHeight();
        if (dy > 0) {
            boolean b;
            if (1 + this.mFirstVisiblePosition + getChildCount() >= getItemCount()) {
                b = true;
            } else {
                b = false;
            }
            if (b && n2 <= this.getContentHeight()) {
                this.preFillGrid(Direction.DOWN, Math.abs(dy), 0, recycler, state);
                n2 = getDecoratedBottom(getChildAt(-1 + getChildCount())) - this.getContentHeight();
            } else if (getDecoratedBottom(child) - dy <= 0) {
                ++this.mFirstVisibleRow;
                n2 = this.preFillGrid(Direction.DOWN, Math.abs(dy), 0, recycler, state);
            } else if (getDecoratedBottom(child2) - dy < this.getContentHeight()) {
                n2 = this.preFillGrid(Direction.DOWN, Math.abs(dy), 0, recycler, state);
            }
        } else if (this.mFirstVisibleRow == 0 && getDecoratedTop(child) - dy >= 0) {
            n2 = -getDecoratedTop(child);
        } else if (getDecoratedTop(child) - dy >= 0) {
            --this.mFirstVisibleRow;
            n2 = this.preFillGrid(Direction.UP, Math.abs(dy), 0, recycler, state);
        } else if (getDecoratedTop(child2) - dy > this.getContentHeight()) {
            n2 = this.preFillGrid(Direction.UP, Math.abs(dy), 0, recycler, state);
        }
        int n3;
        if (Math.abs(dy) > n2) {
            n3 = n2 * (int) Math.signum(dy);
        } else {
            n3 = dy;
        }
        offsetChildrenVertical(-n3);
        return n3;
    }

    public void setMaxRowHeight(final int maxRowHeight) {
        this.mSizeCalculator.setMaxRowHeight(maxRowHeight);
    }

    private enum Direction {
        DOWN,
        NONE,
        UP
    }
}
