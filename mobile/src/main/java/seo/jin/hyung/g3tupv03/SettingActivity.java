package seo.jin.hyung.g3tupv03;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by jinseo on 2015. 6. 16..
 */
public class SettingActivity extends ActionBarActivity//PreferenceActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingFragment()).commit();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Settings");
    }

    public static class SettingFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preference);
        }
    }
}
