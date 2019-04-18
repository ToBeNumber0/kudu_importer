package com.dhph.bigdata.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

@Slf4j
public class DateUtils {

    /**
     * 英文简写（默认）如：2010-12
     */
    public static String PATTERN_SHORTER = "yyyy-MM";
    /**
     * 英文简写（默认）如：2010-12-01
     */
    public static String PATTERN_SHORT = "yyyy-MM-dd";
    /**
     * 英文简写（默认）如：20101201
     */
    public static String PATTERN_SHORT_EXT = "yyyyMMdd";
    /**
     * 英文简写（默认）如：2010.12.01
     */
    public static String PATTERN_SHORT_POINT = "yyyy.MM.dd";
    /**
     * 英文全称如：2010-12-01 23:15:06
     */
    public static String PATTERN_LONG = "yyyy-MM-dd HH:mm:ss";

    /**
     * 英文全称如：2010-12-01 23:15
     */
    public static String PATTERN_WITHOUT_SECOND = "yyyy-MM-dd HH:mm";

    /**
     * 英文全称如：2010/12/01 23:15
     */
    public static String PATTERN_WITHOUT_SECOND_SLANT = "yyyy/MM/dd HH:mm";

    /**
     * 英文全称如：2010/12/01 23:15:15
     */
    public static String PATTERN_WITHOUT_LONG = "yyyy/MM/dd HH:mm:ss";

    /**
     * 英文全称如：20101201231506
     */
    public static String PATTERN_LONG_UNMARK = "yyyyMMddHHmmss";

    public static String PATTERN_LONG_UNMARK_MM = "yyyyMMddHHmm";

    /**
     * 精确到毫秒的完整时间 如：yyyy-MM-dd HH:mm:ss.S
     */
    public static String PATTERN_FULL = "yyyy-MM-dd HH:mm:ss.S";

    /**
     * 格式 2015/07/07
     */
    public static String PATTERN_FULL_NEW = "yyyy/MM/dd";

    /**
     * 中文简写如：2010年12月01日
     */
    public static String PATTERN_SHORT_CN = "yyyy年MM月dd日";

    /**
     * 中文全称如：2010年12月01日23时15分06秒
     */

    public static String PATTERN_LONG_CN = "yyyy年MM月dd日HH时mm分ss秒";

    /**
     * 精确到毫秒的完整中文时间
     */
    public static String PATTERN_FULL_CN = "yyyy年MM月dd日HH时mm分ss秒SSS毫秒";
    /**
     * corn格式转换
     */
    public static String PATTERN_CORN = "ss mm HH dd MM ?";

    public static String PATTERN_LONG_CN_NUM = "yyyy年MM月dd日 HH:mm:ss";

    /**
     * 获取YYYY格式
     *
     * @return
     */
    public static String getYear() {
        return formatDate(new Date(), "yyyy");
    }

    /**
     * 获取当前日期的月份
     *
     * @return
     */
    public static String getMonth() {
        return format(new Date(), "MM");
    }

    /**
     * 获取指定日期的月份
     *
     * @param date
     * @return
     */
    public static String getMonth(Date date) {
        return format(date, "MM");
    }

    /**
     * 获取当前日期的天
     *
     * @return
     */
    public static String getOnlyDay() {
        return format(new Date(), "dd");
    }

    /**
     * 获取指定日期的天
     *
     * @param date
     * @return
     */
    public static String getOnlyDay(Date date) {
        return format(date, "dd");
    }

    /**
     * 获取YYYY格式
     *
     * @return
     */
    public static String getYear(Date date) {
        return formatDate(date, "yyyy");
    }

    /**
     * 获取YYYY-MM-DD格式
     *
     * @return
     */
    public static String getDay() {
        return formatDate(new Date(), "yyyy-MM-dd");
    }

    /**
     * 获取YYYY-MM-DD格式
     *
     * @return
     */
    public static String getDay(Date date) {
        return formatDate(date, "yyyy-MM-dd");
    }

    /**
     * 获取YYYYMMDD格式
     *
     * @return
     */
    public static String getDays() {
        return formatDate(new Date(), "yyyyMMdd");
    }

    /**
     * 获取HHMMSS格式
     *
     * @return
     */
    public static String getHms() {
        return formatDate(new Date(), "HHmmss");
    }

    /**
     * 获取YYYYMMDD格式
     *
     * @return
     */
    public static String getDays(Date date) {
        return formatDate(date, "yyyyMMdd");
    }

    /**
     * 获取YYYY-MM-DD HH:mm:ss格式
     *
     * @return
     */
    public static String getTime() {
        return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 获取YYYY-MM-DD HH:mm:ss.SSS格式
     *
     * @return
     */
    public static String getMsTime() {
        return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss.SSS");
    }

    /**
     * 获取YYYYMMDDHHmmss格式
     *
     * @return
     */
    public static String getAllTime() {
        return formatDate(new Date(), "yyyyMMddHHmmss");
    }

    public static String getAllTimeByDate(Date date) {
        return formatDate(date, "yyyyMMddHHmmss");
    }

    public static String getMTime() {
        return formatDate(new Date(), PATTERN_LONG_UNMARK_MM);
    }

    /**
     * 获取YYYY-MM-DD HH:mm:ss格式
     *
     * @return
     */
    public static String getTime(Date date) {
        return formatDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 获取YYYY-MM-DD HH:mm:ss格式 判断null
     *
     * @param date
     * @return
     */
    public static String getTimeIsNull(Date date) {
        if (date == null) {
            return "";
        }
        return formatDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String formatDate(Date date, String pattern) {
        String formatDate = null;
        if (StringUtils.isNotBlank(pattern)) {
            formatDate = DateFormatUtils.format(date, pattern);
        } else {
            formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
        }
        return formatDate;
    }

    /**
     * @param s
     * @param e
     * @return boolean
     * @throws
     * @Title: compareDate
     * @Description:(日期比较，如果s>=e 返回true 否则返回false)
     * @author luguosui
     */
    public static boolean compareDate(String s, String e) {
        if (parseDate(s) == null || parseDate(e) == null) {
            return false;
        }
        return parseDate(s).getTime() >= parseDate(e).getTime();
    }

    /**
     * @param s 格式 yyyy-MM-dd HH:mm:ss (String)
     * @param e 格式 yyyy-MM-dd HH:mm:ss (String)
     * @return boolean
     * @throws
     * @Title: compareTime
     * @Description:(日期比较，如果s>=e 返回true 否则返回false)
     * @author wulizheng
     */
    public static boolean compareTime(String s, String e) {
        if (parseTime(s) == null || parseTime(e) == null) {
            return false;
        }
        return parseTime(s).getTime() >= parseTime(e).getTime();
    }

    /**
     * @param s
     * @param e
     * @return boolean
     * @throws
     * @Title: compareDate
     * @Description:(日期比较，如果s>e 返回true 否则返回false)
     * @author luguosui
     */
    public static boolean compareTime(Date s, Date e, boolean equals) {
        if (s == null || e == null) {
            return false;
        }
        if (equals) {
            return s.getTime() >= e.getTime();
        } else {
            return s.getTime() > e.getTime();
        }
    }

    /**
     * 格式化日期
     *
     * @return
     */
    public static Date parseDate(String date) {
        return parse(date, "yyyy-MM-dd");
    }

    /**
     * 格式化日期
     *
     * @return
     */
    public static Date parseDateShort(String date) {
        return parse(date, "yyyyMMdd");
    }

    /**
     * 格式化日期
     *
     * @return
     */
    public static Date parseTime(String date) {
        return parse(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 格式化日期
     *
     * @return
     */
    public static Date parse(String date, String pattern) {
        try {
            return org.apache.commons.lang3.time.DateUtils.parseDate(date, pattern);
        } catch (ParseException e) {
            log.error("", e);
            return null;
        }
    }

    /**
     * 格式化日期
     *
     * @return
     */
    public static String formatDefault(Date date) {
        if (null == date) {
            return null;
        }
        return DateFormatUtils.format(date, PATTERN_LONG);
    }

    public static String formatShortExt(Date date) {
        if (null == date) {
            return null;
        }
        return DateFormatUtils.format(date, PATTERN_SHORT_EXT);
    }

    /**
     * 格式化日期
     *
     * @return
     */
    public static String formatShort(Date date) {
        if (null == date) {
            return null;
        }
        return DateFormatUtils.format(date, PATTERN_SHORT);
    }

    /**
     * 格式化日期
     *
     * @return
     */
    public static String format(Date date, String pattern) {
        if (null == date || StringUtils.isBlank(pattern)) {
            return null;
        }
        return DateFormatUtils.format(date, pattern);
    }

    /**
     * 把日期转换为Timestamp
     *
     * @param date
     * @return
     */
    public static Timestamp format(Date date) {
        return new Timestamp(date.getTime());
    }

    /**
     * 校验日期是否合法
     *
     * @return
     */
    public static boolean isValidDate(String s) {
        return parse(s, "yyyy-MM-dd HH:mm:ss") != null;
    }

    /**
     * 校验日期是否合法
     *
     * @return
     */
    public static boolean isValidDate(String s, String pattern) {
        return parse(s, pattern) != null;
    }

    public static int getDiffYear(String startTime, String endTime) {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        try {
            int years = (int) (((fmt.parse(endTime).getTime() - fmt.parse(
                    startTime).getTime()) / (1000 * 60 * 60 * 24)) / 365);
            return years;
        } catch (Exception e) {
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            return 0;
        }
    }

    /**
     * <li>功能描述：时间相减得到天数
     *
     * @param beginDateStr
     * @param endDateStr
     * @return long
     * @author Administrator
     */
    public static long getDaySub(String beginDateStr, String endDateStr) {
        long day = 0;
        SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd");
        Date beginDate = null;
        Date endDate = null;

        try {
            beginDate = format.parse(beginDateStr);
            endDate = format.parse(endDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        day = (endDate.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000);
        // System.out.println("相隔的天数="+day);

        return day;
    }


    /**
     * 获取当天最大时间
     *
     * @param date
     * @return
     */
    public static Date getEndOfDay(Date date) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
        ;
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
        return Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 得到一个日期的n天之后的日期
     *
     * @param days
     * @return
     */
    public static String getAfterDayDate(Date date, String days, String pattern) {

        int daysInt = Integer.parseInt(days);
        if (StringUtils.isBlank(pattern)) {
            pattern = "yyyy-MM-dd";
        }
        Calendar canlendar = Calendar.getInstance(); // java.util包
        canlendar.setTime(date);
        canlendar.add(Calendar.DATE, daysInt);

        Date dateAdd = canlendar.getTime();

        SimpleDateFormat sdfd = new SimpleDateFormat(pattern);
        String dateStr = sdfd.format(dateAdd);

        return dateStr;
    }


    /**
     * 得到n天之后的日期
     *
     * @param daysInt
     * @return
     */
    public static String getAfterDayDate(Integer daysInt) {

        Calendar canlendar = Calendar.getInstance(); // java.util包
        canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
        Date date = canlendar.getTime();

        SimpleDateFormat sdfd = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateStr = sdfd.format(date);

        return dateStr;
    }

    /**
     * 得到n年之后的日期
     *
     * @param year
     * @return
     */
    public static String getAfterYearDate(String year) {
        int yearInt = Integer.parseInt(year);

        Calendar canlendar = Calendar.getInstance(); // java.util包
        canlendar.add(Calendar.YEAR, yearInt); // 日期减 如果不够减会将月变动
        Date date = canlendar.getTime();

        SimpleDateFormat sdfd = new SimpleDateFormat("yyyyMMdd");
        String dateStr = sdfd.format(date);

        return dateStr;
    }

    /**
     * 得到n天之后的日期
     *
     * @param days
     * @return
     */
    public static String getAfterDayDate(String days) {
        int daysInt = Integer.parseInt(days);

        Calendar canlendar = Calendar.getInstance(); // java.util包
        canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
        Date date = canlendar.getTime();

        SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdfd.format(date);

        return dateStr;
    }

    /**
     * 得到n天之后是周几
     *
     * @param days
     * @return
     */
    public static String getAfterDayWeek(String days) {
        int daysInt = Integer.parseInt(days);

        Calendar canlendar = Calendar.getInstance(); // java.util包
        canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
        Date date = canlendar.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("E");
        String dateStr = sdf.format(date);

        return dateStr;
    }

    private Integer compareCorn(String corn, Date date) {
        Date cornDate = parse(corn, PATTERN_CORN);
        if (null == cornDate) {
            return null;
        }
        return cornDate.compareTo(date);
    }

    /**
     * 计算两个时间相差的月份数
     *
     * @param startDate
     * @param endDate
     * @return
     * @throws ParseException
     */
    public static Long getMonthSpace(Date startDate, Date endDate) {
        LocalDate localDate1 = parseLocalDate(startDate);
        LocalDate localDate2 = parseLocalDate(endDate);
        return localDate1.until(localDate2, ChronoUnit.MONTHS);
    }

    /**
     * 计算两个时间相差的天数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static Long getDaySpace(Date startDate, Date endDate) {
        LocalDate localDate1 = parseLocalDate(startDate);
        LocalDate localDate2 = parseLocalDate(endDate);
        return localDate1.until(localDate2, ChronoUnit.DAYS);
    }

    /**
     * 两个时间相减运算,结果向上取整（天）
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static Long getDaySpaceCeil(Date startDate, Date endDate) {
        long timeX = startDate.getTime();
        long timeY = endDate.getTime();
        long timeSub = 0;
        if (timeX > timeY) {
            timeSub = timeX - timeY;
        } else {
            timeSub = timeY - timeX;
        }
        long subDays = timeSub / 86400000;
        long subYu = timeSub % 86400000;
        if (subYu > 0) {
            subDays = ++subDays;
        }
        return subDays;
    }

    public static LocalDate parseLocalDate(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        LocalDate localDate = localDateTime.toLocalDate();
        return localDate;
    }

    public static String long2StrTime(long millSec) {
        if (0 == millSec) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(millSec);
        return sdf.format(date);
    }

    /**
     * 将日期转换成时间戳
     *
     * @param date
     * @return
     */
    public static long getLongTime(Date date) {
        if (date == null) {
            return 0;
        }
        return date.getTime();
    }


    /**
     * 判断日期是否为当前日期，或当前月
     * @param yyyyMMdd
     * @param parten
     * @return
     */
    public static boolean thisMonth(String yyyyMMdd, String parten) {
        Date now = new Date();
        SimpleDateFormat sf = new SimpleDateFormat(parten);
        String nowDay = sf.format(now);
        if (nowDay.equals(yyyyMMdd)) {
            return true;
        }
        return false;
    }

    public static boolean monthAgo(String yyyyMM, String parten) {
        Date now = new Date();
        SimpleDateFormat sf = new SimpleDateFormat(parten);
        String format = sf.format(now);
        if (Integer.parseInt(format) > Integer.parseInt(yyyyMM)) {
            return true;
        }
        return  false;
    }

    /**
     *
     * @param timeStr "yyyyMMddHHmmss"
     * @return 毫秒数
     */
    public static Long parseT(String timeStr)  {
        if(StringUtils.isNotBlank(timeStr)){
            try {
                return new SimpleDateFormat(PATTERN_LONG_UNMARK).parse(timeStr).getTime();
            } catch (ParseException e) {
//                e.printStackTrace();
                log.error("懒猫时间 {} 转换毫秒数失败", timeStr);
            }
        }
        return null;
    }

    public static Date clearHMS(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar.getTime();
    }
//    public static void main(String[] args) {
//        System.out.println(long2StrTime(1523948464));
//    }
}
