package com.example.luke.classnamerefactoring;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

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
        ArrayList<String> late=new ArrayList<>(),checked=new ArrayList<>(),leave=new ArrayList<>(),absenteeism=new ArrayList<>();
        realm=Realm.getDefaultInstance();
        realm.beginTransaction();
        final ArrayList<StudentData> AllRecordsData=getRecordData(realm);
        Spinner mSpinner=(Spinner)getActivity().findViewById(R.id.records_spinner);
        String[] mRecords=new String[AllRecordsData.size()];
        for(int i=0;i<AllRecordsData.size();i++) {
            mRecords[i] = AllRecordsData.get(i).getKey();
            int cnt=0;
            char[] rep={'年','月','日'};
            StringBuilder temp=new StringBuilder(mRecords[i]);
            for(int j=0;j<temp.length();j++)
            {
                if(temp.charAt(j)=='_')
                    temp.setCharAt(j,rep[cnt++]);
                if(cnt==3) break;
            }
            mRecords[i]=temp.toString();
        }
        ArrayAdapter<String> stringAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, mRecords);
        mSpinner.setAdapter(stringAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==nowRecordPosition) return;
                nowRecordPosition=position;
                refreshCardView(AllRecordsData.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        super.onResume();
    }

    @Override
    public void onPause() {
        realm.cancelTransaction();
        realm.close();
        super.onPause();
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
        late=(TextView)getActivity().findViewById(R.id.lateList);
        leave=(TextView)getActivity().findViewById(R.id.leaveList);
        absenteeism=(TextView)getActivity().findViewById(R.id.absenteeismList);
        String lateStr="",leaveStr="",absenteeismStr="";

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
