package com.aliceapps.uielements.views;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;

public class BaseViewBinding {
    @BindingAdapter("android:tag")
    public static void setTag(@NonNull View view, String tag) {
        view.setTag(tag);
    }

    @BindingAdapter("android:background")
    public static void setBackground(@NonNull View view, int image) {
        view.setBackgroundResource(image);
    }
}
