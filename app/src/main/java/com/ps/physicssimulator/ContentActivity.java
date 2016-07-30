package com.ps.physicssimulator;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ps.physicssimulator.data.DataContract;

import io.github.kexanie.library.MathView;

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

        setTitle(mLesson);

        LinearLayout contentContainer = (LinearLayout)findViewById(R.id.content_container);

        Cursor sections = this.getContentResolver().query(
                DataContract.SectionEntry.buildSectionLesson(mLesson),
                null, null, null, null
        );
        sections.moveToFirst();
        for(int i = 0; i < sections.getCount(); i++){
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
            sectionContainer.setPadding(16, 16, 16, 16);

            String[] contentText = sections.getString(
                    sections.getColumnIndex(DataContract.SectionEntry.COLUMN_CONTENT))
                    .split("\\|");
            for(String content : contentText){
                SpannableString text = new SpannableString(content);

                MathView txtContent = new MathView(this, null);
                txtContent.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                ));
                txtContent.setEngine(MathView.Engine.KATEX);
                txtContent.setText(text.toString());


                sectionContainer.addView(txtContent);
                if(imageExists){
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

            while(imageExists){
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

            contentContainer.addView(sectionContainer);

            sections.moveToNext();
        }
    }

    public LinearLayout addImage(String imageName, String caption){
        LinearLayout imageContainer = new LinearLayout(this);
        imageContainer.setLayoutParams(new LinearLayoutCompat.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        );
        imageContainer.setOrientation(OrientationHelper.VERTICAL);
        imageContainer.setPadding(16, 16, 16, 16);

        ImageView img = new ImageView(this);
        img.setLayoutParams(new LinearLayoutCompat.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
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



}
