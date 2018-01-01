package com.example.luke.classnamerefactoring;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

import io.realm.Realm;
import io.realm.RealmResults;


/**
 * A simple {@link Fragment} subclass.
 */
public class StudentRecords extends Fragment {
    Realm realm;

    public StudentRecords() {
        // Required empty public constructor
        nowRecordPosition=-1;
    }

    int nowRecordPosition;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_student_records, container, false);
    }

    @Override
    public void onResume() {

        refreshView();
        FloatingActionButton fab =getActivity().findViewById(R.id.deleteFab);
        fab.setOnClickListener(v -> Toast.makeText(getActivity().getApplicationContext(), "长按有效，小心误删", Toast.LENGTH_SHORT).show());
        fab.setOnLongClickListener(v -> {
            realm.beginTransaction();
            int position=nowRecordPosition;
            RealmResults<StudentData> results = realm.where(StudentData.class).findAll();
            if(results.size()==0)
            {
                Toast.makeText(getActivity().getApplicationContext(), "没有记录啦", Toast.LENGTH_LONG).show();
                realm.cancelTransaction();
                return true;
            }
            results.deleteFromRealm(results.size()-position-1);
            realm.commitTransaction();
            Toast.makeText(getActivity().getApplicationContext(), "已删除", Toast.LENGTH_LONG).show();
            initRecordSpinner();
            return true;
        });
        super.onResume();
    }
    public void refreshView()
    {
        ArrayList<String> late=new ArrayList<>(),checked=new ArrayList<>(),leave=new ArrayList<>(),absenteeism=new ArrayList<>();
        realm=Realm.getDefaultInstance();

        initRecordSpinner();

    }
    @Override
    public void onPause() {
        realm.close();
        super.onPause();
    }
    void initRecordSpinner()
    {
        final ArrayList<StudentData> AllRecordsData=getRecordData(realm);
        Spinner mSpinner=getActivity().findViewById(R.id.records_spinner);
        final String[] mRecords=new String[AllRecordsData.size()];
        for(int i=0;i<mRecords.length;i++) {
            mRecords[i] = AllRecordsData.get(mRecords.length-i-1).getKey();
            int cnt=0;
            char[] rep={'年','月','日'};
            StringBuilder temp=new StringBuilder(mRecords[i]);
            for(int j=0;j<temp.length();j++)
            {
                if(temp.charAt(j)=='_')
                    temp.setCharAt(j,rep[cnt++]);
                if(cnt==3)
                {
                    temp.insert(j+1,' ');
                    break;
                }
            }
            mRecords[i]=temp.toString();
        }
        ArrayAdapter<String> stringAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, mRecords);
        mSpinner.setAdapter(stringAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==nowRecordPosition) return;
                nowRecordPosition=position;
                refreshCardView(AllRecordsData.get(mRecords.length-position-1));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                refreshCardView(null);
            }

        });
        if(mRecords.length==0)
            refreshCardView(null);
        else refreshCardView(AllRecordsData.get(mRecords.length-1));
    }
    ArrayList<StudentData> getRecordData(Realm realm)
    {
        ArrayList<StudentData> AllRecordsData=new ArrayList<>();

        RealmResults<StudentData> results = realm.where(StudentData.class).findAll();
        for(StudentData x:results) {
            AllRecordsData.add(x);
        }
        return AllRecordsData;
    }
    void refreshCardView(StudentData now)
    {
        TextView late,leave,absenteeism;
        late=getActivity().findViewById(R.id.lateList);
        leave=getActivity().findViewById(R.id.leaveList);
        absenteeism=getActivity().findViewById(R.id.absenteeismList);
        String lateStr="",leaveStr="",absenteeismStr="";
        if(now!=null)
            for(Student stu:now.list)
            {
                if(stu.getState()==0) absenteeismStr+=stu.getStuName()+" ";
                if(stu.getState()==1) lateStr+=stu.getStuName()+" ";
                if(stu.getState()==3) leaveStr+=stu.getStuName()+" ";
            }
        late.setText(lateStr);
        leave.setText(leaveStr);
        absenteeism.setText(absenteeismStr);
    }
}
