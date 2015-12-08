package com.tingfm.utils;

/**
 * Created by david on 15/11/27.
 */
/**
 * 定义了 通用内容的常量，可以任何类中使用
 */
public class Constants {



    private Constants(){

    }
    public static final String SP_NAME = "app";

    //版本号
    public static final String  SP_KEY_WELCOME_SHOW_VER = "wsv";

    /**
     * json数据返回码
     */
    public static final int TASK_RESULT_OK = 0;


    //---点击的是哪一种类型的item
    public static final String TAG_DISCOVER_RECOMMEND_ALBUM = "albumRecommend:";

    //推荐
    public static final String KEY_RECOMMENDS = "recommends";
    //广告
    public static final String KEY_FOCUSE_IMAGES = "focusimages";

    //专辑详情
    public static final String KEY_ALBUM_DETAIL = "albumdetail";
    //专辑详情-曲目列表
    public static final String KEY_ALBUM_TRACK_LIST = "albumdetai_tracklist";


    public static final String KEY_ALBUMID = "albumid";

    public static final String KEY_TRACKID = "trackid";


    //曲目列表
    public static final String TRACK_LIST = "tracklist";

//    //当前点击时-要播放的位置
//    public static final String CURRENT_PLAY_POSITION = "curplayposition";




    //-------------------------有关播放的各种KEY

    /**
     * 用户拖动seekbar发送的进度广播
     */
    public static final String CAST_ACTION_SEEKBAR_PROCESS = "cast.action.seekbar.process";

    /**
     * 一首音乐播放完成的广播
     */
    public static final String CAST_ACTION_MUSIC_COMPLETE = "cast.action.music.complete";

    /**
     * service从网络缓存完毕，开始播放
     */
    public static final String CAST_ACTION_MUSIC_START = "cast.action.music.start";

    /**
     * 音乐播放进度广播 - 由Service发向播放界面
     */
    public static final String CAST_ACTION_MUSIC_PROGRESS = "cast.action.music.progress";

    /**
     * 从网络缓存的进度
     */
    public static final String CAST_ACTION_BUFFERING_UPDATE = "cast.action.buffering.update";

    /**
     * 网络缓存进度的标识
     */
    public static final String INTENT_EXTRA_BUFFERING_UPDATE = "extra.buffering.update";

    /**
     * 是否播放另一首音乐的标识
     */
    public static final String INTENT_EXTRA_CHANGE_MUSIC = "extra.change.music";

    /**
     * 当前音乐播放位置
     */
    public static final String INTENT_EXTRA_MUSIC_POSITION = "extra.music.position";

    /**
     * 音乐的总长度
     */
    public static final String INTEXT_EXTRA_MUSIC_TOTAL_LEN = "extra.music.total.len";

    /**
     * 当前播放长度
     */
    public static final String INTENT_EXTRA_MUSIC_CUR_LEN = "extra.music.cur.len";
}
