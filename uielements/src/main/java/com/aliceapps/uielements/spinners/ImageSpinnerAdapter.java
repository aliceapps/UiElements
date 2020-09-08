package com.aliceapps.uielements.spinners;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.aliceapps.uielements.R;

public class ImageSpinnerAdapter extends ArrayAdapter<String> {

    LayoutInflater inflater;
    int viewId;
    String[] entries;
    int[] icons;

    public ImageSpinnerAdapter(@NonNull Context context, int resource, int textViewResourceId, int[] images) {
        super(context, resource, textViewResourceId);
        viewId = textViewResourceId;
        inflater = LayoutInflater.from(context);
        entries = context.getResources().getStringArray(resource);
        icons = images;
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
        ImageView icon = spinnerView.findViewById(R.id.spinner_icon);
        icon.setImageDrawable(ContextCompat.getDrawable(getContext(), icons[position]));
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
        ImageView icon = rowView.findViewById(R.id.spinner_icon);
        icon.setImageDrawable(ContextCompat.getDrawable(getContext(), icons[position]));
        return rowView;
    }


}
