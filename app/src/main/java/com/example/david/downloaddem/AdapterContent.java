package com.example.david.downloaddem;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.download.manager.DownLoadManager;
import com.download.manager.DownLoadTask;
import com.download.manager.DownloadInfo;
import com.download.utils.Utils;

import java.util.ArrayList;

/**
 * Created by david on 15/11/23.
 */
public class AdapterContent extends BaseAdapter {

    private Context mContext;
    private ArrayList<DownloadInfo> list;
    public AdapterContent(Context mContext,ArrayList<DownloadInfo> list){
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
            convertView=inflater.inflate(R.layout.listview_item,null);
            holder=new ViewHolder();
            holder.downloadButton= (Button) convertView.findViewById(R.id.download_button);
            holder.progressBar= (ProgressBar) convertView.findViewById(R.id.downloadbar);
            holder.textViewFileName= (TextView) convertView.findViewById(R.id.fileName);
            holder.totalSize= (TextView) convertView.findViewById(R.id.total_size);
            holder.downloadSize= (TextView) convertView.findViewById(R.id.finfish_size);
            holder.speedView= (TextView) convertView.findViewById(R.id.speed);
            holder.pauseButton= (Button) convertView.findViewById(R.id.pause);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        final DownloadInfo info=getItem(position);
        holder.textViewFileName.setText(info.fileName);
        holder.progressBar.setMax(info.totalSize);
        holder.progressBar.setProgress(info.downloadSize);
        holder.speedView.setText(info.speed + "kb/s");
        holder.downloadSize.setText(Utils.revertBtoMB(info.downloadSize)+ "M/");
        holder.totalSize.setText(Utils.revertBtoMB(info.totalSize)+"M");
        holder.pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info.downloadThrad.setStopFlag(true);
            }
        });
        holder.downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info.downloadThrad=new DownLoadTask(info.urlString, info.mHandler, mContext);
                DownLoadManager.with().submit(info.downloadThrad);
            }
        });
        return convertView;
    }


    class ViewHolder{
        Button downloadButton,pauseButton;
        ProgressBar progressBar;
        TextView textViewFileName,totalSize,downloadSize,speedView;
    }

}
