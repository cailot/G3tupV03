package com.hyung.jin.seo.getup.mobile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by jinseo on 2016. 1. 17..
 */
public class G3UpBootReceiver extends BroadcastReceiver
{
    G3UpAlarmReceiver alarmReceiver = new G3UpAlarmReceiver();
    @Override
    public void onReceive(Context context, Intent intent)
    {
        if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
        {
            alarmReceiver.setAlarm(context);
        }
    }
}
