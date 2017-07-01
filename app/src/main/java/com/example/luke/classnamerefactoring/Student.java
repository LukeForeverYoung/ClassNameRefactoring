package com.example.luke.classnamerefactoring;

import io.realm.RealmObject;

/**
 * Created by Luke on 2017/6/30.
 */

public class Student extends RealmObject
{
    public String stuName;
    String getStuName()
    {
        return stuName;
    }

    public int state;
    int getState()
    {
        return state;
    }
}
