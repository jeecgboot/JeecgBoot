package org.jeecg.common.util;

import java.math.BigDecimal;

public class NumberUtils {
    public static boolean isEqual(BigDecimal a, BigDecimal b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return a.compareTo(b) == 0;
    }
}
