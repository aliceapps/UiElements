package com.aliceapps.uielements.autocomplete;

import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;

import com.aliceapps.rxjavautils.AutoDisposable;
import com.aliceapps.rxjavautils.BaseSchedulerProvider;
import com.aliceapps.rxjavautils.MainSchedulerProvider;
import com.aliceapps.uielements.utility.ViewHelpers;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.disposables.Disposable;

public class AutoCompleteBinding {

    @BindingAdapter(value = {"android:entries", "app:entries_layout"}, requireAll = false)
    public static void setEntriesList(@NonNull AutoCompleteTextView view, @NonNull Object obj, int entries_layout) {
        if (obj instanceof Maybe) {
            Maybe<List<String>> entries = (Maybe<List<String>>) obj;
            loadObservables(view, entries_layout, entries);
        } else if (obj instanceof List) {
            List<String> entries = (List<String>) obj;
            loadAdapter(view, entries_layout, entries);
        }
    }

    private static void loadObservables(@NonNull AutoCompleteTextView view, int entries_layout, Maybe<List<String>> entries) {
        Activity foundActivity = ViewHelpers.getActivity(view);
        if (foundActivity instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) foundActivity;
            BaseSchedulerProvider schedulerProvider = new MainSchedulerProvider();
            AutoDisposable autoDisposable = new AutoDisposable(activity.getLifecycle());
            Disposable d = entries.subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .subscribe(strings -> {
                        if (strings.size() > 0) {
                            loadAdapter(view, entries_layout, strings);
                        }
                    });
            try {
                autoDisposable.add(d);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    private static void loadAdapter(@NonNull AutoCompleteTextView view, int entries_layout, List<String> entries) {
        ArrayAdapter<String> adapter;
        if (entries_layout != 0)
            adapter = new ArrayAdapter<>(view.getContext(), entries_layout, entries);
        else
            adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1, entries);
        view.setAdapter(adapter);
    }
}
