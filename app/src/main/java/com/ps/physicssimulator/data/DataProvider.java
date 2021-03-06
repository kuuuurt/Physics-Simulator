package com.ps.physicssimulator.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

public class DataProvider extends ContentProvider{
    static final int LESSON = 100;
    static final int LESSON_WITH_TITLE = 101;
    static final int LESSON_WITH_CHAPTER = 102;
    static final int CONSTANT = 200;
    static final int CONSTANT_WITH_FORMULA = 201;
    static final int CONSTANT_WITH_ID = 202;
    static final int CHAPTER = 300;
    static final int CHAPTER_WITH_NAME = 301;
    static final int FORMULA = 400;
    static final int FORMULA_WITH_LESSON = 401;
    static final int VARIABLE = 500;
    static final int VARIABLE_WITH_NAME = 501;
    static final int FORMULA_CONSTANT = 600;
    static final int FORMULA_CONSTANT_WITH_FORMULA = 601;
    static final int SECTION = 700;
    static final int SECTION_WITH_LESSON = 701;
    static final int IMAGE = 800;
    static final int IMAGE_WITH_SECTION = 801;
    static final int EXAMPLE = 900;
    static final int EXAMPLE_WITH_SECTION = 901;


    static UriMatcher uriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = DataContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, DataContract.PATH_LESSON, LESSON);
        matcher.addURI(authority, DataContract.PATH_LESSON + "/*", LESSON_WITH_TITLE);
        matcher.addURI(authority, DataContract.PATH_LESSON + "/*/*", LESSON_WITH_CHAPTER);
        matcher.addURI(authority, DataContract.PATH_CONSTANT, CONSTANT);
        matcher.addURI(authority, DataContract.PATH_CONSTANT + "/#", CONSTANT_WITH_ID);
        matcher.addURI(authority, DataContract.PATH_CHAPTER, CHAPTER);
        matcher.addURI(authority, DataContract.PATH_CHAPTER + "/*", CHAPTER_WITH_NAME);
        matcher.addURI(authority, DataContract.PATH_FORMULA, FORMULA);
        matcher.addURI(authority, DataContract.PATH_FORMULA + "/*", FORMULA_WITH_LESSON);
        matcher.addURI(authority, DataContract.PATH_VARIABLE, VARIABLE);
        matcher.addURI(authority, DataContract.PATH_VARIABLE + "/*", VARIABLE_WITH_NAME);
        matcher.addURI(authority, DataContract.PATH_FORMULA_CONSTANT, FORMULA_CONSTANT);
        matcher.addURI(authority, DataContract.PATH_FORMULA_CONSTANT + "/*", FORMULA_CONSTANT_WITH_FORMULA);
        matcher.addURI(authority, DataContract.PATH_SECTION, SECTION);
        matcher.addURI(authority, DataContract.PATH_SECTION + "/*", SECTION_WITH_LESSON);
        matcher.addURI(authority, DataContract.PATH_IMAGE, IMAGE);
        matcher.addURI(authority, DataContract.PATH_IMAGE + "/*", IMAGE_WITH_SECTION);
        matcher.addURI(authority, DataContract.PATH_EXAMPLE, EXAMPLE);
        matcher.addURI(authority, DataContract.PATH_EXAMPLE + "/*", EXAMPLE_WITH_SECTION);
        return matcher;
    }

    private DBHelper dbHelper;
    private static final SQLiteQueryBuilder lessonQueryBuilder;
    private static final SQLiteQueryBuilder constantQueryBuilder;
    private static final SQLiteQueryBuilder chapterQueryBuilder;
    private static final SQLiteQueryBuilder formulaQueryBuilder;
    private static final SQLiteQueryBuilder variableQueryBuilder;
    private static final SQLiteQueryBuilder formulaConstantQueryBuilder;
    private static final SQLiteQueryBuilder sectionQueryBuilder;
    private static final SQLiteQueryBuilder imageQueryBuilder;
    private static final SQLiteQueryBuilder exampleQueryBuilder;


    static{
        lessonQueryBuilder = new SQLiteQueryBuilder();
        lessonQueryBuilder.setTables(DataContract.LessonEntry.TABLE_NAME);

        constantQueryBuilder = new SQLiteQueryBuilder();
        constantQueryBuilder.setTables(DataContract.ConstantEntry.TABLE_NAME);

        chapterQueryBuilder = new SQLiteQueryBuilder();
        chapterQueryBuilder.setTables(DataContract.LessonEntry.TABLE_NAME);

        formulaQueryBuilder = new SQLiteQueryBuilder();
        formulaQueryBuilder.setTables(DataContract.FormulaEntry.TABLE_NAME);

        variableQueryBuilder = new SQLiteQueryBuilder();
        variableQueryBuilder.setTables(DataContract.VariableEntry.TABLE_NAME);

        formulaConstantQueryBuilder = new SQLiteQueryBuilder();
        formulaConstantQueryBuilder.setTables(DataContract.FormulaConstantEntry.TABLE_NAME);

        sectionQueryBuilder = new SQLiteQueryBuilder();
        sectionQueryBuilder.setTables(DataContract.SectionEntry.TABLE_NAME);

        imageQueryBuilder = new SQLiteQueryBuilder();
        imageQueryBuilder.setTables(DataContract.ImageEntry.TABLE_NAME);

        exampleQueryBuilder = new SQLiteQueryBuilder();
        exampleQueryBuilder.setTables(DataContract.ExampleEntry.TABLE_NAME);

    }


    private static final String lessonWithTitleQuery = DataContract.LessonEntry.TABLE_NAME + "." +
            DataContract.LessonEntry.COLUMN_NAME + " = ? ";

    private static final String lessonWithChapterQuery = DataContract.LessonEntry.TABLE_NAME +
            "." + DataContract.LessonEntry.COLUMN_CHAPTER_KEY + " = ? ";

    private static final String lessonWithChapterCalcQuery = DataContract.LessonEntry.TABLE_NAME +
            "." + DataContract.LessonEntry.COLUMN_CHAPTER_KEY + " = ? AND " +
            DataContract.LessonEntry.COLUMN_HAS_CALCULATOR +  " = ?";

    private static final String constantWithIdQuery = DataContract.ConstantEntry.TABLE_NAME +
            "." + DataContract.ConstantEntry._ID + " = ? ";

    private static final String chapterWithNameQuery = DataContract.ChapterEntry.TABLE_NAME + "." +
            DataContract.ChapterEntry.COLUMN_NAME + " = ? ";

    private static final String formulaWithLessonQuery = DataContract.FormulaEntry.TABLE_NAME + "."
            + DataContract.FormulaEntry.COLUMN_LESSON_KEY + " = ? ";

    private static final String variableWithNameQuery = DataContract.VariableEntry.TABLE_NAME + "."
            + DataContract.VariableEntry.COLUMN_FORMULA_KEY + " = ? ";

    private static final String formulaConstantQuery = DataContract.FormulaConstantEntry.TABLE_NAME + "."
            + DataContract.FormulaConstantEntry.COLUMN_FORMULA_KEY + " = ?";

    private static final String sectionLessonQuery = DataContract.SectionEntry.TABLE_NAME + "."
            + DataContract.SectionEntry.COLUMN_LESSON_KEY + " = ? ";

    private static final String imageSectionQuery = DataContract.ImageEntry.TABLE_NAME + "."
            + DataContract.ImageEntry.COLUMN_SECTION_KEY + " = ? ";

    private static final String exampleSectionQuery = DataContract.ExampleEntry.TABLE_NAME + "."
            + DataContract.ExampleEntry.COLUMN_SECTION_KEY + " = ? ";




    public Cursor getLessonByTitle(Uri uri, String[] projection, String sortOrder){
        return lessonQueryBuilder.query(dbHelper.getReadableDatabase(),
                projection,
                lessonWithTitleQuery,
                new String[]{DataContract.LessonEntry.getTitleFromUri(uri)},
                null,
                null,
                sortOrder
        );
    }

    public Cursor getLessonByChapter(Uri uri, String[] projection, String sortOrder){
        String chapter = DataContract.LessonEntry.getChapterFromUri(uri);
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        Cursor c = database.rawQuery("SELECT " + DataContract.ChapterEntry._ID +
                " from chapter WHERE " + DataContract.ChapterEntry.COLUMN_NAME +
                " = \"" + chapter + "\"", null);

        c.moveToFirst();


        if (sortOrder != null && sortOrder.equals("HasCalc"))
            return lessonQueryBuilder.query(database,
                    projection,
                    lessonWithChapterCalcQuery,
                    new String[]{String.valueOf(c.getLong(c.getColumnIndex(
                            DataContract.ChapterEntry._ID))), "1"},
                    null,
                    null,
                    null
            );

        return lessonQueryBuilder.query(database,
                projection,
                lessonWithChapterQuery,
                new String[]{String.valueOf(c.getLong(c.getColumnIndex(
                        DataContract.ChapterEntry._ID)))},
                null,
                null,
                null
        );

    }


    public Cursor getConstantById(Uri uri, String[] projection, String sortOrder){
        return constantQueryBuilder.query(dbHelper.getReadableDatabase(),
                projection,
                constantWithIdQuery,
                new String[]{DataContract.ConstantEntry.getIDFromUri(uri)},
                null,
                null,
                sortOrder
        );
    }

    public Cursor getChapterByName(Uri uri, String[] projection, String sortOrder){
        return chapterQueryBuilder.query(dbHelper.getReadableDatabase(),
                projection,
                chapterWithNameQuery,
                new String[]{DataContract.ChapterEntry.getNameFromUri(uri)},
                null,
                null,
                sortOrder
        );
    }

    public Cursor getFormulasByLesson(Uri uri, String[] projection, String sortOrder){
        String lesson = DataContract.FormulaEntry.getLessonFromUri(uri);

        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor c = database.rawQuery("SELECT " + DataContract.LessonEntry._ID +
                " from lesson WHERE " + DataContract.LessonEntry.COLUMN_NAME +
                " = \"" + lesson + "\"", null);

        c.moveToFirst();

        return formulaQueryBuilder.query(database,
                projection,
                formulaWithLessonQuery,
                new String[]{String.valueOf(c.getLong(c.getColumnIndex(
                        DataContract.LessonEntry._ID)))},
                null,
                null,
                sortOrder
        );
    }

    public Cursor getVariablesByName(Uri uri, String[] projection, String sortOrder){
        String formula = DataContract.VariableEntry.getFormulaFromUri(uri);

        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor c = database.rawQuery("SELECT " + DataContract.FormulaEntry._ID +
                " from formula WHERE " + DataContract.FormulaEntry.COLUMN_NAME +
                " = \"" + formula + "\"", null);

        c.moveToFirst();

        return variableQueryBuilder.query(dbHelper.getReadableDatabase(),
                projection,
                variableWithNameQuery,
                new String[]{String.valueOf(c.getLong(c.getColumnIndex(
                        DataContract.FormulaEntry._ID)))},
                null,
                null,
                sortOrder
        );
    }

    public Cursor getFormulaConstants(Uri uri, String[] projection, String sortOrder){
        String formula = DataContract.FormulaConstantEntry.getFormulaFromUri(uri);

        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor c = database.rawQuery("SELECT " + DataContract.FormulaEntry._ID +
                " from formula WHERE " + DataContract.FormulaEntry.COLUMN_NAME +
                " = \"" + formula + "\"", null);

        c.moveToFirst();

        return formulaConstantQueryBuilder.query(database,
                projection,
                formulaConstantQuery,
                new String[]{String.valueOf(c.getLong(c.getColumnIndex(DataContract.FormulaEntry._ID)))},
                null,
                null,
                sortOrder
        );
    }

    public Cursor getSectionsByLesson(Uri uri, String[] projection, String sortOrder){
        String lesson = DataContract.SectionEntry.getLessonFromUri(uri);

        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor c = database.rawQuery("SELECT " + DataContract.LessonEntry._ID +
                " from lesson WHERE " + DataContract.LessonEntry.COLUMN_NAME +
                " = \"" + lesson + "\"", null);

        c.moveToFirst();

        return sectionQueryBuilder.query(database,
                projection,
                sectionLessonQuery,
                new String[]{String.valueOf(c.getLong(c.getColumnIndex(DataContract.LessonEntry._ID)))},
                null,
                null,
                sortOrder
        );
    }

    public Cursor getImagesBySection(Uri uri, String[] projection, String sortOrder){
        String section = DataContract.ImageEntry.getSectionFromUri(uri);

        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor c = database.rawQuery("SELECT " + DataContract.SectionEntry._ID +
                " from section WHERE " + DataContract.SectionEntry.COLUMN_NAME +
                " = \"" + section + "\"", null);

        c.moveToFirst();

        return imageQueryBuilder.query(database,
                projection,
                imageSectionQuery,
                new String[]{c.getString(c.getColumnIndex(DataContract.SectionEntry._ID))},
                null,
                null,
                sortOrder
        );
    }

    public Cursor getExamplesBySection(Uri uri, String[] projection, String sortOrder){
        String section = DataContract.ExampleEntry.getSectionFromUri(uri);

        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor c = database.rawQuery("SELECT " + DataContract.SectionEntry._ID +
                " from section WHERE " + DataContract.SectionEntry.COLUMN_NAME +
                " = \"" + section + "\"", null);

        c.moveToFirst();

        return exampleQueryBuilder.query(database,
                projection,
                exampleSectionQuery,
                new String[]{c.getString(c.getColumnIndex(DataContract.SectionEntry._ID))},
                null,
                null,
                sortOrder
        );
    }



    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        Cursor data;
        switch(uriMatcher().match(uri)){
            case LESSON:
                data = dbHelper.getReadableDatabase().query(
                        DataContract.LessonEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case LESSON_WITH_TITLE:
                data = getLessonByTitle(uri, projection, sortOrder);
                break;
            case LESSON_WITH_CHAPTER:
                data = getLessonByChapter(uri, projection, sortOrder);
                break;
            case CONSTANT:
                data = dbHelper.getReadableDatabase().query(
                        DataContract.ConstantEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case CONSTANT_WITH_ID:
                data = getConstantById(uri, projection, sortOrder);
                break;
            case CHAPTER:
                if (sortOrder != null && sortOrder.equals("HasCalc"))
                    data = dbHelper.getReadableDatabase().query(
                        DataContract.ChapterEntry.TABLE_NAME,
                        projection,
                        DataContract.ChapterEntry.COLUMN_HAS_CALCULATOR + " = ?",
                        new String[]{"1"},
                        null,
                        null,
                        null
                    );
                else
                    data = dbHelper.getReadableDatabase().query(
                        DataContract.ChapterEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                    );
                break;
            case CHAPTER_WITH_NAME:
                data = getChapterByName(uri, projection, sortOrder);
                break;
            case FORMULA:
                data = dbHelper.getReadableDatabase().query(
                        DataContract.FormulaEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case FORMULA_WITH_LESSON:
                data = getFormulasByLesson(uri, projection, sortOrder);
                break;
            case VARIABLE:
                data = dbHelper.getReadableDatabase().query(
                        DataContract.VariableEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case VARIABLE_WITH_NAME:
                data = getVariablesByName(uri, projection, sortOrder);
                break;
            case FORMULA_CONSTANT:
                data = dbHelper.getReadableDatabase().query(
                        DataContract.FormulaConstantEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case FORMULA_CONSTANT_WITH_FORMULA:
                data = getFormulaConstants(uri, projection, sortOrder);
                break;
            case SECTION:
                data = dbHelper.getReadableDatabase().query(
                        DataContract.SectionEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case SECTION_WITH_LESSON:
                data = getSectionsByLesson(uri, projection, sortOrder);
                break;
            case IMAGE:
                data = dbHelper.getReadableDatabase().query(
                        DataContract.ImageEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case IMAGE_WITH_SECTION:
                data = getImagesBySection(uri, projection, sortOrder);
                break;
            case EXAMPLE:
                data = dbHelper.getReadableDatabase().query(
                        DataContract.ExampleEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case EXAMPLE_WITH_SECTION:
                data = getExamplesBySection(uri, projection, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        data.setNotificationUri(getContext().getContentResolver(), uri);
        return data;
    }



    @Nullable
    @Override
    public String getType(Uri uri) {
        switch(uriMatcher().match(uri)){
            case LESSON:
                return DataContract.LessonEntry.CONTENT_TYPE;
            case LESSON_WITH_TITLE:
                return DataContract.LessonEntry.CONTENT_ITEM_TYPE;
            case LESSON_WITH_CHAPTER:
                return DataContract.LessonEntry.CONTENT_TYPE;
            case CONSTANT:
                return DataContract.ConstantEntry.CONTENT_TYPE;
            case CONSTANT_WITH_ID:
                return DataContract.ConstantEntry.CONTENT_ITEM_TYPE;
            case CONSTANT_WITH_FORMULA:
                return DataContract.ConstantEntry.CONTENT_ITEM_TYPE;
            case CHAPTER:
                return DataContract.ChapterEntry.CONTENT_TYPE;
            case CHAPTER_WITH_NAME:
                return DataContract.ChapterEntry.CONTENT_ITEM_TYPE;
            case FORMULA:
                return DataContract.FormulaEntry.CONTENT_TYPE;
            case FORMULA_WITH_LESSON:
                return DataContract.FormulaEntry.CONTENT_TYPE;
            case VARIABLE:
                return DataContract.VariableEntry.CONTENT_TYPE;
            case VARIABLE_WITH_NAME:
                return DataContract.VariableEntry.CONTENT_TYPE;
            case FORMULA_CONSTANT:
                return DataContract.FormulaConstantEntry.CONTENT_TYPE;
            case FORMULA_CONSTANT_WITH_FORMULA:
                return DataContract.FormulaConstantEntry.CONTENT_TYPE;
            case SECTION:
                return DataContract.SectionEntry.CONTENT_TYPE;
            case SECTION_WITH_LESSON:
                return DataContract.SectionEntry.CONTENT_TYPE;
            case IMAGE:
                return DataContract.ImageEntry.CONTENT_TYPE;
            case IMAGE_WITH_SECTION:
                return DataContract.ImageEntry.CONTENT_TYPE;
            case EXAMPLE:
                return DataContract.ExampleEntry.CONTENT_TYPE;
            case EXAMPLE_WITH_SECTION:
                return DataContract.ExampleEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        int rowsUpdated;

        switch(uriMatcher().match(uri)){
            case CONSTANT:
                rowsUpdated = dbHelper.getWritableDatabase().update(
                        DataContract.ConstantEntry.TABLE_NAME, contentValues, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown/Unsupported uri: " + uri);
        }
        if(rowsUpdated != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}

