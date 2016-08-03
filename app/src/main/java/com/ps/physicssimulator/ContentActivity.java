package com.ps.physicssimulator;


import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ps.physicssimulator.data.DataContract;

import java.lang.reflect.Method;

import io.github.kexanie.library.MathView;

public class ContentActivity extends AppCompatActivity {

    boolean isAudioPlaying;
    String mLesson;
    String mChapter;
    LinearLayout mContentContainer;
    int hasCalc;


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
        isAudioPlaying = false;

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isAudioPlaying){
                    isAudioPlaying = false;
                    fab.setImageResource(R.drawable.ic_play);
                    Snackbar.make(view, "Audio lesson paused", Snackbar.LENGTH_LONG)
                            .setAction("Stop", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            }).setActionTextColor(Color.WHITE).show();
                } else {
                    isAudioPlaying = true;
                    fab.setImageResource(R.drawable.ic_pause);
                }


            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean autoplay = prefs.getBoolean(getString(R.string.pref_autoplay_key), Boolean.parseBoolean(getString(R.string.pref_autoplay_default)));

        if(autoplay){
            //play audio
        }

        Intent intent = getIntent();
        mLesson = intent.getStringExtra("Lesson");
        mChapter = intent.getStringExtra("Chapter");

        setTitle(mLesson);

        mContentContainer = (LinearLayout) findViewById(R.id.content_container);

        Cursor sections = this.getContentResolver().query(
                DataContract.SectionEntry.buildSectionLesson(mLesson),
                null, null, null, null
        );
        sections.moveToFirst();
        int k = 0;
        for (int i = 0; i < sections.getCount(); i++) {
            Cursor images = this.getContentResolver().query(
                    DataContract.ImageEntry.buildImageSectionUri(
                            sections.getString(
                                    sections.getColumnIndex(DataContract.SectionEntry.COLUMN_NAME))),
                    null, null, null, null
            );
            boolean imageExists = images.moveToFirst();

            LinearLayout sectionContainer = new LinearLayout(this);
            sectionContainer.setLayoutParams(new LinearLayoutCompat.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
            );
            sectionContainer.setOrientation(OrientationHelper.VERTICAL);
            sectionContainer.setPadding(16, 0, 16, 16);

            String[] contentText = sections.getString(
                    sections.getColumnIndex(DataContract.SectionEntry.COLUMN_CONTENT))
                    .split("\\|");

            for (String content : contentText) {
                SpannableString text = new SpannableString(content);

                final MathView txtContent = new MathView(this, null);
                txtContent.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                ));
                txtContent.setEngine(MathView.Engine.KATEX);
                txtContent.setText(text.toString());
                txtContent.setTag("content" + k++);
                sectionContainer.addView(txtContent);
                if (imageExists) {
                    sectionContainer.addView(
                            addImage(
                                    images.getString(images
                                            .getColumnIndex(DataContract.ImageEntry.COLUMN_RESOURCE_NAME)),
                                    images.getString(
                                            images.getColumnIndex(DataContract.ImageEntry.COLUMN_CAPTION))
                            )
                    );
                    imageExists = images.moveToNext();
                }
            }

            while (imageExists) {
                sectionContainer.addView(
                        addImage(
                                images.getString(images
                                        .getColumnIndex(DataContract.ImageEntry.COLUMN_RESOURCE_NAME)),
                                images.getString(
                                        images.getColumnIndex(DataContract.ImageEntry.COLUMN_CAPTION))
                        )
                );
                imageExists = images.moveToNext();
            }

            mContentContainer.addView(sectionContainer);

            sections.moveToNext();
        }

        Cursor lesson = this.getContentResolver().query(
                DataContract.LessonEntry.buildLessonTitle(mLesson),
                null, null, null, null
        );


        lesson.moveToFirst();
        hasCalc = lesson.getInt(lesson.getColumnIndex(DataContract.LessonEntry.COLUMN_HAS_CALCULATOR));
        if (lesson.getInt(lesson.getColumnIndex(DataContract.LessonEntry.COLUMN_HAS_SIMULATION)) == 1) {
            Button btnSimulate = new Button(this);
            btnSimulate.setLayoutParams(new LinearLayoutCompat.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            btnSimulate.setText("Simulate");
            mContentContainer.addView(btnSimulate);
        }
    }

    public LinearLayout addImage(String imageName, String caption) {
        LinearLayout imageContainer = new LinearLayout(this);
        imageContainer.setLayoutParams(new LinearLayoutCompat.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        );
        imageContainer.setOrientation(OrientationHelper.VERTICAL);
        imageContainer.setPadding(16, 16, 16, 16);

        ImageView img = new ImageView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                240
        );

        params.gravity = Gravity.CENTER_HORIZONTAL;

        img.setLayoutParams(params);
        img.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        img.setImageResource(this.getResources().getIdentifier(
                imageName,
                "drawable", this.getPackageName()));
        TextView txtCaption = new TextView(this);
        txtCaption.setLayoutParams(new LinearLayoutCompat.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        txtCaption.setText(caption);
        txtCaption.setGravity(Gravity.CENTER_HORIZONTAL);

        imageContainer.addView(img);
        imageContainer.addView(txtCaption);

        return imageContainer;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(hasCalc == 0)
            menu.removeItem(R.id.action_calc);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_content, menu);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            final SearchView search = (SearchView) menu.findItem(R.id.action_search).setActionView(new SearchView(this)).getActionView();

            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public boolean onQueryTextChange(String query) {
                    if (!query.equals("")) {
                        View mathView = null;
                        int i = 0;
                        while ((mathView = mContentContainer.findViewWithTag("content" + i++)) != null) {
                            MathView txtContent = (MathView) mathView;
                            txtContent.findAllAsync(query);

                            try {
                                Method m = WebView.class.getMethod("setFindIsUp", Boolean.TYPE);
                                m.invoke(txtContent, true);
                            } catch (Throwable ignored) {
                            }
                        }
                        return true;
                    }

                    return false;

                }

            });

        }
        return true;
    }



    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_calc) {
            Intent intent = new Intent(this, CalculatorActivity.class);
            intent.putExtra("Lesson", mLesson);
            intent.putExtra("Chapter", mChapter);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
