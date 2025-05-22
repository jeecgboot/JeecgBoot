package org.jeecg.modules.business.util;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {

    public static Date setToEuropeMorning8(Date original) {
        if (original == null) return null;

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
        cal.setTime(original);
        cal.set(Calendar.HOUR_OF_DAY, 8);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}