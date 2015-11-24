package com.download.manager;

import android.content.Context;
import android.os.Message;
import android.widget.Toast;

import com.example.david.downloaddem.MainActivity;

/**
 * Created by david on 15/11/23.
 */
public class DownloadInfo {

    private Context mContext;
   public  String urlString;
    public String fileName;
    public int totalSize;
    public int downloadSize;
    public long speed;
    public DownLoadTask downloadThrad;
    public android.os.Handler mHandler=new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    totalSize=msg.getData().getInt("size");
                    MainActivity activity=(MainActivity)mContext;
                    activity.mAdapter.notifyDataSetChanged();
                    break;
                case 2:
                    downloadSize=msg.getData().getInt("size");
                    speed=msg.getData().getLong("speed");
                    MainActivity activity2=(MainActivity)mContext;
                    activity2.mAdapter.notifyDataSetChanged();
                    break;
                case 3:
                    Toast.makeText(mContext, "服务器被火烧了。。。", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };
    public DownloadInfo(Context mContext,String fileName,String urlString){
        this.fileName=fileName;
        this.urlString=urlString;
        this.mContext=mContext;
    }

}
