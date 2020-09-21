package com.aliceapps.uielements.spinners;

import android.view.View;
import android.widget.AdapterView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

import java.util.Arrays;
import java.util.List;

public class SimpleSpinnerBinding {

    @BindingAdapter("simple_spinner_selected_value")
    public static void setSelectedValue(SimpleSpinnerView view, String value) {
        List<CharSequence> values = Arrays.asList(view.getValues());
        if (values.contains(value)) {
            int position = values.indexOf(value);
            if (view.getSelectedItemPosition() != position)
                view.setSelection(position);

        }
    }

    @InverseBindingAdapter(attribute = "simple_spinner_selected_value", event = "simpleSpinnerSelectedValueAttrChanged")
    public static String getSelectedValue(SimpleSpinnerView view) {
        int position = view.getSelectedItemPosition();
        return (String) view.getValues()[position];
    }

    @BindingAdapter(value = {"android:onItemSelected", "android:onNothingSelected",
            "simpleSpinnerSelectedValueAttrChanged" }, requireAll = false)
    public static void setOnItemSelectedListener(SimpleSpinnerView view, final OnItemSelected selected,
                                                 final OnNothingSelected nothingSelected, final InverseBindingListener attrChanged) {
        if (selected == null && nothingSelected == null && attrChanged == null) {
            view.setOnItemSelectedListener(null);
        } else {
            view.setOnItemSelectedListener(
                    new OnItemSelectedComponentListener(selected, nothingSelected, attrChanged));
        }
    }
    public static class OnItemSelectedComponentListener implements AdapterView.OnItemSelectedListener {
        private final OnItemSelected mSelected;
        private final OnNothingSelected mNothingSelected;
        private final InverseBindingListener mAttrChanged;
        public OnItemSelectedComponentListener(OnItemSelected selected,
                                               OnNothingSelected nothingSelected, InverseBindingListener attrChanged) {
            this.mSelected = selected;
            this.mNothingSelected = nothingSelected;
            this.mAttrChanged = attrChanged;
        }
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (mSelected != null) {
                mSelected.onItemSelected(parent, view, position, id);
            }
            if (mAttrChanged != null) {
                mAttrChanged.onChange();
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            if (mNothingSelected != null) {
                mNothingSelected.onNothingSelected(parent);
            }
            if (mAttrChanged != null) {
                mAttrChanged.onChange();
            }
        }
    }
    public interface OnItemSelected {
        void onItemSelected(AdapterView<?> parent, View view, int position, long id);
    }
    public interface OnNothingSelected {
        void onNothingSelected(AdapterView<?> parent);
    }
}
