package com.lorempixel.presentation.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

    private View mAnchorView;

    private SparseArray<View> cachedViews = new SparseArray<>();
    private SparseArray<Span> spans = new SparseArray<>();

    public interface SpanSizeLookup {
        SpanSize getSpanSize(int position);
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
        View anchorView = getAnchorView();
        int childsCount = getChildCount();

        if (anchorView != null) {
            mAnchorView = anchorView;
        }

        cachedViews.clear();

        for (int i = 0; i < childsCount; i++) {
            View view = getChildAt(i);
            int pos = getPosition(view);
            cachedViews.put(pos, view);
        }

        for (int i = 0; i < cachedViews.size(); i++) {
            detachView(cachedViews.valueAt(i));
        }

        filUp(mAnchorView, recycler);
        fillDown(mAnchorView, recycler);

        for (int i = 0; i < cachedViews.size(); i++) {
            recycler.recycleView(cachedViews.valueAt(i));
        }
    }

    private void filUp2(View anchorView, RecyclerView.Recycler recycler) {
        int adapterPosition;
        int position = 0;
        int left, right, top, bottom;
        int widthSpec;
        int heightSpec;
        if (anchorView != null) {
            position = getPosition(anchorView);
        }

        position--;

        int column = mColumns - 1;

        while (position >= 0) {
            adapterPosition = recycler.convertPreLayoutPositionToPostLayout(position);
            SpanSize spanSize = mSpanSizeLookup.getSpanSize(adapterPosition);

            left = getWidth() / mColumns * spans.get(position).getColumn();
            top = cellHeight * spans.get(position).getRow();
            right = left + getWidth() / mColumns * spanSize.getColumns();
            bottom = top + cellHeight * spanSize.getRows();

            if ((column == 0 || column == mColumns - 1) && bottom < -mOffset) {
                break;
            }

            View view = cachedViews.get(position);
            if (view == null) {
                view = recycler.getViewForPosition(position);
                addView(view, 0);

                widthSpec = View.MeasureSpec.makeMeasureSpec(right - left, View.MeasureSpec.EXACTLY);
                heightSpec = View.MeasureSpec.makeMeasureSpec(bottom - top, View.MeasureSpec.EXACTLY);
                measureChildWithDecorationsAndMargin(view, widthSpec, heightSpec);

                layoutDecorated(view, left, top + mOffset, right, bottom + mOffset);

                //columns.put(position, new Span(row, column));
            } else {
                attachView(view);
                cachedViews.remove(position);
            }

            column -= spanSize.getColumns();
            position--;
        }
    }

    private void filUp(View anchorView, RecyclerView.Recycler recycler) {
        int position = 0;
        int row = 0;
        int column = 0;
        if (anchorView != null) {
            position = getPosition(anchorView);
            row = spans.get(position).getRow() - 1;
            column = mColumns - 1;
        }

        int adapterPosition;
        int widthSpec;
        int heightSpec;
        int top = 0, left, right, bottom = 0;
        int[] rowMeasurements = new int[mColumns];
        int anchorRow = row;

        for (int i = 0; i < mColumns; i++) {
            rowMeasurements[i] = row + 1;
        }

        if (position == 3) {
            int a = 0;
            a++;
        }

        position--;

        while (position >= 0) {
            adapterPosition = recycler.convertPreLayoutPositionToPostLayout(position);
            SpanSize spanSize = mSpanSizeLookup.getSpanSize(adapterPosition);

            while (rowMeasurements[column] < row) {
                column--;
            }

            right = getWidth() - (mColumns - column - 1) * (getWidth() / mColumns);
            left = right - getWidth() / mColumns * spanSize.getColumns();
            top = cellHeight * row;
            bottom = top + cellHeight * spanSize.getRows();

            if (column - spanSize.getColumns() == 0 && bottom < -mOffset) {
                break;
            }

            if (bottom > top - mOffset) {

                View view = cachedViews.get(position);
                if (view == null) {
                    view = recycler.getViewForPosition(position);
                    addView(view);

                    widthSpec = View.MeasureSpec.makeMeasureSpec(right - left, View.MeasureSpec.EXACTLY);
                    heightSpec = View.MeasureSpec.makeMeasureSpec(bottom - top, View.MeasureSpec.EXACTLY);
                    measureChildWithDecorationsAndMargin(view, widthSpec, heightSpec);

                    layoutDecorated(view, left, top + mOffset, right, bottom + mOffset);

                    //columns.put(position, new Span(row, column));
                } else {
                    attachView(view);
                    cachedViews.remove(position);
                }

                Log.d("MEASURE", rowMeasurements[0] + " " + rowMeasurements[1] + " " + rowMeasurements[2] + " " + rowMeasurements[3] + " " + rowMeasurements[4] + " " + rowMeasurements[5]);
                for (int i = 0; i < spanSize.getColumns(); i++) {
                    rowMeasurements[column - i] -= spanSize.getRows();
                }

                row -= spanSize.getRows();
            } else {
                row = anchorRow;
                column = mColumns - 1;
            }

            position--;
        }
    }

    private void fillDown(View anchorView, RecyclerView.Recycler recycler) {
        int position = 0;
        int row = 0;
        int column = 0;
        if (anchorView != null) {
            position = getPosition(anchorView);
            row = spans.get(position).getRow();
            column = 0;
        }
        int itemsCount = getItemCount();
        int adapterPosition;
        int widthSpec;
        int heightSpec;
        int top = 0, left, right, bottom = 0;
        int height = getHeight();
        int[] rowMeasurements = new int[mColumns];

        if (spans.size() > 0) {
            for (int i = 0; i < mColumns; i++) {
                rowMeasurements[i] = spans.get(position).getRow();
            }
        }

        while (position < itemsCount) {

            adapterPosition = recycler.convertPreLayoutPositionToPostLayout(position);
            SpanSize spanSize = mSpanSizeLookup.getSpanSize(adapterPosition);

            if (column + spanSize.getColumns() > mColumns) {
                row++;
                column = 0;
            }

            while (rowMeasurements[column] > row) {
                column++;
                if (column + spanSize.getColumns() > mColumns) {
                    row++;
                    column = 0;
                }
            }

            left = getWidth() / mColumns * column;
            top = cellHeight * row;
            right = left + getWidth() / mColumns * spanSize.getColumns();
            bottom = top + cellHeight * spanSize.getRows();

            if ((column == 0 || column == mColumns - 1) && top > height - mOffset) {
                Log.d("COLUMN", position + " " + column);
                break;
            }

            spans.put(position, new Span(row, column));

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

            for (int i = 0; i < spanSize.getColumns(); i++) {
                rowMeasurements[i + column] += spanSize.getRows();
            }

            column += spanSize.getColumns();
            position++;
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
            if (firstViewAdapterPos > 0) { //если верхняя вюшка не самая первая в адаптере
                delta = dy;
            } else { //если верхняя вьюшка самая первая в адаптере и выше вьюшек больше быть не может
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

    private View getAnchorView() {
        /*int childCount = getChildCount();
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        View centerView = null;
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            int top = getDecoratedTop(view);
            int bottom = getDecoratedBottom(view);
            int left = getDecoratedLeft(view);
            int right = getDecoratedRight(view);
            if (left < centerX && right > centerX && top < centerY && bottom > centerY) {
                centerView = view;
            }
        }
        return centerView;*/
        int childCount = getChildCount();
        View firstView = null;
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            int bottom = getDecoratedBottom(view);
            if (bottom >= 0) {
                firstView = view;
                break;
            }
        }
        return firstView;
    }
}
