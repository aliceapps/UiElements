package com.aliceapps.uielements.datetimepickers;

import android.app.DatePickerDialog;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class DatePickerUtilsTest {

    @Mock
    EditText view;

    @Mock
    AppCompatActivity activity;

    @Mock
    Lifecycle lifecycle;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(activity.getLifecycle()).thenReturn(lifecycle);
    }

    @Test
    public void createDatePicker() {
        DatePickerUtils picker = new DatePickerUtils(view, activity);
        Assert.assertNotNull(picker.loadDatePickerListener());
    }

}
