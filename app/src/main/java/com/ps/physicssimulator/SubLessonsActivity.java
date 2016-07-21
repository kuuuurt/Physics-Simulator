package com.ps.physicssimulator;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SubLessonsActivity extends AppCompatActivity {

    ArrayAdapter<String> subLessonAdap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_lessons);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        List<String> lesson = new ArrayList<String>();
        Intent lessonIntent =  this.getIntent();
        if(lessonIntent != null && lessonIntent.hasExtra(Intent.EXTRA_TEXT)){
            String strLesson = lessonIntent.getStringExtra(Intent.EXTRA_TEXT);
            setTitle(strLesson);
            if(strLesson.equals("One-dimensional Motion")){
                lesson.add("Velocity");
                lesson.add("Acceleration");
                lesson.add("Free-fall");
            } else {
                lesson.add("Test");
            }
        }
        ListView lessonList = (ListView)findViewById(R.id.list_view_sublessons);

        subLessonAdap = new ArrayAdapter<String>(this, R.layout.list_item_lesson, R.id.list_item_text_lessons, lesson);
        lessonList.setAdapter(subLessonAdap);

    }

}
