package com.aliceapps.uielements.datetimepickers;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.aliceapps.uielements.R;
import com.aliceapps.uielements.utility.ViewHelpers;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Objects;

public class DateEditText extends androidx.appcompat.widget.AppCompatEditText implements View.OnClickListener {
    private int style;
    private DateFormat sdf = DateFormat.getDateInstance(DateFormat.SHORT);

    public DateEditText(Context context) {
        this(context, null);
    }

    public DateEditText(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.editTextStyle);
    }

    public DateEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (attrs != null) {
            TypedArray spinnerAttrs = getContext().obtainStyledAttributes(attrs, R.styleable.DateEditText, defStyleAttr, 0);
            style = spinnerAttrs.getResourceId(R.styleable.DateEditText_android_datePickerDialogTheme, 0);
            spinnerAttrs.recycle();
        }
        setFocusable(false);
        setLongClickable(false);
        setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        EditText dateView = (EditText) view;
        Activity foundActivity = ViewHelpers.getActivity(view);
        if (foundActivity != null) {
            DatePickerDialog.OnDateSetListener listener;

            if (foundActivity instanceof AppCompatActivity) {
                DatePickerUtils utils = new DatePickerUtils(dateView, style, (AppCompatActivity) foundActivity);
                listener = utils.loadDatePickerListener();
            } else
                listener = loadDatePickerListener(dateView);

            final Calendar calendar = Calendar.getInstance();
            if (dateView.getText() != null && !dateView.getText().toString().equals("")) {
                try {
                    calendar.setTime(Objects.requireNonNull(sdf.parse(dateView.getText().toString())));
                } catch (ParseException e) {
                    calendar.setTime(Calendar.getInstance().getTime());
                }
            }
            DatePickerDialog datePickerDialog;
            if (style != 0)
                datePickerDialog = new DatePickerDialog(foundActivity, style, listener, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
            else
                datePickerDialog = new DatePickerDialog(foundActivity, listener, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();

        }

    }

    public DatePickerDialog.OnDateSetListener loadDatePickerListener(EditText view) {
        return (datePicker, year, monthOfYear, dayOfMonth) -> {

            Calendar calendar = Calendar.getInstance();
            //Set date selected in dialog
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String s = sdf.format(calendar.getTime());
            view.setText(s);
        };
    }
}
