package seo.jin.hyung.g3tupv03;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import seo.jin.hyung.g3tupv03.utils.G3tUpConstants;


public class MainActivity extends ActionBarActivity {

    private TextView flagText;

//    private Vibrator alarmVibration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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



    private void startAlarm()
    {
//        if(alarmVibration==null) {
          Vibrator  alarmVibration = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//        }
        // pattern doesn't work with cancel ???


        // start without delay
        // vibrate for 100 milliseconds
        // sleep for 1000 miliseconds
        //long[] pattern = {0, 100, 1000};
        //long[] pattern = {0,
//                100, 1000,
//                300, 1000,
//                100, 1000,
//                300, 1000,
//                100, 1000,
//                300, 1000,
//                100, 1000,
//                300, 1000,
//                100, 1000,
//                300, 1000,
//                100, 1000,
//                300, 1000,
//                100, 1000,
//                300, 1000,
//                100, 1000,
//                300, 1000,
//                100, 1000,
//                300, 1000,
//                100, 1000,
//                300, 1000,
//                100, 1000,
//                300, 1000,
//                100, 1000,
//                300, 1000,
//                100, 1000,
//                300, 1000,
//                100, 1000,
//                300, 1000,
//                100, 1000,
//                300, 1000,
//                100, 1000,
//                300, 1000,
//                100, 1000,
//                300, 1000,
//                100, 1000,
//                300, 1000,
//                100, 1000,
//                300, 1000,
//                100, 1000,
//                300, 1000,
//                100, 1000,
//                300, 1000,
//                100, 1000,
//                300, 1000
//        };
        // 0 means to repeat indefinitely
       // alarmVibration.vibrate(pattern, 0);
//        alarmVibration.vibrate(pattern, -1);
        alarmVibration.vibrate(1000*60);
        Log.e(G3tUpConstants.TAG, "Start vibration");
    }

    private void stopAlarm()
    {
//        if(alarmVibration!=null)
//        {
        Vibrator  alarmVibration = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        alarmVibration.cancel();
//        }

//        else{
//            alarmVibration = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//            alarmVibration.cancel();
//        }
        alarmVibration = null;
        Log.e(G3tUpConstants.TAG, "Stop vibration");
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
