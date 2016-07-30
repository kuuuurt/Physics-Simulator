package com.ps.physicssimulator.lessons;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ps.physicssimulator.R;
import com.ps.physicssimulator.data.DataContract;

import io.github.kexanie.library.MathView;

/**
 * A placeholder fragment containing a simple view.
 */
public class ValuesFragment extends Fragment {

    public ValuesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_values_lesson, container, false);


        Cursor lesson = getActivity().getContentResolver().query(
                DataContract.LessonEntry.buildLessonTitle(getArguments().getString("lesson")),
                null,null,null,null
        );

        lesson.moveToFirst();


        ImageSpan is = new ImageSpan(getActivity(), R.drawable.ic_chapter_one_dimensional_motion);
        SpannableString text = new SpannableString(Html.fromHtml(lesson.getString(lesson.getColumnIndex(DataContract.LessonEntry.COLUMN_CONTENT))));
        text.setSpan(is, 5, 6, 0);

        MathView txtContent = (MathView)rootView.findViewById(R.id.text_values_content_1);
        txtContent.setText(Html.toHtml(text) + "$$ a = {{5 + 6} \\over 2} $$");

        return rootView;
    }
}
