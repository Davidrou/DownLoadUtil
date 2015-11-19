package com.example.david.downloaddem;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by david on 15/11/18.
 */
public class DownLoadThread extends Thread {
    String urlString;
    Handler mHandler;
    DownLoadThread(String url,Handler mHandler){
        this.mHandler=mHandler;
        this.urlString=url;
    }
    @Override
    public void run() {
        try {
            URL url=new URL(this.urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            if(conn.getResponseCode()==200){
                InputStream is=conn.getInputStream();
                int contentLength=conn.getContentLength();
                System.out.println("文件长度：" + contentLength);
                Message message=new Message();
                message.what=1;
                Bundle b=new Bundle();
                b.putInt("size",contentLength);
                message.setData(b);
                mHandler.sendMessage(message);

                String dirName = "";
                dirName = Environment.getExternalStorageDirectory()+"/MyDownload/";
                File f = new File(dirName);
                if(!f.exists())
                {
                    f.mkdir();
                }
                String newFilename = urlString.substring(urlString.lastIndexOf("/")+1);
                newFilename = dirName + newFilename;
                int currentDownload=0;
                File file = new File(newFilename);
                // 1K的数据缓冲
                byte[] bs = new byte[1024];
                // 读取到的数据长度
                int len;
                // 输出的文件流
                OutputStream os = new FileOutputStream(newFilename);
                // 开始读取
                while ((len = is.read(bs)) != -1) {
                    os.write(bs, 0, len);
                    currentDownload+=len;
                    Message message2=new Message();
                    message2.what=2;
                    b.putInt("size", currentDownload);
                    message2.setData(b);
                    mHandler.sendMessage(message2);
                }
                System.out.println("下载完成");
                // 完毕，关闭所有链接
                os.close();
                is.close();
            }else{
                System.out.println("网络错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
