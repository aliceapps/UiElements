package com.aliceapps.uielements.recyclerview;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewBinding {

    @BindingAdapter("adapter")
    public static void setAdapter(@NonNull RecyclerView view, RecyclerView.Adapter<?> adapter) {
        view.setAdapter(adapter);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
        view.setLayoutManager(mLayoutManager);
    }

    @BindingAdapter("decoration")
    public static void setDecoration(@NonNull RecyclerView view, RecyclerView.ItemDecoration decoration) {
        view.addItemDecoration(decoration);
    }

}
