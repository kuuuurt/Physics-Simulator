package com.ps.physicssimulator.tests;

import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.ps.physicssimulator.ChapterActivity;
import com.ps.physicssimulator.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.Matchers.anything;


/**
 * Created by qwerasdf on 7/23/16.
 */
@RunWith(AndroidJUnit4.class)
public class ChapterActivityUITest {

    @Rule
    public ActivityTestRule<ChapterActivity> activityTestRule = new ActivityTestRule<>(
            ChapterActivity.class);

    @Test
    public void testListViewContents(){
        Cursor chapters = InstrumentationRegistry.getTargetContext().getContentResolver()
                .query(DataContract.ChapterEntry.CONTENT_URI, null, null, null, null);

        assertTrue("Cursor not loaded", chapters.moveToFirst());

        for (int i = 0; i < chapters.getCount(); i++) {
            String chapterName = chapters.getString(chapters.getColumnIndex(DataContract.
                    ChapterEntry.COLUMN_NAME));
            onData(anything()).inAdapterView(withId(R.id.list_view_chapter)).atPosition(i)
                    .onChildView(withId(R.id.text_title))
                    .check(matches(withText(chapterName)));
            chapters.moveToNext();
        }
    }

    @Test
    public void testLessonNavigation(){
        //Click on 1st Record (One-dimensional Motion)
        onData(anything()).inAdapterView(withId(R.id.list_view_chapter)).atPosition(0)
                .perform(click());
        //Check if Acceleration Lesson is accessible
        onData(anything()).inAdapterView(withId(R.id.list_view_lesson)).atPosition(2).onChildView(
            ViewMatchers.withText("Acceleration")).check(matches(withText("Acceleration")
        ));
    }

    @Test
    public void testCalcNavigation() {
        //Open Drawer
        onView(withId(R.id.drawer_layout)).perform(open());
        //Click Calculator Item
        onView(withText("Calculator")).perform(click());
        //Check if spinner is clickable
        onView(withId(R.id.spinner_chapters)).perform(click());
    }
}
