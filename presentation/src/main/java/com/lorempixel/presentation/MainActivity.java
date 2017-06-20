package com.lorempixel.presentation;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lorempixel.presentation.adapter.EndlessItemsScrollListener;
import com.lorempixel.presentation.adapter.ItemsAdapter;
import com.lorempixel.presentation.adapter.ItemsSpanSizeLookup;
import com.lorempixel.presentation.databinding.ActivityMainBinding;
import com.lorempixel.presentation.model.Item;
import com.lorempixel.presentation.presenter.ItemsPresenter;
import com.lorempixel.presentation.view.ItemsView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements ItemsView {
    private ActivityMainBinding mBinding;
    private ItemsAdapter mItemsAdapter;
    private ItemsPresenter mItemsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initializeList();

        mItemsPresenter = new ItemsPresenter(this);

        mItemsPresenter.getItems();
    }

    private void initializeList() {
        int spanCount = 4;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, spanCount);
        gridLayoutManager.setSpanSizeLookup(new ItemsSpanSizeLookup(spanCount));
        mBinding.itemsList.setLayoutManager(gridLayoutManager);
        mItemsAdapter = new ItemsAdapter(getLayoutInflater(), gridLayoutManager, spanCount);
        mBinding.itemsList.setAdapter(mItemsAdapter);
        mBinding.itemsList.addOnScrollListener(new EndlessItemsScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                mItemsPresenter.getItems();
            }
        });
        mBinding.itemsList.setHasFixedSize(true);
    }

    @Override
    public void onItemsLoaded(List<Item> items) {
        mItemsAdapter.addItems(items);
    }
}
