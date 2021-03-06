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

package com.hyung.jin.seo.getup.wear.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.preference.PreferenceManager;

/**
 * A utility class for some helper methods.
 */
public class G3tUpUtils {

    /**
     * Causes device to vibrate for the given duration (in millis). If duration is set to 0, then it
     * will use the <code>DEFAULT_VIBRATION_DURATION_MS</code>.
     */
    public static void vibrate(Context context) {
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(G3tUpConstants.DEFAULT_VIBRATION_DURATION_MS);
    }

    /**
     * Saves the counter value in the preference storage. If <code>value</code>
     * is negative, then the value will be removed from the preferences.
     */
    public static void saveCounterToPreference(Context context, int value) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        if (value < 0) {
            // we want to remove
            pref.edit().remove(G3tUpConstants.PREF_KEY_COUNTER).apply();
        } else {
            pref.edit().putInt(G3tUpConstants.PREF_KEY_COUNTER, value).apply();
        }
    }

    /**
     * Retrieves the value of counter from preference manager. If no value exists, it will return
     * <code>0</code>.
     */
    public static int getCounterFromPreference(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getInt(G3tUpConstants.PREF_KEY_COUNTER, 0);
    }


}
