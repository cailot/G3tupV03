package seo.jin.hyung.g3tupv03;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by jinseo on 2015. 6. 16..
 */
public class SettingActivity extends PreferenceActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
    }
}
