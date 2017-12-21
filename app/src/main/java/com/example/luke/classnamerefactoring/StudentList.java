package com.example.luke.classnamerefactoring;


import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;


/**
 * A simple {@link Fragment} subclass.
 */
public class StudentList extends Fragment {

    Activity root;
    String[] mClassItems;
    String[] mStudentItems;
    LinearLayout linearArray[];
    Button[] mStudentButton;
    int classTag;
    int CheckMode;
    public StudentList() {
        // Required empty public constructor
        CheckMode = 2;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = getActivity();
        View temp=inflater.inflate(R.layout.fragment_student_list, container, false);
        return temp;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState==null)
        {
            initClassSelector();
            initStudentList();
            initFabListener();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    void initClassSelector() {
        Spinner mSpinner =  root.findViewById(R.id.class_spinner);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mClassItems = sp.getString("classListSetting", "null").split(",|，");
        ArrayAdapter<String> stringAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, mClassItems);
        stringAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(stringAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                classTag = position;
                if (mClassItems[classTag] != null)
                    Toast.makeText(getActivity().getApplicationContext(), mClassItems[position], Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    void initFabListener() {
        FloatingActionButton fab;
        fab =  root.findViewById(R.id.saveFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savingData();
                Toast.makeText(getActivity().getApplicationContext(), "保存完毕", Toast.LENGTH_SHORT).show();
            }
        });
        fab =  root.findViewById(R.id.changeModeFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckMode == 2) {
                    CheckMode = 1;
                    v.getBackground().setColorFilter(getResources().getColor(R.color.origin, null), PorterDuff.Mode.SRC);
                    Toast.makeText(getActivity().getApplicationContext(), "迟到模式", Toast.LENGTH_SHORT).show();
                } else {
                    CheckMode = 2;
                    v.getBackground().setColorFilter(getResources().getColor(R.color.blueName, null), PorterDuff.Mode.SRC);
                    Toast.makeText(getActivity().getApplicationContext(), "签到模式", Toast.LENGTH_SHORT).show();
                }
            }
        });
        fab = root.findViewById(R.id.refreshFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initClassSelector();
                initStudentList();
            }
        });
    }

    void initStudentList() {
        linearArray = new LinearLayout[4];
        linearArray[0] = root.findViewById(R.id.yet_layout);
        linearArray[1] = root.findViewById(R.id.late_layout);
        linearArray[2] = root.findViewById(R.id.checked_layout);
        linearArray[3] = root.findViewById(R.id.leave_layout);
        for (LinearLayout ll : linearArray)
            ll.removeAllViews();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mStudentItems = sp.getString("studentListSetting", "null").split(",|，");
        mStudentButton = new Button[mStudentItems.length];
        int ord = 0;
        for (String stu : mStudentItems) {
            mStudentButton[ord] = new Button(getActivity());
            Button btn = mStudentButton[ord];
            btn.setText(stu);
            btn.setPadding(20, 0, 0, 20);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            btn.setTag(0);
            btn.setLayoutParams(params);
            linearArray[0].addView(btn);
            btn.setOnClickListener(v -> {
                if ((int) v.getTag() == 0) {
                    linearArray[0].removeView(v);
                    v.setTag(CheckMode);
                    linearArray[CheckMode].addView(v);
                } else {
                    linearArray[(int) v.getTag()].removeView(v);
                    v.setTag(0);
                    linearArray[0].addView(v);
                }
            });
            btn.setOnLongClickListener(v -> {
                linearArray[(int) v.getTag()].removeView(v);
                v.setTag(3);
                linearArray[3].addView(v);
                return true;
            });
            ord++;
        }
    }

    void savingData() {
        Calendar cal = Calendar.getInstance();
        String key = cal.get(Calendar.YEAR) + "_" +(Integer.valueOf(cal.get(Calendar.MONTH))+1) + "_" + cal.get(Calendar.DAY_OF_MONTH)+"_" + mClassItems[classTag];
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<StudentData> results = realm.where(StudentData.class).equalTo("key", key).findAll();
        results.deleteAllFromRealm();
        StudentData stuData;
        if (results.size() != 0)
            stuData = results.first();
        else {
            stuData = new StudentData();
            stuData.key = key;
        }
        stuData.lesson = mClassItems[classTag];
        stuData.list = new RealmList<>();
        for (Button btn : mStudentButton) {
            Student stu = new Student();
            stu.stuName = String.valueOf(btn.getText());
            stu.state = (int) btn.getTag();
            stuData.list.add(stu);
        }
        if (results.size() == 0)
            realm.copyToRealm(stuData);

        realm.commitTransaction();
        realm.close();
    }
}