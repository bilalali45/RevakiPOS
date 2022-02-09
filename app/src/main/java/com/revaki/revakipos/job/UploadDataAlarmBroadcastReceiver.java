package com.revaki.revakipos.job;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class UploadDataAlarmBroadcastReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 101;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, UploadDataService.class);
        serviceIntent.putExtra("startCode", UploadDataService.AUTO_START);
        context.startService(serviceIntent);
    }

    public static void setAlarm(Context context, long trigger, long interval) {
        Intent intent = new Intent(context, UploadDataAlarmBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, trigger, interval, pendingIntent);
    }

    public static void cancelAlarm(Context context) {
        Intent intent = new Intent(context, UploadDataAlarmBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }


}