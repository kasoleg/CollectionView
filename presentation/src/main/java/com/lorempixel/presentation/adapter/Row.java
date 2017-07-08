package com.lorempixel.presentation.adapter;

import java.util.List;

public class Row {
    private List<SpanSize> mSpans;
    private int mRows;

    public Row(List<SpanSize> spans, int rows) {
        this.mSpans = spans;
        this.mRows = rows;
    }

    public List<SpanSize> getSpans() {
        return mSpans;
    }

    public int getRows() {
        return mRows;
    }
}
