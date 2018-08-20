package com.example.event.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.event.EventEditActivity;
import com.example.event.R;
import com.example.event.bean.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

import static com.example.event.util.util.showToast;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder>
{

    private Context context;
    private List<Event> eventList;

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        View v;
        TextView text;
        TextView type;
        TextView distance ;
        TextView dayTv ;

        private ViewHolder(View view)
        {
            super(view);
            v = view;
            text = (TextView) view.findViewById(R.id.text);
            type = (TextView) view.findViewById(R.id.type);
            dayTv = (TextView) view.findViewById(R.id.day_tv);
            distance = (TextView) view.findViewById(R.id.distance);
        }
    }

    public EventListAdapter(List<Event>events)
    {
        eventList = events;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if (context == null){
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_item_event, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        //item点击编辑
        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();

                Event event = eventList.get(position);
                Intent intent = new Intent(context, EventEditActivity.class);
                intent.putExtra("event", event);//数据传给详情页
                context.startActivity(intent);
            }
        });
        //长按删除
        holder.v.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View view)
            {
                new AlertDialog.Builder(context)
                        .setTitle("删除笔记")
                        .setMessage("确定删除数据吗？删除后不可恢复呦！请谨慎操作！！！")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int position = holder.getAdapterPosition();
                                Event event = eventList.get(position);
                                //数据库里删除
                                event.delete(event.getObjectId(), new UpdateListener()
                                {
                                    @Override
                                    public void done(BmobException e)
                                    {
                                        if(e == null){
                                            showToast(context, "删除成功");
                                        }else{
                                            showToast(context, "删除失败");
                                        }
                                    }
                                });
                                //列表里删除元素
                                eventList.remove(position);
                                notifyItemRemoved(position);
                                dialogInterface.dismiss();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
                return false;
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        Event event = eventList.get(position);
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date utilDate = sdf.parse(event.getDate().getDate());
            long date = utilDate.getTime()/(1000*60*60*24);
            long today = new Date().getTime()/(1000*60*60*24);
            long interval = date - today + 1;

            if(interval < 0) {
                holder.text.setText(event.getText());
                holder.type.setText(" 已经");
                holder.distance.setText(String.valueOf(Math.abs(interval)));
                holder.distance.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
                holder.dayTv.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
            } else if(interval > 0) {
                holder.text.setText("距离 "+event.getText());
                holder.type.setText(" 还有");
                holder.distance.setText(String.valueOf(Math.abs(interval)));
                holder.distance.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
                holder.dayTv.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccentDark));
            } else {
                holder.text.setText(event.getText()+" 就在今天！！！！");
            }
        } catch (ParseException e){
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount()
    {
        return eventList.size();
    }

}
