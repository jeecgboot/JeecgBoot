package org.jeecg.modules.im.base.tools;


import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日期时间相关
 */
public abstract class ToolDateTime {
	protected final static Logger log = LogManager.getLogger(ToolDateTime.class);

	public static final String pattern_ym = "yyyy-MM"; // pattern_ym
	public static final int pattern_ym_length = 7;

	public static final String pattern_ymd = "yyyy-MM-dd"; // pattern_ymd
	public static final String pattern_ymd2 = "yyyy年MM月dd日";
	public static final String pattern_ymd3 = "yyyyMMdd";
	public static final String pattern_ymd4 = "yyyy.MM.dd";
	public static final String pattern_ymd5 = "yyyy/MM/dd";
	public static final int pattern_ymd_length = 10;
	public static final String pattern_md = "MM-dd"; // pattern_ymd


	public static final String pattern_ymd_hm = "yyyy-MM-dd HH:mm"; // pattern_ymd hm
	public static final String pattern_y = "yyyy"; // pattern_ymd hm
	public static final int pattern_ymd_hm_length = 16;

	public static final String pattern_ymd_hms = "yyyy-MM-dd HH:mm:ss"; // pattern_ymd time
	public static final String pattern_ymd_hms2 = "yyyy年MM月dd日 HH:mm:ss"; // pattern_ymd time
	public static final int pattern_ymd_hms_length = 19;

	public static final String pattern_ymd_hms_s = "yyyy-MM-dd HH:mm:ss:SSS"; // pattern_ymd timeMillisecond
	public static final String pattern_hms = "HH:mm:ss"; // pattern_ymd timeMillisecond
	public static final int pattern_ymd_hms_s_length = 23;


	public static final String pattern_ymd_hm2 = "yyyy年MM月dd日 HH:mm";
	public static final String[] zodiacArr = { "猴", "鸡", "狗", "猪", "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊" };
	public static final String[] constellationArr = { "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "魔羯座" };
	public static final int[] constellationEdgeDay = { 20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22 };

	/**
	 * 主要是给jfinal使用，数据库只认java.sql.*
	 *
	 * @return
	 */
	public static Timestamp getSqlTimestamp() {
		return getSqlTimestamp(new Date().getTime());
	}

	/**
	 * 主要是给jfinal使用，数据库只认java.sql.*
	 *
	 * @param date
	 * @return
	 */
	public static Timestamp getSqlTimestamp(Date date) {
		if (null == date) {
			return getSqlTimestamp();
		}
		return getSqlTimestamp(date.getTime());
	}

	/**
	 * 主要是给jfinal使用，数据库只认java.sql.*
	 *
	 * @param time
	 * @return
	 */
	public static Timestamp getSqlTimestamp(long time) {
		return new Timestamp(time);
	}

	/**
	 * 主要是给jfinal使用，数据库只认java.sql.*
	 *
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static Timestamp getSqlTimestamp(String date, String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		try {
			return new Timestamp(format.parse(date).getTime());
		} catch (ParseException e) {
			log.error("ToolDateTime.parse异常：date值" + date + "，pattern值" + pattern);
			return null;
		}
	}

	/**
	 * 获取当前时间
	 *
	 * @return
	 */
	public static Date getDate() {
		return new Date();
	}
	public static Date getDate(Long time) {
		return new Date(time);
	}
	public static String getDate(String pattern) {
		return format(new Date(),pattern);
	}
	public static Date getDate(String date,String pattern) {
		return parse(date,pattern);
	}
	public static String getDate(Date date,String pattern) {
		return format(date,pattern);
	}

	/**
	 * 判断时间处于给定范围之内
	 * @param time
	 * @param begin
	 * @param end
	 * @return
	 */
	public static Boolean timeInArrange(String time,Time begin,Time end) {
		return begin.toString().compareTo(time)<=0&&end.toString().compareTo(time)>=0;
	}

	public static String getNowTime(){
		return format(new Date(),pattern_hms);
	}

	/**
	 * 获取指定日期
	 *
	 * @param date
	 * @param hour
	 * @param minute
	 * @param second
	 * @param millisecond
	 * @return
	 */
	public static Date getDate(int date, int hour, int minute, int second, int millisecond) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, date);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, second);
		calendar.set(Calendar.MILLISECOND, millisecond);
		return calendar.getTime();
	}

	/**
	 * 获取当前时间的时间戳
	 *
	 * @return
	 */
	public static long getDateByTime() {
		return new Date().getTime();
	}

	/**
	 * 格式化
	 *
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String format(Date date, String pattern) {
		if(date==null){
			return "";
		}
		DateFormat format = new SimpleDateFormat(pattern);
		return format.format(date);
	}

	/**
	 * 格式化
	 * @param date
	 * @param pattern
	 * @param timeZone
	 * @return
	 */
	public static String format(Date date, String pattern, TimeZone timeZone) {
		DateFormat format = new SimpleDateFormat(pattern);
		format.setTimeZone(timeZone);
		return format.format(date);
	}

	/**
	 * 格式化
	 * @param date
	 * @param parsePattern
	 * @param returnPattern
	 * @return
	 */
	public static String format(String date, String parsePattern, String returnPattern) {
		return format(parse(date, parsePattern), returnPattern);
	}

	/**
	 * 格式化
	 * @param date
	 * @param parsePattern
	 * @param returnPattern
	 * @param timeZone
	 * @return
	 */
	public static String format(String date, String parsePattern, String returnPattern, TimeZone timeZone) {
		return format(parse(date, parsePattern), returnPattern, timeZone);
	}

	/**
	 * 解析
	 *
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static Date parse(String date, String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		try {
			if(StringUtils.isEmpty(date)){
				return null;
			}
			return format.parse(date);
		} catch (ParseException e) {
			log.error("ToolDateTime.parse异常：date值" + date + "，pattern值" + pattern);
			return null;
		}
	}

	/**
	 * 解析
	 *
	 * @param dateStr
	 * @return
	 */
	public static Date parse(String dateStr) {
		Date date = null;
		try {
			date = DateFormat.getDateTimeInstance().parse(dateStr);
		} catch (ParseException e) {
			log.error("ToolDateTime.parse异常：date值" + date);
			return null;
		}
		return date;
	}

	/**
	 * 两个日期的时间差，返回"X天X小时X分X秒"
	 *
	 * @param start
	 * @param end
	 * @return
	 */
	public static String getBetween(Date start, Date end) {
		long between = (end.getTime() - start.getTime()) / 1000;// 除以1000是为了转换成秒
		long day = between / (24 * 3600);
		long hour = between % (24 * 3600) / 3600;
		long minute = between % 3600 / 60;
		long second = between % 60;

		StringBuilder sb = new StringBuilder();
		if(day!=0) {
			sb.append(day);
			sb.append("天");
		}
		if(hour!=0) {
			sb.append(hour);
			sb.append("小时");
		}
		if(minute!=0) {
			sb.append(minute);
			sb.append("分");
		}
		if(second!=0) {
			sb.append(second);
			sb.append("秒");
		}

		return sb.toString();
	}

	public static String getBetweenUntilMinute(Date start, Date end) {
		long between = (end.getTime() - start.getTime()) / 1000;// 除以1000是为了转换成秒
		long hour = between % (24 * 3600) / 3600;
		long minute = between % 3600 / 60;

		StringBuilder sb = new StringBuilder();
		if(hour!=0) {
			sb.append(hour);
			sb.append("小时");
		}
		if(minute!=0) {
			sb.append(minute);
			sb.append("分钟");
		}
		return sb.toString();
	}
	/**
	 * 两个日期的时间差，返回list
	 *
	 * @param start
	 * @param end
	 * @return
	 */
	public static Map<String,Long> getAllBetween(Date start, Date end) {
		Long between = (end.getTime() - start.getTime()) / 1000;// 除以1000是为了转换成秒
		Long day = between / (24 * 3600);
		Long hour = between % (24 * 3600) / 3600;
		Long minute = between % 3600 / 60;
		Long second = between % 60 / 60;

		Map<String,Long> map = new HashMap<>();
		map.put("day",day);
		map.put("hour",hour);
		map.put("minute",minute);
		map.put("second",second);
		return map;
	}

	/**
	 * 返回当前日期加n小时后的时间
	 */
	public static Date getDateByPlusHours(Integer hours){
		DateTime now = new DateTime();
		return now.plusHours(hours).toDate();
	}
	/**
	 * 返回当前日期加n秒后的时间
	 */
	public static Date getDateByPlusSeconds(Integer seconds){
		DateTime now = new DateTime();
		return now.plusSeconds(seconds).toDate();
	}
	/**
	 * 返回指定日期加n秒后的时间
	 */
	public static Date getDateByDatePlusSeconds(Date date,Integer seconds){
		return new DateTime(date).plusSeconds(seconds).toDate();
	}
	/**
	 * 返回指定日期加n天后的时间
	 */
	public static Date getDateByDatePlusDays(Date date,Integer days){
		return new DateTime(date).plusDays(days).toDate();
	}
	/**
	 * 返回指定日期加n天后的时间
	 */
	public static Date getDateByDatePlusHours(Date date,Integer hours){
		return new DateTime(date).plusHours(hours).toDate();
	}

	/**
	 * 返回两个日期之间隔了多少秒
	 *
	 * @param start
	 * @param end
	 * @return
	 */
	public static int getDateSecondSpace(Date start, Date end) {
		if(start==null||end==null){
			return 0;
		}
		return (int) ((end.getTime() - start.getTime()) / (1000));
	}
	public static long getDateMinSecondSpace(Date start, Date end) {
		if(start==null||end==null){
			return 0;
		}
		return end.getTime() - start.getTime();
	}
	public static int getDateSecondSpace(Long start, Long end) {
		if(start==null||end==null){
			return 0;
		}
		return (int) ((end - start) / (1000));
	}
	public static long getDateMinSecondSpace(Long start, Long end) {
		if(start==null||end==null){
			return 0;
		}
		return end - start;
	}
	/**
	 * 返回两个日期之间隔了多少分钟
	 *
	 * @param start
	 * @param end
	 * @return
	 */
	public static int getDateMinuteSpace(Date start, Date end) {
		return (int) ((end.getTime() - start.getTime()) / (60 * 1000));
	}

	/**
	 * 返回两个日期之间隔了多少小时
	 *
	 * @param start
	 * @param end
	 * @return
	 */
	public static int getDateHourSpace(Date start, Date end) {
		return (int) ((end.getTime() - start.getTime()) / (60 * 60 * 1000));
	}

	/**
	 * 返回两个日期之间隔了多少天
	 *
	 * @param start
	 * @param end
	 * @return
	 */
	public static int getDateDaySpace(Date start, Date end) {
		int day = (int) ((end.getTime() - start.getTime()) / (60 * 60 * 24 * 1000));
		return day;
	}

	/**
	 * 得到某一天是星期几
	 *
	 *            日期字符串
	 * @return String 星期几
	 */
	@SuppressWarnings("static-access")
	public static String getDateInWeek(Date date) {
		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int dayIndex = calendar.get(calendar.DAY_OF_WEEK) - calendar.SUNDAY;
		if (dayIndex < 0) {
			dayIndex = 0;
		}
		return weekDays[dayIndex];
	}

	/**
	 * 日期减去多少个小时
	 *
	 * @param date
	 * @param hourCount
	 *            多少个小时
	 * @return
	 */
	public static Date getDateReduceHour(Date date, long hourCount) {
		long time = date.getTime() - 3600 * 1000 * hourCount;
		Date dateTemp = new Date();
		dateTemp.setTime(time);
		return dateTemp;
	}
	/**
	 * 返回当前日期加n天后的时间
	 */
	public static Date getDateByPlusDays(Integer days){
		DateTime now = new DateTime();
		return now.plusDays(days).toDate();
	}
	/**
	 * 返回当前日期加n天后的时间
	 */
	public static Date getDateByPlusDays(Date date,Integer days){
		return new DateTime(date).plusDays(days).toDate();
	}

	public static Date getDateByPlusDays(String date,String pattern,Integer days){
		return ToolDateTime.getDateByPlusDays(ToolDateTime.parse(date,pattern),days);
	}



	/**
	 * 日期区间分割
	 *
	 * @param start
	 * @param end
	 * @param splitCount
	 * @return
	 */
	public static List<Date> getDateSplit(Date start, Date end, long splitCount) {
		long startTime = start.getTime();
		long endTime = end.getTime();
		long between = endTime - startTime;

		long count = splitCount - 1l;
		long section = between / count;

		List<Date> list = new ArrayList<Date>();
		list.add(start);

		for (long i = 1l; i < count; i++) {
			long time = startTime + section * i;
			Date date = new Date();
			date.setTime(time);
			list.add(date);
		}

		list.add(end);

		return list;
	}

	public static void main(String[] args) throws ParseException {
		System.out.println(ToolDateTime.getStartTimeOfDate(ToolDateTime.getDateByDatePlusDays(new Date(), -365)));
	}

	/**
	 * 返回两个日期之间隔了多少天，包含开始、结束时间
	 *
	 * @param start
	 * @param end
	 * @return
	 */
	public static List<String> getDaySpaceDate(Date start, Date end) {
		Calendar fromCalendar = Calendar.getInstance();
		fromCalendar.setTime(start);
		fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
		fromCalendar.set(Calendar.MINUTE, 0);
		fromCalendar.set(Calendar.SECOND, 0);
		fromCalendar.set(Calendar.MILLISECOND, 0);

		Calendar toCalendar = Calendar.getInstance();
		toCalendar.setTime(end);
		toCalendar.set(Calendar.HOUR_OF_DAY, 0);
		toCalendar.set(Calendar.MINUTE, 0);
		toCalendar.set(Calendar.SECOND, 0);
		toCalendar.set(Calendar.MILLISECOND, 0);

		List<String> dateList = new LinkedList<String>();

		long dayCount = (toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24);
		if (dayCount < 0) {
			return dateList;
		}

		dateList.add(format(fromCalendar.getTime(), pattern_ymd));

		for (int i = 0; i < dayCount; i++) {
			fromCalendar.add(Calendar.DATE, 1);// 增加一天
			dateList.add(format(fromCalendar.getTime(), pattern_ymd));
		}

		return dateList;
	}

	/**
	 * 获取开始时间
	 *
	 * @param start
	 * @param end
	 * @return
	 */
	public static Date startDateByDay(Date start, int end) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(start);
		calendar.add(Calendar.DATE, end);// 明天1，昨天-1
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date date = calendar.getTime();
		return date;
	}

	/**
	 * 获取结束时间
	 *
	 * @param start
	 * @param start
	 * @return
	 */
	public static Date endDateByDay(Date start) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(start);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		Date date = calendar.getTime();
		return date;
	}

	/**
	 * 获取开始时间
	 *
	 * @param start
	 * @param end
	 * @return
	 */
	public static Date startDateByHour(Date start, int end) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(start);
		calendar.set(Calendar.MINUTE, end);
		Date date = calendar.getTime();
		return date;
	}

	/**
	 * 获取结束时间
	 *
	 * @param end
	 * @param end
	 * @return
	 */
	public static Date endDateByHour(Date end) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(end);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		Date date = calendar.getTime();
		return date;
	}

	/**
	 * 根据年份和周得到周的开始和结束日期
	 *
	 * @param year
	 * @param week
	 * @return
	 */
	public static Map<String, Date> getStartEndDateByWeek(int year, int week) {
		Calendar weekCalendar = new GregorianCalendar();
		weekCalendar.set(Calendar.YEAR, year);
		weekCalendar.set(Calendar.WEEK_OF_YEAR, week);
		weekCalendar.set(Calendar.DAY_OF_WEEK, weekCalendar.getFirstDayOfWeek());

		Date startDate = weekCalendar.getTime(); // 得到周的开始日期

		weekCalendar.roll(Calendar.DAY_OF_WEEK, 6);
		Date endDate = weekCalendar.getTime(); // 得到周的结束日期

		// 开始日期往前推一天
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(startDate);
		startCalendar.add(Calendar.DATE, 1);// 明天1，昨天-1
		startCalendar.set(Calendar.HOUR_OF_DAY, 0);
		startCalendar.set(Calendar.MINUTE, 0);
		startCalendar.set(Calendar.SECOND, 0);
		startCalendar.set(Calendar.MILLISECOND, 0);
		startDate = startCalendar.getTime();

		// 结束日期往前推一天
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(endDate);
		endCalendar.add(Calendar.DATE, 1);// 明天1，昨天-1
		endCalendar.set(Calendar.HOUR_OF_DAY, 23);
		endCalendar.set(Calendar.MINUTE, 59);
		endCalendar.set(Calendar.SECOND, 59);
		endCalendar.set(Calendar.MILLISECOND, 999);
		endDate = endCalendar.getTime();

		Map<String, Date> map = new HashMap<String, Date>();
		map.put("start", startDate);
		map.put("end", endDate);
		return map;
	}

	/**
	 * 根据日期月份，获取月份的开始和结束日期
	 *
	 * @param date
	 * @return
	 */
	public static Map<String, Date> getMonthDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		// 得到前一个月的第一天
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		Date start = calendar.getTime();

		// 得到前一个月的最后一天
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		Date end = calendar.getTime();

		Map<String, Date> map = new HashMap<String, Date>();
		map.put("start", start);
		map.put("end", end);
		return map;
	}

	/**
	 * 分割List
	 *
	 * @param list
	 *            待分割的list
	 * @param pageSize
	 *            每段list的大小
	 * @return List<<List<T>>
	 */
	public static <T> List<List<T>> splitList(List<T> list, int pageSize) {
		int listSize = list.size(); // list的大小
		int page = (listSize + (pageSize - 1)) / pageSize; // 页数

		List<List<T>> listArray = new ArrayList<List<T>>();// 创建list数组
		// ,用来保存分割后的list

		for (int i = 0; i < page; i++) { // 按照数组大小遍历
			List<T> subList = new ArrayList<T>(); // 数组每一位放入一个分割后的list
			for (int j = 0; j < listSize; j++) { // 遍历待分割的list
				int pageIndex = ((j + 1) + (pageSize - 1)) / pageSize; // 当前记录的页码(第几页)
				if (pageIndex == (i + 1)) { // 当前记录的页码等于要放入的页码时
					subList.add(list.get(j)); // 放入list中的元素到分割后的list(subList)
				}

				if ((j + 1) == ((j + 1) * pageSize)) { // 当放满一页时退出当前循环
					break;
				}
			}
			listArray.add(subList); // 将分割后的list放入对应的数组的位中
		}
		return listArray;
	}
	//获取今天的开始时间
	public static Date getStartTimeOfToday(){
		return getStartTimeOfDate(new Date());
	}
	//获取今天的结束时间
	public static Date getEndTimeOfToday(){
		return getEndTimeOfDate(new Date());
	}
	//获取某天的结束时间
	public static Date getStartTimeOfDate(Date date){
		if(date==null){
			return null;
		}
		Calendar todayStart = Calendar.getInstance();
		todayStart.set(Calendar.MONTH, date.getMonth());
		todayStart.set(Calendar.DAY_OF_MONTH, date.getDate());
		todayStart.set(Calendar.HOUR_OF_DAY, 0);
		todayStart.set(Calendar.MINUTE, 0);
		todayStart.set(Calendar.SECOND, 0);
		todayStart.set(Calendar.MILLISECOND, 0);
		todayStart.set(Calendar.YEAR, 1900+date.getYear());
		return todayStart.getTime();
	}

	public static String getStartTimeStrOfDate(Date date){
		return format(getStartTimeOfDate(date),ToolDateTime.pattern_ymd_hms);
	}
	//获取某天的结束时间
	public static Date getEndTimeOfDate(Date date){
		if(date==null){
			return null;
		}
		Calendar todayEnd = Calendar.getInstance();
		todayEnd.set(Calendar.MONTH, date.getMonth());
		todayEnd.set(Calendar.DAY_OF_MONTH, date.getDate());
		todayEnd.set(Calendar.HOUR_OF_DAY, 23);
		todayEnd.set(Calendar.MINUTE, 59);
		todayEnd.set(Calendar.SECOND, 59);
		todayEnd.set(Calendar.MILLISECOND, 999);
		todayEnd.set(Calendar.YEAR, 1900+date.getYear());
		return todayEnd.getTime();
	}
	public static String getEndTimeStrOfDate(Date date){
		return format(getEndTimeOfDate(date),ToolDateTime.pattern_ymd_hms);
	}
	/**
	 * 微信订单日期转换
	 * @param date
	 * @param index
	 * @return
	 */
	public static String dateTrans(String date, int index) {
		String oldFormat = "yyyyMMddHHmmss";
		String newFormat = "yyyy-MM-dd HH:mm:ss";
		String result = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(oldFormat);
			ParsePosition pos = new ParsePosition(index);
			Date d = sdf.parse(date, pos);
			sdf = new SimpleDateFormat(newFormat);
			result = sdf.format(d);
		} catch (Exception err) {
			System.out.println("Trans Error: " + err.getMessage());
		}
		return result;
	}


	public static int compare(Date date1,Date date2) {
		try {
			if (date1.getTime() > date2.getTime()) {
				return 1;
			} else if (date1.getTime() < date2.getTime()) {
				return -1;
			} else {
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}



	/**
	 * 返回昨天的日期
	 *
	 * @return
	 * @throws ParseException
	 */
	public static Date getYesterDay() throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar date = Calendar.getInstance();
		date.set(Calendar.DATE, date.get(Calendar.DATE) - 1);
		Date yesterDay = simpleDateFormat.parse(simpleDateFormat.format(date.getTime()));
		return yesterDay;
	}
	public static Date getPreviousDay(Date date) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar theDate = Calendar.getInstance();
		theDate.set(Calendar.DATE, theDate.get(Calendar.DATE) - 1);
		theDate.set(Calendar.MONTH,theDate.get(Calendar.MONTH)-1);
		Date yesterDay = simpleDateFormat.parse(simpleDateFormat.format(date.getTime()));
		return yesterDay;
	}

	/**
	 * 返回今天的日期
	 *
	 * @return
	 * @throws ParseException
	 */
	public static Date getToDay() throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar date = Calendar.getInstance();
		Date today = simpleDateFormat.parse(simpleDateFormat.format(date.getTime()));
		return today;
	}


	/**
	 * 返回明天的日期
	 *
	 * @return
	 * @throws ParseException
	 */
	public static Date getTomorrow() throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar date = Calendar.getInstance();
		date.set(Calendar.DATE, date.get(Calendar.DATE) + 1);
		Date tomorrow = simpleDateFormat.parse(simpleDateFormat.format(date.getTime()));
		return tomorrow;
	}

	/**
	 * 返回当月第一天的日期
	 *
	 * @return
	 * @throws ParseException
	 */
	public static Date getThisMonthFirstDay() throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar date = Calendar.getInstance();
		date.add(Calendar.MONTH, 0);
		date.set(Calendar.DAY_OF_MONTH, 1);
		Date thisMonth = simpleDateFormat.parse(simpleDateFormat.format(date.getTime()));
		return thisMonth;
	}

	/**
	 * 返回日期月份第一天的日期
	 *
	 * @return
	 * @throws ParseException
	 */
	public static Date getMonthFirstDay(Date date) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		Date thisMonth = simpleDateFormat.parse(simpleDateFormat.format(calendar.getTime()));
		return thisMonth;
	}

	/**
	 * 返回上月第一天的日期
	 *
	 * @return
	 * @throws ParseException
	 */
	public static Date getPreMonthFirstDay() throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar date = Calendar.getInstance();
		date.add(Calendar.MONTH, -1);
		date.set(Calendar.DAY_OF_MONTH, 1);
		Date thisMonth = simpleDateFormat.parse(simpleDateFormat.format(date.getTime()));
		return thisMonth;
	}

	/**
	 * 返回当月最后一天的日期
	 *
	 * @return
	 * @throws ParseException
	 */
	public static Date getThisMonthLastDay() throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar date = Calendar.getInstance();
		date.set(Calendar.DAY_OF_MONTH, date.getActualMaximum(Calendar.DAY_OF_MONTH));
		Date thisMonth = simpleDateFormat.parse(simpleDateFormat.format(date.getTime()));
		return thisMonth;
	}

	/**
	 * 返回当月最后一天的日期
	 *
	 * @return
	 * @throws ParseException
	 */
	public static Date getMonthLastDay(Date date) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		Date thisMonth = simpleDateFormat.parse(simpleDateFormat.format(calendar.getTime()));
		return thisMonth;
	}

	/**
	 * 将日期转化成yyyymmdd格式，day为转化后的时间与当前时间的相差的天数，正数为当前时间之后，负数为当前时间之前
	 *
	 * @param beginDate
	 * @param day
	 * @return
	 * @throws ParseException
	 */
	public static Date getDistanceDate(Date beginDate, int day) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		Calendar date = Calendar.getInstance();
		date.setTime(beginDate);
		date.set(Calendar.DATE, date.get(Calendar.DATE) + day);
		Date endDate = simpleDateFormat.parse(simpleDateFormat.format(date.getTime()));
		return endDate;
	}

	public static boolean isSameDay(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);

		boolean isSameYear = cal1.get(Calendar.YEAR) == cal2
				.get(Calendar.YEAR);
		boolean isSameMonth = isSameYear
				&& cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
		boolean isSameDate = isSameMonth
				&& cal1.get(Calendar.DAY_OF_MONTH) == cal2
				.get(Calendar.DAY_OF_MONTH);

		return isSameDate;
	}

	/**
	 * 根据日期获取生肖
	 *
	 * @param date
	 * @return
	 */
	public static String getZodica(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return zodiacArr[cal.get(Calendar.YEAR) % 12];
	}

	/**
	 * 根据日期获取星座
	 *
	 * @param date
	 * @return
	 */
	public static String getConstellation(Date date) {
		if (date == null) {
			return "";
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		if (day < constellationEdgeDay[month]) {
			month = month - 1;
		}
		if (month >= 0) {
			return constellationArr[month];
		}
		// default to return 魔羯
		return constellationArr[11];
	}

	/**
	 * 根据日期获取年龄
	 *
	 * @param birthday
	 * @return
	 */
	public static int getAge(Date birthday) {
		int age = 0;
		try {
			if (birthday==null){
				return 0;
			}
			Calendar now = Calendar.getInstance();
			now.setTime(new Date());// 当前时间
			Calendar birth = Calendar.getInstance();
			birth.setTime(birthday);
			if (birth.after(now)) {//如果传入的时间，在当前时间的后面，返回0岁
				age = 0;
			} else {
				age = now.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
				if (now.get(Calendar.DAY_OF_YEAR) > birth.get(Calendar.DAY_OF_YEAR)) {
					age += 1;
				}
			}
			return age;
		} catch (Exception e) {//兼容性更强,异常后返回数据
			return 0;
		}
	}
}
