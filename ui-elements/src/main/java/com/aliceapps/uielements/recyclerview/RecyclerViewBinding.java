package com.aliceapps.uielements.recyclerview;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Class provides adapter and decoration bindings for RecyclerView
 */
public class RecyclerViewBinding {

    /**
     * Binding adapter for adapter attribute
     * @param view - RecyclerView
     * @param adapter - RecyclerView.Adapter adapter
     */
    @BindingAdapter("adapter")
    public static void setAdapter(@NonNull RecyclerView view, RecyclerView.Adapter<?> adapter) {
        view.setAdapter(adapter);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
        view.setLayoutManager(mLayoutManager);
    }

    /**
     * Binding adapter for adapter attribute
     * @param view - RecyclerView
     * @param adapter - RecyclerView.Adapter adapter
     * @param layoutManager  layout manager for recyclerview
     */
    @BindingAdapter({"adapter", "layout_manager"})
    public static void setAdapterWithLayout(@NonNull RecyclerView view, RecyclerView.Adapter<?> adapter, LinearLayoutManager layoutManager) {
        view.setAdapter(adapter);
        view.setLayoutManager(layoutManager);
    }

    /**
     * Binding adapter for decoration attribute
     * @param view - RecyclerView
     * @param decoration - RecyclerView.ItemDecoration
     */
    @BindingAdapter("decoration")
    public static void setDecoration(@NonNull RecyclerView view, RecyclerView.ItemDecoration decoration) {
        view.addItemDecoration(decoration);
    }

}
