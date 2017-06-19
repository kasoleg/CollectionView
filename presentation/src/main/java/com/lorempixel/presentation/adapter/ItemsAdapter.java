package com.lorempixel.presentation.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lorempixel.presentation.R;
import com.lorempixel.presentation.databinding.ItemLayoutBinding;
import com.lorempixel.presentation.model.Item;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {
    private LayoutInflater mLayoutInflater;
    private GridLayoutManager mGridLayoutManager;
    private int mSpanCount;
    private int mSumSpanCount;
    private List<Item> mItemList = new ArrayList<>();
    private Map<Integer, Integer> mItemHeights = new HashMap<>();
    private int mCurrentHeight;

    public ItemsAdapter(LayoutInflater inflater, GridLayoutManager gridLayoutManager, int spanCount) {
        mLayoutInflater = inflater;
        mGridLayoutManager = gridLayoutManager;
        mSpanCount = spanCount;
        mSumSpanCount = 0;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Item item = mItemList.get(position);
        int spanSize = mGridLayoutManager.getSpanSizeLookup().getSpanSize(position);
        ViewGroup.LayoutParams layoutParams = holder.binding.itemImage.getLayoutParams();
        layoutParams.width = mGridLayoutManager.getWidth() / mSpanCount * spanSize;
        if (mItemHeights.containsKey(position)) {
            layoutParams.height = mItemHeights.get(position);
        } else {
            if (mSumSpanCount % mSpanCount == 0) {
                mCurrentHeight = (new Random().nextInt(300) + 300);
            }
            layoutParams.height = mCurrentHeight;
            mItemHeights.put(position, mCurrentHeight);
            mSumSpanCount += spanSize;
        }
        holder.binding.itemTitle.setTextSize(spanSize * 7);
        holder.binding.itemImage.setLayoutParams(layoutParams);
        holder.binding.setItem(item);
        Picasso.with(holder.binding.itemImage.getContext())
                .load("http://lorempixel.com/" + layoutParams.width + "/" + layoutParams.height + "/?position=" + position)
                .into(holder.binding.itemImage);
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    public void addItems(List<Item> items) {
        mItemList.addAll(items);
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        ItemLayoutBinding binding;

        public ItemViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
