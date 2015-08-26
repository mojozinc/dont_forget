package com.example.dwarf.alpha.util;

public class itemData {
    public String title,time,date;
    public int id;
    public long minutes;
    public long millis;
    public itemData(String title,String time,String date,int id_,long mins){
        this.title=title;
        this.time=time;
        this.date=date;
        id=id_;
        minutes=mins;
    }
    public itemData(){
        this.title="";
        this.time="";
        this.date="";
        id=0;
        minutes=0;
    }
}
