package com.aliceapps.uielements.utility.di;

import com.aliceapps.uielements.datetimepickers.DatePickerUtils;
import com.aliceapps.uielements.datetimepickers.TimePickerUtils;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
        modules = {
                ModuleUtil.class
        })
public interface MainComponent {
    void inject(TimePickerUtils timePickerUtils);
    void inject(DatePickerUtils datePickerUtils);
}
