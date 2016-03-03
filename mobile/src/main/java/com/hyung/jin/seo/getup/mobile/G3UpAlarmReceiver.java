package com.hyung.jin.seo.getup.mobile;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.hyung.jin.seo.getup.mobile.utils.G3tUpConstants;

import java.util.Calendar;

/**
 * Created by jinseo on 2016. 1. 17..
 */
public class G3UpAlarmReceiver extends WakefulBroadcastReceiver
{

    private AlarmManager alarmManager;

    private PendingIntent alarmIntent;

    private SharedPreferences preferences;

    /***************************************************
     Config Items
     **************************************************/
    private int hour, minute;

    private String[] repeatDays;

    private String exerciseTime;

    private boolean isSoundOn, isVibrationOn;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
//        Intent service = new Intent(context, G3UpAlarmService.class);
//        startWakefulService(context, service);
    }

    public void setAlarm(Context context)
    {
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, G3UpAlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        // Set the alarm's trigger time
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);

        // enable to automatically restart the alarm when the device is rebooted.
        ComponentName receiver = new ComponentName(context, G3UpBootReceiver.class);
        PackageManager packageManager = context.getPackageManager();
        packageManager.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    public void cancelAlarm(Context context)
    {
        if(alarmManager != null)
        {
            alarmManager.cancel(alarmIntent);
        }
        // disable so that it doesn't automatically restart the alarm when the device is rebooted.
        ComponentName receiver = new ComponentName(context, G3UpBootReceiver.class);
        PackageManager packageManager = context.getPackageManager();
        packageManager.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    private void getDataFromPreferences()
    {
        isSoundOn = preferences.getBoolean(G3tUpConstants.SOUND_SET, false);
        isVibrationOn = preferences.getBoolean(G3tUpConstants.VIBRATION_SET, false);
        hour = preferences.getInt(G3tUpConstants.ALARM_HOUR, 0);
        minute = preferences.getInt(G3tUpConstants.ALARM_MINUTE, 0);
        exerciseTime = preferences.getString(G3tUpConstants.EXERCISE_TIME, "");
        repeatDays = preferences.getString(G3tUpConstants.REPEAT_DAY,"").split(G3tUpConstants.SEPARATOR);
    }
}
