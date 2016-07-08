package com.gaojunhui.bean;

/**
 * Created by Administrator on 2016/7/1.
 */
public class MusicBean {
    private String musicName;
    private String path;

    public MusicBean(String musicName, String path) {
        this.musicName = musicName;
        this.path = path;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
