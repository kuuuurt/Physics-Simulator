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
                DataContract.ChapterEntry.COLUMN_HAS_CALCULATOR + " INTEGER NOT NULL, " +
                DataContract.ChapterEntry.COLUMN_LOGO + " TEXT" + ");";

        final String SQL_CREATE_LESSON_TABLE = "CREATE TABLE " +
                DataContract.LessonEntry.TABLE_NAME + " (" +
                DataContract.LessonEntry._ID + " INTEGER PRIMARY KEY, " +
                DataContract.LessonEntry.COLUMN_NAME + " TEXT UNIQUE NOT NULL, " +
                DataContract.LessonEntry.COLUMN_CHAPTER_KEY + " INTEGER NOT NULL," +
                DataContract.LessonEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL," +
                DataContract.LessonEntry.COLUMN_HAS_CALCULATOR + " INTEGER NOT NULL," +
                DataContract.LessonEntry.COLUMN_HAS_SIMULATION + " INTEGER NOT NULL," +
                DataContract.LessonEntry.COLUMN_VIDEO_ID + " TEXT NOT NULL," +
                DataContract.LessonEntry.COLUMN_AUDIO + " TEXT," +
                DataContract.LessonEntry.COLUMN_LOGO + " TEXT NOT NULL" +");";

        final String SQL_CREATE_SECTION_TABLE = "CREATE TABLE " +
                DataContract.SectionEntry.TABLE_NAME + " (" +
                DataContract.SectionEntry._ID + " INTEGER PRIMARY KEY, " +
                DataContract.SectionEntry.COLUMN_NAME + " TEXT UNIQUE NOT NULL, " +
                DataContract.SectionEntry.COLUMN_CONTENT + " TEXT, " +
                DataContract.SectionEntry.COLUMN_LESSON_KEY + " INTEGER NOT NULL" + ");";

        final String SQL_CREATE_IMAGE_TABLE = "CREATE TABLE " +
                DataContract.ImageEntry.TABLE_NAME + " (" +
                DataContract.ImageEntry._ID + " INTEGER PRIMARY KEY, " +
                DataContract.ImageEntry.COLUMN_RESOURCE_NAME + " TEXT NOT NULL, " +
                DataContract.ImageEntry.COLUMN_CAPTION + " TEXT NOT NULL, " +
                DataContract.ImageEntry.COLUMN_SECTION_KEY + " INTEGER NOT NULL" + ")";

        final String SQL_CREATE_CONSTANT_TABLE = "CREATE TABLE " +
                DataContract.ConstantEntry.TABLE_NAME + " (" +
                DataContract.ConstantEntry._ID + " INTEGER PRIMARY KEY, " +
                DataContract.ConstantEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                DataContract.ConstantEntry.COLUMN_DESC + " TEXT NOT NULL, " +
                DataContract.ConstantEntry.COLUMN_SYMBOL + " TEXT NOT NULL, " +
                DataContract.ConstantEntry.COLUMN_DEFAULT + " REAL NOT NULL, " +
                DataContract.ConstantEntry.COLUMN_CURRENT + " REAL NOT NULL);";

        final String SQL_CREATE_FORMULA_TABLE = "CREATE TABLE " +
                DataContract.FormulaEntry.TABLE_NAME + " (" +
                DataContract.FormulaEntry._ID + " INTEGER PRIMARY KEY, " +
                DataContract.FormulaEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                DataContract.FormulaEntry.COLUMN_LESSON_KEY + " INTEGER NOT NULL, " +
                DataContract.FormulaEntry.COLUMN_FORMULA + " TEXT" + ")";

        final String SQL_CREATE_FORMULA_CONSTANT_TABLE = "CREATE TABLE " +
                DataContract.FormulaConstantEntry.TABLE_NAME + " (" +
                DataContract.FormulaConstantEntry._ID + " INTEGER PRIMARY KEY, " +
                DataContract.FormulaConstantEntry.COLUMN_CONSTANT_KEY + " INTEGER NOT NULL, " +
                DataContract.FormulaConstantEntry.COLUMN_FORMULA_KEY + " INTEGER NOT NULL " + ")";

        final String SQL_CREATE_VARIABLE_TABLE = "CREATE TABLE " +
                DataContract.VariableEntry.TABLE_NAME + " (" +
                DataContract.VariableEntry._ID + " INTEGER PRIMARY KEY, " +
                DataContract.VariableEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                DataContract.VariableEntry.COLUMN_FORMULA_KEY + " INTEGER NOT NULL, " +
                DataContract.VariableEntry.COLUMN_CONSTANT_KEY + " INTEGER, " +
                DataContract.VariableEntry.COLUMN_UNIT + " TEXT, " +
                DataContract.VariableEntry.COLUMN_UNIT_TYPE + " TEXT, " +
                DataContract.VariableEntry.COLUMN_SYMBOL_COMPUTE + " TEXT, " +
                DataContract.VariableEntry.COLUMN_SYMBOL_DISPLAY + " TEXT, " +
                DataContract.VariableEntry.COLUMN_FORMULA_COMPUTE + " TEXT, " +
                DataContract.VariableEntry.COLUMN_FORMULA_DISPLAY + " TEXT);";

        final String SQL_CREATE_EXAMPLE_TABLE = "CREATE TABLE " +
                DataContract.ExampleEntry.TABLE_NAME + " (" +
                DataContract.ExampleEntry._ID + " INTEGER PRIMARY KEY, " +
                DataContract.ExampleEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                DataContract.ExampleEntry.COLUMN_SECTION_KEY + " INTEGER NOT NULL, " +
                DataContract.ExampleEntry.COLUMN_CONTENT + " INTEGER" + " );";


        sqLiteDatabase.execSQL(SQL_CREATE_CHAPTER_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_LESSON_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_SECTION_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_IMAGE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_CONSTANT_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_FORMULA_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_FORMULA_CONSTANT_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_VARIABLE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_EXAMPLE_TABLE);
        initChapters(sqLiteDatabase);
        initLessons(sqLiteDatabase);
        initSections(sqLiteDatabase);
        initImages(sqLiteDatabase);
        initFormulas(sqLiteDatabase);
        initConstants(sqLiteDatabase);
        initFormulaConstants(sqLiteDatabase);
        initVariables(sqLiteDatabase);
        initExamples(sqLiteDatabase);
    }




    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DataContract.ChapterEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DataContract.LessonEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DataContract.FormulaEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DataContract.ConstantEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DataContract.VariableEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DataContract.FormulaConstantEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DataContract.SectionEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DataContract.ImageEntry.TABLE_NAME);

        onCreate(sqLiteDatabase);
    }

    private void initChapters(SQLiteDatabase database){
        if(database.isOpen()){
            String[][] chapters = {
                    {"Introduction to Physics","Physics, Conversion of Units","ic_chapter_introduction_to_physics", "0"},
                    {"One-dimensional Motion","Scalar and Vector Values, Velocity, Acceleration, Free fall","ic_chapter_one_dimensional_motion", "1"},
                    {"Two-dimensional Motion","Projectile Motion","ic_chapter_two_dimensional_motion", "1"},
                    {"Isaac Newton's Laws of Motion","Friction, Free Body Diagrams","ic_chapter_newtons_laws", "1"},
                    {"Work, Energy, and Power","Work, Energy, Power","ic_chapter_work_energy_power", "1"},
                    {"Momentum and Impulse","Momentum and Impulse","ic_chapter_momentum_impulse", "1"},
                    {"Uniform Circular Motion","Uniform Circular Motion, Centripetal and Centrifugal Forces, Rotational Motion","ic_chapter_uniform_circular_motion", "1"}
            };
            for(String[] s : chapters){
                ContentValues values = new ContentValues();
                values.put(DataContract.ChapterEntry.COLUMN_NAME, s[0]);
                values.put(DataContract.ChapterEntry.COLUMN_DESCRIPTION, s[1]);
                values.put(DataContract.ChapterEntry.COLUMN_LOGO, s[2]);
                values.put(DataContract.ChapterEntry.COLUMN_HAS_CALCULATOR, s[3]);

                database.insert(DataContract.ChapterEntry.TABLE_NAME, null, values);
            }

        }
    }

    private void initLessons(SQLiteDatabase database){
        if(database.isOpen()){
            String[][] lessons = {
                    {"Physics", "Introduction to Physics",
                            "Definition, Physics",
                            "ic_lesson_introduction_to_physics", "0", "0", "uIojjqSm0m4", "audio_lesson_introduction_to_physics"},
                    {"Conversion of Units", "Introduction to Physics",
                            "Definition, Conversion of Units, Metric System, Units of Time, Units of Angles",
                            "ic_lesson_conversion_of_units", "0", "0", "7lnsGINMK7I", "audio_lesson_unit_conversion"},
                    {"Scalar and Vector Values", "One-dimensional Motion",
                            "Definition, Distance and Displacement",
                            "ic_lesson_scalar_and_vector_quantities", "1", "0", "ihNZlp7iUHE", "audio_lesson_scalar_and_vector_values"},
                    {"Velocity", "One-dimensional Motion",
                            "Definition, Speed and Velocity, Average Velocity, Instantaneous " +
                                    "Velocity",
                            "ic_lesson_velocity", "1", "1", "oRKxmXwLvUU", "audio_lesson_speed_and_velocity"},
                    {"Acceleration", "One-dimensional Motion", "Definition, Acceleration, Average " +
                            "Acceleration, Instantaneous Acceleration",
                            "ic_lesson_acceleration", "1", "1", "FOkQszg1-j8", "audio_lesson_acceleration"},
                    {"Free-fall", "One-dimensional Motion", "Definition, Free fall",
                            "ic_lesson_free_fall", "1", "1", "6wEEa8-RSqU", "audio_lesson_free_fall"},
                    {"Projectile Motion", "Two-dimensional Motion", "Definition, " +
                            "Projectile Motion",
                            "ic_lesson_projectile_motion", "1", "1","rMVBc8cE5GU", "audio_lesson_projectile_motion"},
                    {"Newton's Laws of Motion", "Isaac Newton's Laws of Motion", "Newton's Laws of Motion",
                            "ic_lesson_newtons_laws", "0", "0", "kgLeZuQkyuo", "audio_lesson_newtons_laws"}, //asdf
                    {"Friction", "Isaac Newton's Laws of Motion", "Definition, Two types of " +
                            "Friction",
                            "ic_lesson_friction", "1", "1", "fo_pmp5rtzo", "audio_lesson_friction"},
                    {"Free-body Diagrams", "Isaac Newton's Laws of Motion", "Definition, Free " +
                            "Body Diagrams",
                            "ic_lesson_free_body_diagrams", "0", "0", "nDis6HbXxjg", "audio_lesson_free_body_diagrams"},
                    {"Momentum and Impulse", "Momentum and Impulse", "Definition, Momentum, " +
                            "Impulse",
                            "ic_lesson_momentum_impulse", "1", "1", "XFhntPxow0U","audio_lesson_momentum_and_impulse"},
                    {"Law of Conservation of Energy", "Momentum and Impulse", "Definition, " +
                            "Conservation of Energy",
                            "ic_lesson_laws_of_conservation_of_energy", "0", "0", "PplaBASQ_3M", "audio_lesson_laws_of_conservation_of_energy"},
                    {"Work", "Work, Energy, and Power", "Definition, Work",
                            "ic_lesson_work", "1", "1", "w4QFJb9a8vo", "audio_lesson_work"}, //asdf
                    {"Energy", "Work, Energy, and Power", "Definition, Kinetic Energy, " +
                            "Potential Energy, Total Mechanical Energy",
                            "ic_lesson_energy", "1", "1", "w4QFJb9a8vo", "audio_lesson_energy"}, //asdf
                    {"Power", "Work, Energy, and Power", "Definition, Average Power",
                            "ic_lesson_power", "1", "1", "w4QFJb9a8vo", "audio_lesson_power"}, //asdf
                    {"Uniform Circular Motion", "Uniform Circular Motion", "Definition, " +
                            "Measurements of a Circle, Frequency, Angular Displacement, " +
                            "Length of Arc, Tangential Velocity, Angular Velocity, " +
                            "Centripetal Acceleration",
                            "ic_lesson_uniform_circular_motion", "1", "1", "bpFK2VCRHUs", "audio_lesson_uniform_circular_motion"},
                    {"Centripetal and Centrifugal Forces", "Uniform Circular Motion",
                            "Definition, Centripetal and Centrifugal Forces",
                            "ic_lesson_centripetal_and_centrifugal_force", "1", "1", "9s1IRJbL2Co", "audio_lesson_centripetal_and_centrifugal_forces"},
                    {"Rotational Motion", "Uniform Circular Motion", "Definition, Moment of " +
                            "Torque",
                            "ic_lesson_rotational_motion", "1", "0", "fmXFWi", "audio_lesson_rotational_motion"}
            };

            for(String[] s : lessons){
                Cursor c = database.query(
                        DataContract.ChapterEntry.TABLE_NAME,
                        new String[]{DataContract.ChapterEntry._ID},
                        DataContract.ChapterEntry.COLUMN_NAME + " = ?",
                        new String[]{s[1]},
                        null,
                        null,
                        null
                );
                c.moveToFirst();

                ContentValues values = new ContentValues();
                values.put(DataContract.LessonEntry.COLUMN_NAME, s[0]);
                values.put(DataContract.LessonEntry.COLUMN_CHAPTER_KEY,
                        c.getLong(c.getColumnIndex(DataContract.ChapterEntry._ID)));
                values.put(DataContract.LessonEntry.COLUMN_DESCRIPTION, s[2]);
                values.put(DataContract.LessonEntry.COLUMN_LOGO, s[3]);
                values.put(DataContract.LessonEntry.COLUMN_HAS_CALCULATOR, s[4]);
                values.put(DataContract.LessonEntry.COLUMN_HAS_SIMULATION, s[5]);
                values.put(DataContract.LessonEntry.COLUMN_VIDEO_ID, s[6]);
                values.put(DataContract.LessonEntry.COLUMN_AUDIO, s[7]);

                c.close();

                database.insert(DataContract.LessonEntry.TABLE_NAME, null, values);
            }
        }
    }

    private void initSections(SQLiteDatabase database){
        if(database.isOpen()){
            String[][] sections = {
                    {"Physics","Physics Definition",
                            "<h2><b>Definition</b></h2>" +
                                    "Physics: <br/>" +
                                    "\t •\tis the natural science that involves the study of matter and its motion through space and time<br/>"},
                    {"Physics","Physics",
                            "<h2><b>Physics</b></h2>" +
                                    "Before studying the basics of Physics, the person must be knowledgeable of the following subjects: Algebra, Geometry and Trigonometry. <br/><br/>" +
                                    "There are a lot of topics under Physics, but not all will be discussed in this application. The following topics are the only ones that are included in this " +
                                    "application: Conversion of Units, Scalar and Vector Quantities, Speed and Velocity, Acceleration, Free-fall, Projectile Motion, Newton’s Laws, Friction, " +
                                    "Free-body Diagrams, Work, Energy, Power, Momentum and Impulse, Law of Conservation of Energy, Uniform Circular Motion, Centripetal and Centrifugal Forces, and " +
                                    "Rotational Motion. <br/><br/>" +
                                    "Other topics under Physics includes: Oscillation and Mechanical Waves, Fluids, Thermodynamics, Circuits, Magnetic Forces, Electromagnetic Waves, and others."},
                    {"Physics", "Physics References", "References:<br> Introduction to the Language of Kinematics (2016). Retrieved from http://www.physicsclassroom.com/class/1DKin/Lesson-1/Introduction<br><br>\n" +
                            "Carasco, J. (2016). Introduction to Physics. Retrieved from http://www.introduction-to-physics.com<br>\n"},
                    {"Conversion of Units","Conversion of Units Definition",
                            "<h2><b>Definition</b></h2>" +
                                    "Units: <br/>" +
                                    "\t •\tare standards for measurement of physical quantities that need clear definitions to be useful<br/>" +
                                    "Conversion: <br/>" +
                                    "\t •\tthe act or an instance of converting or the process of being converted<br/>" +
                                    "Unit Conversion: <br/>" +
                                    "\t •\tis a multi-step process that involves multiplication or division by a numerical factor<br/>"},
                    {"Conversion of Units","Conversion of Units",
                            "<h2><b>Conversion of Units</b></h2>" +
                                    "The best way in unit conversion is by multiplying the given unit by a conversion factor which cancels out the unwanted units and replaces it with the desired " +
                                    "unit. <br/><br/>" +
                                    "For example, to convert 4 hours to seconds you first convert it to minutes, and then convert the minutes into seconds. <br/><br/>\n" +
                                    "There are 60 minutes in one hour, hence: " +
                                    "$$\\require{cancel} 4 \\cancel{hr} \\cdot {{60 min} \\over {1 \\cancel{hr}}} = 240 min$$ " +
                                    "And, 60 seconds in one minute, hence:\n" +
                                    "$$240 \\cancel{min} \\cdot {{60 sec} \\over {1 \\cancel{min}}} = 14400 sec$$" +
                                    "In converting hours to minutes, you see that \\(hr\\) is both in the numerator and denominator, so the \\(hr\\) cancels out, leaving you with \\(min\\). Same goes with converting minutes to seconds where \\(min\\) cancels out and \\(sec\\) remains. <br><br>" +
                                    "When converting two units a time, you use two conversion factors. For example, to convert 50 kilometers per hour \\(km \\over hr\\) to meters per second " +
                                    "\\(m \\over s\\) you convert kilometers \\(km\\) to meters \\(m\\) and hours \\(hr\\) to seconds \\(s\\):<br/><br/>" +
                                    "<i>Note: There are 1000 meters in 1 kilometer</i><br/><br/>" +
                                    "$$50 {{\\cancel{km}} \\over {1 \\cancel{hr}}} \\cdot {{1000 m} \\over {1 \\cancel{km}}} \\cdot {{1 \\cancel{hr}} \\over {60 \\cancel{min}}} \\cdot {{1 " +
                                    "\\cancel{min}} \\over {60 sec}} = 13.8888{m \\over s}$$<br/>"},
                    {"Conversion of Units","Metric System and Conversion",
                            "<h2><b>Metric System and Conversion</b></h2>" +
                                    "The following is the Metric System along with its multiplier for conversion:<br/>" +
                                    "\t•\t exa \\(E = 10^{18}\\) or \\(1,000,000,000,000,000,000\\)<br/>" +
                                    "\t•\t peta \\(P = 10^{15}\\) or \\(1,000,000,000,000,000\\)<br/>" +
                                    "\t•\t tera \\(T = 10^{12}\\) or \\(1,000,000,000,000\\)<br/>" +
                                    "\t•\t giga \\(G = 10^{9}\\) or \\(1,000,000,000\\)<br/>" +
                                    "\t•\t mega \\(M = 10^{6}\\) or \\(1,000,000\\)<br/>" +
                                    "\t•\t kilo \\(k = 10^{3}\\) or \\(1, 000\\)<br/>" +
                                    "\t•\t hecto \\(h = 10^{2}\\) or \\(100\\)<br/>" +
                                    "\t•\t deca \\(da = 10^{1}\\) or \\(10\\)<br/>" +
                                    "\t•\t deci \\(d = 10^{-1}\\) or \\(0.1\\)<br/>" +
                                    "\t•\t centi \\(c = 10^{-2}\\) or \\(0.01\\)<br/>" +
                                    "\t•\t milli \\(m = 10^{-3}\\) or \\(0.001\\)<br/>" +
                                    "\t•\t micro \\(\\mu = 10^{-6}\\) or \\(0.000001\\)<br/>" +
                                    "\t•\t nano \\(n = 10^{-9}\\) or \\(0.000000001\\)<br/>" +
                                    "\t•\t pico \\(p = 10^{-12}\\) or \\(0.000000000001\\)<br/>" +
                                    "\t•\t femto \\(f = 10^{-15}\\) or \\(0.000000000000001\\)<br/>" +
                                    "\t•\t atto \\(a = 10^{-18}\\) or \\(0.000000000000000001\\)<br/>"},
                    {"Conversion of Units","Units of Time and Conversion",
                            "<h2><b>Units of Time and Conversion</b></h2>" +
                                    "The following are the units of time along with its conversion to other units of time: <br/>\n" +
                                    "\t•\t \\(1 minute = 60 seconds\\)<br/>" +
                                    "\t•\t \\(1 hour = 60 minutes\\)<br/>" +
                                    "\t•\t \\(1 day = 24hours\\)<br/>" +
                                    "\t•\t \\(1 week = 7 days\\)<br/>" +
                                    "\t•\t \\(1 year = 52 weeks\\)<br/>" +
                                    "\t•\t \\(1 year = 12 months\\)<br/>" +
                                    "\t•\t \\(1 year = 365 days\\)<br/>"},
                    {"Conversion of Units","Units of Angles and Conversion",
                            "<h2><b>Units of Angles and Conversion</b></h2>" +
                                    "There are two units for angles namely, degrees and radians. The following are the conversion of degrees to radians and radians to degree: <br/>" +
                                    "To convert degrees to radians, just multiply the degrees by \\(\\pi \\over 180\\).<br/>" +
                                    "To convert radians to degrees, just multiply the radians by \\(180 \\over \\pi\\). "},
                    {"Conversion of Units", "Conversion of Units References", "References:<br> Metric Units (2016). Retrieved from http://www.ck12.org/physics/Metric-Units/lesson/Metric-Units-PPC<br><br>\n" +
                            "Unit Conversion (2016). Retrieved from http://www.ck12.org/physics/Unit-Conversions/lesson/Unit-Conversions-PPC<br>"},
                    {"Scalar and Vector Values","Distance and Displacement",
                            "<h2><b>Distance and Displacement</b></h2>" +
                                    "Distance:<br/>" +
                                    "\t•\tis a scalar quantity<br/>" +
                                    "\t•\tmeasures the interval between two points that is measured along the actual path that was made that connects them<br/><br/>" +
                                    "Displacement:<br/>" +
                                    "\t•\tis a vector quantity<br/>" +
                                    "\t•\tmeasures the interval between two points along the shortest path that connects them <br/>"},
                    {"Scalar and Vector Values","Scalar and Vector Values Formula",
                            "The SI unit used for distance and displacement is meters (\\(m\\)).<br/><br/>" +
                                    "Displacement can be calculated using this formula:<br/>" +
                                    "$$d = {x_f - x_i}$$" +
                                    "Where:<br/>" +
                                    "\\(x_i\\) = initial position of the object<br/>" +
                                    "\\(x_f\\) = final position of the object<br/>"},
                    {"Scalar and Vector Values", "Scalar and Vector Values References", "References:<br> Paul Cotarlea (2014). Learn Physics (Version 1.0.6) [Mobile Application Software]. Retrieved from https://play.google.com/store/apps<br><br>\n" +
                            "Geckonization (2016). Pocket Physics (Version 3.0.92) [Mobile Application Software]. Retrieved from https://play.google.com/store/apps<br><br>\n" +
                            "Scalar and Vectors (2016). Retrieved from http://www.physicsclassroom.com/class/1DKin/Lesson-1/Scalars-and-Vectors<br><br>\n" +
                            "Distance and Displacement (2016). Retrieved from http://www.physicsclassroom.com/class/1DKin/Lesson-1/Distance-and-Displacement <br>\n"},
                    {"Velocity", "Velocity Definition",
                            "<h2><b>Definition</b></h2>" +
                                    "Speed:<br/>" +
                                    "\t•\tshows the rate at which an object is able to move<br/><br/>" +
                                    "Velocity:<br/>" +
                                    "\t•\tshows the rate at which an object is able to move in a given direction<br/><br/>" +
                                    "The SI unit used for speed and velocity is meter per second (\\({m \\over s}\\)).<br/>"},
                    {"Velocity", "Speed and Velocity",
                            "<h2><b>Speed and Velocity</b></h2>" +
                                    "Speed can be calculated by dividing distance over time:<br/>" +
                                    "$$s = {d \\over t}$$" +
                                    "Velocity can be calculated by dividing displacement over time:<br/>" +
                                    "$$v = {x_f - x_i \\over t}$$"},
                    {"Velocity", "Average Velocity",
                            "<h2><b>Average Velocity</b></h2>" +
                                    "Average velocity is the ratio of total displacement (\\(\\Delta x\\)) taken over time interval (\\(\\Delta t\\))<br/>|" +
                                    "The average velocity from when the object starts to move up to the time when the object stops can be described as:<br/>" +
                                    "$$v_{av} = {x_f - x_i \\over t_f - t_i}$$" +
                                    "Where:<br/>" +
                                    "\\(\\Delta x\\) = change in velocity<br/>" +
                                    "\\(\\Delta t\\) = change in time<br/>" +
                                    "\\(x_i\\) = initial position of the object<br/>" +
                                    "\\(x_f\\) = final position of the object<br/>" +
                                    "\\(t_i\\) = time when the object was at position \\(x_i\\)<br/>" +
                                    "\\(t_f\\) = time when the object was at position \\(x_f\\)<br/>"},
                    {"Velocity", "Instantaneous Velocity",
                            "<h2><b>Instantaneous Velocity</b></h2>" +
                                    "Instantaneous velocity is the measure of the velocity of an object at a particular moment.<br/>"},
                    {"Velocity", "Velocity References", "References:<br> Paul Cotarlea (2014). Learn Physics (Version 1.0.6) [Mobile Application Software]. Retrieved from https://play.google.com/store/apps<br><br>\n" +
                            "Geckonization (2016). Pocket Physics (Version 3.0.92) [Mobile Application Software]. Retrieved from https://play.google.com/store/apps<br><br>\n" +
                            "CK-12 Exploration Series | Physics Simulations (2016). Retrieved from http://interactives.ck12.org/simulations<br><br>\n" +
                            "Speed and Velocity (2016). Retrieved from http://www.physicsclassroom.com/class/1DKin/Lesson-1/Speed-and-Velocity<br>\n"},
                    {"Acceleration", "Acceleration Definition",
                            "<h2><b>Definition</b></h2>" +
                                    "Acceleration:<br/>" +
                                    "\t•\tis the rate of change of velocity with respect to time<br/>" +
                                    "\t•\tcan be positive (speeding up) or negative (slowing down)<br/><br/>" +
                                    "The SI unit used for acceleration is meter per second squared (\\({m \\over s^2}\\)).<br/>"},
                    {"Acceleration", "Acceleration",
                            "<h2><b>Acceleration</b></h2>" +
                                    "Acceleration is the ratio of total velocity change (\\(\\Delta v\\)) taken over time interval (\\(\\Delta t\\)).<br/>" +
                                    "$$a = {v_f - v_i \\over t}$$"},
                    {"Acceleration", "Average Acceleration",
                            "<h2><b>Average Acceleration</b></h2>" +
                                    "Average acceleration can be calculated using this formula:<br/>" +
                                    "$$a_{av} = {v_f - v_i \\over t_f - t_i}$$" +
                                    "Where:<br/>" +
                                    "\\(v_i\\) = initial velocity of the object<br/>" +
                                    "\\(v_f\\) = final velocity of the object<br/>" +
                                    "\\(t_i\\) = time when the object had velocity \\(v_i\\)<br/>" +
                                    "\\(t_f\\) = time when the object had velocity \\(v_f\\)<br/>"},
                    {"Acceleration", "Instantaneous Acceleration",
                            "<h2><b>Instantaneous Acceleration</b></h2>" +
                                    "Instantaneous acceleration is the change of velocity at infinitesimal (very small) time interval.<br/>"},
                    {"Acceleration", "Acceleration References", "References:<br> Paul Cotarlea (2014). Learn Physics (Version 1.0.6) [Mobile Application Software]. Retrieved from https://play.google.com/store/apps<br><br>\n" +
                            "Geckonization (2016). Pocket Physics (Version 3.0.92) [Mobile Application Software]. Retrieved from https://play.google.com/store/apps<br><br>\n" +
                            "CK-12 Exploration Series | Physics Simulations (2016). Retrieved from http://interactives.ck12.org/simulations<br><br>\n" +
                            "Acceleration (2016). Retrieved from http://www.physicsclassroom.com/class/1DKin/Lesson-1/Acceleration <br>\n"},
                    {"Newton's Laws of Motion", "Newton's Laws of Motion",
                            "<h2><b>Newton's Laws of Motion</b></h2>" +
                                    "There are three laws of motion. These laws are the physical laws that laid the foundation for classical" +
                                    "mechanics. It describes the relationship between an object and the forces that acts upon it, and its" +
                                    "motion in response to those forces.<br><br>" +
                                    "Newton’s Laws of Motion have been expressed in several various ways, and can be summarized as the" +
                                    "following:<br><br>" +
                                    "\t•\t<b>First Law</b>: An object in motion tends to stay in motion and an object at rest tends to stay at rest" +
                                    "unless acted upon by an external force.<br><br>" +
                                    "\t•\t<b>Second Law</b>: Force is equal to mass times acceleration.<br><br>" +
                                    "\t•\t<b>Third Law</b>: For every action there is an equal and opposite reaction."},
                    {"Newton's Laws of Motion", "Newton's Laws of Motion References", "References:<br> Nave, R. Newton’s First Law. Retrieved from http://www.bibme.org/citation-guide/apa/website<br><br>\n" +
                            "Nave, R. Newton’s Second Law. Retrieved from http://www.bibme.org/citation-guide/apa/website<br><br>\n" +
                            "Nave, R. Newton’s Third Law. Retrieved from http://www.bibme.org/citation-guide/apa/website<br>\n"},
                    {"Free-fall", "Free-fall Definition",
                            "<h2><b>Definition</b></h2>" +
                                    "Free fall:<br/>" +
                                    "\t•\trefers to the motion of an object where its motion is affected only by gravity<br/>" +
                                    "\t•\tacts only along the y-axis<br/><br/>" +
                                    "Acceleration due to gravity:\t<br/>" +
                                    "\t•\tis equal to 9.8 \\({m \\over s^2}\\)<br/>" +
                                    "\t•\tis constant regardless of the object’s mass<br/>"},
                    {"Free-fall", "Free-fall Velocity",
                            "<h2><b>Free fall</b></h2>|" +
                                    "An object is thrown with an initial velocity (\\(v_i\\)) along the y-axis. The position and speed of an object in free fall motion can be calculated from the " +
                                    "motion " +
                                    "equations.<br/><br/>" +
                                    "Velocity along the y-axis at any instant t:<br/>" +
                                    "$$v_f = v_i + g \\cdot t$$"},
                    {"Free-fall", "Free-fall Displacement",
                            "The displacement along y-axis at any instant t:<br/>" +
                                    "$$d = {1 \\over 2}g  \\cdot t^2$$"},
                    {"Free-fall", "Free-fall Upward Motion",
                            "In the case where an object is initially thrown upwards, the following formula can be used:<br/>" +
                                    "$$v_f^2 = v_i^2 + 2 \\cdot g \\cdot d$$" +
                                    "Where:<br/>" +
                                    "\\(v_i\\) = initial velocity<br/>" +
                                    "\\(v_f\\) = final velocity<br/>" +
                                    "\\(d\\) = vertical distance<br/>" +
                                    "\\(g\\) = acceleration due to gravity<br/>" +
                                    "\\(t\\) = time duration"},
                    {"Free-fall", "Free-fall References", "References:<br> Paul Cotarlea (2014). Learn Physics (Version 1.0.6) [Mobile Application Software]. Retrieved from https://play.google.com/store/apps<br><br>\n" +
                            "Geckonization (2016). Pocket Physics (Version 3.0.92) [Mobile Application Software]. Retrieved from https://play.google.com/store/apps<br><br>\n" +
                            "CK-12 Exploration Series | Physics Simulations (2016). Retrieved from http://interactives.ck12.org/simulations<br>\n"},
                    {"Projectile Motion", "Projectile Motion Definition",
                            "<h2><b>Definition</b></h2>" +
                                    "Projectile:<br/>" +
                                    "\t•\tis an object thrown with an initial velocity in a vertical plane<br/>" +
                                    "\t•\tmoves in two dimensions<br/>" +
                                    "\t•\tacts under the action of gravity alone without being propelled<br/><br/>" +
                                    "Projectile Motion:<br/>" +
                                    "\t•\tis the motion done by the projectile<br/><br/>" +
                                    "Trajectory:<br/>" +
                                    "\t•\tIs the path passed by the projectile<br/>"},
                    {"Projectile Motion", "Projectile Motion",
                            "<h2><b>Projectile Motion</b></h2>" +
                                    "Projectile motion is a two dimensional motion. Any two dimensional motion case can be split up into two cases of one dimensional motion.<br/><br/>" +
                                    "An important reminder is that the motion along the x-axis does not affect the motion along the y-axis. It also applies in vice versa. Each motion along each axis is independent of each other.<br/>" +
                                    "|" +
                                    "Projectile motion formula is given by the following:<br/><br/>" +
                                    "Horizontal distance (\\(m\\)):<br/>" +
                                    "$$x = v_{x}t$$" +
                                    "Horizontal velocity (\\(m \\over s\\)):<br/>" +
                                    "$$v_x = v_{xi}$$" +
                                    "Vertical distance (\\(m\\)):<br/>" +
                                    "$$y = v_{yi}t - {1 \\over 2}g \\cdot t^2$$" +
                                    "Vertical velocity (\\(m \\over s\\)):<br/>" +
                                    "$$v_y =  g \\cdot t + v_{yi}$$" +
                                    "Where:<br/>" +
                                    "\\(v_x\\) = velocity of the object along x-axis<br/>" +
                                    "\\(v_{xi}\\) = initial velocity of the object along x-axis<br/>" +
                                    "\\(v_t\\) = velocity of the object along y-axis<br/>" +
                                    "\\(v_{yi}\\) = initial velocity of the object along y-axis<br/>" +
                                    "\\(g\\) = acceleration due to gravity <br/>" +
                                    "\\(t\\) = time duration<br/><br/>"},
                    {"Projectile Motion", "Trajectory",
                            "Formulas related to trajectory motion is given by the following:<br/>" +
                                    "Time of flight (\\(s\\)):<br/>" +
                                    "$$t = {2v_{1}sin \\theta \\over g}$$" +
                                    "Maximum height reached (\\(m\\)):<br/>" +
                                    "$$H = {v_{1}^{2}sin^{2} \\theta \\over 2g}$$" +
                                    "Horizontal range (\\(m\\)):<br/>" +
                                    "$$R = {v_{1}^{2}sin2 \\theta \\over g}$$" +
                                    "Where:<br/>" +
                                    "\\(v_1\\) = initial velocity of the object<br/>" +
                                    "\\(sin \\theta\\) = component along y-axis<br/>" +
                                    "\\(cos \\theta\\) = component along x-axis<br/>"},
                    {"Projectile Motion", "Projectile Motion References", "References:<br> Paul Cotarlea (2014). Learn Physics (Version 1.0.6) [Mobile Application Software]. Retrieved from https://play.google.com/store/apps<br><br>\n" +
                            "Geckonization (2016). Pocket Physics (Version 3.0.92) [Mobile Application Software]. Retrieved from https://play.google.com/store/apps<br><br>\n" +
                            "CK-12 Exploration Series | Physics Simulations (2016). Retrieved from http://interactives.ck12.org/simulations<br><br>\n" +
                            "Projectile Motion Formula (2016). Retrieved from http://formulas.tutorvista.com/physics/projectile-motion-formula.html<br>\n"},
                    {"Friction", "Friction Definition",
                            "<h2><b>Definition</b></h2>" +
                                    "Friction Force:<br/>" +
                                    "\t•\tis the force that is exerted by a surface as an object moves across it<br/>" +
                                    "|" +
                                    "When an object is being pushed across a surface, the surface offers resistance to the movement of the object.<br/><br/>" +
                                    "The friction force is opposite to the direction of the motion of the object.<br/>"},
                    {"Friction", "Friction",
                            "<h2><b>Two types of Friction</b></h2>" +
                                    "1. Static Friction:<br/>" +
                                    "\t•\tis the friction between multiple solid objects that are not moving relative to each other<br/><br/>" +
                                    "2. Kinetic Friction:<br/>" +
                                    "\t•\tis the friction between multiple solid objects that are moving relative to each other<br/>" +
                                    "The friction force is dependent on two factors:<br/>" +
                                    "\t•\tthe material of the objects that are in contact<br/>" +
                                    "\t•\tthe force that pushes both surfaces together<br/>" +
                                    "The following is the equation that summarizes the topic:<br/>" +
                                    "$$F_f = \\mu \\cdot F_n$$" +
                                    "Where:<br/>" +
                                    "\\(F_f\\) = frictional force <br/>" +
                                    "\\(\\mu\\) = coefficient of friction<br/>" +
                                    "\\(F_n\\) = normal force<br/>"},
                    {"Friction", "Friction References", "References:<br> Paul Cotarlea (2014). Learn Physics (Version 1.0.6) [Mobile Application Software]. Retrieved from https://play.google.com/store/apps<br><br>\n" +
                            "Geckonization (2016). Pocket Physics (Version 3.0.92) [Mobile Application Software]. Retrieved from https://play.google.com/store/apps<br><br>\n" +
                            "CK-12 Exploration Series | Physics Simulations (2016). Retrieved from http://interactives.ck12.org/simulations<br>\n"},
                    {"Free-body Diagrams", "Free Body Diagrams Definition",
                            "<h2><b>Definition</b></h2>" +
                                    "Free Body Diagrams:<br/>" +
                                    "\t•\talso known as “Force Diagram”<br/>" +
                                    "\t•\ta graphical illustration used to visualize the applied forces, movements and resulting reaction on a body in a steady state condition<br/>" +
                                    "\t•\tshows all the forces acting on an object or a “body” that is singled out from or “freed” from a group of objects<br/>"},
                    {"Free-body Diagrams", "Free Body Diagrams",
                            "<h2><b>Free Body Diagrams</b></h2>" +
                                    "There are four types of forces typically used in free body diagrams. These are the forces used.<br/>" +
                                    "1. Weight (W) - gravitational force that is directed towards the ground (downwards).<br/>" +
                                    "2. Normal (N) - its force is directed perpendicular to the object’s surface.<br/>" +
                                    "3. Tension (T) - its force is directed along a string, rope, chain or anything an object is attached to.<br/>" +
                                    "4. Friction (fr) - its force’s direction opposes the relative motion of the object.<br/>" +
                                    "<br/>" +
                                    "Drawing Free Body Diagrams<br/>" +
                                    "<br/>" +
                                    "There are a few rules we should follow when drawing a free body diagram.<br/>" +
                                    "1.\tAlways draw the forces from the center of the object.<br/>" +
                                    "2.\tThe stronger the force is, the longer the arrow is.<br/>" +
                                    "3.\tThe arrow should point in the direction for force is acting.<br/>" +
                                    "4.\tLabel the forces acting with letters/symbols.<br/>" +
                                    "<br/>" +
                                    "|" +
                                    "<br/>" +
                                    "Here are a few hints when working with free body diagrams: <br/>" +
                                    "\t•\tThe force of gravity or weight always points straight down. <br/>" +
                                    "\t•\tThe normal force will always push straight up from a surface.<br/>" +
                                    "\t•\tIf an object is moving in one direction, friction is acting in the opposite direction.<br/>" +
                                    "\t•\tThink about whether the forces on opposite sides are balanced or not. If they are, the two arrows should be about the same length. <br/>"},
                    {"Free-body Diagrams", "Free-body Diagrams References", "References:<br> Paul Cotarlea (2014). Learn Physics (Version 1.0.6) [Mobile Application Software]. Retrieved from https://play.google.com/store/apps<br><br>\n" +
                            "Geckonization (2016). Pocket Physics (Version 3.0.92) [Mobile Application Software]. Retrieved from https://play.google.com/store/apps<br><br>\n" +
                            "Drawing Free-Body Diagrams (2016). Retrieved from http://www.physicsclassroom.com/class/newtlaws/Lesson-2/Drawing-Free-Body-Diagrams<br>\n"},
                    {"Momentum and Impulse", "Impulse and Momentum Definition",
                            "<h2><b>Definition</b></h2>" +
                                    "Momentum:<br/>" +
                                    "\t•\tis defined as “mass in motion”<br/>" +
                                    "\t•\tdepends upon the variables mass and velocity<br/>" +
                                    "\t•\tis a vector quantity<br/>"},
                    {"Momentum and Impulse", "Momentum",
                            "<h2><b>Momentum</b></h2>" +
                                    "In terms of an equation, the momentum of an object is equal to the mass of the object times the velocity of the object. <br/><br/>" +
                                    "<center><i>Momentum = mass * velocity</i></center><br/>" +
                                    "In physics, the symbol for the quantity momentum is the lower case p. Thus, the above equation can be rewritten as:<br/>" +
                                    "$$p = m \\cdot v$$" +
                                    "where:<br/>" +
                                    "\\(m\\) = mass of the object<br/>" +
                                    "\\(v\\) = velocity of the object<br/>" +
                                    "The equation illustrates that momentum is:<br/>" +
                                    "\t•\tdirectly proportional to an object's mass <br/>" +
                                    "\t•\tdirectly proportional to the object's velocity<br/>" +
                                    "Momentum is also a vector quantity. The momentum of an object then is fully described by both magnitude and direction.<br/>"},
                    {"Momentum and Impulse", "Impulse",
                            "<h2><b>Impulse</b></h2>" +
                                    "Impulse is known as quantity force multiplied by time. <br/><br/>" +
                                    "And since the quantity \\(m \\cdot \\Delta v\\) is the change in momentum. The equation really says that the Impulse is equal to Change in Momentum.<br/>" +
                                    "The physics of collisions are governed by the laws of momentum. The first law is the equation known as the impulse-momentum change equation. The law can be expressed this way:<br/>" +
                                    "$$F \\cdot t = m \\cdot \\Delta v$$" +
                                    "In a collision:<br/>" +
                                    "\t•\tobjects experience an impulse<br/>" +
                                    "\t•\tthe impulse causes and is equal to the change in momentum<br/>"},
                    {"Momentum and Impulse", "Momentum and Impulse References", "References:<br> Paul Cotarlea (2014). Learn Physics (Version 1.0.6) [Mobile Application Software]. Retrieved from https://play.google.com/store/apps<br><br>\n" +
                            "Geckonization (2016). Pocket Physics (Version 3.0.92) [Mobile Application Software]. Retrieved from https://play.google.com/store/apps<br><br>\n" +
                            "CK-12 Exploration Series | Physics Simulations (2016). Retrieved from http://interactives.ck12.org/simulations<br><br>\n" +
                            "Momentum and Impulse Connection (2016). Retrieved from http://www.physicsclassroom.com/class/momentum/Lesson-1/Momentum-and-Impulse-Connection<br>\n"},
                    {"Law of Conservation of Energy", "Law of Conservation of Energy Definition",
                            "<h2><b>Definition</b></h2>" +
                                    "Law of Conservation of Energy<br/>" +
                                    "\t•\tstates that the total energy of an isolated system remains constant<br/>" +
                                    "\t•\tIt implies that energy can neither be created nor destroyed, but can be change from one form to another<br/>" +
                                    "\t•\tthe change in energy of an object due to a transformation is equal to the work done on the object or by the object for that transformation<br/>"},
                    {"Law of Conservation of Energy", "Law of Conservation of Energy",
                            "<h2><b>Conservation of Energy</b></h2>" +
                                    "The different types of energy are the potential energy, kinetic energy, and the total mechanical energy. These types of energy will further be discussed in a different part (Energy) of this application.<br/><br/>" +
                                    "The basic formula for the conservation of energy is:<br/>" +
                                    "<br><center><i>Energy spent = Energy gained</i></center><br>" +
                                    "The formula can be more explicitly written as Conservation of Energy Equation depending on the context. <br/>" +
                                    "\t•\tAn object when dropped from a height transforms its potential energy into kinetic energy.<br/><br/>" +
                                    "Mathematically it can be expressed as a Conservation of Energy Equation as follows:<br/>" +
                                    "$$mgh = {1 \\over 2}mv^2$$" +
                                    "Where:<br/>" +
                                    "\\(m\\) = mass of the object<br/>" +
                                    "\\(v\\) = final velocity after falling from a height of <b>h</b><br/>" +
                                    "\\(g\\) = acceleration due to gravity<br/>" +
                                    "<br/>"},
                    {"Law of Conservation of Energy", "Law of Conservation of Energy References", "References:<br> Paul Cotarlea (2014). Learn Physics (Version 1.0.6) [Mobile Application Software]. Retrieved from https://play.google.com/store/apps<br><br>\n" +
                            "Geckonization (2016). Pocket Physics (Version 3.0.92) [Mobile Application Software]. Retrieved from https://play.google.com/store/apps<br><br>\n" +
                            "Tuckerman, M. (2011). Law of conservation of energy. Retrieved from http://www.nyu.edu/classes/tuckerman/adv.chem/lectures/lecture_2/node4.html<br>\n"},
                    {"Work", "Work Definition",
                            "<h2><b>Definition</b></h2>" +
                                    "Work:<br/>" +
                                    "\t•\tis the measure of energy being transferred occurring when an object is moved over a distance<br/>" +
                                    "|" +
                                    "The SI unit used for work is Joule (\\(J\\)). Joule’s base units is \\(kg \\cdot {m^2 \\over s^2}\\)<br/>"},
                    {"Work", "Work",
                            "<h2><b>Work</b></h2>" +
                                    "A force is doing work when it acts on an object which displaces it from the point of application. The force vector and the position vector in a work is " +
                                    "parallel to each other<br/>" +
                                    "Constant work can be calculated using this formula:<br/>" +
                                    "$$W = \\vec{F} \\cdot \\vec{x} \\cdot cos \\theta$$" +
                                    "Where:<br/>" +
                                    "\\(\\vec{F}\\) = force vector<br/>" +
                                    "\\(\\vec{x}\\) = position vector<br/>" +
                                    "\\(cos \\theta\\) = component along x-axis<br/>"},
                    {"Work", "Work References", "References:<br> Paul Cotarlea (2014). Learn Physics (Version 1.0.6) [Mobile Application Software]. Retrieved from https://play.google.com/store/apps<br><br>\n" +
                            "Geckonization (2016). Pocket Physics (Version 3.0.92) [Mobile Application Software]. Retrieved from https://play.google.com/store/apps<br><br>\n" +
                            "CK-12 Exploration Series | Physics Simulations (2016). Retrieved from http://interactives.ck12.org/simulations<br><br>\n" +
                            "Definition and Mathematics of Work (2016). Retrieved from http://www.physicsclassroom.com/class/energy/Lesson-1/Definition-and-Mathematics-of-Work<br><br>\n" +
                            "Calculating the Amount of Work Done by Forces (2016). Retrieved from http://www.physicsclassroom.com/class/energy/Lesson-1/Calculating-the-Amount-of-Work-Done-by-Forces<br>\n"},
                    {"Energy", "Energy Definition",
                            "<h2><b>Definition</b></h2>" +
                                    "Energy:<br/>" +
                                    "\t•\tis the capacity of performing work<br/>" +
                                    "\t•\tmay exist in various forms (potential, kinetic, electric, chemical etc.)<br/>" +
                                    "The SI unit for energy is the same with work, Joule (\\(J\\)).<br/>"},
                    {"Energy", "Kinetic Energy",
                            "<h2><b>Kinetic Energy</b></h2>" +
                                    "Kinetic energy is an energy that is possessed by means of its motion.<br/><br/>" +
                                    "Kinetic energy can be calculated using this formula:<br/>" +
                                    "$$KE = {mv^2 \\over 2}$$" +
                                    "Where:<br/>" +
                                    "\\(m\\) = mass of the object<br/>" +
                                    "\\(v\\) = velocity of the object<br/>"},
                    {"Energy", "Potential Energy",
                            "<h2><b>Potential Energy</b></h2>" +
                                    "Potential energy is an energy that is possessed by means of its position relative to others.<br/><br/>" +
                                    "Gravitational Potential Energy is the energy that is stored in an object as the result of the object’s" +
                                    "vertical position. The gravitational potential energy is stored as the result of the gravitational attraction" +
                                    "of the Earth to for the object. The gravitational potential energy of an object is dependent on two" +
                                    "variables - the mass of the object and the height to which it is raised.<br><br/>" +
                                    "Gravitational potential energy can be calculated using this formula:<br/>" +
                                    "$$PE_g = m \\cdot g \\cdot h$$" +
                                    "Where:<br/>" +
                                    "\\(m\\) = mass of the object<br/>" +
                                    "\\(h\\) = height of the object above the ground<br/>" +
                                    "\\(g\\) = acceleration due to gravity<br/><br/>"},
                    {"Energy", "Total Mechanical Energy",
                            "<h2><b>Total Mechanical Energy</b></h2>" +
                                    "Total mechanical energy is the sum of the kinetic and potential energy of a conservative system:<br/>" +
                                    "$$TME = KE + PE - 0$$"},
                    {"Energy", "Energy References", "References:<br> Paul Cotarlea (2014). Learn Physics (Version 1.0.6) [Mobile Application Software]. Retrieved from https://play.google.com/store/apps<br><br>\n" +
                            "Geckonization (2016). Pocket Physics (Version 3.0.92) [Mobile Application Software]. Retrieved from https://play.google.com/store/apps<br><br>\n" +
                            "CK-12 Exploration Series | Physics Simulations (2016). Retrieved from http://interactives.ck12.org/simulations<br><br>\n" +
                            "Potential Energy (2016). Retrieved from http://www.physicsclassroom.com/class/energy/Lesson-1/Potential-Energy<br><br>\n" +
                            "Kinetic Energy (2016). Retrieved from http://www.physicsclassroom.com/class/energy/Lesson-1/Kinetic-Energy<br><br>\n" +
                            "Potential Energy (2016). Retrieved from http://www.ck12.org/physics/Potential-Energy/lesson/Potential-Energy-PPC <br>\n"},
                    {"Power", "Power Definition",
                            "<h2><b>Definition</b></h2>" +
                                    "Power:<br/>" +
                                    "\t•\tis the rate of doing work<br/>" +
                                    "\t•\thas no direction<br/>" +
                                    "\t•\tis a scalar quantity<br/>" +
                                    "The SI unit for power is Watt (\\(W\\)), or Joule per second (\\(J \\over s\\)).<br/>"},
                    {"Power", "Average Power",
                            "<h2><b>Average Power</b></h2>" +
                                    "Average power, or simple “power”, is the average amount of work done converted per unit of time.<br/><br/>" +
                                    "Average power can be calculated by using this formula:<br/>" +
                                    "$$P_{av} = {\\Delta W \\over \\Delta t}$$" +
                                    "Where:<br/>" +
                                    "\\(\\Delta W\\) = amount of work performed<br/>" +
                                    "\\(\\Delta t\\) = time duration<br/>"},
                    {"Power", "Power References", "References:<br> Paul Cotarlea (2014). Learn Physics (Version 1.0.6) [Mobile Application Software]. Retrieved from https://play.google.com/store/apps<br><br>\n" +
                            "Geckonization (2016). Pocket Physics (Version 3.0.92) [Mobile Application Software]. Retrieved from https://play.google.com/store/apps<br><br>\n" +
                            "CK-12 Exploration Series | Physics Simulations (2016). Retrieved from http://interactives.ck12.org/simulations<br><br>\n" +
                            "Power (2016). Retrieved from http://www.physicsclassroom.com/class/energy/Lesson-1/Power<br>\n"},
//                    {"Power", "Instantaneous Power",
//                            "<h2><b>Instantaneous Power</b></h2>" +
//                                    "Instantaneous power is the limiting value of the average power as the time interval approaches to zero.<br/><br/>" +
//                                    "Average power can be calculated by using this formula:<br/>" +
//                                    "$$P = F \\cdot cosa \\cdot v$$" +
//                                    "Where:<br/>" +
//                                    "\\(W\\) = work done<br/>" +
//                                    "\\(t\\) = time duration<br/>" +
//                                    "\\(F\\) = force applied on the object<br/>" +
//                                    "\\(x\\) = path made by the object<br/>" +
//                                    "\\(a\\) = angle between the force and the position vectors<br/>" +
//                                    "\\(v\\) = velocity of the object<br/>"},
                    {"Uniform Circular Motion", "Uniform Circular Motion Definition",
                            "<h2><b>Definition</b></h2>" +
                                    "Uniform Circular Motion:<br/>" +
                                    "\t•\tis the motion of an object traveling at a constant speed on a path that is circular<br/>"},
                    {"Uniform Circular Motion", "Measurements of a Circle",
                            "<h2><b>Measurements of a Circle</b></h2>" +
                                    "The arc of a circle is a portion of the circumference.<br/><br/>" +
                                    "The length of an arc is the length of its portion of the circumference.<br/><br/>" +
                                    "The radian is the standard unit of angular measure. When it is drawn as a central angle, it subtends an arc whose length is equal to the length of the radius of the circle.<br/>" +
                                    "|" +
                                    "The relationship between the degrees and radians is:<br/>" +
                                    "<center><i>radians = degrees * \\(\\pi \\over 180\\)</i></center>"},
                    {"Uniform Circular Motion", "Frequency",
                            "<h2><b>Frequency</b></h2>" +
                                    "The frequency (\\(f\\)) is the number of revolutions completed per time unit.<br/><br/>" +
                                    "The SI unit used for frequency is hertz (\\(Hz\\)). Hertz’ base units is (\\(1 \\over s\\)).<br/><br/>" +
                                    "Another unit of measure for frequency is cycles per second (\\(cycles \\over seconds\\)). 60 RPM is equal to 1 hertz.<br/><br/>" +
                                    "Period (\\(T\\)):<br/>" +
                                    "\t•\tis the time an object takes to travel one revolution around the circle.<br/>" +
                                    "The SI unit used for period is second \\(s\\). This is based on the reciprocal value of frequency where (\\(T = {1 \\over f}\\)).<br/>"},
                    {"Uniform Circular Motion", "Angular Displacement",
                            "<h2><b>Angular Displacement</b></h2>" +
                                    "|" +
                                    "The angular displacement (Δθ) is the angle traveled by the object while moving from point B to C around the circular path.<br/>"},
                    {"Uniform Circular Motion", "Length of Arc",
                            "<h2><b>Length of Arc</b></h2>" +
                                    "The length of arc (\\(\\Delta S\\)) is directly proportional to the angular displacement subtended traced at the center of circle by the ends of the arc.<br/><br/>" +
                                    "Length of arc can be calculated using this formula:<br/>" +
                                    "$$\\Delta S = r \\cdot \\Delta \\theta$$"},
                    {"Uniform Circular Motion", "Tangential Velocity",
                            "<h2><b>Tangential Velocity</b></h2>" +
                                    "The tangential velocity (\\(v\\) is the velocity measured at any tangential point in a circle.<br/>" +
                                    "Tangential velocity can be calculated using this formula:<br/>" +
                                    "$$v = {\\Delta S \\over \\Delta t}$$"},
                    {"Uniform Circular Motion", "Velocity around a Circle",
                            "The velocity around the circle can be calculated using this formula:<br/>" +
                                    "$$v = 2 \\cdot \\pi \\cdot {r \\over t}$$"},
                    {"Uniform Circular Motion", "Angular Velocity",
                            "<h2><b>Angular Velocity</b></h2>" +
                                    "The angular velocity (ω) is rate of change of angular position of a body that is rotating.<br/><br/>" +
                                    "The SI unit used for angular velocity is radian per second (rad/s).<br/><br/>" +
                                    "Angular velocity can be calculated using this formula:<br/>" +
                                    "$$\\omega = {\\Delta \\theta \\over \\Delta t}$$" +
                                    "Since angular velocity is a vector quantity, it is defined as positive when the motion in the circle is in counter-clockwise, and is defined as negative when the motion in the circle is in clockwise.<br/><br/>" +
                                    "The relationship between the linear velocity and angular velocity is:<br/>" +
                                    "$$v = {\\Delta S \\over \\Delta t} = r \\cdot {\\Delta \\theta \\over \\Delta t} = r \\cdot \\omega$$"},
                    {"Uniform Circular Motion", "Centripetal Acceleration",
                            "<h2><b>Centripetal Acceleration</b></h2>" +
                                    "The centripetal acceleration is the rate of change of tangential velocity.<br/><br/>" +
                                    "The centripetal acceleration is always pointing towards the center of the circle in motion.<br/>" +
                                    "Centripetal acceleration can be calculated using this formula:<br/>" +
                                    "$$a_c = {{\\omega ^2} \\over {r}}$$"},
                    {"Uniform Circular Motion", "Uniform Circular Motion References", "References:<br> Paul Cotarlea (2014). Learn Physics (Version 1.0.6) [Mobile Application Software]. Retrieved from https://play.google.com/store/apps<br><br>\n" +
                            "Geckonization (2016). Pocket Physics (Version 3.0.92) [Mobile Application Software]. Retrieved from https://play.google.com/store/apps<br><br>\n" +
                            "CK-12 Exploration Series | Physics Simulations (2016). Retrieved from http://interactives.ck12.org/simulations<br><br>\n" +
                            "Uniform Circular Motion (2016). Retrieved from http://www.physicsclassroom.com/mmedia/circmot/ucm.cfm<br>\n"},
                    {"Centripetal and Centrifugal Forces", "Centripetal and Centrifugal Forces Definition",
                            "<p><h2><b>Definition</b></h2>" +
                                    "Centripetal Force:<br/>" +
                                    "\t•\tis a force that acts on an object that is moving in a circular path and is directed towards the center where the object is moving<br/><br/>" +
                                    "Centrifugal Force:<br/>" +
                                    "\t•\tis the opposing reaction force of the centripetal force<br/>" +
                                    "\t•\tis a force acting outwards of an object that is moving in a circular path<br/>"},
                    {"Centripetal and Centrifugal Forces", "Centripetal and Centrifugal Forces",
                            "<h2><b>Centripetal and Centrifugal Forces</b></h2>" +
                                    "According the Newton’s second law motion, where there is an acceleration (centripetal acceleration), there is a force (centripetal force).<br/>" +
                                    "|" +
                                    "Magnitude of the centripetal force can be calculated using this formula:<br/>" +
                                    "$$F = {m \\cdot v^2 \\over r} $$" +
                                    "Where:<br/>" +
                                    "\\(m\\) = mass of the object<br/>" +
                                    "\\(v\\) = tangential velocity of the object<br/>" +
                                    "\\(r\\) = radius of curvature cause by the force<br/><br/>" +
                                    "Newton’s third law of motion states that every action has an equal and opposite reaction. Therefore, in this case, there must be an equal and opposite reaction force to the centripetal force which is called the centrifugal force.<br/></p>"},
                    {"Centripetal and Centrifugal Forces", "Centripetal and Centrifugal Forces References", "References:<br>Paul Cotarlea (2014). Learn Physics (Version 1.0.6) [Mobile Application Software]. Retrieved from https://play.google.com/store/apps<br><br>\n" +
                            "Geckonization (2016). Pocket Physics (Version 3.0.92) [Mobile Application Software]. Retrieved from https://play.google.com/store/apps<br><br>\n" +
                            "CK-12 Exploration Series | Physics Simulations (2016). Retrieved from http://interactives.ck12.org/simulations<br><br>\n" +
                            "The Centripetal Force Requirement (2016). Retrieved from http://www.physicsclassroom.com/mmedia/circmot/cf.cfm<br>\n"},
                    {"Rotational Motion", "Rotational Motion Definition",
                            "<p><h2><b>Definition</b></h2>" +
                                    "Rotational Motion:<br/>" +
                                    "\t•\tis a motion of an object in a circular path around a center (or point) of rotation<br/>"},
//                    {"Rotational Motion", "Moment of Inertia",
//                            "<h2><b>Moment of Inertia</b></h2>" +
//                                    "The moment of inertia \\(I\\) is the measure of the object’s resistance to the change to its rotation. It is dependent to the object’s mass \\(m\\) and distance \\(r\\) of the mass further from the center of the rotational motion.<br/><br/>" +
//                                    "Moment of inertia can be calculated using this formula:<br/>" +
//                                    "$$I = m \\cdot r^2$$"},
                    {"Rotational Motion", "Torque",
                            "<h2><b>Torque</b></h2>" +
                                    "The torque \\(\\tau \\) is the twisting force \\(F\\) that tends to cause the rotation of an object which is at position \\(r\\) from its axis of rotation. " +
                                    "The position is perpendicular to the force.<br/><br/>" +
                                    "Torque can be calculated using this formula:<br/>" +
                                    "$$\\tau = r \\cdot F$$"},
                    {"Rotational Motion", "Rotational Motion References", "References:<br> Paul Cotarlea (2014). Learn Physics (Version 1.0.6) [Mobile Application Software]. Retrieved from https://play.google.com/store/apps<br><br>\n" +
                            "Geckonization (2016). Pocket Physics (Version 3.0.92) [Mobile Application Software]. Retrieved from https://play.google.com/store/apps<br>`<br>\n" +
                            "CK-12 Exploration Series | Physics Simulations (2016). Retrieved from http://interactives.ck12.org/simulations<br>\n"},
//                    {"Rotational Motion", "Angular Momentum",
//                            "<h2><b>Angular Momentum</b></h2>" +
//                                    "The angular momentum \\(L\\) is the quantity of rotation of a body. It is dependent on the moment of inertia \\(I\\) of the object and its angular velocity vector \\(\\omega \\).<br/><br/>" +
//                                    "Angular momentum can be calculated using this formula:<br/>" +
//                                    "$$L = I \\cdot \\omega$$"}
            };

            for(String[] s: sections){
                Cursor c = database.query(
                        DataContract.LessonEntry.TABLE_NAME,
                        new String[]{DataContract.LessonEntry._ID},
                        DataContract.LessonEntry.COLUMN_NAME + " = ?",
                        new String[]{s[0]},
                        null,
                        null,
                        null
                );
                c.moveToFirst();

                ContentValues values = new ContentValues();
                values.put(DataContract.SectionEntry.COLUMN_LESSON_KEY,
                        c.getLong(c.getColumnIndex(DataContract.LessonEntry._ID)));
                values.put(DataContract.SectionEntry.COLUMN_NAME, s[1]);
                values.put(DataContract.SectionEntry.COLUMN_CONTENT, s[2]);

                database.insert(DataContract.SectionEntry.TABLE_NAME, null, values);
            }
        }
    }

    private void initImages(SQLiteDatabase database){
        if(database.isOpen()){
            String[][] images = {
                    {"Distance and Displacement", "img_lesson_vector_and_scalar_quantities", "A graph showing displacement."},
                    {"Average Velocity", "img_lesson_velocity", "A graph showing the relationship between displacement and time."},
                    {"Free-fall Velocity", "img_lesson_free_fall", "A graph showing the forces acting on a free-falling body."},
                    {"Projectile Motion", "img_lesson_projectile_motion", "A graph showing the horizontal and vertical velocities of a trajectory."},
                    {"Friction Definition", "img_lesson_friction", "A graph showing the frictional force of a body."},
                    {"Free Body Diagrams", "img_lesson_free_body_diagram", "A graph showing the forces acting upon a body."},
                    {"Work Definition", "img_lesson_work", "A graph showing the forces while doing work."},
                    {"Measurements of a Circle", "img_lesson_uniform_circular_motion_1", "A graph showing how radians work in a circle."},
                    {"Angular Displacement", "img_lesson_uniform_circular_motion_2", "A graph showing the length of arc and tangential velocity in a circle."},
                    {"Centripetal and Centrifugal Forces", "img_lesson_centripetal_and_centrifugal_forces", "A graph showing the centripetal and centrifugal forces acting on a body in a circle."}

//                    {"scalar and vector values definition", "ic_chapter_momentum_impulse", "figure 1: asdfasdf"},
//                    {"scalar and vector values definition", "ic_chapter_two_dimensional_motion", "figure 2: qwerqwer"}
            };

            for(String[] s: images){
                Cursor c = database.query(
                        DataContract.SectionEntry.TABLE_NAME,
                        new String[]{DataContract.SectionEntry._ID},
                        DataContract.SectionEntry.COLUMN_NAME + " = ?",
                        new String[]{s[0]},
                        null,
                        null,
                        null
                );
                c.moveToFirst();

                ContentValues values = new ContentValues();
                values.put(DataContract.ImageEntry.COLUMN_SECTION_KEY,
                        c.getLong(c.getColumnIndex(DataContract.SectionEntry._ID)));
                values.put(DataContract.ImageEntry.COLUMN_RESOURCE_NAME, s[1]);
                values.put(DataContract.ImageEntry.COLUMN_CAPTION, s[2]);

                database.insert(DataContract.ImageEntry.TABLE_NAME, null, values);
            }
        }
    }

    private void initExamples(SQLiteDatabase database) {
        if(database.isOpen()){
            String[][] examples = {
                    //Format
                    //{"Variable Name", "Content", "Section Name(Look at 2nd Column in Sections DB)"
                    {"Distance and Displacement", "QUESTION: <br>\n" +
                            "What is the distance and displacement of Michael Phelps after swimming from the starting line to the finish line, which is 100 meters away, and back to the starting line again? <br><br>\n" +
                            "SOLUTION: <br>\n" +
                            "Distance: <br>\n" +
                            "$$d = 100m + 100m$$\n" +
                            "$$d = 200m$$\n" +
                            "Displacement: <br>\n" +
                            "$$d = 100m - 100m$$\n" +
                            "$$d = 0m$$\n" +
                            "ANSWER: <br>\n" +
                            "Distance: <br>\n" +
                            "$$200m$$\n" +
                            "Distance is 200 meters since he swam 100 meters going to the finish line, and then another 100 meters going back to the starting line. <br>\n" +
                            "Displacement: <br>\n" +
                            "$$0m$$\n" +
                            "Displacement is 0 meters since Michael Phelps finished where he started. \n", "Scalar and Vector Values Formula"},
                    {"Velocity","QUESTION: <br>\n" +
                            "Usain Bolt jogged 2000 meters. He took 250 seconds to reach his destination. What was his average velocity? <br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$v = ?$$\n" +
                            "$$x_i = 0m$$\n" +
                            "$$x_f = 2000m$$\n" +
                            "$$t = 250s$$\n" +
                            "Since \\(v\\) is missing, we use this formula:\n" +
                            "$$v = {{x_f - x_i} \\over {t}}$$\n" +
                            "SOLUTION: <br>\n" +
                            "$$v = {{2000m - 0m} \\over 250s}$$\n" +
                            "$$v = {2000m \\over 250s}$$\n" +
                            "$$v = {8{m \\over s}}$$\n" +
                            "ANSWER: <br />\n" +
                            "$$8{m \\over s}$$ \n","Speed and Velocity"},
                    {"Initial Position","QUESTION: <br>\n" +
                            "Usain Bolt jogged with a speed of 8 meters per second. He ran for 250 seconds and reached 2000 meters. What was his initial distance before he started jogging? <br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$v = 8{m \\over s}$$\n" +
                            "$$x_i = ?$$\n" +
                            "$$x_f = 2000m$$\n" +
                            "$$t = 250s$$\n" +
                            "Since \\(x_i\\) is missing, we use this formula:\n" +
                            "$$x_i = x_f - v \\cdot t$$\n" +
                            "SOLUTION: <br>\n" +
                            "$$x_i = 2000m - 8{m \\over s} \\cdot 250s$$" +
                            "$$x_i = 2000m - 2000m$$\n" +
                            "$$x_i = 0m$$\n" +
                            "ANSWER: <br>\n" +
                            "$$0m$$\n","Speed and Velocity"},
                    {"Final Position","QUESTION: <br>\n" +
                            "Usain Bolt jogged with a speed of 8 meters per second. It took him 250 seconds to reach his destination. How far did he jogged? <br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$v = 8{m \\over s}$$\n" +
                            "$$x_i = 0m$$\n" +
                            "$$x_f = ?$$\n" +
                            "$$t = 250s$$\n" +
                            "Since \\(x_f\\) is missing, we use this formula:\n" +
                            "$$x_f = v \\cdot t - x_i$$\n" +
                            "SOLUTION: <br>\n" +
                            "$$x_f = {8{m \\over s} \\cdot 250s - 0m}$$\n" +
                            "$$x_f = 2000m - 0m$$\n" +
                            "$$x_f = 2000m$$\n" +
                            "ANSWER: <br>\n" +
                            "$$2000m$$\n","Speed and Velocity"},
                    {"Time","QUESTION: <br>\n" +
                            "Usain Bolt jogged 2000 meters. He had an average speed of 8 meters per second. How long did it took him to reach the finish line? <br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$v = 8{m \\over s}$$\n" +
                            "$$x_i = 0m$$\n" +
                            "$$x_f = 2000m$$\n" +
                            "$$t = ?$$\n" +
                            "Since \\(t\\) is missing, we use this formula:\n" +
                            "$$t = {{x_f - x_i} \\over {v}}$$\n" +
                            "SOLUTION: <br>\n" +
                            "$$t = {{2000m - 0m} \\over {8{m \\over s}}}$$\n" +
                            "$$t = {2000m \\over 8{m \\over s}}$$\n" +
                            "$$t = 250s$$\n" +
                            "ANSWER: <br>\n" +
                            "$$250s$$\n","Speed and Velocity"},

                    {"Average Velocity","QUESTION: <br>\n" +
                            "During a circuit race, car #95 did his 2nd lap which started at 100 seconds and finished at 190 seconds. What is the car’s average velocity if one lap is 9000 meters long and the car started from the lap line (0 meters)? <br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$v_{av} = ?$$\n" +
                            "$$x_i = 0m$$\n" +
                            "$$x_f = 9000m$$\n" +
                            "$$t_i = 100s$$\n" +
                            "$$t_f = 190s$$\n" +
                            "Since \\(v_{av}\\) is missing, we use this formula:\n" +
                            "$$v_{av} = {{x_f - x_i} \\over {t_f - t_i}}$$\n" +
                            "SOLUTION: <br>\n" +
                            "$$v_{av} = {{9000m - 0m} \\over {190s - 100s}}$$\n" +
                            "$$v_{av} = {9000m \\over 90s}$$\n" +
                            "$$v_{av} = 100 {m \\over s}$$\n" +
                            "ANSWER: <br>\n" +
                            "$$100{m \\over s}$$\n","Average Velocity"},
                    {"Initial Position","QUESTION: <br>\n" +
                            "During a circuit race, car #95 did his 2nd lap which started at 100 seconds and finished at 190 seconds. The car had an average velocity of 100 meters per second. What is the car’s initial position if one lap is 9000 meters long? <br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$v_{av} = 100{m \\over s}$$\n" +
                            "$$x_i = ?$$\n" +
                            "$$x_f = 9000m$$\n" +
                            "$$t_i = 100s$$\n" +
                            "$$t_f = 190s$$\n" +
                            "Since \\(x_i\\) is missing, we use this formula:\n" +
                            "$$x_i = x_f - v_{av} \\cdot (t_f - t_i)$$\n" +
                            "SOLUTION: <br>\n" +
                            "$$x_i = 9000m - 100{m \\over s} \\cdot (190s - 100s)$$\n" +
                            "$$x_i = 9000m - 100{m \\over s} \\cdot 90s$$\n" +
                            "$$x_i = 9000m - 9000m$$\n" +
                            "$$x_i = 0m$$\n" +
                            "ANSWER: <br>\n" +
                            "$$0m$$\n","Average Velocity"},
                    {"Final Position","QUESTION: <br>\n" +
                            "During a circuit race, car #95 did his 2nd lap which started at 100 seconds and finished at 190 seconds. The car had an average velocity of 100 meters per second. What is the car’s final position if he started from the lap line (0 meters)? <br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$v_{av} = 100{m \\over s}$$\n" +
                            "$$x_i = 0m$$\n" +
                            "$$x_f = ?$$\n" +
                            "$$t_i = 100s$$\n" +
                            "$$t_f = 190s$$\n" +
                            "Since \\(x_f\\) is missing, we use this formula:\n" +
                            "$$x_f = v_{av} \\cdot (t_f - t_f) + x_i$$\n" +
                            "SOLUTION: <br>\n" +
                            "$$x_i = 100{m \\over s} \\cdot (190s - 100s) + 0m$$\n" +
                            "$$x_i = 100{m \\over s} \\cdot 90s + 0m$$\n" +
                            "$$x_i = 9000m + 0m$$\n" +
                            "$$x_i = 9000m$$\n" +
                            "ANSWER: <br>\n" +
                            "$$9000m$$\n","Average Velocity"},
                    {"Initial Time","QUESTION: <br>\n" +
                            "During a circuit race, car #95 did his 2nd lap which ended at 190 seconds. The car had an average velocity of 100 meters per second. What time did the 2nd lap ended if one lap is 9000 meters long and the car started from the lap line (0 meters)? <br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$v_{av} = 100{m \\over s}$$\n" +
                            "$$x_i = 0m$$\n" +
                            "$$x_f = 9000m$$\n" +
                            "$$t_i = ?$$\n" +
                            "$$t_f = 190s$$\n" +
                            "Since \\(t_i\\) is missing, we use this formula:\n" +
                            "$$t_i = t_f - {{x_f - x_i} \\over {v_{av}}}$$\n" +
                            "SOLUTION: <br>\n" +
                            "$$t_i = {190s - {{9000m - 0m} \\over {100{m \\over s}}}}$$\n" +
                            "$$t_i = {190s - {{9000m} \\over {100{m \\over s}}}}$$\n" +
                            "$$t_i = 190s - 90s$$\n" +
                            "$$t_i = 100s$$\n" +
                            "ANSWER: <br>\n" +
                            "$$100s$$\n","Average Velocity"},
                    {"Final Time","QUESTION: <br>\n" +
                            "During a circuit race, car #95 did his 2nd lap which started at 100 seconds. The car had an average velocity of 100 meters per second. What time did the 2nd lap ended if one lap is 9000 meters long and the car started from the lap line (0 meters)? <br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$v_{av} = 100{m \\over s}$$\n" +
                            "$$x_i = 0m$$\n" +
                            "$$x_f = 9000m$$\n" +
                            "$$t_i = 100s$$\n" +
                            "$$t_f = ?$$\n" +
                            "Since \\(t_f\\) is missing, we use this formula:\n" +
                            "$$t_f = {{x_f - x_i} \\over {v_{av}}} + t_i$$\n" +
                            "SOLUTION: <br>\n" +
                            "$$t_f = {{9000m - 0m} \\over 100{m \\over s}} + 100s$$\n" +
                            "$$t_f = {9000m \\over 100{m \\over s}} + 100s$$\n" +
                            "$$t_f = 90s + 100s$$\n" +
                            "$$t_f = 190s$$\n" +
                            "ANSWER: <br>\n" +
                            "$$190s$$\n","Average Velocity"},

                    {"Acceleration", "QUESTION: <br>\n" +
                            "In the latest test run on Jeremy Clarkson's new car, the car took under 5 seconds to go from 0 meters per second to 100 meters per second. How much acceleration does the car have? <br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$a = ?$$\n" +
                            "$$v_i = 0{m \\over s}$$\n" +
                            "$$v_f = 100{m \\over s}$$\n" +
                            "$$t = 5s$$\n" +
                            "Since \\(a\\) is missing, we use this formula:\n" +
                            "$$a = {{v_f - v_i} \\over {t}}$$\n" +
                            "SOLUTION: <br>\n" +
                            "$$a = {{100{m \\over s} - 0{m \\over s}} \\over {5s}}$$\n" +
                            "$$a = {100{m \\over s} \\over 5s}$$\n" +
                            "$$a = 20{{m} \\over {s^2}}$$\n" +
                            "ANSWER: <br>\n" +
                            "$$20{{m} \\over {s^2}}$$\n", "Acceleration"},
                    {"Initial Velocity", "QUESTION: <br>\n" +
                            "In the last test run on Jeremy Clarkson's new car, the car had an acceleration of 20 meters per second squared. He then was going at 100 meters per second in 5 seconds. How fast was he going at time 0 (seconds)? <br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$a = 20{{m} \\over {s^2}}$$\n" +
                            "$$v_i = ?$$\n" +
                            "$$v_f = 0{m \\over s}$$\n" +
                            "$$t = 5s$$\n" +
                            "Since \\(v_i\\) is missing, we use this formula:\n" +
                            "$$v_i = v_f - a \\cdot t$$\n" +
                            "SOLUTION: <br>\n" +
                            "$$v_i = {100{m \\over s} - 20{{m} \\over {s^2}} \\cdot 5s}$$\n" +
                            "$$v_i = 100{m \\over s} - 100{m \\over s}$$\n" +
                            "$$v_i = 0{m \\over s} $$\n" +
                            "ANSWER: <br>\n" +
                            "$$0{m \\over s}$$\n", "Acceleration"},
                    {"Final Velocity", "QUESTION: <br>\n" +
                            "In the last test run on Jeremy Clarkson's new car, the car had an acceleration of 20 meters per second squared. How fast was the car going after 5 seconds if the car started with a velocity of 0 meters per second? <br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$a = 20{{m} \\over {s^2}}$$\n" +
                            "$$v_i = 0{m \\over s}$$\n" +
                            "$$v_f = ?$$\n" +
                            "$$t = 5s$$\n" +
                            "Since \\(v_f\\) is missing, we use this formula:\n" +
                            "$$v_f = a \\cdot t + v_i$$\n" +
                            "SOLUTION: <br>\n" +
                            "$$v_f = 20{{m} \\over {s^2}} \\cdot 5s + 0{m \\over s}$$\n" +
                            "$$v_f = 100{m \\over s} + 0{m \\over s}$$\n" +
                            "$$v_f = 100{m \\over s}$$\n" +
                            "ANSWER: <br>\n" +
                            "$$100m/s$$\n", "Acceleration"},
                    {"Time Interval", "QUESTION: <br>\n" +
                            "In the last test run of Jeremy Clarkson's new car, the car went from 0 meters per second to 100 meters per second with an acceleration of 20 meters per second squared. How long did it took the car to go from 0 meters per second to 100 meters per second? <br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$a = 20{{m} \\over {s^2}}$$\n" +
                            "$$v_i = 0{m \\over s}$$\n" +
                            "$$v_f = 100{m \\over s}$$\n" +
                            "$$t = ?$$\n" +
                            "Since \\(t\\) is missing, we use this formula:\n" +
                            "$$t = {{v_f - v_i} \\over {a}}$$\n" +
                            "SOLUTION: <br>\n" +
                            "$$t = {{100{m \\over s} - 0{m \\over s}} \\over 20{{m} \\over {s^2}}}$$\n" +
                            "$$t = {100{m \\over s} \\over 20{{m} \\over {s^2}}}$$\n" +
                            "$$t = 5s$$\n" +
                            "ANSWER: <br>\n" +
                            "$$5s$$\n", "Acceleration"},

                    {"Average Acceleration", "QUESTION: <br>\n" +
                            "A boy was running while his friend kept track of the time. The boy was running with a speed of 3 meters per second at a time of 10 seconds. He reached a speed of 5 meters per second at a time of 15 seconds. <br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$a_{av} = ?$$\n" +
                            "$$v_i = 3{m \\over s}$$\n" +
                            "$$v_f = 5{m \\over s}$$\n" +
                            "$$t_i = 10s$$\n" +
                            "$$t_f = 15s$$\n" +
                            "Since \\(a_{av}\\) is missing, we use this formula:\n" +
                            "$$a_{av} = {{v_f - v_i} \\over {t_f - t_i}}$$\n" +
                            "SOLUTION: <br>\n" +
                            "$$a_{av} = {{5{m \\over s} - 3{m \\over s}} \\over {15s - 10s}}$$\n" +
                            "$$a_{av} = {2{m \\over s} \\over 5s}$$\n" +
                            "$$a_{av} = 0.4{{m} \\over {s^2}}$$\n" +
                            "ANSWER: <br>\n" +
                            "$$0.4{{m} \\over {s^2}}$$\n", "Average Acceleration"},
                    {"Initial Velocity", "QUESTION: <br>\n" +
                            "A boy was running while his friend kept track of the time. He reached a speed of 5 meters per second at a time of 15 seconds. What is the boy’s initial velocity if he has an acceleration of 0.4 meters per second squared and if his friend starts the timer at time 10 seconds? <br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$a_{av} = 0.4{{m} \\over {s^2}}$$\n" +
                            "$$v_i = ?$$\n" +
                            "$$v_f = 5{m \\over s}$$\n" +
                            "$$t_i = 10s$$\n" +
                            "$$t_f = 15s$$\n" +
                            "Since \\(v_i\\) is missing, we use this formula:\n" +
                            "$$v_i = v_f - a_{av} \\cdot (t_f - t_i)$$\n" +
                            "SOLUTION: <br>\n" +
                            "$$v_i = 5{m \\over s} - (0.4{{m} \\over {s^2}} \\cdot (15s - 10s))$$\n" +
                            "$$v_i = 5{m \\over s} - (0.4{{m} \\over {s^2}} \\cdot (5s))$$\n" +
                            "$$v_i = 5{m \\over s} - 2{m \\over s}$$\n" +
                            "$$v_i = 3{m \\over s}$$\n" +
                            "ANSWER: <br>\n" +
                            "$$3{m \\over s} $$\n", "Average Acceleration"},
                    {"Final Velocity", "QUESTION: <br>\n" +
                            "A boy was running while his friend kept track of the time. The boy was running with a speed of 3 meters per second at a time of 10 seconds. What is the boy’s final velocity if he has an acceleration of 0.4 meters per second squared and if his friend stopped the timer at time 15 seconds? <br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$a_{av} = 0.4{{m} \\over {s^2}}$$\n" +
                            "$$v_i = 3{m \\over s}$$\n" +
                            "$$v_f = ?$$\n" +
                            "$$t_i = 10s$$\n" +
                            "$$t_f = 15s$$\n" +
                            "Since \\(v_f\\) is missing, we use this formula:\n" +
                            "$$v_f = a_{av} \\cdot (t_f - t_i) + v_i$$\n" +
                            "SOLUTION: <br>\n" +
                            "$$v_f = 0.4{{m} \\over {s^2}} \\cdot (15s - 10s) + 3{m \\over s}$$\n" +
                            "$$v_f = 0.4{{m} \\over {s^2}} \\cdot 5s + 3{m \\over s}$$\n" +
                            "$$v_f = 2{m \\over s} + 3{m \\over s} $$\n" +
                            "$$v_f = 5{m \\over s} $$\n" +
                            "ANSWER: <br>\n" +
                            "$$5{m \\over s}$$\n", "Average Acceleration"},
                    {"Initial Time", "QUESTION: <br>\n" +
                            "A boy was running while his friend kept track of the time. He reached a speed of 5 meters per second at a time of 15 seconds. What was the time when the boy was running at a speed of 3 meters per second and an acceleration of 0.4 meters per second squared? <br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$a_{av} = 0.4{{m} \\over {s^2}}$$\n" +
                            "$$v_i = 3{m \\over s}$$\n" +
                            "$$v_f = 5{m \\over s}$$\n" +
                            "$$t_i = ?$$\n" +
                            "$$t_f = 15s$$\n" +
                            "Since \\(t_i\\) is missing, we use this formula:\n" +
                            "$$t_i = t_f - {{v_f - v_i} \\over {a_{av}}}$$\n" +
                            "SOLUTION: <br>\n" +
                            "$$t_i = 15s - {{5{m \\over s} - 3{m \\over s}} \\over 0.4{{m} \\over {s^2}}}$$\n" +
                            "$$t_i = 15s - {{2{m \\over s}} \\over 0.4{{m} \\over {s^2}}}$$\n" +
                            "$$t_i = 15s - 5s$$\n" +
                            "$$t_i = 10s$$\n" +
                            "ANSWER: <br>\n" +
                            "$$10s$$\n", "Average Acceleration"},
                    {"Final Time", "QUESTION: <br>\n" +
                            "A boy was running while his friend kept track of the time. The boy was running with a speed of 3 meters per second at a time of 10 seconds. What was the time when the boy was running with a speed of 5 meters per second and an acceleration of 0.4 meters per second squared? <br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$a_{av} = 0.4{{m} \\over {s^2}}$$\n" +
                            "$$v_i = 3{m \\over s}$$\n" +
                            "$$v_f = 5{m \\over s}$$\n" +
                            "$$t_i = 10s$$\n" +
                            "$$t_f = ?$$\n" +
                            "Since \\(t_f\\) is missing, we use this formula:\n" +
                            "$$t_f = {{v_f - v_i} \\over {a_{av}}} + t_i$$\n" +
                            "SOLUTION: <br>\n" +
                            "$$t_f = {{5{m \\over s} - 3{m \\over s}} \\over 0.4{{m} \\over {s^2}}} + 10s$$\n" +
                            "$$t_f = {{2{m \\over s}} \\over 0.4{{m} \\over {s^2}}} + 10s$$\n" +
                            "$$t_f = 5s + 10s$$\n" +
                            "$$t_f = 15s$$\n" +
                            "ANSWER: <br>\n" +
                            "$$15s$$\n", "Average Acceleration"},

                    {"Velocity", "QUESTION: <br>\n" +
                            "Steve threw a bottle outside his office window from the top of a skyscraper with a velocity of 1 meter per second. What is the velocity of the bottle 5 seconds after he threw the bottle? <br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$v_y = ?$$\n" +
                            "$$u = 1{m \\over s}$$\n" +
                            "$$t = 5s$$\n" +
                            "Since \\(v_y\\) is missing, we use this formula:\n" +
                            "$$v_y = u + g \\cdot t$$\n" +
                            "SOLUTION: <br>\n" +
                            "$$v_y = 1{m \\over s} + 9.8{{m} \\over {s^2}} \\cdot 5s$$\n" +
                            "$$v_y = 1{m \\over s} + 49{m \\over s}$$\n" +
                            "$$v_y = 50{m \\over s}$$\n" +
                            "ANSWER: <br>\n" +
                            "$$50{m \\over s}$$\n", "Free-fall Velocity"},
                    {"Initial Velocity", "QUESTION: <br>\n" +
                            "Steve threw a bottle outside his office window from the top of a skyscraper. What is the initial velocity of the bottle if it was going at 50 meters per second 5 seconds after he threw the bottle? <br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$v_y = 50{m \\over s}$$\n" +
                            "$$u = ?$$\n" +
                            "$$t = 5s$$\n" +
                            "Since \\(u\\) is missing, we use this formula:\n" +
                            "$$u = v_y - g \\cdot t$$\n" +
                            "SOLUTION: <br>\n" +
                            "$$u = 50{m \\over s} - 9.8{{m} \\over {s^2}} \\cdot 5s$$\n" +
                            "$$u = 50{m \\over s} - 49{m \\over s}$$\n" +
                            "$$u = 1{m \\over s}$$\n" +
                            "ANSWER: <br>\n" +
                            "$$1{m \\over s}$$\n", "Free-fall Velocity"},
                    {"Time", "QUESTION: <br>\n" +
                            "Steve threw a bottle outside his office window from the top of a skyscraper with an initial velocity of 1 meter per second. At what time will the bottle's velocity be 50 meters per second. <br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$v_y = 50{m \\over s}$$\n" +
                            "$$u = 1{m \\over s}$$\n" +
                            "$$t = ?$$\n" +
                            "Since \\(t\\) is missing, we use this formula:\n" +
                            "$$t = {{v_y - u} \\over {g}}$$\n" +
                            "SOLUTION: <br>\n" +
                            "$$t = {{50{m \\over s}  - 1{m \\over s}} \\over {9.8{{m} \\over {s^2}}}} $$\n" +
                            "$$t = {{49{m \\over s}} \\over {9.8{{m} \\over {s^2}}}} $$\n" +
                            "$$t = 5s$$\n" +
                            "ANSWER: <br>\n" +
                            "$$5s$$\n", "Free-fall Velocity"},

                    {"Displacement", "QUESTION: <br>\n" +
                            "A ball rolled over from the top of a building. It then hit the ground 10 seconds after. How high was the building? <br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$y = ?$$\n" +
                            "$$t = 10s$$\n" +
                            "Since \\(y\\) is missing, we use this formula:\n" +
                            "$$y = {1 \\over 2} \\cdot g \\cdot t^2$$\n" +
                            "SOLUTION: <br>\n" +
                            "$$y = {1 \\over 2} \\cdot 9.8{{m} \\over {s^2}} \\cdot (10s)^2$$\n" +
                            "$$y = 4.9{{m} \\over {s^2}} \\cdot 100s^2$$\n" +
                            "$$y = 490m$$\n" +
                            "ANSWER: <br>\n" +
                            "$$490m$$\n", "Free-fall Displacement"},
                    {"Time", "QUESTION: <br>\n" +
                            "A ball rolled over from the top of a building. How long did it take for the ball to hit the ground if the building was 490 meters high? <br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$y = 490m$$\n" +
                            "$$t = ?$$\n" +
                            "Since \\(t\\) is missing, we use this formula:\n" +
                            "$$t = \\sqrt{2 \\cdot {y \\over g}}$$\n" +
                            "SOLUTION: <br>\n" +
                            "$$t = {\\sqrt{2 \\cdot {{490m} \\over {9{{m} \\over {s^2}}}}}}$$\n" +
                            "$$t = {\\sqrt{2 \\cdot 50s^2}}$$\n" +
                            "$$t = {\\sqrt{100s^2}}$$\n" +
                            "$$t = 10s$$\n" +
                            "ANSWER: <br>\n" +
                            "$$10s$$\n", "Free-fall Displacement"},

                    {"Final Velocity", "QUESTION: <br>\n" +
                            "A baseball player threw his ball straight up in the sky with an initial velocity of 5 meters per second and went 3.8265 meters high. What was the velocity of the ball just before the baseball player catches it back in his hand? <br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$v_f = ?$$\n" +
                            "$$v_i = 5{m \\over s}$$\n" +
                            "$$d = 3.8265m$$\n" +
                            "Since \\(v_f\\) is missing, we use this formula:\n" +
                            "$$v_f = \\sqrt{v_i^2 + 2 \\cdot g \\cdot d}$$\n" +
                            "SOLUTION: <br>\n" +
                            "$$v_f = {\\sqrt{5{m \\over s}^2 + 2 \\cdot 9.8{{m} \\over {s^2}} \\cdot 5m}}$$\n" +
                            "$$v_f = {\\sqrt{25{{m^2} \\over {s^2}} + 19.6{{m} \\over {s^2}} \\cdot 5m}}$$\n" +
                            "$$v_f = {\\sqrt{25{{m^2} \\over {s^2}} + 75{m^2 \\over s^2} }}$$\n" +
                            "$$v_f = {\\sqrt{100{{m^2} \\over {s^2}}}}$$\n" +
                            "$$v_f = 10{m \\over s}$$\n" +
                            "ANSWER: <br>\n" +
                            "$$10{m \\over s}$$\n", "Free-fall Upward Motion"},
                    {"Initial Velocity", "QUESTION: <br>\n" +
                            "A baseball player threw a ball straight up in the sky 3.8265 meters high. What is the balls initial velocity if its velocity just before the baseball player catches it back in his hand was 10 meters per second. <br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$v_f = 10{m \\over s}$$\n" +
                            "$$v_i = ?$$\n" +
                            "$$d = 3.8265m$$\n" +
                            "Since \\(v_i\\) is missing, we use this formula:\n" +
                            "$$v_i = \\sqrt{v_f^2 - 2 \\cdot g \\cdot d}$$\n" +
                            "SOLUTION: <br>\n" +
                            "$$v_i = {sqrt((10{m \\over s})^2 - ((2 \\cdot 9.8{{m} \\over {s^2}}) \\cdot 3.8265m))}$$\n" +
                            "$$v_i = {sqrt(100{{m^2} \\over {s^2}}- (19.6{{m} \\over {s^2}}  \\cdot 3.8265m))}$$\n" +
                            "$$v_i = {sqrt(100{{m^2} \\over {s^2}} - 75{m^2 \\over s^2})}$$\n" +
                            "$$v_i = {sqrt(25{{m^2} \\over {s^2}})}$$\n" +
                            "$$v_i = 5{m \\over s}$$\n" +
                            "ANSWER: <br>\n" +
                            "$$5{m \\over s}$$\n", "Free-fall Upward Motion"},
                    {"Distance", "QUESTION: <br>\n" +
                            "A baseball player throws a ball straight up in the sky with an initial velocity of 5 meters per second. How high did the ball go if the velocity just before the baseball player catches it back in his hand was 10 meters per second? <br><br>\n" +
                            "$$v_f = 10{m \\over s}$$\n" +
                            "$$v_i = 5{m \\over s}$$\n" +
                            "$$d = ?$$\n" +
                            "Since \\(d\\) is missing, we use this formula:\n" +
                            "$$d = {{v_f^2 - v_i^2} \\over {2 \\cdot g}}$$\n" +
                            "SOLUTION: <br>\n" +
                            "$$d = {{10{m \\over s}^2 - 5{m \\over s}^2)} \\over {2 \\cdot 9.8{{m} \\over {s^2}}}}$$\n" +
                            "$$d = {{100{{m^2} \\over {s^2}}- 25{{m^2} \\over {s^2}}} \\over {19.6m/s^2}}$$\n" +
                            "$$d = {{75{{m^2} \\over {s^2}}} \\over {19.6{{m} \\over {s^2}}}}$$\n" +
                            "$$d = 3.8265m$$\n" +
                            "ANSWER: <br>\n" +
                            "$$3.8265m$$\n", "Free-fall Upward Motion"},

                    {"Components of a Projectile", "QUESTION: <br>\n" +
                            "Andrew accidentally threw his cellphone from the edge of the cliff with an initial velocity along the x-axis of 4 meters per second, and an initial velocity along the y-axis of 1 meter per second. Find the projectile’s (a) horizontal distance, (b) horizontal velocity, (c) vertical distance, and (d) vertical velocity 3 seconds after Bob’s throw. <br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$v_x = 4{m \\over s}$$\n" +
                            "$$v_{yi} = 1{{m} \\over {s^2}}$$\n" +
                            "$$t = 3s$$\n" +
                            "$$x = ?$$\n" +
                            "$$y = ?$$\n" +
                            "$$v_y = ?$$\n" +
                            "To find the horizontal distance, use this formula:\n" +
                            "$$x = v_x \\cdot t$$\n" +
                            "To find the vertical distance, use this formula:\n" +
                            "$$y = v_{yi} \\cdot t + {1 \\over 2} \\cdot g \\cdot t^2$$\n" +
                            "To find the vertical velocity at a certain time, use this formula:\n" +
                            "$$v_y = g \\cdot t + v_{yi}$$\n" +
                            "SOLUTION: <br>\n" +
                            "(a) <br>\n" +
                            "$$x = 4{m \\over s} \\cdot 3s$$\n" +
                            "$$x = 12m$$\n" +
                            "(b) <br>\n" +
                            "$$4{m \\over s}$$\n" +
                            "(c) <br>\n" +
                            "$$y = 1{m \\over s} \\cdot 3s + {1 \\over 2} \\cdot 9.8{{m} \\over {s^2}} \\cdot 3s^2$$\n" +
                            "$$y = 3m + 4.9{{m} \\over {s^2}} \\cdot 9s^2$$\n" +
                            "$$y = 3m + 44.1m$$\n" +
                            "$$y = 47.1m$$\n" +
                            "(d) <br>\n" +
                            "$$v_y = 9.8{{m} \\over {s^2}} \\cdot 3s + 1{m \\over s}$$\n" +
                            "$$v_y = 29.4{m \\over s} + 1{m \\over s}$$\n" +
                            "$$v_y = 30.4{m \\over s}$$\n" +
                            "ANSWER: <br>\n" +
                            "(a) <br>\n" +
                            "$$12m$$\n" +
                            "(b) <br>\n" +
                            "$$4{m \\over s}$$ since the initial velocity along the x-axis never changes. <br><br>\n" +
                            "(c) <br>\n" +
                            "$$47.1m$$\n" +
                            "(d) <br>\n" +
                            "$$30.4{m \\over s}$$\n" +
                            "<i>Note: In cases where the time is the one being computed, we can use any of the above equations depending on what values are given in the problem.</i>\n", "Projectile Motion"},

                    {"Components of a Trajectory", "QUESTION: <br>\n" +
                            "A cannon was slanted 30 degrees parallel to the ground. It then launched a ball with an initial velocity of 40 meters per second. Find the trajectory’s (a) time of flight, (b) maximum height reached, and (c) horizontal range. <br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$\\theta = 30$$\n" +
                            "$$v_i = 40{m \\over s}$$\n" +
                            "$$t = ?$$\n" +
                            "$$H = ?$$\n" +
                            "$$R = ?$$\n" +
                            "To find the time of flight, use this formula:\n" +
                            "$$t = {{2 \\cdot v_i \\cdot sin \\theta} \\over {g}}$$\n" +
                            "To find the maximum height reached, use this formula:\n" +
                            "$$H = {{v_i^2 \\cdot sin^2 \\theta} \\over {2 \\cdot g}}$$\n" +
                            "To find the horizontal range, use this formula:\n" +
                            "$$R = {{v_i^2 \\cdot sin2 \\theta} \\over {g}}$$\n" +
                            "SOLUTION: <br>\n" +
                            "(a) <br>\n" +
                            "$$t = {{2 \\cdot 40{m \\over s} * sin30} \\over {9.8{{m} \\over {s^2}}}}$$\n" +
                            "$$t = {{80{m \\over s} \\cdot 0.5} \\over {9.8{{m} \\over {s^2}}}}<br>\n" +
                            "$$t = {{40{m \\over s} \\over {9.8{{m} \\over {s^2}}}}$$\n" +
                            "$$t = 4.0816s $$\n" +
                            "(b) <br>\n" +
                            "$$H = {{40{m \\over s}^2 \\cdot sin^{2}30} \\over {2 \\cdot 9.8{{m} \\over {s^2}}}}$$\n" +
                            "$$H = {{1600{{m^2} \\over {s^2}} \\cdot 0.25} \\over {19.6{{m} \\over {s^2}}}}$$\n" +
                            "$$H = {{400{{m^2} \\over {s^2}} } \\over {19.6{{m} \\over {s^2}}}}$$$$\n" +
                            "$$H = 20.4082m$$\n" +
                            "(c) <br>\n" +
                            "$$R = {{40{m \\over s}^2 \\cdot sin(2 \\cdot 30)} \\over {9.8{{m} \\over {s^2}}}}$$\n" +
                            "$$R = {{1600{{m^2 }\\over {s^2 }} \\cdot sin60} \\over {9.8{{m} \\over {s^2}}}}$$\n" +
                            "$$R = {{1385.6{{m^2 }\\over {s^2 }}} \\over {9.8{{m} \\over {s^2}}}}$$\n" +
                            "$$R = 141.3878m$$\n" +
                            "ANSWER: <br>\n" +
                            "(a) <br>\n" +
                            "$$4.0816s$$\n" +
                            "(b) <br>\n" +
                            "$$20.4082m$$\n" +
                            "(c) <br>\n" +
                            "$$141.3878m$$\n" +
                            "<i>Note: In cases where the initial velocity and angle of trajectory is the one being computed, we can use any of the above equations depending on what values are given in the problem.</i>\n", "Trajectory"},

                    {"Frictional Force", "QUESTION: <br>\n" +
                            "A wooden pallet carrying a load with a normal force of 5800 Newton rests on a wooden floor. A forklift driver decides to push it without lifting it. What force must be applied to just get the pallet moving? (The friction coefficient value of wood is 0.28) <br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$F_f = ?$$\n" +
                            "$$\\mu = 0.28$$\n" +
                            "$$F_n = 5800N$$\n" +
                            "Since \\(F_f\\) is missing, we use this formula:\n" +
                            "$$F_f = \\mu \\cdot F_n$$\n" +
                            "SOLUTION: <br>\n" +
                            "$$F_f = 0.28 \\mu \\cdot 5800N$$\n" +
                            "$$F_f = 1624N$$\n" +
                            "ANSWER: <br>\n" +
                            "$$1624N$$\n", "Friction"},
                    {"Coefficient of Friction", "QUESTION: <br>\n" +
                            "A wooden pallet carrying a load with a normal force of 5800 Newton rests on a wooden floor. A forklift driver decides to push it without lifting it. What is the friction coefficient value of the wood if the forklift driver needed a force of 1624 Newton to push the wooden pallet? <br><br>\n" +
                            "$$F_f = 1624N$$\n" +
                            "$$\\mu = ?$$\n" +
                            "$$F_n = 5800N$$\n" +
                            "Since \\(\\mu \\) is missing, we use this formula:\n" +
                            "$$\\mu = {F_f \\over F_n}$$\n" +
                            "SOLUTION: <br>\n" +
                            "$$\\mu = {1624N \\over 5800N}$$\n" +
                            "$$0.28$$\n" +
                            "ANSWER: <br>\n" +
                            "$$0.28$$\n", "Friction"},
                    {"Normal Force", "QUESTION: <br>\n" +
                            "A wooden pallet carrying a load rests on a wooden floor. A forklift driver decides to push it without lifting it. What is the normal force of the wood pallet if the forklift driver needed a force of 1624 Newton to push the wooden pallet? (The friction coefficient value of wood is 0.28) <br><br>\n" +
                            "$$F_f = 1624N$$\n" +
                            "$$\\mu = 0.28$$\n" +
                            "$$F_n = $$\n" +
                            "Since \\(F_n\\) is missing, we use this formula:\n" +
                            "$$F_n = {F_f \\over \\mu}$$\n" +
                            "SOLUTION: <br>\n" +
                            "$$F_n = {1624 \\over 0.28}$$\n" +
                            "$$F_n = 5800N$$\n" +
                            "ANSWER: <br>\n" +
                            "$$5800N$$\n", "Friction"},

                    {"Momentum", "QUESTION: <br>\n" +
                            "A body is moving with a speed of 5 meters per second. What is the body’s momentum if it has a mass of 10 kilograms? <br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$p = ?$$\n" +
                            "$$m = 10kg$$\n" +
                            "$$v = 5{m \\over s}$$\n" +
                            "Since \\(p\\) is missing, we use this formula:\n" +
                            "$$p = m \\cdot v$$\n" +
                            "SOLUTION: <br>\n" +
                            "$$p = 10kg \\cdot 5{m \\over s}$$\n" +
                            "$$p = 50 {kgm \\over s}$$\n" +
                            "$$p = 50N s$$\n" +
                            "ANSWER: <br>\n" +
                            "$$50N$$\n", "Momentum"},
                    {"Mass", "QUESTION: <br>\n" +
                            "A body is moving with a speed of 5 meters per second. What is the body’s mass if it has a momentum of 50 Newton seconds? <br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$p = 50Ns$$\n" +
                            "$$m = ?$$\n" +
                            "$$v = 5{m \\over s}$$\n" +
                            "Since \\(m\\) is missing, we use this formula:\n" +
                            "$$m = {p \\over v}$$\n" +
                            "SOLUTION: <br>\n" +
                            "$$m = {{50N s} \\over {5{m \\over s}}}$$\n" +
                            "$$m = {{50{kgm \\over s}} \\over {5{m \\over s}}}$$\n" +
                            "$$m = 10kg$$\n" +
                            "ANSWER: <br>\n" +
                            "$$10kg$$\n", "Momentum"},
                    {"Velocity", "QUESTION: <br>\n" +
                            "A body has a momentum of 50 Newton seconds and a mass of 10 kilograms. What is its velocity? <br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$p = 50Ns$$\n" +
                            "$$m = 10kg$$\n" +
                            "$$v = ?$$\n" +
                            "Since \\(v\\) is missing, we use this formula:\n" +
                            "$$v = {p \\over m}$$\n" +
                            "SOLUTION: <br>\n" +
                            "$$v = {{50N s} \\over {10kg}}$$\n" +
                            "$$v = {{50{kgm \\over s}} \\over {10kg}}$$\n" +
                            "$$v = 5{m \\over s}$$\n" +
                            "ANSWER: <br>\n" +
                            "$$5{m \\over s}$$\n", "Momentum"},

                    {"Work", "QUESTION: <br>\n" +
                            "How much work is done against a cart when a force of 50 Newton parallel to the ground pushes it 100 meters away? <br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$W = ?$$\n" +
                            "$$\\vec F = 50N$$\n" +
                            "$$\\vec x = 100m$$\n" +
                            "$$\\theta = 0$$\n" +
                            "Since \\(W\\) is missing, we use this formula:\n" +
                            "$$W = \\vec{F} \\cdot \\vec{x} \\cdot cos \\theta$$\n" +
                            "SOLUTION: <br>\n" +
                            "$$W = 50N \\cdot 100m \\cdot cos0$$\n" +
                            "$$W = 50{kgm \\over s^2} \\cdot 100m \\cdot 1$$\n" +
                            "$$W = 5000{kgm^2 \\over s^2} \\cdot 1$$\n" +
                            "$$W = 5000{kgm^2 \\over s^2}$$\n" +
                            "$$W = 5000J$$\n" +
                            "ANSWER: <br>\n" +
                            "$$5000J$$\n", "Work"},
                    {"Force", "QUESTION: <br>\n" +
                            "How much force parallel to the ground is applied to a cart if it was pushed 100 meters away with a work equal to 5000 joules? <br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$W = 5000J$$\n" +
                            "$$\\vec{F} = ?$$\n" +
                            "$$\\vec{x} = 100m$$\n" +
                            "$$\\theta = 0$$\n" +
                            "Since \\(\\vec{F}\\) is missing, we use this formula:\n" +
                            "$$\\vec{F} = {{W} \\over {\\vec{x} \\cdot cos \\theta}}$$\n" +
                            "SOLUTION: <br>\n" +
                            "$$\\vec{F} = {{5000J} \\over {100m \\cdot cos0}}$$\n" +
                            "$$\\vec{F} = {{5000{kgm^2 \\over s^2}} \\over {100m \\cdot 1}}$$\n" +
                            "$$\\vec{F} = {{5000{kgm^2 \\over s^2}} \\over {100m}}$$\n" +
                            "$$\\vec{F} = {500{kgm \\over s^2}}$$\n" +
                            "$$\\vec{F} = 50N$$\n" +
                            "ANSWER: <br>\n" +
                            "$$50N$$\n", "Work"},
                    {"Distance", "QUESTION: <br>\n" +
                            "How far was a cart pushed away when a force of 50 Newton parallel to the ground and a work of 5000 joules was applied to it? <br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$W = 5000J$$\n" +
                            "$$\\vec{F} = 50N$$\n" +
                            "$$\\vec{x} = ?$$\n" +
                            "$$\\theta = 0$$\n" +
                            "Since \\(\\vec{x}\\) is missing, we use this formula:\n" +
                            "$$\\vec{x} = {{W} \\over {\\vec F \\cdot cos \\theta}}$$\n" +
                            "SOLUTION: <br>\n" +
                            "$$\\vec{x} = {{5000J} \\over {50N \\cdot cos0}}$$\n" +
                            "$$\\vec{x} = {{5000{kgm^2 \\over s^2}} \\over {50{kgm \\over s^2} \\cdot 1}}$$\n" +
                            "$$\\vec{x} = {{5000{kgm^2 \\over s^2}} \\over {50{kgm \\over s^2}}}$$\n" +
                            "$$\\vec{x} = 100m$$\n" +
                            "ANSWER: <br>\n" +
                            "$$100m$$\n", "Work"},

                    {"Kinetic Energy", "QUESTION: <br>\n" +
                            "Andy Murray serves a tennis ball with a velocity of 35 meters per second. If the ball has a mass of 0.15 kilogram, what is the kinetic energy of the ball? <br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$KE = ?$$\n" +
                            "$$m = 0.15kg$$\n" +
                            "$$v = 35{m \\over s}$$\n" +
                            "Since \\(KE\\) is missing, we use this formula:\n" +
                            "$$KE = {{m \\cdot v^2} \\over {2}}$$\n" +
                            "SOLUTION: <br>\n" +
                            "$$KE = {{0.15kg \\cdot {35{m \\over s}}^2} \\over {2}}$$\n" +
                            "$$KE = {{0.15kg \\cdot 1225{{m^2} \\over {s^2}}} \\over {2}}$$\n" +
                            "$$KE = {{183.75{{kgm^2} \\over {s^2}}} \\over {2}}$$\n" +
                            "$$KE = {91.875{{kgm^2} \\over {s^2}}}$$\n" +
                            "$$KE = 91.875J$$\n" +
                            "ANSWER: <br>\n" +
                            "$$91.875J$$\n", "Kinetic Energy"},
                    {"Mass", "QUESTION: <br>\n" +
                            "Andy Murray serves a tennis ball with a velocity of 35 meters per second. If the ball has a kinetic energy of 91.875 joules, what is the mass of the ball? <br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$KE = 91.875J$$\n" +
                            "$$m = ?$$\n" +
                            "$$v = 35{m \\over s}$$\n" +
                            "Since \\(m\\) is missing, we use this formula:\n" +
                            "$$m = {{2 \\cdot KE} \\over {v^2}}$$\n" +
                            "SOLUTION: <br>\n" +
                            "$$m = {{2 \\cdot 91.875J} \\over {35{m \\over s}^2}}$$\n" +
                            "$$m = {{183.75{{kgm^2} \\over {s^2}}} \\over 1225{{m^2} \\over {s^2}}}$$\n" +
                            "$$m = 0.15kg$$\n" +
                            "ANSWER: <br>\n" +
                            "$$0.15kg$$\n", "Kinetic Energy"},
                    {"Velocity", "QUESTION: <br>\n" +
                            "Andy Murray serves a tennis ball that has a mass of 0.15 kilograms. If the ball has a kinetic energy of 91.875, what is the velocity of the ball? <br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$KE = 91.875J$$\n" +
                            "$$m = 0.15kg $$\n" +
                            "$$v = ?$$\n" +
                            "Since \\(v\\) is missing, we use this formula:\n" +
                            "$$v = \\sqrt{{2 \\cdot KE} \\over {m}}$$\n" +
                            "SOLUTION: <br>\n" +
                            "$$v = \\sqrt{{2 \\cdot 91.875J} \\over {0.15kg}}$$\n" +
                            "$$v = \\sqrt{{183.75{{kgm^2} \\over {s^2}}} \\over {0.15kg}}$$\n" +
                            "$$v = \\sqrt{1225{{m^2} \\over {s^2}}}$$\n" +
                            "$$v = 35{m \\over s}$$\n" +
                            "ANSWER: <br>\n" +
                            "$$35{m \\over s}$$\n", "Kinetic Energy"},

                    {"Potential Energy", "QUESTION: <br>\n" +
                            "Andy Murray’s tennis ball has a mass of 0.3 kilograms. If he holds the ball above the ground at a height of 2 meters to serve, what is its gravitational potential energy? <br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$PE_g = ?$$\n" +
                            "$$m = 0.3kg$$\n" +
                            "$$h = 2m$$\n" +
                            "Since \\(PE_g\\) is missing, we use this formula:\n" +
                            "$$PE_g = m \\cdot g \\cdot h$$\n" +
                            "SOLUTION: <br>\n" +
                            "$$PE_g = 0.3kg \\cdot 9.8{{m} \\over {s^2}} \\cdot 2m$$\n" +
                            "$$PE_g = 0.3kg \\cdot 19.6{{m^2} \\over {s^2}}$$\n" +
                            "$$PE_g = 5.88{{kgm^2} \\over {s^2}}$$\n" +
                            "$$PE_g = 5.88J$$\n" +
                            "ANSWER: <br>\n" +
                            "$$5.88J$$\n", "Potential Energy"},
                    {"Mass", "QUESTION: <br>\n" +
                            "Andy Murray holds his ball above the ground at a height of 2 meters to serve. If the ball has a gravitational potential energy of 5.88 joules, what is its mass? <br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$PE_g = 5.88J$$\n" +
                            "$$m = ?$$\n" +
                            "$$h = 2m$$\n" +
                            "Since \\(m\\) is missing, we use this formula:\n" +
                            "$$m = {{PE_g} \\cdot {g \\cdot h}}$$\n" +
                            "SOLUTION: <br>\n" +
                            "$$m = {{5.88J} \\cdot {9.8{{m} \\over {s^2}} \\cdot 2m}}$$\n" +
                            "$$m = {{5.88{{kgm^2} \\cdot {s^2}}} \\cdot {19.6{{m^2} \\over {s^2}}}}$$\n" +
                            "$$m = 0.3kg$$\n" +
                            "ANSWER: <br>\n" +
                            "$$0.3kg$$\n", "Potential Energy"},
                    {"Height", "QUESTION: <br>\n" +
                            "Andy Murray’s tennis ball has a mass of 0.3 kilograms. How high does he hold his ball above the ground if the ball has a gravitational potential energy of 5.88 joules? <br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$PE_g = 5.88J$$\n" +
                            "$$m = 0.3kg$$\n" +
                            "$$h = ?$$\n" +
                            "Since \\(h\\) is missing, we use this formula:\n" +
                            "$$h = {{PE_g} \\cdot {m \\cdot g}}$$\n" +
                            "SOLUTION: <br>\n" +
                            "$$h = {{5.88J} \\over {0.3kg \\cdot 9.8{{m} \\cdot {s^2}}}}$$\n" +
                            "$$h = {{5.88{{kgm^2} \\over {s^2}}} \\over {2.94{{kgm} \\over {s^2}}}}$$\n" +
                            "$$h = 2m$$\n" +
                            "ANSWER: <br>\n" +
                            "$$2m$$\n", "Potential Energy"},

                    {"Average Power", "QUESTION: <br>\n" +
                            "How much power is required to do 200 joules of work in 5 seconds? <br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$P_{av} = ?$$\n" +
                            "$$\\Delta W = 200J$$\n" +
                            "$$\\Delta t = 5s$$\n" +
                            "Since \\(P_{av}\\) is missing, we use this formula:\n" +
                            "$$P_{av} = {{\\Delta W} \\over {\\Delta t}}$$\n" +
                            "SOLUTION: <br>\n" +
                            "$$P_{av} = {200J \\over 5s}$$\n" +
                            "$$P_{av} = {200{{kgm^2} \\over {s^2}} \\over 5s}$$\n" +
                            "$$P_{av} = 40{{kgm^2} \\over {s^3}}$$\n" +
                            "$$P_{av} = 40W$$\n" +
                            "ANSWER: <br>\n" +
                            "$$40W$$\n", "Average Power"},
                    {"Work", "QUESTION: <br>\n" +
                            "How much work is done in 5 seconds if a power of 40 watts is required? <br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$P_{av} = 40W$$\n" +
                            "$$\\Delta W = ?$$\n" +
                            "$$\\Delta t = 5s$$\n" +
                            "Since \\(\\Delta W\\) is missing, we use this formula:\n" +
                            "$$\\Delta W = P_{av} \\cdot \\Delta t$$\n" +
                            "SOLUTION: <br>\n" +
                            "$$\\Delta W = 40W \\cdot 5s$$\n" +
                            "$$\\Delta W = 40{{kgm^2} \\over {s^3}} \\cdot 5s$$\n" +
                            "$$\\Delta W = 200{{kgm^2} \\over {s^2}}$$\n" +
                            "$$\\Delta W = 200J$$\n" +
                            "ANSWER: <br>\n" +
                            "$$200J$$\n", "Average Power"},
                    {"Time", "QUESTION: <br>\n" +
                            "How much time did it take to do 200 joules of work and 40 watts of power? <br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$P_{av} = 40W$$\n" +
                            "$$\\Delta W = 200J$$\n" +
                            "$$\\Delta t = ?$$\n" +
                            "Since \\(P_{av}\\) is missing, we use this formula:\n" +
                            "$$\\Delta t = {{\\Delta W} \\over {P_{av}}}$$\n" +
                            "SOLUTION: <br>\n" +
                            "$$\\Delta t = {200J \\over 40W}$$\n" +
                            "$$\\Delta t = {200{{kgm^2} \\over {s^2}} \\over 40{{kgm^2} \\over {s^3}}}$$\n" +
                            "$$\\Delta t = 5s$$\n" +
                            "ANSWER: <br>\n" +
                            "$$5s$$\n", "Average Power"},

                    {"Length of Arc", "QUESTION: <br>\n" +
                            "What is the length of the arc of a circle which has a radius of 3 meters and a central angle of 1.5 radians? <br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$\\Delta S = ?$$\n" +
                            "$$r = 3m$$\n" +
                            "$$\\Delta \\theta = 1.5rad$$\n" +
                            "Since \\(\\Delta S\\) is missing, we use this formula:\n" +
                            "$$\\Delta S = r \\cdot \\Delta \\theta$$\n" +
                            "SOLUTION: <br>\n" +
                            "$$\\Delta S = 3m \\cdot 1.5rad$$\n" +
                            "$$\\Delta S = 4.5m$$\n" +
                            "ANSWER: <br>\n" +
                            "$$4.5m$$\n", "Length of Arc"},
                    {"Radius", "QUESTION: <br>\n" +
                            "What is the radius of a circle which has a length of arc of 4.5 meters and a central angle of 1.5 radians? <br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$\\Delta S = 4.5m$$\n" +
                            "$$r = ?$$\n" +
                            "$$\\Delta \\theta = 1.5rad$$\n" +
                            "Since \\(r\\) is missing, we use this formula:\n" +
                            "$$r = {{\\Delta S} \\over {\\Delta \\theta}}$$\n" +
                            "SOLUTION: <br>\n" +
                            "$$r = {4.5m \\over 1.5rad}$$\n" +
                            "$$r = 3m$$\n" +
                            "ANSWER: <br>\n" +
                            "$$3m$$\n", "Length of Arc"},
                    {"Angle", "QUESTION: <br>\n" +
                            "What is the central angle (in radians) of a circle which has a length of arc of 4.5 meters and a radius of 3 meters. <br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$\\Delta S = 4.5m$$\n" +
                            "$$r = 3m$$\n" +
                            "$$\\Delta \\theta = ? $$\n" +
                            "Since \\(\\Delta \\theta \\) is missing, we use this formula:\n" +
                            "$$\\Delta \\theta = {{\\Delta S} \\over {r}}$$\n" +
                            "SOLUTION: <br>\n" +
                            "$$\\Delta \\omega = {4.5m \\over 3m}$$\n" +
                            "$$\\Delta \\omega = 1.5$$\n" +
                            "ANSWER: <br>\n" +
                            "$$1.5rad$$\n", "Length of Arc"},

                    {"Tangential Velocity", "QUESTION: <br>\n" +
                            "A car is moving in a circular path. What is the car’s tangential velocity when it takes 20 seconds to cover a length of the arc equal to 1000 meters? <br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$v = ?$$\n" +
                            "$$\\Delta S = 1000m$$\n" +
                            "$$\\Delta t = 20s$$\n" +
                            "Since \\(v\\) is missing, we use this formula:\n" +
                            "$$v = {{\\Delta S} \\over {\\Delta t}}$$\n" +
                            "SOLUTION: <br>\n" +
                            "$$v = {1000m \\over 20s}$$\n" +
                            "$$v = 50{m \\over s}$$\n" +
                            "ANSWER: <br>\n" +
                            "$$50{m \\over s}$$\n", "Tangential Velocity"},
                    {"Length of Arc", "QUESTION: <br>\n" +
                            "A car is moving in a circular path. How much length of the arc did the car cover when it had a tangential velocity of 50 meters per second for 20 seconds. <br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$v = 50{m \\over s}$$\n" +
                            "$$\\Delta S = ?$$\n" +
                            "$$\\Delta t = 20s$$\n" +
                            "Since \\(\\Delta S\\) is missing, we use this formula:\n" +
                            "$$\\Delta S = v \\cdot \\Delta t$$\n" +
                            "SOLUTION: <br>\n" +
                            "$$\\Delta S = 50{m \\over s} \\cdot 20s$$\n" +
                            "$$\\Delta S = 1000m$$\n" +
                            "ANSWER: <br>\n" +
                            "$$1000m$$\n", "Tangential Velocity"},
                    {"Time", "QUESTION: <br>\n" +
                            "A car is moving in a circular path. How long did it take the car to cover a length of the arc equal to 1000 meters if it had a tangential velocity of 50 meters per second? <br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$v = 50{m \\over s}$$\n" +
                            "$$\\Delta S = 1000m$$\n" +
                            "$$\\Delta t = ?$$\n" +
                            "Since \\(\\Delta t\\) is missing, we use this formula:\n" +
                            "$$\\Delta t = {{\\Delta S} \\over {v}}$$\n" +
                            "SOLUTION: <br>\n" +
                            "$$\\Delta t = {1000m \\over 50{m \\over s}}$$\n" +
                            "$$\\Delta t = 20s$$\n" +
                            "ANSWER: <br>\n" +
                            "$$20s$$\n", "Tangential Velocity"},

                    {"Velocity", "QUESTION: <br>\n" +
                            "Michael was jogging around a circular path in a park. The circular path he was jogging around in has a radius of 10 meters. How fast was he going if he finished one revolution in 30 seconds? <br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$v = ?$$\n" +
                            "$$r = 10m$$\n" +
                            "$$t = 30s$$\n" +
                            "Since \\(v\\) is missing, we use this formula:\n" +
                            "$$v = 2 \\cdot \\pi \\cdot {r \\over t}$$\n" +
                            "SOLUTION: <br>\n" +
                            "$$v = 2 \\cdot 3.1416 \\cdot {10m \\over 30s}$$\n" +
                            "$$v = 6.2832 \\cdot 0.3333{m \\over s}$$\n" +
                            "$$v = 2.0942{m \\over s}$$\n" +
                            "ANSWER: <br>\n" +
                            "$$2.0942{m \\over s}$$\n", "Velocity around a Circle"},
                    {"Radius", "QUESTION: <br>\n" +
                            "Michael was jogging around a circular path in a park. He finished one revolution in 30 seconds. How long is the circular path’s radius if he was jogging with a speed of 2.0942 meters per second? <br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$v = 2.0942{m \\over s}$$\n" +
                            "$$r = ?$$\n" +
                            "$$t = 30s$$\n" +
                            "Since \\(r\\) is missing, we use this formula:\n" +
                            "$$r = {{v \\cdot t} \\over {2 \\cdot \\pi}}$$\n" +
                            "SOLUTION: <br>\n" +
                            "$$r = {{2.0942{m \\over s} \\cdot 30s} \\over {2 \\cdot 3.1416}}$$\n" +
                            "$$r = {{62.826m} \\over {6.2832}}$$\n" +
                            "$$r = 10m$$\n" +
                            "ANSWER: <br>\n" +
                            "$$10m$$\n", "Velocity around a Circle"},
                    {"Time", "QUESTION: <br>\n" +
                            "Michael was jogging around a circular path in a park. The circular path he was jogging around in has a radius of 10 meters. How long did it take him to finish one revolution when he was jogging with a speed of 2.0942 meters per second? <br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$v = 2.0942{m \\over s}$$\n" +
                            "$$r = 10m$$\n" +
                            "$$t = ?$$\n" +
                            "Since \\(t\\) is missing, we use this formula:\n" +
                            "$$t = 2 \\cdot \\pi \\cdot {r \\over v}$$\n" +
                            "SOLUTION: <br>\n" +
                            "$$t = 2 \\cdot 3.1416 \\cdot {{10m} \\over {2.0942{m \\over s}}}$$\n" +
                            "$$t = 6.2832 \\cdot 4.7751s$$\n" +
                            "$$t = 30s$$\n" +
                            "ANSWER: <br>\n" +
                            "$$30$$\n", "Velocity around a Circle"},

                    {"Angular Velocity", "QUESTION: <br>\n" +
                            "What is the angular velocity of the Moon around the Earth if it takes 27.3 days for the moon to make one whole revolution around the Earth? <br><br>\n" +
                            "<i>Note: a whole revolution is equal to \\(2 \\pi\\) or \\(6.2832rad\\). 27.3 days is equal to 2358720 seconds.</i><br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$\\omega = ?$$\n" +
                            "$$\\Delta \\theta = 6.2832rad$$\n" +
                            "$$\\Delta t = 2358720s$$\n" +
                            "Since \\(\\omega \\) is missing, we use this formula:\n" +
                            "$$\\omega = {{\\Delta \\theta} \\over {\\Delta t}}$$\n" +
                            "SOLUTION: <br>\n" +
                            "$$\\omega = {6.2832rad \\over 2358720s}$$\n" +
                            "$$\\omega = 2.6638E-6{rad \\over s}$$\n" +
                            "ANSWER: <br>\n" +
                            "$$2.6638E-6{rad \\over s}$$\n", "Angular Velocity"},
                    {"Angle", "QUESTION: <br>\n" +
                            "The Moon has an angular speed of 2.6638E-6 radians per second. How much around the Earth does it go after 27.3 days? <br><br>\n" +
                            "<i>Note: 27.3 days is equal to 2358720 seconds.</i><br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$\\omega = 2.6638E-6{rad \\over s}$$\n" +
                            "$$\\Delta \\theta = ?$$\n" +
                            "$$\\Delta t = 2358720s$$\n" +
                            "Since \\(\\Delta \\theta \\) is missing, we use this formula:\n" +
                            "$$\\Delta \\theta = \\omega \\cdot \\Delta t$$\n" +
                            "SOLUTION: <br>\n" +
                            "$$\\Delta \\theta = 2.6638E-6{rad \\over s} \\cdot 2358720s$$\n" +
                            "$$\\Delta \\theta = 6.2832$$\n" +
                            "ANSWER: <br>\n" +
                            "$$6.2832rad$$\n", "Angular Velocity"},
                    {"Time", "QUESTION: <br>\n" +
                            "How long does it take for the Moon to go around the Earth if it has an angular speed of 2.6638E-6 radians per second? <br><br>\n" +
                            "<i>Note: a whole revolution is equal to \\(2 \\pi\\) or \\(6.2832rad\\).</i><br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$\\omega = 2.6638E-6{rad \\over s}$$\n" +
                            "$$\\Delta \\theta = 6.2832rad$$\n" +
                            "$$\\Delta t = ?$$\n" +
                            "Since \\(\\Delta t \\) is missing, we use this formula:\n" +
                            "$$\\Delta t = {{\\Delta \\theta} \\over {\\omega}}$$\n" +
                            "SOLUTION: <br>\n" +
                            "$$\\Delta t = {{6.2832rad} \\over {2.6638E-6{r \\over s}}}$$\n" +
                            "$$\\Delta t = 2358720s$$\n" +
                            "ANSWER: <br>\n" +
                            "$$2358720s$$\n", "Angular Velocity"},

                    {"Centripetal Acceleration", "QUESTION: <br>\n" +
                            "What is the centripetal acceleration of a ball tied in a rope and swung in a circular path with a radius of 0.75 meters which moves in a velocity of 4 meters per second? <br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$a_c = ?$$\n" +
                            "$$\\omega = 4{m \\over s}$$\n" +
                            "$$r = 0.75m$$\n" +
                            "Since \\(a_c\\) is missing, we use this formula:\n" +
                            "$$a_c = r \\cdot \\omega ^2$$\n" +
                            "SOLUTION: <br>\n" +
                            "$$a_c = {{4{m \\over s}^2} \\over {0.75m}}$$\n" +
                            "$$a_c = {{16{{m^2} \\over {s^2}}} \\over {0.75m}}$$\n" +
                            "$$a_c = 21.3333{{m} \\over {s^2}}$$\n" +
                            "ANSWER: <br>\n" +
                            "$$21.3333{{m} \\over {s^2}}$$\n", "Centripetal Acceleration"},
                    {"Angular Velocity", "QUESTION: <br>\n" +
                            "What is the angular velocity of a ball tied in a rope and swung in a circle with a circular path with a radius of 0.75 meters and a centripetal acceleration of 21.3333 meters per second squared? <br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$a_c = 21.3333{{m} \\over {s^2}}$$\n" +
                            "$$\\omega = ?$$\n" +
                            "$$r = 0.75m$$\n" +
                            "Since \\(\\omega \\) is missing, we use this formula:\n" +
                            "$$\\omega = \\sqrt{a_c \\over r}$$\n" +
                            "SOLUTION: <br>\n" +
                            "$$\\omega = \\sqrt{21.3333{{m} \\over {s^2}} \\cdot 0.75m}$$\n" +
                            "$$\\omega = \\sqrt{16{{m^2} \\over {s^2}}}$$\n" +
                            "$$\\omega = 4{m \\over s}$$\n" +
                            "ANSWER: <br>\n" +
                            "$$4{m \\over s}$$\n", "Centripetal Acceleration"},
                    {"Radius", "QUESTION: <br>\n" +
                            "A ball is tied in a rope and swung in a circular path. What is the circular path’s radius if the ball has a centripetal acceleration of 21.3333 meters per second squared and an angular velocity of 4 meters per second? <br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$a_c = 21.3333{{m} \\over {s^2}}$$\n" +
                            "$$\\omega = 4{m \\over s}$$\n" +
                            "$$r = ? $$\n" +
                            "Since \\(r\\) is missing, we use this formula:\n" +
                            "$$r = {{a_c} \\over {\\omega ^2}}$$\n" +
                            "SOLUTION: <br>\n" +
                            "$$r = {{4{m \\over s}^2} \\over {21.3333{{m} \\over {s^2}}}}$$\n" +
                            "$$r = 0.75m$$\n" +
                            "ANSWER: <br>\n" +
                            "$$0.75m$$\n", "Centripetal Acceleration"},

                    {"Centripetal Force", "QUESTION:\n" +
                            "A car with a mass of 1000 kilograms is moving in a circular path with a radius of 50 meters. The car has a tangential velocity of 12 meters per second. What is the car’s centripetal force? <br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$F = ?$$\n" +
                            "$$m = 1000kg$$\n" +
                            "$$v = 12{m \\over s}$$\n" +
                            "$$r = 50m$$\n" +
                            "Since \\(F\\) is missing, we use this formula:\n" +
                            "$$F = {{m \\cdot v^2} \\over {r}}$$\n" +
                            "SOLUTION:\n" +
                            "$$F = {{1000kg \\cdot 12{m \\over s}^2} \\over {50m}}$$\n" +
                            "$$F = {{1000kg \\cdot 144{{m^2} \\over {s^2}}} \\over {50m}}$$\n" +
                            "$$F = {{144000{{kgm^2} \\cdot {s^2}}} \\over {50m}}$$\n" +
                            "$$F = 2880{{kgm} \\over {s^2}}$$\n" +
                            "$$F = 2880N$$\n" +
                            "ANSWER:\n" +
                            "$$2880N$$\n", "Centripetal and Centrifugal Forces"},
                    {"Mass", "QUESTION:\n" +
                            "A car is moving in a circular path with a radius of 50 meters. The car has a tangential velocity of 12 meters per second and a centripetal force of 2880 Newton. What is the mass of the car? <br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$F = 2880N$$\n" +
                            "$$m = ?$$\n" +
                            "$$v = 12{m \\over s}$$\n" +
                            "$$r = 50m$$\n" +
                            "Since \\(m\\) is missing, we use this formula:\n" +
                            "$$m = {{F \\cdot r} \\over {v^2}}$$\n" +
                            "SOLUTION:\n" +
                            "$$m = {{2880N \\cdot 50m} \\over {12{m \\over s}^2}}$$\n" +
                            "$$m = {{2880{{kgm} \\over {s^2}} \\cdot 50m} \\over {12{m \\over s}^2}}$$\n" +
                            "$$m = {{144000{{kgm^2} \\over {s^2}}} \\over {144{{m^2} \\over {s^2}}}}$$\n" +
                            "$$m = 1000kg$$\n" +
                            "ANSWER:\n" +
                            "$$1000kg$$\n", "Centripetal and Centrifugal Forces"},
                    {"Tangential Velocity", "QUESTION:\n" +
                            "A car with a mass of 1000 kilograms is moving in a circular path with a radius of 50 meters. The car has a centripetal force of 2880 Newton. What is the tangential velocity of the car? <br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$F = 2880N$$\n" +
                            "$$m = 1000kg$$\n" +
                            "$$v = ?$$\n" +
                            "$$r = 50m$$\n" +
                            "Since \\(v\\) is missing, we use this formula:\n" +
                            "$$v = \\sqrt{{F \\cdot r} \\over {m}}$$\n" +
                            "SOLUTION:\n" +
                            "$$v = \\sqrt{{2880N \\cdot 50m} \\over {1000kg}}$$\n" +
                            "$$v = \\sqrt{{2880{{kgm} \\over {s^2}} \\cdot 50m} \\over {1000kg}}$$\n" +
                            "$$v = \\sqrt{{144000{{kgm^2} \\over {s^2}}} \\over {1000kg}}$$\n" +
                            "$$v = \\sqrt{144{{m^2} \\over {s^2}}}$$\n" +
                            "$$v = 12{m \\over s}$$\n" +
                            "ANSWER:\n" +
                            "$$12{m \\over s}$$\n", "Centripetal and Centrifugal Forces"},
                    {"Radius", "QUESTION:\n" +
                            "A car with a mass of 1000 kilograms is moving in a circular path. The car has a tangential velocity of 12 meters per second and a centripetal force of 2880 Newton. What is the radius of the circular path?\n" +
                            "GIVEN: <br>\n" +
                            "$$F = 2880N$$\n" +
                            "$$m = 1000kg$$\n" +
                            "$$v = 12{m \\over s}$$\n" +
                            "$$r = ?$$\n" +
                            "Since \\(r\\) is missing, we use this formula:\n" +
                            "$$r = {{m \\cdot v^2} \\over {F}}$$\n" +
                            "SOLUTION:\n" +
                            "$$r = {{1000kg \\cdot 12{m \\over s}^2} \\over {2880N}}$$\n" +
                            "$$r = {{1000kg \\cdot 144{{m^2} \\over {s^2}}} \\over {2880{{kgm} \\over {s^2}}}}$$\n" +
                            "$$r = {{144000{{kgm^2} \\over {s^2}}} \\over {2880{{kgm} \\over {s^2}}}}$$\n" +
                            "$$r =50m$$\n" +
                            "ANSWER:\n" +
                            "$$50m$$\n", "Centripetal and Centrifugal Forces"},

                    {"Torque", "QUESTION:\n" +
                            "A force of 5 Newton is applied at the end of a lever which has a length of 2 meters. If the force is applied perpendicular to the lever, what would be the torque equal to? <br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$\\tau = ?$$\n" +
                            "$$F = 5N$$\n" +
                            "$$r = 2m$$\n" +
                            "Since \\(\\tau \\) is missing, we use this formula:\n" +
                            "$$\\tau = F \\cdot r$$\n" +
                            "SOLUTION:\n" +
                            "$$\\tau = 5N \\cdot 2m$$\n" +
                            "$$\\tau = 10Nm$$\n" +
                            "ANSWER:\n" +
                            "$$10Nm$$\n", "Torque"},
                    {"Force", "QUESTION:\n" +
                            "A lever has a length of 2 meters. It has a torque of 10 Newton meters. What is the force applied to it if the force was applied at the end of the lever and is perpendicular to the lever? <br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$\\tau = 10Nm$$\n" +
                            "$$F = ?$$\n" +
                            "$$r = 2m$$\n" +
                            "Since \\(F\\) is missing, we use this formula:\n" +
                            "$$F = {\\tau \\over r}$$\n" +
                            "SOLUTION:\n" +
                            "$$F = {10Nm \\over 2m}$$\n" +
                            "$$F = 5N$$\n" +
                            "ANSWER:\n" +
                            "$$5N$$\n", "Torque"},
                    {"Position", "QUESTION:\n" +
                            "A force of 5 Newton is applied at the end of a lever. The force is applied perpendicular to the lever. If it has a torque of 10 Newton meters, how long is the lever? <br><br>\n" +
                            "GIVEN: <br>\n" +
                            "$$\\tau = 10Nm$$\n" +
                            "$$F = 5N$$\n" +
                            "$$r = ?$$\n" +
                            "Since \\(r\\) is missing, we use this formula:\n" +
                            "$$r = {\\tau \\over F}$$\n" +
                            "SOLUTION:\n" +
                            "$$r = {10Nm \\over 5N}$$\n" +
                            "$$r = 2m$$\n" +
                            "ANSWER:\n" +
                            "$$2m$$\n", "Torque"}
            };
            for(String[] s : examples){
                Cursor c = database.query(
                        DataContract.SectionEntry.TABLE_NAME,
                        new String[]{DataContract.SectionEntry._ID},
                        DataContract.SectionEntry.COLUMN_NAME + " = ?",
                        new String[]{s[2]},
                        null,
                        null,
                        null
                );
                c.moveToFirst();

                ContentValues values = new ContentValues();
                values.put(DataContract.ExampleEntry.COLUMN_NAME, s[0]);
                values.put(DataContract.ExampleEntry.COLUMN_CONTENT, s[1]);
                values.put(DataContract.ExampleEntry.COLUMN_SECTION_KEY, c.getLong(c.getColumnIndex(DataContract.SectionEntry._ID)));

                database.insert(DataContract.ExampleEntry.TABLE_NAME, null, values);
            }

        }
    }

    private void initFormulas(SQLiteDatabase database){
        if(database.isOpen()) {
            String[][] formulas = {
                    {"Displacement", "Scalar and Vector Values", "$$d = {x_f - x_i}$$"},
                    {"Speed", "Velocity", "$$s = {d \\over t}$$"},
                    {"Velocity", "Velocity", "$$v = {x_f - x_i \\over t}$$"},
                    {"Average Velocity", "Velocity", "$$v_{av} = {x_f - x_i \\over t_f - t_i}$$"},
                    {"Acceleration", "Acceleration", "$$a = {v_f - v_i \\over t}$$"},
                    {"Average Acceleration","Acceleration", "$$a_{av} = {v_f - v_i \\over t_f - t_i}$$"},
                    {"Free-fall Velocity","Free-fall", "$$v_y = u + g \\cdot t$$"},
                    {"Free-fall Displacement", "Free-fall", "$$y = {1 \\over 2}g  \\cdot t^2$$"},
                    {"Free-fall Upward Motion", "Free-fall", "$$v_f^2 = v_i^2 + 2 \\cdot g \\cdot d$$"},
                    {"Horizontal Components", "Projectile Motion", "$$x = v_{x}t$$"},
                    {"Vertical Components",  "Projectile Motion", "$$y = v_{yi}t - {1 \\over 2}g \\cdot t^2$$"},
                    {"Vertical Velocity at a Certain Time",  "Projectile Motion", "$$v_y = g \\cdot t + v_{yi}$$"},
                    {"Time of Flight", "Projectile Motion", "$$t = {2v_{yi}sin \\theta \\over g}$$"},
                    {"Maximum Height Reached", "Projectile Motion", "$$H = {v_{1}^{2}sin^{2} \\theta \\over 2g}$$"},
                    {"Horizontal Range", "Projectile Motion", "$$R = {v_{1}^{2}sin2 \\theta \\over g}$$"},
                    {"Friction", "Friction", "$$F_f = \\mu \\cdot F_n$$"},
                    {"Momentum", "Momentum and Impulse", "$$p = m \\cdot v$$"},
                    {"Work", "Work", "$$W = \\vec{F} \\cdot \\vec{x} \\cdot cos \\theta$$"},
                    {"Kinetic Energy", "Energy", "$$KE = {mv^2 \\over 2}$$"},
                    {"Gravitational Potential Energy", "Energy", "$$PE_g = m \\cdot g \\cdot h$$"},
                    {"Total Mechanical Energy", "Energy", "$$TME = KE + PE$$"},
                    {"Average Power", "Power", "$$P_{av} = {\\Delta W \\over \\Delta t}$$"},
//                    {"Instantaneous Power","Power", "$$P = F \\cdot cos(a) \\cdot v$$"},
                    {"Length of Arc",  "Uniform Circular Motion", "$$\\Delta S = r \\cdot \\Delta \\theta$$"},
                    {"Tangential Velocity", "Uniform Circular Motion", "$$v = {\\Delta S \\over \\Delta t}$$"},
                    {"Velocity around a Circle", "Uniform Circular Motion", "$$v = 2 \\cdot \\pi \\cdot {r \\over t}$$"},
                    {"Angular Velocity", "Uniform Circular Motion", "$$\\omega = {\\Delta \\theta \\over \\Delta t}$$"},
                    {"Centripetal Acceleration", "Uniform Circular Motion", "$$a_c = {{\\omega ^2} \\over {r}}$$"},
                    {"Centripetal Force","Centripetal and Centrifugal Forces", "$$F = {m \\cdot v^2 \\over r} $$"},
//                    {"Moment of Inertia", "Rotational Motion", "$$I = m \\cdot r^2$$"},
                    {"Torque","Rotational Motion", "$$\\tau = F \\cdot r$$"},
//                    {"Angular Momentum", "Rotational Motion", "$$L = I \\cdot \\omega$$"}
            };

            for(String[] s: formulas){
                Cursor c = database.query(
                        DataContract.LessonEntry.TABLE_NAME,
                        new String[]{DataContract.LessonEntry._ID},
                        DataContract.LessonEntry.COLUMN_NAME + " = ?",
                        new String[]{s[1]},
                        null,
                        null,
                        null
                );
                c.moveToFirst();

                ContentValues values = new ContentValues();
                values.put(DataContract.FormulaEntry.COLUMN_NAME, s[0]);
                values.put(DataContract.FormulaEntry.COLUMN_LESSON_KEY,
                        c.getLong(c.getColumnIndex(DataContract.LessonEntry._ID)));
                values.put(DataContract.FormulaEntry.COLUMN_FORMULA, s[2]);

                c.close();
                database.insert(DataContract.FormulaEntry.TABLE_NAME, null, values);
            }
        }
    }

    private void initConstants(SQLiteDatabase database){
        if(database.isOpen()) {
            String[][] constants = {
                    {"Acceleration due to Gravity", "9.8", "The acceleration for any object moving under the sole influence of gravity", "g"}
            };


            for(String[] s: constants){

                ContentValues values = new ContentValues();
                values.put(DataContract.ConstantEntry.COLUMN_NAME, s[0]);
                values.put(DataContract.ConstantEntry.COLUMN_DEFAULT, Double.parseDouble(s[1]));
                values.put(DataContract.ConstantEntry.COLUMN_CURRENT, Double.parseDouble(s[1]));
                values.put(DataContract.ConstantEntry.COLUMN_DESC, s[2]);
                values.put(DataContract.ConstantEntry.COLUMN_SYMBOL, s[3]);

                database.insert(DataContract.ConstantEntry.TABLE_NAME, null, values);
            }
        }
    }

    private void initFormulaConstants(SQLiteDatabase database){
        if(database.isOpen()){
            String[][] formula_constants = {
                    {"Acceleration due to Gravity", "Free-fall Velocity"},
                    {"Acceleration due to Gravity", "Free-fall Displacement"},
                    {"Acceleration due to Gravity", "Free-fall Upward Motion"},
                    {"Acceleration due to Gravity", "Vertical Components"},
                    {"Acceleration due to Gravity", "Vertical Velocity at a Certain Time"},
                    {"Acceleration due to Gravity", "Time of Flight"},
                    {"Acceleration due to Gravity", "Maximum Height Reached"},
                    {"Acceleration due to Gravity", "Horizontal Range"},
                    {"Acceleration due to Gravity", "Gravitational Potential Energy"}
            };

            for(String[] s: formula_constants){
                Cursor a = database.query(
                        DataContract.ConstantEntry.TABLE_NAME,
                        new String[]{DataContract.ConstantEntry._ID},
                        DataContract.ConstantEntry.COLUMN_NAME + " = ?",
                        new String[]{s[0]},
                        null,
                        null,
                        null
                );
                Cursor b = database.query(
                        DataContract.FormulaEntry.TABLE_NAME,
                        new String[]{DataContract.FormulaEntry._ID},
                        DataContract.FormulaEntry.COLUMN_NAME + " = ?",
                        new String[]{s[1]},
                        null,
                        null,
                        null
                );
                a.moveToFirst();
                b.moveToFirst();
                ContentValues values = new ContentValues();
                values.put(DataContract.FormulaConstantEntry.COLUMN_CONSTANT_KEY,
                        a.getLong(a.getColumnIndex(DataContract.ConstantEntry._ID)));
                values.put(DataContract.FormulaConstantEntry.COLUMN_FORMULA_KEY,
                        b.getLong(b.getColumnIndex(DataContract.FormulaEntry._ID)));

                a.close();
                b.close();

                database.insert(DataContract.FormulaConstantEntry.TABLE_NAME, null, values);
            }
        }
    }

    private void initVariables(SQLiteDatabase database) {
        if(database.isOpen()) {
            String[][] variables = {
                    //
                    {"Displacement", "Displacement", "$$d = {x_f - x_i}$$", "xf - xi", "d", "d", "{{m}}", "Length"},
                    {"Displacement", "Initial Position","$$x_i= {x_f - d}$$", "xf - d", "x_i", "xi", "{{m}}", "Length"},
                    {"Displacement", "Final Position", "$$x_f= {x_i + d}$$", "xi + d", "x_f", "xf", "{{m}}","Length"},
                    //
                    {"Speed", "Speed", "$$s = {{d} \\over {t}}$$", "d / tt", "s", "s" ,"{{m} \\over {s}}", "Speed"},
                    {"Speed", "Distance", "$$d = {s \\cdot {t}}$$", "s * tt", "{d}", "d", "{{m}}", "Length"},
                    {"Speed", "Time", "$$t = {{d} \\over s}$$", "d / s", "{t}", "tt", "{{s}}", "Duration"},
                    //
                    {"Velocity", "Velocity", "$$v = {x_f - x_i \\over {t}}$$", "(xf - xi) / tt", "{v}", "vel", "{{m} \\over {s}}", "Speed"},
                    {"Velocity", "Initial Position", "$$x_i = {x_f - ({v} \\cdot {t})}$$", "xf - (vel * tt)", "x_i", "xi", "{{m}}","Length"},
                    {"Velocity", "Final Position", "$$x_f = {({v} \\cdot {t}) + x_i}$$", "(vel * tt) + xi", "x_f", "xf","{{m}}", "Length"},
                    {"Velocity", "Time", "$$t = {(x_f - x_i) \\over {v}}$$", "(xf - xi) / vel", "{t}", "tt", "{{s}}", "Duration"},
                    //
                    {"Average Velocity", "Average Velocity", "$$v_{av} = {(x_f - x_i) \\over (t_f - t_i)}$$", "(xf - xi) / (tf - ti)", "v_{av}", "vel", "{{m} \\over {s}}", "Speed"},
                    {"Average Velocity", "Initial Position", "$$x_i = {x_f - (v_{av} \\cdot (t_f - t_i))}$$", "xf - (vel * (tf - ti))", "x_i", "xi", "{{m}}", "Length"},
                    {"Average Velocity", "Final Position",  "$$x_f = {(v_{av} \\cdot (t_f - t_i)) + x_i}$$", "(vel * (tf - ti)) + xi", "x_f", "xf", "{{m}}", "Length"},
                    {"Average Velocity", "Initial Time","$$t_i = {t_f - {(x_f - x_i) \\over v_{av}}}$$", "tf - ((xf - xi) / vel)", "t_i","ti", "{{s}}", "Duration"},
                    {"Average Velocity", "Final Time", "$$t_f = {{(x_f - x_i) \\over v_{av}} + t_i}$$", "((xf - xi) / vel) + ti", "t_f", "tf", "{{s}}", "Duration"},
                    //
                    {"Acceleration", "Acceleration", "$$a = {(v_f - v_i) \\over {t}}$$", "(vf - vi) / t", "a", "a", "{{m} \\over {s^2}}", "Acceleration"},
                    {"Acceleration", "Initial Velocity", "$$v_i = {v_f - (a \\cdot {t})}$$", "vf - (a * t)", "v_i", "vi", "{{m} \\over {s}}", "Speed"},
                    {"Acceleration", "Final Velocity", "$$v_f = {(a \\cdot {t}) + v_i}$$", "(a * t) + vi", "v_f", "vf", "{{m} \\over {s}}", "Speed"},
                    {"Acceleration", "Time Interval", "$$t = {(v_f - v_i) \\over a}$$", "(vf - vi) / a", "{t}", "t", "{{s}}", "Duration"},
                    //asdf - Kurt
                    {"Average Acceleration", "Average Acceleration", "$$a_{av} = {(v_f - v_i) \\over (t_f - t_i)}$$", "(vf - vi) / (tf - ti)","a_{av}", "a", "{{m} \\over {s^2}}", "Acceleration"},
                    {"Average Acceleration", "Initial Velocity", "$$v_i = {v_f - (a_{av} \\cdot (t_f - t_i))}$$", "vf - (a * (tf - ti))","v_i", "vi", "{{m} \\over {s}}", "Speed"},
                    {"Average Acceleration", "Final Velocity", "$$v_f = {(a_{av} \\cdot (t_f - t_i)) + v_i}$$", "(a * (tf - ti)) + vi","v_f", "vf", "{{m} \\over {s}}", "Speed"},
                    {"Average Acceleration", "Initial Time", "$$t_i = {t_f - {(v_f - v_i) \\over a_{av}}}$$", "tf - ((vf - vi) / a)","t_i","ti", "{{s}}", "Duration"},
                    {"Average Acceleration", "Final Time", "$$t_f = {{(v_f - v_i) \\over a_{av}} + t_i}$$", "((vf - vi) / a) + ti","t_f", "tf", "{{s}}", "Duration"},
                    //
                    {"Free-fall Velocity", "Velocity", "$$v_y = {u + (g \\cdot {t})}$$", "u + (g * t)","v_y", "v", "{{m} \\over {s}}", "Speed"},
                    {"Free-fall Velocity", "Initial Velocity", "$$u = {v_y - (g \\cdot {t})}$$", "v - (g * t)", "u", "u", "{{m} \\over {s}}", "Speed"},
                    {"Free-fall Velocity", "Acceleration due to Gravity", "$$g = {(v_y - u) \\over {t}}$$", "(v - u) / t", "g","g", "{{m} \\over {s^2}}", "Acceleration", "Acceleration due to " +
                            "Gravity"},
                    {"Free-fall Velocity", "Time", "$$t = {(v_y - u) \\over g}$$", "(v - u) / g", "{t}", "t", "{{s}}", "Duration"},
                    //
                    {"Free-fall Displacement", "Displacement", "$$y = {({1 \\over 2} \\cdot g) \\cdot {({t})^2}}$$", "((1 / 2) * g) * (t) ^ 2","y", "y", "{{m}}", "Length"},
                    {"Free-fall Displacement", "Acceleration due to Gravity", "$$g = {2 \\cdot {y \\over {({t})^2}}}$$", "2 * (y / (t) ^ 2)","g", "g", "{{m} \\over {s^2}}",
                            "Acceleration", "Acceleration due to Gravity"},
                    {"Free-fall Displacement", "Time", "$$t = {\\sqrt{{2 \\cdot {y \\over g}}}}$$", "sqrt(2 * (y / g))","{t}", "t", "{{s}}", "Duration"},
                    //
                    {"Free-fall Upward Motion", "Final Velocity", "$$v_f= {\\sqrt{{((v_i)^2) + ((2 \\cdot g) \\cdot {d})}}}$$", "sqrt((vi)^2 + (2 * g * d))","v_f", "vf", "{{m} \\over {s}}", "Speed"},
                    {"Free-fall Upward Motion", "Initial Velocity", "$$v_i = {\\sqrt{{((v_f)^2) - ((2 \\cdot g) \\cdot {d})}}}$$", "sqrt((vf)^2 - (2 * g * d))","v_i", "vi", "{{m} \\over {s}}", "Speed"},
                    {"Free-fall Upward Motion", "Acceleration due to Gravity", "$$g = {{((v_f)^2 - (v_i)^2) \\over (2 \\cdot {d})}}$$", "((vf)^2 - (vi)^2) / (2 * d)","g", "g", "{{m} \\over " +
                            "{s^2}}", "Acceleration", "Acceleration due to Gravity"},
                    {"Free-fall Upward Motion", "Distance", "$${d} = {{((v_f)^2 - (v_i)^2) \\over (2 \\cdot g)}}$$", "((vf)^2 - (vi)^2) / (2 * g)","{d}", "d", "{{m}}", "Length"},
                    //
                    {"Horizontal Components", "Horizontal Distance","$${x} = {v_x \\cdot {t}}$$", "v * t", "{x}", "x", "{{m}}", "Length"},
                    {"Horizontal Components", "Velocity along the x-axis","$$v_x = {{x} \\over {t}}$$", "x / t","v_x", "v", "{{m} \\over {s}}", "Speed"},
                    {"Horizontal Components", "Time","$$t = {{x} \\over v_x}$$", "x / v","{t}", "t", "{{s}}", "Duration"},
                    //
                    {"Vertical Components", "Vertical Distance", "$${y} = {(v_{yi} \\cdot {t}) + ({1 \\over 2} \\cdot g \\cdot ({t})^2)}$$", "(vi * t) + ((1 / 2) * g * (t)^2)","{y}", "y", "{{m}}",
                            "Length"},
                    {"Vertical Components", "Initial Velocity along the y-axis", "$$v_{yi} = {({y} - {1 \\over 2} \\cdot g \\cdot ({t})^2) \\over {t}}$$","(y - (1 / 2) * g * (t)^2) / t", "v_{yi}",
                            "vi", "{{m} " +
                            "\\over" +
                            " {s}}", "Speed"},//zxcv
                    {"Vertical Components", "Time","$${t} = {\\sqrt{{(2 \\cdot {y}) \\over g}}}$$","sqrt((2 * y) / g)", "{t}", "t", "{{s}}", "Duration"},//zxcv
                    {"Vertical Components", "Acceleration due to Gravity","$$g = {2 \\cdot ({y} - (v_{yi} \\cdot {t})) \\over ({t})^2}$$", "(2 * (y - (vi * t))) / (t)^2","g", "g", "{{m} \\over {s^2}}",
                            "Acceleration", "Acceleration due to Gravity"},
                    //zxcv
                    //
                    {"Vertical Velocity at a Certain Time", "Vertical Velocity","$$v_y = {(g \\cdot {t}) + v_{yi}}$$", "vi + (g * t)", "v_y", "v", "{{m} \\over {s}}", "Speed"},
                    {"Vertical Velocity at a Certain Time", "Initial Velocity along the y-axis", "$$v_{yi} = {v_y - (g \\cdot {t})}$$", "v - (g * t)","v_{yi}", "vi", "{{m} \\over {s}}", "Speed"},
                    {"Vertical Velocity at a Certain Time", "Time","$$t = {(v_y - v_{yi}) \\over g}$$", "(v - vi) / g", "{t}", "t", "{{s}}", "Duration"},
                    {"Vertical Velocity at a Certain Time", "Acceleration due to Gravity","$$g = {(v_y - v_{yi}) \\over {t}}$$", "(v - vi) / t", "g", "g", "{{m} \\over {s^2}}", "Acceleration",
                            "Acceleration due to Gravity"},
                    //
                    {"Time of Flight", "Time of Flight","$$t = {((2 \\cdot v_{yi}) \\cdot sin(\\theta)) \\over g}$$", "((2 * v) * sin(a)) / g", "{t}", "t", "{{s}}", "Duration"},
                    {"Time of Flight", "Initial Velocity","$$v_{yi} = {({t} \\cdot g) \\over (2 \\cdot sin(\\theta))}$$", "(t * g) / (2 * sin(a))", "v_{yi}", "v", "{{m} \\over {s}}", "Speed"},
                    {"Time of Flight", "Angle of Trajectory","$$\\theta = {sin^{-1}({(g \\cdot {t}) \\over (2 \\cdot v_{yi})})}$$", "asin((g * t) / (2 * v))", "\\theta","a", "^{{\\circ}}",
                            "Angle Degrees"},
                    {"Time of Flight", "Acceleration due to Gravity","$$g = {((2 \\cdot v_{yi}) \\cdot sin(\\theta)) \\over {t}}$$", "((2 * v) * sin(a)) / t","g", "g", "{{m} \\over {s^2}}",
                            "Acceleration", "Acceleration due to Gravity"},
                    //
                    {"Maximum Height Reached", "Maximum Height Reached","$$H = {((v_{1})^2 \\cdot (sin(\\theta) \\cdot sin(\\theta))) \\over (2 \\cdot g)}$$", "((v)^2 * (sin(a) * sin(a))) / (2 * g)",
                            "H",
                            "H", "{{m}}", "Length"},
                    {"Maximum Height Reached", "Initial Velocity",  "$$v_{1} = {\\sqrt{{(2 \\cdot g \\cdot H) \\over (sin(\\theta) \\cdot sin(\\theta))}}}$$", "sqrt((2 * g * H) / (sin(a) * sin(a)))", "v_{1}", "v", "{{m} " +
                            "\\over " +
                            "{s}}", "Speed"},
                    {"Maximum Height Reached", "Angle of Trajectory", "$$\\theta = {sin^{-1}({\\sqrt{{(2 \\cdot g \\cdot H) \\over (v_{1})^2}}})}$$", "asin(sqrt((2 * g * H) / (v)^2))", "\\theta",
                            "a", "^{{\\circ}}", "Angle Degrees"},
                    {"Maximum Height Reached", "Acceleration due to Gravity", "$$g = {((v_{1})^2 \\cdot (sin(\\theta) \\cdot sin(\\theta))) \\over (2 \\cdot H)}$$", "((v) ^ 2 * (sin(a) * sin(a))) /" +
                            " " +
                            "(2" +
                            " * H)",
                            "g", "g", "{{m} \\over {s^2}}", "Acceleration", "Acceleration due to Gravity"},
                    //
                    {"Horizontal Range", "Horizontal Range","$$R = {((v_{1})^2 \\cdot sin((2 \\cdot \\theta))) \\over g}$$", "((v) ^ 2 * sin(2 * a)) / g", "R", "R", "{{m}}", "Length"},
                    {"Horizontal Range", "Initial Velocity","$$v_{1} = \\sqrt{{(g \\cdot R) \\over (sin((2 \\cdot \\theta)))}}$$", "sqrt((g * R) / (sin(2 * a)))", "v_{1}", "v", "{{m} \\over {s}}",
                            "Speed"},
                    {"Horizontal Range", "Angle of Trajectory", "$$\\theta = {sin^{-1}({(g \\cdot R) \\over (2 * v_{1})})}$$", "asin((g * R) / (2 * v))", "\\theta", "a", "^{{\\circ}}", "Angle Degrees"},
                    {"Horizontal Range", "Acceleration due to Gravity", "$$g = {((v_{1})^2 \\cdot sin((2 \\cdot \\theta))) \\over R}$$", "((v) ^ 2 * sin(2 * a)) / R", "g", "g", "{{m} \\over {s^2}}",
                            "Acceleration", "Acceleration due to Gravity"},
                    //
                    {"Friction", "Frictional Force", "$$F_f = {\\mu \\cdot F_n}$$", "m * N", "F_f", "F", "{{{N}}}", "Force"},
                    {"Friction", "Coefficient of Friction", "$$\\mu = {F_f \\over F_n}$$", "F / N", "\\mu", "m", "{{\\mu}}", "None"},
                    {"Friction", "Normal Force", "$$F_n = {F_f \\over \\mu}$$", "F / m", "F_n", "N", "{{{N}}}", "Force"},
                    //
                    {"Momentum", "Momentum","$$p = {m \\cdot {v}}$$", "m * v", "p", "p", "{{N}{s}}", "Momentum"},
                    {"Momentum", "Mass", "$$m = {p \\over {v}}$$", "p / v", "m", "m", "{{kg}}", "Mass"},
                    {"Momentum", "Velocity", "$${v} = {p \\over m}$$", "p / m", "{v}", "v", "{{m} \\over {s}}", "Speed"},
                    //
                    {"Work", "Work", "$$W = {\\vec{F} \\cdot \\vec{x} \\cdot cos(\\theta)}$$", "F * x * cos(a)", "W", "W", "{{J}}", "Work"},
                    {"Work", "Force", "$$\\vec{F} = {W \\over {\\vec{x} \\cdot cos(\\theta)}}$$", "W / (x * cos(a))", "\\vec{F}", "F", "{{N}}", "Force"},
                    {"Work", "Distance", "$$\\vec{x} = {W \\over {\\vec{F} \\cdot cos(\\theta)}}$$", "W / (F cos(a))", "\\vec{x}", "x", "{{m}}", "Length"},
                    {"Work", "Angle", "$$\\theta = {cos^{-1}({W \\over {\\vec{F} \\cdot \\vec{x}}})}$$", "acos(W / (F * x))", "\\theta", "a", "^{{\\circ}}", "Angle Degrees"},
                    //
                    {"Kinetic Energy", "Kinetic Energy", "$$KE = {(m \\cdot ({v})^2) \\over 2}$$", "(m * (v) ^ 2) / 2", "KE", "KE", "{{J}}", "Energy"},
                    {"Kinetic Energy", "Mass", "$$m = {(2 \\cdot KE) \\over ({v})^2}$$", "(2 * KE) / (v)^2", "m", "m", "{{kg}}", "Mass"},
                    {"Kinetic Energy", "Velocity", "$${v} = {\\sqrt{{(2 \\cdot KE) \\over m}}}$$", "sqrt((2 * KE) / m)", "{v}", "v", "{{m} \\over {s}}", "Speed"},
                    //
                    {"Gravitational Potential Energy", "Potential Energy",  "$$PE_g = {m \\cdot ({g} \\cdot h)}$$", "m * (g * h)","PE_g", "PE", "{{J}}", "Energy"},
                    {"Gravitational Potential Energy", "Mass", "$$m = {PE_g \\over ({g} \\cdot h)}$$", "PE / (g * h)", "m", "m", "{{kg}}", "Mass"},
                    {"Gravitational Potential Energy", "Height", "$$h = {PE_g \\over (m \\cdot {g})}$$", "PE / (m * g)", "h", "h", "{{m}}", "Length"},
                    {"Gravitational Potential Energy", "Acceleration due to Gravity", "$$g = {PE_g \\over (m \\cdot h)}$$", "PE / (m * h)", "{g}", "g", "{{m} \\over {s^2}}",
                            "Acceleration", "Acceleration due to Gravity"},
                    //
                    {"Total Mechanical Energy", "Total Mechanical Energy", "$$TME = {KE + PE}$$",  "KE + PE","TME", "TME", "{{J}}", "Energy"},
                    {"Total Mechanical Energy", "Kinetic Energy", "$$KE = {TME - PE}$$", "TME - PE", "KE", "KE", "{{J}}", "Energy"},
                    {"Total Mechanical Energy", "Potential Energy", "$$PE = {TME - KE}$$", "TME - KE", "PE", "PE", "{{J}}", "Energy"},
                    //
                    {"Average Power", "Average Power", "$$P_{av} = {\\Delta W \\over \\Delta t}$$", "W / t", "P_{av}", "P", "{{W}}", "Power"},
                    {"Average Power", "Amount of Work done", "$$\\Delta W = {P_{av} \\cdot \\Delta t}$$", "P * t", "\\Delta W", "W", "{{J}}", "Work"},
                    {"Average Power", "Time", "$$\\Delta t = {\\Delta W \\over P_{av}}$$", "W / P", "\\Delta t", "t", "{{s}}", "Duration"},
                    //
//                    {"Instantaneous Power", "Instantaneous Power", "$$P = {F \\cdot cos({a}) \\cdot {v}}$$", "F * cos(a) * v", "P", "P", "{{W}}", "Power"},
//                    {"Instantaneous Power", "Force",  "$$F = {P \\over (cos({a}) \\cdot {v})}$$", "P / (cos(a) * v)", "F", "F", "{{N}}", "Force"},
//                    {"Instantaneous Power", "Angle", "$${a} = {cos^{-1}({P \\over (F \\cdot {v})})}$$", "acos(P / (F * v))", "{a}", "a", "^{{\\circ}}", "Angle"},
//                    {"Instantaneous Power", "Velocity", "$${v} = {P \\over (F \\cdot (cos({a})))}$$", "P / (F * cos(a))", "{v}", "v", "{{m} \\over {s}}", "Speed"},
                    //
                    {"Length of Arc", "Length of Arc", "$$\\Delta S = {{r} \\cdot \\Delta \\theta}$$", "r * a", "\\Delta S", "s", "{{m}}", "Length"},
                    {"Length of Arc", "Radius", "$${r} = {\\Delta S \\over \\Delta \\theta}$$", "s / a", "{r}", "r", "{{m}}", "Length"},
                    {"Length of Arc", "Angle",  "$$\\Delta \\theta = {\\Delta S \\over {r}}$$", "s / r", "\\Delta \\theta", "a", "{{rad}}", "Angle Radians"},//
                    //
                    {"Tangential Velocity", "Tangential Velocity", "$${v} = {\\Delta S \\over \\Delta t}$$", "s / t", "{v}", "v", "{{m} \\over {s}}", "Speed"},
                    {"Tangential Velocity", "Length of Arc","$$\\Delta S = {{v} \\cdot \\Delta t}$$", "v * t", "\\Delta S", "s", "{{m}}", "Length"},
                    {"Tangential Velocity", "Time",  "$$\\Delta t = {\\Delta S \\over {v}}$$", "s / v", "\\Delta t", "t", "{{s}}", "Duration"},
                    //
                    {"Velocity around a Circle", "Velocity around a Circle", "$${v} = {(2 \\cdot \\pi) \\cdot {{r} \\over {t}}}$$", "(2 * pi) * (r / t)", "{v}", "v", "{{m} \\over {s}}", "Speed"},
                    {"Velocity around a Circle", "Radius", "$${r} = {({v} \\cdot {t}) \\over (2 \\cdot \\pi)}$$", "(v * t) / (2 * pi)", "{r}", "r", "{{m}}", "Length"},
                    {"Velocity around a Circle", "Time", "$${t} = {(2 \\cdot \\pi) \\cdot {{r} \\over {v}}}$$", "(2 * pi) * (r / v)", "{t}", "t", "{{s}}", "Duration"},
                    //
                    {"Angular Velocity", "Angular Velocity", "$$\\omega = {\\Delta \\theta \\over \\Delta t}$$", "a / t", "\\omega", "v", "{{rad} \\over {s}}", "Speed"},
                    {"Angular Velocity", "Angle", "$$\\Delta \\theta = {\\omega \\cdot \\Delta t}$$", "v * t", "\\Delta \\theta", "a", "{{rad}}", "Angle Radians"},
                    {"Angular Velocity", "Time", "$$\\Delta t = {\\Delta \\theta \\over \\omega}$$", "a / v", "\\Delta t", "t", "{{s}}", "Duration"},
                    //
                    {"Centripetal Acceleration", "Centripetal Acceleration", "$$a_c = {(\\omega)^2 \\over {r}}$$", "((w)^2) / r", "a_c", "a", "{{m} \\over {s^2}}", "Acceleration"},
                    {"Centripetal Acceleration", "Angular Velocity", "$$\\omega = {\\sqrt{{a_c \\cdot {r}}}}$$", "sqrt(a * r)", "\\omega", "w", "{{rad} \\over {s}}", "Speed"},
                    {"Centripetal Acceleration", "Radius", "$${r} = {(\\omega)^2 \\over a_c}$$", "((w)^2) / a", "{r}", "r", "{{m}}", "Length"},
                    //
                    {"Centripetal Force", "Centripetal Force", "$$F = {(m \\cdot ({v})^2) \\over {r}}$$", "(m * (v)^2) / r", "F",  "F", "{{N}}", "Force"},
                    {"Centripetal Force", "Mass", "$$m = {(F \\cdot {r}) \\over ({v})^2}$$", "(F * r) / (v)^2", "m", "m", "{{kg}}", "Mass"},
                    {"Centripetal Force", "Tangential Velocity", "$${v} = {\\sqrt{{(F \\cdot {r}) \\over m}}}$$", "sqrt((F * r) / m)", "{v}", "v", "{{m} \\over {s}}", "Speed"},
                    {"Centripetal Force", "Radius", "$${r} = {(m \\cdot ({v})^2) \\over F}$$", "(m * (v)^2) / F", "{r}", "r", "{{m}}", "Length"},
                    //
//                    {"Moment of Inertia", "Moment of Inertia", "$$I = {{m} \\cdot ({r})^2}$$", "m * (r)^2", "I", "I", "{{kg}{m^2}}", "Moment of Inertia"},
//                    {"Moment of Inertia", "Mass", "$$m = {I \\over ({r})^2}$$", "I / (r)^2", "{m}", "m", "{{kg}}", "Mass"},
//                    {"Moment of Inertia", "Distance","$${r} = {\\sqrt{{I \\over {m}}}}$$", "sqrt(I / m)", "{r}", "r", "{{m}}", "Length"},
                    //
                    {"Torque", "Torque", "$$\\tau = {F \\cdot {r}}$$", "F * r", "\\tau", "T", "{{N}{m}}", "Torque"},
                    {"Torque", "Force", "$$F = {\\tau \\over {r}}$$", "T / r", "F", "F", "{{N}}", "Force"},
                    {"Torque", "Position",  "$${r} = {\\tau \\over F}$$", "T / F", "{r}", "r", "{{m}}", "Length"},
                    //
//                    {"Angular Momentum", "Angular Momentum", "$$L = {I \\cdot \\omega}$$", "I * w", "L", "L", "{{kg}{m^3} \\over {s}}", "Angular Momentum"},
//                    {"Angular Momentum", "Moment of Inertia", "$$I = {L \\over \\omega}$$", "L / w", "I", "I", "{{kg}{m^2}}", "Moment of Inertia"},
//                    {"Angular Momentum", "Angular Velocity", "$$\\omega = {L \\over I}$$", "L / I", "\\omega", "w", "{{m} \\over {s}}", "Speed"}
            };

            for(String[] s: variables){
                Cursor c = database.query(
                        DataContract.FormulaEntry.TABLE_NAME,
                        new String[]{DataContract.FormulaEntry._ID},
                        DataContract.FormulaEntry.COLUMN_NAME + " = ?",
                        new String[]{s[0]},
                        null,
                        null,
                        null
                );
                c.moveToFirst();

                ContentValues values = new ContentValues();
                values.put(DataContract.VariableEntry.COLUMN_FORMULA_KEY,
                        c.getLong(c.getColumnIndex(DataContract.FormulaEntry._ID)));
                values.put(DataContract.VariableEntry.COLUMN_NAME, s[1]);
                values.put(DataContract.VariableEntry.COLUMN_FORMULA_DISPLAY, s[2]);
                values.put(DataContract.VariableEntry.COLUMN_FORMULA_COMPUTE, s[3]);

                values.put(DataContract.VariableEntry.COLUMN_SYMBOL_DISPLAY, s[4]);
                values.put(DataContract.VariableEntry.COLUMN_SYMBOL_COMPUTE, s[5]);
                values.put(DataContract.VariableEntry.COLUMN_UNIT, s[6]);
                try {
                    c = database.query(
                            DataContract.ConstantEntry.TABLE_NAME,
                            new String[]{DataContract.ConstantEntry._ID},
                            DataContract.ConstantEntry.COLUMN_NAME + " = ?",
                            new String[]{s[8]},
                            null,
                            null,
                            null
                    );
                    c.moveToFirst();
                    values.put(DataContract.VariableEntry.COLUMN_CONSTANT_KEY,
                            c.getLong(c.getColumnIndex(DataContract.ConstantEntry._ID)));
                } catch (Exception ex) {
                    values.put(DataContract.VariableEntry.COLUMN_CONSTANT_KEY, -1);
                }
                try {;
                    values.put(DataContract.VariableEntry.COLUMN_UNIT_TYPE, s[7]);
                }catch (Exception ex){

                }

                c.close();
                long asdf = database.insert(DataContract.VariableEntry.TABLE_NAME, null, values);
                String test = "";
            }
        }
    }


}






