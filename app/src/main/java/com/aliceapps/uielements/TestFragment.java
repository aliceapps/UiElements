package com.aliceapps.uielements;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aliceapps.uielements.databinding.FragmentTestBinding;
import com.aliceapps.uielements.datetimepickers.DatePickerUtils;
import com.aliceapps.uielements.datetimepickers.TimePickerUtils;

public class TestFragment extends Fragment {
    private FragmentTestBinding binding;
    private TestObject object;

    public TestFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTestBinding.inflate(inflater, container, false);
        object = new TestObject();
        binding.setSimpleSelectedValue(object);
        //binding = DataBindingUtil.inflate(inflater, R.layout.fragment_test, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        loadDateField();
        loadTimeField();
        object.setTestString("female");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void loadDateField() {
        final DatePickerUtils utils = new DatePickerUtils(binding.dateField, this);
        final DatePickerDialog.OnDateSetListener listener = utils.loadDatePickerListener();
        binding.dateField.setOnClickListener(view -> {
            try {
                utils.showDatePicker(listener);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    private void loadTimeField() {
        final TimePickerUtils utils = new TimePickerUtils(binding.timeField, this);
        final TimePickerDialog.OnTimeSetListener listener = utils.loadTimePickerListener();
        binding.timeField.setOnClickListener(view -> {
            try {
                utils.showTimePicker(listener, false);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    public FragmentTestBinding getBinding() {
        return  binding;
    }
}