package com.ps.physicssimulator;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;

import io.github.kexanie.library.MathView;

public class ExampleActivity extends AppCompatActivity {

    String lesson, chapter;
    int textSize;
    MathView txtContent;

    @Nullable
    @Override
    public Intent getSupportParentActivityIntent() {
        return super.getSupportParentActivityIntent()
                .putExtra("Lesson", lesson)
                .putExtra("Chapter", chapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        textSize = prefs.getInt(getString(R.string.pref_lesson_text_size), Integer.parseInt(getString(R.string.pref_lesson_text_size_default))) + 16;

        Intent intent = getIntent();
        lesson = intent.getStringExtra("Lesson");
        chapter = intent.getStringExtra("Chapter");

        setTitle(intent.getStringExtra("name"));

        txtContent = (MathView)findViewById(R.id.text_content);
        txtContent.setText(intent.getStringExtra("content"));
        txtContent.getSettings().setMinimumFontSize(textSize);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_example, menu);
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
                        txtContent.findAllAsync(query);

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

        if(id == R.id.action_font_size){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.dialog_font_size, null);
            SeekBar seekBar = (SeekBar)dialogView.findViewById(R.id.seekbar_font_size);
            seekBar.setProgress(textSize-16);
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ExampleActivity.this);
                    textSize = i+16;
                    prefs.edit().remove(getString(R.string.pref_lesson_text_size)).apply();
                    prefs.edit().putInt(getString(R.string.pref_lesson_text_size), textSize).apply();
                    txtContent.getSettings().setMinimumFontSize(textSize);
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

}
