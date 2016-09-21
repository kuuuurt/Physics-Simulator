package com.ps.physicssimulator;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
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

    //Container for entered values
    String[][] values;

    //Calculator button
    Button btnCalc;

    //Intent flags and vars
    boolean fromConstants, fromLesson;
    Bundle b;

    //Constants
    List<String> constValues;
    Button btnChangeConstants;

    //Spinners
    Spinner spnChapters, spnLessons, spnFormula, spnVar;

    //Containers for generated views
    List<TextInputLayout> inputFields;
    List<Spinner> inputSpinners;
    List<ConversionUtils> conversionHelpers;
    List<MathView> inputConversion;

    //For final units
    String finalUnit;
    int finalUnitIndex;
    String finalType;
    ConversionUtils finalConversionHelper;

    //ActivityForResult
    int CONSTANT = 0;
    long constantId = 0;
    int constantIndex = 0;


    //Instuctions
    List<View> views;
    List<String> titles;
    List<String> instructions;
    boolean skip;
    boolean isScrollBlocked;
    ScrollView page;
    Toolbar toolbar;

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

    /**
     * Dispatch incoming result to the correct fragment.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CONSTANT){
            Cursor a = CalculatorActivity.this.getContentResolver().query(
                    DataContract.ConstantEntry.buildConstantUri(constantId),
                    null,
                    null,
                    null,
                    null);
            a.moveToFirst();
            String val = a.getString(a.getColumnIndex(DataContract.ConstantEntry.COLUMN_CURRENT));

            String value = a.getString(a.getColumnIndex(DataContract.ConstantEntry.COLUMN_SYMBOL)) + " = " + val + values[constantIndex][1];
            constValues.clear();
            constValues.add(value);
            values[constantIndex][2] = val;
            renderConstants();
            substituteValues();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        page = (ScrollView)findViewById(R.id.scroll_calculator);
        page.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return isScrollBlocked;
            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnChangeConstants = (Button) findViewById(R.id.button_change_constants);

        final SimpleCursorAdapter chaptersAdap = setSpinnerAdapter(
                this.getContentResolver().query(DataContract.ChapterEntry.CONTENT_URI,
                        null, null, null, "HasCalc"),
                new String[]{DataContract.ChapterEntry.COLUMN_NAME}
        );

        inputFields = new ArrayList<>();
        inputSpinners = new ArrayList<>();
        conversionHelpers = new ArrayList<>();
        inputConversion = new ArrayList<>();

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



                                                inputFields.clear();
                                                inputSpinners.clear();
                                                conversionHelpers.clear();
                                                inputConversion.clear();

                                                constValues = new ArrayList<>();

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

                                                            //
//                                                            txtInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

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
                                                            constantId = const_id;
                                                            constantIndex = varCtr;
                                                            Cursor a = CalculatorActivity.this.getContentResolver().query(
                                                                    DataContract.ConstantEntry.buildConstantUri(constantId),
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
                                                renderConstants();

                                                substituteValues();
                                                expression = expressionBuilder.build();
                                                fromConstants = false;
                                                fromLesson = false;

                                                TextView conversionHeader = (TextView)findViewById(R.id.text_conversion_divider);
                                                conversionHeader.setVisibility(View.GONE);

                                                LinearLayout linearLayoutCon = (LinearLayout)findViewById(R.id.steps_container_conversion);
                                                linearLayoutCon.removeAllViews();

                                                if(isFirstTime()){
                                                    startInstructions();
                                                }

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
                startActivityForResult(intent, CONSTANT);
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

    public void renderConstants(){
        TextView txt = (TextView) findViewById(R.id.text_constants_label);
        LinearLayout constContainer = (LinearLayout) findViewById(R.id.const_container);
        constContainer.removeAllViews();

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
        String finalAnswer = "";
        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.steps_container);
        final LinearLayout linearLayoutCon = (LinearLayout) findViewById(R.id.steps_container_conversion);
        linearLayoutCon.setVisibility(View.GONE);

        TextView conversionHeader = (TextView)findViewById(R.id.text_conversion_divider);
        conversionHeader.setVisibility(View.GONE);

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
                    if(i == steps.length-1) {
                        finalAnswer = result[0] + finalUnit;
                        formulaDisplay = formulaDisplay.replace(strForm, result[0] + finalUnit);
                    } else
                        formulaDisplay = formulaDisplay.replace(strForm, result[0] + result[1]);
                    txtStep.setText(formulaDisplay);
                    linearLayout.addView(txtStep);

                    if(i == steps.length-1 && !finalType.equals("None") && (finalUnitIndex != finalConversionHelper.defaultUnit)){ //Display Conversion Steps
                        String formula = "{{c} \\cdot {{t} \\over {f}}}";

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

                        linearLayoutCon.setVisibility(View.VISIBLE);
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

                                    finalAnswer = resultCon[0] + resultCon[1].replace("µ", "\\mu ").replace(NonSI
                                                    .DEGREE_ANGLE
                                                    .toString(),
                                            "^{{\\circ}}");

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
        TextView txtFinal = (TextView)findViewById(R.id.text_final_label);
        txtFinal.setVisibility(View.VISIBLE);

        MathView txtAnswer = (MathView)findViewById(R.id.text_final_answer);
        txtAnswer.setText("$$" + finalAnswer + "$$");

        page.post(new Runnable() {
            @Override
            public void run() {
                final ProgressDialog dialog = new ProgressDialog(CalculatorActivity.this);
                dialog.setCancelable(false);
                dialog.setMessage("Calculating...");
                dialog.setIndeterminate(true);
                dialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (Exception e) {

                        }
                        dialog.dismiss();
                        page.smoothScrollTo(0, linearLayoutCon.getBottom());
                    }
                }).start();
            }
        });
    }

    private void removeEditTextFocus() {
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.input_container);
        try {
            ((InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
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
        } else if (id == R.id.action_help){
            startInstructions();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_calculator, menu);
        return true;
    }


    public void resetSteps() {
        LinearLayout stepsContainer = (LinearLayout) findViewById(R.id.steps_container);
        stepsContainer.removeAllViews();
    }

    private void startInstructions(){
        //initialize instructions
        views = new ArrayList<>();
        titles = new ArrayList<>();
        instructions = new ArrayList<>();

        views.add(findViewById(R.id.spinner_chapters));
        views.add(findViewById(R.id.spinner_lessons));
        views.add(findViewById(R.id.spinner_formula));
        views.add(findViewById(R.id.spinner_variable));
        views.add(inputFields.get(0));
        views.add(inputSpinners.get(0));
        views.add(inputConversion.get(0));
        views.add(toolbar.findViewById(R.id.action_converter));
        views.add(btnCalc);
        views.add(findViewById(R.id.steps_container));
        views.add(toolbar.findViewById(R.id.action_help));

        titles.add("Chapters");
        titles.add("Lessons");
        titles.add("Formulas");
        titles.add("Variables");
        titles.add("Input");
        titles.add("Units");
        titles.add("Converted Values");
        titles.add("Unit Conversions");
        titles.add("Calculate");
        titles.add("Results");
        titles.add("Done!");

        instructions.add("Select the chapter for the formula here.");
        instructions.add("You can also change the lessons.");
        instructions.add("Next, select the formula you want to solve.");
        instructions.add("If you want to solve for a different variable, you can change it here,");
        instructions.add("Enter the values of the variables here.");
        instructions.add("You can change the unit of the value you entered.");
        instructions.add("All values would be converted to their base unit (e.g. km -> m, minutes -> seconds, etc.)");
        instructions.add("If you want to see how the values were converted, go to the Unit Converter Calculator.");
        instructions.add("If everything is done, just tap on the calculate button to show the results.");
        instructions.add("The results would be shown here at the bottom part of the page.");
        instructions.add("If you want to repeat this tutorial, tap on the help button on the toolbar.");

        skip = false;
        isScrollBlocked = true;

        final ShowcaseView sv = new ShowcaseView.Builder(CalculatorActivity.this)
                .setContentTitle("Calculator")
                .setContentText("Hello! This is the calculator where you can solve different formulas!")
                .setStyle(R.style.CustomShowcaseTheme)
                .hideOnTouchOutside()
                .withNewStyleShowcase()
                .setShowcaseEventListener(new OnShowcaseEventListener() {
                    @Override
                    public void onShowcaseViewHide(ShowcaseView showcaseView) {
                        if(!skip)
                            showInstruction(0);
                        else
                            isScrollBlocked = false;
                    }

                    @Override
                    public void onShowcaseViewDidHide(ShowcaseView showcaseView) {

                    }

                    @Override
                    public void onShowcaseViewShow(ShowcaseView showcaseView) {

                    }

                    @Override
                    public void onShowcaseViewTouchBlocked(MotionEvent motionEvent) {

                    }
                })
                .build();

        Button btnSkip = (Button) LayoutInflater.from(CalculatorActivity.this).inflate(com.github.amlcurran.showcaseview.R.layout.showcase_button, null);
        RelativeLayout.LayoutParams  params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        params.setMargins(16,16,16,16);
        btnSkip.setLayoutParams(params);
        btnSkip.setText("Skip");
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                skip = true;
                sv.hide();
                isScrollBlocked = false;
            }
        });
        btnSkip.getBackground().setColorFilter(Color.parseColor("#33B5E5"), PorterDuff.Mode.MULTIPLY);

        sv.addView(btnSkip, sv.getChildCount()-1);
        sv.show();
    }

    private void showInstruction(final int idTarget){
        final int nextTarget = idTarget + 1;
        page.post(new Runnable() {
            @Override
            public void run() {
                page.smoothScrollTo(0, views.get(idTarget).getTop());
            }
        });
        final ShowcaseView sv = new ShowcaseView.Builder(CalculatorActivity.this)
                .setTarget(new ViewTarget(views.get(idTarget)))
                .setContentTitle(titles.get(idTarget))
                .setContentText(instructions.get(idTarget))
                .setStyle(R.style.CustomShowcaseTheme)
                .withNewStyleShowcase()
                .hideOnTouchOutside()
                .setShowcaseEventListener(new OnShowcaseEventListener() {
                    @Override
                    public void onShowcaseViewHide(ShowcaseView showcaseView) {
                        if(!skip) {
                            if (nextTarget != views.size()) {
                                showInstruction(nextTarget);
                            } else {
                                isScrollBlocked = false;
                            }
                        } else {
                            isScrollBlocked = false;
                        }

                    }

                    @Override
                    public void onShowcaseViewDidHide(ShowcaseView showcaseView) {

                    }

                    @Override
                    public void onShowcaseViewShow(ShowcaseView showcaseView) {

                    }

                    @Override
                    public void onShowcaseViewTouchBlocked(MotionEvent motionEvent) {

                    }
                })
                .build();
        if(idTarget != views.size()-1) {

            Button btnSkip = (Button) LayoutInflater.from(CalculatorActivity.this).inflate(com.github.amlcurran.showcaseview.R.layout.showcase_button, null);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params.setMargins(16, 16, 16, 16);
            btnSkip.setLayoutParams(params);
            btnSkip.setText("Skip");
            btnSkip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    skip = true;
                    sv.hide();
                    isScrollBlocked = false;
                }
            });
            btnSkip.getBackground().setColorFilter(Color.parseColor("#33B5E5"), PorterDuff.Mode.MULTIPLY);

            sv.addView(btnSkip);
        }
        sv.show();
    }

    private boolean isFirstTime()
    {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        boolean ranBefore = preferences.getBoolean("CalculatorRanBefore", false);
        if (!ranBefore) {
            // first time
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("CalculatorRanBefore", true);
            editor.commit();
        }
        return !ranBefore;
    }

}
