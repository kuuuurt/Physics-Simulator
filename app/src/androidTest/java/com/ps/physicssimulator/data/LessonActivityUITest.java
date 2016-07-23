package com.ps.physicssimulator.data;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.ps.physicssimulator.LessonActivity;
import com.ps.physicssimulator.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.anything;

/**
 * Created by qwerasdf on 7/23/16.
 */
@RunWith(AndroidJUnit4.class)
public class LessonActivityUITest {


    @Rule
    public ActivityTestRule<LessonActivity> mActivityRule = new ActivityTestRule<LessonActivity>(
        LessonActivity.class, true, false);


    @Test
    public void testContentNavigation(){
        Intent intent = new  Intent();
        //Intent intent = new Intent(InstrumentationRegistry.getContext(), LessonActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, "One-dimensional Motion");
        mActivityRule.launchActivity(intent);
        //Click on 1st Record (One-dimensional Motion)
        onData(anything()).inAdapterView(withId(R.id.list_view_lesson)).atPosition(0).perform(
                click());
        //Check if TextView is accessible
        onView(withId(R.id.txtValuesContent1)).check(matches(withId(R.id.txtValuesContent1)));

    }
}
