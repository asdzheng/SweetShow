package com.asdzheng.sweetshow.utils.recyclerview;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by asdzheng on 2015/12/26.
 */
public class AspectRatioLayoutSizeCalculator {

    private static final int DEFAULT_MAX_ROW_HEIGHT = 600;
    private static final int INVALID_CONTENT_WIDTH = -1;
    private static final String TAG;
    private static int mMaxRowHeight;
    private int mContentWidth;
    private List<Integer> mFirstChildPositionForRow;
    private List<Integer> mRowForChildPosition;
    private SizeCalculatorDelegate mSizeCalculatorDelegate;
    private List<Size> mSizeForChildAtPosition;

    static {
        TAG = AspectRatioLayoutSizeCalculator.class.getName();
        AspectRatioLayoutSizeCalculator.mMaxRowHeight = 600;
    }

    public AspectRatioLayoutSizeCalculator(final SizeCalculatorDelegate mSizeCalculatorDelegate) {
        this.mContentWidth = -1;
        this.mSizeCalculatorDelegate = mSizeCalculatorDelegate;
        this.mSizeForChildAtPosition = new ArrayList<Size>();
        this.mFirstChildPositionForRow = new ArrayList<Integer>();
        this.mRowForChildPosition = new ArrayList<Integer>();
    }

    private void computeFirstChildPositionsUpToRow(final int i) {
        while (i >= this.mFirstChildPositionForRow.size()) {
            this.computeChildSizesUpToPosition(1 + this.mSizeForChildAtPosition.size());
        }
    }

    public void computeChildSizesUpToPosition(final int n) {
        if (this.mContentWidth == -1) {
            throw new RuntimeException("Invalid content width. Did you forget to set it?");
        }
        if (this.mSizeCalculatorDelegate == null) {
            throw new RuntimeException("Size calculator delegate is missing. Did you forget to set it?");
        }
        final int size = this.mSizeForChildAtPosition.size();
        int n2;
        if (this.mRowForChildPosition.size() > 0) {
            n2 = 1 + this.mRowForChildPosition.get(-1 + this.mRowForChildPosition.size());
        }
        else {
            n2 = 0;
        }
        double n3 = 0.0;
        int n4 = Integer.MAX_VALUE;
        final ArrayList<Double> list = new ArrayList<Double>();
        for (int n5 = size; n5 < n || n4 > AspectRatioLayoutSizeCalculator.mMaxRowHeight; ++n5) {
            final double aspectRatioForIndex = this.mSizeCalculatorDelegate.aspectRatioForIndex(n5);
            n3 += aspectRatioForIndex;
            list.add(aspectRatioForIndex);
            n4 = (int)Math.ceil(this.mContentWidth / n3);
            if (n4 <= AspectRatioLayoutSizeCalculator.mMaxRowHeight) {
                this.mFirstChildPositionForRow.add(1 + (n5 - list.size()));
                int mContentWidth = this.mContentWidth;
                final Iterator<Double> iterator = list.iterator();
                while (iterator.hasNext()) {
                    final int min = Math.min(mContentWidth, (int)Math.ceil(n4 * iterator.next()));
                    this.mSizeForChildAtPosition.add(new Size(min, n4));
                    this.mRowForChildPosition.add(n2);
                    mContentWidth -= min;
                }
                list.clear();
                n3 = 0.0;
                ++n2;
            }
        }
    }

    int getFirstChildPositionForRow(final int n) {
        if (n >= this.mFirstChildPositionForRow.size()) {
            this.computeFirstChildPositionsUpToRow(n);
        }
        return this.mFirstChildPositionForRow.get(n);
    }

    int getRowForChildPosition(final int n) {
        if (n >= this.mRowForChildPosition.size()) {
            this.computeChildSizesUpToPosition(n);
        }
        return this.mRowForChildPosition.get(n);
    }

    void reset() {
        this.mSizeForChildAtPosition.clear();
        this.mFirstChildPositionForRow.clear();
        this.mRowForChildPosition.clear();
    }

    public void setContentWidth(final int mContentWidth) {
        if (this.mContentWidth != mContentWidth) {
            this.mContentWidth = mContentWidth;
            this.reset();
        }
    }

    public void setMaxRowHeight(final int mMaxRowHeight) {
        if (AspectRatioLayoutSizeCalculator.mMaxRowHeight != mMaxRowHeight) {
            AspectRatioLayoutSizeCalculator.mMaxRowHeight = mMaxRowHeight;
            this.reset();
        }
    }

    Size sizeForChildAtPosition(final int n) {
        if (n >= this.mSizeForChildAtPosition.size()) {
            this.computeChildSizesUpToPosition(n);
        }
        return this.mSizeForChildAtPosition.get(n);
    }

    public interface SizeCalculatorDelegate
    {
        double aspectRatioForIndex(int p0);
    }
}
