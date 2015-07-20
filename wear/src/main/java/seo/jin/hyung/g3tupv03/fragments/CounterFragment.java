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

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import seo.jin.hyung.g3tupv03.R;

/**
 * A simple fragment for showing the count
 */
public class CounterFragment extends AbstractFragment {

    private TextView bodyText;
    private ImageView bodyImage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.counter_layout, container, false);
        bodyText = (TextView) view.findViewById(R.id.counterText);
        bodyImage = (ImageView) view.findViewById(R.id.counterImage);
        bodyImage.setBackgroundResource(R.drawable.w_timer);
        return view;
    }

    @Override
    public void setText(int i) {
        setText(i < 0 ? "0" : String.valueOf(i));
    }

    @Override
    public void setText(String s) {

        //bodyText.setText(s);

        Spannable words = new SpannableString(s);
        int minIndex = s.indexOf("Min");
        int secIndex = s.indexOf("Sec");
        words.setSpan(new ForegroundColorSpan(Color.parseColor("#00A4F0")), 0, minIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        words.setSpan(new ForegroundColorSpan(Color.parseColor("#00A4F0")), minIndex + "Min".length(), secIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        bodyText.setText(words);
    }

    @Override
    public void stopAction() {

    }
}
