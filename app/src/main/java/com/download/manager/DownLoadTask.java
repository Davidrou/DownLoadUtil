package com.download.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by david on 15/11/18.
 */

public class DownLoadTask implements Runnable {
    private final  String YOURFLODERNAME="MyDownload";
    String urlString;
    Handler mHandler;
    int contentLength,currentDownload,lastDownload;
    long timeLast,timeNow;
    Timer timer;
    Context mContext;
    SharedPreferences preferences;
    boolean stopFlag=false;
    public DownLoadTask(String url,Handler mHandler,Context context){
        this.mHandler=mHandler;
        this.urlString=url;
        this.mContext=context;
        this.preferences=mContext.getSharedPreferences("DownLoadRecord",Context.MODE_PRIVATE);
    }
    @Override
    public void run() {
        try {
            //上次下载的字节数
            int hasDownLoadLastTimeSize = preferences.getInt(urlString, 0);
            URL url = new URL(this.urlString);
            if (hasDownLoadLastTimeSize == 0) {//没有下载过
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                conn.setRequestMethod("GET");
                if (conn.getResponseCode() == 200) {
                    InputStream is = conn.getInputStream();
                    contentLength = conn.getContentLength();
                    showTotalSize();

                    //在SD卡进行存储
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
                    OutputStream os = new FileOutputStream(newFilename);
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
                    os.close();
                    is.close();
                } else {
                    System.out.println("服务器错误");
                    showNetError();
                }
            } else {//已经下载过进行断点续传
                if (new File(getFileName(urlString)).exists()) {
                    currentDownload = hasDownLoadLastTimeSize;
                    HttpURLConnection connForSize = (HttpURLConnection) url.openConnection();
                    int totalSize = connForSize.getContentLength();
                    connForSize.connect();
                    if (totalSize == hasDownLoadLastTimeSize) {
                        System.out.println("已经下载完成了");
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
                        InputStream is = conn.getInputStream();
                        //在SD卡进行存储
                        String name = getFileName(urlString);
                        RandomAccessFile storedFile = new RandomAccessFile(name, "rwd");
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
                        System.out.println("下载完成");
                        // 完毕，关闭所有链接
                        is.close();
                        storedFile.close();
                    } else {
                        showNetError();
                        System.out.println("服务器异常");
                    }
                }else{
                    preferences.edit().putInt(urlString, 0).commit();
                    run();
                }
            }
        }catch(Exception e){
                e.printStackTrace();
                showNetError();
            System.out.println("下载异常");
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
        Message msg = mHandler.obtainMessage();
        msg.what = 2;
        Bundle b=new Bundle();
        b.putInt("size", currentDownload);
        b.putLong("speed", speed);
        msg.setData(b);
        mHandler.sendMessage(msg);//更新界面
        if(currentDownload==contentLength){
            msg = mHandler.obtainMessage();
            msg.what = 2;
            b.putInt("size", currentDownload);
            b.putLong("speed", speed);
            msg.setData(b);
            mHandler.sendMessage(msg);//更新界面
            timer.cancel();
        }
    }
    /**
     * 通知UI线程下载文件的大小
     */
    private void showTotalSize(){
        Message message = new Message();
        message.what = 1;
        Bundle b = new Bundle();
        b.putInt("size", contentLength);
        message.setData(b);
        mHandler.sendMessage(message);
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
        mHandler.sendEmptyMessage(3);
    }

    public void setStopFlag(boolean b){
        this.stopFlag=b;
    }
}
