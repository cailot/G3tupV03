package com.hyung.jin.seo.getup.mobile;

import android.app.Application;

import wearprefs.WearPrefs;

/**
 * Created by jinseo on 2015. 6. 18..
 */
public class G3tUpApplication extends Application
{
    @Override
    public void onCreate() {
        super.onCreate();
        WearPrefs.init(this);
    }
}
