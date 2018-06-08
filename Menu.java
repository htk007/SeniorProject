package com.example.dell.augmentedreality;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class Menu extends TabActivity {

    public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.menu);

    Resources ressources = getResources();
    TabHost tabHost = getTabHost();

    // Lesson tab
    Intent intentLesson = new Intent().setClass(this, Lesson.class);
    TabHost.TabSpec lesson = tabHost
            .newTabSpec("Lesson")
            .setIndicator("", ressources.getDrawable(R.drawable.lesson_tab))
            .setContent(intentLesson);

    // Classes tab
    Intent intentClass = new Intent().setClass(this, Classes.class);
    TabHost.TabSpec classes = tabHost
            .newTabSpec("Classes")
            .setIndicator("", ressources.getDrawable(R.drawable.class_tab))
            .setContent(intentClass);

    // Lecture tab
    Intent intentLecture = new Intent().setClass(this, Lecture.class);
    TabHost.TabSpec lecture = tabHost
            .newTabSpec("Lecture")
            .setIndicator("", ressources.getDrawable(R.drawable.lecture_tab))
            .setContent(intentLecture);

    // add all tabs
    tabHost.addTab(lesson);
    tabHost.addTab(classes);
    tabHost.addTab(lecture);


    //set Windows tab as default (zero based)
    tabHost.setCurrentTab(0);
    }
}
