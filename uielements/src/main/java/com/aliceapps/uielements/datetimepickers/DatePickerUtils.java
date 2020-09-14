package com.aliceapps.uielements.datetimepickers;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.aliceapps.rxjavautils.BaseSchedulerProvider;
import com.aliceapps.rxjavautils.MainSchedulerProvider;
import com.aliceapps.uielements.R;
import com.aliceapps.uielements.utility.di.DaggerWrapper;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableSingleObserver;

public class DatePickerUtils {
    private String TAG = "AliceApps.DatePickerUtils";
    private TextInputEditText dateView;
    private int datePickerStyle;
    private Context context;
    @Inject
    BaseSchedulerProvider schedulerProvider;

    public DatePickerUtils(TextInputEditText dateView, Context context) {
        DaggerWrapper.getComponent().inject(this);
        this.dateView = dateView;
        this.datePickerStyle = 0;
        this.context = context;
    }

    public DatePickerUtils(TextInputEditText dateView, int datePickerStyle, Context context) {
        DaggerWrapper.getComponent().inject(this);
        this.dateView = dateView;
        this.datePickerStyle = datePickerStyle;
        this.context = context;
    }

    public void showDatePicker(final DatePickerDialog.OnDateSetListener datePickerListener) {

        Single.fromCallable(() -> {
            final Calendar calendar = Calendar.getInstance();
            final DateFormat sdf = DateFormat.getDateInstance(DateFormat.SHORT);
            if (dateView.getText() != null && !dateView.getText().toString().equals("")) {
                Log.d(TAG, "showDatePicker: value " + dateView.getText().toString());
                try {
                    calendar.setTime(Objects.requireNonNull(sdf.parse(dateView.getText().toString())));
                } catch (ParseException e) {
                    calendar.setTime(Calendar.getInstance().getTime());
                    Log.d(TAG, "showDatePicker: error ");
                }
            }
            return calendar;
        })
                .subscribeOn(schedulerProvider.computation())
                .observeOn(schedulerProvider.ui())
                .subscribe(new DisposableSingleObserver<Calendar>() {
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
            Calendar calendar = Calendar.getInstance();
            DateFormat sdf = DateFormat.getDateInstance(DateFormat.SHORT);
            //Set date selected in dialog
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            return sdf.format(calendar.getTime());
        })
                .subscribeOn(schedulerProvider.computation())
                .observeOn(schedulerProvider.ui())
                .subscribe(s -> dateView.setText(s));
    }
}
