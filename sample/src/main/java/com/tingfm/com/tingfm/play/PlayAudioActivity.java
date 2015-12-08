package com.tingfm.com.tingfm.play;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.tingfm.FMApplication;
import com.tingfm.tingfm.R;
import com.tingfm.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by david on 15/11/27 copy from chenLiu
 */
public class PlayAudioActivity extends Activity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    //--一大波UI视图
    private ImageButton backIb;
    private TextView titletv;
    private TextView playTimesTv;
    private ImageButton alarmClockIb;
    private ImageView albumIconIv;
    private TextView playListTv;
    private TextView playHistoryTv;

    private ImageButton notifyPlayIb;
    private ImageButton notifyPreIb;
    private ImageButton notifyNextIb;


    SeekBar seekBar;// 进度条
    TextView nowTimeTv;//当前时间
    TextView totalTimeTv;//总时间


    // -- 听书者的 信息
    private ImageView artistCover;
    private TextView artisName;
    private TextView subDetail;
    private TextView albumDeacrip;


    private SimpleDateFormat dateFormat;
    private LocalBroadcastManager lbManager;// 本地广播管理器
    private String audioName;
    private String playUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_audio);
        handleIntent();
        initView();
        setData();
        playMusic(0,true);
    }

    private void setData() {
        titletv.setText(audioName);
    }

    private void handleIntent() {
        Intent i=getIntent();
        audioName=i.getStringExtra("audioTitle");
        playUrl=i.getStringExtra("playUrl");
    }

    private void initView() {
        backIb = (ImageButton) findViewById(R.id.ac_play_back);
        titletv = (TextView) findViewById(R.id.ac_play_title);
        playTimesTv = (TextView) findViewById(R.id.ac_play_times_tv);
        alarmClockIb = (ImageButton) findViewById(R.id.ac_play_alarm_clock);
        albumIconIv = (ImageView) findViewById(R.id.ac_play_album_icon);
        playListTv = (TextView) findViewById(R.id.ac_play_list);
        playHistoryTv = (TextView) findViewById(R.id.ac_play_history);
        seekBar = (SeekBar) findViewById(R.id.ac_play_seekbar);
        nowTimeTv = (TextView) findViewById(R.id.a_play_nowtime_tv);
        totalTimeTv = (TextView) findViewById(R.id.a_play_totaltime_tv);
        notifyPlayIb = (ImageButton) findViewById(R.id.ac_notify_play_ib);
        //刚进来时，播放按钮设置为不可用
        notifyPlayIb.setEnabled(false);
        notifyPreIb = (ImageButton) findViewById(R.id.ac_notify_pre_ib);
        notifyNextIb = (ImageButton) findViewById(R.id.ac_notify_next_ib);
        artistCover = (ImageView) findViewById(R.id.play_artist_cover);
        artisName = (TextView) findViewById(R.id.play_artist_name);
        subDetail = (TextView) findViewById(R.id.play_sub_detail);
        albumDeacrip = (TextView) findViewById(R.id.play_ablum_descrip);
        backIb.setOnClickListener(this);
        alarmClockIb.setOnClickListener(this);
        playListTv.setOnClickListener(this);
        playHistoryTv.setOnClickListener(this);
        notifyPlayIb.setOnClickListener(this);
        notifyPreIb.setOnClickListener(this);
        notifyNextIb.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
    /**
     *  通知服务播放对应位置的音乐
     * @param position
     */
    private void playMusic(int position, boolean playNew){
        ArrayList<String> playList=new ArrayList<>();
        playList.add(playUrl);
        FMApplication.INSTANCE.setPlayList(playList);
        Intent serviceIntent = new Intent(this, PlayService.class);
        serviceIntent.putExtra(Constants.INTENT_EXTRA_CHANGE_MUSIC, playNew);
        if(playNew){
            if(position > -1 && position < FMApplication.INSTANCE.getPlayList().size())
                serviceIntent.putExtra(Constants.INTENT_EXTRA_MUSIC_POSITION, position);
        }
        startService(serviceIntent);
    }

}
