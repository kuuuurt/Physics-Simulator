package com.ps.physicssimulator;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.ps.physicssimulator.utils.ExpressionBuilderModified;
import com.ps.physicssimulator.utils.ExpressionModified;

import org.jscience.physics.amount.Amount;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.measure.quantity.Length;
import javax.measure.quantity.Mass;
import javax.measure.unit.Unit;

import io.github.kexanie.library.MathView;

import static javax.measure.unit.SI.ATTO;
import static javax.measure.unit.SI.CENTI;
import static javax.measure.unit.SI.DECI;
import static javax.measure.unit.SI.DEKA;
import static javax.measure.unit.SI.EXA;
import static javax.measure.unit.SI.FEMTO;
import static javax.measure.unit.SI.GIGA;
import static javax.measure.unit.SI.GRAM;
import static javax.measure.unit.SI.HECTO;
import static javax.measure.unit.SI.JOULE;
import static javax.measure.unit.SI.KILO;
import static javax.measure.unit.SI.MEGA;
import static javax.measure.unit.SI.METERS_PER_SECOND;
import static javax.measure.unit.SI.METERS_PER_SQUARE_SECOND;
import static javax.measure.unit.SI.METRE;
import static javax.measure.unit.SI.MICRO;
import static javax.measure.unit.SI.MILLI;
import static javax.measure.unit.SI.NANO;
import static javax.measure.unit.SI.NEWTON;
import static javax.measure.unit.SI.PETA;
import static javax.measure.unit.SI.PICO;
import static javax.measure.unit.SI.SECOND;
import static javax.measure.unit.SI.TERA;
import static javax.measure.unit.SI.YOCTO;
import static javax.measure.unit.SI.YOTTA;
import static javax.measure.unit.SI.ZEPTO;
import static javax.measure.unit.SI.ZETTA;

//import javax.measure.Unit;

public class ConverterActivity extends AppCompatActivity {

    public static String types[] = {"Mass", "Length", "Time", "Speed", "Acceleration", "Force", "Energy", "Power"};
    public static Unit<?> baseUnits[] = {GRAM, METRE, SECOND, METERS_PER_SECOND, METERS_PER_SQUARE_SECOND, NEWTON, JOULE, JOULE};
    public static String keywords[] = {"grams", "meters", "seconds", "meters per seconds", "meters per seconds squared", "newtons", "joules", "joules"};
    public static int defaultUnit;
    public static List<Integer> unitPow = new ArrayList<>();
    public static List<String> units = new ArrayList<>();
    public static List<Unit<?>> unitsCompute = new ArrayList<>();

    String value;
    String type;
    int startUnit;
    int finalUnit;

    double num = 0,
            den = 0;
    Button btnConvert;

    String formula;
    String formulaSubbed;
    ExpressionBuilderModified expressionBuilder;
    ExpressionModified expression;
    TextInputEditText txtInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_converter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        expressionBuilder = new ExpressionBuilderModified("x * (y / z)");
        expressionBuilder.variable("x");
        expressionBuilder.variable("y");
        expressionBuilder.variable("z");
        expression = expressionBuilder.build();
        value = "";
        btnConvert = (Button)findViewById(R.id.button_convert);
        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                convert();
            }
        });
        formula = "$${c} = {{c} \\cdot {{t} \\over {f}}}$$";
        formulaSubbed = "";



        Spinner spnType = (Spinner)findViewById(R.id.spinner_unit_type);
        spnType.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, types));
        spnType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                type = adapterView.getItemAtPosition(i).toString();

                Spinner spnUnit = (Spinner)findViewById(R.id.spinner_unit_origin);
                //spnUnit.setAdapter(new ArrayAdapter<String>(ConverterActivity.this, android.R.layout.simple_spinner_dropdown_item, units));
                populateListsSI(ConverterActivity.this, baseUnits[i], keywords[i],spnUnit);
                spnUnit.setSelection(defaultUnit);
                spnUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        startUnit = i;
                        substituteValues();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                Spinner spnFinal = (Spinner)findViewById(R.id.spinner_unit_final);
                //spnFinal.setAdapter(new ArrayAdapter<String>(ConverterActivity.this, android.R.layout.simple_spinner_dropdown_item, units));
                populateListsSI(ConverterActivity.this, baseUnits[i], keywords[i],spnFinal);
                spnFinal.setSelection(defaultUnit);
                spnFinal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        finalUnit = i;
                        substituteValues();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                substituteValues();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        txtInput = (TextInputEditText)findViewById(R.id.text_unit_source);
        txtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    String text = charSequence.toString();
//                    if (text.charAt(0) == '.')
//                        if (text.length() != 1) {
//                            text = "0" + text;
//                        } else {
//                            btnConvert.setEnabled(false);
//                            return;
//                        }

//                    if(text.contains(".")){
//                        int idx = text.indexOf(".");
//                        txtInput.setFilters(new InputFilter[] {new InputFilter.LengthFilter(idx+5)});
//                    } else {
//                        txtInput.setFilters(new InputFilter[] {new InputFilter.LengthFilter(15)});
//                    }
                    if(!text.contains(".")){
                        for(int x = 0; x < text.length(); x++){
                            if(text.charAt(x) != '0'){
                                text = text.substring(x);
                                break;
                            }
                            if(x == text.length()-1){
                                text = "0";
                            }
                        }
                    } else {
                        for(int x = 0; x < text.indexOf("."); x++){
                            if(text.charAt(x) != '0'){
                                text = text.substring(x);
                                break;
                            }
                            if(x == text.indexOf(".")-1){
                                text = "0" + text.substring(text.indexOf("."));
                            }
                        }
                    }
                    if(text.charAt(0) == '-')
                        if(text.length() == 1)
                            text = "";

                    if(text.charAt(text.length()-1) == '.')
                        text = text + "0";

                    int idx = text.indexOf(".");
                    String zeroTemp = "";
                    if(idx != -1 && idx < text.length()) {
                        for (int z = idx; z < text.length() - 1; z++)
                            zeroTemp += 0;
                        if(text.substring(idx+1).equals(zeroTemp)) {
                            text = text.substring(0, idx);
                            if(Double.parseDouble(text.substring(0, idx)) == 0)
                                text = "0";
                        }
                    }


                    Double textValue = Double.parseDouble(text);
                    if(textValue > Math.pow(10, 7) || ((textValue > 0) && (textValue < Math.pow(10, -3))))
                        text = "(" + new DecimalFormat("#.####E0").format(textValue) + ")";
                    value = text;
                    btnConvert.setEnabled(true);
                    substituteValues();
                } catch (Exception ex){
                    value = "";
                    btnConvert.setEnabled(false);
                    substituteValues();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void substituteValues() {
        formulaSubbed = formula;
        formulaSubbed = formulaSubbed.replace("{c}", value + "{{" + unitsCompute.get(startUnit).toString()+ "}}") ;
        num = 1;
        den = 1;
        if(finalUnit < startUnit) {
            if(finalUnit < 10 && startUnit > 10){
                num = Math.pow(10, unitPow.get(finalUnit) + unitPow.get(startUnit));
                String strNum = String.valueOf(num);
                if(num % 1 == 0)
                    strNum = strNum.replace(".0", "");
                formulaSubbed = formulaSubbed.replace("{t}", strNum + "{{" + unitsCompute.get(finalUnit).toString() + "}}");
            } else {
                if(finalUnit > 9)
                    num = Math.pow(10, unitPow.get(startUnit) - unitPow.get(finalUnit));
                else
                    num = Math.pow(10, unitPow.get(finalUnit) - unitPow.get(startUnit));
                String strNum = String.valueOf(num);
                if(num % 1 == 0)
                    strNum = strNum.replace(".0", "");
                formulaSubbed = formulaSubbed.replace("{t}", strNum + "{{" + unitsCompute.get(finalUnit).toString() + "}}");
            }
            formulaSubbed = formulaSubbed.replace("{f}", "1{{" + unitsCompute.get(startUnit).toString() + "}}");
        } else {
            if(finalUnit < 10 && startUnit > 10){
                den = Math.pow(10, unitPow.get(finalUnit) + unitPow.get(startUnit));
                String strDen = String.valueOf(den);
                if(den % 1 == 0)
                    strDen = strDen.replace(".0", "");
                formulaSubbed = formulaSubbed.replace("{f}", strDen + "{{" + unitsCompute.get(startUnit).toString() + "}}");
            } else {
                if(finalUnit < 11)
                    den = Math.pow(10, unitPow.get(startUnit) - unitPow.get(finalUnit));
                else
                    den = Math.pow(10, unitPow.get(finalUnit) - unitPow.get(startUnit));
                String strDen = String.valueOf(den);
                if(den % 1 == 0)
                    strDen = strDen.replace(".0", "");
                formulaSubbed = formulaSubbed.replace("{f}", strDen + "{{" + unitsCompute.get(startUnit).toString() + "}}");
            }
            formulaSubbed = formulaSubbed.replace("{t}", "1{{" + unitsCompute.get(finalUnit).toString() + "}}");
        }
        expression.setVariable("y", num, "{{" + unitsCompute.get(finalUnit).toString() + "}}");
        expression.setVariable("z", den, "{{" + unitsCompute.get(startUnit).toString() + "}}");
        formulaSubbed = formulaSubbed.replace("µ", "\\mu ");
        MathView txtSub = (MathView) findViewById(R.id.text_substitute);
        txtSub.setText(formulaSubbed);
    }

    private void convert() {
        if(type.equals("Mass")) {
            showSolutionSI();
            Amount<Mass> massValue = (Amount<Mass>) Amount.valueOf(Double.parseDouble(value), unitsCompute.get(startUnit));
            massValue = (Amount<Mass>) massValue.to(unitsCompute.get(finalUnit));
        } else if(type.equals("Length")) {
            showSolutionSI();
            Amount<Length> lengthValue = (Amount<Length>) Amount.valueOf(Double.parseDouble(value), unitsCompute.get(startUnit));
            lengthValue = (Amount<Length>) lengthValue.to(unitsCompute.get(finalUnit));
        }
    }

    private void showSolutionSI() {
        resetSteps();
        expression.setVariable("x", Double.parseDouble(value), "{{" + unitsCompute.get(startUnit).toString() + "}}");
        List<String> results = expression.evaluate();
        Object[] steps = results.toArray();
        MathView txtSub = (MathView) findViewById(R.id.text_substitute);
        String formulaDisplay = txtSub.getText();
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.steps_container);

        for (int i = 0; i < steps.length; i++) {
            MathView txtStep = new MathView(this, null);
            txtStep.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            txtStep.setEngine(MathView.Engine.KATEX);
            try {
                String result[] = (String[]) steps[i];
                if (Double.parseDouble(result[0].toString()) % 1 == 0) {
                    result[0] = result[0].replace(".0", "");
                }

                String strForm = result[2].toString();
                if (strForm.contains(" ^ ")) {
                    int idx = strForm.indexOf("^");
                    strForm = "(" + strForm.substring(0, idx - 1) + ")" + strForm.substring(idx - 1);
                }

                strForm = strForm.replace("µ", "\\mu ");

                if (formulaDisplay.contains(strForm)) {
                    if (formulaDisplay.contains("(" + strForm + ")")) {
                        formulaDisplay = formulaDisplay.replace("(" + strForm + ")", strForm);
                    }

                    if (formulaDisplay.contains("{" + strForm + "}")) {
                        formulaDisplay = formulaDisplay.replace("{" + strForm + "}", strForm);
                    }

                    formulaDisplay = formulaDisplay.replace(strForm, result[0] + result[1].replace("µ", "\\mu "));

                    txtStep.setText(formulaDisplay);
                    linearLayout.addView(txtStep);
                }
            } catch (Exception ex){
                txtStep.setText("$$Error! Division  by Zero!$$");
                linearLayout.addView(txtStep);
                break;
            }

        }
    }

    public void resetSteps() {
        LinearLayout stepsContainer = (LinearLayout) findViewById(R.id.steps_container);
        stepsContainer.removeAllViews();
    }

    public static void populateListsSI(Context context, Unit<?> unit, String stringUnit, Spinner spn){
        unitsCompute.clear();
        units.clear();
        unitsCompute.add(YOCTO(unit)); unitPow.add(24);
        unitsCompute.add(ZEPTO(unit)); unitPow.add(21);
        unitsCompute.add(ATTO(unit)); unitPow.add(18);
        unitsCompute.add(FEMTO(unit)); unitPow.add(15);
        unitsCompute.add(PICO(unit)); unitPow.add(12);
        unitsCompute.add(NANO(unit)); unitPow.add(9);
        unitsCompute.add(MICRO(unit)); unitPow.add(6);
        unitsCompute.add(MILLI(unit)); unitPow.add(3);
        unitsCompute.add(CENTI(unit)); unitPow.add(2);
        unitsCompute.add(DECI(unit)); unitPow.add(1);
        unitsCompute.add(unit); unitPow.add(0);
        unitsCompute.add(DEKA(unit)); unitPow.add(1);
        unitsCompute.add(HECTO(unit)); unitPow.add(2);
        unitsCompute.add(KILO(unit)); unitPow.add(3);
        unitsCompute.add(MEGA(unit)); unitPow.add(6);
        unitsCompute.add(GIGA(unit)); unitPow.add(9);
        unitsCompute.add(TERA(unit)); unitPow.add(-12);
        unitsCompute.add(PETA(unit)); unitPow.add(15);
        unitsCompute.add(EXA(unit)); unitPow.add(18);
        unitsCompute.add(ZETTA(unit)); unitPow.add(21);
        unitsCompute.add(YOTTA(unit)); unitPow.add(24);
        units.add("Yocto" + stringUnit + " (" + YOCTO(unit) + ")");
        units.add("Zepto" + stringUnit + " (" + ZEPTO(unit) + ")");
        units.add("Atto" + stringUnit + " (" + ATTO(unit) + ")");
        units.add("Femto" + stringUnit + " (" + FEMTO(unit) + ")");
        units.add("Pico" + stringUnit + " (" + PICO(unit) + ")");
        units.add("Nano" + stringUnit + " (" + NANO(unit) + ")");
        units.add("Micro" + stringUnit + " (" + MICRO(unit) + ")");
        units.add("Milli" + stringUnit + " (" + MILLI(unit) + ")");
        units.add("Centi" + stringUnit + " (" + CENTI(unit) + ")");
        units.add("Deci" + stringUnit + " (" + DECI(unit) + ")");
        units.add(stringUnit.substring(0, 1).toUpperCase() + stringUnit.substring(1) +  " (" + unit + ")");
        units.add("Deka" + stringUnit + " (" + DEKA(unit) + ")");
        units.add("Hecto" + stringUnit + " (" + HECTO(unit) + ")");
        units.add("Kilo" + stringUnit + " (" + KILO(unit) + ")");
        units.add("Giga" + stringUnit + " (" + GIGA(unit) + ")");
        units.add("Mega" + stringUnit + " (" + MEGA(unit) + ")");
        units.add("Tera" + stringUnit + " (" + TERA(unit) + ")");
        units.add("Peta" + stringUnit + " (" + PETA(unit) + ")");
        units.add("Exa" + stringUnit + " (" + EXA(unit) + ")");
        units.add("Zetta" + stringUnit + " (" + ZETTA(unit) + ")");
        units.add("Yotta" + stringUnit + " (" + YOTTA(unit) + ")");
        defaultUnit = 10;
        spn.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, units));
    }
}
