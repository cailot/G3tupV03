package com.hyung.jin.seo.getup.mobile;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.hyung.jin.seo.getup.R;
import com.hyung.jin.seo.getup.mobile.utils.G3tUpConstants;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import static com.hyung.jin.seo.getup.R.drawable.toast;


public class G3UpMobileActivity extends ActionBarActivity {

    /***************************************************
     System Managers
     **************************************************/
    private AlarmManager alarmManager;

    private NotificationManager notificationManager;

//    private Calendar alarmTime;

    private PendingIntent alarmIntent;

    /***************************************************
     UI Components
     **************************************************/
    private TextView flagText;

    private ImageView imageView, soundImage, vibrationImage;

    private AnimationDrawable animationDrawable;

    /***************************************************
     Config Items
     **************************************************/
    private int hour, minute;

    private String[] repeatDays;

    private String exerciseTime;

    private boolean isSoundOn, isVibrationOn;

    private String version = "v1";


    SharedPreferences preferences;
    SharedPreferences.OnSharedPreferenceChangeListener listener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // register system manager
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // load config items from SharedPreference
        setUpConfig();
        // build layout
        setUpUI();
        // display setting value
        showSetting();


        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        registerPreferenceListener();
        /*
        String message = getIntent().getStringExtra(G3tUpConstants.FLAG_FROM_CLIENT);
        if(message==null || message.equalsIgnoreCase(""))
        {
            message = "Nothing comes from client";
        }else if(message.equalsIgnoreCase(G3tUpConstants.ALARM_START)){
            // start ring & vibration
            startAlarm();
        }else if(message.equalsIgnoreCase(G3tUpConstants.ALARM_STOP)){
            // stop ring & vibration
            cancelAlarm();
        }
        */
//        showAd();


    }

    private void registerPreferenceListener() {
        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                // cancel existing alarm
                cancelAlarm();
                // reload changed config items
                setUpConfig();
                // display updated values
                showSetting();
                // set alarm based on updated value
                startAlarm();
            }
        };
        preferences.registerOnSharedPreferenceChangeListener(listener);
    }

    private void startAlarm()
    {
        animationDrawable.start();
        triggerAlarm(G3tUpConstants.ALARM_START, getAlarmTime());
    }

    private void cancelAlarm()
    {
        animationDrawable.stop();
        //triggerAlarm(G3tUpConstants.ALARM_STOP);
        triggerAlarm(G3tUpConstants.ALARM_STOP, getAlarmTime());
    }

    public void cancelAlarm(View v)
    {
        cancelAlarm();
    }

    private Calendar getAlarmTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        return calendar;
    }

    private void setUpUI() {
        imageView = (ImageView) findViewById(R.id.imageAnimation);
        imageView.setBackgroundResource(R.drawable.animation_image);
        animationDrawable = (AnimationDrawable) imageView.getBackground();
        soundImage = (ImageView) findViewById(R.id.soundImage);
        vibrationImage = (ImageView) findViewById(R.id.vibrationImage);
        flagText = (TextView) findViewById(R.id.showInfo);
    }

    public void imageClick(View view) {
        TextView tv = new TextView(getApplicationContext());
        tv.setText("Toggle can be done under Settings Menu");
        tv.setHeight(80);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(16);
        tv.setTextColor(Color.CYAN);
        LinearLayout ll = new LinearLayout(getApplicationContext());
        ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        ll.setBackgroundResource(toast);
        ll.setPadding(30, 0, 30, 0);
        ll.setGravity(Gravity.CENTER);
        ll.addView(tv);
        Toast t = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
        t.setGravity(Gravity.CENTER, 0, 0);
        t.setView(ll);
        t.show();
    }
    private void showAd() {
        AdView adView = (AdView)findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private void setUpConfig()
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        isSoundOn = preferences.getBoolean(G3tUpConstants.SOUND_SET, false);
        isVibrationOn = preferences.getBoolean(G3tUpConstants.VIBRATION_SET, false);
        hour = preferences.getInt(G3tUpConstants.ALARM_HOUR, 0);
        minute = preferences.getInt(G3tUpConstants.ALARM_MINUTE, 0);
        exerciseTime = preferences.getString(G3tUpConstants.EXERCISE_TIME, "");
        repeatDays = preferences.getString(G3tUpConstants.REPEAT_DAY,"").split(G3tUpConstants.SEPARATOR);
    }

//    private void triggerAlarm(String action)
//    {
//        Intent intent = new Intent(this, G3tUpReceiver.class);
//        intent.putExtra(G3tUpConstants.ACTION, action);
//        intent.putExtra(G3tUpConstants.SOUND, isSoundOn);
//        intent.putExtra(G3tUpConstants.VIBRATION, isVibrationOn);
//
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent);
//        Log.d(G3tUpConstants.TAG, "start alarm  -  sound : " + isSoundOn + " , vibration : " + isVibrationOn);
//    }


    private void triggerAlarm(String action, Calendar calendar)
    {
        Intent intent = new Intent(this, G3tUpReceiver.class);
        intent.putExtra(G3tUpConstants.ACTION, action);
        intent.putExtra(G3tUpConstants.SOUND, isSoundOn);
        intent.putExtra(G3tUpConstants.VIBRATION, isVibrationOn);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        //alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        Log.d(G3tUpConstants.TAG, "start alarm  -  sound : " + isSoundOn + " , vibration : " + isVibrationOn + "  set at " + new Date());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
        if (id == R.id.action_settings) {

            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void showSetting()
    {
        if(isSoundOn)
        {
            soundImage.setBackgroundResource(R.drawable.m_sound_on);
        }else{
            soundImage.setBackgroundResource(R.drawable.m_sound_off);
        }
        if(isVibrationOn)
        {
            vibrationImage.setBackgroundResource(R.drawable.m_vibration_on);
        }else{
            vibrationImage.setBackgroundResource(R.drawable.m_vibration_off);
        }

        StringBuilder builder = new StringBuilder();
        builder.append("1. Timer : " + hour + " : " + minute);
        builder.append("\n2. Repeat : " + Arrays.toString(repeatDays));
        builder.append("\n3. Exercise Time : " + exerciseTime);
        builder.append("\n4. Sound : " + isSoundOn);
        builder.append("\n5. Vibratons : " + isVibrationOn);
        Log.d(G3tUpConstants.TAG, builder.toString());
    }
}
