package com.ps.physicssimulator;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.ps.physicssimulator.data.DataContract;
import com.ps.physicssimulator.utils.ExpressionBuilderModified;
import com.ps.physicssimulator.utils.ExpressionModified;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.github.kexanie.library.MathView;

public class CalculatorActivity extends AppCompatActivity {

    static ExpressionBuilderModified expressionBuilder;
    static ExpressionModified expression;
    static String currentFormula;
    static String variableToSolve;
    String mChapter, mLesson;
    static String formulaName;
    static String[][] values;
    static Button btnCalc;
    static Bundle b;
    static boolean fromConstants, fromLesson;

    @Nullable
    @Override
    public Intent getSupportParentActivityIntent() {
        if(mChapter != null && mLesson != null){
            return new Intent(this, ContentActivity.class)
                    .putExtra("Chapter", mChapter)
                    .putExtra("Lesson", mLesson);

        }

        return super.getSupportParentActivityIntent();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Button btnChangeConstants = (Button) findViewById(R.id.button_change_constants);

        final SimpleCursorAdapter chaptersAdap = setSpinnerAdapter(
                this.getContentResolver().query(DataContract.ChapterEntry.CONTENT_URI,
                        null, null, null, null),
                new String[]{DataContract.ChapterEntry.COLUMN_NAME}
        );

        final Spinner spnChapters = (Spinner) findViewById(R.id.spinner_chapters);
        final Spinner spnLessons = (Spinner) findViewById(R.id.spinner_lessons);
        final Spinner spnFormula = (Spinner) findViewById(R.id.spinner_formula);
        final Spinner spnVar = (Spinner) findViewById(R.id.spinner_variable);

        final Intent intent = getIntent();
        fromConstants = intent != null && intent.hasExtra("currentChapter");
        fromLesson = intent != null && intent.hasExtra("Lesson");

        spnChapters.setAdapter(chaptersAdap);
        if(fromConstants) {
            b = intent.getExtras();
            values = new String[b.getInt("size")][];
            for (int i = 0; i < values.length; i++) {
                values[i] = intent.getStringArrayExtra("value" + i);
            }
            spnChapters.setSelection(b.getInt("currentChapter"));
        }
        if(fromLesson){
            b = intent.getExtras();
            mChapter = b.getString("Chapter");
            mLesson = b.getString("Lesson");
            spnChapters.setSelection(getTextIndex(chaptersAdap.getCursor(), DataContract.ChapterEntry.COLUMN_NAME,
                    mChapter));
        }

        spnChapters.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor c = (Cursor) chaptersAdap.getItem(i);
                String chapter =
                        c.getString(c.getColumnIndex(DataContract.ChapterEntry.COLUMN_NAME));

                final SimpleCursorAdapter lessonsAdap = setSpinnerAdapter(
                        CalculatorActivity.this.getContentResolver().query(
                                DataContract.LessonEntry.buildLessonChapter(chapter),
                                null, null, null, "HasCalc"),
                        new String[]{DataContract.LessonEntry.COLUMN_NAME}
                );


                spnLessons.setAdapter(lessonsAdap);
                if(fromConstants)
                    spnLessons.setSelection(b.getInt("currentLesson"));
                if(fromLesson){
                    spnLessons.setSelection(getTextIndex(lessonsAdap.getCursor(), DataContract.LessonEntry.COLUMN_NAME,
                            mLesson));
                }
                spnLessons.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i,
                                               long l) {
                        Cursor c = (Cursor) lessonsAdap.getItem(i);
                        String lesson = c.getString(c.getColumnIndex(DataContract.LessonEntry
                                .COLUMN_NAME));

                        final SimpleCursorAdapter formulaAdap = setSpinnerAdapter(
                                CalculatorActivity.this.getContentResolver().query(
                                        DataContract.FormulaEntry.buildFormulaLesson(lesson),
                                        null, null, null, null),
                                new String[]{DataContract.FormulaEntry.COLUMN_NAME}
                        );


                        spnFormula.setAdapter(formulaAdap);
                        if(fromConstants)
                            spnFormula.setSelection(b.getInt("currentFormula"));
                        spnFormula.setOnItemSelectedListener(new AdapterView
                                .OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view,
                                                       int i, long l) {
                                Cursor c = (Cursor) formulaAdap.getItem(i);
                                formulaName = c.getString(c.getColumnIndex(DataContract.FormulaEntry.COLUMN_NAME));

                                MathView txtFormula = (MathView) findViewById(R.id.text_main_formula);
                                txtFormula.setText(c.getString(c.getColumnIndex(DataContract.FormulaEntry.COLUMN_FORMULA)));


                                final SimpleCursorAdapter varAdap = setSpinnerAdapter(
                                        CalculatorActivity.this.getContentResolver().query(
                                                DataContract.VariableEntry.buildVariableFormula(
                                                        formulaName), null, null, null, null),
                                        new String[]{DataContract.VariableEntry.COLUMN_NAME}
                                );


                                spnVar.setAdapter(varAdap);
                                if(fromConstants)
                                    spnVar.setSelection(b.getInt("currentVariable"));
                                spnVar.setOnItemSelectedListener(
                                        new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                Cursor selected = (Cursor) varAdap.getItem(i);

                                                resetSteps();
                                                String formula = selected.getString(selected.getColumnIndex(DataContract.VariableEntry.COLUMN_FORMULA_DISPLAY));

                                                MathView txtFormula = (MathView) findViewById(R.id.text_formula);
                                                txtFormula.setText(formula);

                                                currentFormula = selected.getString(selected.getColumnIndex(DataContract.VariableEntry.COLUMN_FORMULA_DISPLAY));
                                                variableToSolve = selected.getString(selected.getColumnIndex(DataContract.VariableEntry.COLUMN_SYMBOL_DISPLAY));
                                                expressionBuilder = new ExpressionBuilderModified(selected.getString(selected.getColumnIndex(DataContract.VariableEntry.COLUMN_FORMULA_COMPUTE)));


                                                Cursor c = varAdap.getCursor();
                                                c.moveToFirst();

                                                int varCtr = 0;
                                                if(!fromConstants) {
                                                    values = new String[c.getCount() - 1][4];
                                                }

                                                LinearLayout inputContainer = (LinearLayout) findViewById(R.id.input_container);
                                                inputContainer.removeAllViews();

                                                LinearLayout constContainer = (LinearLayout) findViewById(R.id.const_container);
                                                constContainer.removeAllViews();

                                                List<TextInputLayout> inputFields = new ArrayList<>();
                                                List<String> constValues = new ArrayList<>();

                                                for (int k = 0; k < c.getCount(); k++) {

                                                    if (k != i) {
                                                        String symbolC = c.getString(c.getColumnIndex(DataContract.VariableEntry.COLUMN_SYMBOL_COMPUTE));
                                                        String symbolD = c.getString(c.getColumnIndex(DataContract.VariableEntry.COLUMN_SYMBOL_DISPLAY));
                                                        String unit = c.getString(c.getColumnIndex(DataContract.VariableEntry.COLUMN_UNIT));
                                                        long const_id = c.getLong(c.getColumnIndex(DataContract.VariableEntry.COLUMN_CONSTANT_KEY));

                                                        expressionBuilder.variable(symbolC);
                                                        if(!fromConstants) {
                                                            values[varCtr][0] = symbolC;
                                                            values[varCtr][1] = unit;
                                                            values[varCtr][3] = symbolD;
                                                        }
                                                        String val = "";

                                                        if (const_id == -1) {
                                                            TextInputLayout txtLayout = new TextInputLayout(CalculatorActivity.this);
                                                            txtLayout.setLayoutParams(new LinearLayout.LayoutParams(
                                                                    0,
                                                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                                                    0.5f
                                                            ));
                                                            TextInputEditText txtInput = new TextInputEditText(CalculatorActivity.this);
                                                            txtInput.setLayoutParams(new LinearLayout.LayoutParams(
                                                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                                            ));
                                                            txtInput.setHint(c.getString(c.getColumnIndex(DataContract.VariableEntry.COLUMN_NAME)) +
                                                                    " (" +
                                                                    c.getString(c.getColumnIndex(DataContract.VariableEntry.COLUMN_SYMBOL_DISPLAY))
                                                                            .replace("_", "")
                                                                            .replace("{", "")
                                                                            .replace("}", "")
                                                                    + ")");
                                                            txtInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
                                                            txtInput.setEms(10);
                                                            txtInput.addTextChangedListener(createTextWatcher(symbolC));
                                                            if(fromConstants)
                                                                txtInput.setText(values[varCtr][2]);

                                                            txtLayout.addView(txtInput);
                                                            inputFields.add(txtLayout);

                                                        } else {
                                                            Cursor a = CalculatorActivity.this.getContentResolver().query(
                                                                    DataContract.ConstantEntry.buildConstantUri(const_id),
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    null);
                                                            a.moveToFirst();
                                                            val = a.getString(a.getColumnIndex(DataContract.ConstantEntry.COLUMN_CURRENT));

                                                            String value = a.getString(a.getColumnIndex(DataContract.ConstantEntry.COLUMN_SYMBOL)) + " = " + val + unit;
                                                            constValues.add(value);
                                                            values[varCtr][2] = val;
                                                        }
                                                        if(!fromConstants)
                                                            values[varCtr][2] = val;
                                                        varCtr++;
                                                    }
                                                    c.moveToNext();
                                                }


                                                for (int j = 0; j < inputFields.size(); j++) {
                                                    LinearLayout layoutInput = new LinearLayout(CalculatorActivity.this);
                                                    layoutInput.setLayoutParams(new LinearLayoutCompat.LayoutParams(
                                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                                            1.0f
                                                    ));
                                                    layoutInput.setOrientation(OrientationHelper.HORIZONTAL);
                                                    layoutInput.setPadding(0, 8, 0, 8);
                                                    layoutInput.setGravity(Gravity.CENTER_HORIZONTAL);
                                                    try {
                                                        layoutInput.addView(inputFields.get(j++));
                                                        layoutInput.addView(inputFields.get(j));
                                                    } catch (Exception ex) {

                                                    }
                                                    inputContainer.addView(layoutInput);
                                                }
                                                TextView txt = (TextView) findViewById(R.id.text_constants_label);

                                                if (constValues.size() > 0) {
                                                    txt.setVisibility(View.VISIBLE);
                                                    for (int j = 0; j < constValues.size(); j++) {
                                                        MathView txtConstant = new MathView(CalculatorActivity.this, null);
                                                        txtConstant.setLayoutParams(new LinearLayout.LayoutParams(
                                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                                ViewGroup.LayoutParams.WRAP_CONTENT
                                                        ));
                                                        txtConstant.setEngine(MathView.Engine.KATEX);
                                                        String constants = "";
                                                        try {
                                                            for (int o = 0; o < 5; o++) {
                                                                constants += constValues.get(j++) + ", ";
                                                            }
                                                        } catch (Exception ex) {

                                                        }
                                                        txtConstant.setText("$$" + constants.substring(0, constants.length() - 2) + "$$");
                                                        constContainer.addView(txtConstant);
                                                    }
                                                    btnChangeConstants.setVisibility(View.VISIBLE);

                                                } else {
                                                    txt.setVisibility(View.GONE);
                                                    constContainer.removeAllViews();
                                                    btnChangeConstants.setVisibility(View.GONE);
                                                }

                                                substituteValues();
                                                expression = expressionBuilder.build();
                                                fromConstants = false;
                                                fromLesson = false;
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {
                                            }
                                        });
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        //spnChapters.setSelection(1);
        btnChangeConstants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CalculatorActivity.this, ConstantActivity.class);
                intent.putExtra("currentChapter", spnChapters.getSelectedItemPosition());
                intent.putExtra("currentLesson", spnLessons.getSelectedItemPosition());
                intent.putExtra("currentFormula", spnFormula.getSelectedItemPosition());
                intent.putExtra("currentVariable", spnVar.getSelectedItemPosition());
                intent.putExtra("formulaName", formulaName);
                int i = 0;
                intent.putExtra("size", values.length);
                for (String[] value : values) {
                    intent.putExtra("value"+ i++, value);
                }
                startActivity(intent);
            }
        });

        btnCalc = (Button) findViewById(R.id.button_calculate);
        btnCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculate();
            }
        });

        if(fromConstants) {
            b = intent.getExtras();
            values = new String[b.getInt("size")][];
            for (int i = 0; i < values.length; i++) {
                values[i] = intent.getStringArrayExtra("value" + i);
            }
            spnChapters.setSelection(b.getInt("currentChapter"));
        }
    }

    public int getTextIndex(Cursor c, String column, String text){
        int i = 0;
        c.moveToFirst();
        while(i < c.getCount()){
            if(c.getString(c.getColumnIndex(column)).equals(text)){
                break;
            }
            c.moveToNext();
            i++;
        }
        return i;
    }


    public void calculate() {
        removeEditTextFocus();
        resetSteps();
        for (String[] s : values) {
            expression.setVariable(s[0], Double.parseDouble(s[2]), s[1]);
        }
        List<String> results = expression.evaluate();
        Object[] steps = results.toArray();

        MathView txtSub = (MathView) findViewById(R.id.text_substitute);

        String formulaDisplay = txtSub.getText();

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.steps_container);

        for (int i = 0; i < steps.length; i++) {
            MathView txtStep = new MathView(CalculatorActivity.this, null);
            txtStep.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            txtStep.setEngine(MathView.Engine.KATEX);
            String result[] = (String[]) steps[i];
            if (Double.parseDouble(result[0].toString()) % 1 == 0) {
                result[0] = result[0].replace(".0", "");
            }

            String strForm = result[2].toString();
            if (strForm.contains(" ^ ")) {
                int idx = strForm.indexOf("^");
                strForm = "(" + strForm.substring(0, idx - 1) + ")" + strForm.substring(idx - 1);
            }

            if(formulaDisplay.contains(strForm)) {
                if (formulaDisplay.contains("(" + strForm + ")")) {
                    formulaDisplay = formulaDisplay.replace("(" + strForm + ")", strForm);
                }

                if (formulaDisplay.contains("{" + strForm + "}")) {
                    formulaDisplay = formulaDisplay.replace("{" + strForm + "}", strForm);
                }

                formulaDisplay = formulaDisplay.replace(strForm, result[0] + result[1]);

                txtStep.setText(formulaDisplay);
                linearLayout.addView(txtStep);
            }
        }
    }

    private void removeEditTextFocus() {
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.input_container);
        try {
            linearLayout.getFocusedChild().clearFocus();
        } catch (Exception ex) {}
    }


    public SimpleCursorAdapter setSpinnerAdapter(Cursor c, String[] projection) {
        int[] views = new int[]{android.R.id.text1};
        return new SimpleCursorAdapter(this,
                android.R.layout.simple_spinner_dropdown_item, c, projection, views, 1);
    }

    public TextWatcher createTextWatcher(final String variable) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    values[findVariableIndex(variable)][2] = charSequence.toString();
                    if(checkValues())
                        btnCalc.setEnabled(true);
                    else
                        btnCalc.setEnabled(false);
                } catch (Exception ex) {
                    try {
                        values[findVariableIndex(variable)][2] = null;
                    } catch (Exception ex2) {}
                    btnCalc.setEnabled(false);
                }
                substituteValues();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
    }

    private boolean checkValues() {
        for (String[] s : values) {
            if (s[2].equals(""))
                return false;
        }
        return true;
    }

    public void substituteValues() {
        String formula = currentFormula;
        formula = formula.replace("*", "\\cdot");
        formula = formula.replace("/", "\\over");
        formula = formula.replace("\\pi", String.valueOf(new DecimalFormat("#.####").format(Math.PI)));
        //formula = formula.replace("(", "{").replace(")", "}");
//        if (formula.contains("^")) {
//            int i = formula.indexOf("^");
//            //if(formula.charAt(i-2) == ' ' || formula.charAt(i+1) == ' ')
//                formula = formula.substring(0, i) + "(" + formula.charAt(i) + ")" + formula.substring(i + 1);
//        }
        //Substitute values
        for (String[] s : values) {
            formula = formula.replace(s[3], s[2] + s[1]);
        }


        MathView txtSub = (MathView) findViewById(R.id.text_substitute);
        txtSub.setText(formula);
        //txtSub.setText("$$" + variableToSolve + " = {" + formula + "}$$");
    }

    public int findVariableIndex(String variable) {
        for (int i = 0; i < values.length; i++) {
            if (values[i][0].equals(variable))
                return i;
        }
        return -1;
    }

    public void resetSteps() {
        LinearLayout stepsContainer = (LinearLayout) findViewById(R.id.steps_container);
        stepsContainer.removeAllViews();
    }
}
