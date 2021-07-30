package com.khadim.audioplayer;

public class Song {
    private String sname;
    private String spath;
    private String duration;

    public Song(String sname, String spath,String duration) {
        this.sname = sname;
        this.spath = spath;
        this.duration = duration;
    }

    public Song() {
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getSpath() {
        return spath;
    }

    public void setSpath(String spath) {
        this.spath = spath;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
