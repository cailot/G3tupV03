package seo.jin.hyung.g3tupv03;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // initialise audioManager & vibrator
        setUpAlarm();
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


    // trigger alarm with maximum volume & vibration
    private void startAlarm()
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
//        mediaPlayer.start();

        // start vibration as well
        alarmVibration.vibrate(1000*60);

        Log.e(G3tUpConstants.TAG, "Start Alarm");
    }

    // stop alarm and vibration. rollback volume to original state
    private void stopAlarm()
    {
        audioManager.setStreamVolume(AudioManager.STREAM_ALARM, originalVolume, 0);
//        if(mediaPlayer.isPlaying())
//        {
//            mediaPlayer.stop();
//        }
        alarmVibration.cancel();

        // stop vibration as well
        alarmVibration = null;
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


//            return true;
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
}
