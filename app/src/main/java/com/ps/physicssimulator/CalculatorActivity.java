package com.ps.physicssimulator;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.ps.physicssimulator.data.DataContract;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import io.github.kexanie.library.MathView;

public class CalculatorActivity extends AppCompatActivity {

    static ExpressionBuilder expressionBuilder;
    static Expression expression;
    static String resultUnit;


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
                        spnFormula.setOnItemSelectedListener(new AdapterView
                                .OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view,
                                                       int i, long l) {
                                Cursor c = (Cursor)formulaAdap.getItem(i);
                                String formulaName = c.getString(c.getColumnIndex(DataContract.FormulaEntry.COLUMN_NAME));

                                final MathView txtFormula = (MathView)findViewById(R.id.text_main_formula);
                                txtFormula.setText(c.getString(c.getColumnIndex(DataContract.FormulaEntry.COLUMN_FORMULA)));


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
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        Cursor selected = (Cursor)varAdap.getItem(i);

                                        MathView txtResult = (MathView)findViewById(R.id.text_result);
                                        txtResult.setText("");

                                        MathView txtFormula = (MathView)findViewById(R.id.text_formula);
                                        txtFormula.setText(selected.getString(selected.getColumnIndex(DataContract.VariableEntry.COLUMN_FORMULA_DISPLAY)));

                                        resultUnit = selected.getString(selected.getColumnIndex(DataContract.VariableEntry.COLUMN_UNIT));

                                        expressionBuilder = new ExpressionBuilder(selected.getString(selected.getColumnIndex(DataContract.VariableEntry.COLUMN_FORMULA_COMPUTE)));

                                        final Cursor c = varAdap.getCursor();
                                        c.moveToFirst();

                                        LinearLayout linearLayout = (LinearLayout)
                                                findViewById(R.id.input_container);
                                        linearLayout.removeAllViews();

                                        for(int k = 0; k < c.getCount(); k++){
                                            if(k != i){
                                                expressionBuilder.variable(c.getString(c.getColumnIndex(DataContract.VariableEntry.COLUMN_SYMBOL)));

                                                LinearLayout layoutInput = new LinearLayout(CalculatorActivity.this);
                                                layoutInput.setLayoutParams(new LinearLayout.LayoutParams(
                                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                                        ViewGroup.LayoutParams.WRAP_CONTENT,
                                                        1.0f
                                                ));
                                                layoutInput.setOrientation(OrientationHelper.HORIZONTAL);
                                                layoutInput.setGravity(Gravity.CENTER);
                                                layoutInput.setPadding(16, 16, 16, 16);


                                                EditText txtInput = new EditText(CalculatorActivity.this);
                                                txtInput.setLayoutParams(new LinearLayout.LayoutParams(
                                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                                        ViewGroup.LayoutParams.WRAP_CONTENT,
                                                        0.1f
                                                ));
                                                txtInput.setHint(c.getString(c.getColumnIndex(DataContract.VariableEntry.COLUMN_NAME)));
                                                txtInput.setInputType(InputType.TYPE_CLASS_NUMBER);
                                                txtInput.setEms(10);
                                                txtInput.setTag(c.getString(c.getColumnIndex(DataContract.VariableEntry.COLUMN_SYMBOL)));
                                                txtInput.addTextChangedListener(createTextWatcher(c.getString(c.getColumnIndex(DataContract.VariableEntry.COLUMN_SYMBOL))));

                                                MathView txtUnit = new MathView(CalculatorActivity.this, null);
                                                txtUnit.setLayoutParams(new LinearLayout.LayoutParams(
                                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                                        ViewGroup.LayoutParams.WRAP_CONTENT,
                                                        0.3f
                                                ));
                                                txtUnit.setEngine(MathView.Engine.MATHJAX);
                                                txtUnit.setText(c.getString(c.getColumnIndex(DataContract.VariableEntry.COLUMN_UNIT)));

                                                LinearLayout webViewCenterHack = new LinearLayout(CalculatorActivity.this);
                                                webViewCenterHack.setLayoutParams(new LinearLayout.LayoutParams(
                                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                                        ViewGroup.LayoutParams.WRAP_CONTENT,
                                                        1.0f
                                                ));
                                                webViewCenterHack.setOrientation(OrientationHelper.VERTICAL);

                                                TextView centerHack = new TextView(CalculatorActivity.this);
                                                centerHack.setLayoutParams(new LinearLayout.LayoutParams(
                                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                                        ViewGroup.LayoutParams.WRAP_CONTENT,
                                                        0.9f
                                                ));

                                                webViewCenterHack.addView(centerHack);
                                                webViewCenterHack.addView(txtUnit);

                                                layoutInput.addView(txtInput);
                                                layoutInput.addView(webViewCenterHack);

                                                linearLayout.addView(layoutInput);
                                            }
                                            c.moveToNext();
                                        }
                                        expression = expressionBuilder.build();
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
                spnLessons.setSelection(1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        Button btnCalc = (Button)findViewById(R.id.button_calculate);
        btnCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MathView txtResult = (MathView)findViewById(R.id.text_result);
                txtResult.setText("$$Result: " + String.valueOf(expression.evaluate()) + resultUnit.replace("\\(", "{").replace("\\)", "}") + "$$");
            }
        });
    }



    public SimpleCursorAdapter setSpinnerAdapter(Cursor c, String[] projection){
        int[] views = new int[]{android.R.id.text1};
        return new SimpleCursorAdapter(this,
                android.R.layout.simple_spinner_dropdown_item, c, projection, views, 1);
    }

    public TextWatcher createTextWatcher(final String variable){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    expression.setVariable(variable, Double.parseDouble(charSequence.toString()));
                } catch (Exception ex){
                    expression.setVariable(variable, 0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
    }
}
