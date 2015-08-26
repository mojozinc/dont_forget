package com.example.dwarf.alpha;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="myDB.db";
    public static final String TABLE_NAME="event_list";
    public static final String KEY_TIME="time";
    public static final String KEY_DATE="date";
    public static final String KEY_TITLE="event_title";
    public static final String KEY_ID="_id";
    public static final String COUNT_DOWN="countDown";
    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE="CREATE TABLE "+TABLE_NAME+"("+KEY_TIME+" TIME,"+KEY_DATE+" DATE,"+KEY_TITLE+" TEXT,"+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+COUNT_DOWN+" LONG);";
        Log.i("Helper","create table query is:"+CREATE_TABLE);
        db.execSQL(CREATE_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
        }

}