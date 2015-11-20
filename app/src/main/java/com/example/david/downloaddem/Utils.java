package com.example.david.downloaddem;

/**
 * Created by david on 15/11/19.
 */
public class Utils {
    /**
     * 下载过程相关的工具方法
     */


    /**
     * 将以B为单位的数字转换成以MB为单位的字符串
     * @param B
     * @return MBString
     */
    public static String revertBtoMB(int B){
        return String.format("%.2f",B/1024.0/1024.0);
    }

}
