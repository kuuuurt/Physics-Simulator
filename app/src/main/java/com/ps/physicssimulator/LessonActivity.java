package com.ps.physicssimulator;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ps.physicssimulator.data.DataContract;

public class LessonActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>{

    String mChapter;
    private static final int LESSONS_LOADER = 0;
    private LessonAdapter mLessonAdap;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        mChapter = intent.getStringExtra(Intent.EXTRA_TEXT);

        mLessonAdap = new LessonAdapter(this, null, true);
        getSupportLoaderManager().initLoader(LESSONS_LOADER, null, this);

        setTitle(mChapter);

        ListView lessonList = (ListView)findViewById(R.id.list_view_lesson);
        lessonList.setAdapter(mLessonAdap);
        lessonList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor data = mLessonAdap.getCursor();
                Intent intent = new Intent(LessonActivity.this, ContentActivity.class);
                intent.putExtra("Lesson", data.getString(data.getColumnIndex(
                        DataContract.LessonEntry.COLUMN_TITLE)));
                intent.putExtra("Chapter", mChapter);
                intent.putExtra("FragmentName", data.getString(data.getColumnIndex(
                        DataContract.LessonEntry.COLUMN_FRAGNAME)));
                startActivity(intent);
            }
        });


    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                DataContract.LessonEntry.buildLessonChapter(mChapter),
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mLessonAdap.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mLessonAdap.swapCursor(null);
    }
}
