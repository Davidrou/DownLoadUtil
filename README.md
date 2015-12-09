# DownLoadUtil
###This is an Android DownLoad Utils
###这是我的下载框架，目前尚未完善，谢谢！
###主要实现了：断点续传，任务排队及常见的下载操作。
###如果想使用该框架，可以直接下载downloadLibrary ,依赖即可。
### ![](https://github.com/Davidrou/DownLoadUtil/raw/master/show.gif)

##使用方法：
###1.导入downloadlibrary Moudle 到你的工程，并建立依赖<br>
###2.新建下载任务：<br>
 `DownLoadTask task=new DownLoadTask(info.trackId,info.title,info.downloadUrl,mContext);`<br>
   
   参数为：自定义的下载任务的ID  自定义的下载文件的名字 下载文件的URL 上下文对象
###3.提交下载任务：如果有任务在下载默认加入等待队列
   `DownLoadManager.with().submit(task);`
###4.获取已提交的下载任务列表 （包括暂停中 排队中 下载中的任务，不包括已经取消的下载任务 ）
`DownLoadManager.with().getDownloadingList()`<br>
返回值：`ArrayList <DownloadInfo>`
通过该DownloadInfo你可以获取：<br>
   1.下载任务的文件的大小 totalSize<br>
   2.下载速度            speed<br>
   3.已经下载的大小       downloadSize<br>
   4.文件名              fileName<br>
   5.任务的状态           status <br>
       DownloadInfo提供一下静态变量用于比较
       
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
       
当任务取消或者下载完成后，该任务DownloadInfo 自动从List中移除

 

###5.实时更新下载进度（注意下面两个回调在异步线程执行）
实现 DownLoadManager.DownloadingChangeListener接口，重写<br>
1.void refreshData();<br> 
      下载信息有变化 就是DownloadInfo有变化，可以重新获取下载速度，已下载大小，这样就实现了更新<br>
2.void onHasDownloadingWarn();<br>
      提交重复任务时，回调<br>
###6.取消，暂停，继续 (参数均为 DownloadInfo)
暂停 ：DownLoadManager.with().pauseDownloading(info);<br>
继续：DownLoadManager.with().continueTask(info);<br>
取消：DownLoadManager.with().cancleDownloading(info);<br>
###7.注意在关闭APP时，关闭正在下载的任务
DownLoadManager.with().closeAllTask();



      
         

