package com.ks3.demo.main.utils;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;

public class DateUtils {

    public static enum DATETIME_PROTOCOL {
        RFC1123, ISO8861;
    };
    public static Date parseRfc822Date(String dateString) throws ParseException {
        return getRfc822DateFormat().parse(dateString);
    }

    private static DateFormat getRfc822DateFormat() {
        SimpleDateFormat rfc822DateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        rfc822DateFormat.setTimeZone(new SimpleTimeZone(0, "GMT"));
        return rfc822DateFormat;
    }
    /**
     * 获取指定时间对应的毫秒数
     * @param time "HH:mm:ss"
     * @return
     */
    public static long getTimeMillis(String time) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
            DateFormat dayFormat = new SimpleDateFormat("yy-MM-dd");
            Date curDate = dateFormat.parse(dayFormat.format(new Date()) + " " + time);
            return curDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public static String convertDate2Str(Date date, DATETIME_PROTOCOL protocol) {
        if (protocol.equals(DATETIME_PROTOCOL.RFC1123)) {

            org.joda.time.format.DateTimeFormatter RFC1123_DATE_TIME_FORMATTER = DateTimeFormat
                    .forPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'")
                    .withZoneUTC().withLocale(Locale.ENGLISH);

            return RFC1123_DATE_TIME_FORMATTER.print(date.getTime());

        } else if (protocol.equals(DATETIME_PROTOCOL.ISO8861)) {

            DateTimeFormatter ISO8861_FORMATTER = ISODateTimeFormat.dateTime()
                    .withZoneUTC();
            return ISO8861_FORMATTER.print(date.getTime());
        }
        return null;
    }

}