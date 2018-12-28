package com.tysoft.common;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
/**
 * <p>Title: 综合养护管理系统(GMMS)</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2012</p>
 *
 * <p>Company: 厦门市路桥信息工程有限公司</p>
 *
 * @author 
 * @version 2.0
 */
public class CalendarUtil {

    public static final String FORMAT_DATETIME = "yyyy.MM.dd HH:mm";
    public static final String YY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String HH_MM = "HH:mm";

    public static final String FORMAT_DATE = "yyyy.MM.dd";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";

    public static final String OTHER_FORMAT_DATE = "yyyy.M.d";

    public static final SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd");

    public static final DecimalFormat dd = new DecimalFormat("0.00");
    
    public static int getYear(Date date) {
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    // 取得当前年份
    public static int getNowYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR);
    }

    // 取得当前月份
    public static int getNowMonth() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.MONTH) + 1;
    }

    // 取得当前是一个月中的第几天
    public static int getNowDay() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    // 取得当前的总星期数
    public static int getCountWeek(int taskYear) {
        /*
         * Calendar cal = Calendar.getInstance(); cal.set(taskYear,11,31,23,59);
         * System.out.println(cal.getTime()); cal.get(Calendar.WEEK_OF_YEAR);
         */
        return 53;
    }

    // 取得当前年的总天数
    public static int getCountDay(int taskYear) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, taskYear);
        cal.set(Calendar.MONTH, 11);
        cal.set(Calendar.DAY_OF_MONTH, 31);
        return cal.get(Calendar.DAY_OF_YEAR);
    }

    // 取得某个时间所在月份
    public static int setDateMonth(Date date) {
        Calendar datex = Calendar.getInstance();
        datex.setTime(date);
        return datex.get(Calendar.MONTH) + 1;
    }

    // 取得某个时间所在月份
    public static String getDateWeek(Date date) {
        Calendar datex = Calendar.getInstance();
        datex.setTime(date);
        Map week = new HashMap();
        week.put(new Integer(1), "星期天");
        week.put(new Integer(2), "星期一");
        week.put(new Integer(3), "星期二");
        week.put(new Integer(4), "星期三");
        week.put(new Integer(5), "星期四");
        week.put(new Integer(6), "星期五");
        week.put(new Integer(7), "星期六");
        return (week.get(new Integer(datex.get(Calendar.DAY_OF_WEEK))))
                .toString();
    }

    // 取得某个时间是一个月的第几天
    public static int setDateDay(Date date) {
        Calendar datex = Calendar.getInstance();
        datex.setTime(date);
        return datex.get(Calendar.DAY_OF_MONTH);
    }

    public static int setDateYear(Date date) {
        Calendar datex = Calendar.getInstance();
        datex.setTime(date);
        return datex.get(Calendar.YEAR);
    }

    public static String getFormat_Date(Date date) {
        return df.format(date);
    }

    public static String getDateBetween(String datecol, Date fromdate,
            Date todate) {
        StringBuffer s = new StringBuffer(100);
        // add from date;
        s.append("((1=1)");
        if (fromdate == null) {
            s.append("or");
        } else {
            s.append("and");
        }
        s.append("(" + datecol + ">=?))");
        // and;
        s.append("and");
        // add to date;
        s.append("((1=1)");
        if (todate == null) {
            s.append("or");
        } else {
            s.append("and");
        }
        s.append("(" + datecol + "< (?)))");
        return s.toString();
    }

    public static String getAllFiel(String fiel, String type) {
        StringBuffer s = new StringBuffer(100);
        // add from date;
        s.append("((1=1)");
        if (type.equals("all")) {
            s.append("or");
        } else {
            s.append("and");
        }
        s.append("(" + fiel + "=?))");
        return s.toString();
    }

    public static String getObjectBetween(String col, String col2, Float from,
            Float to) {
        StringBuffer s = new StringBuffer(100);
        // add from date;
        s.append("((1=1)");
        if (from.intValue() == 0) {
            s.append("or");
        } else {
            s.append("and");
        }
        s.append("(" + col + "<=?))");
        // and;
        s.append("and");
        // add to date;
        s.append("((1=1)");
        if (to.intValue() == 0) {
            s.append("or");
        } else {
            s.append("and");
        }
        s.append("(" + col2 + ">=?))");
        return s.toString();
    }

    // 取得本周最后一天
    public static String getLastWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, calendar
                .getActualMaximum(Calendar.DAY_OF_WEEK));
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        return df.format(calendar.getTime());
    }

    // 取得本月最后一天
    public static String getLastMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar
                .getActualMaximum(Calendar.DAY_OF_MONTH));
        return df.format(calendar.getTime());
    }

    // 取得本季度最后一天
    public static String getLastSeason() {
        String thisYear = String.valueOf(new Integer(getNowYear()));
        String datex = "";
        if (getNowMonth() <= 3) {
            datex = thisYear + ".03.31";
        } else if (4 <= getNowMonth() && getNowMonth() <= 6) {
            datex = thisYear + ".06.30";
        } else if (7 <= getNowMonth() && getNowMonth() <= 9) {
            datex = thisYear + ".09.30";
        } else {
            datex = thisYear + ".12.31";
        }
        return datex;
    }

    // 取得本年最后一天
    public static String getLastYearDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar
                .getActualMaximum(Calendar.DAY_OF_YEAR));
        return df.format(calendar.getTime());
    }

    // 取得本月最后一天
    public static String getLastMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, calendar
                .getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        return df.format(calendar.getTime());
    }

    public static Date getLastMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar
                .getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        return calendar.getTime();
    }

    public static Date getFirstMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar
                .getActualMinimum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        return calendar.getTime();
    }

    public static Date getDLastMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, calendar
                .getActualMaximum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

    // 取得本月第一天
    public static Date getDStartMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, calendar
                .getActualMinimum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
    // 取得本月最后一天
    public static Date getMonthLastDay(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, calendar
                .getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    // 取得本月第一天
    public static String getStartMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, calendar
                .getActualMinimum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        return df.format(calendar.getTime());
    }

    // 取得某一年月的有多少天
    public static int getMaxDay(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

    }

    public static String getFloat(float f) {
        return dd.format(f);
    }

    public static void main(String[] arg) {
        Float t1000 = new Float(1009);
        long t = t1000.longValue();
        String tem = new Long(t).toString();
        System.out.println(tem.substring(tem.length() - 3, tem.length()));
    }

    public static String formatValue(double value) {
        StringBuffer results = new StringBuffer();
        if (String.valueOf(((long) value)).length() > 6) {
            String temvalue = String.valueOf(((long) value));
            value = value
                    - padld(temvalue.substring(0, temvalue.length() - 6),
                            temvalue.length());
            results.append(temvalue.substring(0, temvalue.length() - 6));
        }
        results.append("K");
        results.append((long) value / 1000);
        results.append("+");
        results.append(padl(String.valueOf(((long) value) % 1000).trim(), "0",
                3));

        long decimal = ((long) (value * 1000)) % 1000;
        if (decimal > 0) {
            results.append(".");
            results.append(padl(String.valueOf(decimal).trim(), "0", 3));
        }
        return results.toString();
    }

    public static String padl(String value, String r, int length) {
        String result = value;
        for (int i = value.length(); i < length; i++) {
            result = r + result;
        }
        return result;
    }

    public static double padld(String value, int length) {
        String result = value;
        // System.out.println(length);
        for (int i = 0; i < 6; i++) {
            result = result + "0";
        }
        // System.out.println(result);
        return new Double(result).doubleValue();
    }

    public static String nullStrToEmpty(String str) {
        return str == null ? "" : str;
    }

    public static String emptyToNull(String str) {
        return (str == null || str.trim().equals("")) ? null : str;
    }

    public static List clearStr(String all, String dele, String sign) {
        List clearedStr = new ArrayList();
        List allList = clearStr(all, sign);
        List deleList = clearStr(dele, sign);
        if (allList.isEmpty())
            return clearedStr;
        if (deleList.isEmpty())
            return allList;
        Map map = new HashMap();
        Iterator it = allList.iterator();
        while (it.hasNext()) {
            String v = (String) it.next();
            map.put(v, v);
        }
        Iterator itDel = deleList.iterator();
        while (itDel.hasNext()) {
            String v = (String) itDel.next();
            String allv = (String) map.get(v);
            if (allv != null)
                map.remove(allv);
        }
        clearedStr.add(map.values());
        return clearedStr;
    }

    public static List clearStr(String str, String sign) {
        List clearedStr = new ArrayList();
        String[] allArray = StringUtils.split(str, sign);
        if (allArray == null)
            return clearedStr;
        for (int i = 0; i < allArray.length; i++) {
            if (!allArray[i].equals(""))
                clearedStr.add(allArray[i]);
        }
        return clearedStr;

    }

    public static String getIdsStr(List objects) {
        String str = null;
        if (objects == null || objects.size() == 0)
            return str;
        Object[] objArray = objects.toArray();
        for (int i = 0; i < objArray.length; i++) {
            String temp = objArray[i].toString();
            if (i < objArray.length - 1)
                str += temp + ",";
            else
                str += temp;

        }
        return str;
    }

    /**
	 * 获取days天以后的日期
	 * @param date 当前日期
	 * @param days 天数
	 * @return
	 */
    public static Date dateAfterDays(Date date,int days){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, days);
		return calendar.getTime();
	}
	
	/**
	 * 获取两个日期之间间隔的天数
	 * @param date1
	 * @param date2
	 * @return 
	 */
	public static int daysBetween(Date date1,Date date2) {
		int days = (int) ((date1.getTime() - date2.getTime())/(24*60*60*1000));
		return Math.abs(days);
	}
	
	/**
	 * 获取两个日期之间间隔的分钟数
	 * @param date1
	 * @param date2
	 * @return 
	 */
	public static int daysBetweenMinutes(Date date1,Date date2) {
		int days = (int) ((date1.getTime() - date2.getTime())/(60*1000));
		return Math.abs(days);
	}
	
	/**
	 * years年以后的今天
	 * @param date 当前日期
	 * @param years 年数
	 * @return
	 */
	public static Date dateAfterYears(Date date,int years){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.YEAR, years);
		return calendar.getTime();
	}
	
	/**
	 * seasons个季节以后的今天
	 * @param date 当前日期
	 * @param seasons 季节个数
	 * @return
	 */
	public static Date dateAfterSeasons(Date date,int seasons) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, seasons*3);
		return calendar.getTime();
	}
	
	/**
	 * months个月之后的今天
	 * @param date 当前日期
	 * @param months 月数
	 * @return
	 */
	public static Date dateAfterMonths(Date date,int months) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, months);
		return calendar.getTime();
	}
	
	/**
	 * 只获取日期，不要时分秒
	 * @param date 日期
	 * @return
	 */
	public static Date getDateOnly(Date date) {		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	/**
	 * 日期之间相隔月数，如2016-03-05与2016-02-06相隔1个月
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int getBetweenMonth(Date date1,Date date2) {		
        Calendar c = Calendar.getInstance();
        c.setTime(date1);
        int year1 = c.get(Calendar.YEAR);
        int month1 = c.get(Calendar.MONTH);
        int day1 = c.get(Calendar.DATE);
         
        c.setTime(date2);
       
        int year2 = c.get(Calendar.YEAR);
        int month2 = c.get(Calendar.MONTH);
        int day2 = c.get(Calendar.DATE);
         
        int result;
        if(year1 == year2) {
            result = month1 - month2;
        } else {
            result = 12*(year1 - year2) + month1 - month2;
        }
        if((day1 - day2) < -1){
        	result = result - 1;
        }
        if(result < 0) result = -result;
		return result;
	}
	
	/**
	 * 获取某个月的周数（从周一开始，周日结束）
	 * 
	 * @param year 年份
	 * @param month 月份
	 * @return 周数
	 */
	public static int getWeeksOfMonth(String year, String month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Integer.parseInt(year), Integer.parseInt(month) - 1, 1, 0, 0, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		// 本月第一天
		Date curMonthFirstDay = calendar.getTime();

		// 本月最后一天
		calendar.add(Calendar.MONTH, 1);
		calendar.add(Calendar.DATE, -1);
		Date curMonthLastDay = calendar.getTime();

		calendar.setTime(curMonthFirstDay);
		calendar.add(Calendar.DATE, -1); // 因为是周一开始，跟世界标准的周日开始不同，所以要倒退一天来算
		int firstWeek = calendar.get(Calendar.WEEK_OF_YEAR);

		calendar.setTime(curMonthLastDay);
		calendar.add(Calendar.DATE, -1); // 因为是周一开始，跟世界标准的周日开始不同，所以要倒退一天来算
		int lastWeek = calendar.get(Calendar.WEEK_OF_YEAR);

		if (firstWeek > lastWeek) {
			return lastWeek + 1; // 一般是碰上元旦的时候，才会走这个分支
		}

		return lastWeek - firstWeek + 1;
	}
	
	/**
	 * 获取某个月每周的起止时间（周一开始，周日结束）
	 * @param year 年份
	 * @param month 月份
	 * @return
	 */
	public static String[] getWeeksPeriodOfMonth(String year, String month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Integer.parseInt(year), Integer.parseInt(month) - 1, 1, 0, 0, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		// 本月第一天，真实第一天
		Date curMonthFirstDay = calendar.getTime();

		// 本月最后一天，真实最后一天
		calendar.add(Calendar.MONTH, 1);
		calendar.add(Calendar.DATE, -1);
		Date curMonthLastDay = calendar.getTime();

		calendar.setTime(curMonthFirstDay);
		calendar.add(Calendar.DATE, -1); // 因为是周一开始，跟世界标准的周日开始不同，所以要倒退一天来算
		int firstWeek = calendar.get(Calendar.WEEK_OF_YEAR);

		calendar.setTime(curMonthLastDay);
		calendar.add(Calendar.DATE, -1); // 因为是周一开始，跟世界标准的周日开始不同，所以要倒退一天来算
		int lastWeek = calendar.get(Calendar.WEEK_OF_YEAR);

		int size;
		if (firstWeek > lastWeek) {
			size = lastWeek + 1; // 一般是碰上元旦的时候，才会走这个分支
			firstWeek = 0;
		} else {
			size = lastWeek - firstWeek + 1;
		}

		calendar.setTime(curMonthFirstDay);
		SimpleDateFormat sdf = new SimpleDateFormat("dd");
		int tmpDayOfWeek;
		String tmpStartStr, tmpEndStr;
		String[] resultArr = new String[size];
		for (int i = 0; i < size; i++) {
			tmpStartStr = null;
			tmpEndStr = null;
			tmpDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
			if (i == 0) {
				tmpStartStr = sdf.format(calendar.getTime());
				if (tmpDayOfWeek == 1) {
					// 周日
					tmpEndStr = null;
				} else {
					calendar.add(Calendar.DATE, 8 - tmpDayOfWeek);
					tmpEndStr = sdf.format(calendar.getTime());
				}
			} else {
				calendar.add(Calendar.DATE, 1);
				tmpStartStr = sdf.format(calendar.getTime());
				
				calendar.add(Calendar.DATE, 7 - tmpDayOfWeek);
				if (calendar.getTime().after(curMonthLastDay)) {
					calendar.setTime(curMonthLastDay);
				}
				
				tmpEndStr = sdf.format(calendar.getTime());
				
				if (tmpEndStr.equals(tmpStartStr)) {
					tmpEndStr = null;
				}
			}

			if (tmpStartStr != null && tmpEndStr == null) {
				resultArr[i] = tmpStartStr;
			} else {
				resultArr[i] = tmpStartStr + "~" + tmpEndStr;
			}
		}

		return resultArr;
	}
}
