package com.lorempixel.presentation.adapter;

import android.support.v7.widget.GridLayoutManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ItemsSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {
    private int countSpans;
    private int sumSpans;
    private Random random = new Random();
    private Map<Integer, Integer> set = new HashMap<>();

    public ItemsSpanSizeLookup(int countSpans) {
        this.countSpans = countSpans;
    }

    @Override
    public int getSpanSize(int position) {
        if (set.containsKey(position)) {
            return set.get(position);
        }

        int min = 1;
        int max = countSpans - sumSpans;

        int currentCountSpans = random.nextInt(max) + min;
        sumSpans += currentCountSpans;
        if (sumSpans == countSpans) {
            sumSpans = 0;
        }
        set.put(position, currentCountSpans);
        return currentCountSpans;
    }
}
