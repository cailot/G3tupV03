package seo.jin.hyung.g3tupv03;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import seo.jin.hyung.g3tupv03.utils.G3tUpConstants;


public class MainActivity extends ActionBarActivity {

    private TextView flagText;

    private boolean isSoundOn, isVibrationOn;

    private String version = "v09";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // load config items from SharedPreference
        setUpConfig();
        flagText = (TextView) findViewById(R.id.flagText);
        String message = getIntent().getStringExtra(G3tUpConstants.FLAG_FROM_CLIENT);
        if(message==null || message.equalsIgnoreCase(""))
        {
            message = "Nothing comes from client";
        }else if(message.equalsIgnoreCase(G3tUpConstants.ALARM_START)){
            // start ring & vibration
            triggerAlarm(G3tUpConstants.ALARM_START);
        }else if(message.equalsIgnoreCase(G3tUpConstants.ALARM_STOP)){
            // stop ring & vibration
            triggerAlarm(G3tUpConstants.ALARM_STOP);
        }
        flagText.setText(message);
    }

    private void setUpConfig()
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        isSoundOn = preferences.getBoolean("soundSet", false);
        isVibrationOn = preferences.getBoolean("vibrationSet", false);
    }

    @Override
    protected void onResume() {
        super.onResume();
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

//    public void startClick(View view)
//    {
//        triggerAlarm(G3tUpConstants.ALARM_START);
//    }
//
//    public void stopClick(View view)
//    {
//        triggerAlarm(G3tUpConstants.ALARM_STOP);
//    }

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
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        StringBuilder builder = new StringBuilder();
        builder.append(version + "\n");
        builder.append("\nTimer : " + preferences.getString("timerSet","NULL"));
        builder.append("\nExercise : " + preferences.getString("exerciseSet","NULL"));
        builder.append("\nSound : " + preferences.getBoolean("soundSet", false));
        builder.append("\nVibratons : " + preferences.getBoolean("vibrationSet", false));
        builder.append("\nButton : " + preferences.getBoolean("buttonSet", false));
        flagText.setText(builder.toString());
        Log.e(G3tUpConstants.TAG, builder.toString());
    }

}
