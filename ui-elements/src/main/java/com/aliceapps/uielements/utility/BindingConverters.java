package com.aliceapps.uielements.utility;

import android.content.Context;
import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.InverseMethod;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class BindingConverters {

    @NonNull
    @InverseMethod("stringToDate")
    public static String dateToString(Date value) {
        if (value != null) {
            DateFormat sdf = DateFormat.getDateInstance(DateFormat.SHORT);
            return sdf.format(value);
        } else return "";
    }

    @Nullable
    public static Date stringToDate(String value) {
        DateFormat sdf = DateFormat.getDateInstance(DateFormat.SHORT);
        if (value != null) {
            try {
                return sdf.parse(value);
            } catch (ParseException e) {
                return null;
            }
        } else return null;
    }

    @NonNull
    @InverseMethod("stringToDateTime")
    public static String dateTimeToString(Date value) {
        if (value != null) {
            DateFormat sdf = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
            return sdf.format(value);
        } else return "";
    }

    @Nullable
    public static Date stringToDateTime(String value) {
        if (value!= null) {
            DateFormat sdf = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
            try {
                return sdf.parse(value);
            } catch (ParseException e) {
                return null;
            }
        } else return null;
    }

    @InverseMethod("stringToDouble")
    @NonNull
    public static String doubleToString(Double value) {
        if (value != null && value != 0.0) {
            if ((value == Math.floor(value)) && !Double.isInfinite(value))
                return "" + value.intValue();
            else
                return "" + value;
        } else return "";
    }

    @NonNull
    public static Double stringToDouble(String value) {
        if (value != null && !value.equals(""))
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException e) {
                return 0.0;
            }
        else return 0.0;
    }

    @NonNull
    @InverseMethod("stringToInt")
    public static String intToString(int value) {
        if (value != 0)
            return "" + value;
        else return "";
    }

    public static int stringToInt(String value) {
        if (value != null && !value.equals(""))
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                return 0;
            }
        else return 0;
    }

    @NonNull
    @InverseMethod("stringToInteger")
    public static String integerToString(Integer value) {
        if (value != null && value != 0)
            return "" + value;
        else return "";
    }

    @NonNull
    public static Integer stringToInteger(String value) {
        if (value != null && !value.equals(""))
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                return 0;
            }
        else return 0;
    }

    @NonNull
    @InverseMethod("stringToList")
    public static <T> String listToString(List<T> values) {
        if (values != null) {
            String text = values.toString();
            if (text.length() > 2)
                text = text.substring(1, text.length() - 1);
            else
                text = "";
            return text;
        }
        else return "";
    }

    @NonNull
    public static List<String> stringToList(String value) {
        if (value != null && !value.equals("")) {
            String[] list = value.split(",");
            for (int i = 0; i < list.length; i++) {
                String v = list[i];
                list[i] = v.trim();
            }
            return Arrays.asList(list);
        } else
            return new ArrayList<>();
    }

    @InverseMethod("labelToValue")
    public static String valueToLabel(String value, int valueResource, int labelResource, @NonNull Context mContext) {
        Resources resources = mContext.getResources();
        List<String> entries = Arrays.asList(resources.getStringArray(valueResource));
        int position = entries.indexOf(value);
        List<String> labels = Arrays.asList(resources.getStringArray(labelResource));
        if (position != -1 && position < labels.size())
            return labels.get(position);
        else
            return null;
    }

    public static String labelToValue(String label, int valueResource, int labelResource, @NonNull Context mContext) {
        Resources resources = mContext.getResources();
        List<String> labels = Arrays.asList(resources.getStringArray(labelResource));
        int position = labels.indexOf(label);
        List<String> entries = Arrays.asList(resources.getStringArray(valueResource));
        if (position != -1 && position < entries.size())
            return entries.get(position);
        else
            return null;
    }
}
