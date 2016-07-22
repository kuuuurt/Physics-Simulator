package com.ps.physicssimulator.lessons;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ps.physicssimulator.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccelerationFragment extends Fragment {


    public AccelerationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_acceleration, container, false);
    }

}
