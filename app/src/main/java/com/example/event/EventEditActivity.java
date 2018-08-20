package com.example.event;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import cn.bmob.v3.listener.UpdateListener;

import static com.example.event.util.util.showToast;
import static com.example.event.util.util.stringToCalendar;

public class EventEditActivity extends AppCompatActivity
{

    private Event event;

    @BindView(R.id.edit_text)EditText editText;

    @BindView(R.id.date_text)TextView date;

    @BindView(R.id.delete_btn)Button deleteBtn;

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
        String textString = editText.getText().toString();
        String dateString = date.getText().toString();

        if (("").equals(textString) || ("").equals(dateString))
        {
            Toast.makeText(this, "请填写完整信息", Toast.LENGTH_SHORT).show();
        } else {
            Event eventNew = new Event();
            eventNew.setText(textString);
            try{
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date utilDate = sdf.parse(dateString);
                eventNew.setDate(new BmobDate(utilDate));
                eventNew.update(event.getObjectId(), new UpdateListener()
                {
                    @Override
                    public void done(BmobException e)
                    {
                        if(e == null){
                            Toast.makeText(EventEditActivity.this, "保存成功！", Toast.LENGTH_SHORT).show();
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

    }

    @OnClick(R.id.delete_btn) void deleteEvent(){
        event.delete(event.getObjectId(), new UpdateListener()
        {
            @Override
            public void done(BmobException e)
            {
                if(e == null){
                    showToast(EventEditActivity.this, "删除成功");
                    finish();
                }else{
                    showToast(EventEditActivity.this, "删除失败");
                }
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_add);

        ButterKnife.bind(this);

        event = (Event) getIntent().getSerializableExtra("event");

        editText.setText(event.getText());
        try{
            Calendar calendar = stringToCalendar(event.getDate().getDate());
            String dateString = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
            date.setText(dateString);
        } catch (ParseException e){
            e.printStackTrace();
        }
        deleteBtn.setVisibility(View.VISIBLE);
    }

}
