package com.ps.physicssimulator;

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

import com.ps.physicssimulator.utils.ConversionUtils;
import com.ps.physicssimulator.utils.ExpressionBuilderModified;
import com.ps.physicssimulator.utils.ExpressionModified;

import java.util.List;

import io.github.kexanie.library.MathView;

//import javax.measure.Unit;

public class ConverterActivity extends AppCompatActivity {

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

    ConversionUtils conversionHelper = new ConversionUtils();


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
        formula = "{{c} \\cdot {{t} \\over {f}}}";
        formulaSubbed = "";



        Spinner spnType = (Spinner)findViewById(R.id.spinner_unit_type);
        spnType.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, ConversionUtils.typesConverter));
        spnType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                type = adapterView.getItemAtPosition(i).toString();

                List<String> units = conversionHelper.populateLists(type, ConversionUtils.baseUnits[i], ConversionUtils.keywords[i]);
                startUnit = conversionHelper.defaultUnit;
                finalUnit = conversionHelper.defaultUnit;

                Spinner spnUnit = (Spinner)findViewById(R.id.spinner_unit_origin);
                spnUnit.setAdapter(new ArrayAdapter<String>(ConverterActivity.this, android.R.layout.simple_spinner_dropdown_item, units));
                spnUnit.setSelection(conversionHelper.defaultUnit);
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
                spnFinal.setAdapter(new ArrayAdapter<String>(ConverterActivity.this, android.R.layout.simple_spinner_dropdown_item, units));
                //conversionHelper.populateLists(ConverterActivity.this, type, ConversionUtils.baseUnits[i], ConversionUtils.keywords[i],spnFinal);
                spnFinal.setSelection(conversionHelper.defaultUnit);
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
                    text = ConversionUtils.convertExponentialNotation(textValue);
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
        formulaSubbed = formulaSubbed.replace("{c}", value + "{" + conversionHelper.unitSymbol.get(startUnit).toString()+ "}") ;
        num = 1;
        den = 1;
        if(finalUnit < startUnit) {
            if(finalUnit < conversionHelper.defaultUnit && startUnit > conversionHelper.defaultUnit){
                num = conversionHelper.unitFactor.get(finalUnit) * conversionHelper.unitFactor.get(startUnit);
                String strNum = ConversionUtils.convertExponentialNotation(num);
                formulaSubbed = formulaSubbed.replace("{t}", strNum + "{" + conversionHelper.unitSymbol.get(finalUnit).toString() + "}");
            } else {
                if(finalUnit > conversionHelper.defaultUnit-1)
                    num = conversionHelper.unitFactor.get(startUnit) / conversionHelper.unitFactor.get(finalUnit);
                else
                    num = conversionHelper.unitFactor.get(finalUnit) / conversionHelper.unitFactor.get(startUnit);
                String strNum = ConversionUtils.convertExponentialNotation(num);
                formulaSubbed = formulaSubbed.replace("{t}", strNum + "{" + conversionHelper.unitSymbol.get(finalUnit).toString() + "}");
            }
            formulaSubbed = formulaSubbed.replace("{f}", "1{" + conversionHelper.unitSymbol.get(startUnit).toString() + "}");
        } else {
            if(finalUnit > conversionHelper.defaultUnit && startUnit < conversionHelper.defaultUnit){
                den = conversionHelper.unitFactor.get(finalUnit) * conversionHelper.unitFactor.get(startUnit);
                String strDen = ConversionUtils.convertExponentialNotation(den);
                formulaSubbed = formulaSubbed.replace("{f}", strDen + "{" + conversionHelper.unitSymbol.get(startUnit).toString() + "}");
            } else {
                if(finalUnit < conversionHelper.defaultUnit+1)
                    den = conversionHelper.unitFactor.get(startUnit) / conversionHelper.unitFactor.get(finalUnit);
                else
                    den = conversionHelper.unitFactor.get(finalUnit) / conversionHelper.unitFactor.get(startUnit);
                String strDen = ConversionUtils.convertExponentialNotation(den);
                formulaSubbed = formulaSubbed.replace("{f}", strDen + "{" + conversionHelper.unitSymbol.get(startUnit).toString() + "}");
            }
            formulaSubbed = formulaSubbed.replace("{t}", "1{" + conversionHelper.unitSymbol.get(finalUnit).toString() + "}");
        }
        expression.setVariable("y", num, "{" + conversionHelper.unitSymbol.get(finalUnit).toString() + "}");
        expression.setVariable("z", den, "{" + conversionHelper.unitSymbol.get(startUnit).toString() + "}");
        formulaSubbed = formulaSubbed.replace("µ", "\\mu ");
        if(!value.equals(""))
            formulaSubbed = formulaSubbed.replace("µ", "\\mu ").replace("{{\\circ}}","^{{\\circ}}");
        MathView txtSub = (MathView) findViewById(R.id.text_substitute);
        txtSub.setText("$$" + value + "{" + conversionHelper.unitSymbol.get(startUnit).toString()+ "} = " + formulaSubbed + "$$");
    }

    private void convert() {
        showSolution();
    }

    private void showSolution() {
        resetSteps();
        expression.setVariable("x", Double.parseDouble(value.replace("(", "").replace(")","")), "{" + conversionHelper.unitSymbol.get(startUnit).toString() + "}");
        List<String> results = expression.evaluate();
        Object[] steps = results.toArray();

        String formulaDisplay = formulaSubbed;
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
                if (Double.parseDouble(result[0].toString().replace("(", "").replace(")","")) % 1 == 0) {
                    result[0] = result[0].replace(".0", "");
                }

                String strForm = result[2].toString();
                if (strForm.contains(" ^ ")) {
                    int idx = strForm.indexOf("^");
                    strForm = "(" + strForm.substring(0, idx - 1) + ")" + strForm.substring(idx - 1);
                }

                strForm = strForm.replace("µ", "\\mu ").replace("{{\\circ}}","^{{\\circ}}");

                if (formulaDisplay.contains(strForm)) {
                    if (formulaDisplay.contains("(" + strForm + ")")) {
                        formulaDisplay = formulaDisplay.replace("(" + strForm + ")", strForm);
                    }

                    if (formulaDisplay.contains("{" + strForm + "}")) {
                        formulaDisplay = formulaDisplay.replace("{" + strForm + "}", strForm);
                    }

                    formulaDisplay = formulaDisplay.replace(strForm, result[0] + result[1].replace("µ", "\\mu "));

                    txtStep.setText("$$" + value + "{" + conversionHelper.unitSymbol.get(startUnit).toString()+ "} = " + formulaDisplay + "$$");
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


}
