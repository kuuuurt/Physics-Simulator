package com.ps.physicssimulator.calculator;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ps.physicssimulator.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class VelocityFragment extends Fragment {


    public VelocityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_velocity_calculator, container, false);
    }

}
