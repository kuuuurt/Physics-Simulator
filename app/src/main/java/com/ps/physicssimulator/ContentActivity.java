package com.ps.physicssimulator;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.ps.physicssimulator.data.DataContract;

public class ContentActivity extends AppCompatActivity {

    String mLesson;
    String mChapter;

    @Override
    public Intent getSupportParentActivityIntent() {
        Intent intent = super.getSupportParentActivityIntent();
        return intent.putExtra(Intent.EXTRA_TEXT, mChapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        mLesson = intent.getStringExtra("Lesson");
        mChapter = intent.getStringExtra("Chapter");

        String fragName = intent.getStringExtra("FragmentName");
        Fragment frag = Fragment.instantiate(this, fragName);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_content, frag)
                .addToBackStack(null).commit();

        setTitle(mLesson);

        Cursor lesson = this.getContentResolver().query(
                DataContract.LessonEntry.buildLessonTitle(mLesson),
                null,null,null,null
        );

        lesson.moveToFirst();


        TextView txtContent = (TextView)findViewById(R.id.text_values_content_1);
        txtContent.setText(Html.fromHtml(lesson.getString(lesson.getColumnIndex(DataContract.LessonEntry.COLUMN_CONTENT))));
    }



}
