package com.aliceapps.uielements.spinners;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.aliceapps.uielements.R;

/**
 * Provides custom ArrayAdapter for Spinner
 */
public class SimpleSpinnerAdapter extends ArrayAdapter<String> {
    private final int viewId;
    private final CharSequence[] entries;
    private final CharSequence[] values;

    /**
     * Constructor for SimpleSpinnerAdapter
     * @param context - current context
     * @param entries - entries that Spinner shows
     * @param values - values that will be stored in the database
     */
    public SimpleSpinnerAdapter(@NonNull Context context, int entries, int values) {
        super(context, R.layout.simple_spinner, entries);
        viewId = R.layout.simple_spinner;
        this.entries = context.getResources().getTextArray(entries);
        this.values = context.getResources().getTextArray(values);
    }

    /**
     * Constructor for SimpleSpinnerAdapter
     * @param context - current context
     * @param entries - entries that Spinner shows
     * @param values - values that will be stored in the database
     * @param textViewResourceId - layout ID for spinner adapter
     */
    public SimpleSpinnerAdapter(@NonNull Context context, int entries, int values, int textViewResourceId) {
        super(context, textViewResourceId, entries);
        viewId = textViewResourceId;
        this.entries = context.getResources().getTextArray(entries);
        this.values = context.getResources().getTextArray(values);
    }

    /**
     * Constructor for SimpleSpinnerAdapter
     * @param context - current context
     * @param entries - entries that Spinner shows
     * @param values - values that will be stored in the database
     * @param textViewResourceId - layout ID for spinner adapter
     */
    public SimpleSpinnerAdapter(@NonNull Context context, @NonNull CharSequence[] entries, @NonNull CharSequence[] values, int textViewResourceId) {
        super(context, textViewResourceId);
        viewId = textViewResourceId;
        this.entries = entries;
        this.values = values;
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

