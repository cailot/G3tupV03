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

package seo.jin.hyung.g3tupv03.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import seo.jin.hyung.g3tupv03.R;

/**
 * A simple fragment for showing the count
 */
public class CounterFragment extends Fragment {

    private TextView bodyText;


    private CountDownTimer countDownTimer;
    private final long startTime = 10*1000;
    private final long interval = 1*1000;


/*
    private boolean up = false;
    private Drawable downDrawable;
    private Drawable upDrawable;
    private Timer exerciseTimer;
    private Handler exerciseHandler;
    private TimerTask exerciseTask;

    int count;

*/

    // 1. start timer
    // 2. when timer reaches to 0
    // 2-1 trigger alarm & vibration on phone
    // 2-2 start animation
    // 3. update exercise count
    // 4. when acount reaches to goal
    // 4-1 trigger stop alarm & vibration on phone
    // 4-2 display good message
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.counter_layout, container, false);
        bodyText = (TextView) view.findViewById(R.id.counter);
        //downDrawable = getResources().getDrawable(R.drawable.jump_down_50);
        //upDrawable = getResources().getDrawable(R.drawable.jump_up_50);
        startTimer();
        return view;
    }

    private void startTimer()
    {
        countDownTimer = new MyCountDownTimer(startTime, interval);
        countDownTimer.start();
    }

/*
    private void startExercise()
    {
        bodyText.setText("" + 0);
        bodyText.setCompoundDrawablesWithIntrinsicBounds(upDrawable, null, null, null);
        exerciseHandler = new Handler();
        startAnimation();
    }

    private void startAnimation() {
        exerciseTask = new TimerTask() {
            @Override
            public void run() {
                exerciseHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        count++;
                        bodyText.setCompoundDrawablesWithIntrinsicBounds(
                                up ? upDrawable : downDrawable, null, null, null);
                        up = !up;
                        setCounter(count);
                    }
                });
            }
        };
        exerciseTimer = new Timer();
        exerciseTimer.scheduleAtFixedRate(exerciseTask, G3tUpConstants.ANIMATION_INTERVAL_MS,
                G3tUpConstants.ANIMATION_INTERVAL_MS);
    }

    public void stopExercise()
    {
        //exerciseTask.cancel();
        if(exerciseTimer != null)
        {
            exerciseTimer.cancel();
            exerciseTimer = null;
        }
        bodyText.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        bodyText.setTextSize(15);
        bodyText.setText("Have a nice day !");
    }


*/
    public void setCounter(String text) {
        bodyText.setText(text);
    }

    public void setCounter(int i) {
        setCounter(i < 0 ? "0" : String.valueOf(i));
    }

    @Override
    public void onDetach() {
  //      exerciseTimer.cancel();
        super.onDetach();
    }


    public class MyCountDownTimer extends CountDownTimer
    {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            bodyText.setText("" + millisUntilFinished / 1000);

        }

        @Override
        public void onFinish() {
           bodyText.setText("Finished");
    //        startExercise();
        }
    }
}
