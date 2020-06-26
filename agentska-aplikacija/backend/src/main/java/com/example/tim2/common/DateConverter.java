package com.example.tim2.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateConverter {
    private static DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");       // promeniti patern ili dodati novi
    private static DateTimeFormatter dtf3 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SS");
    private static DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
    private static DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public static Date stringToDate(String date) {
        try {
            return format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static LocalDate dateToLocalDate(String date) {
        return LocalDate.parse(date, dtf1);
    }

    public static LocalDateTime dateToDateTime(String date) {
        LocalDateTime ret = null;
        try {
             ret = LocalDateTime.parse(date, dtf1);
        }catch (Exception e) {
            System.out.println("PRVI EXEPSN");
            try {
                ret = LocalDateTime.parse(date, dtf3);
            }catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return ret;
    }

    public static String currentDate() {
       return  dtf2.format(LocalDate.now());
    }

    public static LocalDate convertFromDateType(Date d) {
        Instant instant = d.toInstant();
        ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
        return zdt.toLocalDate();
    }
}
