package com.ps.physicssimulator.data.data;

import android.net.Uri;
import android.support.test.runner.AndroidJUnit4;

import com.ps.physicssimulator.data.DataContract;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by qwerasdf on 7/21/16.
 */
@RunWith(AndroidJUnit4.class)
public class DataContractTest {

    private static final long TEST_RECORD_ID = 1234;
    private static final String TEST_LESSON_TITLE = "/Title";
    private static final String TEST_CHAPTER_NAME = "/Chapter";
    private static final String TEST_FORMULA_NAME = "/Formula";
    private static final String TEST_SECTION_NAME = "/Section";

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
        assertEquals("Name not properly appended to Uri!", String.valueOf(TEST_RECORD_ID),
                DataContract.ConstantEntry.getIDFromUri(constantUri));
        assertEquals("Uri does not match expected result", constantUri.toString(),
                DataContract.ConstantEntry.CONTENT_URI + "/"  + TEST_RECORD_ID);
    }

    @Test
    public void testBuildFormula(){
        Uri formulaUri = DataContract.FormulaEntry.buildFormulaUri(TEST_RECORD_ID);
        assertNotNull("Uri not created!", formulaUri);
        assertEquals("Uri does not match expected result", formulaUri.toString(),
                DataContract.FormulaEntry.CONTENT_URI + "/"  + TEST_RECORD_ID);
    }

    @Test
    public void testBuildFormulaLesson(){
        Uri lessonUri = DataContract.FormulaEntry.buildFormulaLesson(TEST_LESSON_TITLE);
        assertNotNull("Uri not created!", lessonUri);
        assertEquals("Title not properly appended to Uri!", TEST_LESSON_TITLE,
                lessonUri.getLastPathSegment());
        assertEquals("Uri does not match expected result", lessonUri.toString(),
                DataContract.FormulaEntry.CONTENT_URI + "/%2FTitle");
    }

    @Test
    public void testBuildVariable(){
        Uri variableUri = DataContract.VariableEntry.buildVariableUri(TEST_RECORD_ID);
        assertNotNull("Uri not created!", variableUri);
        assertEquals("Uri does not match expected result", variableUri.toString(),
                DataContract.VariableEntry.CONTENT_URI + "/"  + TEST_RECORD_ID);
    }

    @Test
    public void testBuildVariableName(){
        Uri nameUri = DataContract.VariableEntry.buildVariableFormula(TEST_FORMULA_NAME);
        assertNotNull("Uri not created!", nameUri);
        assertEquals("Formula not properly appended to Uri!", TEST_FORMULA_NAME,
                nameUri.getLastPathSegment());
        assertEquals("Uri does not match expected result", nameUri.toString(),
                DataContract.VariableEntry.CONTENT_URI + "/%2FFormula");
    }

    @Test
    public void testBuildFormulaConstant(){
        Uri formulaUri = DataContract.FormulaConstantEntry.buildFormulaConstantUri(TEST_RECORD_ID);
        assertNotNull("Uri not created!", formulaUri);
        assertEquals("Uri does not match expected result", formulaUri.toString(),
                DataContract.FormulaConstantEntry.CONTENT_URI + "/"  + TEST_RECORD_ID);
    }

    @Test
    public void testBuildFormulaConstantFormula(){
        Uri formulaUri = DataContract.FormulaConstantEntry.buildFormulaConstant(TEST_FORMULA_NAME);
        assertNotNull("Uri not created!", formulaUri);
        assertEquals("Formula not properly appended to Uri!", TEST_FORMULA_NAME,
                formulaUri.getLastPathSegment());
        assertEquals("Uri does not match expected result", formulaUri.toString(),
                DataContract.FormulaConstantEntry.CONTENT_URI + "/%2FFormula");
    }

    @Test
    public void testBuildSection(){
        Uri sectionUri = DataContract.SectionEntry.buildSectionUri(TEST_RECORD_ID);
        assertNotNull("Uri not created!", sectionUri);
        assertEquals("Uri does not match expected result", sectionUri.toString(),
                DataContract.SectionEntry.CONTENT_URI + "/"  + TEST_RECORD_ID);
    }

    @Test
    public void testBuildSectionLesson(){
        Uri lessonUri = DataContract.SectionEntry.buildSectionLesson(TEST_LESSON_TITLE);
        assertNotNull("Uri not created!", lessonUri);
        assertEquals("Lesson not properly appended to Uri!", TEST_LESSON_TITLE,
                lessonUri.getLastPathSegment());
        assertEquals("Uri does not match expected result", lessonUri.toString(),
                DataContract.SectionEntry.CONTENT_URI + "/%2FTitle");
    }

    @Test
    public void testBuildImage(){
        Uri imageUri = DataContract.ImageEntry.buildImageUri(TEST_RECORD_ID);
        assertNotNull("Uri not created!", imageUri);
        assertEquals("Uri does not match expected result", imageUri.toString(),
                DataContract.ImageEntry.CONTENT_URI + "/"  + TEST_RECORD_ID);
    }

    @Test
    public void testBuildImageSection(){
        Uri sectionUri = DataContract.ImageEntry.buildImageSectionUri(TEST_SECTION_NAME);
        assertNotNull("Uri not created!", sectionUri);
        assertEquals("Section not properly appended to Uri!", TEST_SECTION_NAME,
                sectionUri.getLastPathSegment());
        assertEquals("Uri does not match expected result", sectionUri.toString(),
                DataContract.ImageEntry.CONTENT_URI + "/%2FSection");
    }
}
