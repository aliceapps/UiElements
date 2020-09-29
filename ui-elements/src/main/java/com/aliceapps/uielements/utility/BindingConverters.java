package com.aliceapps.uielements.utility;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.InverseMethod;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

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
        try {
            return sdf.parse(value);
        } catch (ParseException e) {
            return null;
        }
    }

    @InverseMethod("stringToDouble")
    @NonNull
    public static String doubleToString (Double value) {
        if (value != null && value != 0.0)
            return "" + value;
        else return "";
    }

    @NonNull
    public static Double stringToDouble(@NonNull String value) {
        if (!value.equals(""))
            return Double.parseDouble(value);
        else return 0.0;
    }

    @NonNull
    @InverseMethod("stringToInt")
    public static String intToString(int value) {
        if (value != 0)
            return "" + value;
        else return "";
    }

    public static int stringToInt(@NonNull String value) {
        if (!value.equals(""))
            return Integer.parseInt(value);
        else return 0;
    }
}
