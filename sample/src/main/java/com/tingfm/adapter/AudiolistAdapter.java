package com.tingfm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.tingfm.download.DownLoadManager;
import com.tingfm.download.DownLoadTask;
import com.tingfm.download.DownloadInfo;
import com.tingfm.Asset;
import com.tingfm.tingfm.R;
import com.tingfm.utils.Utils;

import java.util.List;

/**
 * Created by david on 15/11/26.
 */
public class AudiolistAdapter extends BaseAdapter{
    private Context mContext;
    private List<Asset> list;

    public AudiolistAdapter(List<Asset> list,Context mContext){
        this.list=list;
        this.mContext=mContext;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Asset getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView( final int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        Asset info=getItem(position);

        if(convertView==null){
            final LayoutInflater inflater=LayoutInflater.from(mContext);
            convertView=inflater.inflate(R.layout.list_item_audio,null);
            holder=new ViewHolder();
            holder.title= (TextView) convertView.findViewById(R.id.textvie_audio_listitem_title);
            holder.createdAt= (TextView) convertView.findViewById(R.id.textview_audio_listitem_createtime);
            holder.playTimes= (TextView) convertView.findViewById(R.id.textvie_audio_listitem_playtimes);
            holder.duration= (TextView) convertView.findViewById(R.id.textvie_audio_listitem_duration);
            holder.comments= (TextView) convertView.findViewById(R.id.textvie_audio_listitem_comments);
            holder.operationButton= (Button) convertView.findViewById(R.id.button_audio_listitem_download);
            holder.operationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Todo  下载点击
                    Asset info = (Asset) v.getTag();
                    /**下载器的入口 todo*/

                    DownLoadTask task=new DownLoadTask(info.trackId,info.title,info.downloadUrl,mContext);
                    DownLoadManager.with().submit(task);
                }
            });
//            convertView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Asset info = (Asset) v.getTag(R.id.list_item_tag_1);
//                    Intent i=new Intent(mContext, PlayAudioActivity.class);
//                    i.putExtra("audioTitle", info.title);
//                    i.putExtra("playUrl",info.playUrl64);
//                    mContext.startActivity(i);
//                    System.out.println(position+"   ");
//                }
//            });
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        holder.operationButton.setTag(info);
        convertView.setTag(R.id.list_item_tag_1,info);
        if(info!=null) {
            holder.title.setText(info.title);
            holder.createdAt.setText(Utils.convertTimeDifferent(info.createdAt));
            holder.playTimes.setText(Utils.convertWithTenthousand(info.playtimes));
            holder.duration.setText(Utils.formatTime(info.duration));
            holder.comments.setText(info.comments+" ");
        }
        return convertView;
    }
    class ViewHolder{
        TextView title,createdAt,playTimes,duration,comments;
        Button operationButton;
    }
}
