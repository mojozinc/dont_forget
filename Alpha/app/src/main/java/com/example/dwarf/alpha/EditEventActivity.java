package com.example.dwarf.alpha;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

import com.example.dwarf.alpha.util.date;
import com.example.dwarf.alpha.util.Date_and_Time;

public class EditEventActivity extends Activity {
    DbManager db;
    private Long id_;
    private int STATE;
    Intent intent;
    TextView date, time,title;
    EditText title_mod;
    String title_,title_mod_, date_, time_,left_butt_,right_butt_,Head_;
    Button left_butt,right_butt;
    Date_and_Time now;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
        STATE=0;
        setTitle("Edit");
        db = new DbManager(getApplicationContext());
        db.open();
        intent = getIntent();
        String id = intent.getStringExtra("id_");
        id_ = Long.parseLong(id);
        extract_data();
        title.setText(title_);
        date.setText(date_);
        time.setText(time_);
        System.out.print(id);
    }
    public void extract_data() {
        date = (TextView) findViewById(R.id.edit_date);
        time = (TextView) findViewById(R.id.edit_time);
        title= (TextView) findViewById(R.id.edit_title);
        title_mod = (EditText) findViewById(R.id.edit_title_mod);
        left_butt=(Button)findViewById(R.id.left_butt);
        right_butt=(Button)findViewById(R.id.right_butt);
        left_butt_=left_butt.getText().toString();
        right_butt_=right_butt.getText().toString();
        title_ = "" + intent.getStringExtra("title_");
        date_ = "" + intent.getStringExtra("date_");
        time_ = "" + intent.getStringExtra("time_");
        now=new Date_and_Time(date_,time_);
        title_mod_=title_;


        // title.setText("title"); date.setText("date"); time.setText("time");
    }

    final int TIME_PICKER = 1;
    final int DATE_PICKER = 2;

    @Override
    protected Dialog onCreateDialog(int c) {
        switch (c) {
            case TIME_PICKER:
                return new TimePickerDialog(this,timeListener, get_H(time_), get_M(time_),true);
            case DATE_PICKER:
                Toast.makeText(getApplicationContext(),""+now.d+"-"+now.m+"-"+now.y,Toast.LENGTH_LONG).show();
                DatePickerDialog dpd=new DatePickerDialog(this,dateListener,now.y,now.m-1,now.d);
                return dpd;

        }

        return null;
    }
    private TimePickerDialog.OnTimeSetListener timeListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hour, int min) {
                    time_ = pad(hour) + ":" + pad(min);
                    time.setText(time_);
                }
            };
    private DatePickerDialog.OnDateSetListener dateListener=
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    date_=pad(dayOfMonth)+"-"+pad(monthOfYear+1)+"-"+year;
                    date.setText(date_);
                }
            };
    public void right_button_clicked(View v) {
        if (STATE==0) {
            right_butt_="CANCEL";
            left_butt_="SAVE";
            right_butt.setText(right_butt_);
            left_butt.setText(left_butt_);
            setListenerToDateAndTimeTextView();
            STATE=1;
         }
        else if(STATE==1){
            go_back();
        }
    }
    private void setListenerToDateAndTimeTextView() {
        time.setClickable(true);
        date.setClickable(true);
        LayoutParams lp=(LayoutParams)title.getLayoutParams(),tmp;
        tmp=new LayoutParams(0,0);
        title.setLayoutParams(tmp);
        title.setVisibility(View.INVISIBLE);
        title_mod.setLayoutParams(lp);
        title_mod.setVisibility(View.VISIBLE);
        title_mod.setText(title_mod_);
    }
    public void time_clicked(View v){
        showDialog(TIME_PICKER);
    }
    public void date_clicked(View v){
        showDialog(DATE_PICKER);
    }
    public void save() {
        title_mod_=title_mod.getText().toString();
        db.update(id_, title_mod_, time_, date_);
        db.close();
        go_back();
    }
    public void left_button_clicked(View v){
        if(STATE==0)
            delete_record();
        else if(STATE==1)
            save();
    }
    public void delete_record() {
        db.delete(id_);
        db.close();
        go_back();
    }

    public void go_back() {
        Intent home = new Intent(this, MainActivity.class);
        home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home);
    }

    private String pad(int c) {
        if (c < 10)
            return "0" + c;
        return "" + c;
    }

    private int get_H(String t) {
        if (t.charAt(0) == '0')
            return ((int) t.charAt(1));
        else
            return Integer.parseInt(t.split(":")[0]);
    }

    private int get_M(String t) {
        if (t.charAt(3) == '0')
            return ((int) t.charAt(4));
        else
            return Integer.parseInt(t.split(":")[1]);
    }
    public void debug_1(View v){
        Toast.makeText(getApplicationContext(),"TextView is present here",Toast.LENGTH_SHORT);

    }
}
