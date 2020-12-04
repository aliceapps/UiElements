package com.aliceapps.uielements.datetimepickers;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.aliceapps.uielements.R;
import com.aliceapps.uielements.utility.ViewHelpers;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class TimeEditText extends androidx.appcompat.widget.AppCompatEditText implements View.OnClickListener {
    /**
     * TAG for logging
     */
    private static final String TAG = TimeEditText.class.getSimpleName();
    /**
     * Theme of date picker dialog
     */
    private int style;
    /**
     * True if 24 hours format should be applied
     */
    private boolean format24Hours;
    /**
     * Time format
     */
    private final DateTimeFormatter sdf = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);

    /**
     * Constructor
     * @param context - current context
     */
    public TimeEditText(Context context) {
        this(context, null);
    }

    /**
     * Constructor
     * @param context - current context
     * @param attrs - view attributes
     */
    public TimeEditText(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.editTextStyle);
    }

    /**
     * Constructor
     * @param context - current context
     * @param attrs - view attributes
     * @param defStyleAttr - default style
     */
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

    /**
     * Shows time picker when view is clicked
     * @param view - current view
     */
    @Override
    public void onClick(View view) {
        EditText dateView = (EditText) view;
        Activity foundActivity = ViewHelpers.getActivity(view);
        if (foundActivity != null) {
            TimePickerDialog.OnTimeSetListener listener;

        if (foundActivity instanceof AppCompatActivity) {
            TimePickerUtils utils = new TimePickerUtils(dateView, style, (AppCompatActivity) foundActivity);
            listener = utils.loadTimePickerListener();
        } else
            listener = loadTimePickerListener(dateView);

            LocalTime currentTime = LocalTime.now();
            if (dateView.getText() != null && !dateView.getText().toString().equals("")) {
                currentTime = LocalTime.parse(dateView.getText().toString(),sdf);
            }
            TimePickerDialog datePickerDialog;
            if (style != 0)
                datePickerDialog = new TimePickerDialog(foundActivity, style, listener,
                        currentTime.getHour(), currentTime.getMinute(), format24Hours);
            else
                datePickerDialog = new TimePickerDialog(foundActivity, listener,
                        currentTime.getHour(), currentTime.getMinute(), format24Hours);
            datePickerDialog.show();
        }
    }

    private TimePickerDialog.OnTimeSetListener loadTimePickerListener(EditText dateView) {
        return (timePicker, hour, minute) -> {
            LocalTime time = LocalTime.of(hour, minute);
            String s = sdf.format(time);
            dateView.setText(s);
        };
    }
}
