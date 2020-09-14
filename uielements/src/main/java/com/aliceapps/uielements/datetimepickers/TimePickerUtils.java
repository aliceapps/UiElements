package com.aliceapps.uielements.datetimepickers;

import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.aliceapps.rxjavautils.AutoDisposable;
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
import io.reactivex.observers.DisposableSingleObserver;

public class TimePickerUtils {
    @Inject
    BaseSchedulerProvider schedulerProvider;
    private TextInputEditText dateView;
    private int timePickerStyle = 0;
    private Context context;

    public TimePickerUtils(TextInputEditText dateView, int timePickerStyle, Context context, AutoDisposable autoDisposable) {
        DaggerWrapper.getComponent().inject(this);
        this.dateView = dateView;
        this.timePickerStyle = timePickerStyle;
        this.context = context;
        //schedulerProvider = new MainSchedulerProvider();
    }

    public TimePickerUtils(TextInputEditText dateView, int timePickerStyle, Context context, BaseSchedulerProvider schedulerProvider, AutoDisposable autoDisposable) {
        DaggerWrapper.getComponent().inject(this);
        this.dateView = dateView;
        this.timePickerStyle = timePickerStyle;
        this.context = context;
        //this.schedulerProvider = schedulerProvider;
    }

    public void showTimePicker(TimePickerDialog.OnTimeSetListener timeSetListener, boolean show24Hours) {
        Single.fromCallable(() -> {
            final Calendar calendar = Calendar.getInstance();
            final DateFormat sdf = DateFormat.getDateInstance(DateFormat.SHORT);
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
                TimePickerDialog datePickerDialog;
                if (timePickerStyle != 0)
                    datePickerDialog = new TimePickerDialog(context, timePickerStyle, timeSetListener, calendar
                            .get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), show24Hours);
                else
                    datePickerDialog = new TimePickerDialog(context, timeSetListener, calendar
                            .get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), show24Hours);
                datePickerDialog.show();
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(context, context.getResources().getString(R.string.failed_show_date_picker), Toast.LENGTH_LONG).show();
            }
        });
    }

    public TimePickerDialog.OnTimeSetListener loadTimePickerListener() {

        return (timePicker, hour, minute) -> Single.fromCallable(() -> {
            DateFormat sdf = DateFormat.getDateInstance();
            Calendar c = getAbsoluteTime(hour, minute);
            return sdf.format(c.getTime());
        })
                .subscribeOn(schedulerProvider.computation())
                .observeOn(schedulerProvider.ui())
                .subscribe(s -> dateView.setText(s));
    }

    @NonNull
    public static Calendar getAbsoluteTime(int hourOfDay, int minute) {
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
