package com.young.java.chat.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dell on 2016/10/31.
 */
public class DateUtils {
    public static String date2String(String format,Date date){
        DateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }
}
