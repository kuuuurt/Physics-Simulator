package com.ps.physicssimulator.lessons;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ps.physicssimulator.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class VelocityFragment extends Fragment {

    public VelocityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_velocity, container, false);
    }
}
