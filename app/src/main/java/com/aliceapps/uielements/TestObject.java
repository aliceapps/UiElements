package com.aliceapps.uielements;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

public class TestObject extends BaseObservable {
    private String testString;
    private String testStringImage;

    @Bindable
    public String getTestString() {
        return testString;
    }

    public void setTestString(String string) {
        testString = string;
        notifyPropertyChanged(BR.testString);
    }

    @Bindable
    public String getTestStringImage() {
        return testStringImage;
    }

    public void setTestStringImage(String string) {
        testStringImage = string;
        notifyPropertyChanged(BR.testStringImage);
    }
}
