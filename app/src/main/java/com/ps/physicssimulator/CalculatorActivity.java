package com.ps.physicssimulator;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class CalculatorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        List list = new ArrayList<String>();
        list.add("Test");
        list.add("Test");


        ArrayAdapter adap = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, list);

        Spinner spnCalc = (Spinner) findViewById(R.id.spnCalc);
        spnCalc.setAdapter(adap);
    }

}
