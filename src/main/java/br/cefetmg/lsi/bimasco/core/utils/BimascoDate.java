package br.cefetmg.lsi.bimasco.core.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BimascoDate {

    private static final String FORMAT_DEFAULT = "HH:mm:ss";

    public static Date getTime(){
        return Calendar.getInstance().getTime();
    }

    public static String formattedTime(){
        return new SimpleDateFormat(FORMAT_DEFAULT).format(getTime());
    }
}
