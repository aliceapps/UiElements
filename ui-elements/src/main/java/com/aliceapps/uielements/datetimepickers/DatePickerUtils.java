package com.aliceapps.uielements.datetimepickers;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.aliceapps.rxjavautils.AutoDisposable;
import com.aliceapps.rxjavautils.BaseSchedulerProvider;
import com.aliceapps.uielements.utility.di.DaggerWrapper;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Class provides utility functions for DatePicker dialog
 */
public class DatePickerUtils {
    /**
     * TAG for logging
     */
    private final String TAG = DatePickerUtils.class.getSimpleName();
    /**
     * EditText field that will contain the date value
     */
    private final EditText dateView;
    /**
     * Theme of date picker dialog
     */
    private final int datePickerStyle;
    /**
     * Current context
     */
    private final Context context;
    /**
     * Date format
     */
    private final DateFormat sdf = DateFormat.getDateInstance(DateFormat.SHORT);
    /**
     * AutoDisposable is used to track lifecycle of an object for RxJava
     */
    private final AutoDisposable autoDisposable;
    /**
     * BaseSchedulerProvider is used to schedule background job for RxJava
     */
    @Inject
    BaseSchedulerProvider schedulerProvider;

    /**
     * Constructor for DatePickerUtils class
     * @param dateView - view to which DatePicker listener will be attached
     * @param activity - current activity
     */
    public DatePickerUtils(@NonNull EditText dateView, @NonNull AppCompatActivity activity) {
        this(dateView, 0, activity);
    }

    /**
     * Constructor for DatePickerUtils class
     * @param dateView - view to which DatePicker listener will be attached
     * @param fragment - current fragment
     */
    public DatePickerUtils(@NonNull EditText dateView, @NonNull Fragment fragment) {
        this(dateView, 0, fragment);
    }

    /**
     * Constructor for DatePickerUtils class
     * @param dateView - view to which DatePicker listener will be attached
     * @param datePickerStyle - DatePicker theme that will be used in DatePickerDialog
     * @param activity - current activity
     */
    public DatePickerUtils(EditText dateView, int datePickerStyle, @NonNull AppCompatActivity activity) {
        DaggerWrapper.getComponent().inject(this);
        this.dateView = dateView;
        this.datePickerStyle = datePickerStyle;
        this.context = activity.getApplicationContext();
        autoDisposable = new AutoDisposable(activity.getLifecycle());
    }

    /**
     * Constructor for DatePickerUtils class
     * @param dateView - view to which DatePicker listener will be attached
     * @param datePickerStyle - DatePicker theme that will be used in DatePickerDialog
     * @param fragment - current fragment
     */
    public DatePickerUtils(EditText dateView, int datePickerStyle, @NonNull Fragment fragment) {
        DaggerWrapper.getComponent().inject(this);
        this.dateView = dateView;
        this.datePickerStyle = datePickerStyle;
        this.context = fragment.getContext();
        autoDisposable = new AutoDisposable(fragment.getLifecycle());
    }

    /**
     * Initializes and shows DatePickerDialog
     * @param datePickerListener - DatePickerDialog.OnDateSetListener listener that will be used
     * @throws Throwable - throws error if dialog can't be shown
     */
    public void showDatePicker(final DatePickerDialog.OnDateSetListener datePickerListener) throws Throwable {

        Disposable d = Single.fromCallable(() -> {
            final Calendar calendar = Calendar.getInstance();
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
                .subscribe(calendar -> {
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
                });
        autoDisposable.add(d);
    }

    /**
     * Initializes DatePickerDialog.OnDateSetListener listener
     * @return DatePickerDialog.OnDateSetListener
     */
    public DatePickerDialog.OnDateSetListener loadDatePickerListener() {
        return (datePicker, year, monthOfYear, dayOfMonth) -> {
            Disposable d = Single.fromCallable(() -> {
                Calendar calendar = Calendar.getInstance();
                //Set date selected in dialog
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                return sdf.format(calendar.getTime());
            }) .subscribeOn(schedulerProvider.computation())
                    .observeOn(schedulerProvider.ui())
                    .subscribe((Consumer<String>) dateView::setText);
            try {
                autoDisposable.add(d);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        };
    }
}
