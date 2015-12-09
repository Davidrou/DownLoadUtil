package com.tingfm.download;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by david on 15/11/23.
 */
public class DownLoadManager implements DownLoadTask.DownLoadStatusListener  {

    private static DownLoadManager manager;
    private ExecutorService downloadThreadPool;
    private DownLoadTask currentTask;
    private ArrayList<DownloadInfo> downloadingList;
    private DownloadingChangeListener mDownloadingChangeListener;
    /**
     * 将构造函数设为私有的不让其它人创建，这样就只能使用提供的单例了
     */
    private DownLoadManager(){
        downloadingList=new ArrayList<>(5);
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

    /**
     * 提交下载任务
     * @param task
     */
    public void submit(DownLoadTask task){
        //检查任务是否正在下载
        if(isDownloading(task)){
            task.stopFlag=true;
            mDownloadingChangeListener.onHasDownloadingWarn();
            return;
        }
        if(currentTask!=null){
            /**"加入等待队列"**/
            task.joinDownloadWaitingQueue();
        }
        task.setDownloadStatusListener(this);
        getDownloadExecutor().submit(task);
        task.getDownloadInfo().downloadThread=task;
        downloadingList.add(task.getDownloadInfo());
    }

    /**
     * 提交继续下载任务
     * @param task
     */

    public void submitFromContinue(DownLoadTask task){
        if(task.getDownloadInfo().status!=DownloadInfo.DOWNLOAD_STATUS_PAUSE){
            //// TODO: 15/12/7 继续下载失败
        }
        if(currentTask!=null){
            /**"加入等待队列"**/
            task.joinDownloadWaitingQueue();
        }
        task.setDownloadStatusListener(this);
        getDownloadExecutor().submit(task);
        task.getDownloadInfo().downloadThread=task;
        task.stopFlag=false;
    }

    /**
     * 检查要提交的任务是否正在下载
     * @param task
     * @return
     */
    private boolean isDownloading(DownLoadTask task ){
        String preSubmitUrl=task.getDownloadInfo().urlString;
        for(int i=0;i<downloadingList.size();i++){
           if( downloadingList.get(i).urlString.equals(preSubmitUrl)){
               return true;
           }
        }
        return false ;
    }


    @Override
    public void onStartDownload(DownLoadTask task, int contentLength) {
        currentTask=task;
        DownloadInfo info=downloadingList.get(downloadingList.indexOf(task.getDownloadInfo()));
        info.totalSize=contentLength;
        mDownloadingChangeListener.refreshData();
    }

    @Override
    public void onFinishDownload(DownLoadTask task) {
        if(task==currentTask) {
            currentTask = null;
        }
        downloadingList.remove(task.getDownloadInfo());
        mDownloadingChangeListener.refreshData();
    }

    @Override
    public void onPauseDownload(DownLoadTask task) {
        mDownloadingChangeListener.refreshData();
    }

    @Override
    public void onUpdateDownloadingInfo(DownLoadTask task, long speed, int downLoadsize) {
        if(downloadingList.contains(task.getDownloadInfo())){
            DownloadInfo info=downloadingList.get(downloadingList.indexOf(task.getDownloadInfo()));
            info.speed=speed;
            info.downloadSize=downLoadsize;
            mDownloadingChangeListener.refreshData();
        }
    }

    /**
     * 获取正在下载的列表
     * @return
     */
    public ArrayList<DownloadInfo> getDownloadingList(){
        return downloadingList;
    }

    /**
     * 继续下载任务
     * @param info
     */
    public void continueTask(DownloadInfo info) {
        submitFromContinue(info.downloadThread);
    }

    /**
     * 取消下载
     * @param info
     */
    public void cancleDownloading(DownloadInfo info){
        info.downloadThread.stopFlag=true;
        downloadingList.remove(info);
    }

    /**
     * 暂停下载
     * @param info
     */
    public void pauseDownloading(DownloadInfo info) {
        info.downloadThread.stopFlag=true;
    }

    /**
     * 关闭所有下载任务
     */
    public void closeAllTask() {
        for(int i=0;i<downloadingList.size();i++){
            downloadingList.get(i).downloadThread.stopFlag=true;
        }
    }

    public void saveDownloadingList() {

    }

    /**
     * 下载进度刷新，状态改变的时候回调的接口
     */
    public interface DownloadingChangeListener{
        void refreshData();
        void onHasDownloadingWarn();
    }
    public void setDownloadingListener(DownloadingChangeListener listener){
        this.mDownloadingChangeListener=listener;
    }
}