package com.ps.physicssimulator.data;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
//                DataContract.ChapterEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL," +
//                DataContract.ChapterEntry.COLUMN_LOGO + " TEXT NOT NULL" + ");";
                DataContract.ChapterEntry.COLUMN_DESCRIPTION + " TEXT," +
                DataContract.ChapterEntry.COLUMN_LOGO + " TEXT" + ");";

        final String SQL_CREATE_LESSON_TABLE = "CREATE TABLE " +
                DataContract.LessonEntry.TABLE_NAME + " (" +
                DataContract.LessonEntry._ID + " INTEGER PRIMARY KEY, " +
                DataContract.LessonEntry.COLUMN_TITLE + " TEXT UNIQUE NOT NULL, " +
                DataContract.LessonEntry.COLUMN_CHAPTER_KEY + " INTEGER NOT NULL," +
                DataContract.LessonEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL," +
                DataContract.LessonEntry.COLUMN_CONTENT + " TEXT NOT NULL," +
//                DataContract.LessonEntry.COLUMN_LESSON_FRAGMENT_NAME + " TEXT NOT NULL," +
//                DataContract.LessonEntry.COLUMN_CALCULATOR_FRAGMENT_NAME + " TEXT NOT NULL," +
                DataContract.LessonEntry.COLUMN_LESSON_FRAGMENT_NAME + " TEXT," +
                DataContract.LessonEntry.COLUMN_CALCULATOR_FRAGMENT_NAME + " TEXT," +
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
                DataContract.FormulaEntry.COLUMN_LESSON_KEY + " INTEGER NOT NULL, " +
                DataContract.FormulaEntry.COLUMN_FORMULA + " TEXT" + ")";
//                DataContract.FormulaEntry.COLUMN_FORMULA + " TEXT NOT NULL" + ")";
//                DataContract.FormulaEntry.COLUMN_FRAGMENT_NAME + " TEXT NOT NULL"+ ")";

        final String SQL_CREATE_VARIABLE_TABLE = "CREATE TABLE " +
                DataContract.VariableEntry.TABLE_NAME + " (" +
                DataContract.VariableEntry._ID + " INTEGER PRIMARY KEY, " +
                DataContract.VariableEntry.COLUMN_NAME + " TEXT UNIQUE NOT NULL, " +
                DataContract.VariableEntry.COLUMN_FORMULA_KEY + " INTEGER NOT NULL, " +
                DataContract.VariableEntry.COLUMN_FRAGMENT_NAME + " TEXT, " +
//                DataContract.VariableEntry.COLUMN_FRAGMENT_NAME + " TEXT NOT NULL, " +
                DataContract.VariableEntry.COLUMN_FORMULA + " TEXT);";
//                DataContract.VariableEntry.COLUMN_FORMULA + " TEXT NOT NULL);";

        sqLiteDatabase.execSQL(SQL_CREATE_CHAPTER_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_LESSON_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_CONSTANT_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_FORMULA_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_VARIABLE_TABLE);
        initChapters(sqLiteDatabase);
        initLessons(sqLiteDatabase);
        initConstants(sqLiteDatabase);
        initFormulas(sqLiteDatabase);
        initVariables(sqLiteDatabase);
    }



    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DataContract.ChapterEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DataContract.LessonEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DataContract.ConstantEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DataContract.FormulaEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DataContract.VariableEntry.TABLE_NAME);

        onCreate(sqLiteDatabase);
    }

    private void initChapters(SQLiteDatabase database){
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

    private void initLessons(SQLiteDatabase database){
        if(database.isOpen()){
            String[][] lessons = {
                    {"Scalar and Vector Values", "One-dimensional Motion",
                            "Definition, Distance and Displacement","","",
                            "com.ps.physicssimulator.lessons.ValuesFragment",
                            "com.ps.physicssimulator.calculator.ValuesFragment"},
                    {"Velocity", "One-dimensional Motion",
                            "Definition, Speed and Velocity, Average Velocity, Instantaneous " +
                                    "Velocity", "","",
                            "com.ps.physicssimulator.lessons.VelocityFragment",
                            "com.ps.physicssimulator.calculator.VelocityFragment"},
                    {"Acceleration", "One-dimensional Motion","Definition, Acceleration, Average " +
                            "Acceleration, Instantaneous Acceleration","","","", ""},
                    {"Free-fall", "One-dimensional Motion","Definition, Free fall","","","", ""},
                    {"Projectile Motion", "Two-dimensional Motion","Definition, " +
                            "Projectile Motion","","","", ""},
                    {"Friction", "Isaac Newton's Laws of Motion","Definition, Two types of " +
                            "Friction","","","", ""},
                    {"Free-body Diagrams", "Isaac Newton's Laws of Motion","Definition, Free " +
                            "Body Diagrams","","","", ""},
                    {"Momentum and Impulse", "Momentum and Impulse","Definition, Momentum, " +
                            "Impulse","","","", ""},
                    {"Law of Conservation of Energy", "Momentum and Impulse","Definition, " +
                            "Conservation of Energy","","","", ""},
                    {"Work", "Work, Energy, and Power","Definition, Work","","","", ""},
                    {"Energy", "Work, Energy, and Power","Definition, Kinetic Energy, " +
                            "Potential Energy, Total Mechanical Energy","","","", ""},
                    {"Power", "Work, Energy, and Power","Definition, Average Power, " +
                            "Instantaneous Power","","","", ""},
                    {"Uniform Circular Motion", "Uniform Circular Motion","Definition, " +
                            "Measurements of a Circle, Frequency, Angular Displacement, " +
                            "Length of Arc, Tangential Velocity, Angular Velocity, " +
                            "Centripetal Acceleration","","","", ""},
                    {"Centripetal and Centrifugal Forces", "Uniform Circular Motion",
                            "Definition, Centripetal and Centrifugal Forces","","","", ""},
                    {"Rotational Motion", "Uniform Circular Motion","Definition, Moment of " +
                            "Inertia, Torque, Angular Momentum","","","", ""}
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
                values.put(DataContract.LessonEntry.COLUMN_LESSON_FRAGMENT_NAME, s[5]);
//                values.put(DataContract.LessonEntry.COLUMN_CALCFRAGNAME, s[6]);

                database.insert(DataContract.LessonEntry.TABLE_NAME, null, values);
            }
        }
    }

    private void initConstants(SQLiteDatabase database){
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

    private void initFormulas(SQLiteDatabase database){
        if(database.isOpen()) {
            String[][] formulas = {
                    {"Displacement", "Scalar and Vector Values", ""},
                    {"Speed", "Velocity", "", ""},
                    {"Velocity", "Velocity", "", ""},
                    {"Average Velocity", "Velocity", "", ""},
                    {"Acceleration", "Acceleration", "", ""},
                    {"Average Acceleration","Acceleration", "", ""},
                    {"Velocity","Free-fall", "", ""},
                    {"Displacement", "Free-fall", "", ""},
                    {"Horizontal Distance", "Projectile Motion", "", ""},
                    {"Horizontal Velocity", "Projectile Motion", "", ""},
                    {"Vertical Distance",  "Projectile Motion", "", ""},
                    {"Vertical Velocity",  "Projectile Motion", "", ""},
                    {"Time of Flight", "Projectile Motion", "", ""},
                    {"Maximum Height Reached", "Projectile Motion", "", ""},
                    {"Horizontal Range", "Projectile Motion", "", ""},
                    {"Friction", "Friction", "", ""},
                    {"Momentum", "Momentum and Impulse", "", ""},
                    {"Impulse", "Momentum and Impulse", "", ""},
                    {"Conservation of Energy", "Law of Conservation of Energy", "", ""},
                    {"Work", "Work", "", ""},
                    {"Kinetic Energy", "Energy", "", ""},
                    {"Gravitational Potential Energy", "Energy", "", ""},
                    {"Spring Potential Energy", "Energy", "", ""},
                    {"Total Mechanical Energy", "Energy", "", ""},
                    {"Average Power", "Power", "", ""},
                    {"Instantaneous Power","Power", "", ""},
                    {"Length of Arc",  "Uniform Circular Motion", "", ""},
                    {"Tangential Velocity", "Uniform Circular Motion", "", ""},
                    {"Velocity around a Circle", "Uniform Circular Motion", "", ""},
                    {"Angular Velocity", "Uniform Circular Motion", "", ""},
                    {"Angular Velocity", "Uniform Circular Motion", "", ""},
                    {"Centripetal Acceleration", "Uniform Circular Motion", "", ""},
                    {"Centripetal Force","Centripetal and Centrifugal Forces", "", ""},
                    {"Moment of Inertia", "Rotational Motion", "", ""},
                    {"Torque","Rotational Motion", "", ""},
                    {"Angular Momentum", "Rotational Motion", "", ""},
            };

            for(String[] s: formulas){
                Log.d("asdf", s[1]);
                Cursor c = database.rawQuery("SELECT " + DataContract.LessonEntry._ID +
                        " from lesson WHERE " + DataContract.LessonEntry.COLUMN_TITLE +
                        " = \"" + s[1] + "\"", null);
                c.moveToFirst();

                ContentValues values = new ContentValues();
                values.put(DataContract.FormulaEntry.COLUMN_NAME, s[0]);
                //values.put(DataContract.FormulaEntry.COLUMN_VAR, s[1]);
                values.put(DataContract.FormulaEntry.COLUMN_LESSON_KEY,
                        c.getLong(c.getColumnIndex(DataContract.LessonEntry._ID)));
                values.put(DataContract.FormulaEntry.COLUMN_FORMULA, s[2]);
//                values.put(DataContract.FormulaEntry.COLUMN_FRAGMENT_NAME, s[4]);

                database.insert(DataContract.FormulaEntry.TABLE_NAME, null, values);
            }
        }
    }

    private void initVariables(SQLiteDatabase database) {
        if(database.isOpen()) {
            String[][] formulas = {
                    {"Displacement", "Average Acceleration", "", ""},
                    {"Displacement", "Initial Velocity","", ""},
                    {"Displacement", "Final Velocity", "", ""},

                    {"Speed", "Speed", "", ""},
                    {"Speed", "Distance", "", ""},
                    {"Speed", "Time", "", ""},

                    {"Velocity", "Velocity", "", ""},
                    {"Velocity", "Initial Position", "", ""},
                    {"Velocity", "Final Position", "", ""},
                    {"Velocity", "Time", "", ""},

                    {"Average Velocity", "Average Velocity", "", ""},
                    {"Average Velocity", "Initial Position", "", ""},
                    {"Average Velocity", "Final Position",  "", ""},
                    {"Average Velocity", "Initial Time","", ""},
                    {"Average Velocity", "Final Time", "", ""},

                    {"Acceleration", "Acceleration", "", ""},
                    {"Acceleration", "Total Velocity Change", "", ""},
                    {"Acceleration", "Time Interval", "", ""},

                    {"Average Acceleration", "Average Acceleration", "", ""},
                    {"Average Acceleration", "Initial Velocity", "", ""},
                    {"Average Acceleration", "Final Velocity", "", ""},
                    {"Average Acceleration", "Initial Time", "", ""},
                    {"Average Acceleration", "Final Time", "", ""},

                    {"Velocity", "Velocity", "", ""},
                    {"Velocity", "Initial Velocity", "", ""},
                    {"Velocity", "Acceleration due to Gravity", "", ""},
                    {"Velocity", "Time", "", ""},

                    {"Displacement", "Displacement","", ""},
                    {"Displacement", "Initial Velocity","", ""},
                    {"Displacement", "Acceleration due to Gravity", "", ""},
                    {"Displacement", "Time",  "", ""},

                    {"Horizontal Distance", "Horizontal Distance","", ""},
                    {"Horizontal Distance", "Velocity along the x-axis","", ""},
                    {"Horizontal Distance", "Time","", ""},

                    {"Horizontal Velocity", "Velocity along the x-axis", "", ""},
                    {"Horizontal Velocity", "Initial Velocity along the x-axis", "", ""},

                    {"Vertical Distance", "Vertical Distance", "", ""},
                    {"Vertical Distance", "Initial Velocity along the y-axis", "", ""},
                    {"Vertical Distance", "Time","", ""},
                    {"Vertical Distance", "Acceleration due to Gravity","", ""},

                    {"Vertical Velocity", "Vertical Velocity","", ""},
                    {"Vertical Velocity", "Initial Velocity along the y-axis", "", ""},
                    {"Vertical Velocity", "Time","", ""},
                    {"Vertical Velocity", "Acceleration due to Gravity","", ""},

                    {"Time of Flight", "Time of Flight","", ""},
                    {"Time of Flight", "Initial Velocity","", ""},
                    {"Time of Flight", "Angle of Trajectory","", ""},
                    {"Time of Flight", "Acceleration due to Gravity","", ""},

                    {"Maximum Height Reached", "Maximum Height Reached","", ""},
                    {"Maximum Height Reached", "Initial Velocity",  "", ""},
                    {"Maximum Height Reached", "Angle of Trajectory", "", ""},
                    {"Maximum Height Reached", "Acceleration due to Gravity", "", ""},

                    {"Horizontal Range", "Horizontal Range","", ""},
                    {"Horizontal Range", "Initial Velocity","", ""},
                    {"Horizontal Range", "Time of Flight", "", ""},

                    {"Friction", "Frictional Force", "", ""},
                    {"Friction", "Coefficient of Friction", "", ""},
                    {"Friction", "Normal Force", "", ""},

                    {"Momentum", "Momentum","", ""},
                    {"Momentum", "Mass", "", ""},
                    {"Momentum", "Velocity", "", ""},

                    {"Impulse", "Force","", ""},
                    {"Impulse", "Distance", "", ""},
                    {"Impulse", "Change in Velocity", "", ""},
                    {"Impulse", "Time","", ""},

                    {"Conservation of Energy", "Mass",  "", ""},
                    {"Conservation of Energy", "Final Velocity", "", ""},
                    {"Conservation of Energy", "Height",  "", ""},
                    {"Conservation of Energy", "Acceleration due to Gravity", "", ""},

                    {"Work", "Work", "", ""},
                    {"Work", "Force", "", ""},
                    {"Work", "Distance", "", ""},

                    {"Kinetic Energy", "Kinetic Energy", "", ""},
                    {"Kinetic Energy", "Mass", "", ""},
                    {"Kinetic Energy", "Velocity", "", ""},

                    {"Gravitational Potential Energy", "Potential Energy",  "", ""},
                    {"Gravitational Potential Energy", "Height", "", ""},
                    {"Gravitational Potential Energy", "Acceleration due to Gravity", "", ""},

                    {"Spring Potential Energy", "Potential Energy", "", ""},
                    {"Spring Potential Energy", "Spring Constant", "", ""},
                    {"Spring Potential Energy", "Spring Displacement", "", ""},

                    {"Total Mechanical Energy", "Total Mechanical Energy", "", ""},
                    {"Total Mechanical Energy", "Kinetic Energy", "", ""},
                    {"Total Mechanical Energy", "Potential Energy", "", ""},

                    {"Average Power", "Average Power", "", ""},
                    {"Average Power", "Amount of Work done", "", ""},
                    {"Average Power", "Time", "", ""},

                    {"Instantaneous Power", "Instantaneous Power", "", ""},
                    {"Instantaneous Power", "Amount of Work done", "", ""},
                    {"Instantaneous Power", "Time", "", ""},
                    {"Instantaneous Power", "Force",  "", ""},
                    {"Instantaneous Power", "Path", "", ""},
                    {"Instantaneous Power", "Angle", "", ""},
                    {"Instantaneous Power", "Velocity", "", ""},

                    {"Length of Arc", "Length of Arc", "", ""},
                    {"Length of Arc", "Radius", "", ""},
                    {"Length of Arc", "Angle",  "", ""},

                    {"Tangential Velocity", "Tangential Velocity", "", ""},
                    {"Tangential Velocity", "Length of Arc","", ""},
                    {"Tangential Velocity", "Time",  "", ""},

                    {"Velocity around a Circle", "Velocity around a Circle", "", ""},
                    {"Velocity around a Circle", "Radius", "", ""},
                    {"Velocity around a Circle", "Time","", ""},

                    {"Angular Velocity", "Angular Velocity", "", ""},
                    {"Angular Velocity", "Angel", "", ""},
                    {"Angular Velocity", "Time","", ""},

                    {"Angular Velocity", "Angular Velocity",  "", ""},
                    {"Angular Velocity", "Angle",  "", ""},
                    {"Angular Velocity", "Time", "", ""},

                    {"Centripetal Acceleration", "Centripetal Acceleration", "", ""},
                    {"Centripetal Acceleration", "Angular Velocity","", ""},
                    {"Centripetal Acceleration", "Angle", "", ""},

                    {"Centripetal Force", "Centripetal Force", "", ""},
                    {"Centripetal Force", "Mass", "", ""},
                    {"Centripetal Force", "Tangential Velocity", "", ""},
                    {"Centripetal Force", "Radius","", ""},

                    {"Moment of Inertia", "Moment of Inertia", "", ""},
                    {"Moment of Inertia", "Mass", "", ""},
                    {"Moment of Inertia", "Distance","", ""},

                    {"Torque", "Torque","", ""},
                    {"Torque", "Force", "", ""},
                    {"Torque", "Position",  "", ""},

                    {"Angular Momentum", "Angular Momentum", "", ""},
                    {"Angular Momentum", "Moment of Inertia", "", ""},
                    {"Angular Momentum", "Angular Velocity", "", ""},
            };

            for(String[] s: formulas){
                Cursor c = database.rawQuery("SELECT " + DataContract.FormulaEntry._ID +
                        " from formula WHERE " + DataContract.FormulaEntry.COLUMN_NAME +
                        " = \"" + s[0] + "\"", null);
                c.moveToFirst();

                ContentValues values = new ContentValues();
                values.put(DataContract.VariableEntry.COLUMN_FORMULA_KEY,
                        c.getLong(c.getColumnIndex(DataContract.FormulaEntry._ID)));
                values.put(DataContract.VariableEntry.COLUMN_NAME, s[1]);
                values.put(DataContract.VariableEntry.COLUMN_FORMULA, s[2]);
                values.put(DataContract.VariableEntry.COLUMN_FRAGMENT_NAME, s[3]);

                database.insert(DataContract.VariableEntry.TABLE_NAME, null, values);
            }
        }
    }
}





