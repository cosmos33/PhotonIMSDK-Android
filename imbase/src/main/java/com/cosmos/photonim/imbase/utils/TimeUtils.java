package com.cosmos.photonim.imbase.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {
    private static final String TIME_FORMAT_DAY = "yyyy/MM/dd";
    private static final String TIME_FORMAT_HOUR = "HH:mm";
    private static final int YESTERDY = -1;
    private static ThreadLocal<SimpleDateFormat> dateLocal = new ThreadLocal<>();
    private static ThreadLocal<SimpleDateFormat> hourLocal = new ThreadLocal<>();

    //获取聊天中的时间
    public static String getTimeContent(long curData, long preData) {
        if (preData == 0) {
            return TimeUtils.getTimeFormat(curData);
        }
        if (curData - preData >= 1000 * 60 * 5) {
            return TimeUtils.getTimeFormat(curData);
        }
        return null;
    }

    public static String getTimeFormat(long time) {
        if (isToday(time)) {
            Date d = new Date(time);
            if (d.getHours() < 12) {
                return String.format("上午%s", getHourFormat().format(d));
            } else {
                return String.format("下午%s", getHourFormat().format(d));
            }

        } else if (isYesterday(time)) {
            return "昨天";
        } else {
            return getDateFormat().format(new Date(time));
        }
    }

    //判断选择的日期是否是今天
    public static boolean isToday(long time) {
        return isThisTime(time, TIME_FORMAT_DAY);
    }

    private static boolean isThisTime(long time, String pattern) {
        Date date = new Date(time);
        String param = getDateFormat().format(date);//参数时间
        String now = getDateFormat().format(new Date());//当前时间
        if (param.equals(now)) {
            return true;
        }
        return false;
    }

    /**
     * 读取日期的格式
     */
    public static SimpleDateFormat getDateFormat() {
        if (null == dateLocal.get()) {
            dateLocal.set(new SimpleDateFormat(TIME_FORMAT_DAY, Locale.CHINA));
        }
        return dateLocal.get();
    }

    public static SimpleDateFormat getHourFormat() {
        if (null == hourLocal.get()) {
            hourLocal.set(new SimpleDateFormat(TIME_FORMAT_HOUR, Locale.CHINA));
        }
        return hourLocal.get();
    }


    public static boolean isYesterday(long time) {
        String format = getDateFormat().format(new Date(time));

        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);

        Calendar cal = Calendar.getInstance();
        Date date = null;
        try {
            date = getDateFormat().parse(format);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        cal.setTime(date);

        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == YESTERDY) {
                return true;
            }
        }
        return false;
    }
}
