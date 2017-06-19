package com.lorempixel.presentation.presenter;

import com.lorempixel.presentation.model.Item;
import com.lorempixel.presentation.view.ItemsView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ItemsPresenter {
    private ItemsView mItemsView;

    public ItemsPresenter(ItemsView mItemsView) {
        this.mItemsView = mItemsView;
    }

    private String[] titles = {"Tree Of The Sea",
            "Hero Of The Ancestors",
            "Witches Of The Gods",
            "Turtles Without Shame",
            "Invaders And Traitors",
            "Turtles And Rats",
            "Source Without A Conscience",
            "Inception Of The Forsaken",
            "Helping Technology",
            "Prepare For My Husband",
            "Agent With Honor",
            "Foe Of Earth",
            "Serpents Of Fortune",
            "Foes Of The Light",
            "Vultures And Men",
            "Soldiers And Lions",
            "Future Of The Sea",
            "Annihilation Of Fortune",
            "Escaping My Future",
            "Temptations In The Shadows",
            "Witch With Strength",
            "Raven Of Reality",
            "Swindlers Of Fire",
            "Creators Of The Void",
            "Creators And Butchers",
            "Vultures And Rats",
            "Staff Of The Stars",
            "Surprise Of Yesterday",
            "Guarded By My Dreams",
            "Begging In The City"};

    public void getItems() {
        List<Item> items = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 50; i++) {
            String title = titles[random.nextInt(titles.length - 1)];
            items.add(new Item(title));
        }

        mItemsView.onItemsLoaded(items);
    }


}
