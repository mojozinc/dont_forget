package com.example.dwarf.alpha.util;


import java.util.Calendar;
import java.util.Date;
public class Date_and_Time extends date{
    public int hour,min;
    String time_;
    public Date_and_Time(String date,String Time){
        super(date);
        String []hm=new String[2];
        hm=Time.split(":");
        hour=Integer.parseInt(hm[0]);
        min=Integer.parseInt(hm[1]);
    }
    public Date_and_Time(int h,int m,int dd,int mm,int yyyy){
        super(dd,mm,yyyy);
        time_=pad(h)+":"+pad(m);
        hour=h;
        min=m;
    }
    public long getCountDownMillis(){
        Calendar cal=Calendar.getInstance();
        cal.set(y,m,d,hour,min);
        return cal.getTimeInMillis();
    }
    private int is_leap(int year){
        if(year%100==0&&year%400==0)
            return 1;
        else if(year%4==0)
            return 1;
        return 0;
    }
}
