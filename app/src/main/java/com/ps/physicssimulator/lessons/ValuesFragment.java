package com.ps.physicssimulator.lessons;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ps.physicssimulator.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class ValuesFragment extends Fragment {

    public ValuesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_values_lesson, container, false);
    }
}
