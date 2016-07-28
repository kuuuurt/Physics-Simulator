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
import com.ps.physicssimulator.utils.ExpressionBuilderModified;
import com.ps.physicssimulator.utils.ExpressionModified;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.github.kexanie.library.MathView;

public class CalculatorActivity extends AppCompatActivity {

    static ExpressionBuilderModified expressionBuilder;
    static ExpressionModified expression;
    static String resultUnit;
    static String currentFormula;
    static String variableToSolve;
    static String[][] values;
    static Button btnSteps;
    static Button btnCalc;


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
                                spnVar.setOnItemSelectedListener(
                                        new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        Cursor selected = (Cursor)varAdap.getItem(i);

                                        MathView txtResult = (MathView)findViewById(R.id.text_result);
                                        txtResult.setText("");

                                        String formula = selected.getString(selected.getColumnIndex(DataContract.VariableEntry.COLUMN_FORMULA_DISPLAY));

                                        MathView txtFormula = (MathView)findViewById(R.id.text_formula);
                                        txtFormula.setText(formula);

                                        MathView txtSub = (MathView)findViewById(R.id.text_substitute);
                                        txtSub.setText(formula);

                                        resultUnit = selected.getString(selected.getColumnIndex(DataContract.VariableEntry.COLUMN_UNIT));
                                        currentFormula = selected.getString(selected.getColumnIndex(DataContract.VariableEntry.COLUMN_FORMULA_COMPUTE));
                                        variableToSolve = selected.getString(selected.getColumnIndex(DataContract.VariableEntry.COLUMN_SYMBOL));
                                        expressionBuilder = new ExpressionBuilderModified(currentFormula);


                                        final Cursor c = varAdap.getCursor();
                                        c.moveToFirst();


                                        values = new String[c.getCount() - 1][3];
                                        int varCtr = 0;

                                        LinearLayout linearLayout = (LinearLayout)
                                                findViewById(R.id.input_container);
                                        linearLayout.removeAllViews();

                                        for(int k = 0; k < c.getCount(); k++){
                                            if(k != i){
                                                String symbol = c.getString(c.getColumnIndex(DataContract.VariableEntry.COLUMN_SYMBOL));
                                                String unit = c.getString(c.getColumnIndex(DataContract.VariableEntry.COLUMN_UNIT));

                                                expressionBuilder.variable(symbol);
                                                values[varCtr][0] = symbol;
                                                values[varCtr][1] = unit;
                                                values[varCtr++][2] = "";

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
                                                txtInput.addTextChangedListener(createTextWatcher(symbol));

                                                MathView txtUnit = new MathView(CalculatorActivity.this, null);
                                                txtUnit.setLayoutParams(new LinearLayout.LayoutParams(
                                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                                        ViewGroup.LayoutParams.WRAP_CONTENT,
                                                        0.3f
                                                ));
                                                txtUnit.setEngine(MathView.Engine.MATHJAX);
                                                txtUnit.setText(unit);

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
        btnCalc = (Button)findViewById(R.id.button_calculate);
        btnCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MathView txtResult = (MathView)findViewById(R.id.text_result);
                if(checkValues()) {
                    for(String[] s : values){
                        expression.setVariable(s[0], Double.parseDouble(s[2]));
                    }
                    Map<String, Double> results = expression.evaluate();
                    Object[] rep = results.keySet().toArray();
                    Object[] res = results.values().toArray();

                    String formula = currentFormula;

                    for(String[] s : values){
                        formula = formula.replace(s[0], s[2]);
                    }

                    LinearLayout linearLayout = (LinearLayout)findViewById(R.id.steps_container);

                    for(int i = results.size()-1; i >= 0; i--){
                        MathView txtStep = new MathView(CalculatorActivity.this, null);
                        txtStep.setLayoutParams(new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        ));
                        txtStep.setEngine(MathView.Engine.MATHJAX);
                        String result = res[i].toString();
                        if(Double.parseDouble(res[i].toString()) % 1 == 0){
                            result = result.replace(".0", "");
                        }
                        if(formula.contains("(" + rep[i].toString() + ")")){
                            formula = formula.replace("(" + rep[i].toString() + ")", rep[i].toString());
                        }

                        formula = formula.replace(rep[i].toString(), result);
                        if(i == 0){
                            formula += resultUnit; //Format unit
                        }
                        txtStep.setText("$$" + variableToSolve + " = {" + formula + "}$$");
                        linearLayout.addView(txtStep);
                    }

                    //txtResult.setText("$$Result: " + String.valueOf(expression.evaluate()) + resultUnit.replace("\\(", "{").replace("\\)", "}") + "$$");
                } else {
                    //display error not all values set
                }
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
                    values[findVariableIndex(variable)][2] = charSequence.toString();
                    if(checkValues()){
                        btnCalc.setEnabled(true);
                    } else {
                        btnCalc.setEnabled(false);
                    }
                    //values.put(variable, Double.parseDouble();
                    //expression.setVariable(variable, Double.parseDouble(charSequence.toString()));

                } catch (Exception ex){
                    values[findVariableIndex(variable)][2] = null;
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
        for(String[] s : values){
            if(s[2].equals(""))
                return false;
        }
        return true;
    }

    public Object[] getSteps(){

        List<String> steps = new ArrayList<String>();
        //steps.add()

        String formula = currentFormula;
        String formattedFormula = formula;
        //Substitute values
        for(String[] s : values){
            formattedFormula = formattedFormula.replace(s[0], s[2] + s[1].replace("\\(", "{").replace("\\)", "}"));
            formula = formula.replace(s[0], s[2]);
        }
        formattedFormula = formattedFormula.replace("/", "\\over");
        steps.add(formattedFormula);
        //Calculate
        String[] componentsArray = formula.split(" ");
        List<String> components = Arrays.asList(componentsArray);
        while(hasOperators(components)){
            if(formula.contains("/")){
                for(int i = 0; i < componentsArray.length; i++){
                    if(componentsArray[i].equals("/")){

                        String strNum1 = componentsArray[i-1];
                        String strNum2 = componentsArray[i+1];

                        double result = Double.parseDouble(strNum1) / Double.parseDouble(strNum2);
                        formula = formula.replace(strNum1 + " / " + strNum2, String.valueOf(result));
                        formattedFormula = formula;
                        steps.add(formattedFormula);

                        componentsArray = formula.split(" ");
                        components = Arrays.asList(componentsArray);

                    }
                }
            }
        }


//        Log.d("asdf", values.keySet().toArray()[1].toString());
//        Log.d("asdf", values.values().toArray()[1].toString());
        return steps.toArray();
    }

    public boolean hasOperators(List<String> components){
        String[][] operators = {
                {"(", ")"},
                {"/", "*"},
                {"+", "-"}
        };
        for(String s : components){
            for(String[] o : operators){
                for(String p : o){
                    if(s.contains(p)){
                         return true;
                    }
                }
            }
        }
        return false;
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
}
