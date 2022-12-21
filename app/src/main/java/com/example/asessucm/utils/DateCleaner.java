package com.example.asessucm.utils;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateCleaner {

    /**
     *
     * @param OGValidTime a time in formet 'yyyy-MM-dd'. UGLY!
     * @return a cleaned up timestamp that's more easy to read.
     * @throws ParseException
     */
    public static String cleanTimeStamp(String OGValidTime) throws ParseException {
        String oldFormat = "yyyy-MM-dd";
        String newFormat = "EEEE\nMMM d";
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat(oldFormat);
        Date validDate = simpleDateFormat.parse(OGValidTime.substring(0,10));
        simpleDateFormat.applyPattern(newFormat);
        Log.i("cleaned Time stamp: ",simpleDateFormat.format(validDate) + " "+ OGValidTime.substring(11,16));
        return simpleDateFormat.format(validDate) + " "+ OGValidTime.substring(11,16);
    }
}
