package com.ps.physicssimulator.calculator;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import com.ps.physicssimulator.R;
import com.ps.physicssimulator.data.DataContract;

/**
 * A simple {@link Fragment} subclass.
 */
public class ValuesFragment extends Fragment {


    public ValuesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_values_calculator, container, false);

        Bundle args = this.getArguments();

        final SimpleCursorAdapter formulaAdap = setSpinnerAdapter(
                getActivity().getContentResolver().query(
                        DataContract.FormulaEntry.buildFormulaLesson(args.getString("Lesson")),
                        null, null, null, null),
                new String[]{DataContract.FormulaEntry.COLUMN_NAME}
        );

        Spinner spnFormula = (Spinner) rootView.findViewById(R.id.spinner_formula);
        spnFormula.setAdapter(formulaAdap);
        spnFormula.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Cursor c = (Cursor)formulaAdap.getItem(i);
//                String formulaName = c.getString(c.getColumnIndex(DataContract.FormulaEntry
//                        .COLUMN_NAME));
//
//                final SimpleCursorAdapter varAdap = setSpinnerAdapter(
//                        getActivity().getContentResolver().query(
//                                DataContract.FormulaEntry.buildFormulaName(formulaName),
//                                null, null, null, null),
//                        new String[]{DataContract.FormulaEntry.COLUMN_VAR}
//                );
//
//                Spinner spnVar = (Spinner) rootView.findViewById(R.id.spinner_variable);
//                spnVar.setAdapter(varAdap);
//                spnVar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                        Cursor c = (Cursor)varAdap.getItem(i);
//                        String var = c.getString(c.getColumnIndex(DataContract.FormulaEntry
//                                .COLUMN_VAR));
//
//                        Log.d("asdf", var);
//
//                        //Load or generate Fields
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> adapterView) {
//
//                    }
//                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        return rootView;
    }

    public SimpleCursorAdapter setSpinnerAdapter(Cursor c, String[] projection){
        int[] views = new int[]{android.R.id.text1};
        return new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, c, projection, views, 1);
    }

}
