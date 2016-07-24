package com.ps.physicssimulator;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import com.ps.physicssimulator.tests.DataContract;

public class CalculatorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final SimpleCursorAdapter chaptersAdap = setSpinnerAdapter(
            this.getContentResolver().query(DataContract.ChapterEntry.CONTENT_URI,
                    null, null, null, null),
            new String[]{DataContract.ChapterEntry.COLUMN_NAME}
        );




        Spinner spnChapters = (Spinner) findViewById(R.id.spinner_chapters);
        spnChapters.setAdapter(chaptersAdap);
        spnChapters.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor c = (Cursor)chaptersAdap.getItem(i);
                String chapter =
                        c.getString(c.getColumnIndex(DataContract.ChapterEntry.COLUMN_NAME));

                final SimpleCursorAdapter lessonsAdap = setSpinnerAdapter(
                        CalculatorActivity.this.getContentResolver().query(
                                DataContract.LessonEntry.buildLessonChapter(chapter),
                                null, null, null, null),
                        new String[]{DataContract.LessonEntry.COLUMN_TITLE}
                );

                Spinner spnLessons = (Spinner) findViewById(R.id.spinner_lessons);
                spnLessons.setAdapter(lessonsAdap);
                spnLessons.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i,
                                               long l) {
                        Cursor c = (Cursor)lessonsAdap.getItem(i);
                        String lesson = c.getString(c.getColumnIndex(DataContract.LessonEntry
                            .COLUMN_TITLE));

                        Log.d("qwer", lesson);

                        //Load Calculator Fragment
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {}
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    public SimpleCursorAdapter setSpinnerAdapter(Cursor c, String[] projection){
        int[] views = new int[]{android.R.id.text1};

        return new SimpleCursorAdapter(this,
                android.R.layout.simple_spinner_dropdown_item, c, projection, views, 1);
    }
}
