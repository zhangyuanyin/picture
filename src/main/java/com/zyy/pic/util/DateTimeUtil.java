package com.zyy.pic.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.util.StringUtils;

/**
 * date，time，datetime的辅助类
 */
public class DateTimeUtil {
	/**
	 * 取得当前的Timestamp
	 * 
	 * @return 当前的Timestamp
	 */
	public static java.sql.Timestamp nowTimestamp() {
		return getTimestamp(System.currentTimeMillis());
	}

	/**
	 * 毫秒数转化成Tiemstamp时间类型
	 * 
	 * @param time
	 * @return
	 */
	public static java.sql.Timestamp getTimestamp(long time) {
		return new java.sql.Timestamp(time);
	}

	/**
	 * 抽取日期的年
	 */
	public static int getYear(java.util.Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.YEAR);
	}

	public static int getHour(java.util.Date date) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 日期格式化成字符串，默认采用格式为"yyyy-MM-dd"
	 * 
	 * @param date
	 *            日期
	 * @param pattern
	 *            格式字符串(null采用默认格式)
	 * @return
	 */
	public static String formatDate(Date date, String pattern) {
		if (date == null) {
			return "";
		}
		if (pattern == null) {
			pattern = "yyyy-MM-dd";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return (sdf.format(date));
	}

	/**
	 * 日期格式化成字符串，采用格式为"yyyy-MM-dd HH:mm:ss"
	 * 
	 * @param date
	 *            日期
	 * @return
	 */
	public static String formatDateTime(Date date) {
		return (formatDate(date, "yyyy-MM-dd HH:mm:ss"));
	}

	/**
	 * 当前日期格式化成字符串，采用格式为"yyyy-MM-dd HH:mm:ss"
	 * 
	 * @return
	 */
	public static String formatDateTime() {
		return (formatDate(now(), "yyyy-MM-dd HH:mm:ss"));
	}

	/**
	 * 当前日期格式化成字符串，采用格式为"yyyyMMddHHmmss"
	 *
	 * @return
	 */
	public static String formatDateTimeNormal() {
		return (formatDate(now(), "yyyyMMddHHmmss"));
	}

	/**
	 * 日期格式化成字符串，采用格式为"yyyy-MM-dd"
	 * 
	 * @param date
	 *            日期
	 * @return
	 */
	public static String formatDate(Date date) {
		return (formatDate(date, "yyyy-MM-dd"));
	}

	/**
	 * 日期格式化成字符串，采用格式为"yyyyMMdd"
	 * 
	 * @param date
	 *            日期
	 * @return
	 */
	public static String formatDate(Date date, boolean flag) {
		return (formatDate(date, "yyyyMMdd"));
	}

	/**
	 * 当前日期格式化成字符串，采用格式为"yyyy-MM-dd"
	 * 
	 * @return
	 */
	public static String formatDate() {
		return (formatDate(now(), "yyyy-MM-dd"));
	}

	/**
	 * 日期的时间部分转化成字符串，采用格式为"HH:mm:ss"
	 * 
	 * @param date
	 *            日期
	 * @return
	 */
	public static String formatTime(Date date) {
		return (formatDate(date, "HH:mm:ss"));
	}

	/**
	 * 当前时间部分转化成字符串，采用格式为"HH:mm:ss"
	 * 
	 * @return
	 */
	public static String formatTime() {
		return (formatDate(now(), "HH:mm:ss"));
	}

	/**
	 * 日期的时间部分转化成字符串，采用格式为"HHmmss"
	 * 
	 * @param date
	 *            日期
	 * @return
	 */
	public static String formatTime2() {
		return (formatDate(now(), "HHmmss"));
	}

	/**
	 * 返回当前时间
	 * 
	 * @return
	 */
	public static Date now() {
		return (new Date());
	}

	/**
	 * 字符串转化城市间，采用格式为"yyyy-MM-dd HH:mm:ss"
	 * 
	 * @param datetime
	 *            日期
	 * @return
	 */
	public static Date parseDateTime(String datetime) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if ((datetime == null) || (datetime.equals(""))) {
			return null;
		} else {
			try {
				return formatter.parse(datetime);
			} catch (ParseException e) {
				return parseDate(datetime);
			}
		}
	}

	/**
	 * 字符串转化日期，采用格式为"yyyy-MM-dd"
	 * 
	 * @param datetime
	 *            日期
	 * @return
	 */
	public static Date parseDate(String date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		if ((date == null) || (date.equals(""))) {
			return null;
		} else {
			try {
				return formatter.parse(date);
			} catch (ParseException e) {
				return null;
			}
		}
	}

	/**
	 * 截取时间部分，返回只有年月日的日期
	 * 
	 * @param datetime
	 * @return
	 */
	public static Date parseDate(Date datetime) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		if (datetime == null) {
			return null;
		} else {
			try {
				return formatter.parse(formatter.format(datetime));
			} catch (ParseException e) {
				return null;
			}
		}
	}

	/**
	 * 字符串转化日期，采用格式为"yyyyMMdd"
	 * 
	 * @param date
	 *            日期
	 * @return
	 */
	public static Date parseDateYYMMDD(String date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

		if ((date == null) || (date.equals(""))) {
			return null;
		} else {
			try {
				return formatter.parse(date);
			} catch (ParseException e) {
				return null;
			}
		}
	}

	/**
	 * 字符串按指定的格式转化日期
	 * 
	 * @param date
	 * @param format
	 *            例如yyyyMMdd
	 * @return
	 */
	public static Date parseDate(String date, String format) {
		if ((date == null) || (date.equals("")) || StringUtils.isEmpty(format)) {
			return null;
		} else {
			try {
				SimpleDateFormat formatter = new SimpleDateFormat(format);
				return formatter.parse(date);
			} catch (ParseException e) {
				return null;
			}
		}
	}

	/**
	 * 给时间加上或减去指定毫秒，秒，分，时，天、月或年等，返回变动后的时间
	 * 
	 * @param date
	 *            要加减前的时间，如果不传，则为当前日期
	 * @param field
	 *            时间域，有Calendar.MILLISECOND,Calendar.SECOND,Calendar.MINUTE,<br>
	 *            Calendar.HOUR,Calendar.DATE, Calendar.MONTH,Calendar.YEAR
	 * @param amount
	 *            按指定时间域加减的时间数量，正数为加，负数为减。
	 * @return 变动后的时间
	 */
	public static Date add(Date date, int field, int amount) {
		if (date == null) {
			date = new Date();
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(field, amount);

		return cal.getTime();
	}

	/**
	 * 日期增加毫秒数
	 * 
	 * @param date
	 * @param amount
	 * @return
	 */
	public static Date addMilliSecond(Date date, int amount) {
		return add(date, Calendar.MILLISECOND, amount);
	}

	/**
	 * 日期增加秒数部分
	 * 
	 * @param date
	 * @param amount
	 * @return
	 */
	public static Date addSecond(Date date, int amount) {
		return add(date, Calendar.SECOND, amount);
	}

	/**
	 * 日期增加分钟部分
	 * 
	 * @param date
	 * @param amount
	 * @return
	 */
	public static Date addMiunte(Date date, int amount) {
		return add(date, Calendar.MINUTE, amount);
	}

	/**
	 * 日期增加小时部分
	 * 
	 * @param date
	 * @param amount
	 * @return
	 */
	public static Date addHour(Date date, int amount) {
		return add(date, Calendar.HOUR, amount);
	}

	/**
	 * 日期增加天数部分
	 * 
	 * @param date
	 * @param amount
	 * @return
	 */
	public static Date addDay(Date date, int amount) {
		return add(date, Calendar.DATE, amount);
	}

	/**
	 * 日期增加月份部分
	 * 
	 * @param date
	 * @param amount
	 * @return
	 */
	public static Date addMonth(Date date, int amount) {
		return add(date, Calendar.MONTH, amount);
	}

	/**
	 * 日期增加年份部分
	 * 
	 * @param date
	 * @param amount
	 * @return
	 */
	public static Date addYear(Date date, int amount) {
		return add(date, Calendar.YEAR, amount);
	}

	/**
	 * 求两个日期相差多少分钟
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int intervalMin(Date startDate, Date endDate) {
		startDate = startDate != null ? startDate : new Date();
		endDate = endDate != null ? endDate : new Date();
		long start = startDate.getTime();
		long end = endDate.getTime();
		int intervalMin = (int) (Math.abs(end - start) / (1000 * 60));
		return intervalMin;
	}

	/**
	 * 
	 * @param date1
	 *            起始日期
	 * @param date2
	 *            结束日期
	 * @return int
	 * @throws ParseException
	 */
	public static int getMonthSpace(Date date1, Date date2) {
		int result = 0;
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(date1);
		c2.setTime(date2);
		result = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
		return result == 0 ? 1 : Math.abs(result);
	}
	/**
	 * 计算两个时间相差的月份
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int getTimeSpaceOfMonth(Date date1, Date date2) {
		Calendar bef = Calendar.getInstance();  
		Calendar aft = Calendar.getInstance();  
		bef.setTime(date1);  
		aft.setTime(date2);  
		int result = aft.get(Calendar.MONTH) - bef.get(Calendar.MONTH);  
		int month = (aft.get(Calendar.YEAR) - bef.get(Calendar.YEAR)) * 12;  
		return Math.abs(month + result);
	}
	
	public static void main(String[] args) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		String str1 = "2012-02";  
		String str2 = "2010-11";  

		System.out.println(DateTimeUtil.getTimeSpaceOfMonth(sdf.parse(str1), sdf.parse(str2)));
	}
}
