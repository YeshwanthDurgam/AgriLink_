package com.agrilink.common.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Utility class for date and time operations.
 */
public final class DateUtils {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    private DateUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
    
    public static String formatDate(LocalDate date) {
        return date != null ? date.format(DATE_FORMATTER) : null;
    }
    
    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(DATETIME_FORMATTER) : null;
    }
    
    public static LocalDate parseDate(String dateStr) {
        return dateStr != null ? LocalDate.parse(dateStr, DATE_FORMATTER) : null;
    }
    
    public static LocalDateTime parseDateTime(String dateTimeStr) {
        return dateTimeStr != null ? LocalDateTime.parse(dateTimeStr, DATETIME_FORMATTER) : null;
    }
    
    public static Date toDate(LocalDateTime localDateTime) {
        return localDateTime != null 
                ? Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()) 
                : null;
    }
    
    public static LocalDateTime toLocalDateTime(Date date) {
        return date != null 
                ? date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime() 
                : null;
    }
}
