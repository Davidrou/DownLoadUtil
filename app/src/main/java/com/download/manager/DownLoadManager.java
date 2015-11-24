package com.download.manager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by david on 15/11/23.
 */
public class DownLoadManager {

    private static DownLoadManager manager;
    private ExecutorService downloadThreadPool;
    /**
     * 将构造函数设为私有的不让其它人创建，这样就只能使用提供的单例了
     */
    private DownLoadManager(){

    }
    /**
     * 获取实例
     * @return
     */
    public static DownLoadManager with(){
        if(manager==null){
            synchronized (DownLoadManager.class){
                if(manager==null){
                    manager=new DownLoadManager();
                }
            }
        }
        return manager;
    }

    /**
     * 获取下载线程池,需要考虑到同步。
     * @return
     */
    private ExecutorService getDownloadExecutor() {
        if (downloadThreadPool == null) {
            synchronized (DownLoadManager.class) {
                if (downloadThreadPool == null) {
                    downloadThreadPool=Executors.newSingleThreadExecutor();
                }
            }
        }
        return downloadThreadPool;
    }

    public void submit(DownLoadTask task){
        getDownloadExecutor().submit(task);
        //todo
    }

    private boolean isContainTask(String id){

        //todo
        return false ;

    }



}