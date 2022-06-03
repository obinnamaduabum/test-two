package com.revent.test.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyUtils {

    public static Date dateOfTypeStringToDate(String dateString, String datePattern) throws ParseException {
        return new SimpleDateFormat(datePattern, Locale.ENGLISH).parse(dateString);
    }
}
