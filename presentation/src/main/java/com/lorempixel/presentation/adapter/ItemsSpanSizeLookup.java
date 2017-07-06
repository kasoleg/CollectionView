package com.lorempixel.presentation.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ItemsSpanSizeLookup implements SpannedGridLayoutManager.SpanSizeLookup {
    private final Random random = new Random();
    private List<SpanSize> rows = new ArrayList();

    public ItemsSpanSizeLookup() {
        //rows.add(new SpanSize(2, 2));
        //rows.add(new SpanSize(1, 2));
        //rows.add(new SpanSize(1, 2));
        //rows.add(new SpanSize(1, 2));
        //rows.add(new SpanSize(1, 2));

        rows.add(new SpanSize(2, 4));
        rows.add(new SpanSize(1, 2));
        rows.add(new SpanSize(1, 2));

        rows.add(new SpanSize(1, 2));
        rows.add(new SpanSize(1, 2));
        rows.add(new SpanSize(1, 2));

        rows.add(new SpanSize(1, 3));
        rows.add(new SpanSize(1, 3));

        rows.add(new SpanSize(1, 2));
        rows.add(new SpanSize(2, 4));
        rows.add(new SpanSize(1, 2));
    }

    @Override
    public SpanSize getSpanSize(int position) {
        return rows.get(position % rows.size());
    }

    private List<SpanSize> generateRowSpans() {
        List<SpanSize> row = new ArrayList<>();
        switch (random.nextInt(4)) {
            case 0:
                row.add(new SpanSize(1, 1));
                row.add(new SpanSize(1, 1));
                row.add(new SpanSize(2, 2));
                break;
            case 1:
                row.add(new SpanSize(2, 2));
                row.add(new SpanSize(1, 1));
                row.add(new SpanSize(1, 1));
                break;
            case 2:
                row.add(new SpanSize(2, 2));
                row.add(new SpanSize(2, 2));
                break;
            case 3:
                row.add(new SpanSize(1, 1));
                row.add(new SpanSize(1, 1));
                row.add(new SpanSize(1, 1));
                break;
        }
        return row;
    }
}
