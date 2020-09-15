package com.aliceapps.uielements.datetimepickers;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Log;
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

public class DatePickerUtils {
    private String TAG = "AliceApps.DatePickerUtils";
    private TextInputEditText dateView;
    private int datePickerStyle;
    private Context context;
    final DateFormat sdf = DateFormat.getDateInstance(DateFormat.SHORT);
    private AutoDisposable autoDisposable;
    @Inject
    BaseSchedulerProvider schedulerProvider;

    public DatePickerUtils(@NonNull TextInputEditText dateView, @NonNull AppCompatActivity activity) {
        DaggerWrapper.getComponent().inject(this);
        this.dateView = dateView;
        this.datePickerStyle = 0;
        this.context = activity.getApplicationContext();
        autoDisposable = new AutoDisposable(activity.getLifecycle());

    }

    public DatePickerUtils(@NonNull TextInputEditText dateView, @NonNull Fragment fragment) {
        DaggerWrapper.getComponent().inject(this);
        this.dateView = dateView;
        this.datePickerStyle = 0;
        this.context = fragment.getContext();
        autoDisposable = new AutoDisposable(fragment.getLifecycle());
    }

    public DatePickerUtils(TextInputEditText dateView, int datePickerStyle, @NonNull AppCompatActivity activity) {
        DaggerWrapper.getComponent().inject(this);
        this.dateView = dateView;
        this.datePickerStyle = datePickerStyle;
        this.context = activity.getApplicationContext();
        autoDisposable = new AutoDisposable(activity.getLifecycle());
    }

    public DatePickerUtils(TextInputEditText dateView, int datePickerStyle, @NonNull Fragment fragment) {
        DaggerWrapper.getComponent().inject(this);
        this.dateView = dateView;
        this.datePickerStyle = datePickerStyle;
        this.context = fragment.getContext();
        autoDisposable = new AutoDisposable(fragment.getLifecycle());
    }

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
                .subscribeWith(new DisposableSingleObserver<Calendar>() {
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
        autoDisposable.add(d);
    }

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
                    .subscribe(s -> dateView.setText(s));
            try {
                autoDisposable.add(d);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        };
    }
}
