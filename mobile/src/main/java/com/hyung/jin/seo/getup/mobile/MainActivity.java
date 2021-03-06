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

import static com.hyung.jin.seo.getup.R.drawable.toast;


public class MainActivity extends ActionBarActivity {

    /***************************************************
         System Managers
     **************************************************/
    private AlarmManager alarmManager;

    private NotificationManager notificationManager;


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

        String message = getIntent().getStringExtra(G3tUpConstants.FLAG_FROM_CLIENT);
        if(message==null || message.equalsIgnoreCase(""))
        {
            message = "Nothing comes from client";
        }else if(message.equalsIgnoreCase(G3tUpConstants.ALARM_START)){
            // start ring & vibration
            animationDrawable.start();
            triggerAlarm(G3tUpConstants.ALARM_START);
        }else if(message.equalsIgnoreCase(G3tUpConstants.ALARM_STOP)){
            // stop ring & vibration
            animationDrawable.stop();
            triggerAlarm(G3tUpConstants.ALARM_STOP);
        }
//        flagText.setText("[" + version + "]" + "\t" + message);

        showAd();
    }

    private void setUpUI() {
        imageView = (ImageView) findViewById(R.id.imageAnimation);
        imageView.setBackgroundResource(R.drawable.animation_image);
        animationDrawable = (AnimationDrawable) imageView.getBackground();
        soundImage = (ImageView) findViewById(R.id.soundImage);
//        soundImage.setBackgroundResource(R.drawable.animation_image);
        vibrationImage = (ImageView) findViewById(R.id.vibrationImage);
//        vibrationImage.setBackgroundResource(R.drawable.animation_image);
        flagText = (TextView) findViewById(R.id.showInfo);
//        animationDrawable.start();
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

    @Override
    protected void onResume() {
        super.onResume();
        setUpConfig();
        showSetting();
    }

    private void triggerAlarm(String action)
    {
        Intent intent = new Intent(this, G3tUpReceiver.class);
        intent.putExtra(G3tUpConstants.ACTION, action);
        intent.putExtra(G3tUpConstants.SOUND, isSoundOn);
        intent.putExtra(G3tUpConstants.VIBRATION, isVibrationOn);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent);
        Log.d(G3tUpConstants.TAG, "start alarm  -  sound : " + isSoundOn + " , vibration : " + isVibrationOn);
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
//        if(audioManager!=null && mediaPlayer!=null) {
//            audioManager.setStreamVolume(AudioManager.STREAM_ALARM, originalVolume, 0);
//            mediaPlayer.release();
//        }
//        triggerAlarm(G3tUpConstants.RELEASE);
        super.onDestroy();
    }

    private void showSetting()
    {
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
//        boolean isSound = preferences.getBoolean("soundSet", false);
//        boolean isVibration = preferences.getBoolean("vibrationSet", false);

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
//        builder.append(version + "\n");
        builder.append("1. Timer : " + hour + " : " + minute);
        builder.append("\n2. Repeat : " + Arrays.toString(repeatDays));
        builder.append("\n3. Exercise Time : " + exerciseTime);
        builder.append("\n4. Sound : " + isSoundOn);
        builder.append("\n5. Vibratons : " + isVibrationOn);
        flagText.setText(builder.toString());
//
//
//
        Log.e(G3tUpConstants.TAG, builder.toString());
    }
//    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
//        MultiDex.install(this);
//    }
}
