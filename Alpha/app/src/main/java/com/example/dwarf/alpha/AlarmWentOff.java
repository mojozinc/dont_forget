package com.example.dwarf.alpha;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class AlarmWentOff extends BroadcastReceiver {
    String Eventname;
    String triggerTime;
    public AlarmWentOff() {
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        Vibrator v=(Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(1000);
        Eventname=intent.getStringExtra("EventName");
        triggerTime=intent.getStringExtra("EventTime");
        Toast.makeText(context,Eventname+"\n"+triggerTime,Toast.LENGTH_LONG).show();
        issueNotif(context);
       // alarmActivity(context);

    }

    private void alarmActivity(Context context){
        Intent alarm=new Intent(context,ALARMActivity.class);
        alarm.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(alarm);
    }
    private void issueNotif(Context context){
        NotificationCompat.Builder builder=new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(Eventname)
                .setContentText(notification_message());
        NotificationManager notifManage=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notifManage.notify(5,builder.build());
    }
    private String notification_message(){
        String msg="At "+triggerTime;
        return msg;
    }
    private String pad(long x){
        if(x<10)
            return "0"+x;
        else
            return ""+x;
    }
}
