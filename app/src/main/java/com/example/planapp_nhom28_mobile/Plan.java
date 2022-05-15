package com.example.planapp_nhom28_mobile;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "Plan")
public class Plan {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="uid")
    public int uid;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "content")
    private String content;
    @ColumnInfo(name = "dc")
    private String dc;
    @ColumnInfo(name = "time")
    private String time;
    @ColumnInfo(name = "img")
    private String img;
    @ColumnInfo(name = "loai")
    private String loai;

    public Plan()
    {

    }

    public Plan(String title, String content)
    {
        this.title=title;
        this.content=content;
    }

    public Plan(String title, String content, String dc, String time, String img) {
        this.title = title;
        this.content = content;
        this.dc = dc;
        this.time = time;
        this.img = img;
    }

    public Plan(String title, String content, String dc, String time, String img, String loai) {
        this.title = title;
        this.content = content;
        this.dc = dc;
        this.time = time;
        this.img = img;
        this.loai = loai;
    }

    public Plan(String img) {
        this.img = img;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDc() {
        return dc;
    }

    public void setDc(String dc) {
        this.dc = dc;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getLoai() {
        return loai;
    }

    public void setLoai(String loai) {
        this.loai = loai;
    }
}

