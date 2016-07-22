package com.ps.physicssimulator.data;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "physicssimulator.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_CHAPTER_TABLE = "CREATE TABLE " +
                DataContract.ChapterEntry.TABLE_NAME + " (" +
                DataContract.ChapterEntry._ID + " INTEGER PRIMARY KEY, " +
                DataContract.ChapterEntry.COLUMN_NAME + " TEXT UNIQUE NOT NULL, " +
                DataContract.ChapterEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL," +
                DataContract.ChapterEntry.COLUMN_LOGO + " TEXT NOT NULL" + ");";

        final String SQL_CREATE_LESSON_TABLE = "CREATE TABLE " +
                DataContract.LessonEntry.TABLE_NAME + " (" +
                DataContract.LessonEntry._ID + " INTEGER PRIMARY KEY, " +
                DataContract.LessonEntry.COLUMN_TITLE + " TEXT UNIQUE NOT NULL, " +
                DataContract.LessonEntry.COLUMN_CHAPTER_KEY + " INTEGER NOT NULL," +
                DataContract.LessonEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL," +
                DataContract.LessonEntry.COLUMN_CONTENT + " TEXT NOT NULL," +
                DataContract.LessonEntry.COLUMN_FRAGNAME + " TEXT NOT NULL," +
                DataContract.LessonEntry.COLUMN_LOGO + " TEXT NOT NULL" +");";

        final String SQL_CREATE_CONSTANT_TABLE = "CREATE TABLE " +
                DataContract.ConstantEntry.TABLE_NAME + " (" +
                DataContract.ConstantEntry._ID + " INTEGER PRIMARY KEY, " +
                DataContract.ConstantEntry.COLUMN_NAME + " TEXT UNIQUE NOT NULL, " +
                DataContract.ConstantEntry.COLUMN_DEFAULT + " REAL UNIQUE NOT NULL, " +
                DataContract.ConstantEntry.COLUMN_CURRENT + " REAL NOT NULL);";

        sqLiteDatabase.execSQL(SQL_CREATE_CHAPTER_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_LESSON_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_CONSTANT_TABLE);
        initChapters(sqLiteDatabase);
        initLessons(sqLiteDatabase);
        initConstants(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DataContract.ChapterEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DataContract.LessonEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DataContract.ConstantEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void initChapters(SQLiteDatabase database){
        if(database.isOpen()){
            String[][] chapters = {
                    {"One-dimensional Motion","",""},
                    {"Two-dimensional Motion","",""},
                    {"Isaac Newton's Laws of Motion","",""},
                    {"Momentum and Impulse","",""},
                    {"Work, Energy, and Power","",""},
                    {"Circular Motion","",""}
            };
            for(String[] s : chapters){
                ContentValues values = new ContentValues();
                values.put(DataContract.ChapterEntry.COLUMN_NAME, s[0]);
                values.put(DataContract.ChapterEntry.COLUMN_DESCRIPTION, s[1]);
                values.put(DataContract.ChapterEntry.COLUMN_LOGO, s[2]);

                database.insert(DataContract.ChapterEntry.TABLE_NAME, null, values);
            }

        }
    }

    public void initLessons(SQLiteDatabase database){
        if(database.isOpen()){
            String[][] lessons = {
                    {"Scalar and Vector Values", "One-dimensional Motion","","","",
                        "com.ps.physicssimulator.lessons.ValuesFragment"},
                    {"Velocity", "One-dimensional Motion","","","",
                        "com.ps.physicssimulator.lessons.VelocityFragment"},
                    {"Acceleration", "One-dimensional Motion","","","",""},
                    {"Free-fall", "One-dimensional Motion","","","",""},
                    {"Projectile Motion", "Two-dimensional Motion","","","",""},
                    {"First Law of Motion", "Isaac Newton's Laws of Motion","","","",""},
                    {"Friction", "Isaac Newton's Laws of Motion","","","",""},
                    {"Free-body Diagrams", "Isaac Newton's Laws of Motion","","","",""},
                    {"Second Law of Motion", "Isaac Newton's Laws of Motion","","","",""},
                    {"Third Law of Motion", "Isaac Newton's Laws of Motion","","","",""},
                    {"Momentum", "Momentum and Impulse","","","",""},
                    {"Impulse", "Momentum and Impulse","","","",""},
                    {"Law of Conservation of Energy", "Momentum and Impulse","","","",""},
                    {"Work", "Work, Energy, and Power","","","",""},
                    {"Energy", "Work, Energy, and Power","","","",""},
                    {"Power", "Work, Energy, and Power","","","",""},
                    {"Circular Velocity", "Circular Motion","","","",""},
                    {"Circular Acceleration", "Circular Motion","","","",""},
                    {"Centripetal Force", "Circular Motion","","","",""}
            };

            for(String[] s : lessons){
                Cursor c = database.rawQuery("SELECT " + DataContract.ChapterEntry._ID +
                        " from chapter WHERE " + DataContract.ChapterEntry.COLUMN_NAME +
                        " = \"" + s[1] + "\"", null);
                c.moveToFirst();

                ContentValues values = new ContentValues();
                values.put(DataContract.LessonEntry.COLUMN_TITLE, s[0]);
                values.put(DataContract.LessonEntry.COLUMN_CHAPTER_KEY,
                        c.getLong(c.getColumnIndex(DataContract.ChapterEntry._ID)));
                values.put(DataContract.LessonEntry.COLUMN_DESCRIPTION, s[2]);
                values.put(DataContract.LessonEntry.COLUMN_CONTENT, s[3]);
                values.put(DataContract.LessonEntry.COLUMN_LOGO, s[4]);
                values.put(DataContract.LessonEntry.COLUMN_FRAGNAME, s[5]);

                database.insert(DataContract.LessonEntry.TABLE_NAME, null, values);
            }
        }
    }

    public void initConstants(SQLiteDatabase database){
        if(database.isOpen()) {
            String[][] constants = {
                    {"Acceleration of Gravity", "9.8"}
            };

            for(String[] s: constants){
                ContentValues values = new ContentValues();
                values.put(DataContract.ConstantEntry.COLUMN_NAME, s[0]);
                values.put(DataContract.ConstantEntry.COLUMN_DEFAULT, Double.parseDouble(s[1]));
                values.put(DataContract.ConstantEntry.COLUMN_CURRENT, Double.parseDouble(s[1]));

                database.insert(DataContract.ConstantEntry.TABLE_NAME, null, values);
            }
        }
    }
}





