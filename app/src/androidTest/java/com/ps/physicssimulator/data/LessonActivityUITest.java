package com.ps.physicssimulator.data;

import android.content.Intent;
import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.ps.physicssimulator.LessonActivity;
import com.ps.physicssimulator.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertTrue;
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
    public void testListViewContents(){


        Cursor chapters = InstrumentationRegistry.getTargetContext().getContentResolver()
                .query(DataContract.ChapterEntry.CONTENT_URI, null, null, null, null);

        assertTrue("Chapter Cursor not loaded", chapters.moveToFirst());

        for (int i = 0; i < chapters.getCount(); i++) {
            String chapterName = chapters.getString(chapters.getColumnIndex(DataContract.
                    ChapterEntry.COLUMN_NAME));

            Intent intent = new Intent();
            intent.putExtra(Intent.EXTRA_TEXT, chapterName);
            mActivityRule.launchActivity(intent);

            Cursor lessons = InstrumentationRegistry.getTargetContext().getContentResolver()
                    .query(DataContract.LessonEntry.buildLessonChapter(chapterName), null,
                            null, null, null);

            assertTrue("Lesson Cursor not loaded", lessons.moveToFirst());

            for (int k = 0; k < lessons.getCount(); k++) {
                String lessonTitle = lessons.getString(lessons.getColumnIndex(DataContract.
                        LessonEntry.COLUMN_TITLE));
                onData(anything()).inAdapterView(withId(R.id.list_view_lesson)).atPosition(k)
                        .onChildView(withId(R.id.text_title))
                        .check(matches(withText(lessonTitle)));
                lessons.moveToNext();
            }

            chapters.moveToNext();

            mActivityRule.getActivity().finish();
        }
    }

}
