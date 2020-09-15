package com.aliceapps.uielements.datetimepickers;

import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.aliceapps.rxjavautils.AutoDisposable;
import com.aliceapps.rxjavautils.BaseSchedulerProvider;
import com.aliceapps.uielements.R;
import com.aliceapps.uielements.utility.di.DaggerWrapper;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;

public class TimePickerUtils {
    @Inject
    BaseSchedulerProvider schedulerProvider;
    private TextInputEditText dateView;
    private int timePickerStyle;
    private Context context;
    private AutoDisposable autoDisposable;
    final DateFormat sdf = DateFormat.getTimeInstance(DateFormat.SHORT);

    public TimePickerUtils(TextInputEditText dateView, @NonNull AppCompatActivity activity) {
        DaggerWrapper.getComponent().inject(this);
        this.dateView = dateView;
        this.timePickerStyle = 0;
        this.context = activity.getApplicationContext();
        autoDisposable = new AutoDisposable(activity.getLifecycle());
    }

    public TimePickerUtils(TextInputEditText dateView, @NonNull Fragment fragment) {
        DaggerWrapper.getComponent().inject(this);
        this.dateView = dateView;
        this.timePickerStyle = 0;
        this.context = fragment.getContext();
        autoDisposable = new AutoDisposable(fragment.getLifecycle());
    }

    public TimePickerUtils(TextInputEditText dateView, int timePickerStyle, @NonNull AppCompatActivity activity) {
        DaggerWrapper.getComponent().inject(this);
        this.dateView = dateView;
        this.timePickerStyle = timePickerStyle;
        this.context = activity.getApplicationContext();
        autoDisposable = new AutoDisposable(activity.getLifecycle());
    }

    public TimePickerUtils(TextInputEditText dateView, int timePickerStyle, @NonNull Fragment fragment) {
        DaggerWrapper.getComponent().inject(this);
        this.dateView = dateView;
        this.timePickerStyle = timePickerStyle;
        this.context = fragment.getContext();
        autoDisposable = new AutoDisposable(fragment.getLifecycle());
    }


    public void showTimePicker(TimePickerDialog.OnTimeSetListener timeSetListener, boolean show24Hours) throws Throwable {
        Disposable d = Single.fromCallable(() -> {
            final Calendar calendar = Calendar.getInstance();
            if (dateView.getText() != null && !dateView.getText().toString().equals("")) {
                try {
                    calendar.setTime(Objects.requireNonNull(sdf.parse(dateView.getText().toString())));
                } catch (ParseException e) {
                    calendar.setTime(Calendar.getInstance().getTime());
                }
            }
            return calendar;
        })
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeWith(new DisposableSingleObserver<Calendar>() {
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
        autoDisposable.add(d);
    }

    public TimePickerDialog.OnTimeSetListener loadTimePickerListener() {

        return (timePicker, hour, minute) -> {
            Disposable d = Single.fromCallable(() -> {
                Calendar c = getAbsoluteTime(hour, minute);
                return sdf.format(c.getTime());
            })
                    .subscribeOn(schedulerProvider.computation())
                    .observeOn(schedulerProvider.ui())
                    .subscribe(s -> dateView.setText(s));
            try {
                autoDisposable.add(d);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        };
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
