package org.jeecg.modules.fms.reimburse.biz.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtils {

	public static String getTimeNowStr() {
		Date today = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = simpleDateFormat.format(today);
		return dateStr;
	}
}
