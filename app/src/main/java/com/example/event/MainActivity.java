package com.example.event;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.event.adapter.EventListAdapter;
import com.example.event.base.BaseActivity;
import com.example.event.bean.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static com.example.event.util.util.dip2px;

public class MainActivity extends BaseActivity
{

    private List<Event> events = new ArrayList<Event>();
    private Date date  = null;

    @BindView(R.id.toolbar)Toolbar toolbar;

    @BindView(R.id.event_list_rv)RecyclerView eventListRv;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        findAllEvent();
    }

    private void initView()
    {
        setSupportActionBar(toolbar);
        eventListRv.setLayoutManager(new LinearLayoutManager(this));
        eventListRv.addItemDecoration(new SpaceItemDecoration(dip2px(this, 8), dip2px(this, 8)));
    }

    private void findAllEvent()
    {
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH) + 1;
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        String start = mYear + "-" + mMonth + "-" + mDay + " 00:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = sdf.parse(start);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        BmobQuery<Event> q1 = new BmobQuery<Event>();
        q1.addWhereGreaterThanOrEqualTo("date",new BmobDate(date));
        q1.order("date,createdAt");
        q1.findObjects(new FindListener<Event>()
        {
            @Override
            public void done(List<Event> list, BmobException e)
            {
                if(e == null){
                    events.clear();
                    if(list.size() > 0){
                        events.addAll(list);
                    }
                    BmobQuery<Event> q2 = new BmobQuery<Event>();
                    q2.addWhereLessThan("date",new BmobDate(date));
                    q2.order("-date,createdAt");
                    q2.findObjects(new FindListener<Event>()
                    {
                        @Override
                        public void done(List<Event> list, BmobException e)
                        {
                            if(e == null){
                                if(list.size() > 0){
                                    events.addAll(list);
                                }
                                eventListRv.setAdapter(new EventListAdapter(events));
                            }else{
                                Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                            }
                        }
                    });
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()){
            case R.id.event_add:
                Intent intent = new Intent(this, EventAddActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
