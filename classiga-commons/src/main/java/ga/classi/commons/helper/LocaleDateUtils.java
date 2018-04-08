package ga.classi.commons.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import lombok.extern.slf4j.Slf4j;

/**
 * Utility class for date and time formatting. Default locale is Indonesia (id).
 *
 * @author Muhammad
 */
@Slf4j
public class LocaleDateUtils {

    private LocaleDateUtils() {}

    /**
     * Array of date patterns commonly used in Indonesia, first string always
     * considered as day.
     */
    public static final String[] DATE_PATTERNS = new String[] {
        "dd-MM-yy",
        "dd-MM-yyyy",
        "dd-MMM-yy",
        "dd-MMM-yyyy",
        "dd-MMMM-yy",
        "dd-MMMM-yyyy",
        "dd/MM/yy",
        "dd/MM/yyyy",
        "dd/MMM/yy",
        "dd/MMM/yyyy",
        "dd/MMMM/yy",
        "dd/MMMM/yyyy",
        "dd MM yy",
        "dd MM yyyy",
        "dd MMM yy",
        "dd MMM yyyy",
        "dd MMMM yy",
        "dd MMMM yyyy"};

    public static final String[] DATE_TIME_PATTERNS = new String[] {};

    /**
     * The default locale is "id" (Indonesia) to get the name of months and days
     * in Indonesian language.
     */
    private static Locale locale = new Locale("id");

    public static Locale getLocale() {
        return locale;
    }

    public static void setLocale(Locale locale) {
        LocaleDateUtils.locale = locale;
    }

    public static final long MILLIS_PER_DAY = 24 * 3600 * 1000;

    /**
     * Returns the number of days between 2 dates.
     *
     * @param olderDate the older date to count the days from
     * @param newerDate the newer date to count the days until
     * @return The number of days between 2 dates.
     */
    public static long daysBetween(Date olderDate, Date newerDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(olderDate);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        Date dateFrom = cal.getTime();

        cal.setTime(newerDate);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        Date dateTo = cal.getTime();

        return Math.round((dateTo.getTime() - dateFrom.getTime()) / ((double) MILLIS_PER_DAY));
    }

    /**
     * Returns new date object that has been added with specified amount for specified
     * date field.
     *
     * @param date the date.
     * @param field the calendar field.
     * @param amount the amount of date or time to be added to the field.
     * @return a date after being added
     */
    public static Date addDate(Date date, int field, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(field, amount);
        return cal.getTime();
    }

    /**
     * Parses a string to {@link Date} object using specified array of date
     * patterns. First pattern that matches the date format of the given string
     * will be used for parsing.
     *
     * @param dateString string to parse to Date
     * @param datePatterns array of date patterns to use for parsing
     * @param locale locale to use for parsing
     * @return A {@link Date} object of specified string
     */
    public static Date toDate(String dateString, String[] datePatterns, Locale locale) {
        Date date = null;
        for (String pattern : datePatterns) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(pattern, locale);
                date = sdf.parse(dateString);
            } catch (ParseException pe) {
                log.warn("Cannot parse date with pattern: {}", pattern);
            }
            if (null != date) {
                break;
            }
        }
        return date;
    }

    /**
     * Parses a string to {@link Date} object using default array of date
     * patterns. First pattern that matches the date format of the given string
     * will be used for parsing.
     *
     * @param dateString string to parse to Date
     * @param locale locale to use for parsing
     * @return A {@link Date} object of specified string
     */
    public static Date toDate(String dateString, Locale locale) {
        return toDate(dateString, DATE_PATTERNS, locale);
    }

    /**
     * Parses a string to {@link Date} object using default array of date patterns
     * and default locale (id). First pattern that matches the date format of
     * the given string will be used for parsing.
     *
     * @param dateString string to parse to Date
     * @return A {@link Date} object of specified string
     */
    public static Date toDate(String dateString) {
        return toDate(dateString, locale);
    }

    /**
     * Parses a string to {@link Date} object using specified array of date
     * patterns and default locale. First pattern that matches the date format
     * of the given string will be used for parsing.
     *
     * @param dateString string to parse to Date
     * @param datePatterns array of date patterns to use for parsing
     * @return A {@link Date} object of specified string
     */
    public static Date toDate(String dateString, String[] datePatterns) {
        return toDate(dateString, datePatterns, locale);
    }

    /**
     * Parses a string to {@link Date} object using specified date pattern and locale. 
     *
     * @param dateString string to parse to Date
     * @param datePattern date pattern to use for parsing
     * @param locale locale to use for parsing
     * @return A {@link Date} object of specified string
     */
    public static Date toDate(String dateString, String datePattern, Locale locale) {
        return toDate(dateString, new String[] {datePattern}, locale);
    }

    /**
     * Parses a string to {@link Date} object using specified date pattern and default locale. 
     *
     * @param dateString string to parse to Date
     * @param datePattern date pattern to use for parsing
     * @return A {@link Date} object of specified string
     */
    public static Date toDate(String dateString, String datePattern) {
        return toDate(dateString, datePattern, locale);
    }

    /**
     * Returns formatted date string of given date millisecond.
     * 
     * @param milliseconds date millisecond since Jan 1, 1970
     * @param pattern date pattern for formatting
     * @param locale locale to use for formatting
     * @return Formatted date string
     */
    public static String toString(long milliseconds, String pattern, Locale locale) {
        String dateString = null;
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, locale);
        dateString = sdf.format(milliseconds);
        return dateString;
    }

    /**
     * Returns formatted date string of given date object.
     *
     * @param date date object to be formatted
     * @param pattern date pattern for formatting
     * @param locale locale to use for formatting
     * @return Formatted date string 
     */
    public static String toString(Date date, String pattern, Locale locale) {
        return toString(date.getTime(), pattern, locale);
    }

    /**
     * Returns formatted date string of given date object.
     *
     * @param date date object to be formatted
     * @param pattern date pattern for formatting
     * @return Formatted date string
     */
    public static String toString(Date date, String pattern) {
        return toString(date, pattern, locale);
    }

    /**
     * Returns formatted date string of given date millisecond.
     * 
     * @param milliseconds date millisecond since Jan 1, 1970
     * @param pattern date pattern for formatting
     * @return Formatted date string
     */
    public static String toString(long milliseconds, String pattern) {
        return toString(milliseconds, pattern, locale);
    }

}