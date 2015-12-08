package com.tingfm.download;

/**
 * Created by david on 15/12/4.
 */
/**
 * 下载过程相关的工具方法
 */
public class DownloadUtils {
    /**
     * 将以B为单位的数字转换成以MB为单位的字符串
     * @param B
     * @return MBString
     */
    public static String revertBtoMB(int B){
        return String.format("%.2f",B/1024.0/1024.0);
    }

}