package com.aliceapps.uielements.datetimepickers;

import android.app.TimePickerDialog;
import android.content.Context;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.aliceapps.rxjavautils.AutoDisposable;
import com.aliceapps.rxjavautils.BaseSchedulerProvider;
import com.aliceapps.uielements.R;
import com.aliceapps.uielements.utility.di.DaggerWrapper;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableSingleObserver;

/**
 * Class provides utility functions for TimePicker dialog
 */
public class TimePickerUtils {
    /**
     * TAG for logging
     */
    private final String TAG = TimePickerUtils.class.getSimpleName();
    /**
     * BaseSchedulerProvider is used to schedule background job for RxJava
     */
    @Inject
    BaseSchedulerProvider schedulerProvider;
    /**
     * EditText field that will contain the date value
     */
    private final EditText dateView;
    /**
     * Theme of date picker dialog
     */
    private final int timePickerStyle;
    /**
     * Current context
     */
    private final Context context;
    /**
     * AutoDisposable is used to track lifecycle of an object for RxJava
     */
    private final AutoDisposable autoDisposable;
    /**
     * Time format
     */
    private final DateFormat sdf = DateFormat.getTimeInstance(DateFormat.SHORT);

    /**
     * Constructor for TimePickerUtils class
     * @param dateView - view to which TimePicker listener will be attached
     * @param activity - current activity
     */
    public TimePickerUtils(EditText dateView, @NonNull AppCompatActivity activity) {
        this(dateView, 0 ,activity);
    }

    /**
     * Constructor for TimePickerUtils class
     * @param dateView - view to which TimePicker listener will be attached
     * @param fragment - current fragment
     */
    public TimePickerUtils(EditText dateView, @NonNull Fragment fragment) {
        this(dateView, 0, fragment);
    }

    /**
     * Constructor for TimePickerUtils class
     * @param dateView - view to which TimePicker listener will be attached
     * @param timePickerStyle - TimePicker theme that will be used in TimePickerDialog
     * @param activity - current activity
     */
    public TimePickerUtils(EditText dateView, int timePickerStyle, @NonNull AppCompatActivity activity) {
        DaggerWrapper.getComponent().inject(this);
        this.dateView = dateView;
        this.timePickerStyle = timePickerStyle;
        this.context = activity.getApplicationContext();
        autoDisposable = new AutoDisposable(activity.getLifecycle());
    }

    /**
     * Constructor for TimePickerUtils class
     * @param dateView - view to which TimePicker listener will be attached
     * @param timePickerStyle - TimePicker theme that will be used in TimePickerDialog
     * @param fragment - current fragment
     */
    public TimePickerUtils(EditText dateView, int timePickerStyle, @NonNull Fragment fragment) {
        DaggerWrapper.getComponent().inject(this);
        this.dateView = dateView;
        this.timePickerStyle = timePickerStyle;
        this.context = fragment.getContext();
        autoDisposable = new AutoDisposable(fragment.getLifecycle());
    }

    /**
     * Initializes and shows TimePickerDialog
     * @param timeSetListener - TimePickerDialog.OnTimeSetListener listener that will be used
     * @param show24Hours - true if 24 hours format should be used
     * @throws Throwable - throws error if dialog can't be shown
     */
    public void showTimePicker(TimePickerDialog.OnTimeSetListener timeSetListener, boolean show24Hours) throws Throwable {
        Disposable d = Single.fromCallable(() -> {
            final Calendar calendar = Calendar.getInstance();
            if (dateView.getText() != null && !dateView.getText().toString().equals("")) {
                Date time;
                try {
                    time = sdf.parse(dateView.getText().toString());
                } catch (ParseException e) {
                    time = Calendar.getInstance().getTime();
                    Log.e(TAG, "showTimePicker: error", e);
                }
                if (time == null)
                    time = Calendar.getInstance().getTime();
                calendar.setTime(time);
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
                Log.e(TAG, "onError: ", e);
                Toast.makeText(context, context.getResources().getString(R.string.failed_show_date_picker), Toast.LENGTH_LONG).show();
            }
        });
        autoDisposable.add(d);
    }

    /**
     * Initializes TimePickerDialog.OnTimeSetListener listener
     * @return TimePickerDialog.OnTimeSetListener
     */
    public TimePickerDialog.OnTimeSetListener loadTimePickerListener() {

        return (timePicker, hour, minute) -> {
            Disposable d = Single.fromCallable(() -> {
                Calendar c = getAbsoluteTime(hour, minute);
                return sdf.format(c.getTime());
            })
                    .subscribeOn(schedulerProvider.computation())
                    .observeOn(schedulerProvider.ui())
                    .subscribe((Consumer<String>) dateView::setText);
            try {
                autoDisposable.add(d);
            } catch (Throwable throwable) {
                Log.e(TAG, "loadTimePickerListener: error", throwable);
                Toast.makeText(context, context.getResources().getString(R.string.failed_show_date_picker), Toast.LENGTH_LONG).show();
                throwable.printStackTrace();
            }
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
