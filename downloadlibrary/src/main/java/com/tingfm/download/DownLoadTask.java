package com.tingfm.download;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 下载任务
 */

public class DownLoadTask implements Runnable {
    private final  String YOURFLODERNAME="TingFM";
    private String urlString;
    private  int contentLength,currentDownload,lastDownload;
    private long timeLast,timeNow;
    private Timer timer;
    private Context mContext;
    private SharedPreferences preferences;
    boolean stopFlag=false;
    private DownLoadStatusListener downloadStatuslistener;
    private DownloadInfo mDownloadInfo;
    public DownLoadTask(DownloadInfo mInfo,Context context){
        this.mDownloadInfo=mInfo;
        this.urlString=mInfo.urlString;
        this.mContext=context;
        this.preferences=mContext.getSharedPreferences("DownLoadRecord",Context.MODE_PRIVATE);
    }
    @Override
    public void run() {
        mDownloadInfo.status=DownloadInfo.DOWNLOAD_STATUS_DOWNLOADING;
        try {
            //上次下载的字节数
            int hasDownLoadLastTimeSize = preferences.getInt(urlString, 0);

            if (hasDownLoadLastTimeSize == 0) {
                /**没有下载过*/
               downloadFromStart();
            } else {//已经下载过进行断点续传
               resumeBrokenDownload(hasDownLoadLastTimeSize);
            }
        }catch(Exception e){
                e.printStackTrace();
                showNetError();
               System.out.println("下载异常");
            }

    }

    /**
     * 从头开始下载 downloadFromStart
     */
     private void downloadFromStart(){
         URL url = null;
         OutputStream os = null;
         InputStream is = null;
         try {
             url = new URL(this.urlString);
             HttpURLConnection conn = (HttpURLConnection) url.openConnection();
             conn.setRequestMethod("GET");
             conn.connect();
             if (conn.getResponseCode() == 200) {
                 is = conn.getInputStream();
                 contentLength = conn.getContentLength();
                 showTotalSize();
                 /**在SD卡进行存储*/
                 File f = new File(getDirName());
                 if (!f.exists()) {
                     f.mkdir();
                 }
                 String newFilename = getFileName(urlString);
                 File file = new File(newFilename);
                 // 1K的数据缓冲
                 byte[] bs = new byte[1024];
                 // 读取到的数据长度
                 int len;
                 // 输出的文件流
                  os = new FileOutputStream(newFilename);
                 timeLast = System.currentTimeMillis();
                 TimerTask task = new TimerTask() {
                     @Override
                     public void run() {
                         showSpeedAndDownloadedSize();
                     }
                 };
                 timer = new Timer();
                 timer.schedule(task, 1000, 2000); // 1s后启动任务，每2s执行一次
                 // 开始读取
                 while ((len = is.read(bs)) != -1 ) {
                     if (stopFlag) {
                         timer.cancel();
                         break;
                     }
                     os.write(bs, 0, len);
                     upDateDownSizeRecord(len);
                 }
                 if(!stopFlag) {
                     if(downloadStatuslistener!=null) {
                         downloadStatuslistener.onFinishDownload(this);
                     }
                     System.out.println("下载完成");
                     mDownloadInfo.status=DownloadInfo.DOWNLOAD_STATUS_FINISH;
                 }else{
                     if(downloadStatuslistener!=null) {
                         downloadStatuslistener.onPauseDownload(this);
                     }
                     mDownloadInfo.status=DownloadInfo.DOWNLOAD_STATUS_PAUSE;
    //                 notifyStatusChange();
                     System.out.println("下载暂停");
                 }
             } else {
                 System.out.println("服务器错误");
                 showNetError();
             }
         } catch (MalformedURLException e) {
             e.printStackTrace();
         } catch (ProtocolException e) {
             e.printStackTrace();
         } catch (FileNotFoundException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         }finally {
             try {
                 if(os!=null){
                     os.close();
                 }
                 if(is!=null) {
                     is.close();
                 }
             } catch (IOException e) {
                 e.printStackTrace();
             }

         }

     }

    /**
     * 断点续传
     */
    private void resumeBrokenDownload(int hasDownLoadLastTimeSize)  {

        URL url = null;
        InputStream is=null;
        RandomAccessFile storedFile=null;
        try {
            url = new URL(this.urlString);
        if (new File(getFileName(urlString)).exists()) {
            currentDownload = hasDownLoadLastTimeSize;
            HttpURLConnection connForSize = (HttpURLConnection) url.openConnection();
            int totalSize = connForSize.getContentLength();
            connForSize.connect();
            if (totalSize == hasDownLoadLastTimeSize) {
                System.out.println("已经下载完成了");
                if(downloadStatuslistener!=null) {
                    downloadStatuslistener.onFinishDownload(this);
                }
                mDownloadInfo.status=DownloadInfo.DOWNLOAD_STATUS_FINISH;
                return;
            }
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            System.out.println("hasDownLoadLastTimeSize" + hasDownLoadLastTimeSize);
            conn.setRequestProperty("RANGE", "bytes=" + hasDownLoadLastTimeSize + "-");
            contentLength = conn.getContentLength() + hasDownLoadLastTimeSize;
            System.out.println("conn.getContentLength()" + conn.getContentLength());
            conn.connect();
            System.out.println("conn.getResponseCode():" + conn.getResponseCode());
            if (conn.getResponseCode() == 206) {
                showTotalSize();
                is = conn.getInputStream();
                //在SD卡进行存储
                String name = getFileName(urlString);
                storedFile = new RandomAccessFile(name, "rwd");
                storedFile.seek(hasDownLoadLastTimeSize);
                byte[] bs = new byte[1024];
                // 读取到的数据长度
                int len;
                // 输出的文件流
                timeLast = System.currentTimeMillis();
                lastDownload = hasDownLoadLastTimeSize;
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        showSpeedAndDownloadedSize();
                    }
                };
                timer = new Timer();
                timer.schedule(task, 1000, 2000); // 1s后启动任务，每2s执行一次
                // 开始读取
                while ((len = is.read(bs)) != -1) {
                    if (stopFlag) {
                        timer.cancel();
                        break;
                    }
                    storedFile.write(bs, 0, len);
                    upDateDownSizeRecord(len);
                }
                if(!stopFlag) {
                    if(downloadStatuslistener!=null) {
                        downloadStatuslistener.onFinishDownload(this);
                    }
                    mDownloadInfo.status=DownloadInfo.DOWNLOAD_STATUS_FINISH;
                    System.out.println("下载完成");
                }else{
                    if(downloadStatuslistener!=null) {
                        downloadStatuslistener.onPauseDownload(this);
                    }
                    mDownloadInfo.status=DownloadInfo.DOWNLOAD_STATUS_PAUSE;
                    System.out.println("下载暂停");
                  //  notifyStatusChange();
                }
            } else {
                showNetError();
                System.out.println("服务器异常");
            }
        }else{
            System.out.println("文件存储异常，重新下载");
            preferences.edit().putInt(urlString, 0).commit();
            downloadFromStart();
        }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                storedFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 文件名和文件夹的处理
     * @return
     */
    private String getDirName(){
        String dirName = "";
        dirName = Environment.getExternalStorageDirectory() + "/"+YOURFLODERNAME+"/";
        return dirName;
    }
    private String getFileName(String urlString) {
        String newFilename = urlString.substring(urlString.lastIndexOf("/") + 1);
        return getDirName() + newFilename;
    }

    /**
     * 通知UI线程下载速度和已经下载大小
     */
    private void showSpeedAndDownloadedSize() {
        timeNow=System.currentTimeMillis();
        long speed=((currentDownload-lastDownload))/((timeNow-timeLast));
        lastDownload=currentDownload;
        timeLast=timeNow;
        downloadStatuslistener.onUpdateDownloadingInfo(this, speed, currentDownload);
        if(currentDownload==contentLength){
            timer.cancel();
        }
    }
    /**
     * 通知UI线程下载文件的大小
     */
    private void showTotalSize(){
        if(downloadStatuslistener!=null) {
            downloadStatuslistener.onStartDownload(this, contentLength);
        }
    }

    /**
     * 更新已经下载的大小的记录
     */
    private void upDateDownSizeRecord(int len){
        currentDownload += len;
        preferences.edit().putInt(urlString, currentDownload).commit();
    }


    /**
     * 通知UI线程网络错误
     */

    private void showNetError(){
       // mHandler.sendEmptyMessage(3);
    }

    public void setDownloadStatusListener(DownLoadStatusListener listener){
            this.downloadStatuslistener=listener;
    }


    interface DownLoadStatusListener{
        void onStartDownload(DownLoadTask task, int contentLength);
        void onFinishDownload(DownLoadTask task);
        void onPauseDownload(DownLoadTask task);
        void onUpdateDownloadingInfo(DownLoadTask task, long speed, int downLoadsize);

    }




    /**
     * 加入下载等待队列中
     */
    public void joinDownloadWaitingQueue(){
        mDownloadInfo.status=DownloadInfo.DOWNLOAD_STATUS_QUEUE;
    }

    public DownloadInfo getDownloadInfo(){
     return this.mDownloadInfo;
    }
}
