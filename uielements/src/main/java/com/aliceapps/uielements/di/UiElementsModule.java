package com.aliceapps.uielements.di;

import com.aliceapps.rxjavautils.BaseSchedulerProvider;
import com.aliceapps.rxjavautils.MainSchedulerProvider;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;

@Module
@InstallIn(ActivityComponent.class)
public class UiElementsModule {

    @Provides
    public static MainSchedulerProvider provideScheduler() {
        return new MainSchedulerProvider();
    }

    @Provides
    public static BaseSchedulerProvider provideBaseScheduler() {
        return new MainSchedulerProvider();
    }
}
