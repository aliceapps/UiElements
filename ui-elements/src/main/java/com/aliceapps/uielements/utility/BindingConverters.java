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

    /**
     * Converts Date to String
     * @param value - Date
     * @param format - DateFormat
     * @return - String in specified format or DateFormat.SHORT
     */
    @NonNull
    @InverseMethod("stringToDate")
    public static String dateToString(Date value, int format) {
        if (format == 0)
            format = DateFormat.SHORT;

        if (value != null) {
            DateFormat sdf = DateFormat.getDateInstance(format);
            return sdf.format(value);
        } else return "";
    }

    /**
     * Converts String to Date
     * @param value - String
     * @param format - DateFormat
     * @return - Date in specified format or DateFormat.SHORT
     */
    @Nullable
    public static Date stringToDate(String value, int format) {
        if (format == 0)
            format = DateFormat.SHORT;

        DateFormat sdf = DateFormat.getDateInstance(format);
        if (value != null) {
            try {
                return sdf.parse(value);
            } catch (ParseException e) {
                return null;
            }
        } else return null;
    }

    /**
     * Converts Date to String
     * @param value - Date
     * @param dateFormat - DateFormat
     * @param timeFormat - TimeFormat
     * @return String in specified format or DateFormat.SHORT, DateFormat.SHORT
     */
    @NonNull
    @InverseMethod("stringToDateTime")
    public static String dateTimeToString(Date value, int dateFormat, int timeFormat) {
        if (dateFormat == 0)
            dateFormat = DateFormat.SHORT;
        if (timeFormat == 0)
            timeFormat = DateFormat.SHORT;
        if (value != null) {
            DateFormat sdf = DateFormat.getDateTimeInstance(dateFormat, timeFormat);
            return sdf.format(value);
        } else return "";
    }

    /**
     * Converts String to Date
     * @param value - String
     * @param dateFormat - DateFormat
     * @param timeFormat - TimeFormat
     * @return Date in specified format or DateFormat.SHORT, DateFormat.SHORT
     */
    @Nullable
    public static Date stringToDateTime(String value, int dateFormat, int timeFormat) {
        if (dateFormat == 0)
            dateFormat = DateFormat.SHORT;
        if (timeFormat == 0)
            timeFormat = DateFormat.SHORT;
        if (value!= null) {
            DateFormat sdf = DateFormat.getDateTimeInstance(dateFormat, timeFormat);
            try {
                return sdf.parse(value);
            } catch (ParseException e) {
                return null;
            }
        } else return null;
    }

    /**
     * Converts Double to String
     * @param value - Double
     * @return String or ""
     */
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

    /**
     * Converts String to Double
     * @param value - String
     * @return Double or 0
     */
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

    /**
     * Converts int to String
     * @param value - int
     * @return String or ""
     */
    @NonNull
    @InverseMethod("stringToInt")
    public static String intToString(int value) {
        if (value != 0)
            return "" + value;
        else return "";
    }

    /**
     * Converts String to int
     * @param value - String
     * @return int or 0
     */
    public static int stringToInt(String value) {
        if (value != null && !value.equals(""))
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                return 0;
            }
        else return 0;
    }

    /**
     * Converts Integer to String
     * @param value - Integer
     * @return String or ""
     */
    @NonNull
    @InverseMethod("stringToInteger")
    public static String integerToString(Integer value) {
        if (value != null && value != 0)
            return "" + value;
        else return "";
    }

    /**
     * Converts String to Integer
     * @param value - String
     * @return Integer or 0
     */
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

    /**
     * Converts List to String
     * @param values - List
     * @return String or ""
     */
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

    /**
     * Converts String to List
     * @param value - String
     * @return List of String
     */
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

    /**
     * Converts value to label. Used for Spinners
     * @param value - current value
     * @param valueResource - values resource ID
     * @param labelResource - labels resource ID
     * @param mContext - current context
     * @return corresponding label
     */
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

    /**
     * Converts label to value. Used for Spinners
     * @param label - current label
     * @param valueResource - values resource ID
     * @param labelResource - labels resource ID
     * @param mContext - current context
     * @return corresponding value
     */
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
