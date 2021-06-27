package com.aliceapps.uielements.utility;

import androidx.room.TypeConverter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

public class TypeConverters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static LocalDate localDateFromEpoch(Long value) {
        return value == null ? null : LocalDate.ofEpochDay(value);
    }

    @TypeConverter
    public static Integer localTimeToSeconds(LocalTime date) {
        return date == null ? null : date.toSecondOfDay();
    }

    @TypeConverter
    public static LocalTime secondsToLocalTime(Integer value) {
        return value == null ? null : LocalTime.ofSecondOfDay(value);
    }

    @TypeConverter
    public static Long localDateToEpoch(LocalDate date) {
        return date == null ? null : date.toEpochDay();
    }
}
