package com.hdl.gctzsc.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    /**
     * 把系统当前的时间格式化
     *
     * @return
     */
    public static String getCurrenDateTime() {
        Date utilDate = new Date();
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(utilDate);
    }

    /**
     * 将一个"2016年04月21日 21:44"格式的日期 装换为毫秒值输出
     *
     * @param time
     * @return
     * @throws ParseException
     */
    public static long dateToLong(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyy年MM月dd日 HH:mm");
        try {
            return format.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取格式化之后的时间格式
     *
     * @param time
     * @return
     * @throws ParseException
     */
    public static String getTimeAfterFormat(long time) throws ParseException {
        return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date(time));
    }

    /**
     * 将毫秒值转换为"天时分秒的格式"
     *
     * @param ms
     * @return
     */
    public static String formartTime(long ms) {
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;
        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;
//        return (day / 10 > 0 ? day : "0" + day) + "天 "
//                + (hour / 10 > 0 ? hour : "0" + hour) + ":"
//                + (minute / 10 > 0 ? minute : "0" + minute) + ":"
//                + (second / 10 > 0 ? second : "0" + second);
        return (day / 10 > 0 ? day : "0" + day) + "天"
                + (hour / 10 > 0 ? hour : "0" + hour) + "小时"
                + (minute / 10 > 0 ? minute : "0" + minute)+"分钟";
    }
}
