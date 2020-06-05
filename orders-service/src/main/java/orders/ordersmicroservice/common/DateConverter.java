package orders.ordersmicroservice.common;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateConverter {
    private static DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss.S");       // promeniti patern ili dodati novi
    private static DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
    private static DateTimeFormatter dtf3 = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    //ovo promeniti kad dodje pretraga
    public static LocalDate dateToLocalDate(String date) {
        return LocalDate.parse(date, dtf3);
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
