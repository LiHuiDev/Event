package com.example.event;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.event.adapter.EventListAdapter;
import com.example.event.base.BaseActivity;
import com.example.event.bean.Event;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MainActivity extends BaseActivity
{

    @BindView(R.id.toolbar)Toolbar toolbar;

    @BindView(R.id.event_list_rv)RecyclerView eventList;

    @BindView(R.id.event_add_fab)FloatingActionButton eventAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();
    }

    private void initView()
    {
        setSupportActionBar(toolbar);

        eventList.setLayoutManager(new LinearLayoutManager(this));
        findAllEvent();
    }

    private void findAllEvent()
    {
        BmobQuery<Event> query = new BmobQuery<Event>();
        query.order("-createdAt");
        query.findObjects(new FindListener<Event>()
        {
            @Override
            public void done(List<Event> list, BmobException e)
            {
                if(e == null){
                    eventList.setAdapter(new EventListAdapter(list));
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    @OnClick(R.id.event_add_fab)void toAddActivity()
    {
        Intent intent = new Intent(this, EventAddActivity.class);
        startActivity(intent);
    }

}
