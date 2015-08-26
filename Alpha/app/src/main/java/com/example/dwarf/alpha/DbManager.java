package com.example.dwarf.alpha;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;
import com.example.dwarf.alpha.util.Date_and_Time;
import com.example.dwarf.alpha.util.itemData;

import android.content.Context;

import java.util.Calendar;

public class DbManager {
    private SQLiteDatabase db;
    private DataBaseHelper db_helper;
    private Context context;//To save the instance of the current activity the DataBase is activated
    private static int row_count=0;
    public DbManager(Context c){
        context=c;
    }
    public void open(){
        db_helper=new DataBaseHelper(context);
        db = db_helper.getReadableDatabase();
    }
    public void close(){
        db_helper.close();
    }
    public void insert(String title,String time,String date){
        row_count++;
        Date_and_Time DT=new Date_and_Time(date,time);
        long count_down=DT.getCountDownMillis();
        ContentValues values=new ContentValues();
        values.put(DataBaseHelper.KEY_TITLE,title);
        values.put(DataBaseHelper.KEY_TIME,time);
        values.put(DataBaseHelper.KEY_DATE,date);
        values.put(DataBaseHelper.COUNT_DOWN,count_down);
        db.insert(DataBaseHelper.TABLE_NAME,null,values);
    }
    public Cursor fetch(){
        //todo insert a query that puts past events below others
        Cursor c=db.rawQuery("SELECT * FROM "+DataBaseHelper.TABLE_NAME+" ORDER BY "+DataBaseHelper.COUNT_DOWN+" ASC;",null);
        c.moveToFirst();
        c.moveToPosition(1);
        return c;
    }
    public Cursor fetch(long id){
        return db.rawQuery("SELECT * FROM "+DataBaseHelper.TABLE_NAME+" WHERE "+DataBaseHelper.KEY_ID+"=?"+id+";",null);
    }
    public itemData getMinInfo(){
        itemData min_data=new itemData();

        Cursor c=db.rawQuery("SELECT * FROM "+DataBaseHelper.TABLE_NAME+" WHERE "+DataBaseHelper.COUNT_DOWN+" >= "+Calendar.getInstance().getTimeInMillis()+" ORDER BY "+DataBaseHelper.COUNT_DOWN+" ASC;",null);
       // Cursor c= db.rawQuery("SELECT * FROM "+DataBaseHelper.TABLE_NAME+" ORDER BY "+DataBaseHelper.COUNT_DOWN+" ASC;",null);

        if(c.moveToFirst()){
            min_data.title=c.getString(c.getColumnIndex(DataBaseHelper.KEY_TITLE));
            min_data.time=c.getString(c.getColumnIndex(DataBaseHelper.KEY_TIME));
            min_data.date=c.getString(c.getColumnIndex(DataBaseHelper.KEY_DATE));
            min_data.id=Integer.parseInt(c.getString(c.getColumnIndex(DataBaseHelper.KEY_ID)));
            min_data.millis=Long.parseLong(c.getString(c.getColumnIndex(DataBaseHelper.COUNT_DOWN)));
            Log.d("database","minutes saved in itemInfo:"+min_data.minutes);
            Log.d("database","actuall minutes in db:"+c.getString(c.getColumnIndex(DataBaseHelper.COUNT_DOWN)));
        }
        return min_data;
    }
    public void update(long id,String title,String time,String date){
        Date_and_Time DT=new Date_and_Time(date,time);
        long count_down=DT.getCountDownMillis();
        ContentValues values=new ContentValues();
        values.put(DataBaseHelper.KEY_TITLE,title);
        values.put(DataBaseHelper.KEY_TIME,time);
        values.put(DataBaseHelper.KEY_DATE,date);
        values.put(DataBaseHelper.COUNT_DOWN,count_down);
        Log.i("art","countDown"+count_down);
        db.update(DataBaseHelper.TABLE_NAME,values,DataBaseHelper.KEY_ID+" = "+id,null);
    }
    public void delete(long id){
        db.delete(DataBaseHelper.TABLE_NAME,DataBaseHelper.KEY_ID+"="+id,null);
        row_count--;
    }
}
