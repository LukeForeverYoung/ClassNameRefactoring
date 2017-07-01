package com.example.luke.classnamerefactoring;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.v7.widget.Toolbar;
import android.util.Log;


/**
 * Created by LukeDada on 2017/3/20.
 */

public class SettingActivity extends PreferenceActivity  {
    @Override
    protected void onCreate(Bundle saveInstanceState)
    {
        super.onCreate(saveInstanceState);
        //addPreferencesFromResource(R.xml.settings_content);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        preference.setSummary(newValue.toString());
        return true;
    }
    public static class MyPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener
    {
        @Override
        public void onCreate(Bundle saveInstanceState)
        {
            super.onCreate(saveInstanceState);
            addPreferencesFromResource(R.xml.settings_content);
            EditTextPreference stp=(EditTextPreference)findPreference("studentListSetting");
            stp.setOnPreferenceChangeListener(this);
            EditTextPreference ctp=(EditTextPreference)findPreference("classListSetting");
            ctp.setOnPreferenceChangeListener(this);
            stp.setSummary(stp.getText());
            ctp.setSummary(ctp.getText());
            if(stp.getText()==null) stp.setSummary("请输入学生名单，用逗号隔开");
            if(ctp.getText()==null) ctp.setSummary("请输入各课程，用逗号隔开");
            Log.d("111", "onCreate: ");
        }
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            preference.setSummary(newValue.toString());
            return true;
        }


    }

}
