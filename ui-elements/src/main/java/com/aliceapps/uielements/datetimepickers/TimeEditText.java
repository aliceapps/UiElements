package com.aliceapps.uielements.datetimepickers;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.aliceapps.uielements.R;
import com.aliceapps.uielements.utility.ViewHelpers;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Objects;

public class TimeEditText extends androidx.appcompat.widget.AppCompatEditText implements View.OnClickListener {
    private int style;
    private boolean format24Hours;
    private final DateFormat sdf = DateFormat.getTimeInstance(DateFormat.SHORT);

    public TimeEditText(Context context) {
        this(context, null);
    }

    public TimeEditText(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.editTextStyle);
    }

    public TimeEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (attrs != null) {
            TypedArray spinnerAttrs = getContext().obtainStyledAttributes(attrs, R.styleable.TimeEditText, defStyleAttr, 0);
            style = spinnerAttrs.getResourceId(R.styleable.TimeEditText_android_datePickerDialogTheme, 0);
            format24Hours = spinnerAttrs.getBoolean(R.styleable.TimeEditText_use24HourFormat, true);
            spinnerAttrs.recycle();
        }
        setFocusable(false);
        setLongClickable(false);
        setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        EditText dateView = (EditText) view;
        DateFormat sdf = DateFormat.getDateInstance(DateFormat.SHORT);
        Activity foundActivity = ViewHelpers.getActivity(view);
        if (foundActivity != null) {
            TimePickerDialog.OnTimeSetListener listener;

        if (foundActivity instanceof AppCompatActivity) {
            TimePickerUtils utils = new TimePickerUtils(dateView, style, (AppCompatActivity) foundActivity);
            listener = utils.loadTimePickerListener();
        } else
            listener = loadTimePickerListener(dateView);

            final Calendar calendar = Calendar.getInstance();
            if (dateView.getText() != null && !dateView.getText().toString().equals("")) {
                try {
                    calendar.setTime(Objects.requireNonNull(sdf.parse(dateView.getText().toString())));
                } catch (ParseException e) {
                    calendar.setTime(Calendar.getInstance().getTime());
                }
            }
            TimePickerDialog datePickerDialog;
            if (style != 0)
                datePickerDialog = new TimePickerDialog(foundActivity, style, listener, calendar
                        .get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), format24Hours);
            else
                datePickerDialog = new TimePickerDialog(foundActivity, listener, calendar
                        .get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), format24Hours);
            datePickerDialog.show();
        }
    }

    private TimePickerDialog.OnTimeSetListener loadTimePickerListener(EditText dateView) {
        return (timePicker, hour, minute) -> {
                Calendar c = getAbsoluteTime(hour, minute);
                String s = sdf.format(c.getTime());
            dateView.setText(s);
        };
    }

    @NonNull
    private static Calendar getAbsoluteTime(int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.YEAR, 2000);
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }
}
