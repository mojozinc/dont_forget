package com.example.dwarf.alpha.util;

public class date {
    public int d,m,y;
    public String date_;
    public date(String d_t){
        date_=d_t;
        String[] dmy=new String[3];
        dmy=d_t.split("-");
        d=Integer.parseInt(dmy[0]);
        m=Integer.parseInt(dmy[1])-1;
        y=Integer.parseInt(dmy[2]);
    }
    public date(int d_,int m_,int y_){
        d=d_;
        m=m_;
        y=y_;
        date_=pad(d)+"-"+pad(m)+"-"+pad(y);
    }
    protected String pad(int c) {
        if (c < 10)
            return "0" + c;
        return "" + c;
    }
}
