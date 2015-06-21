/*
 * Copyright (C) 2014 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package seo.jin.hyung.g3tupv03;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import seo.jin.hyung.g3tupv03.fragments.AbstractFragment;
import seo.jin.hyung.g3tupv03.fragments.CounterFragment;
import seo.jin.hyung.g3tupv03.fragments.DisplayFragment;
import seo.jin.hyung.g3tupv03.fragments.ExerciseFragment;
import seo.jin.hyung.g3tupv03.utils.G3tUpConstants;
import seo.jin.hyung.g3tupv03.utils.G3tUpUtils;

/**
 * The main activity for the Jumping Jack application. This activity registers itself to receive
 * sensor values. Since on wearable devices a full screen activity is very short-lived, we set the
 * FLAG_KEEP_SCREEN_ON to give user adequate time for taking actions but since we don't want to
 * keep screen on for an extended period of time, there is a SCREEN_ON_TIMEOUT_MS that is enforced
 * if no interaction is discovered.
 *
 * This activity includes a {@link android.support.v4.view.ViewPager} with two pages, one that
 * shows the current count and one that allows user to reset the counter. the current value of the
 * counter is persisted so that upon re-launch, the counter picks up from the last value. At any
 * stage, user can set this counter to 0.
 *
 * What to do ?
 *  1. start timer
 *  2. when timer reaches to 0
 *      2-1 trigger alarm & vibration on phone
 *      2-2 start animation
 *  3. update exercise count
 *  4. when count reaches to goal
 *      4-1 trigger stop alarm & vibration on phone
 *      4-2 display good message
 */
public class G3tUpActivity extends Activity
        implements SensorEventListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private SensorManager sensorManager;
    private Sensor sensor;
    private long lastTime = 0;


    private float last_x, last_y, last_z;



    private int jumpCounter = 0;

    private Button btnStop;


    private int status = 0;

    private AbstractFragment fragment;


    private long timerDuration;
    private int exerciseCount;

    private String version = "v04";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        setUpViews();
        setUpThreshold();
        prepareCommunication();

        status = G3tUpConstants.COUNTER_STATE; // first fragment
        selectFragment();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);

    }


    // initialise component, in this case it just show Button for test
    private void setUpViews()
    {

        btnStop = (Button) findViewById(R.id.btnStop);
    }

    // load info from SharedPreference
    private void setUpThreshold()
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        timerDuration = Long.parseLong(preferences.getString("timerSet", "30"));
        exerciseCount = Integer.parseInt(preferences.getString("exerciseSet","5"));
        btnStop.setText(exerciseCount + " - " + version);
    }


    // determine displaying fragment based on status
    private void selectFragment()
    {
        fragment = null;
        // If CounterFragment is about to display, trigger timer as well
        if(status==G3tUpConstants.COUNTER_STATE)
        {
            fragment = new CounterFragment();
            CountDownTimer timer = new MyCountDownTimer(G3tUpConstants.SECOND*timerDuration, G3tUpConstants.SECOND);
            timer.start();
        // ExerciseFragment
        }else if(status==G3tUpConstants.EXERCISE_STATE){
            fragment = new ExerciseFragment();

        // DisplayFragment
        }else{
            fragment = new DisplayFragment();
        }
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ftx = fm.beginTransaction();
        ftx.replace(R.id.fragment_place, fragment);
        ftx.commit();
    }

    // event handler when button pressed. this is just for testing purpose
    public void clickButton(View view)
    {
        if(status==G3tUpConstants.COUNTER_STATE) {

        }else if(status == G3tUpConstants.EXERCISE_STATE) {
            increaseCount();
        }else{
            // dismiss
            Log.e(G3tUpConstants.TAG, "Dismiss Activity ?");
        }
    }

    private void increaseCount()
    {
        jumpCounter++;
        Log.e(G3tUpConstants.TAG, "Exercise - " + jumpCounter);

        if (jumpCounter >= exerciseCount) {
            fragment.stopAction();


            message = G3tUpConstants.ALARM_STOP;
            triggerActionOnPhone();



            status = G3tUpConstants.DISPLAY_STATE;
            selectFragment();
            return;
        }
        fragment.setText(jumpCounter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensorManager.registerListener(this, sensor,
                SensorManager.SENSOR_DELAY_NORMAL)) {
            if (Log.isLoggable(G3tUpConstants.TAG, Log.DEBUG)) {
                Log.d(G3tUpConstants.TAG, "Successfully registered for the sensor updates");
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        if (Log.isLoggable(G3tUpConstants.TAG, Log.DEBUG)) {
            Log.d(G3tUpConstants.TAG, "Unregistered for sensor events");
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        Sensor g3tupSensor = event.sensor;

        if( (status==G3tUpConstants.EXERCISE_STATE) && (fragment!=null) && (g3tupSensor.getType() == Sensor.TYPE_ACCELEROMETER) )
        {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            long currentTime = System.currentTimeMillis();

            long diffTime = currentTime - lastTime;
//            if( diffTime > 100)
            if (diffTime > 150)
            {
                lastTime = currentTime;
                float speed = Math.abs(x + y + z - last_x - last_y - last_z)/diffTime * 10000;
//                Toast.makeText(this, "Speed - " + speed, Toast.LENGTH_LONG);
                if(speed > G3tUpConstants.SHAKE_THRESHOLD)
                {

                    // increase number
                    G3tUpUtils.vibrate(this);
                    increaseCount();



                    // update on phone
                    message = jumpCounter + "";
                    triggerActionOnPhone();
                }
                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    // This is timer class for CounterFragment
    public class MyCountDownTimer extends CountDownTimer
    {
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
//            Log.e(G3tUpConstants.TAG, "" + millisUntilFinished/1000);
            fragment.setText("" + millisUntilFinished/1000);

        }

        @Override
        public void onFinish() {
//            fragment.setText("Finished");
            message = G3tUpConstants.ALARM_START;
            triggerActionOnPhone();

            status = G3tUpConstants.EXERCISE_STATE;
            selectFragment();
        }
    }





    ///////////////////////////////////////////////////////////////////////////////

    GoogleApiClient googleApiClient;
    String message;

    private void prepareCommunication()
    {
        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(this);
        builder.addApi(Wearable.API);
        builder.addConnectionCallbacks(this);
        builder.addOnConnectionFailedListener(this);
        googleApiClient = builder.build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.e(G3tUpConstants.TAG, "onConnected()");
//        triggerActionOnPhone();
    }

    public void triggerActionOnPhone()
    {
        if(googleApiClient!=null && googleApiClient.isConnected())
        {
            new SendMessageToDataLayer(G3tUpConstants.COMMUNICATIO_PATH, message).start();
            Log.e(G3tUpConstants.TAG, "Communication thread has triggered");
        }else{
            Log.e(G3tUpConstants.TAG, "Failed to send messages");
        }
    }


    public class SendMessageToDataLayer extends Thread
    {
        String path;
        String message;

        public SendMessageToDataLayer(String path, String message)
        {
            this.path = path;
            this.message = message;
        }

        @Override
        public void run() {
            NodeApi.GetConnectedNodesResult nodesResult = Wearable.NodeApi.getConnectedNodes(googleApiClient).await();
            for(Node node : nodesResult.getNodes())
            {
                MessageApi.SendMessageResult messageResult = Wearable.MessageApi.sendMessage(googleApiClient, node.getId(), path, message.getBytes()).await();
                if(messageResult.getStatus().isSuccess())
                {
                    Log.e(G3tUpConstants.TAG, "Sent to " + node.getDisplayName());
                    Log.e(G3tUpConstants.TAG, "Node Id is " + node.getId());
                    Log.e(G3tUpConstants.TAG, "Node size is " + nodesResult.getNodes().size());
                }else{
                    Log.e(G3tUpConstants.TAG, "Error while sending message");
                }
            }
        }
    }








    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
        Log.e(G3tUpConstants.TAG, "onStop()");
    }

    @Override
    protected void onStop() {
        if(googleApiClient!=null && googleApiClient.isConnected())
        {
            googleApiClient.disconnect();
        }
        super.onStop();
        Log.e(G3tUpConstants.TAG, "onStop()");
    }
}
