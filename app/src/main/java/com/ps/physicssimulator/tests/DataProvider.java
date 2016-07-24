package com.ps.physicssimulator.tests;

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
    static final int CONSTANT_WITH_NAME = 201;
    static final int CHAPTER = 300;
    static final int CHAPTER_WITH_NAME = 301;

    static UriMatcher uriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = DataContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, DataContract.PATH_LESSON, LESSON);
        matcher.addURI(authority, DataContract.PATH_LESSON + "/*", LESSON_WITH_TITLE);
        matcher.addURI(authority, DataContract.PATH_LESSON + "/*/*", LESSON_WITH_CHAPTER);
        matcher.addURI(authority, DataContract.PATH_CONSTANT, CONSTANT);
        matcher.addURI(authority, DataContract.PATH_CONSTANT + "/*", CONSTANT_WITH_NAME);
        matcher.addURI(authority, DataContract.PATH_CHAPTER, CHAPTER);
        matcher.addURI(authority, DataContract.PATH_CHAPTER + "/*", CHAPTER_WITH_NAME);

        return matcher;
    }

    private DBHelper dbHelper;
    private static final SQLiteQueryBuilder lessonQueryBuilder;
    private static final SQLiteQueryBuilder constantQueryBuilder;
    private static final SQLiteQueryBuilder chapterQueryBuilder;

    static{
        lessonQueryBuilder = new SQLiteQueryBuilder();
        lessonQueryBuilder.setTables(DataContract.LessonEntry.TABLE_NAME);
//        lessonQueryBuilder.setTables(DataContract.LessonEntry.TABLE_NAME + " LEFT OUTER JOIN " +
//                DataContract.ChapterEntry.TABLE_NAME + " ON " +
//                DataContract.LessonEntry.TABLE_NAME + "." +
//                DataContract.LessonEntry.COLUMN_CHAPTER_KEY + " = " +
//                DataContract.ChapterEntry.TABLE_NAME + "." +
//                DataContract.ChapterEntry._ID);

        constantQueryBuilder = new SQLiteQueryBuilder();
        constantQueryBuilder.setTables(DataContract.ConstantEntry.TABLE_NAME);

        chapterQueryBuilder = new SQLiteQueryBuilder();
        chapterQueryBuilder.setTables(DataContract.LessonEntry.TABLE_NAME);
    }

    private static final String lessonWithTitleQuery = DataContract.LessonEntry.TABLE_NAME + "." +
            DataContract.LessonEntry.COLUMN_TITLE + " = ? ";

    private static final String lessonWithChapterQuery = DataContract.LessonEntry.TABLE_NAME +
            "." + DataContract.LessonEntry.COLUMN_CHAPTER_KEY + " = ?";

    private static final String constantWithNameQuery = DataContract.ConstantEntry.TABLE_NAME +
            "." + DataContract.ConstantEntry.COLUMN_NAME + " = ? ";

    private static final String chapterWithNameQuery = DataContract.ChapterEntry.TABLE_NAME + "." +
            DataContract.ChapterEntry.COLUMN_NAME + " = ? ";



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



        return lessonQueryBuilder.query(database,
                projection,
                lessonWithChapterQuery,
                new String[]{String.valueOf(c.getLong(c.getColumnIndex(
                        DataContract.ChapterEntry._ID)))},
                null,
                null,
                sortOrder
        );
    }

    public Cursor getConstantWithName(Uri uri, String[] projection, String sortOrder){
        return constantQueryBuilder.query(dbHelper.getReadableDatabase(),
                projection,
                constantWithNameQuery,
                new String[]{DataContract.ConstantEntry.getNameFromUri(uri)},
                null,
                null,
                sortOrder
        );
    }

    public Cursor getChapterWithName(Uri uri, String[] projection, String sortOrder){
        return chapterQueryBuilder.query(dbHelper.getReadableDatabase(),
                projection,
                chapterWithNameQuery,
                new String[]{DataContract.ChapterEntry.getNameFromUri(uri)},
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
            case CONSTANT_WITH_NAME:
                data = getConstantWithName(uri, projection, sortOrder);
                break;
            case CHAPTER:
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
                data = getChapterWithName(uri, projection, sortOrder);
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
            case CONSTANT_WITH_NAME:
                return DataContract.ConstantEntry.CONTENT_ITEM_TYPE;
            case CHAPTER:
                return DataContract.ChapterEntry.CONTENT_TYPE;
            case CHAPTER_WITH_NAME:
                return DataContract.ChapterEntry.CONTENT_ITEM_TYPE;
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

