package com.example.luke.classnamerefactoring;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Luke on 2017/6/30.
 */

public class StudentData extends RealmObject {
    @PrimaryKey
    public String key;
    String getKey()
    {
        return key;
    }
    public String lesson;
    String getLesson()
    {
        return lesson;
    }
    public RealmList<Student> list;
}
