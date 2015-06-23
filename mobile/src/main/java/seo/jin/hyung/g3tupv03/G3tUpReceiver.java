package seo.jin.hyung.g3tupv03;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;

import seo.jin.hyung.g3tupv03.utils.G3tUpConstants;

/**
 * Created by jinseo on 2015. 6. 23..
 */
public class G3tUpReceiver extends BroadcastReceiver {

    static Vibrator vibrator;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(vibrator==null) {
            vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        }
        String action = intent.getStringExtra(G3tUpConstants.ACTION);
        Log.d(G3tUpConstants.TAG, "Action is " + action);
        // if action is start then start vibration
        if((action!=null) && (action.equalsIgnoreCase(G3tUpConstants.ALARM_START)))
        {
            long pattern[] = {10, 200, 0};
            vibrator.vibrate(pattern, 0);
            Log.d(G3tUpConstants.TAG, "Alarm starts");

        }else if((action!=null) && (action.equalsIgnoreCase(G3tUpConstants.ALARM_STOP))){
            vibrator.cancel();
            Log.d(G3tUpConstants.TAG, "Alarm stops");

        }else{
            Log.d(G3tUpConstants.TAG, "Nothing happens");

        }
    }
}
