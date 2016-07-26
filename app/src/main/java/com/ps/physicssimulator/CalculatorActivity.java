package com.ps.physicssimulator;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import com.ps.physicssimulator.data.DataContract;

import io.github.kexanie.library.MathView;

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

                        final SimpleCursorAdapter formulaAdap = setSpinnerAdapter(
                                CalculatorActivity.this.getContentResolver().query(
                                        DataContract.FormulaEntry.buildFormulaLesson(lesson),
                                        null, null, null, null),
                                new String[]{DataContract.FormulaEntry.COLUMN_NAME}
                        );

                        Spinner spnFormula = (Spinner) findViewById(R.id.spinner_formula);
                        spnFormula.setAdapter(formulaAdap);
                        spnFormula.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view,
                                                       int i, long l) {
                                Cursor c = (Cursor)formulaAdap.getItem(i);
                                String formulaName = c.getString(c.getColumnIndex(
                                        DataContract.FormulaEntry.COLUMN_NAME));
                                String formula = c.getString(c.getColumnIndex(
                                        DataContract.FormulaEntry.COLUMN_FORMULA));

                                MathView txtFormula = (MathView)findViewById(R.id.text_formula);
                                txtFormula.setText(formula);


                                final SimpleCursorAdapter varAdap = setSpinnerAdapter(
                                        CalculatorActivity.this.getContentResolver().query(
                                                DataContract.VariableEntry.buildVariableFormula(
                                                        formulaName), null, null, null, null),
                                        new String[]{DataContract.VariableEntry.COLUMN_NAME}
                                );

                                Spinner spnVar = (Spinner) findViewById(R.id.spinner_variable);
                                spnVar.setAdapter(varAdap);
                                spnVar.setOnItemSelectedListener(
                                        new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView,
                                                               View view, int i, long l) {
                                        Cursor c = varAdap.getCursor();
                                        c.moveToFirst();

                                        LinearLayout linearLayout = (LinearLayout)
                                                findViewById(R.id.input_container);
                                        linearLayout.removeAllViews();

                                        for(int k = 0; k < c.getCount(); k++){
                                            if(k != i){
                                                EditText txtInput =
                                                        new EditText(CalculatorActivity.this);
                                                txtInput.setLayoutParams(new ViewGroup
                                                        .LayoutParams(
                                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                                        ViewGroup.LayoutParams.WRAP_CONTENT
                                                ));
                                                txtInput.setHint(c.getString(c.getColumnIndex(
                                                        DataContract.VariableEntry
                                                                .COLUMN_NAME)));
                                                txtInput.setInputType(InputType.TYPE_CLASS_NUMBER);
                                                txtInput.setEms(10);
                                                txtInput.setPadding(16, 16, 16, 16);
                                                linearLayout.addView(txtInput);
                                            }
                                            c.moveToNext();
                                        }

                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {}
                                });
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {}
                        });
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
