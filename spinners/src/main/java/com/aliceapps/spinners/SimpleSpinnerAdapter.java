package com.aliceapps.spinners;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class SimpleSpinnerAdapter extends ArrayAdapter<String> {
    LayoutInflater inflater;
    int viewId;
    String[] entries;

    public SimpleSpinnerAdapter(@NonNull Context context, int resource, int textViewResourceId) {
        super(context, textViewResourceId, resource);
        viewId = textViewResourceId;
        inflater = LayoutInflater.from(context);
        entries = context.getResources().getStringArray(resource);
    }

    public SimpleSpinnerAdapter(@NonNull Context context, @NonNull ArrayList<String> resource, int textViewResourceId) {
        super(context, textViewResourceId);
        viewId = textViewResourceId;
        inflater = LayoutInflater.from(context);
        entries = resource.toArray(new String[0]);
    }

    @Override
    public int getCount() {
        return entries.length;
    }

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
}

