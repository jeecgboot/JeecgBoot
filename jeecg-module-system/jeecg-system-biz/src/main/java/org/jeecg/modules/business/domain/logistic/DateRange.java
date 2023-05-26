package org.jeecg.modules.business.domain.logistic;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The class module date interval.
 */
public class DateRange {
    private final Date start;
    private final Date end;
    private final static DateFormat DEFAULT_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * Construct a date interval by its beginning and end.
     *
     * @param start the start of the range
     * @param end   the end of the range
     */
    public DateRange(Date start, Date end) {
        this.start = start;
        this.end = end;
    }

    /**
     * Get the beginning of the range.
     * The date will be formatted to default style: new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
     *
     * @return the beginning of the range.
     */
    public String getStart() {
        return DEFAULT_FORMAT.format(start);
    }

    /**
     * Get the formatted beginning of the range.
     *
     * @param dateFormat the format of the date
     * @return the formatted beginning of the range
     */
    public String getStart(DateFormat dateFormat) {
        return dateFormat.format(start);
    }

    /**
     * Get the end of the date range.
     * The date will be formatted to default style: new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
     *
     * @return the end of the range
     */
    public String getEnd() {
        return DEFAULT_FORMAT.format(end);
    }

    /**
     * Get the formatted end of the date range.
     *
     * @param dateFormat the format of the date
     * @return the end of the range
     */
    public String getEnd(DateFormat dateFormat) {
        return dateFormat.format(end);
    }
}
