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
                DataContract.ChapterEntry.COLUMN_DESCRIPTION + " TEXT," +
                DataContract.ChapterEntry.COLUMN_LOGO + " TEXT" + ");";

        final String SQL_CREATE_LESSON_TABLE = "CREATE TABLE " +
                DataContract.LessonEntry.TABLE_NAME + " (" +
                DataContract.LessonEntry._ID + " INTEGER PRIMARY KEY, " +
                DataContract.LessonEntry.COLUMN_TITLE + " TEXT UNIQUE NOT NULL, " +
                DataContract.LessonEntry.COLUMN_CHAPTER_KEY + " INTEGER NOT NULL," +
                DataContract.LessonEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL," +
                DataContract.LessonEntry.COLUMN_CONTENT + " TEXT NOT NULL," +
                DataContract.LessonEntry.COLUMN_LESSON_FRAGMENT_NAME + " TEXT," +
                DataContract.LessonEntry.COLUMN_CALCULATOR_FRAGMENT_NAME + " TEXT," +
                DataContract.LessonEntry.COLUMN_LOGO + " TEXT NOT NULL" +");";

        final String SQL_CREATE_CONSTANT_TABLE = "CREATE TABLE " +
                DataContract.ConstantEntry.TABLE_NAME + " (" +
                DataContract.ConstantEntry._ID + " INTEGER PRIMARY KEY, " +
                DataContract.ConstantEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                DataContract.ConstantEntry.COLUMN_DEFAULT + " REAL UNIQUE NOT NULL, " +
                DataContract.ConstantEntry.COLUMN_CURRENT + " REAL NOT NULL);";

        final String SQL_CREATE_FORMULA_TABLE = "CREATE TABLE " +
                DataContract.FormulaEntry.TABLE_NAME + " (" +
                DataContract.FormulaEntry._ID + " INTEGER PRIMARY KEY, " +
                DataContract.FormulaEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                DataContract.FormulaEntry.COLUMN_LESSON_KEY + " INTEGER NOT NULL, " +
                DataContract.FormulaEntry.COLUMN_FORMULA + " TEXT" + ")";

        final String SQL_CREATE_VARIABLE_TABLE = "CREATE TABLE " +
                DataContract.VariableEntry.TABLE_NAME + " (" +
                DataContract.VariableEntry._ID + " INTEGER PRIMARY KEY, " +
                DataContract.VariableEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                DataContract.VariableEntry.COLUMN_FORMULA_KEY + " INTEGER NOT NULL, " +
                DataContract.VariableEntry.COLUMN_UNIT + " TEXT, " +
                DataContract.VariableEntry.COLUMN_SYMBOL + " TEXT, " +
                DataContract.VariableEntry.COLUMN_FORMULA_COMPUTE + " TEXT, " +
                DataContract.VariableEntry.COLUMN_FORMULA_DISPLAY + " TEXT);";

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
                    {"One-dimensional Motion","Scalar and Vector Values, Velocity, Acceleration, Free fall",""},
                    {"Two-dimensional Motion","Projectile Motion",""},
                    {"Isaac Newton's Laws of Motion","Friction, Free Body Diagrams",""},
                    {"Momentum and Impulse","Momentum and Impulse",""},
                    {"Work, Energy, and Power","Work, Energy, Power",""},
                    {"Uniform Circular Motion","Uniform Circular Motion, Centripetal and Centrifugal Forces, Rotational Motion",""}
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
                    {"Speed", "Velocity", "$$s = {d \\over t}$$", ""},
                    {"Velocity", "Velocity", "$$v = {d \\over t}$$", ""},
                    {"Average Velocity", "Velocity", "$$v_{av} = {x_2 - x_1 \\over t_2 - t_1}$$", ""},
                    {"Acceleration", "Acceleration", "$$a = {\\Delta v \\over \\Delta t}$$", ""},
                    {"Average Acceleration","Acceleration", "$$a_{av} = {v_2 - v_1 \\over t_2 - t_1}$$", ""},
                    {"Free-fall Velocity","Free-fall", "$$v_y = u + g \\cdot t$$", ""},
                    {"Free-fall Displacement", "Free-fall", "$$y = {u \\cdot t + {1 \\over 2}g  \\cdot t^2}$$", ""},
                    {"Horizontal Distance", "Projectile Motion", "$$x = v_{x}t$$", ""},
                    {"Horizontal Velocity", "Projectile Motion", "$$v_x = v_{x1}$$", ""},
                    {"Vertical Distance",  "Projectile Motion", "$$y = v_{y1}t - {1 \\over 2}g \\cdot t^2$$", ""},
                    {"Vertical Velocity",  "Projectile Motion", "$$v_y = v_{y1} - g \\cdot t$$", ""},
                    {"Time of Flight", "Projectile Motion", "$$t = {2v_{1}sin \\Theta \\over g}$$", ""},
                    {"Maximum Height Reached", "Projectile Motion", "$$H = {v_{1}^{2}sin^{2} \\Theta \\over 2g}$$", ""},
                    {"Horizontal Range", "Projectile Motion", "$$R = {v_{1}^{2}sin2 \\Theta \\over g}$$", ""},
                    {"Friction", "Friction", "$$F_f = \\mu \\cdot F_n$$", ""},
                    {"Momentum", "Momentum and Impulse", "$$p = m \\cdot v$$", ""},
                    {"Impulse", "Momentum and Impulse", "$$F \\cdot t = m \\cdot \\Delta v$$", ""},
                    {"Conservation of Energy", "Law of Conservation of Energy", "$$mgh = {1 \\over 2}mv^2$$", ""},
                    {"Work", "Work", "$$W = \\vec{F} \\cdot \\vec{x}$$", ""},
                    {"Kinetic Energy", "Energy", "$$KE = {mv^2 \\over 2}$$", ""},
                    {"Gravitational Potential Energy", "Energy", "$$PE_g = m \\cdot g \\cdot h$$", ""},
                    {"Spring Potential Energy", "Energy", "$$PE_{sp} = {1 \\over 2}k \\cdot \\Delta x^2$$", ""},
                    {"Total Mechanical Energy", "Energy", "$$TME = KE + PE - 0$$", ""},
                    {"Average Power", "Power", "$$P_{av} = {\\Delta W \\over \\Delta t}$$", ""},
                    {"Instantaneous Power","Power", "$$P = F \\cdot cosa \\cdot v$$", ""},
                    {"Length of Arc",  "Uniform Circular Motion", "$$\\Delta S = r \\cdot \\Delta \\Theta$$", ""},
                    {"Tangential Velocity", "Uniform Circular Motion", "$$v = {\\Delta S \\over \\Delta t}$$", ""},
                    {"Velocity around a Circle", "Uniform Circular Motion", "$$v = 2 \\cdot \\pi \\cdot {r \\over t}$$", ""},
                    {"Angular Velocity", "Uniform Circular Motion", "$$\\omega = {\\Delta \\Theta \\over \\Delta t}$$", ""},
                    {"Centripetal Acceleration", "Uniform Circular Motion", "$$a_c = r \\cdot \\omega ^2$$", ""},
                    {"Centripetal Force","Centripetal and Centrifugal Forces", "$$F = {m \\cdot v^2 \\over r} $$", ""},
                    {"Moment of Inertia", "Rotational Motion", "$$l = m \\cdot r^2$$", ""},
                    {"Torque","Rotational Motion", "$$\\tau = r \\cdot F$$", ""},
                    {"Angular Momentum", "Rotational Motion", "$$L = l \\cdot \\omega$$", ""}
            };

            for(String[] s: formulas){
                Cursor c = database.rawQuery("SELECT " + DataContract.LessonEntry._ID +
                        " from lesson WHERE " + DataContract.LessonEntry.COLUMN_TITLE +
                        " = \"" + s[1] + "\"", null);
                c.moveToFirst();

                ContentValues values = new ContentValues();
                values.put(DataContract.FormulaEntry.COLUMN_NAME, s[0]);
                values.put(DataContract.FormulaEntry.COLUMN_LESSON_KEY,
                        c.getLong(c.getColumnIndex(DataContract.LessonEntry._ID)));
                values.put(DataContract.FormulaEntry.COLUMN_FORMULA, s[2]);

                database.insert(DataContract.FormulaEntry.TABLE_NAME, null, values);
            }
        }
    }

    private void initVariables(SQLiteDatabase database) {
        if(database.isOpen()) {
            String[][] formulas = {
                    {"Displacement", "Displacement", "", "", "", ""},
                    {"Displacement", "Average Acceleration", "", "", "", ""},
                    {"Displacement", "Initial Velocity","", "", "", ""},
                    {"Displacement", "Final Velocity", "", "", "", ""},

                    {"Speed", "Speed", "$$s = {d \\over t}$$", "d / t", "s", "\\(m \\over s \\)"},
                    {"Speed", "Distance", "$$d = {s \\cdot t}$$", "s * t", "d", "\\(m\\)"},
                    {"Speed", "Time", "$$t = {d \\over s}$$", "d / s", "t", "\\(s\\)"},

                    {"Velocity", "Velocity", "$$v = {x_f - x_i \\over t}$$", "(xf - xi) / t", "v", "\\(m \\over s\\)"},
                    {"Velocity", "Initial Position", "$$x_i = x_f - vt$$", "xf - (v * t)", "xi", "\\(m\\)"},
                    {"Velocity", "Final Position", "$$x_f = vt + x_i$$", "(v * t) + xi", "xf", "\\(m\\)"},
                    {"Velocity", "Time", "$$t = {x_f - x_i \\over v}$$", "(xf - xi) / v", "t", "\\(s\\)"},

                    {"Average Velocity", "Average Velocity", "$$v_{av} = {x_f - x_i \\over t_f - t_i}$$", "(xf - xi) / (tf - ti)", "v", "\\(m \\over s\\)"},
                    {"Average Velocity", "Initial Position", "$$x_i = x_f - v_{av}(t_f - t_i)$$", "xf - (v * (tf - ti))", "xi", "\\(m\\)"},
                    {"Average Velocity", "Final Position",  "$$x_f = v_{av}(t_f - t_i) + x_i$$", "(v * (tf - ti)) + xi", "xf", "\\(m\\)"},
                    {"Average Velocity", "Initial Time","$$t_i = t_f - (x_f - x_i \\over v_{av})$$", "tf - ((xf - xi) / v)", "ti", "\\(s\\)"},
                    {"Average Velocity", "Final Time", "$$t_f = (x_f - x_i \\over v_{av}) + t_i$$", "((xf - xi) / v) + ti", "tf", "\\(s\\)"},

                    {"Acceleration", "Acceleration", "$$a = {v_f - v_i \\over t}$$", "(vf - vi) / t", "a", "\\(m \\over s^2\\)"},
                    {"Acceleration", "Initial Velocity", "$$v_i = v_f - at$$", "vf - (a * t)", "vi", "\\(m \\over s\\)"},
                    {"Acceleration", "Final Velocity", "$$v_f = at + v_i$$", "(a * t) + vi", "vf", "\\(m \\over s\\)"},
                    {"Acceleration", "Time Interval", "$$t = {v_f - v_i \\over a}$$", "(vf - vi) / a", "t", "\\(s\\)"},

                    {"Average Acceleration", "Average Acceleration", "$$a_{av} = {v_f - v_i \\over t_f - t_i}$$", "(vf -vi) / (tf - ti)", "a", "\\(m \\over s^2\\)"},
                    {"Average Acceleration", "Initial Velocity", "$$v_i = v_f - a_{av}(t_f - t_i)$$", "vf - (a * (tf - ti))", "vi", "\\(m \\over s\\)"},
                    {"Average Acceleration", "Final Velocity", "$$v_f = a_{av}(t_f - t_i) + v_i$$", "(a * (tf - ti)) + vi\", \"xf", "vf", "\\(m \\over s\\)"},
                    {"Average Acceleration", "Initial Time", "$$t_i = t_f - (v_f - v_i \\over a_{av})$$", "tf - ((vf - vi) / a)", "ti", "\\(s\\)"},
                    {"Average Acceleration", "Final Time", "$$$$t_f = (v_f - v_i \\over a_{av}) + t_i$$", "((vf - vi) / a) + ti", "tf", "\\(s\\)"},

                    {"Free-fall Velocity", "Velocity", "$$v_y = u + g \\cdot t$$", "u + (g * t)", "v", "\\(m \\over s\\)"},
                    {"Free-fall Velocity", "Initial Velocity", "$$u = v - g \\cdot t$$", "v - (g / t)", "u", "\\(m \\over s\\)"},
                    {"Free-fall Velocity", "Acceleration due to Gravity", "$$g = {v - u \\over t}$$", "v - (u / t)", "a", "\\(m \\over s^2\\)"},
                    {"Free-fall Velocity", "Time", "$$t = {v - u \\over g}$$", "v - (u / g)", "t", "\\(s\\)"},

                    {"Free-fall Displacement", "Displacement", "$$y = {1 \\over 2}g  \\cdot t^2$$", "(g / 2) * t^2", "y", "\\(m\\)"},
                    {"Free-fall Displacement", "Acceleration due to Gravity", "$$g = 2{y \\ t^2}$$", "2 * (y / t^2)", "g", "\\(m \\over s^2\\)"},
                    {"Free-fall Displacement", "Time", "$$t = \\sqrt{2{y \\over g}}$$", "sqrt(2 * (y / g))", "t", "\\(s\\)"},

                    {"Horizontal Distance", "Horizontal Distance","$$x = v_{x}t$$", "v * t", "x", "\\(m\\)"},
                    {"Horizontal Distance", "Velocity along the x-axis","$$v_x = {x \\over t}$$", "x / t", "v", "\\(m \\over s\\)"},
                    {"Horizontal Distance", "Time","$$t = { x \\over v_x}$$", "x / v", "t", "\\(s\\)"},

                    {"Horizontal Velocity", "Velocity along the x-axis", "$$v_x = v_{xi}$$", "vi", "v", "\\(m \\over s\\)"},
                    {"Horizontal Velocity", "Initial Velocity along the x-axis", "$$$$", "v", "vi", "\\(m \\over s\\)"},

                    {"Vertical Distance", "Vertical Distance", "$$y = v_{yi}t - {1 \\over 2}g \\cdot t^2$$", "", "y", "\\(m\\)"},
                    {"Vertical Distance", "Initial Velocity along the y-axis", "$$TEXT NA NAKA CAPS$$", "", "vi", "\\(m \\over s\\)"},
                    {"Vertical Distance", "Time","$$PARA MAKITA NATIN$$", "", "t", "\\(s\\)"},
                    {"Vertical Distance", "Acceleration due to Gravity","$$IF EVER$$", "", "g", "\\(m \\over s^2\\)"},

                    {"Vertical Velocity", "Vertical Velocity","$$v_y = v_{yi} - g \\cdot t$$", "vi - (g * t)", "v", "\\(m \\over s\\)"},
                    {"Vertical Velocity", "Initial Velocity along the y-axis", "$$v_{yi} = v_y + g \\cdot t$$", "v + (g * t)", "vi", "\\(m \\over s\\)"},
                    {"Vertical Velocity", "Time","$$g = {v_{yi} - v_y \\over t}$$", "(vi - v) / g", "t", "\\(s\\)"},
                    {"Vertical Velocity", "Acceleration due to Gravity","$$t = {v_{yi} - v_y \\over g}$$", "(vi - v) / t", "g", "\\(m \\over s^2\\)"},

                    {"Time of Flight", "Time of Flight","$$t = {2v_{1}sin \\Theta \\over g}$$", "((2 * v) * sin(a)) / g", "t", "\\(s\\)"},
                    {"Time of Flight", "Initial Velocity","$$g = {2v_{1}sin \\Theta \\over t}$$", "((2 * v) * sin(a)) / t", "v", "\\(m \\over s\\)"},
                    {"Time of Flight", "Angle of Trajectory","\\Theta = sin^{-1} {gt \\ 2v}", "asin((g * t) / (2 * v))", "a", "\\(\\Theta \\)"},
                    {"Time of Flight", "Acceleration due to Gravity","$$$$v_{yi} = {gt \\over 2sin \\Theta}$$$$", "(t * g) / (2 * sin(a))", "g", "\\(m \\over s^2\\)"},

                    {"Maximum Height Reached", "Maximum Height Reached","$$H = {v_{1}^{2}sin^{2} \\Theta \\over 2g}$$", "(v^2 * sin(a) * sin(a)) / 2 * g", "H", "\\(m\\)"},
                    {"Maximum Height Reached", "Initial Velocity",  "$$v_{1} = \\sqrt{2gH \\over sin^2 \\Theta}$$", "sqrt((2 * g *H) / (sin(a) * sin(a)))", "v", "\\(m \\over s\\)"},
                    {"Maximum Height Reached", "Angle of Trajectory", "$$\\Theta = sin^{2-1}{2gH \\over v_{1}^{2}}$$", "asin(sqrt((2 * g * H) / (v^2)))", "a", "\\(\\Theta \\)"},
                    {"Maximum Height Reached", "Acceleration due to Gravity", "$$g = {v_{1}^{2}sin^{2} \\Theta \\over 2H}$$", "(v^2 * sin(a) * sin(a)) / 2 * H", "g", "\\(m \\over s^2\\)"},

                    {"Horizontal Range", "Horizontal Range","$$R = {v_{1}^{2}sin2 \\Theta \\over g}$$", "(v^2 * sin(2 * a)) / g", "R", "\\(m\\)"},
                    {"Horizontal Range", "Initial Velocity","$$v_{1} = \\sqrt{gR \\over sin2 \\Theta}$$", "sqrt((g * R) / (sin(2 * a)))", "v", "\\(m \\over s\\)"},
                    {"Horizontal Range", "Time of Flight", "$$\\Theta = sin^{-1}{gR \\over 2v_{1}^{2}}$$", "asin((g * R) / (2 * v))", "a", "\\(\\Theta \\)"},
                    {"Horizontal Range", "Acceleration due to Gravity", "$$g = {v_{1}^{2}sin2 \\Theta \\over R$$", "(v^2 * sin(2 * a)) / R", "g", "\\(m \\over s^2\\)"},

                    {"Friction", "Frictional Force", "$$F_f = \\mu \\cdot F_n$$", "m * N", "F", "\\(N\\)"},
                    {"Friction", "Coefficient of Friction", "$$\\mu = {F_f \\over F_n}$$", "F / N", "m", "\\(\\)"},
                    {"Friction", "Normal Force", "$$F_n = {F_f \\over \\mu}$$", "f / m", "N", "\\(N\\)"},

                    {"Momentum", "Momentum","$$p = m \\cdot v$$", "m * v", "p", "\\(kg \\cdot m \\over s\\)"},
                    {"Momentum", "Mass", "$$m = {p \\over v}$$", "p / v", "m", "\\(kg\\)"},
                    {"Momentum", "Velocity", "$$v = {p \\over m}$$", "p / m", "v", "\\(m \\over s\\)"},

                    {"Work", "Work", "$$W = \\vec{F} \\cdot \\vec{x}$$", "", "", "\\(\\)"},
                    {"Work", "Force", "$$$$", "", "", "\\(\\)"},
                    {"Work", "Distance", "$$$$", "", "", "\\(\\)"},

                    {"Kinetic Energy", "Kinetic Energy", "$$KE = {mv^2 \\over 2}$$", "", "", "\\(\\)"},
                    {"Kinetic Energy", "Mass", "$$$$", "", "", "\\(\\)"},
                    {"Kinetic Energy", "Velocity", "$$$$", "", "", "\\(\\)"},

                    {"Gravitational Potential Energy", "Potential Energy",  "$$PE_g = m \\cdot g \\cdot h$$", "", "", "\\(\\)"},
                    {"Gravitational Potential Energy", "Height", "$$$$", "", "", "\\(\\)"},
                    {"Gravitational Potential Energy", "Acceleration due to Gravity", "$$$$", "", "", "\\(\\)"},

                    {"Spring Potential Energy", "Potential Energy", "$$PE_{sp} = {1 \\over 2}k \\cdot \\Delta x^2$$", "", "", "\\(\\)"},
                    {"Spring Potential Energy", "Spring Constant", "$$$$", "", "", "\\(\\)"},
                    {"Spring Potential Energy", "Spring Displacement", "$$$$", "", "", "\\(\\)"},

                    {"Total Mechanical Energy", "Total Mechanical Energy", "$$TME = KE + PE - 0$$", "", "", "\\(\\)"},
                    {"Total Mechanical Energy", "Kinetic Energy", "$$$$", "", "", "\\(\\)"},
                    {"Total Mechanical Energy", "Potential Energy", "$$$$", "", "", "\\(\\)"},

                    {"Average Power", "Average Power", "$$P_{av} = {\\Delta W \\over \\Delta t}$$", "", "", "\\(\\)"},
                    {"Average Power", "Amount of Work done", "$$$$", "", "", "\\(\\)"},
                    {"Average Power", "Time", "$$$$", "", "", "\\(\\)"},

                    {"Instantaneous Power", "Force",  "$$P = F \\cdot cosa \\cdot v$$", "", "", "\\(\\)"},
                    {"Instantaneous Power", "Angle", "$$$$", "", "", "\\(\\)"},
                    {"Instantaneous Power", "Velocity", "$$$$", "", "", "\\(\\)"},

                    {"Length of Arc", "Length of Arc", "$$\\Delta S = r \\cdot \\Delta \\Theta$$", "", "", "\\(\\)"},
                    {"Length of Arc", "Radius", "$$$$", "", "", "\\(\\)"},
                    {"Length of Arc", "Angle",  "$$$$", "", "", "\\(\\)"},

                    {"Tangential Velocity", "Tangential Velocity", "$$v = {\\Delta S \\over \\Delta t}$$", "", "", "\\(\\)"},
                    {"Tangential Velocity", "Length of Arc","$$$$", "", "", "\\(\\)"},
                    {"Tangential Velocity", "Time",  "$$$$", "", "", "\\(\\)"},

                    {"Velocity around a Circle", "Velocity around a Circle", "$$v = 2 \\cdot \\pi \\cdot {r \\over t}$$", "", "", "\\(\\)"},
                    {"Velocity around a Circle", "Radius", "$$$$", "", "", "\\(\\)"},
                    {"Velocity around a Circle", "Time", "$$$$", "", "", "\\(\\)"},

                    {"Angular Velocity", "Angular Velocity", "$$\\omega = {\\Delta \\Theta \\over \\Delta t}$$", "", "", "\\(\\)"},
                    {"Angular Velocity", "Angel", "$$$$", "", "", "\\(\\)"},
                    {"Angular Velocity", "Time", "$$$$", "", "", "\\(\\)"},

                    {"Centripetal Acceleration", "Centripetal Acceleration", "$$a_c = r \\cdot \\omega ^2$$", "", "", "\\(\\)"},
                    {"Centripetal Acceleration", "Angular Velocity", "$$$$", "", "", "\\(\\)"},
                    {"Centripetal Acceleration", "Angle", "$$$$", "", "", "\\(\\)"},

                    {"Centripetal Force", "Centripetal Force", "$$F = {m \\cdot v^2 \\over r} $$", "", "", "\\(\\)"},
                    {"Centripetal Force", "Mass", "$$$$", "", "", "\\(\\)"},
                    {"Centripetal Force", "Tangential Velocity", "$$$$", "", "", "\\(\\)"},
                    {"Centripetal Force", "Radius","$$$$", "", "", "\\(\\)"},

                    {"Moment of Inertia", "Moment of Inertia", "$$l = m \\cdot r^2$$", "", "", "\\(\\)"},
                    {"Moment of Inertia", "Mass", "$$$$", "", "", "\\(\\)"},
                    {"Moment of Inertia", "Distance","$$$$", "", "", "\\(\\)"},

                    {"Torque", "Torque", "$$\\tau = r \\cdot F$$", "", "", "\\(\\)"},
                    {"Torque", "Force", "$$$$", "", "", "\\(\\)"},
                    {"Torque", "Position",  "$$$$", "", "", "\\(\\)"},

                    {"Angular Momentum", "Angular Momentum", "$$L = l \\cdot \\omega$$", "", "", "\\(\\)"},
                    {"Angular Momentum", "Moment of Inertia", "$$$$", "", "", "\\(\\)"},
                    {"Angular Momentum", "Angular Velocity", "$$$$", "", "", "\\(\\)"}
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
                values.put(DataContract.VariableEntry.COLUMN_FORMULA_DISPLAY, s[2]);
                values.put(DataContract.VariableEntry.COLUMN_FORMULA_COMPUTE, s[3]);
                values.put(DataContract.VariableEntry.COLUMN_SYMBOL, s[4]);
                values.put(DataContract.VariableEntry.COLUMN_UNIT, s[5]);

                database.insert(DataContract.VariableEntry.TABLE_NAME, null, values);
            }
        }
    }
}







