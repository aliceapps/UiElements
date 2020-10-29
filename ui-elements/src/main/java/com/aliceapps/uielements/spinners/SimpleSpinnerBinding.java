package com.aliceapps.uielements.spinners;

import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

import java.util.Arrays;
import java.util.List;

/**
 * Binding adapter class for SimpleSpinnerView
 */
public class SimpleSpinnerBinding {

    /**
     * Binding adapter for spinner_selected_value attribute
     * @param view - SimpleSpinnerView
     * @param value - field where we want to store value
     */
    @BindingAdapter("spinner_selected_value")
    public static void setSelectedValue(@NonNull SimpleSpinnerView view, String value) {
        List<CharSequence> values = Arrays.asList(view.getValues());
        if (values.contains(value)) {
            int position = values.indexOf(value);
            if (view.getSelectedItemPosition() != position)
                view.setSelection(position);

        }
    }

    /**
     * Inverse adaptor for spinner_selected_value attribute
     * @param view - SimpleSpinnerView
     * @return current value
     */
    @InverseBindingAdapter(attribute = "spinner_selected_value", event = "spinnerSelectedValueAttrChanged")
    public static String getSelectedValue(@NonNull SimpleSpinnerView view) {
        int position = view.getSelectedItemPosition();
        return (String) view.getValues()[position];
    }

    /**
     * Binding adapter for different actions. Used to properly store value
     * @param view - SimpleSpinnerView
     * @param selected - OnItemSelected
     * @param nothingSelected - OnNothingSelected
     * @param attrChanged - InverseBindingListener
     */
    @BindingAdapter(value = {"android:onItemSelected", "android:onNothingSelected",
            "spinnerSelectedValueAttrChanged" }, requireAll = false)
    public static void setOnItemSelectedListener(SimpleSpinnerView view, final OnItemSelected selected,
                                                 final OnNothingSelected nothingSelected, final InverseBindingListener attrChanged) {
        if (selected == null && nothingSelected == null && attrChanged == null) {
            view.setOnItemSelectedListener(null);
        } else {
            view.setOnItemSelectedListener(
                    new OnItemSelectedComponentListener(selected, nothingSelected, attrChanged));
        }
    }

    /**
     * Listeners for SimpleSpinnerView
     */
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
