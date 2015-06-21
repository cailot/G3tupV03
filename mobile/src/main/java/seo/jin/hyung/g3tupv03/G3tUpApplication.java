package seo.jin.hyung.g3tupv03;

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
