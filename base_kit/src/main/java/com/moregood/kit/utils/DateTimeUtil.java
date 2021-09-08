package com.moregood.kit.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Devin.Ding on 2020/9/3 20:31.
 * Descripe:
 */
public class DateTimeUtil {

    public static final String yyyyMMdd = "yyyy-MM-dd";
    public static final String yyyyMMddHHmmss = "yyyy-MM-dd HH:mm:ss";
    public static final String HHmmss = "HH:mm:ss";

    /**
     * 获取当前日期的字符串
     * 日期格式，如：yyyy-MM-dd
     *
     * @return
     */
    public static String getDateNow() {
        return stampToDate(System.currentTimeMillis(), yyyyMMdd);
    }

    /**
     * 获取当前时间的字符串
     * 日期格式，如：HH:mm:ss
     *
     * @return
     */
    public static String getTimeNow() {
        return stampToDate(System.currentTimeMillis(), HHmmss);
    }

    /**
     * 将时间戳转换为时间
     *
     * @param timestamp 时间戳
     * @param format    时间格式
     * @return
     */
    public static String stampToDate(long timestamp, String format) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = new Date(timestamp);
        res = simpleDateFormat.format(date);
        return res;
    }

    /**
     * 将一段时间转换为具体时长，例：1小时10分钟5秒
     * @param durationMillis
     * @return
     */
    public static String getTimeDuration(long durationMillis) {
        StringBuffer stringBuffer = new StringBuffer();
        if (durationMillis <= 0) return null;
        int[] timeArys = millisToTimeArys(durationMillis);
        for (int i = 0; i < timeArys.length; i++) {
            if (timeArys[i] > 0) {
                String mode = i == 0 ? "小时" : (i == 1 ? "分钟" : "秒");
                stringBuffer.append(timeArys[i] + mode);
            }
        }
        return stringBuffer.toString();
    }

    /**
     * 转换时间长度为数组
     *
     * @param millis
     * @return int[] {小时, 分钟, 秒}
     */
    public static int[] millisToTimeArys(final long millis) {
        if (millis <= 0) return null;
        int[] timeArys = new int[3];
        int[] unitLen = {3600000, 60000, 1000};
        long millisTime = millis;
        for (int i = 0; i < 3; i++) {
            if (millisTime >= unitLen[i]) {
                long mode = millisTime / unitLen[i];
                millisTime -= mode * unitLen[i];
                timeArys[i] = (int) mode;
            }
        }
        return timeArys;
    }
    public static String timeDateStr(String time){
        SimpleDateFormat sdf = new SimpleDateFormat(yyyyMMdd);
        Date date = null;
        try {
            date = sdf.parse(time);
            return date.getTime()+"";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
    //月,日小于10前面补0
    public static  String zeroize(int obj) {
        return  obj < 10 ? "0" + obj : obj+"";
    }
}