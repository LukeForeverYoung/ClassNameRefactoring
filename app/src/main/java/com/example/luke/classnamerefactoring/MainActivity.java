package com.example.luke.classnamerefactoring;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainActivity extends AppCompatActivity {

    StudentList StudentListFragment;
    StudentRecords StudentRecordsFragment;
    private FragmentPagerAdapter mAdapter;
    ViewPager vp;
    BottomNavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //initFragment();
        initViewPaper();
        initNavigation();
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent toSetting = new Intent();
        toSetting.setClass(MainActivity.this,SettingActivity.class);
        startActivity(toSetting);
        return super.onOptionsItemSelected(item);

    }
    private void initNavigation() {
        navigationView=findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                vp.setCurrentItem(item.getOrder()-1,true);
                return false;
            }
        });
    }
    private void  initViewPaper()
    {
        vp=findViewById(R.id.view_pager);
        StudentListFragment=new StudentList();
        StudentRecordsFragment=new StudentRecords();
        mAdapter=new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public android.support.v4.app.Fragment getItem(int position) {
                switch (position)
                {
                    case 0:
                        return StudentListFragment;
                    case 1:
                        return StudentRecordsFragment;
                }
                return null;
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
        vp.setAdapter(mAdapter);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                vp.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                StudentRecordsFragment.refreshView();
            }
        });
    }
    /*
    private void initFragment()
    {
        FragmentManager fm= getFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        StudentListFragment = new StudentList();
        StudentRecordsFragment = new StudentRecords();
        ft.add(R.id.contentFragment,StudentListFragment);
        ft.add(R.id.contentFragment,StudentRecordsFragment);
        ft.hide(StudentRecordsFragment);
        //ft.replace(R.id.contentFragment,StudentListFragment).commit();
        //ft.show(StudentRecordsFragment);
        ft.commit();
    }
    */
}
