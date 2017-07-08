package com.lorempixel.presentation.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

public class SpannedGridLayoutManager extends RecyclerView.LayoutManager {
    /**
     * columns count
     */
    private final int mColumns;

    private int cellHeight;

    private int mOffset = 0;

    private SpanSizeLookup mSpanSizeLookup;

    private SparseArray<View> cachedViews = new SparseArray<>();

    private int mAnchorTemplate = 0;

    private int mAnchorRow = 0;

    private int mLastOffset = 0;

    private int mPosition = 0;

    public interface SpanSizeLookup {
        Row getSpanSize(int position);
    }

    public SpannedGridLayoutManager(SpanSizeLookup spanSizeLookup, int columns) {
        this.mColumns = columns;
        this.mSpanSizeLookup = spanSizeLookup;
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        detachAndScrapAttachedViews(recycler);
        if (getItemCount() > 0) {
            cellHeight = getCellHeight(recycler.getViewForPosition(0));
        }
        fill(recycler);
    }

    private void fill(RecyclerView.Recycler recycler) {
        int childsCount = getChildCount();

        cachedViews.clear();

        for (int i = 0; i < childsCount; i++) {
            View view = getChildAt(i);
            int pos = getPosition(view);
            cachedViews.put(pos, view);
        }

        for (int i = 0; i < cachedViews.size(); i++) {
            detachView(cachedViews.valueAt(i));
        }

        fillDown(recycler);

        for (int i = 0; i < cachedViews.size(); i++) {
            recycler.recycleView(cachedViews.valueAt(i));
        }
    }

    private void fillDown(RecyclerView.Recycler recycler) {
        int position = mPosition;
        int row = mAnchorRow;
        int itemsCount = getItemCount();
        int left, right, top, bottom = 0;
        int cellWidth = getWidth() / mColumns;
        int widthSpec;
        int heightSpec;
        int height = getHeight();
        int template = mAnchorTemplate;

        while (position < itemsCount) {
            Row rowSpan = mSpanSizeLookup.getSpanSize(template);

            for (SpanSize spanSize : rowSpan.getSpans()) {
                left = cellWidth * spanSize.getColumn();
                top = row * cellHeight + spanSize.getRow() * cellHeight;
                right = left + cellWidth * spanSize.getColumns();
                bottom = top + cellHeight * spanSize.getRows();

                View view = cachedViews.get(position);
                if (view == null) {
                    view = recycler.getViewForPosition(position);
                    addView(view);

                    widthSpec = View.MeasureSpec.makeMeasureSpec(right - left, View.MeasureSpec.EXACTLY);
                    heightSpec = View.MeasureSpec.makeMeasureSpec(bottom - top, View.MeasureSpec.EXACTLY);
                    measureChildWithDecorationsAndMargin(view, widthSpec, heightSpec);

                    layoutDecorated(view, left, top + mOffset, right, bottom + mOffset);
                } else {
                    attachView(view);
                    cachedViews.remove(position);
                }

                position++;
            }

            if (bottom > height - mOffset) {
                return;
            }

            row += rowSpan.getRows();
            template++;
        }
    }

    private void measureChildWithDecorationsAndMargin(View child, int widthSpec, int heightSpec) {
        Rect decorRect = new Rect();
        calculateItemDecorationsForChild(child, decorRect);
        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) child.getLayoutParams();
        widthSpec = updateSpecWithExtra(widthSpec, lp.leftMargin + decorRect.left,
                lp.rightMargin + decorRect.right);
        heightSpec = updateSpecWithExtra(heightSpec, lp.topMargin + decorRect.top,
                lp.bottomMargin + decorRect.bottom);
        child.measure(widthSpec, heightSpec);
    }

    private int updateSpecWithExtra(int spec, int startInset, int endInset) {
        if (startInset == 0 && endInset == 0) {
            return spec;
        }
        final int mode = View.MeasureSpec.getMode(spec);
        if (mode == View.MeasureSpec.AT_MOST || mode == View.MeasureSpec.EXACTLY) {
            return View.MeasureSpec.makeMeasureSpec(
                    View.MeasureSpec.getSize(spec) - startInset - endInset, mode);
        }
        return spec;
    }

    private int getCellHeight(View view) {
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
        int height = layoutParams.height;
        if (height == ViewGroup.LayoutParams.MATCH_PARENT && height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            height = 500;
        }
        return height;
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int delta = scrollBy(dy);
        delta = -delta;
        mOffset += delta;
        Row row = mSpanSizeLookup.getSpanSize(mAnchorTemplate);
        if (-mOffset - mLastOffset > row.getRows() * cellHeight) {
            mPosition += row.getSpans().size();
            mAnchorRow += row.getRows();
            mAnchorTemplate++;
            mLastOffset += row.getRows() * cellHeight;
        } else if (-mOffset - mLastOffset < 0) {
            row = mSpanSizeLookup.getSpanSize(mAnchorTemplate - 1);
            mPosition -= row.getSpans().size();
            mAnchorRow -= row.getRows();
            mAnchorTemplate--;
            mLastOffset -= row.getRows() * cellHeight;
        }

        offsetChildrenVertical(delta);
        fill(recycler);
        return delta;
    }

    private int scrollBy(int dy) {
        int childCount = getChildCount();
        int itemCount = getItemCount();
        if (childCount == 0) {
            return 0;
        }

        final View topView = getChildAt(0);
        final View bottomView = getChildAt(childCount - 1);

        int viewSpan = getDecoratedBottom(bottomView) - getDecoratedTop(topView);
        if (viewSpan <= getHeight() + mOffset) {
            return 0;
        }

        int delta = 0;
        if (dy < 0) {
            View firstView = getChildAt(0);
            int firstViewAdapterPos = getPosition(firstView);
            if (firstViewAdapterPos > 0) {
                delta = dy;
            } else {
                int viewTop = getDecoratedTop(firstView);
                delta = Math.max(viewTop, dy);
            }
        } else if (dy > 0) {
            View lastView = getChildAt(childCount - 1);
            int lastViewAdapterPos = getPosition(lastView);
            if (lastViewAdapterPos < itemCount - 1) {
                delta = dy;
            } else {
                int viewBottom = getDecoratedBottom(lastView);
                int parentBottom = getHeight();
                delta = Math.min(viewBottom - parentBottom, dy);
            }
        }
        return delta;
    }
}
