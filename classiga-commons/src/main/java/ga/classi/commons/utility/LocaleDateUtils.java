/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.commons.utility;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import lombok.extern.slf4j.Slf4j;

/**
 * Utility class for date and time formatting with locale support. 
 *
 * @author muhammad
 */
@Slf4j
public class LocaleDateUtils {

    private LocaleDateUtils() {}

    /**
     * The default JVM locale.
     */
    private static Locale locale = Locale.getDefault();

    public static Locale getLocale() {
        return locale;
    }

    public static void setLocale(Locale locale) {
        LocaleDateUtils.locale = locale;
    }

    public static final long MILLIS_PER_SECOND = 1000;
    public static final long MILLIS_PER_MINUTE = 60 * MILLIS_PER_SECOND;
    public static final long MILLIS_PER_HOUR = 60 * MILLIS_PER_MINUTE;
    public static final long MILLIS_PER_DAY = 24 * MILLIS_PER_HOUR;

    private static final int[] PATTERN_STYLES = new int[] {DateFormat.SHORT, DateFormat.MEDIUM, DateFormat.LONG, DateFormat.FULL};
    
    public static String[] datePatterns(Locale locale) {
        String[] datePatterns = new String[PATTERN_STYLES.length];
        int i = 0;
        for (int df : PATTERN_STYLES) {
            SimpleDateFormat sdf = (SimpleDateFormat) DateFormat.getDateInstance(df, locale);
            datePatterns[i] = sdf.toLocalizedPattern();
            i++;
        }
        return datePatterns;
    }

    public static String[] timePatterns(Locale locale) {
        String[] timePatterns = new String[PATTERN_STYLES.length];
        int i = 0;
        for (int tf : PATTERN_STYLES) {
            SimpleDateFormat sdf = (SimpleDateFormat) DateFormat.getTimeInstance(tf, locale);
            timePatterns[i] = sdf.toLocalizedPattern();
            i++;
        }
        return timePatterns;
    }

    public static String[] dateTimePatterns(Locale locale) {
        String[] dateTimePatterns = new String[PATTERN_STYLES.length * PATTERN_STYLES.length];
        int i = 0;
        for (int df : PATTERN_STYLES) {
            for (int tf : PATTERN_STYLES) {
                SimpleDateFormat sdf = (SimpleDateFormat) DateFormat.getDateTimeInstance(df, tf, locale);
                dateTimePatterns[i] = sdf.toLocalizedPattern();
                i++;
            }
        }
        return dateTimePatterns;
    }

    public static String[] allPatterns(Locale locale) {
        int datePatternsCount = PATTERN_STYLES.length;
        int timePatternsCount = PATTERN_STYLES.length;
        int dateTimePatternsCount = datePatternsCount * timePatternsCount;
        String[] allPatterns = new String[datePatternsCount + timePatternsCount + dateTimePatternsCount];
        int dfCount = 0;
        int loop = 0;
        for (int df : PATTERN_STYLES) {
            SimpleDateFormat sdf = (SimpleDateFormat) DateFormat.getDateInstance(df, locale);
            String datePattern = sdf.toLocalizedPattern();
            allPatterns[loop] = datePattern;
            loop++;
            for (int tf : PATTERN_STYLES) {
                sdf = (SimpleDateFormat) DateFormat.getTimeInstance(tf, locale);
                String timePattern = sdf.toLocalizedPattern();
                String dateTimePattern = datePattern + " " + timePattern;
                allPatterns[loop] = dateTimePattern;
                loop++;
                if (dfCount == 0) {
                    allPatterns[loop] = timePattern;
                    loop++;
                }
            }
            dfCount++;
        }
        return allPatterns;
    }

    public static String[] datePatterns() {
        return datePatterns(locale);
    }

    public static String[] timePatterns() {
        return timePatterns(locale);
    }
    
    public static String[] dateTimePatterns() {
        return dateTimePatterns(locale);
    }
        
    public static String[] allPatterns() {
        return allPatterns(locale);
    }
    
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
    public static Date add(Date date, int field, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(field, amount);
        return cal.getTime();
    }

    public static Date substract(Date date, int field, int amount) {
        return add(date, field, -amount);
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
        return toDate(dateString, allPatterns(locale), locale);
    }

    /**
     * Parses a string to {@link Date} object using default array of date patterns
     * and default locale. First pattern that matches the date format of
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
        String dateString;
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