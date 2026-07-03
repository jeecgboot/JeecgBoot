package org.jeecg.modules.business.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {
    private static final ZoneId PARIS_ZONE = ZoneId.of("Europe/Paris");
    private static final ZoneId SHANGHAI_ZONE = ZoneId.of("Asia/Shanghai");
    private static final DateTimeFormatter MABANG_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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

    public static Date parseMabangFranceTime(String rawTime) {
        if (rawTime == null || rawTime.trim().isEmpty()) {
            return null;
        }
        try {
            LocalDateTime localDateTime = LocalDateTime.parse(rawTime.trim(), MABANG_TIME_FORMATTER);
            return Date.from(localDateTime.atZone(PARIS_ZONE).toInstant());
        } catch (DateTimeParseException ignored) {
            return null;
        }
    }

    /**
     * Keep the same France wall-clock value when the Date is later written to a DB session
     * configured in Asia/Shanghai.
     */
    public static Date normalizeFranceBusinessTimeForShanghaiDb(Date source) {
        if (source == null) {
            return null;
        }
        LocalDateTime franceWallClock = LocalDateTime.ofInstant(source.toInstant(), PARIS_ZONE);
        return Date.from(franceWallClock.atZone(SHANGHAI_ZONE).toInstant());
    }
}
