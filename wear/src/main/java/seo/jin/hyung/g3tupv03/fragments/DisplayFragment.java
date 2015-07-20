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

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import seo.jin.hyung.g3tupv03.R;

/**
 * A simple fragment for showing the count
 */
public class DisplayFragment extends AbstractFragment {


    private TextView bodyText;
    private ImageView bodyImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.display_layout, container, false);
        bodyText = (TextView) view.findViewById(R.id.completeText);
        setText("Have a nice day!");
        bodyImage = (ImageView)view.findViewById(R.id.completeImage);
        bodyImage.setBackgroundResource(R.drawable.w_complete);
        return view;
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

    }
}
