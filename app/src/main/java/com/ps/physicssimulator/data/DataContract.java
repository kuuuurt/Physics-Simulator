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
    public static final String PATH_FORMULA_CONSTANT = "formula_constant";
    public static final String PATH_VARIABLE = "variable";
    public static final String PATH_SECTION = "section";
    public static final String PATH_IMAGE = "image";
    public static final String PATH_EXAMPLE = "example";

    public static final class ChapterEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_CHAPTER).build();

        public static final String TABLE_NAME = "chapter";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "desc";
        public static final String COLUMN_LOGO = "logo";
        public static final String COLUMN_HAS_CALCULATOR = "has_calculator";


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

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_CHAPTER_KEY = "chapter_id";
        public static final String COLUMN_DESCRIPTION = "desc";
        public static final String COLUMN_LOGO = "logo";
        public static final String COLUMN_HAS_SIMULATION = "has_simulation";
        public static final String COLUMN_HAS_CALCULATOR = "has_calculator";
        public static final String COLUMN_VIDEO_ID = "video";
        public static final String COLUMN_AUDIO = "audio";


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

    public static final class SectionEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_SECTION).build();

        public static final String TABLE_NAME = "section";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_LESSON_KEY = "lesson_id";

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SECTION;

        public static Uri buildSectionUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildSectionLesson(String lesson){
            return CONTENT_URI.buildUpon().appendPath(lesson).build();
        }

        public static String getLessonFromUri(Uri uri){
            return uri.getLastPathSegment();
        }
    }

    public static final class ImageEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_IMAGE).build();

        public static final String TABLE_NAME = "image";

        public static final String COLUMN_RESOURCE_NAME = "resname";
        public static final String COLUMN_CAPTION = "caption";
        public static final String COLUMN_SECTION_KEY = "section_id";


        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_IMAGE;

        public static Uri buildImageUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildImageSectionUri(String section){
            return CONTENT_URI.buildUpon().appendPath(section).build();
        }

        public static String getSectionFromUri(Uri uri){
            return uri.getLastPathSegment();
        }

    }

    public static final class FormulaEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FORMULA).build();

        public static final String TABLE_NAME = "formula";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_LESSON_KEY = "lesson_id";
        public static final String COLUMN_FORMULA = "formula";

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FORMULA;

        public static Uri buildFormulaUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildFormulaLesson(String lesson){
            return CONTENT_URI.buildUpon().appendPath(lesson).build();
        }

        public static String getLessonFromUri(Uri uri){
            return uri.getLastPathSegment();
        }

    }

    public static final class VariableEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_VARIABLE).build();

        public static final String TABLE_NAME = "variable";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_FORMULA_KEY = "formula_id";
        public static final String COLUMN_FORMULA_DISPLAY = "formula_display";
        public static final String COLUMN_FORMULA_COMPUTE = "formula_compute";
        public static final String COLUMN_SYMBOL_DISPLAY = "symbol_display";
        public static final String COLUMN_SYMBOL_COMPUTE = "symbol_compute";
        public static final String COLUMN_CONSTANT_KEY = "constant_id";
        public static final String COLUMN_UNIT = "unit";
        public static final String COLUMN_UNIT_TYPE = "unit_type";
        public static final String COLUMN_ALLOW_NEGATIVE = "allow_negative";

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" +
                        PATH_VARIABLE;

        public static Uri buildVariableUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildVariableFormula(String formula){
            return CONTENT_URI.buildUpon().appendPath(formula).build();
        }

        public static String getFormulaFromUri(Uri uri){
            return uri.getLastPathSegment();
        }
    }

    public static final class FormulaConstantEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FORMULA_CONSTANT).build();

        public static final String TABLE_NAME = "formula_constant";
        public static final String COLUMN_FORMULA_KEY = "formula_id";
        public static final String COLUMN_CONSTANT_KEY = "constant_id";

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FORMULA_CONSTANT;

        public static Uri buildFormulaConstantUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildFormulaConstant(String formula){
            return CONTENT_URI.buildUpon().appendPath(formula).build();
        }

        public static String getFormulaFromUri(Uri uri){
            return uri.getLastPathSegment();
        }
    }

    public static final class ConstantEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_CONSTANT).build();

        public static final String TABLE_NAME = "constant";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESC = "description";
        public static final String COLUMN_SYMBOL = "symbol";
        public static final String COLUMN_DEFAULT = "def";
        public static final String COLUMN_CURRENT = "current";

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CONSTANT;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CONSTANT;

        public static Uri buildConstantUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getIDFromUri(Uri uri){
            return uri.getLastPathSegment();
        }
    }

    public static final class ExampleEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_EXAMPLE).build();

        public static final String TABLE_NAME = "example";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_SECTION_KEY = "section_id";



        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EXAMPLE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EXAMPLE;

        public static Uri buildConstantUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getIDFromUri(Uri uri){
            return uri.getLastPathSegment();
        }

        public static Uri buildExampleSection(String section){
            return CONTENT_URI.buildUpon().appendPath(section).build();
        }

        public static String getSectionFromUri(Uri uri){
            return uri.getLastPathSegment();
        }
    }
}

