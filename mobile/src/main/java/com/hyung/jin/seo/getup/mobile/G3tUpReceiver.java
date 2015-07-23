package com.hyung.jin.seo.getup.mobile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.util.Log;

import com.hyung.jin.seo.getup.mobile.utils.G3tUpConstants;

import java.io.IOException;

/**
 * Created by jinseo on 2015. 6. 23..
 */
public class G3tUpReceiver extends BroadcastReceiver {

    // Vibration
    static Vibrator vibrator;
    private long pattern[] = {10, 200, 0};

    // Sound
    static AudioManager audioManager;
    static MediaPlayer mediaPlayer;
    private int originalVolume;
    private int maxVolume;
    private Uri alarmSound;

    @Override
    public void onReceive(Context context, Intent intent) {
        setupAlarm(context);
        String action = intent.getStringExtra(G3tUpConstants.ACTION);
        boolean isSound = intent.getBooleanExtra(G3tUpConstants.SOUND, false);
        boolean isVibration = intent.getBooleanExtra(G3tUpConstants.VIBRATION, false);
        Log.d(G3tUpConstants.TAG, "Action is " + action + ", Sound : " + isSound + ",  Vibration : " + isVibration);
        // if action is start then start vibration
        if((action!=null) && (action.equalsIgnoreCase(G3tUpConstants.ALARM_START)))
        {
            if(isVibration) {
                vibrator.vibrate(pattern, 0);
            }
            if(isSound){
                originalVolume = audioManager.getStreamVolume(AudioManager.STREAM_ALARM);
                mediaPlayer.reset();
                // max volume
                audioManager.setStreamVolume(AudioManager.STREAM_ALARM, maxVolume, 0);
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                try{
                    mediaPlayer.setDataSource(context, alarmSound);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    Log.e(G3tUpConstants.TAG, "Failed to prepare media player to play alarm \n" + e);
                }
                mediaPlayer.start();
            }
            Log.d(G3tUpConstants.TAG, "Alarm starts");

        }else if((action!=null) && (action.equalsIgnoreCase(G3tUpConstants.ALARM_STOP))){
            if(isVibration) {
                vibrator.cancel();
            }

            if(isSound){
                audioManager.setStreamVolume(AudioManager.STREAM_ALARM, originalVolume, 0);
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
            }

            Log.d(G3tUpConstants.TAG, "Alarm stops");

        }else{// if((action!=null) && (action.equalsIgnoreCase(G3tUpConstants.RELEASE))){
//            release();
            Log.d(G3tUpConstants.TAG, "Nothing happens");

        }
    }

    private void setupAlarm(Context context)
    {
        if(vibrator == null) {
            vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        }

        if(audioManager == null){
            audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        }
        originalVolume = audioManager.getStreamVolume(AudioManager.STREAM_ALARM);
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
        alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        if(mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
    }

    private void release()
    {
        if(audioManager!=null && mediaPlayer!=null) {
            audioManager.setStreamVolume(AudioManager.STREAM_ALARM, originalVolume, 0);
            mediaPlayer.release();
        }
    }
}
