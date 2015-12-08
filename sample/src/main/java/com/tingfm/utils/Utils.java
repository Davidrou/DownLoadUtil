package com.tingfm.utils;

/**
 * Created by david on 15/11/26.
 */
public class Utils {
    /**
     * 15366->1.5万
     * 1569->1569
     */
    public final static String convertWithTenthousand(int srcNum){
        if(srcNum<10000){
            return srcNum+"";
        }else{
            int littleArea=(srcNum%10000)/1000;
            return srcNum/10000+"."+littleArea+"万";
        }
    }

    public final static String formatTime(float srcNum){
        int hour=(int)srcNum/60;
        int minute=(int)srcNum%60;
        if(minute>10){
            return hour + ":" + minute;
        }else{
            return hour + ":0" + minute;
        }
    }

    public final static String convertTimeDifferent(long pastTimeStamp){
        long oneHourMills=60*60*1000;
        long oneDayMills=oneHourMills*24;
        long oneMonthMills=oneDayMills*30;
        long oneYearMills=365*oneDayMills;
        long different=System.currentTimeMillis()-pastTimeStamp;
        System.out.println(System.currentTimeMillis()+"differnt:  "+different);
        if(different<oneHourMills){
            return "刚刚";
        }else if(different<oneDayMills){
            return different/oneHourMills+"小时前";
        }else if(different<oneMonthMills){
            return different/oneDayMills+"天前";
        }else if(different<oneYearMills){
            return different/oneMonthMills+"个月前";
        }else{
            return different/oneYearMills+"年前";
        }
    }
}
