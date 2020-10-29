package com.aliceapps.uielements;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Maybe;

public class TestObject extends BaseObservable {
    private String testString;
    private String testStringImage;
    private Date dateField;
    private String timeField;
    public Maybe<List<String>> result;

    public TestObject() {
        List<String> values = new ArrayList<>();
        values.add("One Auto");
        values.add("Two Auto");
        result = Maybe.just(values);
    }

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

    @Bindable
    public Date getDateField() {
        return dateField;
    }

    public void setDateField(Date date) {
        dateField = date;
        notifyPropertyChanged(BR.dateField);
    }

    @Bindable
    public String getTimeField() {
        return timeField;
    }

    public void setTimeField(String string) {
        timeField = string;
        notifyPropertyChanged(BR.timeField);
    }
}
