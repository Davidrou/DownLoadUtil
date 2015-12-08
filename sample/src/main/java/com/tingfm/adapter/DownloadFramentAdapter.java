package com.tingfm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tingfm.download.DownLoadManager;
import com.tingfm.download.DownloadInfo;
import com.tingfm.download.DownloadUtils;
import com.tingfm.tingfm.R;

import java.util.ArrayList;

/**
 * Created by david on 15/11/27.
 */
public class DownloadFramentAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<DownloadInfo> list;
    public DownloadFramentAdapter(Context mContext, ArrayList<DownloadInfo> list){
        this.mContext=mContext;
        this.list=list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public DownloadInfo getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView==null){
            LayoutInflater inflater= LayoutInflater.from(mContext);
            convertView=inflater.inflate(R.layout.listitem_downloading,null);
            holder=new ViewHolder();
            holder.canceldButton = (Button) convertView.findViewById(R.id.button_downloadfragment_item_cancel);
            holder.progressBar= (ProgressBar) convertView.findViewById(R.id.progress_downloadfragment_item_downloadbar);
            holder.textViewFileName= (TextView) convertView.findViewById(R.id.textview_downloadfragment_item_fileName);
            holder.totalSize= (TextView) convertView.findViewById(R.id.textview_downloadfragment_item_total_size);
            holder.downloadSize= (TextView) convertView.findViewById(R.id.textview_downloadfragment_item_finfish_size);
            holder.speedView= (TextView) convertView.findViewById(R.id.textview_downloadfragment_item_speed);
            holder.pauseButton= (Button) convertView.findViewById(R.id.button_downloadfragment_item_pause);
            holder.statusView= (TextView) convertView.findViewById(R.id.textview_downloadfragment_item_status);
            convertView.setTag(holder);
            holder.pauseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DownloadInfo info = (DownloadInfo) v.getTag(R.id.list_item_tag_1);
                    if (((Integer) v.getTag()) == 0)
                        DownLoadManager.with().pauseDownloading(info);
                    else {
                        DownLoadManager.with().continueTask(info);
                    }
                }
            });
            holder.canceldButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DownloadInfo info = (DownloadInfo) v.getTag(R.id.list_item_tag_1);
                    DownLoadManager.with().cancleDownloading(info);
                }
            });
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        final DownloadInfo info=getItem(position);
        holder.textViewFileName.setText(info.fileName);
        holder.progressBar.setMax(info.totalSize);
        holder.progressBar.setProgress(info.downloadSize);
        holder.speedView.setText(info.speed + "kb/s");
        holder.downloadSize.setText(DownloadUtils.revertBtoMB(info.downloadSize)+ "M/");
        holder.totalSize.setText(DownloadUtils.revertBtoMB(info.totalSize) + "M");
        if(info.status==DownloadInfo.DOWNLOAD_STATUS_QUEUE){
            holder.statusView.setText("排队中");
            holder.progressBar.setVisibility(View.GONE);
            holder.pauseButton.setText("暂停");
            holder.pauseButton.setTag(new Integer(0));
        }else if(info.status==DownloadInfo.DOWNLOAD_STATUS_PAUSE){
            holder.statusView.setText("暂停中");
            holder.progressBar.setVisibility(View.GONE);
            holder.pauseButton.setText("继续");
            holder.pauseButton.setTag(new Integer(1));
        }else if(info.status==DownloadInfo.DOWNLOAD_STATUS_DOWNLOADING){
            holder.statusView.setText("下载中");
            holder.progressBar.setVisibility(View.VISIBLE);
            holder.pauseButton.setText("暂停");
            holder.pauseButton.setTag(new Integer(0));
        }else if(info.status==DownloadInfo.DOWNLOAD_STATUS_FINISH){
            holder.statusView.setText("下载完成");
            holder.progressBar.setVisibility(View.GONE);
        }else {
            holder.statusView.setText("");
            holder.progressBar.setVisibility(View.GONE);
        }

        holder.pauseButton.setTag(R.id.list_item_tag_1,info);
        holder.canceldButton.setTag(R.id.list_item_tag_1,info);
        return convertView;
    }
    class ViewHolder{
        Button canceldButton,pauseButton;
        ProgressBar progressBar;
        TextView textViewFileName,totalSize,downloadSize,speedView,statusView;
    }
}

