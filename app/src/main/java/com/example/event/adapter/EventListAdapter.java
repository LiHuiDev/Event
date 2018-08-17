package com.example.event.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
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
