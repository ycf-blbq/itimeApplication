package com.example.jszx.itimeapplication;

import java.io.Serializable;

/**
 * Created by jszx on 2019/11/20.
 */

public class Event implements Serializable {
    private String Title;
    private String Date;
    private String Count;
   /* public Event(String title, int coverResourceId,String date) {
        setTitle(title);
        setDate(date);
        this.setCoverResourceId(coverResourceId);
    }*/

    public Event(String title, String count,String date) {
        setTitle(title);
        setDate(date);
        setCount(count);
        //private int coverResourceId;
    }
    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
    public String getCount() {
        return Count;
    }

    public void setCount(String count) {
        Count = count;
    }
}
