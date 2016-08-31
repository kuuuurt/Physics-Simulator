package com.ps.physicssimulator;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ps.physicssimulator.data.DataContract;
import com.ps.physicssimulator.utils.ConversionUtils;
import com.ps.physicssimulator.utils.ExpressionBuilderModified;
import com.ps.physicssimulator.utils.ExpressionModified;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.measure.unit.NonSI;

import io.github.kexanie.library.MathView;

public class CalculatorActivity extends AppCompatActivity {

    ExpressionBuilderModified expressionBuilder;
    ExpressionModified expression;
    String currentFormula;
    String variableToSolve;
    String mChapter, mLesson;
    String formulaName;
    String[][] values;
    Button btnCalc;
    Bundle b;
    boolean fromConstants, fromLesson;
    Spinner spnChapters, spnLessons, spnFormula, spnVar;

    List<TextInputLayout> inputFields = new ArrayList<>();
    List<Spinner> inputSpinners = new ArrayList<>();
    List<ConversionUtils> conversionHelpers = new ArrayList<>();
    List<MathView> inputConversion = new ArrayList<>();

    String finalUnit;
    int finalUnitIndex;
    String finalType;
    ConversionUtils finalConversionHelper;

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
                        null, null, null, "HasCalc"),
                new String[]{DataContract.ChapterEntry.COLUMN_NAME}
        );

        spnChapters = (Spinner) findViewById(R.id.spinner_chapters);
        spnLessons = (Spinner) findViewById(R.id.spinner_lessons);
        spnFormula = (Spinner) findViewById(R.id.spinner_formula);
        spnVar = (Spinner) findViewById(R.id.spinner_variable);

        final Intent intent = getIntent();
        fromConstants = intent != null && intent.hasExtra("currentChapter");
        fromLesson = intent != null && intent.hasExtra("Lesson");

        btnCalc = (Button) findViewById(R.id.button_calculate);
        btnCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculate();
            }
        });

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

                                                TableLayout inputContainer = (TableLayout)findViewById(R.id.table_input);
                                                inputContainer.removeViews(1, inputContainer.getChildCount()-1);

                                                LinearLayout constContainer = (LinearLayout) findViewById(R.id.const_container);
                                                constContainer.removeAllViews();

                                                inputFields.clear();
                                                inputSpinners.clear();
                                                conversionHelpers.clear();
                                                inputConversion.clear();

                                                List<String> constValues = new ArrayList<>();

                                                for (int k = 0; k < c.getCount(); k++) {

                                                    if (k != i) {
                                                        String symbolC = c.getString(c.getColumnIndex(DataContract.VariableEntry.COLUMN_SYMBOL_COMPUTE));
                                                        String symbolD = c.getString(c.getColumnIndex(DataContract.VariableEntry.COLUMN_SYMBOL_DISPLAY));
                                                        final String unit = c.getString(c.getColumnIndex(DataContract.VariableEntry.COLUMN_UNIT));
                                                        String type = c.getString(c.getColumnIndex(DataContract.VariableEntry.COLUMN_UNIT_TYPE));
                                                        long const_id = c.getLong(c.getColumnIndex(DataContract.VariableEntry.COLUMN_CONSTANT_KEY));

                                                        expressionBuilder.variable(symbolC);
                                                        if(!fromConstants) {
                                                            values[varCtr][0] = symbolC;
                                                            values[varCtr][1] = unit;
                                                            values[varCtr][3] = symbolD;
                                                        }
                                                        String val = "";

                                                        if (const_id == -1) {
                                                            //Edit Text for values
                                                            TextInputLayout txtLayout = new TextInputLayout(CalculatorActivity.this);
                                                            txtLayout.setLayoutParams(new TableRow.LayoutParams(
                                                                    0,
                                                                    TableRow.LayoutParams.WRAP_CONTENT,
                                                                    0.33f
                                                            ));
                                                            final TextInputEditText txtInput = new TextInputEditText(CalculatorActivity.this);
                                                            txtInput.setLayoutParams(new TableRow.LayoutParams(
                                                                    TableRow.LayoutParams.MATCH_PARENT,
                                                                    TableRow.LayoutParams.WRAP_CONTENT
                                                            ));
                                                            txtInput.setHint(c.getString(c.getColumnIndex(DataContract.VariableEntry.COLUMN_NAME)) +
                                                                    " (" +
                                                                    c.getString(c.getColumnIndex(DataContract.VariableEntry.COLUMN_SYMBOL_DISPLAY))
                                                                            .replace("_", "")
                                                                            .replace("{", "")
                                                                            .replace("}", "")
                                                                            .replace("\\", "")
                                                                    + ")");
                                                            txtInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
                                                            txtInput.setFilters(new InputFilter[] {new InputFilter.LengthFilter(15)});
                                                            txtInput.setEms(10);
                                                            txtInput.addTextChangedListener(createTextWatcher(symbolC, varCtr));


                                                            txtLayout.addView(txtInput);
                                                            inputFields.add(txtLayout);
                                                            //End of Edit Text for Values

                                                            //Conversion result
                                                            final MathView txtConversion = new MathView(CalculatorActivity.this, null);
                                                            txtConversion.setLayoutParams(new TableRow.LayoutParams(
                                                                    0,
                                                                    TableRow.LayoutParams.WRAP_CONTENT,
                                                                    0.33f
                                                            ));
                                                            txtConversion.setPadding(16, 0, 0, 0);
                                                            txtConversion.setText("\\(" + unit + "\\)");
                                                            //txtConversion.setGravity(Gravity.CENTER|Gravity.BOTTOM);
                                                            inputConversion.add(txtConversion);
                                                            //End of Conversion result

                                                            //Spinners for Units
                                                            final ConversionUtils conversionHelper = new ConversionUtils();
                                                            Spinner spnInpUnit = new Spinner(CalculatorActivity.this);
                                                            List<String> spnData = new ArrayList<String>();
                                                            int typeIndex = ConversionUtils.findTypeIndex(type);
                                                            if(typeIndex == -1){
                                                                spnInpUnit.setEnabled(false);
                                                                spnData.add("None");
                                                            } else {
                                                                spnData = conversionHelper.populateLists(type,
                                                                        ConversionUtils.baseUnits[typeIndex],
                                                                        ConversionUtils.keywords[typeIndex]);
                                                            }
                                                            spnInpUnit.setLayoutParams(new TableRow.LayoutParams(
                                                                    0,
                                                                    TableRow.LayoutParams.WRAP_CONTENT,
                                                                    0.33f
                                                            ));
                                                            spnInpUnit.setAdapter(new ArrayAdapter<>(CalculatorActivity.this, android.R.layout.simple_spinner_dropdown_item,
                                                                spnData));
                                                            spnInpUnit.setPadding(8,0,0,0);

                                                            spnInpUnit.setSelection(conversionHelper.defaultUnit);
                                                            final int finalVarCtr = varCtr;
                                                            spnInpUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                                @Override
                                                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                                    String value = txtInput.getText().toString();
                                                                    try {
                                                                        Double converted = ConversionUtils.convertValue(value, conversionHelper, i, conversionHelper.defaultUnit);
                                                                        value = ConversionUtils.convertExponentialNotation(converted);
                                                                        values[finalVarCtr][2] = value;
                                                                        txtConversion.setText("\\(" + value + unit + "\\)");
                                                                        substituteValues();
                                                                    } catch (Exception ex) {
                                                                        txtConversion.setText("\\(" + unit + "\\)");
                                                                    }
                                                                }

                                                                @Override
                                                                public void onNothingSelected(AdapterView<?> adapterView) {

                                                                }
                                                            });

                                                            inputSpinners.add(spnInpUnit);
                                                            conversionHelpers.add(conversionHelper);
                                                            if(fromConstants)
                                                                txtInput.setText(values[varCtr][2]);
                                                            //End of Spinners for Units



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
                                                            inputFields.add(null);
                                                            inputSpinners.add(null);
                                                            inputConversion.add(null);
                                                            conversionHelpers.add(null);
                                                            values[varCtr][2] = val;
                                                        }
                                                        if(!fromConstants)
                                                            values[varCtr][2] = val;
                                                        varCtr++;

                                                    } else {
                                                        finalUnit = c.getString(c.getColumnIndex(DataContract.VariableEntry.COLUMN_UNIT));
                                                        finalType = c.getString(c.getColumnIndex(DataContract.VariableEntry.COLUMN_UNIT_TYPE));
                                                        Spinner spnFinal = (Spinner)findViewById(R.id.spinner_unit_final);
                                                        finalConversionHelper = new ConversionUtils();
                                                        int typeIndex = ConversionUtils.findTypeIndex(finalType);
                                                        if(typeIndex > 0) {
                                                            List<String> units = finalConversionHelper.populateLists(finalType, finalConversionHelper.baseUnits[typeIndex],
                                                                    finalConversionHelper.keywords[typeIndex]);
                                                            spnFinal.setAdapter(new ArrayAdapter<String>(CalculatorActivity.this, android.R.layout.simple_spinner_dropdown_item, units));
                                                            spnFinal.setSelection(finalConversionHelper.defaultUnit);
                                                            spnFinal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                                @Override
                                                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                                    finalUnitIndex = i;
                                                                }

                                                                @Override
                                                                public void onNothingSelected(AdapterView<?> adapterView) {

                                                                }
                                                            });
                                                        } else {
                                                            spnFinal.setVisibility(View.GONE);
                                                        }

                                                    }
                                                    c.moveToNext();
                                                }

                                                for (int j = 0; j < inputFields.size(); j++) {
                                                    TableRow layoutInput = new TableRow(CalculatorActivity.this);
                                                    layoutInput.setLayoutParams(new TableRow.LayoutParams(
                                                            TableRow.LayoutParams.MATCH_PARENT,
                                                            TableRow.LayoutParams.WRAP_CONTENT,
                                                            1.0f
                                                    ));
                                                    layoutInput.setWeightSum(1.0f);
                                                    layoutInput.setGravity(Gravity.CENTER);
                                                    try{
                                                        layoutInput.addView(inputFields.get(j));
                                                        layoutInput.addView(inputSpinners.get(j));
                                                        layoutInput.addView(inputConversion.get(j));
                                                        inputContainer.addView(layoutInput);
                                                    } catch(Exception ex) {}

                                                }
                                                if (checkValues())
                                                    btnCalc.setEnabled(true);
                                                else
                                                    btnCalc.setEnabled(false);
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
            expression.setVariable(s[0], Double.parseDouble(s[2].replace("(", "").replace(")", "")), s[1]);
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
            try {
                String result[] = (String[]) steps[i];
                if (Double.parseDouble(result[0].toString().replace("(", "").replace(")", "")) % 1 == 0) {
                    result[0] = result[0].replace(".0", "");
                }

                String strForm = result[2].toString();
                if (strForm.contains(" ^ ")) {
                    int idx = strForm.indexOf("^");
                    strForm = "(" + strForm.substring(0, idx - 1) + ")" + strForm.substring(idx - 1);
                }

                if (formulaDisplay.contains(strForm)) {
                    if (formulaDisplay.contains("(" + strForm + ")")) {
                        formulaDisplay = formulaDisplay.replace("(" + strForm + ")", strForm);
                    }

                    if (formulaDisplay.contains("{" + strForm + "}")) {
                        formulaDisplay = formulaDisplay.replace("{" + strForm + "}", strForm);
                    }
                    if(i == steps.length-1)
                        formulaDisplay = formulaDisplay.replace(strForm, result[0] + finalUnit);
                    else
                        formulaDisplay = formulaDisplay.replace(strForm, result[0] + result[1]);
                    txtStep.setText(formulaDisplay);
                    linearLayout.addView(txtStep);

                    if(i == steps.length-1 && !finalType.equals("None") && (finalUnitIndex != finalConversionHelper.defaultUnit)){ //Display Conversion Steps
                        String formula = "{{c} \\cdot {{t} \\over {f}}}";

                        TextView conversionHeader = (TextView)findViewById(R.id.text_conversion_divider);
                        conversionHeader.setVisibility(View.VISIBLE);

                        formulaDisplay = formula;
                        formulaDisplay = formulaDisplay.replace("{c}", result[0] + result[1]) ;
                        double num = 1;
                        double den = 1;
                        if(finalUnitIndex < finalConversionHelper.defaultUnit) {
                            num = finalConversionHelper.unitFactor.get(finalUnitIndex);

                        } else {
                            den = finalConversionHelper.unitFactor.get(finalUnitIndex);
                        }
                        String strNum = ConversionUtils.convertExponentialNotation(num);
                        String strDen = ConversionUtils.convertExponentialNotation(den);
                        formulaDisplay = formulaDisplay.replace("{t}", strNum + "{" + finalConversionHelper.unitSymbol.get(finalUnitIndex) + "}");
                        formulaDisplay = formulaDisplay.replace("{f}", strDen + result[1]);

                        LinearLayout linearLayoutCon = (LinearLayout) findViewById(R.id.steps_container_conversion);
                        linearLayoutCon.removeAllViews();

                        ExpressionModified expressionCon = new ExpressionBuilderModified("x * (y / z)").variable("x").variable("y").variable("z").build();
                        expressionCon.setVariable("y", num, "{" + finalConversionHelper.unitSymbol.get(finalUnitIndex).toString() + "}");
                        expressionCon.setVariable("z", den, result[1]);
                        formulaDisplay = formulaDisplay.replace("µ", "\\mu ").replace(NonSI.DEGREE_ANGLE.toString(),"^{{\\circ}}");
                        expressionCon.setVariable("x", Double.parseDouble(result[0].replace("(", "").replace(")", "")), result[1]);
                        results = expressionCon.evaluate();
                        steps = results.toArray();

                        MathView txtSubs = new MathView(CalculatorActivity.this, null);
                        txtSubs.setLayoutParams(new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        ));
                        txtSubs.setEngine(MathView.Engine.KATEX);
                        txtSubs.setText("$$" + result[0] + result[1] + " = " + formulaDisplay + "$$");
                        linearLayoutCon.addView(txtSubs);

                        for (int k = 0; k < steps.length; k++) {
                            MathView txtConversion = new MathView(CalculatorActivity.this, null);
                            txtConversion.setLayoutParams(new LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                            ));
                            txtConversion.setEngine(MathView.Engine.KATEX);
                            try {
                                String resultCon[] = (String[]) steps[k];
                                if (Double.parseDouble(resultCon[0].toString().replace("(", "").replace(")","")) % 1 == 0) {
                                    resultCon[0] = resultCon[0].replace(".0", "");
                                }

                                String strForm2 = resultCon[2].toString();
                                if (strForm2.contains(" ^ ")) {
                                    int idx = strForm.indexOf("^");
                                    strForm2 = "(" + strForm2.substring(0, idx - 1) + ")" + strForm2.substring(idx - 1);
                                }

                                strForm2 = strForm2.replace("µ", "\\mu ");

                                if (formulaDisplay.contains(strForm2)) {
                                    if (formulaDisplay.contains("(" + strForm2 + ")")) {
                                        formulaDisplay = formulaDisplay.replace("(" + strForm2 + ")", strForm2);
                                    }

                                    if (formulaDisplay.contains("{" + strForm2 + "}")) {
                                        formulaDisplay = formulaDisplay.replace("{" + strForm2 + "}", strForm2);
                                    }

                                    formulaDisplay = formulaDisplay.replace(strForm2, resultCon[0] + resultCon[1].replace("µ", "\\mu ").replace(NonSI
                                            .DEGREE_ANGLE
                                            .toString(),
                                            "^{{\\circ}}"));

                                    txtConversion.setText("$$" + result[0] + result[1] + " = " + formulaDisplay + "$$");
                                    linearLayoutCon.addView(txtConversion);
                                }
                            } catch (Exception ex){
                                txtConversion.setText("$$Error! Division  by Zero!$$");
                                linearLayoutCon.addView(txtConversion);
                                break;
                            }
                        }
                        break;
                    }
                }
            } catch (Exception ex){
                txtStep.setText("$$Error! Division  by Zero!$$");
                linearLayout.addView(txtStep);
                break;
            }

        }
    }

    private void removeEditTextFocus() {
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.input_container);
        try {
            ((InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                    linearLayout.getFocusedChild().getWindowToken(),0);
        } catch (Exception ex) {}
    }

    public TextInputEditText getFocusedEditText() {
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.input_container);
        for(int i = 0; i < linearLayout.getChildCount(); i++) {
            LinearLayout inputContainer = (LinearLayout)linearLayout.getChildAt(i);
            for(int l = 0; l < inputContainer.getChildCount(); l++) {
                TextInputEditText editText = (TextInputEditText)(((TextInputLayout)inputContainer.getChildAt(l)).getChildAt(0));
                if(editText.hasFocus())
                    return editText;
            }
        }
        return null;
    }


    public SimpleCursorAdapter setSpinnerAdapter(Cursor c, String[] projection) {
        int[] views = new int[]{android.R.id.text1};
        return new SimpleCursorAdapter(this,
                android.R.layout.simple_spinner_dropdown_item, c, projection, views, 1);
    }

    public TextWatcher createTextWatcher(final String variable, final int index) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                try {
                    String text = charSequence.toString();
                    if (!text.contains(".")) {
                        for (int x = 0; x < text.length(); x++) {
                            if (text.charAt(x) != '0') {
                                text = text.substring(x);
                                break;
                            }
                            if (x == text.length() - 1) {
                                text = "0";
                            }
                        }
                    } else {
                        for (int x = 0; x < text.indexOf("."); x++) {
                            if (text.charAt(x) != '0') {
                                text = text.substring(x);
                                break;
                            }
                            if (x == text.indexOf(".") - 1) {
                                text = "0" + text.substring(text.indexOf("."));
                            }
                        }
                    }
                    if (text.charAt(0) == '-')
                        if (text.length() == 1)
                            text = "";

                    if (text.charAt(text.length() - 1) == '.')
                        text = text + "0";

                    int idx = text.indexOf(".");
                    String zeroTemp = "";
                    if (idx != -1 && idx < text.length()) {
                        for (int z = idx; z < text.length() - 1; z++)
                            zeroTemp += 0;
                        if (text.substring(idx + 1).equals(zeroTemp)) {
                            text = text.substring(0, idx);
                            if (Double.parseDouble(text.substring(0, idx)) == 0)
                                text = "0";
                        }
                    }
                    Double textValue = Double.parseDouble(text);
                    text = ConversionUtils.convertExponentialNotation(textValue);

                    ConversionUtils helper = conversionHelpers.get(index);
                    try{
                        int unit = inputSpinners.get(index).getSelectedItemPosition();
                        Double converted = ConversionUtils.convertValue(text, helper, unit, helper.defaultUnit);
                        text = ConversionUtils.convertExponentialNotation(converted);
                    } catch(Exception ex) {}
                    inputConversion.get(index).setText("\\(" + text + values[index][1] + "\\)");
                    values[index][2] = text;

                    if (checkValues())
                        btnCalc.setEnabled(true);
                    else
                        btnCalc.setEnabled(false);

                } catch (Exception ex) {
                    try {
                        values[index][2] = "";
                        inputConversion.get(index).setText("\\(" + values[index][1] + "\\)");
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
            if (s[2].equals("") || s[2].equals(".") || s[2].equals("-"))
                return false;
        }
        return true;
    }

    public void substituteValues() {
        String formula = currentFormula;
        formula = formula.replace("*", "\\cdot");
        formula = formula.replace("/", "\\over");
        formula = formula.replace("\\pi", String.valueOf(new DecimalFormat("#.####").format(Math.PI)));

        //Substitute values
        for (String[] s : values) {
            formula = formula.replace(s[3], s[2] + s[1]);
        }


        MathView txtSub = (MathView) findViewById(R.id.text_substitute);
        txtSub.setText(formula);
        //txtSub.setText("$$" + variableToSolve + " = {" + formula + "}$$");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_converter) {
            Intent intent = new Intent(this, ConverterActivity.class);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_converter, menu);
        return true;
    }


    public void resetSteps() {
        LinearLayout stepsContainer = (LinearLayout) findViewById(R.id.steps_container);
        stepsContainer.removeAllViews();
    }

}
