package com.example.dwarf.alpha;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.dwarf.alpha.util.Date_and_Time;

import java.util.Calendar;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

public class New_Event_picker extends Activity {
    private DbManager db;
    private String time;
    private String date;
    private String title;
    Date_and_Time DT;
    int hour,minute,yyyy,mm,dd;
    final int TIME_DIALOG_ID=999;
    final int DATE_DIALOG_ID=998;
    TextView time_display,date_display;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_event_picker_version);
        setTitle("Add Event");
        setCurrentTimeAndDate();
    }
    private void setCurrentTimeAndDate(){
        Calendar c=Calendar.getInstance();
        hour=c.get(Calendar.HOUR_OF_DAY);
        minute=c.get(Calendar.MINUTE);
        yyyy=c.get(Calendar.YEAR);
        mm=c.get(Calendar.MONTH);
        dd=c.get(Calendar.DATE);
        time_display=(TextView)findViewById(R.id.time_display);
        date_display=(TextView)findViewById(R.id.date_display);
        time_display.setText("" + pad(hour) + ":" + pad(minute));
        date_display.setText(""+pad(dd)+"-"+pad(mm+1)+"-"+pad(yyyy));

    }
    public void show_timePicker(View v){
        showDialog(TIME_DIALOG_ID);
    }
    public void show_datePicker(View v){
        showDialog(DATE_DIALOG_ID);
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID:
                // set time picker as current time
                return new TimePickerDialog(this,timePickerListener, hour, minute,true);
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this,datePickerListener,yyyy,mm,dd);
        }
        return null;
    }
    private TimePickerDialog.OnTimeSetListener timePickerListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int selectedHour,
                                      int selectedMinute) {
                    hour = selectedHour;
                    minute = selectedMinute;
                    // set current time into textview
                    time_display.setText("" + pad(hour) + ":" + pad(minute));
                }
            };
    private DatePickerDialog.OnDateSetListener datePickerListener=
            new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view,int year,int month,int day){
                dd=day;
                yyyy=year;
                mm=month;
                date_display.setText(""+pad(day)+"-"+pad(month+1)+"-"+pad(year));
            }
            };
    public void add_event(View v){
        saveData();
        exit();
    }
    private String pad(int c){
        if(c<10)
            return "0"+c;
        return ""+c;

    }
    private void saveData()
    {//this function mapped to Done button
        //Extracting Data
        //{
        EditText Title=(EditText)findViewById(R.id.title_);
        date=date_display.getText().toString();
        time=time_display.getText().toString();
        title=Title.getText().toString();
        DT=new Date_and_Time(date,time);
        //}
        //Inserting a new row with freshly extracted data
        db=new DbManager(this);
        db.open();
        db.insert(title,time,date);
        db.close();

    }
    public void go_back(View v){//this function mapped to Cancel button
        exit();
    }
    private void exit(){//Return to homepage
        Intent home=new Intent(New_Event_picker.this,MainActivity.class);
        home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home);
    }
}
