package com.hyung.jin.seo.getup.mobile;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;
import com.hyung.jin.seo.getup.mobile.utils.G3tUpConstants;

/**
 * Created by jinseo on 2015. 6. 11..
 */
public class G3tUpListenerService extends WearableListenerService
{
    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if(messageEvent.getPath().equals(G3tUpConstants.COMMUNICATIO_PATH))
        {
            final String message = new String(messageEvent.getData());
            Log.d(G3tUpConstants.TAG, "@@@ Server got data - " + message);
            Intent startIntent = new Intent(this, MainActivity.class);
            startIntent.putExtra(G3tUpConstants.FLAG_FROM_CLIENT, message);
            startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startIntent);
            Log.d(G3tUpConstants.TAG, "==== MainActivity has been started ====");

        }else{
            super.onMessageReceived(messageEvent);
        }
    }
}
