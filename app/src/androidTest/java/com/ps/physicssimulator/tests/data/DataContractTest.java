package com.ps.physicssimulator.tests.data;

import android.net.Uri;
import android.support.test.runner.AndroidJUnit4;

import com.ps.physicssimulator.tests.DataContract;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by qwerasdf on 7/21/16.
 */
@RunWith(AndroidJUnit4.class)
public class DataContractTest {

    private static final long TEST_RECORD_ID = 1234;
    private static final String TEST_LESSON_TITLE = "/Title";
    private static final String TEST_CHAPTER_NAME = "/Chapter";
    private static final String TEST_CONSTANT_NAME = "/Name";

    @Test
    public void testBuildChapter(){
        Uri chapterUri = DataContract.ChapterEntry.buildChapterUri(TEST_RECORD_ID);
        assertNotNull("Uri not created!", chapterUri);
        assertEquals("Uri does not match expected result", chapterUri.toString(),
                DataContract.ChapterEntry.CONTENT_URI + "/"  + TEST_RECORD_ID);
    }

    @Test
    public void testBuildChapterName(){
        Uri nameUri = DataContract.ChapterEntry.buildChapterName(TEST_CHAPTER_NAME);
        assertNotNull("Uri not created!", nameUri);
        assertEquals("Title not properly appended to Uri!", TEST_CHAPTER_NAME,
                nameUri.getLastPathSegment());
        assertEquals("Uri does not match expected result", nameUri.toString(),
                DataContract.ChapterEntry.CONTENT_URI + "/%2FChapter");
    }

    @Test
    public void testGetNameFromUri(){
        Uri nameUri = DataContract.ChapterEntry.buildChapterName(TEST_CHAPTER_NAME);
        String title = DataContract.ChapterEntry.getNameFromUri(nameUri);
        assertNotNull("Uri not created!", nameUri);
        assertEquals("Title not extracted from Uri!", TEST_CHAPTER_NAME, title);
    }

    @Test
    public void testBuildLesson(){
        Uri lessonUri = DataContract.LessonEntry.buildLessonUri(TEST_RECORD_ID);
        assertNotNull("Uri not created!", lessonUri);
        assertEquals("Uri does not match expected result", lessonUri.toString(),
                DataContract.LessonEntry.CONTENT_URI + "/"  + TEST_RECORD_ID);
    }

    @Test
    public void testBuildLessonTitle(){
        Uri titleUri = DataContract.LessonEntry.buildLessonTitle(TEST_LESSON_TITLE);
        assertNotNull("Uri not created!", titleUri);
        assertEquals("Title not properly appended to Uri!", TEST_LESSON_TITLE,
                titleUri.getLastPathSegment());
        assertEquals("Uri does not match expected result", titleUri.toString(),
                DataContract.LessonEntry.CONTENT_URI + "/%2FTitle");
    }

    @Test
    public void testBuildLessonChapter(){
        Uri chapterUri = DataContract.LessonEntry.buildLessonChapter(TEST_CHAPTER_NAME);
        assertNotNull("Uri not created!", chapterUri);
        assertEquals("Chapter not properly appended to Uri!", TEST_CHAPTER_NAME,
                chapterUri.getLastPathSegment());
        assertEquals("Uri does not match expected result", chapterUri.toString(),
                DataContract.LessonEntry.CONTENT_URI + "/chapter/%2FChapter");
    }

    @Test
    public void testGetTitleFromUri(){
        Uri titleUri = DataContract.LessonEntry.buildLessonTitle(TEST_LESSON_TITLE);
        String title = DataContract.LessonEntry.getTitleFromUri(titleUri);
        assertNotNull("Uri not created!", titleUri);
        assertEquals("Title not extracted from Uri!", TEST_LESSON_TITLE, title);
    }

    @Test
    public void testGetChapterFromUri(){
        Uri chapterUri = DataContract.LessonEntry.buildLessonChapter(TEST_CHAPTER_NAME);
        String chapter = DataContract.LessonEntry.getChapterFromUri(chapterUri);
        assertNotNull("Uri not created!", chapterUri);
        assertEquals("Chapter not extracted from Uri!", TEST_CHAPTER_NAME, chapter);
    }


    @Test
    public void testBuildConstant(){
        Uri constantUri = DataContract.ConstantEntry.buildConstantUri(TEST_RECORD_ID);
        assertNotNull("Uri not created!", constantUri);
        assertEquals("Uri does not match expected result", constantUri.toString(),
                DataContract.ConstantEntry.CONTENT_URI + "/"  + TEST_RECORD_ID);
    }

    @Test
    public void testBuildConstantName(){
        Uri nameUri = DataContract.ConstantEntry.buildConstantName(TEST_CONSTANT_NAME);
        assertNotNull("Uri not created!", nameUri);
        assertEquals("Title not properly appended to Uri!", TEST_CONSTANT_NAME,
                nameUri.getLastPathSegment());
        assertEquals("Uri does not match expected result", nameUri.toString(),
                DataContract.ConstantEntry.CONTENT_URI + "/%2FName");
    }
}
