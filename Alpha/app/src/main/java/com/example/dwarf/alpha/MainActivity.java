package com.example.dwarf.alpha;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.usage.UsageEvents;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dwarf.alpha.util.PalleteMaker;
import com.example.dwarf.alpha.util.itemData;

import java.util.Calendar;

public class MainActivity extends ActionBarActivity {
    private ListView event_list;
    private DbManager myDB;
    private AlarmManager Event_proxim;
    private PendingIntent fire;
    private SimpleCursorAdapter adapter;
    final String[] from=new String[] {DataBaseHelper.KEY_TITLE,DataBaseHelper.KEY_TIME,DataBaseHelper.KEY_DATE,DataBaseHelper.KEY_ID};
    final int[] to=new int[]{R.id.event,R.id.time,R.id.date,R.id._id};
    private itemData minInfo;
    private Activity Main;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Main=this;
        event_list=(ListView)findViewById(R.id.list_of_events);
        populate_list();
        setMinAlarm();
        //todo implement swipe listview items
        View.OnTouchListener Swiper=new View.OnTouchListener() {
            private int padding=0;
            private float MaxDelWidth=0;
            private int initPadding=0;
            private int initX=0;
            private int currX=0;
            private View Curr_View=null;
            private View view=null;
            private View del;
            private int initColor=0;
            private int Down_X;
            private String _id;
            private int[] color_array;
            private int Delete_Boundary=300;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int X=(int)event.getRawX();
                int Y=(int)event.getRawY();
                MaxDelWidth=getResources().getDimension(R.dimen.abc_action_button_min_width_material);
                int Red=getResources().getColor(R.color.start_Sliding);
                int Blue=getResources().getColor(R.color.text_color);
                color_array=(new PalleteMaker(Blue,Red,200)).getPalleteArray();
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d("Touch", "Action_Down");
                        initX = (int) event.getX();
                        currX = (int) event.getX();
                        Down_X = initX;
                        Curr_View = grabView(v, X, Y);
                        view = Curr_View.findViewById(R.id.item);
                        del = Curr_View.findViewById(R.id.imageView);
                        initPadding = view.getPaddingLeft();
                        initColor = Curr_View.getDrawingCacheBackgroundColor();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.d("Touch", "Action_Move");
                        currX = (int) event.getX();
                        padding = currX - initX;

                        if (Curr_View == null) {
                            Curr_View = grabView(v, X, Y);
                            view = Curr_View.findViewById(R.id.item);
                            del = Curr_View.findViewById(R.id.imageView);
                        }

                        if (padding>10&&padding<Delete_Boundary) {
                                view.setPadding(padding, 0, 0, 0);
                                Curr_View.setBackgroundColor(Blue);
                                if(padding>MaxDelWidth+10)
                                    del.setVisibility(View.VISIBLE);
                                else if(padding<MaxDelWidth+10)
                                    del.setVisibility(View.INVISIBLE);
                                if(padding>100)
                                    Curr_View.setBackgroundColor(color_array[padding-100]);
                            }
                        if (padding >=Delete_Boundary){
                            view.setPadding(padding,0,0,0);
                            del.setVisibility(View.VISIBLE);
                            Curr_View.setBackgroundColor(Red);
                           }
                            break;
                    case MotionEvent.ACTION_CANCEL: Log.d("Touch","Action_CANCEL");
                    case MotionEvent.ACTION_UP:
                        up_or_cancel:
                        Log.d("Touch","Action_Up");
                        Log.d("Touch","Padding_UP="+padding);
                        initX=0;
                        currX=0;
                        if(padding>Delete_Boundary)
                            delete_swiped(_id);
                        padding=initPadding;
                        del.setVisibility(View.INVISIBLE);
                        view.setPadding(padding, 0, 0, 0);
                        Curr_View.setBackgroundColor(initColor);
                        Log.d("Touch","Down_X="+Down_X+"UP_X="+X);
                        if(mod(X-Down_X)<2)
                            do_what_click_listener_did(Curr_View);

                }

                return true;
            }
            private View grabView(View v,int X,int Y) {
                View tmp_v=null;
                int[] coords = new int[2];
                ListView l = (ListView) v;
                int num_of_childs = l.getChildCount(), i;
                for (i = 0; i < num_of_childs; i++) {
                    tmp_v = l.getChildAt(i);
                    tmp_v.getLocationOnScreen(coords);
                    if (Y >= coords[1] && Y <= (coords[1] + tmp_v.getHeight())) {
                        Log.d("Touch","Child grabbed index="+i);
                        break;
                    }

                }
                _id=((TextView)tmp_v.findViewById(R.id._id)).getText().toString();
                return tmp_v;
            }
            private int mod(int x){
                if(x<0)
                    return -1*x;
                return x;
            }
            private void do_what_click_listener_did(View view){
                Log.d("Touch","Custom itemClick Triggered");
                TextView _idView = (TextView) view.findViewById(R.id._id);
                TextView timeView = (TextView) view.findViewById(R.id.time);
                TextView dateView = (TextView) view.findViewById(R.id.date);
                TextView titleView = (TextView) view.findViewById(R.id.event);

                _id = _idView.getText().toString();
                String time = timeView.getText().toString();
                String date = dateView.getText().toString();
                String title = titleView.getText().toString();

                Intent edit_intent;
                edit_intent = new Intent(getApplicationContext(), EditEventActivity.class);
                edit_intent.putExtra("id_", _id);
                edit_intent.putExtra("time_", time);
                edit_intent.putExtra("date_", date);
                edit_intent.putExtra("title_", title);

                //Toast.makeText(getApplicationContext(), "" + position, Toast.LENGTH_SHORT).show();
                startActivity(edit_intent);
            }
            private void delete_swiped(String _id){
                long id=Long.parseLong(_id);
                myDB=new DbManager(getApplicationContext());
                myDB.open();
                myDB.delete(id);
                myDB.close();
                Main.finish();
                Main.startActivity(getIntent());
            }

        };
        event_list.setOnTouchListener(Swiper);
    }
    void populate_list(){
        myDB=new DbManager(this);
        myDB.open();
        Cursor cursor=myDB.fetch();
        adapter=new SimpleCursorAdapter(this,R.layout.item_layout,cursor,from,to,0);
        adapter.notifyDataSetChanged();
        event_list.setAdapter(adapter);
        minInfo=myDB.getMinInfo();
        myDB.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        Intent intent;
        switch(id) {
            case R.id.new_event:
                intent = new Intent(this, New_Event_picker.class);
                startActivity(intent);
                break;
          }
        return super.onOptionsItemSelected(item);
    }
    private void setMinAlarm(){
        int type=AlarmManager.RTC_WAKEUP;
        long triggerTime=minInfo.millis;
        Intent intent=new Intent("FIRED");
        intent.putExtra("id",minInfo.id);
        intent.putExtra("EventName",minInfo.title);
        intent.putExtra("EventTime",minInfo.time);
        fire=PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        Event_proxim=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Event_proxim.cancel(fire);
        if(triggerTime>=Calendar.getInstance().getTimeInMillis())
            {
                Event_proxim.set(type, triggerTime, fire);
                Log.d("Alarm","Alarm set for "+triggerTime);
                Log.d("Alarm","Alarm set on "+Calendar.getInstance().getTimeInMillis());
            }
        Log.d("Alarm","mininfo.millis="+minInfo.millis);
    }
    /*
    public void broadcast(View vw){
        Intent intent=new Intent("FIRED");
        intent.putExtra("id",minInfo.id);
        intent.putExtra("EventName",minInfo.title);
        intent.putExtra("EventTime",minInfo.time);
        fire=PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        Event_proxim=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Calendar now=Calendar.getInstance();
        Event_proxim.set(AlarmManager.RTC_WAKEUP,now.getTimeInMillis(),fire);
    }
    public void cancel_alarm(View v){
        Toast.makeText(this,"Current Time in millis:"+Calendar.getInstance().getTimeInMillis(),Toast.LENGTH_LONG).show();
    }*/
  }