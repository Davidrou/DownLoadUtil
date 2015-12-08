package com.tingfm.download;

import android.content.Context;

/**
 * Created by david on 15/11/23.
 */
public class DownloadInfo {

    /**
     * 刚刚创建下载任务，还未启动
     */
    public static final int DOWNLOAD_STATUS_NEW=1;

    /**
     * 排队中
     */
    public static final int DOWNLOAD_STATUS_QUEUE=2;

    /**
     * 下载中
     */
    public static final int DOWNLOAD_STATUS_DOWNLOADING=3;
    /**
     *暂停
     */
    public static final int DOWNLOAD_STATUS_PAUSE=4;
    /**
     * 下载完成
     */
    public static final int DOWNLOAD_STATUS_FINISH=5;

    private Context mContext;
    public int id;
    public  String urlString;
    public String fileName;
    public int totalSize;
    public int downloadSize;
    public long speed;
    public DownLoadTask downloadThread;
    public int status=this.DOWNLOAD_STATUS_NEW;
    public DownloadInfo(int id,Context mContext,String fileName,String urlString){
        this.id=id;
        this.fileName=fileName;
        this.urlString=urlString;
        this.mContext=mContext;
    }

}
