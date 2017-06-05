package com.pard.common.utils;

/**
 * Created by wawe on 17/6/3.
 */

import com.pard.common.utils.time.DateFormatter;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 * 日期时间工具类
 * <p>
 * Created by wawe on 17/5/30.
 */
public class DateTimeUtils extends org.joda.time.DateTimeUtils {

    /**
     * 年(yyyy)
     */
    public static final String YEAR = "yyyy";

    /**
     * 年-月(yyyy-MM)
     */
    public static final String YEAR_MONTH = "yyyy-MM";

    /**
     * 年-月-日(yyyy-MM-dd)
     */
    public static final String YEAR_MONTH_DAY = "yyyy-MM-dd";

    /**
     * 年月日(yyyyMMdd)
     */
    public static final String YEAR_MONTH_DAY_SIMPLE = "yyyyMMdd";

    /**
     * 年-月-日 小时(yyyy-MM-dd HH)
     */
    public static final String YEAR_MONTH_DAY_HOUR = "yyyy-MM-dd HH";

    /**
     * 年-月-日 小时(yyyy-MM-dd HH)中文输出
     */
    public static final String YEAR_MONTH_DAY_HOUR_CN = "yyyy年MM月dd日HH时";

    /**
     * 年-月-日 小时:分钟(yyyy-MM-dd HH:mm)
     */
    public static final String YEAR_MONTH_DAY_HOUR_MINUTE = "yyyy-MM-dd HH:mm";

    /**
     * 年-月-日 小时:分钟:秒钟(yyyy-MM-dd HH:mm:ss)
     */
    public static final String YEAR_MONTH_DAY_HOUR_MINUTE_SECOND = "yyyy-MM-dd HH:mm:ss";

    /**
     * 年月日小时分钟秒钟(yyyyMMddHHmmss)
     */
    public static final String YEAR_MONTH_DAY_HOUR_MINUTE_SECOND_SIMPLE = "yyyyMMddHHmmss";

    /**
     * 小时:分钟:秒钟(HH:mm:ss)
     */
    public static final String HOUR_MINUTE_SECOND = "HH:mm:ss";

    /**
     * 小时:分钟(HH:mm)
     */
    public static final String HOUR_MINUTE = "HH:mm";

    /**
     * 月.日(M.d)
     */
    public static final String MONTH_DAY = "M.d";

    /**
     * 一天的秒数
     */
    private static final int DAY_SECOND = 24 * 60 * 60;

    /**
     * 一小时的秒数
     */
    private static final int HOUR_SECOND = 60 * 60;

    /**
     * 一分钟的秒数
     */
    private static final int MINUTE_SECOND = 60;

    /**
     * 格式化日期时间
     *
     * @param date    Date对象
     * @param pattern 模式
     * @return 格式化后的日期时间字符串
     */
    public static String format(Date date, String pattern) {
        if (date == null)
            return "";
        return new DateTime(date).toString(pattern);
    }

    /**
     * 将字符串转换为日期类型
     *
     * @param dateString
     * @param pattern
     * @return
     */
    public static Date toDate(String dateString, String pattern) {
        try {
            DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(pattern);
            return dateTimeFormatter.parseDateTime(dateString).toDate();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据秒数获得X天X小时X分钟X秒字符串
     *
     * @param second 秒数
     * @return x天x小时x分钟x秒字符串
     */
    public static String getDayHourMinuteSecond(int second) {
        if (second == 0) {
            return "0秒";
        }
        StringBuilder sb = new StringBuilder();
        int days = second / DAY_SECOND;
        if (days > 0) {
            sb.append(days).append("天");
            second -= days * DAY_SECOND;
        }

        int hours = second / HOUR_SECOND;
        if (hours > 0) {
            sb.append(hours).append("小时");
            second -= hours * HOUR_SECOND;
        }

        int minutes = second / MINUTE_SECOND;
        if (minutes > 0) {
            sb.append(minutes).append("分钟");
            second -= minutes * MINUTE_SECOND;
        }

        if (second > 0) {
            sb.append(second).append("秒");
        }
        return sb.toString();
    }

    /**
     * 根据秒数获得X天X小时X分钟字符串
     *
     * @param second 秒数
     * @return x天x小时x分钟字符串
     */
    public static String getDayHourMinute(int second) {
        if (second == 0) {
            return "0分钟";
        }
        StringBuilder sb = new StringBuilder();
        int days = second / DAY_SECOND;
        if (days > 0) {
            sb.append(days).append("天");
            second -= days * DAY_SECOND;
        }

        int hours = second / HOUR_SECOND;
        if (hours > 0) {
            sb.append(hours).append("小时");
            second -= hours * HOUR_SECOND;
        }

        int minutes = second / MINUTE_SECOND;
        if (minutes > 0) {
            sb.append(minutes).append("分钟");
        }
        return sb.toString();
    }

    /**
     * 获取只含有年月日的DateTime对象
     *
     * @param dateTime
     * @return
     */
    public static DateTime getDateOnly(DateTime dateTime) {
        return new DateTime(dateTime.toString(YEAR_MONTH_DAY));
    }

    /**
     * 获取当前周的周一和下周一
     *
     * @return 日期数组（索引0为周一，索引1为下周一）
     */
    public static Date[] getMondayAndNextMonday() {
        DateTime dateTime = getDateOnly(new DateTime());
        DateTime monday = dateTime.dayOfWeek().withMinimumValue();
        DateTime nextMonday = monday.plusDays(7);
        return new Date[]{monday.toDate(), nextMonday.toDate()};
    }

    /**
     * 获取指定时间的周一和周日
     *
     * @param dateTime DateTime对象
     * @return 日期数组(索引0 为周一， 索引1为周日)
     */
    public static Date[] getMondayAndSunday(DateTime dateTime) {
        dateTime = getDateOnly(dateTime);
        DateTime monday = dateTime.dayOfWeek().withMinimumValue();
        DateTime sunday = monday.plusDays(6);
        return new Date[]{monday.toDate(), sunday.toDate()};
    }

    /**
     * 获取当前时间相比的天数差(正数为大于天数，负数为小于天数， 零为同一天)
     *
     * @param date Date对象
     * @return 和当前时间相比的天数差
     */
    public static int compareDaysWithNow(Date date) {
        return Days.daysBetween(new DateTime(), new DateTime(date)).getDays();
    }

    /**
     * 获取和今天相比的天数差(正数为大于天数，负数为小于天数， 零为同一天)
     *
     * @param date Date对象
     * @return 和今天相比的天数差
     */
    public static int compareDaysWithToday(Date date) {
        DateTime today = new DateTime();
        today = new DateTime(today.getYear(), today.getMonthOfYear(), today.getDayOfMonth(), 0, 0, 0, 0);
        DateTime compareDay = new DateTime(date);
        compareDay = new DateTime(compareDay.getYear(), compareDay.getMonthOfYear(), compareDay.getDayOfMonth(), 0, 0, 0, 0);
        return Days.daysBetween(today, compareDay).getDays();
    }

    /**
     * 比较时间a到时间b的天数差(正数为大于天数，负数为小于天数， 零为同一天)
     *
     * @param a Date对象
     * @param b Date对象
     * @return 天数差
     */
    public static int compareDate(Date a, Date b) {
        DateTime dateB = new DateTime(b);
        dateB = new DateTime(dateB.getYear(), dateB.getMonthOfYear(), dateB.getDayOfMonth(), 0, 0, 0, 0);
        DateTime dateA = new DateTime(a);
        dateA = new DateTime(dateA.getYear(), dateA.getMonthOfYear(), dateA.getDayOfMonth(), 0, 0, 0, 0);
        return Days.daysBetween(dateB, dateA).getDays();
    }

    /**
     * 比较两个时间是否相等（忽略毫秒）
     *
     * @param date        Date对象
     * @param compareDate 比较Date对象
     * @return 是否相等
     */
    public static boolean compareDateIgnoreMillisecond(Date date, Date compareDate) {
        if (date == null && compareDate == null) {
            return true;
        } else if (date == null && compareDate != null) {
            return false;
        } else if (date != null && compareDate == null) {
            return false;
        }

        return date.getTime() / 1000 == compareDate.getTime() / 1000;
    }

    /**
     * 根据秒数获取天数
     *
     * @param second 秒数
     * @return 天数
     */
    public static int getDay(int second) {
        return second / DAY_SECOND;
    }

    /**
     * 获取和今天相比的日期字符串
     *
     * @param date Date对象
     * @return 和今天相比的日期字符串
     */
    public static String getCompareWithTodayDateString(Date date) {
        int days = Math.abs(compareDaysWithToday(date));
        if (days == 0) {
            return "今天";
        } else if (days == 1) {
            return "昨天";
        } else if (days == 2) {
            return "2天前";
        } else if (days == 3) {
            return "3天前";
        } else if (days == 4) {
            return "4天前";
        } else if (days == 5) {
            return "5天前";
        } else if (days == 6) {
            return "6天前";
        } else if (days > 6 && days <= 14) {
            return "1周前";
        } else if (days > 14 && days <= 21) {
            return "2周前";
        } else if (days > 21 && days <= 30) {
            return "3周前";
        } else if (days > 30 && days <= 365) {
            return "1月前";
        } else if (days > 365 && days <= 365 * 3) {
            return "1年前";
        } else if (days > 365 * 3) {
            return "3年前";
        }
        return "";
    }

    /**
     * 比较两个时间相差分钟数
     *
     * @param date
     * @param compareDate
     * @return
     */
    public static int compareMinutes(Date date, Date compareDate) {
        return (int) (date.getTime() - compareDate.getTime()) / 60000;
    }

    public static long compareMinsecond(Date date, Date compareDate) {
        return date.getTime() - compareDate.getTime();
    }

    /**
     * 获取日期是月的第几天
     *
     * @param date
     * @return
     */
    public static int getDayOfMonth(Date date) {
        DateTime dateTime = new DateTime(date);
        return dateTime.getDayOfMonth();
    }

    /**
     * 获取指定日期所在的月后多少天
     *
     * @param date
     * @return
     */
    public static int getDateOfMonth(Date date) {
        DateTime dateTime = new DateTime(date);
        return dateTime.dayOfMonth().getMaximumValue();
    }

    /**
     * 判断指定时间到现在的年数
     *
     * @param date
     * @return
     */
    public static int compareYear(Date date) {
        DateTime btd = new DateTime(date);
        DateTime nowDate = new DateTime();
        int year = 0;
        if (nowDate.getMonthOfYear() > btd.getMonthOfYear()) {
            year = nowDate.getYear() - btd.getYear();
        } else if (nowDate.getMonthOfYear() < btd.getMonthOfYear()) {
            year = nowDate.getYear() - btd.getYear() - 1;
        } else if (nowDate.getMonthOfYear() == btd.getMonthOfYear()) {
            if (nowDate.getDayOfMonth() >= btd.getDayOfMonth()) {
                year = nowDate.getYear() - btd.getYear();
            } else {
                year = nowDate.getYear() - btd.getYear() - 1;
            }
        }
        return year;
    }

    /**
     * 判断2个时间的时间差，返回字符串形式
     *
     * @param date
     * @param date2
     * @return
     */
    public static String compareDaysWithDate(Date date, Date date2) {
        StringBuilder sb = new StringBuilder();
        int minutes = (int) Math.abs((date.getTime() - date2.getTime()) / 60000);
        if (minutes / 60 > 0 && minutes / 60 / 24 <= 0) {
            sb.append(minutes / 60).append("小时");
        }
        if (minutes / 60 / 24 > 0) {
            sb.append(minutes / 60 / 24).append("天");
            sb.append(minutes / 60 % 24).append("小时");
        }
        return sb.toString();
    }

    /**
     * 身份证号转生日
     *
     * @param identityCard 身份证
     * @return 生日
     */
    public static Date identityCard2Date(String identityCard) {
        try {
            String dateStr;
            if (identityCard.length() == 18) {
                dateStr = identityCard.substring(6, 14);// 截取18位身份证身份证中生日部分
                return DateFormatter.fromString(dateStr, YEAR_MONTH_DAY_SIMPLE);
            }
            if (identityCard.length() == 15) {
                dateStr = identityCard.substring(6, 12);// 截取15位身份证中生日部分
                return DateFormatter.fromString(dateStr, "yyMMdd");
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 验证是否为日期类型
     *
     * @param str
     * @return
     */
    public static boolean validDate(String str) {
        try {
            Date date = DateFormatter.fromString(str);
            return date != null;
        } catch (Exception e) {
            return false;
        }
    }
}

