package com.tingfm;

import android.app.Application;

import java.util.ArrayList;

/**
 * Created by david on 15/11/27.
 */
public class FMApplication extends Application {


    public static FMApplication INSTANCE;


    /**
     * 全局播放列表
     */
    private ArrayList<String> playList;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
    }

    public ArrayList<String> getPlayList() {
        return playList;
    }

    public void setPlayList(ArrayList<String> playList) {
        this.playList = playList;
    }



}