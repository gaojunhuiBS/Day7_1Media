package com.education.mymediaplayertest;

/**
 * Created by zhonghang on 16/7/1.
 */

public class MusicBean {
    private String displayName;
    private String path;

    public MusicBean(String displayName, String path) {
        this.displayName = displayName;
        this.path = path;
    }

    public MusicBean() {
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
