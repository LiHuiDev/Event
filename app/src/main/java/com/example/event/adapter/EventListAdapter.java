package com.example.event.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.event.R;
import com.example.event.bean.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

        private ViewHolder(View view)
        {
            super(view);
            v = view;
            text = (TextView) view.findViewById(R.id.text);
            type = (TextView) view.findViewById(R.id.type);
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
//        //item点击编辑
//        holder.v.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int position = holder.getAdapterPosition();
//            }
//        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        Event event = eventList.get(position);

        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date utilDate = sdf.parse(event.getDate().getDate());

            int interval = getIntervalDays(utilDate, new Date());

            if(interval < 0) {
                holder.text.setText("距离" + event.getText());
                holder.type.setText("已经");
                holder.distance.setText(String.valueOf(Math.abs(interval))+" 天");
            } else if(interval > 0) {
                holder.text.setText("距离"+event.getText());
                holder.type.setText("还有");
                holder.distance.setText(String.valueOf(Math.abs(interval))+" 天");
            } else {
                holder.text.setText(event.getText()+"就在今天！");
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

    //计算两个时间的间隔(过去的为负数，还没到的为正数，今天为0)
    public static int getIntervalDays(Date date, Date today){
        long ei = date.getTime() - today.getTime();
        return (int)(ei / (1000*60*60*24));
    }
}
