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
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import seo.jin.hyung.g3tupv03.R;

/**
 * A simple fragment for showing the count
 */
public class CountDownFragment extends Fragment {

    private static final long ANIMATION_INTERVAL_MS = 1000; // in milliseconds
    private TextView bodyText;
    private Timer exerciseTimer;
    private Handler exerciseHandler;
    private TimerTask exerciseTask;

    private CountDownTimer countDownTimer;
    private final long startTime = 10*1000;
    private final long interval = 1*1000;

//    int count;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.counter_layout, container, false);
        bodyText = (TextView) view.findViewById(R.id.counter);
        startTimer();
        return view;
    }

    private void startTimer()
    {
        countDownTimer = new MyCountDownTimer(startTime, interval);
        countDownTimer.start();
    }


    public void setCounter(String text) {
        bodyText.setText(text);
    }

    public void setCounter(int i) {
        setCounter(i < 0 ? "0" : String.valueOf(i));
    }

    @Override
    public void onDetach() {
        exerciseTimer.cancel();
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
        }
    }
}
