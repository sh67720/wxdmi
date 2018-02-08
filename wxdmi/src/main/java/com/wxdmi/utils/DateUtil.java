package com.wxdmi.utils;

import org.jetbrains.annotations.NotNull;
import org.springframework.util.StringUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by bin on 2015/6/23.
 */
public class DateUtil {
    public static final String FULL_FORMAT_STR = "yyyyMMddHHmmssSSS";
    public static final String FULL_FORMAT_STR2 = "yyyyMMddHHmmss";
    public static final String FULL_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String FULL_DAY_FORMAT = "yyyy-MM-dd";
    public static final String YEAR_MONTH_FORMAT = "yyyyMM";
    public static final String YEAR_FORMAT = "yyyy";
    public static final String MONTH_FORMAT = "MM";
    public static final String DATE_BEGIN_PREFIX = " 00:00:00";
    public static final String DATE_END_SUFFIX = " 23:59:59";
    public static final String MONTH_DAY = "MM-dd";
    public static final String DHST_FORMAT = "yyyyMMddHHmm";
    public static final String DAY = "dd";
    public static String toYearMonth(Date date) {
        return format(date, "yyyy-MM");
    }

    public static String toDay(Date date) {
        return format(date, FULL_DAY_FORMAT);
    }

    public static String toDayOfMonth(Date date) {
        return format(date, "dd");
    }
    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }
    public static String format(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return date == null?"":sdf.format(date);
    }
    public static String format(Date date){
        SimpleDateFormat format = new SimpleDateFormat(FULL_DAY_FORMAT);
        return format.format(date);
    }

    public static String buildFullBeginStr(String input) {
        if(StringUtils.isEmpty(input)) return null;

        return input + DATE_BEGIN_PREFIX;
    }
    public static String buildFullEndStr(String input) {
        if(StringUtils.isEmpty(input)) return null;

        return input + DATE_END_SUFFIX;
    }

    /**
     *
     * @param input 格式HH:MM
     * @return
     */
    public static Integer convertHMToMins(@NotNull String input) {
        if(StringUtils.isEmpty(input)) return null;

        if(!input.contains(":")) return null;
        String[] arrays = input.split(":");
        if(arrays == null || arrays.length != 2) return null;

        int hour = Integer.valueOf(arrays[0]);          //小时
        int minute = Integer.valueOf(arrays[1]);        //分

        return hour * 60 + minute;
    }
    public static Integer convertHMToMins(@NotNull Date input) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(input);

        int hour = cal.get(Calendar.HOUR_OF_DAY);          //小时

        int minute = cal.get(Calendar.MINUTE);      //分

        return hour * 60 + minute;
    }



    // 获取月份差
    public static int getMonthDiff(Date startDate, Date endDate){
        int monthday;
        Calendar starCal = Calendar.getInstance();
        starCal.setTime(startDate);

        int sYear = starCal.get(Calendar.YEAR);
        int sMonth = starCal.get(Calendar.MONTH);
        int sDay = starCal.get(Calendar.DAY_OF_MONTH);

        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);
        int eYear = endCal.get(Calendar.YEAR);
        int eMonth = endCal.get(Calendar.MONTH);
        int eDay = endCal.get(Calendar.DAY_OF_MONTH);

        monthday = ((eYear - sYear) * 12 + (eMonth - sMonth));

        //这里计算零头的情况，根据实际确定是否要加1 还是要减1
        if (sDay < eDay)
            monthday = monthday + 1;

        return monthday;
    }

    public static String toFullStr(Date date){
        return format(date, FULL_FORMAT_STR);
    }
    public static String toSeconds(Date date) {
        return format(date, FULL_FORMAT);
    }
    public static String getNowStr() {
        return format(new Date(), FULL_FORMAT);
    }
    public static Date getDateSecondBeofre(Date date, int num) {
        Calendar cld = Calendar.getInstance();
        cld.setTime(date);
        cld.set(13, cld.get(13) - num);
        return cld.getTime();
    }

    public static String getChineseDate(){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日hh时mm分");
        return  sdf.format(new Date());
    }

    public static String getChineseMonthDay(Date date) {
        return format(date, "MM月dd日");
    }
    public static String getChineseDay(Date date) {
        return format(date, "dd日");
    }
    /**
     * 日期相减 返回分钟
     *
     * @param bigTime
     *            大的日期
     * @param samllTime
     *            小的日期
     * @return
     */

    public static double minuteIntervalTime(Long bigTime,Long samllTime){
        return (bigTime-samllTime)/(1000*60);
    }

    public static Date valueOfStandard(String s) {
        return parse(s, "yyyy-MM");
    }
    public static Date valueOfStandard2(String s) {
        return parse(s, "yyyyMM");
    }

    /**
     * 以指定的格式返回日期
     *
     * @param s
     *            日期
     * @param format
     *            格式
     * @return
     */
    public static Date valueof(String s, String format) {
        return parse(s, format);
    }
    /**
     * 以指定格式格式化日期
     *
     * @param s
     *            日期
     * @param format
     *            格式
     * @return
     */
    private static Date parse(String s, String format) {
        if (s == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(s);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 获取日期的0点
     * @return
     */
    public static Date getBeginT(Date date){
        if(date == null) return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return getBeginT(cal);
    }

    public static String getBeginT(String beginDate){
        return beginDate +" "+ "00:00:00";
    }
    public static String getEndT(String beginDate){
        return beginDate +" "+ "23:59:59";
    }
    /**
     * 获取日期的0点
     * @return
     */
    public static Date getBeginT(Calendar calendar){
        if(calendar == null) calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }
    /**
     * 获取日期的中间时间点
     * @return
     */
    public static Date getMiddleT(Calendar calendar){
        if(calendar == null) calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }
    /**
     * 分秒设置为0
     * @return
     */
    public static Date getHourBeginT(Calendar calendar){
        if(calendar == null) calendar = Calendar.getInstance();

        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    /**
     *  获取日期的24点
     * @return
     */
    public static Date getEndT(Date date){
        if(date == null) return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        return getEndT(cal);
    }
    /**
     *  获取日期的24点
     * @param calendar
     * @return
     */
    public static Date getEndT(Calendar calendar){
        if(calendar == null) calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);

        return calendar.getTime();
    }
    /**
     * 格式化日期 返回 如(20000110)
     *
     * @param date
     *            日期
     * @return
     */
    public static String toShortDay(Date date) {
        return format(date, "yyyyMMdd");
    }


    public static String toShortDayTime(Date date){
        return format(date,"yyyyMMddHH");
    }
    /**
     * 格式化日期 返回月份
     *
     * @param date
     *            日期
     * @return
     */
    public static String toMonth(Date date) {
        return format(date, "MM月");
    }

    /**
     * 格式化日期 返回日期
     *
     * @param date
     *            日期
     * @return
     */
    public static String toShortdd(Date date) {
        return format(date, "dd");
    }

    /**
     * 格式化日期 返回时分
     *
     * @param date
     *            日期
     * @return
     */
    public static String toShortHm(Date date) {
        return format(date, "HH:mm");
    }

    public static String toShortHH(Date date) {
        return format(date, "HH");
    }
    public static Integer toShotHHInteger(Date date){
        String time = toShortHH(date);
        if (time.startsWith("0")){
            time = time.substring(1);
        }
        return Integer.parseInt(time);
    }

    /**
     * 格式化日期 返回时分秒
     *
     * @param date
     *            日期
     * @return
     */
    public static String toShortSeconds(Date date) {
        return format(date, "HH:mm:ss");
    }

    /**
     * 格式化日期 返回年份
     *
     * @param date
     *            日期
     * @return
     */
    public static String toShortYear(Date date) {
        return format(date, "yyyy");
    }
    /**
     * 取 指定天数 后的日期
     *
     * @param date
     *            参考日期
     * @param days
     *            指定天数
     * @return date
     */
    @SuppressWarnings("static-access")
    public static Date getDateAfter(Date date, int days) {
        Calendar cld = Calendar.getInstance();
        cld.setTime(date);
        cld.set(cld.DATE, (cld.get(cld.DATE) + days));
        return cld.getTime();
    }
    @SuppressWarnings("static-access")
    public static  Date getDateBefore(Date date,int days){
        Calendar cld = Calendar.getInstance().getInstance();
        cld.setTime(date);
        cld.set(cld.DATE, (cld.get(cld.DATE) - days));
        return cld.getTime();
    }
    /**
     * 日期转换成字符串
     * @param date
     * @return str
     */
    public static String dateToStr(Date date) {
        return dateToStr(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 日期转换成字符串
     * @param date
     * @return str
     */
    public static String dateToStr(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String str = sdf.format(date);
        return str;
    }

    public static String dateToStr(String format) {
        return dateToStr(new Date(), format);
    }

    /**
     * 字符串转换成日期
     * @param str
     * @return date
     */
    public static Date strToDate(String str) {

        return strToDate(str, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 字符串转换成日期
     * @param str
     * @return date
     */
    public static Date strToDate(String str, String format) {

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static double dayInterval(Date big, Date small) {
        big = roundToDay(big);
        small = roundToDay(small);
        return (double)((big.getTime() - small.getTime()) / 86400000L);
    }

    public static double minuteInterval(Date big, Date small) {
        return (double)((big.getTime() - small.getTime()) / 60000L);
    }

    public static double secondInterval(Date big, Date small) {
        return (double)((big.getTime() - small.getTime()) / 1000L);
    }

    public static int workDayInterval(Date small, Date big) {
        big = roundToDay(big);
        small = roundToDay(small);
        GregorianCalendar smallGc = new GregorianCalendar();
        smallGc.setTime(small);
        GregorianCalendar bigGc = new GregorianCalendar();
        bigGc.setTime(big);
        int workDays = 0;
        long bigTime = bigGc.getTime().getTime();

        while(smallGc.getTime().getTime() < bigTime) {
            int week = smallGc.get(7);
            smallGc.add(5, 1);
            if(week != 7 && week != 1) {
                ++workDays;
            }
        }

        return workDays;
    }

    public static boolean isWorkDay(Date date) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        int week = gc.get(7);
        return week == 7 || week == 1;
    }

    public static Date roundToDay1(Date date) {
        date = roundToHour1(date);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.set(11, 0);
        return gc.getTime();
    }

    public static Date roundToHour1(Date date) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.set(12, 0);
        gc.set(13, 0);
        gc.set(14, 0);
        return gc.getTime();
    }

    public static Date roundToDay(Date date) {
        date = roundToHour(date);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
        String sf_date = sf.format(date);

        try {
            gc.set(11, sf.parse(sf_date).getHours());
        } catch (ParseException var5) {
            var5.printStackTrace();
        }

        return gc.getTime();
    }

    public static Date roundToHour(Date date) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.set(12, date.getMinutes());
        gc.set(13, date.getSeconds());
        gc.set(14, 0);
        return gc.getTime();
    }

    public static Date nextDate(Date date) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.add(5, 1);
        return roundToDay(gc.getTime());
    }
    public static Date lastNumMonth(Integer flag){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.MONTH,-flag);
        return calendar.getTime();
    }

    public static Date nextHour(Date date) {
        date = add(date, 10, 1);
        return roundToHour(date);
    }

    public static Date add(Date date, int field, int amount) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.add(field, amount);
        return gc.getTime();
    }

    public static Date lastDate(Date date) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.add(Calendar.DAY_OF_MONTH, -1);
        return roundToDay(gc.getTime());
    }


    public static Date roundToMonth(Date date) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.set(5, 1);
        gc.set(11, 0);
        gc.set(12, 0);
        gc.set(13, 0);
        gc.set(14, 0);
        return gc.getTime();
    }

    public static Date getFirstDayOfMonth(Date date) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.set(5, 1);
        return roundToDay(gc.getTime());
    }

    public static Date getFirstDayOfMonthByNum(Date date, int num) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.add(2, num);
        gc.set(5, 1);
        return roundToDay(gc.getTime());
    }

    public static Date getFirstDayOfWeek(Date date) {
        while(getTimeField(date, 7) != 2) {
            date = lastDate(date);
        }

        return date;
    }

    public static Date getLastDayOfMonth(Date date) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.add(2, 1);
        gc.set(5, 0);
        return roundToDay(gc.getTime());
    }

    public static Date getLastDayOfWeek(Date date) {
        while(getTimeField(date, 7) != 1) {
            date = nextDate(date);
        }

        return date;
    }

    public static String oracleToDate(Date date) {
        return "to_date(\'" + format(date, "yyyy-MM-dd") + "\', \'yyyy-mm-dd\')";
    }

    public static int getTimeField(Date date, int field) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        return gc.get(field);
    }

    public static Date setTimeField(Date date, int field, int timeNum) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.set(field, timeNum);
        return gc.getTime();
    }

    public static String ampm(Date date) {
        int hours = getTimeField(date, 11);
        return hours <= 12?"A":"P";
    }

    public static String ampm(Date startTime, Date endTime) {
        String start = ampm(startTime);
        String end = ampm(endTime);
        return "A".equals(start) && "A".equals(end)?"A":("P".equals(start) && "P".equals(end)?"P":"N");
    }

    public static Date[] getTimeInterval(Date date, String ampm) {
        Date startDate = (Date)date.clone();
        Date endDate = (Date)date.clone();
        if("A".equals(ampm)) {
            startDate = setTimeField(startDate, 11, 9);
            endDate = setTimeField(endDate, 11, 12);
        } else if("P".equals(ampm)) {
            startDate = setTimeField(startDate, 11, 12);
            endDate = setTimeField(endDate, 11, 18);
        } else if("N".equals(ampm)) {
            startDate = setTimeField(startDate, 11, 9);
            endDate = setTimeField(endDate, 11, 18);
        }

        startDate = setTimeField(startDate, 12, 0);
        endDate = setTimeField(endDate, 12, 0);
        startDate = setTimeField(startDate, 13, 0);
        endDate = setTimeField(endDate, 13, 0);
        Date[] dates = new Date[]{startDate, endDate};
        return dates;
    }

    public static String getChineseWeekName(Date date) {
        int w = getTimeField(date, 7);
        String cw = "";
        switch(w) {
            case 1:
                cw = "星期日";
                break;
            case 2:
                cw = "星期一";
                break;
            case 3:
                cw = "星期二";
                break;
            case 4:
                cw = "星期三";
                break;
            case 5:
                cw = "星期四";
                break;
            case 6:
                cw = "星期五";
                break;
            case 7:
                cw = "星期六";
        }

        return cw;
    }

    public static Date prevYear(Date date, int num) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.add(Calendar.YEAR, num);
        return roundToDay(gc.getTime());
    }

    public static Date setTimeOfDay(Date date, int hour, int minute, int second) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.set(11, hour);
        gc.set(12, minute);
        gc.set(13, second);
        gc.set(14, 0);
        return gc.getTime();
    }

    public static Date min(Date d1, Date d2) {
        return d1.getTime() > d2.getTime()?d2:d1;
    }

    public static Date max(Date d1, Date d2) {
        return d1.getTime() < d2.getTime()?d2:d1;
    }

    public static int compareTime(Date date1, Date date2) {
        GregorianCalendar g1 = new GregorianCalendar();
        g1.setTime(date1);
        GregorianCalendar g2 = new GregorianCalendar();
        g1.setTime(date2);
        clearYMD(g1);
        clearYMD(g2);
        return g1.before(g2)?1:(g2.before(g1)?-1:0);
    }

    private static void clearYMD(GregorianCalendar g) {
        g.set(1, 1900);
        g.set(2, 0);
        g.set(5, 1);
    }

    public static List listMonthOption(Date startMonth, Date endMonth) {
        ArrayList list = new ArrayList();

        for(Date date = endMonth; date.getTime() - startMonth.getTime() > 0L; date = add(date, 2, -1)) {
            list.add(date);
        }

        return list;
    }

    public static List listMonthOption(int monthNum) {
        Date endDate = new Date();
        Date startDate = add(endDate, 2, -monthNum);
        return listMonthOption(startDate, endDate);
    }

    public static Date getBeforeDate(Date date, int days) {
        Calendar cld = Calendar.getInstance();
        cld.setTime(date);
        cld.set(5, cld.get(5) - days);
        return cld.getTime();
    }

    public static Date getBeforeDate(int days) {
        return getBeforeDate(new Date(), days);
    }

    public static Date getAfterDate(Date date, int days) {
        Calendar cld = Calendar.getInstance();
        cld.setTime(date);
        cld.set(5, cld.get(5) + days);
        return cld.getTime();
    }

    public static Date getAfterDate(int days) {
        return getAfterDate(new Date(), days);
    }

    public static int getDays(Date startDate, Date endDate) {
        startDate = setTimeOfDay(startDate, 0, 0, 0);
        endDate = setTimeOfDay(endDate, 0, 0, 0);
        int days = (int)((endDate.getTime() - startDate.getTime()) / 86400000L);
        return days;
    }

    public static String dateArithmetic(Date originalDate, int countDay) {
        SimpleDateFormat sdm = new SimpleDateFormat("yyyy-MM-dd");
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(originalDate);
        int year = cal.get(1);
        int month = cal.get(2);
        int day = cal.get(5);
        cal.add(5, -countDay);
        return sdm.format(cal.getTime());
    }

    public static Date dateArithmeticForDate(Date originalDate, int countDay) {
        new SimpleDateFormat("yyyy-MM-dd");
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(originalDate);
        int year = cal.get(1);
        int month = cal.get(2);
        int day = cal.get(5);
        cal.add(5, -countDay);
        return cal.getTime();
    }

    public static String getEnglishWeekName(Date date) {
        int w = getTimeField(date, 7);
        String ew = "";
        switch(w) {
            case 1:
                ew = "SUNDAY";
                break;
            case 2:
                ew = "MONDAY";
                break;
            case 3:
                ew = "TUESDAY";
                break;
            case 4:
                ew = "WEDNESDAY";
                break;
            case 5:
                ew = "THURSDAY";
                break;
            case 6:
                ew = "FRIDAY";
                break;
            case 7:
                ew = "SATURDAY";
        }

        return ew;
    }

    public static double millisecondsInterval(Date big, Date small) {
        return (double)(big.getTime() - small.getTime());
    }

    public static Date getMonday(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return calendar.getTime();
    }

    public static Date getSunday(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return calendar.getTime();
    }

    public static Date getCurrYearFirst(int year){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        return calendar.getTime();
    }

    public static void main(String[] args) throws ParseException {
        Date beginT = new Date();
        String dd = getWeekOfDate(beginT);
        System.out.println(dd);
    }
  }
