package com.ps.physicssimulator;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import com.ps.physicssimulator.data.DataContract;
import com.ps.physicssimulator.utils.ExpressionBuilderModified;
import com.ps.physicssimulator.utils.ExpressionModified;

import java.util.ArrayList;
import java.util.List;

import io.github.kexanie.library.MathView;

public class CalculatorActivity extends AppCompatActivity {

    static ExpressionBuilderModified expressionBuilder;
    static ExpressionModified expression;
    static String currentFormula;
    static String variableToSolve;
    static String[][] values;


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

        MathView txtArrow = (MathView)findViewById(R.id.text_arrow);
        txtArrow.setText("$$\\Rightarrow$$");



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
                spnLessons.setSelection(0);
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
                        spnFormula.setSelection(0);
                        spnFormula.setOnItemSelectedListener(new AdapterView
                                .OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view,
                                                       int i, long l) {
                                Cursor c = (Cursor)formulaAdap.getItem(i);
                                String formulaName = c.getString(c.getColumnIndex(DataContract.FormulaEntry.COLUMN_NAME));

                                MathView txtFormula = (MathView)findViewById(R.id.text_main_formula);
                                txtFormula.setText(c.getString(c.getColumnIndex(DataContract.FormulaEntry.COLUMN_FORMULA)));


                                final SimpleCursorAdapter varAdap = setSpinnerAdapter(
                                        CalculatorActivity.this.getContentResolver().query(
                                                DataContract.VariableEntry.buildVariableFormula(
                                                        formulaName), null, null, null, null),
                                        new String[]{DataContract.VariableEntry.COLUMN_NAME}
                                );

                                Spinner spnVar = (Spinner) findViewById(R.id.spinner_variable);
                                spnVar.setAdapter(varAdap);
                                spnVar.setSelection(0);
                                spnVar.setOnItemSelectedListener(
                                        new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        Cursor selected = (Cursor)varAdap.getItem(i);

                                        resetSteps();

                                        MathView txtResult = (MathView)findViewById(R.id.text_result);
                                        txtResult.setText("");

                                        String formula = selected.getString(selected.getColumnIndex(DataContract.VariableEntry.COLUMN_FORMULA_DISPLAY));

                                        MathView txtFormula = (MathView)findViewById(R.id.text_formula);
                                        txtFormula.setText(formula);

                                        currentFormula = selected.getString(selected.getColumnIndex(DataContract.VariableEntry.COLUMN_FORMULA_COMPUTE));
                                        variableToSolve = selected.getString(selected.getColumnIndex(DataContract.VariableEntry.COLUMN_SYMBOL));
                                        expressionBuilder = new ExpressionBuilderModified(currentFormula);


                                        final Cursor c = varAdap.getCursor();
                                        c.moveToFirst();


                                        values = new String[c.getCount() - 1][3];
                                        int varCtr = 0;

                                        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.input_container);
                                        linearLayout.removeAllViews();

                                        List<TextInputLayout> inputFields = new ArrayList<>();

                                        for(int k = 0; k < c.getCount(); k++){
                                            if(k != i){
                                                String symbol = c.getString(c.getColumnIndex(DataContract.VariableEntry.COLUMN_SYMBOL));
                                                String unit = c.getString(c.getColumnIndex(DataContract.VariableEntry.COLUMN_UNIT));

                                                expressionBuilder.variable(symbol);
                                                values[varCtr][0] = symbol;
                                                values[varCtr][1] = unit;
                                                values[varCtr++][2] = "";

                                                TextInputLayout txtLayout = new TextInputLayout(CalculatorActivity.this);
                                                txtLayout.setLayoutParams(new LinearLayout.LayoutParams(
                                                        0,
                                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                                        0.5f
                                                ));



                                                EditText txtInput = new EditText(CalculatorActivity.this);
                                                txtInput.setLayoutParams(new LinearLayout.LayoutParams(
                                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                                ));
                                                txtInput.setHint(c.getString(c.getColumnIndex(DataContract.VariableEntry.COLUMN_NAME)) +
                                                        " (" + c.getString(c.getColumnIndex(DataContract.VariableEntry.COLUMN_SYMBOL)) + ")");
                                                txtInput.setInputType(InputType.TYPE_CLASS_NUMBER);
                                                txtInput.setEms(10);
                                                txtInput.addTextChangedListener(createTextWatcher(symbol));

                                                txtLayout.addView(txtInput);
                                                inputFields.add(txtLayout);
                                            }
                                            c.moveToNext();
                                        }
                                        for(int j = 0; j < inputFields.size(); j++){
                                            LinearLayout layoutInput = new LinearLayout(CalculatorActivity.this);
                                            layoutInput.setLayoutParams(new LinearLayoutCompat.LayoutParams(
                                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                                    1.0f
                                            ));
                                            layoutInput.setOrientation(OrientationHelper.HORIZONTAL);
                                            layoutInput.setPadding(0,8,0,8);
                                            layoutInput.setGravity(Gravity.CENTER_HORIZONTAL);
                                            try{
                                                layoutInput.addView(inputFields.get(j++));
                                                layoutInput.addView(inputFields.get(j));
                                            } catch (Exception ex){

                                            }
                                            linearLayout.addView(layoutInput);
                                        }
                                        substituteValues();
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
                //spnLessons.setSelection(1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        spnChapters.setSelection(1);
    }

    public void calculate(){
        resetSteps();
        for(String[] s : values){
            expression.setVariable(s[0], Double.parseDouble(s[2]), s[1]);
        }
        List<String> results = expression.evaluate();
        Object[] steps = results.toArray();

        MathView txtSub = (MathView) findViewById(R.id.text_substitute);

        String formulaDisplay = txtSub.getText();

        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.steps_container);

        for(int i = 0; i < steps.length ; i++){
            MathView txtStep = new MathView(CalculatorActivity.this, null);
            txtStep.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            txtStep.setEngine(MathView.Engine.KATEX);
            String result[] = (String[])steps[i];
            if(Double.parseDouble(result[0].toString()) % 1 == 0){
                result[0] = result[0].replace(".0", "");
            }

            String strForm = result[2].toString();
            if(formulaDisplay.contains("(" + strForm + ")")){
                formulaDisplay = formulaDisplay.replace("(" + strForm + ")", strForm);
            }
            if(i == steps.length-1) {
                formulaDisplay = formulaDisplay.replace(strForm, result[0] + "{" + result[1] + "}");
            } else {
                formulaDisplay = formulaDisplay.replace(strForm, result[0] + result[1]);
            }

            txtStep.setText(formulaDisplay);
            linearLayout.addView(txtStep);
        }
    }


    public SimpleCursorAdapter setSpinnerAdapter(Cursor c, String[] projection){
        int[] views = new int[]{android.R.id.text1};
        return new SimpleCursorAdapter(this,
                android.R.layout.simple_spinner_dropdown_item, c, projection, views, 1);
    }

    public TextWatcher createTextWatcher(final String variable){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    values[findVariableIndex(variable)][2] = charSequence.toString();
                } catch (Exception ex){
                    values[findVariableIndex(variable)][2] = null;
                }
                substituteValues();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(checkValues()){
                    calculate();
                } else {
                    resetSteps();
                }
            }
        };
    }

    private boolean checkValues() {
        for(String[] s : values){
            if(s[2].equals(""))
                return false;
        }
        return true;
    }

    public void substituteValues(){
        String formula = currentFormula;
        //Substitute values
        for(String[] s : values){
            formula = formula.replace(s[0], s[2] + s[1].replace("\\(", "{").replace("\\)", "}"));
        }
        formula = formula.replace("/", "\\over");
        MathView txtSub = (MathView)findViewById(R.id.text_substitute);
        txtSub.setText("$$" + variableToSolve + " = {" + formula + "}$$");
    }

    public int findVariableIndex(String variable){
        for(int i = 0; i < values.length; i++){
            if(values[i][0].equals(variable))
                return i;
        }
        return -1;
    }

    public void resetSteps(){
        LinearLayout stepsContainer = (LinearLayout)findViewById(R.id.steps_container);
        stepsContainer.removeAllViews();
    }
}
