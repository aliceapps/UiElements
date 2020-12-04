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

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

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
    private final DateTimeFormatter sdf = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);

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
            LocalTime currentTime = LocalTime.now();
            if (dateView.getText() != null && !dateView.getText().toString().equals("")) {
                if (dateView.getText() != null && !dateView.getText().toString().equals("")) {
                    currentTime = LocalTime.parse(dateView.getText().toString(),sdf);
                }
            }
            return currentTime;
        })
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeWith(new DisposableSingleObserver<LocalTime>() {
            @Override
            public void onSuccess(@NonNull LocalTime calendar) {
                TimePickerDialog datePickerDialog;
                if (timePickerStyle != 0)
                    datePickerDialog = new TimePickerDialog(context, timePickerStyle, timeSetListener,
                            calendar.getHour(), calendar.getMinute(), show24Hours);
                else
                    datePickerDialog = new TimePickerDialog(context, timeSetListener,
                            calendar.getHour(), calendar.getMinute(), show24Hours);
                datePickerDialog.show();
            }

            @Override
            public void onError(@NonNull Throwable e) {
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
                LocalTime time = LocalTime.of(hour, minute);
                return sdf.format(time);
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
}
