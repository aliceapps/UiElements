package com.aliceapps.uielements.utility;

import android.content.Context;
import android.content.res.Resources;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class BindingConvertersTest {

    @Mock
    Context context;

    @Mock
    Resources resources;

    @Test
    public void dateToString() {
        DateFormat sdf = DateFormat.getDateInstance(DateFormat.SHORT); //Date only
        Date time = Calendar.getInstance().getTime();
        Assert.assertEquals("Correct date",sdf.format(time), BindingConverters.dateToString(time));
        Assert.assertEquals("Empty date","", BindingConverters.dateToString(null));
    }

    @Test
    public void stringToDate() throws ParseException {
        DateFormat sdf = DateFormat.getDateInstance(DateFormat.SHORT);
        Date time = Calendar.getInstance().getTime();
        String res = sdf.format(time);

        Assert.assertEquals("Correct string",sdf.parse(res), BindingConverters.stringToDate(sdf.format(time)));
        Assert.assertNull("Empty string", BindingConverters.stringToDate(null));
        Assert.assertNull("Wrong string", BindingConverters.stringToDate("Some wrong string"));
    }

    @Test
    public void dateTimeToString() {
        DateFormat sdf = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT); //Date only
        Date time = Calendar.getInstance().getTime();
        Assert.assertEquals("Correct date",sdf.format(time), BindingConverters.dateTimeToString(time));
        Assert.assertEquals("Empty date","", BindingConverters.dateToString(null));
    }

    @Test
    public void stringToDateTime() throws ParseException {
        DateFormat sdf = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
        Date time = Calendar.getInstance().getTime();
        String res = sdf.format(time);

        Assert.assertEquals("Correct string",sdf.parse(res), BindingConverters.stringToDateTime(sdf.format(time)));
        Assert.assertNull("Empty string", BindingConverters.stringToDate(null));
        Assert.assertNull("Wrong string", BindingConverters.stringToDate("Some wrong string"));
    }

    @Test
    public void doubleToString() {
        Double d = new Random().nextDouble();
        Assert.assertEquals(d.toString(), BindingConverters.doubleToString(d));
        Assert.assertEquals("", BindingConverters.doubleToString(null));
        Assert.assertEquals("", BindingConverters.doubleToString(0.0));
    }

    @Test
    public void stringToDouble() {
        Double d = new Random().nextDouble();
        Assert.assertEquals(d, BindingConverters.stringToDouble(d.toString()));
        Assert.assertEquals(0.0, BindingConverters.stringToDouble(null), 0.001);
        Assert.assertEquals(0.0, BindingConverters.stringToDouble(""), 0.001);
        Assert.assertEquals(0.0, BindingConverters.stringToDouble("Wrong string"), 0.001);
    }

    @Test
    public void intToString() {
        int i = new Random().nextInt();
        Assert.assertEquals("" + i, BindingConverters.intToString(i));
        Assert.assertEquals("", BindingConverters.intToString(0));
    }

    @Test
    public void stringToInt() {
        int i = new Random().nextInt();
        Assert.assertEquals(i, BindingConverters.stringToInt("" + i));
        Assert.assertEquals(0, BindingConverters.stringToInt(null));
        Assert.assertEquals(0, BindingConverters.stringToInt(""));
        Assert.assertEquals(0, BindingConverters.stringToInt("Wrong string"));
    }

    @Test
    public void integerToString() {
        Integer i = new Random().nextInt();
        Assert.assertEquals("" + i, BindingConverters.integerToString(i));
        Assert.assertEquals("", BindingConverters.integerToString(0));
        Assert.assertEquals("", BindingConverters.integerToString(null));
    }

    @Test
    public void stringToInteger() {
        Integer i = new Random().nextInt();
        Assert.assertEquals(i, BindingConverters.stringToInteger("" + i));
        Assert.assertEquals((Integer) 0, BindingConverters.stringToInteger(null));
        Assert.assertEquals((Integer)0, BindingConverters.stringToInteger(""));
        Assert.assertEquals((Integer)0, BindingConverters.stringToInteger("Wrong string"));
    }

    @Test
    public void listToString() {
        List<String> test = new ArrayList<>();
        test.add("One");
        test.add("Two");
        List<Integer> test2 = new ArrayList<>();
        test2.add(1);
        test2.add(2);
        Assert.assertEquals("One, Two", BindingConverters.listToString(test));
        Assert.assertEquals("", BindingConverters.listToString(new ArrayList<>()));
        Assert.assertEquals("", BindingConverters.listToString(null));
        Assert.assertEquals("1, 2", BindingConverters.listToString(test2));
    }

    @Test
    public void stringToList() {
        List<String> test = new ArrayList<>();
        test.add("One");
        test.add("Two");
        List<String> result = BindingConverters.stringToList("One, Two");

        for (int i = 0; i < result.size(); i++) {
            Assert.assertEquals(test.get(i), result.get(i));
        }
        List<String> result2 = BindingConverters.stringToList("");
        Assert.assertEquals(0, result2.size());
        List<String> result3 = BindingConverters.stringToList(null);
        Assert.assertEquals(0, result3.size());
        List<String> result4 = BindingConverters.stringToList("Blah Blah");
        Assert.assertEquals(1, result4.size());
    }

    @Test
    public void valueToLabel() {
        MockitoAnnotations.initMocks(this);
        String[] entries = {"one","two","three"};
        String[] labels = {"One","Two","Three"};
        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(resources.getStringArray(1)).thenReturn(entries);
        Mockito.when(resources.getStringArray(2)).thenReturn(labels);
        String result = BindingConverters.valueToLabel("two", 1, 2, context);
        Mockito.verify(context).getResources();
        Assert.assertEquals("Two", result);
    }

    @Test
    public void valueToLabelError() {
        MockitoAnnotations.initMocks(this);
        String[] entries = {"one","two","three"};
        String[] labels = {"One","Two","Three"};
        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(resources.getStringArray(1)).thenReturn(entries);
        Mockito.when(resources.getStringArray(2)).thenReturn(labels);
        String result = BindingConverters.valueToLabel("four", 1, 2, context);
        Mockito.verify(context).getResources();
        Assert.assertNull(result);
    }

    @Test
    public void labelToValue() {
        MockitoAnnotations.initMocks(this);
        String[] entries = {"one","two","three"};
        String[] labels = {"One","Two","Three"};
        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(resources.getStringArray(1)).thenReturn(entries);
        Mockito.when(resources.getStringArray(2)).thenReturn(labels);
        String result = BindingConverters.labelToValue("Two", 1, 2, context);
        Mockito.verify(context).getResources();
        Assert.assertEquals("two", result);
    }

    @Test
    public void labelToValueError() {
        MockitoAnnotations.initMocks(this);
        String[] entries = {"one","two","three"};
        String[] labels = {"One","Two","Three"};
        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(resources.getStringArray(1)).thenReturn(entries);
        Mockito.when(resources.getStringArray(2)).thenReturn(labels);
        String result = BindingConverters.labelToValue("Four", 1, 2, context);
        Mockito.verify(context).getResources();
        Assert.assertNull(result);
    }
}