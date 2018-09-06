package com.example.event.countdown.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

public class Event extends BmobObject
{
    private String text;
    private BmobDate date;

    public Event()
    {
    }

    public Event(String text, BmobDate date)
    {
        this.text = text;
        this.date = date;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public BmobDate getDate()
    {
        return date;
    }

    public void setDate(BmobDate date)
    {
        this.date = date;
    }
}
