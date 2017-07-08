package com.lorempixel.presentation.adapter;

import java.util.ArrayList;
import java.util.List;

public class ItemsSpanSizeLookup implements SpannedGridLayoutManager.SpanSizeLookup {
    private List<Row> mRows = new ArrayList();

    public ItemsSpanSizeLookup() {
        List<SpanSize> template1 = new ArrayList<>();

        template1.add(new SpanSize(2, 4, 0, 0));
        template1.add(new SpanSize(1, 2, 0, 4));
        template1.add(new SpanSize(1, 2, 1, 4));

        mRows.add(new Row(template1, 2));

        List<SpanSize> template2 = new ArrayList<>();

        template2.add(new SpanSize(1, 2, 0, 0));
        template2.add(new SpanSize(1, 2, 0, 2));
        template2.add(new SpanSize(1, 2, 0, 4));

        mRows.add(new Row(template2, 1));

        List<SpanSize> template3 = new ArrayList<>();

        template3.add(new SpanSize(1, 3, 0, 0));
        template3.add(new SpanSize(1, 3, 0, 3));

        mRows.add(new Row(template3, 1));

        List<SpanSize> template4 = new ArrayList<>();

        template4.add(new SpanSize(1, 2, 0, 0));
        template4.add(new SpanSize(1, 2, 1, 0));
        template4.add(new SpanSize(2, 4, 0, 2));

        mRows.add(new Row(template4, 2));
    }

    @Override
    public Row getSpanSize(int position) {
        return mRows.get(position % mRows.size());
    }
}
