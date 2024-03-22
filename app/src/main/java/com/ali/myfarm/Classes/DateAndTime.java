package com.ali.myfarm.Classes;

import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

public class DateAndTime {
    public static LocalDateTime getLocalTime() {
        return LocalDateTime.now();
    }

    public static String timeFormatter(LocalDateTime time) {
        return time.format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a"));
    }

    public static String getCurrentDateTime() {
        return DateAndTime.timeFormatter(DateAndTime.getLocalTime());
    }

    public static String getYear() {
        return Integer.toString(LocalDate.now().getYear());
    }

    public static int getCurrentMonth() {
        return Calendar.getInstance().get(Calendar.MONTH);
    }

    public static String getArabicNameOfMonth() {
        return new DateFormatSymbols(new Locale("ar")).getMonths()[getCurrentMonth()];
    }

    public static String getArabicNameOfMonth(int month) {
        return new DateFormatSymbols(new Locale("ar")).getMonths()[month];
    }

    public static String getMonthAndYear() {
        return String.format("%s, %s", getArabicNameOfMonth(), getYear());
    }

    public static String getMonthAndYear(int month, String year) {
        return String.format("%s, %s", getArabicNameOfMonth(month), year);
    }
}
