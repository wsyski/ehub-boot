package com.axiell.ehub.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class ConverterUtil {
    public static LocalDate date2LocalDate(final Date date, final ZoneId zoneId) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(zoneId).toLocalDate();
    }

    public static LocalDate date2LocalDate(final Date date) {
        return date2LocalDate(date, ZoneId.systemDefault());
    }

    public static Date localDate2Date(final LocalDate localDate, final ZoneId zoneId) {
        if (localDate == null) {
            return null;
        }
        return Date.from(localDate.atStartOfDay().atZone(zoneId).toInstant());
    }

    public static Date localDate2Date(final LocalDate localDate) {
        return localDate2Date(localDate, ZoneId.systemDefault());
    }
}
