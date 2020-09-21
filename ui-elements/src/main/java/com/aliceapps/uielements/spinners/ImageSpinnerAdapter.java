package com.aliceapps.uielements.spinners;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.aliceapps.uielements.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides custom ArrayAdapter with images for Spinner
 */
public class ImageSpinnerAdapter extends ArrayAdapter<String> {
    LayoutInflater inflater;
    private int viewId;
    private CharSequence[] entries;
    private CharSequence[] values;
    private List<Integer> icons = new ArrayList<>();

    /**
     * Constructor for SimpleSpinnerAdapter
     * @param context - current context
     * @param entries - entries that Spinner shows
     * @param values - values that will be stored in the database
     * @param images - id of the integer array where images are stored
     */
    public ImageSpinnerAdapter(@NonNull Context context, int entries, int values, int images) {
        super(context, R.layout.image_spinner, entries);
        viewId = R.layout.image_spinner;
        inflater = LayoutInflater.from(context);
        this.entries = context.getResources().getTextArray(entries);
        this.values = context.getResources().getTextArray(values);
        TypedArray a = context.getResources().obtainTypedArray(images);
        for (int i = 0; i < a.length(); i++) {
            int id = a.getResourceId(i,-1);
            if (id != -1)
                icons.add(id);
        }
        a.recycle();
    }

    /**
     * Constructor for SimpleSpinnerAdapter
     * @param context - current context
     * @param entries - entries that Spinner shows
     * @param values - values that will be stored in the database
     * @param textViewResourceId - layout ID for spinner adapter
     * @param images - array with images
     */
    public ImageSpinnerAdapter(@NonNull Context context, int entries, int values, int textViewResourceId, List<Integer> images) {
        super(context, textViewResourceId, entries);
        viewId = textViewResourceId;
        inflater = LayoutInflater.from(context);
        this.entries = context.getResources().getTextArray(entries);
        this.values = context.getResources().getTextArray(values);
        icons = images;
    }

    /**
     * Constructor for SimpleSpinnerAdapter
     * @param context - current context
     * @param entries - entries that Spinner shows
     * @param values - values that will be stored in the database
     * @param textViewResourceId - layout ID for spinner adapter
     * @param imagesResource - id of the integer array where images are stored
     */
    public ImageSpinnerAdapter(@NonNull Context context, int entries, int values, int textViewResourceId, int imagesResource) {
        super(context, textViewResourceId, entries);
        viewId = textViewResourceId;
        inflater = LayoutInflater.from(context);
        this.entries = context.getResources().getTextArray(entries);
        this.values = context.getResources().getTextArray(values);
        TypedArray a = context.getResources().obtainTypedArray(imagesResource);
        for (int i = 0; i < a.length(); i++) {
            int id = a.getResourceId(i,-1);
            if (id != -1)
                icons.add(id);
        }
        a.recycle();
    }

    /**
     * Constructor for SimpleSpinnerAdapter
     * @param context - current context
     * @param entries - entries that Spinner shows
     * @param values - values that will be stored in the database
     * @param textViewResourceId - layout ID for spinner adapter
     * @param imagesResource - id of the integer array where images are stored
     */
    public ImageSpinnerAdapter(@NonNull Context context, CharSequence[] entries, CharSequence[] values, int textViewResourceId, int imagesResource) {
        super(context, textViewResourceId);
        viewId = textViewResourceId;
        inflater = LayoutInflater.from(context);
        this.entries = entries;
        this.values = values;
        TypedArray a = context.getResources().obtainTypedArray(imagesResource);
        for (int i = 0; i < a.length(); i++) {
            int id = a.getResourceId(i,-1);
            if (id != -1)
                icons.add(id);
        }
        a.recycle();
    }

    /**
     *
     * @return number of entries in the adapter
     */
    @Override
    public int getCount() {
        return entries.length;
    }

    /**
     * Returns the view created for Spinner adapter
     * @param position - position on current entry
     * @param convertView - spinner view
     * @param parent - parent view
     * @return view created for Spinner adapter
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View spinnerView = convertView;

        if (spinnerView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            spinnerView = inflater.inflate(viewId, parent, false);
        }
        ImageView icon = spinnerView.findViewById(R.id.spinner_icon);
        icon.setImageDrawable(ContextCompat.getDrawable(getContext(), icons.get(position)));
        TextView names = spinnerView.findViewById(R.id.spinner_text);
        names.setText(entries[position]);

        spinnerView.setPadding(0,16,0,8);
        return spinnerView;
    }

    /**
     *
     * Returns dropdown view created for Spinner adapter
     * @param position - position on current entry
     * @param convertView - spinner view
     * @param parent - parent view
     * @return dropdown view created for Spinner adapter
     */
    @Override
    public View getDropDownView (int position, View convertView, @NonNull ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            rowView = inflater.inflate(viewId, parent, false);
        }
        TextView listView = rowView.findViewById(R.id.spinner_text);
        listView.setText(entries[position]);
        ImageView icon = rowView.findViewById(R.id.spinner_icon);
        icon.setImageDrawable(ContextCompat.getDrawable(getContext(), icons.get(position)));
        return rowView;
    }

    /**
     *
     * @return array of values that will be stored in the database
     */
    public CharSequence[] getValues() {
        return values;
    }

    /**
     *
     * @param position - selected item position
     * @return value of currently selected entry
     */
    public CharSequence getValue(int position) {
        return values[position];
    }
}
