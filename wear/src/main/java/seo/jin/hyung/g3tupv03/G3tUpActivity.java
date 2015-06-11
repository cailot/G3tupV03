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
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import seo.jin.hyung.g3tupv03.fragments.AbstractFragment;
import seo.jin.hyung.g3tupv03.fragments.CounterFragment;
import seo.jin.hyung.g3tupv03.fragments.DisplayFragment;
import seo.jin.hyung.g3tupv03.fragments.ExerciseFragment;
import seo.jin.hyung.g3tupv03.utils.G3tUpConstants;
import seo.jin.hyung.g3tupv03.utils.GetUpUtils;

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
 *  4. when acount reaches to goal
 *      4-1 trigger stop alarm & vibration on phone
 *      4-2 display good message
 */
public class G3tUpActivity extends Activity
        implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private long mLastTime = 0;
    private boolean mUp = false;
    private int jumpCounter = 0;

    private Button btnStop;


    private int status = 0;

    private AbstractFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        setUpViews();
        status = G3tUpConstants.COUNTER_STATE; // first fragment
        selectFragment();
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
    }

    // initialise component, in this case it just show Button for test
    private void setUpViews()
    {
        btnStop = (Button) findViewById(R.id.btnStop);
    }


    // determine displaying fragment based on status
    private void selectFragment()
    {
        fragment = null;
        // If CounterFragment is about to display, trigger timer as well
        if(status==G3tUpConstants.COUNTER_STATE)
        {
            fragment = new CounterFragment();
            CountDownTimer timer = new MyCountDownTimer(G3tUpConstants.SECOND*10, G3tUpConstants.SECOND);
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
            jumpCounter++;
            Log.e(G3tUpConstants.TAG, "Stop Exercise - " + jumpCounter);

            if (jumpCounter >= 10) {
                btnStop.setText("STOP");
                fragment.stopAction();
                status = G3tUpConstants.DISPLAY_STATE;
                selectFragment();
                return;
            }
            fragment.setText(jumpCounter);
        }else{
            // dismiss
            Log.e(G3tUpConstants.TAG, "Dismiss Activity ?");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSensorManager.registerListener(this, mSensor,
                SensorManager.SENSOR_DELAY_NORMAL)) {
            if (Log.isLoggable(G3tUpConstants.TAG, Log.DEBUG)) {
                Log.d(G3tUpConstants.TAG, "Successfully registered for the sensor updates");
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
        if (Log.isLoggable(G3tUpConstants.TAG, Log.DEBUG)) {
            Log.d(G3tUpConstants.TAG, "Unregistered for sensor events");
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        detectJump(event.values[0], event.timestamp);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    /**
     * A simple algorithm to detect a successful up-down movement of hand(s). The algorithm is
     * based on the assumption that when a person is wearing the watch, the x-component of gravity
     * as measured by the Gravity Sensor is +9.8 when the hand is downward and -9.8 when the hand
     * is upward (signs are reversed if the watch is worn on the right hand). Since the upward or
     * downward may not be completely accurate, we leave some room and instead of 9.8, we use
     * GRAVITY_THRESHOLD. We also consider the up <-> down movement successful if it takes less than
     * TIME_THRESHOLD_NS.
     */
    private void detectJump(float xValue, long timestamp) {
        if ((Math.abs(xValue) > G3tUpConstants.GRAVITY_THRESHOLD)) {
            if(timestamp - mLastTime < G3tUpConstants.TIME_THRESHOLD_NS && mUp != (xValue > 0)) {
                onJumpDetected(!mUp);
            }
            mUp = xValue > 0;
            mLastTime = timestamp;
        }
    }

    /**
     * Called on detection of a successful down -> up or up -> down movement of hand.
     */
    private void onJumpDetected(boolean up) {
        // we only count a pair of up and down as one successful movement
        if (up) {
            return;
        }
        jumpCounter++;
        setCounter(jumpCounter);


        ///////////////////////////////////////////////////////
        //  stop when reaching the goal, for example 10 times
        ///////////////////////////////////////////////////////
        //countDownFragment.stopExercise();
    }

    /**
     * Updates the counter on UI, saves it to preferences and vibrates the watch when counter
     * reaches a multiple of 10.
     */
    private void setCounter(int i) {
        GetUpUtils.saveCounterToPreference(this, i);
        if (i > 0 && i % 10 == 0) {
            GetUpUtils.vibrate(this, 0);
        }
    }

    // This is timer class for CounterFragment
    public class MyCountDownTimer extends CountDownTimer
    {
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            Log.e(G3tUpConstants.TAG, "" + millisUntilFinished/1000);
            fragment.setText("" + millisUntilFinished/1000);

        }

        @Override
        public void onFinish() {
//            fragment.setText("Finished");
            status = G3tUpConstants.EXERCISE_STATE;
            selectFragment();
        }
    }
}
