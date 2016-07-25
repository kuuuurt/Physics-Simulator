package com.ps.physicssimulator.data.data;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.ps.physicssimulator.data.DBHelper;
import com.ps.physicssimulator.data.DataContract;
import com.ps.physicssimulator.data.DataContract.ChapterEntry;
import com.ps.physicssimulator.data.DataContract.ConstantEntry;
import com.ps.physicssimulator.data.DataContract.FormulaEntry;
import com.ps.physicssimulator.data.DataContract.LessonEntry;
import com.ps.physicssimulator.data.DataProvider;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class DataProviderTest {
    private Context mContext;
    private DBHelper dbHelper;
    private SQLiteDatabase db;


    @Before
    public void setup(){
        mContext = InstrumentationRegistry.getTargetContext();
        dbHelper = new DBHelper(mContext);
        db = dbHelper.getWritableDatabase();
    }

    @Test
    public void testProviderRegistry() {
        PackageManager pm = mContext.getPackageManager();
        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                DataProvider.class.getName());
        try {
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);
            Assert.assertEquals("DataProvider registered with authority: " + providerInfo.authority +
                    " instead of authority: " + DataContract.CONTENT_AUTHORITY,
                    providerInfo.authority, DataContract.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            assertTrue("DataProvider not registered at " + mContext.getPackageName(), false);
        }
    }

    @Test
    public void testGetType(){
        String type = mContext.getContentResolver().getType(LessonEntry.CONTENT_URI);
        assertEquals("Uri should return CONTENT_TYPE",
                LessonEntry.CONTENT_TYPE, type);

        String testVar = "Name";
        type = mContext.getContentResolver().getType(LessonEntry.buildLessonTitle(testVar));
        assertEquals("Uri should return CONTENT_ITEM_TYPE",
                LessonEntry.CONTENT_ITEM_TYPE, type);

        type = mContext.getContentResolver().getType(LessonEntry.buildLessonChapter(testVar));
        assertEquals("Uri should return CONTENT_TYPE",
                LessonEntry.CONTENT_TYPE, type);

        type = mContext.getContentResolver().getType(ChapterEntry.CONTENT_URI);
        assertEquals("Uri should return CONTENT_TYPE",
                ChapterEntry.CONTENT_TYPE, type);

        type = mContext.getContentResolver().getType(ChapterEntry.buildChapterName(testVar));
        assertEquals("Uri should return CONTENT_ITEM_TYPE",
                ChapterEntry.CONTENT_ITEM_TYPE, type);

        type = mContext.getContentResolver().getType(ConstantEntry.CONTENT_URI);
        assertEquals("Uri should return CONTENT_TYPE",
                ConstantEntry.CONTENT_TYPE, type);

        type = mContext.getContentResolver().getType(ConstantEntry.buildConstantName(testVar));
        assertEquals("Uri should return CONTENT_ITEM_TYPE",
                ConstantEntry.CONTENT_ITEM_TYPE, type);

        type = mContext.getContentResolver().getType(FormulaEntry.CONTENT_URI);
        assertEquals("Uri should return CONTENT_TYPE",
                FormulaEntry.CONTENT_TYPE, type);

        type = mContext.getContentResolver().getType(FormulaEntry.buildFormulaLesson(testVar));
        assertEquals("Uri should return CONTENT_TYPE",
                FormulaEntry.CONTENT_TYPE, type);
    }

    @Test
    public void testBasicChapterQuery(){

        ContentValues testValues = new ContentValues();
        testValues.put(ChapterEntry.COLUMN_NAME, "One-dimensional Motion");
        testValues.put(ChapterEntry.COLUMN_DESCRIPTION, "");

        Cursor chapterCursor = mContext.getContentResolver().query(
                ChapterEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        validateCursor("testBasicChapterQuery", chapterCursor, testValues);
    }

    @Test
    public void testBasicConstantQuery(){
        ContentValues testValues = new ContentValues();
        testValues.put(ChapterEntry.COLUMN_NAME, "One-dimensional Motion");
        testValues.put(ChapterEntry.COLUMN_DESCRIPTION, "");

        Cursor chapterCursor = mContext.getContentResolver().query(
                ChapterEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        validateCursor("testBasicConstantQuery", chapterCursor, testValues);
    }

    @Test
    public void testBasicLessonQuery(){
        ContentValues testValues = new ContentValues();
        testValues.put(LessonEntry.COLUMN_TITLE, "Scalar and Vector Values");
        testValues.put(LessonEntry.COLUMN_CHAPTER_KEY, "1");
        testValues.put(LessonEntry.COLUMN_DESCRIPTION, "");
        testValues.put(LessonEntry.COLUMN_CONTENT, "");

        Cursor lessonCursor = mContext.getContentResolver().query(
                LessonEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        validateCursor("testBasicLessonQuery", lessonCursor, testValues);
        lessonCursor.close();
    }

    @Test
    public void testBasicFormulaQuery(){
        ContentValues testValues = new ContentValues();
        testValues.put(FormulaEntry.COLUMN_NAME, "Velocity");
        testValues.put(FormulaEntry.COLUMN_LESSON_KEY, "2");
        testValues.put(FormulaEntry.COLUMN_FORMULA, "");

        Cursor formulaCursor = mContext.getContentResolver().query(
                FormulaEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        validateCursor("testBasicFormulaQuery", formulaCursor, testValues);
        formulaCursor.close();
    }

    @Test
    public void testLessonWithChapterQuery(){
        ContentValues testValues = new ContentValues();
        testValues.put(LessonEntry.COLUMN_CHAPTER_KEY, "1");

        Cursor lessonCursor = mContext.getContentResolver().query(
                LessonEntry.buildLessonChapter("One-dimensional Motion"),
                null,
                null,
                null,
                null
        );

        validateCursor("testLessonWithChapterQuery", lessonCursor, testValues);
        lessonCursor.close();
    }

    @Test
    public void testFormulaWithLessonQuery(){
        ContentValues testValues = new ContentValues();
        testValues.put(FormulaEntry.COLUMN_NAME, "Velocity");
        testValues.put(FormulaEntry.COLUMN_LESSON_KEY, "2");
        testValues.put(FormulaEntry.COLUMN_FORMULA, "");

        Cursor formulaCursor = mContext.getContentResolver().query(
                FormulaEntry.buildFormulaLesson("Velocity"),
                null,
                null,
                null,
                null
        );

        validateCursor("testFormulaWithLessonQuery", formulaCursor, testValues);
        formulaCursor.close();
    }

    @Test
    public void testUpdateConstant(){
        Cursor c = db.rawQuery("SELECT " + DataContract.ConstantEntry._ID +
                " from " + ConstantEntry.TABLE_NAME + " WHERE " +
                DataContract.ChapterEntry.COLUMN_NAME + " = \"Acceleration of Gravity\"", null);
        c.moveToFirst();

        ContentValues updatedValues = new ContentValues();
        updatedValues.put(ConstantEntry.COLUMN_CURRENT, 9.81);

        int rowsUpdated = mContext.getContentResolver().update(ConstantEntry.CONTENT_URI,
                updatedValues, ConstantEntry._ID + " = ?",
                new String[] { Long.toString(c.getLong(c.getColumnIndex(ConstantEntry._ID)))}
        );
        assertTrue("No rows were updated.", rowsUpdated >= 1);
        c.close();
    }

    @After
    public void close(){
        db.close();
    }

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor,
                                      ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }
}
