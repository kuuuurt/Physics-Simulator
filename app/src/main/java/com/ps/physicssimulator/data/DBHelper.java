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

        final String SQL_CREATE_FORMULA_TABLE = "CREATE TABLE " +
                DataContract.FormulaEntry.TABLE_NAME + " (" +
                DataContract.FormulaEntry._ID + " INTEGER PRIMARY KEY, " +
                DataContract.FormulaEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                DataContract.FormulaEntry.COLUMN_VAR + " TEXT NOT NULL, " +
                DataContract.FormulaEntry.COLUMN_LESSON_KEY + " INTEGER NOT NULL, " +
                DataContract.FormulaEntry.COLUMN_FORMULA + " TEXT NOT NULL" + ")";

        sqLiteDatabase.execSQL(SQL_CREATE_CHAPTER_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_LESSON_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_CONSTANT_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_FORMULA_TABLE);
        initChapters(sqLiteDatabase);
        initLessons(sqLiteDatabase);
        initConstants(sqLiteDatabase);
        initFormulas(sqLiteDatabase);
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
                    {"Uniform Circular Motion","",""}
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
                    {"Scalar and Vector Values", "One-dimensional Motion",
                            "Definition, Distance and Displacement","","",
                        "com.ps.physicssimulator.lessons.ValuesFragment"},
                    {"Velocity", "One-dimensional Motion",
                            "Definition, Speed and Velocity, Average Velocity, Instantaneous " +
                                    "Velocity","","",
                        "com.ps.physicssimulator.lessons.VelocityFragment"},
                    {"Acceleration", "One-dimensional Motion","Definition, Acceleration, Average " +
                            "Acceleration, Instantaneous Acceleration","","",""},
                    {"Free-fall", "One-dimensional Motion","Definition, Free fall","","",""},
                    {"Projectile Motion", "Two-dimensional Motion","Definition, " +
                            "Projectile Motion","","",""},
                    {"Friction", "Isaac Newton's Laws of Motion","Definition, Two types of " +
                            "Friction","","",""},
                    {"Free-body Diagrams", "Isaac Newton's Laws of Motion","Definition, Free " +
                            "Body Diagrams","","",""},
                    {"Momentum and Impulse", "Momentum and Impulse","Definition, Momentum, " +
                            "Impulse","","",""},
                    {"Law of Conservation of Energy", "Momentum and Impulse","Definition, " +
                            "Conservation of Energy","","",""},
                    {"Work", "Work, Energy, and Power","Definition, Work","","",""},
                    {"Energy", "Work, Energy, and Power","Definition, Kinetic Energy, " +
                            "Potential Energy, Total Mechanical Energy","","",""},
                    {"Power", "Work, Energy, and Power","Definition, Average Power, " +
                            "Instantaneous Power","","",""},
                    {"Uniform Circular Motion", "Uniform Circular Motion","Definition, " +
                            "Measurements of a Circle, Frequency, Angular Displacement, " +
                            "Length of Arc, Tangential Velocity, Angular Velocity, " +
                            "Centripetal Acceleration","","",""},
                    {"Centripetal and Centrifugal Forces", "Uniform Circular Motion",
                            "Definition, Centripetal and Centrifugal Forces","","",""},
                    {"Rotational Motion", "Uniform Circular Motion","Definition, Moment of " +
                            "Inertia, Torque, Angular Momentum","","",""}
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

    public void initFormulas(SQLiteDatabase database){
        if(database.isOpen()) {
            String[][] formulas = {
                    {"Displacement", "Average Acceleration", "Scalar and Vector Values", ""},
                    {"Displacement", "Initial Velocity", "Scalar and Vector Values", ""},
                    {"Displacement", "Final Velocity", "Scalar and Vector Values", ""},

                    {"Speed", "Speed", "Velocity", ""},
                    {"Speed", "Distance", "Velocity", ""},
                    {"Speed", "Time", "Velocity", ""},

                    {"Velocity", "Velocity", "Velocity", ""},
                    {"Velocity", "Initial Position", "Velocity", ""},
                    {"Velocity", "Final Position", "Velocity", ""},
                    {"Velocity", "Time", "Velocity", ""},

                    {"Average Velocity", "Average Velocity", "Velocity", ""},
                    {"Average Velocity", "Initial Position", "Velocity", ""},
                    {"Average Velocity", "Final Position", "Velocity", ""},
                    {"Average Velocity", "Initial Time", "Velocity", ""},
                    {"Average Velocity", "Final Time", "Velocity", ""},

                    {"Acceleration", "Acceleration", "Acceleration", ""},
                    {"Acceleration", "Total Velocity Change", "Acceleration", ""},
                    {"Acceleration", "Time Interval", "Acceleration", ""},

                    {"Average Acceleration", "Average Acceleration", "Acceleration", ""},
                    {"Average Acceleration", "Initial Velocity", "Acceleration", ""},
                    {"Average Acceleration", "Final Velocity", "Acceleration", ""},
                    {"Average Acceleration", "Initial Time", "Acceleration", ""},
                    {"Average Acceleration", "Final Time", "Acceleration", ""},

                    {"Velocity", "Velocity", "Free-fall", ""},
                    {"Velocity", "Initial Velocity", "Free-fall", ""},
                    {"Velocity", "Acceleration due to Gravity", "Free-fall", ""},
                    {"Velocity", "Time", "Free-fall", ""},

                    {"Displacement", "Displacement", "Free-fall", ""},
                    {"Displacement", "Initial Velocity", "Free-fall", ""},
                    {"Displacement", "Acceleration due to Gravity", "Free-fall", ""},
                    {"Displacement", "Time", "Free-fall", ""},

                    {"Horizontal Distance", "Horizontal Distance", "Projectile Motion", ""},
                    {"Horizontal Distance", "Velocity along the x-axis", "Projectile Motion", ""},
                    {"Horizontal Distance", "Time", "Projectile Motion", ""},

                    {"Horizontal Velocity", "Velocity along the x-axis", "Projectile Motion", ""},
                    {"Horizontal Velocity", "Initial Velocity along the x-axis",
                            "Projectile Motion", ""},

                    {"Vertical Distance", "Vertical Distance", "Projectile Motion", ""},
                    {"Vertical Distance", "Initial Velocity along the y-axis", "Projectile Motion",
                            ""},
                    {"Vertical Distance", "Time", "Projectile Motion", ""},
                    {"Vertical Distance", "Acceleration due to Gravity", "Projectile Motion", ""},

                    {"Vertical Velocity", "Vertical Velocity", "Projectile Motion", ""},
                    {"Vertical Velocity", "Initial Velocity along the y-axis", "Projectile Motion",
                            ""},
                    {"Vertical Velocity", "Time", "Projectile Motion", ""},
                    {"Vertical Velocity", "Acceleration due to Gravity", "Projectile Motion", ""},

                    {"Time of Flight", "Time of Flight", "Projectile Motion", ""},
                    {"Time of Flight", "Initial Velocity", "Projectile Motion", ""},
                    {"Time of Flight", "Angle of Trajectory", "Projectile Motion", ""},
                    {"Time of Flight", "Acceleration due to Gravity", "Projectile Motion", ""},

                    {"Maximum Height Reached", "Maximum Height Reached", "Projectile Motion", ""},
                    {"Maximum Height Reached", "Initial Velocity", "Projectile Motion", ""},
                    {"Maximum Height Reached", "Angle of Trajectory", "Projectile Motion", ""},
                    {"Maximum Height Reached", "Acceleration due to Gravity", "Projectile Motion",
                            ""},

                    {"Horizontal Range", "Horizontal Range", "Projectile Motion", ""},
                    {"Horizontal Range", "Initial Velocity", "Projectile Motion", ""},
                    {"Horizontal Range", "Time of Flight", "Projectile Motion", ""},

                    {"Friction", "Frictional Force", "Friction", ""},
                    {"Friction", "Coefficient of Friction", "Friction", ""},
                    {"Friction", "Normal Force", "Friction", ""},

                    {"Momentum", "Momentum", "Momentum and Impulse", ""},
                    {"Momentum", "Mass", "Momentum and Impulse", ""},
                    {"Momentum", "Velocity", "Momentum and Impulse", ""},

                    {"Impulse", "Force", "Momentum and Impulse", ""},
                    {"Impulse", "Distance", "Momentum and Impulse", ""},
                    {"Impulse", "Change in Velocity", "Momentum and Impulse", ""},
                    {"Impulse", "Time", "Momentum and Impulse", ""},

                    {"Conservation of Energy", "Mass", "Law of Conservation of Energy", ""},
                    {"Conservation of Energy", "Final Velocity", "Law of Conservation of Energy",
                            ""},
                    {"Conservation of Energy", "Height", "Law of Conservation of Energy", ""},
                    {"Conservation of Energy", "Acceleration due to Gravity",
                            "Law of Conservation of Energy", ""},

                    {"Work", "Work", "Work", ""},
                    {"Work", "Force", "Work", ""},
                    {"Work", "Distance", "Work", ""},

                    {"Kinetic Energy", "Kinetic Energy", "Energy", ""},
                    {"Kinetic Energy", "Mass", "Energy", ""},
                    {"Kinetic Energy", "Velocity", "Energy", ""},

                    {"Gravitational Potential Energy", "Potential Energy",
                            "Energy", ""},
                    {"Gravitational Potential Energy", "Height", "Energy", ""},
                    {"Gravitational Potential Energy", "Acceleration due to Gravity",
                            "Energy", ""},

                    {"Spring Potential Energy", "Potential Energy", "Energy", ""},
                    {"Spring Potential Energy", "Spring Constant", "Energy", ""},
                    {"Spring Potential Energy", "Spring Displacement", "Energy",
                            ""},

                    {"Total Mechanical Energy", "Total Mechanical Energy",
                            "Energy", ""},
                    {"Total Mechanical Energy", "Kinetic Energy", "Energy", ""},
                    {"Total Mechanical Energy", "Potential Energy", "Energy", ""},

                    {"Average Power", "Average Power", "Power", ""},
                    {"Average Power", "Amount of Work done", "Power", ""},
                    {"Average Power", "Time", "Power", ""},

                    {"Instantaneous Power", "Instantaneous Power", "Power", ""},
                    {"Instantaneous Power", "Amount of Work done", "Power", ""},
                    {"Instantaneous Power", "Time", "Power", ""},
                    {"Instantaneous Power", "Force", "Power", ""},
                    {"Instantaneous Power", "Path", "Power", ""},
                    {"Instantaneous Power", "Angle", "Power", ""},
                    {"Instantaneous Power", "Velocity", "Power", ""},

                    {"Length of Arc", "Length of Arc", "Uniform Circular Motion", ""},
                    {"Length of Arc", "Radius", "Uniform Circular Motion", ""},
                    {"Length of Arc", "Angle", "Uniform Circular Motion", ""},

                    {"Tangential Velocity", "Tangential Velocity", "Uniform Circular Motion", ""},
                    {"Tangential Velocity", "Length of Arc", "Uniform Circular Motion", ""},
                    {"Tangential Velocity", "Time", "Uniform Circular Motion", ""},

                    {"Velocity around a Circle", "Velocity around a Circle",
                            "Uniform Circular Motion", ""},
                    {"Velocity around a Circle", "Radius", "Uniform Circular Motion", ""},
                    {"Velocity around a Circle", "Time", "Uniform Circular Motion", ""},

                    {"Angular Velocity", "Angular Velocity", "Uniform Circular Motion", ""},
                    {"Angular Velocity", "Angel", "Uniform Circular Motion", ""},
                    {"Angular Velocity", "Time", "Uniform Circular Motion", ""},

                    {"Angular Velocity", "Angular Velocity", "Uniform Circular Motion", ""},
                    {"Angular Velocity", "Angle", "Uniform Circular Motion", ""},
                    {"Angular Velocity", "Time", "Uniform Circular Motion", ""},

                    {"Centripetal Acceleration", "Centripetal Acceleration",
                            "Uniform Circular Motion", ""},
                    {"Centripetal Acceleration", "Angular Velocity", "Uniform Circular Motion", ""},
                    {"Centripetal Acceleration", "Angle", "Uniform Circular Motion", ""},

                    {"Centripetal Force", "Centripetal Force",
                            "Centripetal and Centrifugal Forces", ""},
                    {"Centripetal Force", "Mass", "Centripetal and Centrifugal Forces", ""},
                    {"Centripetal Force", "Tangential Velocity",
                            "Centripetal and Centrifugal Forces", ""},
                    {"Centripetal Force", "Radius", "Centripetal and Centrifugal Forces", ""},

                    {"Moment of Inertia", "Moment of Inertia", "Rotational Motion", ""},
                    {"Moment of Inertia", "Mass", "Rotational Motion", ""},
                    {"Moment of Inertia", "Distance", "Rotational Motion", ""},

                    {"Torque", "Torque", "Rotational Motion", ""},
                    {"Torque", "Force", "Rotational Motion", ""},
                    {"Torque", "Position", "Rotational Motion", ""},

                    {"Angular Momentum", "Angular Momentum", "Rotational Motion", ""},
                    {"Angular Momentum", "Moment of Inertia", "Rotational Motion", ""},
                    {"Angular Momentum", "Angular Velocity", "Rotational Motion", ""},
            };

            for(String[] s: formulas){
                Cursor c = database.rawQuery("SELECT " + DataContract.LessonEntry._ID +
                        " from lesson WHERE " + DataContract.LessonEntry.COLUMN_TITLE +
                        " = \"" + s[2] + "\"", null);
                c.moveToFirst();

                ContentValues values = new ContentValues();
                values.put(DataContract.FormulaEntry.COLUMN_NAME, s[0]);
                values.put(DataContract.FormulaEntry.COLUMN_VAR, s[1]);
                values.put(DataContract.FormulaEntry.COLUMN_LESSON_KEY,
                        c.getLong(c.getColumnIndex(DataContract.LessonEntry._ID)));
                values.put(DataContract.FormulaEntry.COLUMN_FORMULA, s[3]);

                database.insert(DataContract.FormulaEntry.TABLE_NAME, null, values);
            }
        }
    }
}





