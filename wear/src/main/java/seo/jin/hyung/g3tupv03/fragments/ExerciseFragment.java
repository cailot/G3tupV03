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

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import seo.jin.hyung.g3tupv03.R;
import seo.jin.hyung.g3tupv03.utils.G3tUpConstants;
import seo.jin.hyung.g3tupv03.utils.G3tUpUtils;

/**
 * A simple fragment for showing the count
 */
public class ExerciseFragment extends AbstractFragment {

    private TextView bodyText;
    private Timer exerciseTimer;
    private Handler exerciseHandler;
    private TimerTask exerciseTask;
    private boolean up = false;
    private Drawable downDrawable;
    private Drawable upDrawable;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.exercise_layout, container, false);
        downDrawable = getResources().getDrawable(R.drawable.jump_down_50);
        upDrawable = getResources().getDrawable(R.drawable.jump_up_50);
        bodyText = (TextView) view.findViewById(R.id.exerciseText);
//        bodyText.setCompoundDrawablesWithIntrinsicBounds(upDrawable, null, null, null);
        setText(G3tUpUtils.getCounterFromPreference(getActivity()));
        exerciseHandler = new Handler();
        startAnimation();
        return view;
    }

    // start animation
    private void startAnimation() {
        exerciseTask = new TimerTask() {
            @Override
            public void run() {
                exerciseHandler.post(new Runnable() {
                    @Override
                    public void run() {
//                        Log.e("#############", "" + up);
                        bodyText.setCompoundDrawablesWithIntrinsicBounds(
                                up ? upDrawable : downDrawable, null, null, null);
                        up = !up;
                        G3tUpUtils.vibrate(getActivity().getApplicationContext());
                    }
                });
            }
        };
        exerciseTimer = new Timer();
        exerciseTimer.scheduleAtFixedRate(exerciseTask, G3tUpConstants.SECOND,
                G3tUpConstants.SECOND);
    }

    @Override
    public void setText(int i) {
        setText(i < 0 ? "0" : String.valueOf(i));
    }

    @Override
    public void setText(String s) {
        bodyText.setText(s);
    }

    @Override
    public void stopAction() {
        //exerciseTask.cancel();
        if(exerciseTimer != null)
        {
            exerciseTimer.cancel();
            exerciseTimer = null;
        }
    }
    /*
    public void setText(String text) {
        bodyText.setText(text);
    }

    public void setText(int i) {
        setText(i < 0 ? "0" : String.valueOf(i));
    }

    public void stopAction()
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

}
