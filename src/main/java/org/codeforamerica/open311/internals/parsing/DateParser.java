package org.codeforamerica.open311.internals.parsing;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * Provides operations to handle dates. Check the <a
 * href="http://wiki.open311.org/GeoReport_v2#Date.2Ftime_format">GeoReport
 * wiki</a> for more information.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public class DateParser {

	/**
	 * List of possible formats sorted by preference.
	 */
	private DateTimeFormatter[] dateFormats = {
			ISODateTimeFormat.dateTimeNoMillis(),
			DateTimeFormat.forPattern("YYYY-MM-DD HH:mm") };

	/**
	 * Sets the timezone of the system.
	 * 
	 * @param timeZone
	 *            A valid timezone.
	 */
	public DateParser withTimezone(DateTimeZone timeZone) {
		for (int i = 0; i < dateFormats.length; i++) {
			dateFormats[i] = dateFormats[i].withZone(timeZone);
		}
		return this;
	}

	/**
	 * Parses a string representing a date.
	 * 
	 * @param rawDate
	 *            ISO 8601 is the preferred format. Check
	 *            <code>dateFormats</code> to check all the accepted formats.
	 * @return A date object.
	 */
	public Date parseDate(String rawDate) {
            for (DateTimeFormatter dateFormat : dateFormats) {
                try {
                    return dateFormat.parseDateTime(rawDate).toDate();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
	}

	/**
	 * Prints a date. ISO 8601 is the preferred format. Check
	 * <code>dateFormats</code> to check all the accepted formats.
	 * 
	 * @param date
	 *            Date to print.
	 * @return ISO 8601 format date if possible (else, the first valid) or
	 *         <code>null</code> if it didn't match any format.
	 */
	public String printDate(Date date) {
            for (DateTimeFormatter dateFormat : dateFormats) {
                try {
                    return dateFormat.print(new DateTime(date));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
    }
}
