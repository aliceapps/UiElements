package com.aliceapps.uielements.datetimepickers;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.Toast;

import com.aliceapps.rxjavautils.BaseSchedulerProvider;
import com.aliceapps.uielements.R;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.observers.DisposableSingleObserver;

public class DatePickerUtils {
    @Inject
    protected BaseSchedulerProvider schedulerProvider;
    private TextInputEditText dateView;
    private int datePickerStyle = 0;
    private Context context;

    @Inject
    public DatePickerUtils(TextInputEditText dateView, int datePickerStyle, Context context) {
        this.dateView = dateView;
        this.datePickerStyle = datePickerStyle;
        this.context = context;
    }

    public void showDatePicker(final DatePickerDialog.OnDateSetListener datePickerListener) {

        Single.fromCallable(() -> {
            final Calendar calendar = Calendar.getInstance();
            final DateFormat sdf = DateFormat.getDateInstance();
            if (dateView.getText() != null && !dateView.getText().toString().equals("")) {
                try {
                    calendar.setTime(Objects.requireNonNull(sdf.parse(dateView.getText().toString())));
                } catch (ParseException e) {
                    calendar.setTime(Calendar.getInstance().getTime());
                }
            }
            return calendar;
        }).subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui()).subscribe(new DisposableSingleObserver<Calendar>() {
            @Override
            public void onSuccess(Calendar calendar) {
                DatePickerDialog datePickerDialog;
                if (datePickerStyle != 0)
                    datePickerDialog = new DatePickerDialog(context, datePickerStyle, datePickerListener, calendar
                            .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH));
                else
                    datePickerDialog = new DatePickerDialog(context, datePickerListener, calendar
                            .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(context, context.getResources().getString(R.string.failed_show_date_picker), Toast.LENGTH_LONG).show();
            }
        });
    }

    public DatePickerDialog.OnDateSetListener loadDatePickerListener() {

        return (view, year, monthOfYear, dayOfMonth) -> Single.fromCallable(() -> {
            final Calendar calendar = Calendar.getInstance();
            //Set date selected in dialog
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            return calendar;
        }).subscribeOn(schedulerProvider.computation()).observeOn(schedulerProvider.ui()).subscribe(new DisposableSingleObserver<Calendar>() {
            @Override
            public void onSuccess(Calendar calendar) {
                final DateFormat sdf = DateFormat.getDateInstance();
                dateView.setText(sdf.format(calendar.getTime()));
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(context, context.getResources().getString(R.string.failed_show_date_picker), Toast.LENGTH_LONG).show();
            }
        });
    }
}
