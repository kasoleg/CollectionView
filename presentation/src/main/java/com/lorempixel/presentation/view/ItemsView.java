package com.lorempixel.presentation.view;

import com.lorempixel.presentation.model.Item;

import java.util.List;

public interface ItemsView {
    void onItemsLoaded(List<Item> items);
}
