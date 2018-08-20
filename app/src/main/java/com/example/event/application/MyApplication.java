package com.example.event.application;

import android.app.Application;

import com.example.event.R;

import cn.bmob.v3.Bmob;

public class MyApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        Bmob.initialize(this, getString(R.string.bmob_appkey));
    }

}
