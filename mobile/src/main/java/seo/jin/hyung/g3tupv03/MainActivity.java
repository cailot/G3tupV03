package seo.jin.hyung.g3tupv03;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;

import seo.jin.hyung.g3tupv03.utils.G3tUpConstants;


public class MainActivity extends ActionBarActivity {

    private TextView flagText;

    private Vibrator alarmVibration;
    private AudioManager audioManager;
    private int originalVolume;
    private int maxVolume;
    private Uri alarmSound;
    private MediaPlayer mediaPlayer;

    private boolean isSoundOn, isVibrationOn;

    private String version = "v01";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // initialise audioManager & vibrator
        setUpAlarm();
        // load config items from SharedPreference
        setUpConfig();
        flagText = (TextView) findViewById(R.id.flagText);
        String message = getIntent().getStringExtra(G3tUpConstants.FLAG_FROM_CLIENT);
        if(message==null || message.equalsIgnoreCase(""))
        {
            message = "Nothing comes from client";
        }else if(message.equalsIgnoreCase(G3tUpConstants.ALARM_START)){
            // start ring & vibration
            startAlarm();
        }else if(message.equalsIgnoreCase(G3tUpConstants.ALARM_STOP)){
            // stop ring & vibration
            stopAlarm();
        }
        flagText.setText(message);
    }

    // initialise vibration & alarm components
    private void setUpAlarm()
    {
        alarmVibration = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        originalVolume = audioManager.getStreamVolume(AudioManager.STREAM_ALARM);
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
        alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        mediaPlayer = new MediaPlayer();
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

    // trigger alarm with maximum volume & vibration
    private void startAlarm()
    {

        if(isSoundOn)
        {
            originalVolume = audioManager.getStreamVolume(AudioManager.STREAM_ALARM);
            mediaPlayer.reset();
            // max volume
            audioManager.setStreamVolume(AudioManager.STREAM_ALARM, maxVolume, 0);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
            try{
                mediaPlayer.setDataSource(getApplicationContext(), alarmSound);
                mediaPlayer.prepare();
            } catch (IOException e) {
                Log.e(G3tUpConstants.TAG, "Failed to prepare media player to play alarm \n" + e);
            }
            mediaPlayer.start();
        }
        // start vibration as well
        if(isVibrationOn) {
            alarmVibration.vibrate(1000 * 60 * 5);
            // Start immediately
            // Vibrate for 200 milliseconds
            // Sleep for 500 milliseconds
//            long[] pattern = { 10, 200, 0 };
//            alarmVibration.vibrate(pattern, 0);

        }
        Log.e(G3tUpConstants.TAG, "Start Alarm");
    }

    // stop alarm and vibration. rollback volume to original state
    private void stopAlarm()
    {
        if(isSoundOn) {
            audioManager.setStreamVolume(AudioManager.STREAM_ALARM, originalVolume, 0);
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
        }

        if(isVibrationOn) {
            flagText.setText("Got stop sign");
            Log.e(G3tUpConstants.TAG, "Got stop sign");
            alarmVibration.cancel();
            alarmVibration = null;
//            alarmVibration.cancel();
            flagText.setText("STOPPPPPP!!!!");
            Log.e(G3tUpConstants.TAG, "Stopped");

//            long[] pattern = {0, 100, 100};
//            alarmVibration.vibrate(pattern, 1);
//            alarmVibration.vibrate(500);
            // stop vibration as well
//            alarmVibration = null;
        }
        Log.e(G3tUpConstants.TAG, "Stop Alarm");
    }


    public void startClick(View view)
    {
        startAlarm();
    }

    public void stopClick(View view)
    {
        stopAlarm();
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
        if(audioManager!=null && mediaPlayer!=null) {
            audioManager.setStreamVolume(AudioManager.STREAM_ALARM, originalVolume, 0);
            mediaPlayer.release();
        }

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
