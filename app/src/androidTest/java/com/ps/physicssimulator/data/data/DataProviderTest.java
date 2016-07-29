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

import static com.ps.physicssimulator.data.DataContract.CONTENT_AUTHORITY;
import static com.ps.physicssimulator.data.DataContract.VariableEntry;
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
                    " instead of authority: " + CONTENT_AUTHORITY,
                    providerInfo.authority, CONTENT_AUTHORITY);
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

        type = mContext.getContentResolver().getType(ConstantEntry.buildConstantUri(1234));
        assertEquals("Uri should return CONTENT_ITEM_TYPE",
                ConstantEntry.CONTENT_ITEM_TYPE, type);

        type = mContext.getContentResolver().getType(FormulaEntry.CONTENT_URI);
        assertEquals("Uri should return CONTENT_TYPE",
                FormulaEntry.CONTENT_TYPE, type);

        type = mContext.getContentResolver().getType(FormulaEntry.buildFormulaLesson(testVar));
        assertEquals("Uri should return CONTENT_TYPE",
                FormulaEntry.CONTENT_TYPE, type);

        type = mContext.getContentResolver().getType(VariableEntry.CONTENT_URI);
        assertEquals("Uri should return CONTENT_TYPE",
                VariableEntry.CONTENT_TYPE, type);

        type = mContext.getContentResolver().getType(VariableEntry.buildVariableFormula(testVar));
        assertEquals("Uri should return CONTENT_TYPE",
                VariableEntry.CONTENT_TYPE, type);

        type = mContext.getContentResolver().getType(DataContract.FormulaConstantEntry.buildFormulaConstant(testVar));
        assertEquals("Uri should return CONTENT_TYPE",
                DataContract.FormulaConstantEntry.CONTENT_TYPE, type);
    }

    @Test
    public void testBasicChapterQuery(){

        ContentValues testValues = new ContentValues();
        testValues.put(ChapterEntry.COLUMN_NAME, "One-dimensional Motion");


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
        testValues.put(ConstantEntry.COLUMN_NAME, "Acceleration due to Gravity");
        testValues.put(ConstantEntry.COLUMN_DEFAULT, 9.8);
        testValues.put(ConstantEntry.COLUMN_CURRENT, 9.8);

        Cursor constantCursor = mContext.getContentResolver().query(
                ConstantEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );


        validateCursor("testBasicConstantQuery", constantCursor, testValues);
        constantCursor.close();
    }

    @Test
    public void testBasicLessonQuery(){
        ContentValues testValues = new ContentValues();
        testValues.put(LessonEntry.COLUMN_TITLE, "Scalar and Vector Values");
        testValues.put(LessonEntry.COLUMN_CHAPTER_KEY, "1");
        testValues.put(LessonEntry.COLUMN_DESCRIPTION, "Definition, Distance and Displacement");

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
        testValues.put(FormulaEntry.COLUMN_NAME, "Displacement");
        testValues.put(FormulaEntry.COLUMN_LESSON_KEY, "1");
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
    public void testBasicVariableQuery(){
        ContentValues testValues = new ContentValues();
        testValues.put(VariableEntry.COLUMN_NAME, "Speed");
        testValues.put(VariableEntry.COLUMN_FORMULA_KEY, "2");
//        testValues.put(VariableEntry.COLUMN_FORMULA_DISPLAY, "");
//        testValues.put(VariableEntry.COLUMN_FORMULA_COMPUTE, "");

        Cursor varCursor  = mContext.getContentResolver().query(
                VariableEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        validateCursor("testBasicVariableQuery", varCursor, testValues);
        varCursor.close();
    }

    @Test
    public void testBasicFormulaConstantQuery(){
        Cursor b = dbHelper.getReadableDatabase().rawQuery("SELECT " + FormulaEntry._ID + " FROM formula"
                + " WHERE " + FormulaEntry.COLUMN_NAME + " = 'Free-fall Velocity'", null);

        b.moveToFirst();

        Cursor c = dbHelper.getReadableDatabase().rawQuery("SELECT " + ConstantEntry._ID + " FROM constant"
                + " WHERE " + ConstantEntry.COLUMN_NAME + " = 'Acceleration due to Gravity'", null);

        c.moveToFirst();

        ContentValues testValues = new ContentValues();
        testValues.put(DataContract.FormulaConstantEntry.COLUMN_CONSTANT_KEY, c.getLong(c.getColumnIndex(FormulaEntry._ID)));
        testValues.put(DataContract.FormulaConstantEntry.COLUMN_FORMULA_KEY, b.getLong(b.getColumnIndex(ConstantEntry._ID)));
//        testValues.put(VariableEntry.COLUMN_FORMULA_DISPLAY, "");
//        testValues.put(VariableEntry.COLUMN_FORMULA_COMPUTE, "");

        Cursor formulaConstantCursor  = mContext.getContentResolver().query(
                DataContract.FormulaConstantEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        validateCursor("testBasicFormulaConstantQuery", formulaConstantCursor, testValues);
        formulaConstantCursor.close();
    }

    @Test
    public void testBasicFormulaConstantWithFormulaQuery(){
        Cursor b = dbHelper.getReadableDatabase().rawQuery("SELECT " + FormulaEntry._ID + " FROM formula"
                + " WHERE " + FormulaEntry.COLUMN_NAME + " = 'Free-fall Velocity'", null);

        b.moveToFirst();

        Cursor c = dbHelper.getReadableDatabase().rawQuery("SELECT " + ConstantEntry._ID + " FROM constant"
                + " WHERE " + ConstantEntry.COLUMN_NAME + " = 'Acceleration due to Gravity'", null);

        c.moveToFirst();

        ContentValues testValues = new ContentValues();
        testValues.put(DataContract.FormulaConstantEntry.COLUMN_CONSTANT_KEY, c.getLong(c.getColumnIndex(FormulaEntry._ID)));
        testValues.put(DataContract.FormulaConstantEntry.COLUMN_FORMULA_KEY, b.getLong(b.getColumnIndex(ConstantEntry._ID)));
//        testValues.put(VariableEntry.COLUMN_FORMULA_DISPLAY, "");
//        testValues.put(VariableEntry.COLUMN_FORMULA_COMPUTE, "");

        Cursor formulaConstantCursor  = mContext.getContentResolver().query(
                DataContract.FormulaConstantEntry.buildFormulaConstant("Free-fall Velocity"),
                null,
                null,
                null,
                null
        );

        validateCursor("testBasicFormulaConstantWithFormulaQuery", formulaConstantCursor, testValues);
        formulaConstantCursor.close();
    }

    @Test
    public void testConstantWithId(){
        ContentValues testValues = new ContentValues();
        testValues.put(ConstantEntry.COLUMN_NAME, "Acceleration due to Gravity");
        testValues.put(ConstantEntry.COLUMN_DEFAULT, 9.8);
        testValues.put(ConstantEntry.COLUMN_CURRENT, 9.8);

        Cursor c = dbHelper.getReadableDatabase().rawQuery("SELECT " + ConstantEntry._ID + " FROM constant"
        + " WHERE " + ConstantEntry.COLUMN_NAME + " = 'Acceleration due to Gravity'", null);

        c.moveToFirst();

        Cursor constantCursor = mContext.getContentResolver().query(
                ConstantEntry.buildConstantUri(c.getLong(c.getColumnIndex(ConstantEntry._ID))),
                null,
                null,
                null,
                null
        );

        validateCursor("testConstantWithId", constantCursor, testValues);
        constantCursor.close();
    }

    @Test
    public void testVariableWithNameQuery(){
        ContentValues testValues = new ContentValues();
        testValues.put(VariableEntry.COLUMN_NAME, "Speed");
        testValues.put(VariableEntry.COLUMN_FORMULA_KEY, "2");
        testValues.put(VariableEntry.COLUMN_FORMULA_DISPLAY, "$$s = {d \\over t}$$");
        testValues.put(VariableEntry.COLUMN_FORMULA_COMPUTE, "d / t");

        Cursor varCursor = mContext.getContentResolver().query(
                VariableEntry.buildVariableFormula("Speed"),
                null,
                null,
                null,
                null
        );

        validateCursor("testVariableWithNameQuery", varCursor, testValues);
        varCursor.close();
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
        testValues.put(FormulaEntry.COLUMN_NAME, "Speed");
        testValues.put(FormulaEntry.COLUMN_LESSON_KEY, "2");
        testValues.put(FormulaEntry.COLUMN_FORMULA, "$$s = {d \\over t}$$");

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
    public void testFormulaWithNameQuery(){
        ContentValues testValues = new ContentValues();
        testValues.put(FormulaEntry.COLUMN_NAME, "Displacement");
        testValues.put(FormulaEntry.COLUMN_LESSON_KEY, "1");
        testValues.put(FormulaEntry.COLUMN_FORMULA, "");

        Cursor formulaCursor = mContext.getContentResolver().query(
                FormulaEntry.buildFormulaLesson("Scalar and Vector Values"),
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
        Cursor c = db.rawQuery("SELECT " + ConstantEntry._ID +
                " from " + ConstantEntry.TABLE_NAME + " WHERE " +
                ChapterEntry.COLUMN_NAME + " = \"Acceleration due to Gravity\"", null);
        c.moveToFirst();

        ContentValues updatedValues = new ContentValues();
        updatedValues.put(ConstantEntry.COLUMN_CURRENT, 9.8);

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
