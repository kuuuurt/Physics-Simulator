package com.ps.physicssimulator;


import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.ps.physicssimulator.data.DataContract;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import io.github.kexanie.library.MathView;

public class ContentActivity extends AppCompatActivity implements YouTubePlayer.OnInitializedListener{


    String mLesson;
    String mChapter;
    LinearLayout mContentContainer;
    int hasCalc;
    boolean autoplay;
    MediaPlayer audioPlayer;
    FloatingActionButton fab;
    int textSize;
    List<MathView> contents;

    private final String youTubeAPIKey = "AIzaSyC5kxF8pAU4jQqc2XUoa-58CH08C8BOvCY";
    private static final int RQS_ErrorDialog = 1;
    private YouTubePlayer mPlayer;
    private String video;

    @Override
    public Intent getSupportParentActivityIntent() {
        Intent intent = super.getSupportParentActivityIntent();
        return intent.putExtra(Intent.EXTRA_TEXT, mChapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        audioPlayer.pause();
        audioPlayer.seekTo(0);
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        audioPlayer.pause();
        audioPlayer.seekTo(0);
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(audioPlayer.isPlaying()){
                    fab.setImageResource(R.drawable.ic_play);
                    audioPlayer.pause();
                    Snackbar.make(view, "Audio lesson paused", Snackbar.LENGTH_LONG)
                            .setAction("Stop", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    audioPlayer.seekTo(0);
                                    audioPlayer.pause();
                                    Toast.makeText(ContentActivity.this, "The audio has been stopped.", Toast.LENGTH_SHORT).show();
                                }
                            }).setActionTextColor(Color.WHITE).show();
                } else {
                    audioPlayer.start();
                    fab.setImageResource(R.drawable.ic_pause);
                }


            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        autoplay = prefs.getBoolean(getString(R.string.pref_autoplay_key), Boolean.parseBoolean(getString(R.string.pref_autoplay_default)));
        textSize = prefs.getInt(getString(R.string.pref_lesson_text_size), Integer.parseInt(getString(R.string.pref_lesson_text_size_default))) + 16;



        Intent intent = getIntent();
        mLesson = intent.getStringExtra("Lesson");
        mChapter = intent.getStringExtra("Chapter");


        setTitle(mLesson);

        renderLesson();


    }

    public void renderLesson(){
        mContentContainer = (LinearLayout) findViewById(R.id.content_container);

        contents = new ArrayList<>();

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


            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setIndeterminate(true);
            progressDialog.show();

            for (int l = 0; l < contentText.length; l++) {
                SpannableString text = new SpannableString(contentText[l]);

                final MathView txtContent = new MathView(this, null);
                txtContent.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                ));
                txtContent.setEngine(MathView.Engine.KATEX);
                //txtContent.setText(content);
                txtContent.setText(text.toString());
                txtContent.setTag("content" + k++);
                txtContent.getSettings().setJavaScriptEnabled(true);
                txtContent.getSettings().setDomStorageEnabled(true);
                txtContent.getSettings().setAppCacheEnabled(true);

                if(l == contentText.length-1)
                    txtContent.setWebViewClient(new WebViewClient(){
                        @Override
                        public void onPageFinished(WebView view, String url) {
                            progressDialog.dismiss();
                            ScrollView scrollView = (ScrollView)findViewById(R.id.scroll_content);
                            scrollView.setVisibility(View.VISIBLE);
                        }
                    });
                contents.add(txtContent);
                Cursor examples = this.getContentResolver().query(
                        DataContract.ExampleEntry.buildExampleSection(sections.getString(
                                sections.getColumnIndex(DataContract.SectionEntry.COLUMN_NAME))),
                        null, null, null, null
                );
                examples.moveToFirst();
                sectionContainer.addView(txtContent);
                for(int o = 0; o < examples.getCount(); o++){
                    LinearLayout exampleButtons = new LinearLayout(ContentActivity.this);
                    exampleButtons.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            1.0f
                    ));
                    exampleButtons.setOrientation(OrientationHelper.HORIZONTAL);
                    exampleButtons.setGravity(Gravity.CENTER_HORIZONTAL);
                    exampleButtons.setPadding(16, 16, 16, 16);
                    try {
                        exampleButtons.addView(createExampleButton(
                                examples.getString(examples.getColumnIndex(DataContract.ExampleEntry.COLUMN_NAME)),
                                examples.getString(examples.getColumnIndex(DataContract.ExampleEntry.COLUMN_CONTENT)),
                                sections.getString(sections.getColumnIndex(DataContract.SectionEntry.COLUMN_NAME))
                        ));
                        examples.moveToNext();
                        o++;
                        exampleButtons.addView(createExampleButton(
                                examples.getString(examples.getColumnIndex(DataContract.ExampleEntry.COLUMN_NAME)),
                                examples.getString(examples.getColumnIndex(DataContract.ExampleEntry.COLUMN_CONTENT)),
                                sections.getString(sections.getColumnIndex(DataContract.SectionEntry.COLUMN_NAME))
                        ));
                        examples.moveToNext();
                        o++;
                        exampleButtons.addView(createExampleButton(
                                examples.getString(examples.getColumnIndex(DataContract.ExampleEntry.COLUMN_NAME)),
                                examples.getString(examples.getColumnIndex(DataContract.ExampleEntry.COLUMN_CONTENT)),
                                sections.getString(sections.getColumnIndex(DataContract.SectionEntry.COLUMN_NAME))
                        ));
                        examples.moveToNext();
                        o++;
                    } catch(Exception ex) {}
                    sectionContainer.addView(exampleButtons);
                }


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
        changeFontSize();





        Cursor lesson = this.getContentResolver().query(
                DataContract.LessonEntry.buildLessonTitle(mLesson),
                null, null, null, null
        );


        lesson.moveToFirst();
        video = lesson.getString(lesson.getColumnIndex(DataContract.LessonEntry.COLUMN_VIDEO_ID));
        hasCalc = lesson.getInt(lesson.getColumnIndex(DataContract.LessonEntry.COLUMN_HAS_CALCULATOR));
        Button btnSimulate = (Button)findViewById(R.id.button_simulate);
        if (lesson.getInt(lesson.getColumnIndex(DataContract.LessonEntry.COLUMN_HAS_SIMULATION)) == 1) {
            btnSimulate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    audioPlayer.pause();
                    audioPlayer.seekTo(0);
                    fab.setImageResource(R.drawable.ic_play);
                    Intent intent = new Intent(ContentActivity.this, UnityPlayerActivity.class);
                    intent.putExtra("Lesson", mLesson);
                    intent.putExtra("Chapter", mChapter);
                    startActivity(intent);
                }
            });
        } else {
            btnSimulate.setVisibility(View.GONE);
        }

        audioPlayer = MediaPlayer.create(this, getResources().getIdentifier(
                lesson.getString(lesson.getColumnIndex(DataContract.LessonEntry.COLUMN_AUDIO)),
                "raw", this.getPackageName()));

        audioPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.seekTo(0);
                ///Toast.makeText(ContentActivity.this, "The audio lesson has finished playing.", Toast.LENGTH_SHORT).show();
                fab.setImageResource(R.drawable.ic_play);
            }
        });

        if(autoplay){
            audioPlayer.start();
            fab.setImageResource(R.drawable.ic_pause);
        }



        YouTubePlayerFragment youtubePlayer = (YouTubePlayerFragment)getFragmentManager()
                .findFragmentById(R.id.youtube_fragment);
        youtubePlayer.initialize(youTubeAPIKey, this);

    }

    public Button createExampleButton(final String name, final String content, final String section){
        Button button = new Button(this);
        button.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                0.33f
        ));
        button.setText(name);
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContentActivity.this, ExampleActivity.class);
                intent.putExtra("name", name + " - " + section);
                intent.putExtra("content", content);
                intent.putExtra("Lesson", mLesson);
                intent.putExtra("Chapter", mChapter);
                startActivity(intent);
            }
        });
        return button;

    }

    public void changeFontSize(){
        for(MathView m : contents){
            m.getSettings().setMinimumFontSize(textSize);
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
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id == R.id.action_calc) {
            Intent intent = new Intent(this, CalculatorActivity.class);
            intent.putExtra("Lesson", mLesson);
            intent.putExtra("Chapter", mChapter);
            startActivity(intent);
            return true;
        } else if(id == R.id.action_font_size){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.dialog_font_size, null);
            SeekBar seekBar = (SeekBar)dialogView.findViewById(R.id.seekbar_font_size);
            seekBar.setProgress(textSize-16);
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ContentActivity.this);
                    textSize = i+16;
                    changeFontSize();
                    prefs.edit().remove(getString(R.string.pref_lesson_text_size)).apply();
                    prefs.edit().putInt(getString(R.string.pref_lesson_text_size), i).apply();

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            builder.setTitle("Set Font Size")
                .setView(dialogView)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                .create().show();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if (!b) {
            youTubePlayer.cueVideo(video);
        }

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult result) {
        if (result.isUserRecoverableError()) {
            result.getErrorDialog(this, RQS_ErrorDialog).show();
        } else {
            Toast.makeText(this,
                    "YouTubePlayer.onInitializationFailure(): " + result.toString(),
                    Toast.LENGTH_LONG).show();
        }
    }
}
