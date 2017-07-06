package com.lorempixel.presentation.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lorempixel.presentation.R;
import com.lorempixel.presentation.databinding.ItemLayoutBinding;
import com.lorempixel.presentation.model.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {
    private LayoutInflater mLayoutInflater;
    private List<Item> mItemList = new ArrayList<>();

    public ItemsAdapter(LayoutInflater inflater) {
        mLayoutInflater = inflater;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        ViewGroup.LayoutParams layoutParams = holder.binding.cardView.getLayoutParams();
        int width = layoutParams.width;
        int height = layoutParams.height;
        Item item = mItemList.get(position);
        item.setTitle(String.valueOf(position));
        holder.binding.setItem(item);
        /*Picasso.with(holder.binding.itemImage.getContext())
                .load("http://lorempixel.com/" + 1920 + "/" + 1080 + "/?position=" + position)
                .fit()
                .centerCrop()
                .into(holder.binding.itemImage);*/
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
