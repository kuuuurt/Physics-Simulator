package com.ps.physicssimulator.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class DataContract   {

    public static final String CONTENT_AUTHORITY = "com.ps.physicssimulator";

    public static final Uri BASE_CONTENT_URI =  Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_CHAPTER = "chapter";
    public static final String PATH_LESSON = "lesson";
    public static final String PATH_CONSTANT = "constant";
    public static final String PATH_FORMULA = "formula";

    public static final class ChapterEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_CHAPTER).build();

        public static final String TABLE_NAME = "chapter";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "desc";
        public static final String COLUMN_LOGO = "logo";


        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CHAPTER;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CHAPTER;

        public static Uri buildChapterUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildChapterName (String name){
            return CONTENT_URI.buildUpon().appendPath(name).build();
        }

        public static String getNameFromUri(Uri uri){
            return uri.getPathSegments().get(1);
        }
    }

    public static final class LessonEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_LESSON).build();

        public static final String TABLE_NAME = "lesson";

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_CHAPTER_KEY = "chapter_id";
        public static final String COLUMN_DESCRIPTION = "desc";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_LOGO = "logo";
        public static final String COLUMN_LESSONFRAGNAME = "lessonfrag";
        public static final String COLUMN_CALCFRAGNAME = "calcfrag";


        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LESSON;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LESSON;

        public static Uri buildLessonUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildLessonTitle (String title){
            return CONTENT_URI.buildUpon().appendPath(title).build();
        }

        public static Uri buildLessonChapter (String chapter){
            return CONTENT_URI.buildUpon().appendPath("chapter").appendPath(chapter).build();
        }

        public static String getTitleFromUri(Uri uri){
            return uri.getPathSegments().get(1);
        }

        public static String getChapterFromUri(Uri uri){
            return uri.getPathSegments().get(2);
        }
    }

    public static final class FormulaEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FORMULA).build();

        public static final String TABLE_NAME = "formula";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_VAR = "variable";
        public static final String COLUMN_LESSON_KEY = "lesson_id";
        public static final String COLUMN_FORMULA = "image";
        public static final String COLUMN_FRAGNAME = "fragname";

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FORMULA;

        public static Uri buildFormulaUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildFormulaLesson(String lesson){
            return CONTENT_URI.buildUpon().appendPath(lesson).build();
        }

        public static Uri buildFormulaName(String name){
            return CONTENT_URI.buildUpon().appendPath("name").appendPath(name).build();
        }

        public static String getLessonFromUri(Uri uri){
            return uri.getLastPathSegment();
        }

        public static String getNameFromUri(Uri uri){
            return uri.getLastPathSegment();
        }
    }

    public static final class ConstantEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_CONSTANT).build();

        public static final String TABLE_NAME = "constant";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DEFAULT = "def";
        public static final String COLUMN_CURRENT = "current";

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CONSTANT;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CONSTANT;

        public static Uri buildConstantUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildConstantName(String name){
            return CONTENT_URI.buildUpon().appendPath(name).build();
        }

        public static String getNameFromUri(Uri uri){
            return uri.getPathSegments().get(1);
        }
    }
}
