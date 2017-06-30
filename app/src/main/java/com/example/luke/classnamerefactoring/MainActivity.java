package com.example.luke.classnamerefactoring;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    Fragment StudentListFragment,StudentRecordsFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id==R.id.action_settings)
        {
            Intent toSetting = new Intent();
            toSetting.setClass(MainActivity.this,SettingActivity.class);
            startActivity(toSetting);
            return super.onOptionsItemSelected(item);
        }
        FragmentManager fm= getFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        Fragment nextFragment=null;
        switch (id)
        {
            case R.id.action_main:
                nextFragment=StudentListFragment;
                break;
            case R.id.action_list:
                nextFragment=StudentRecordsFragment;
                break;
        }
        ft.replace(R.id.contentFragment,nextFragment).commit();
        return super.onOptionsItemSelected(item);
    }
    private void initFragment()
    {
        FragmentManager fm= getFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        StudentListFragment = new StudentList();
        StudentRecordsFragment = new StudentRecords();
        ft.replace(R.id.contentFragment,StudentListFragment).commit();
    }
}
