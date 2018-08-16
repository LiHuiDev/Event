package com.example.event;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.event.base.BaseActivity;
import com.example.event.bean.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class EventAddActivity extends BaseActivity
{
    @BindView(R.id.edit_text)EditText editText;

    @BindView(R.id.date_text)TextView date;

    @OnClick(R.id.date_text)void datepicker(){
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        date.setText(year+"-"+(month+1)+"-"+dayOfMonth);
                    }
                },
                mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    @OnClick(R.id.save_btn)void save(){
        Event event = new Event();
        event.setText(editText.getText().toString());
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//yyyy-mm-dd, 会出现时间不对, 因为小写的mm是代表: 秒
            Date utilDate = sdf.parse(date.getText().toString());
            event.setDate(new BmobDate(utilDate));
            event.save(new SaveListener<String>()
            {
                @Override
                public void done(String s, BmobException e)
                {
                    if(e == null){
                        Toast.makeText(EventAddActivity.this, "保存成功！", Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                    }
                }
            });
        }catch (ParseException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_add);
        ButterKnife.bind(this);
    }
}
