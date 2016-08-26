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
                DataContract.SectionEntry.COLUMN_NAME + " TEXT NOT NULL, " +
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


        sqLiteDatabase.execSQL(SQL_CREATE_CHAPTER_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_LESSON_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_SECTION_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_IMAGE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_CONSTANT_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_FORMULA_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_FORMULA_CONSTANT_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_VARIABLE_TABLE);
        initChapters(sqLiteDatabase);
        initLessons(sqLiteDatabase);
        initSections(sqLiteDatabase);
        initImages(sqLiteDatabase);
        initFormulas(sqLiteDatabase);
        initConstants(sqLiteDatabase);
        initFormulaConstants(sqLiteDatabase);
        initVariables(sqLiteDatabase);
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
                    {"One-dimensional Motion","Scalar and Vector Values, Velocity, Acceleration, Free fall","ic_chapter_one_dimensional_motion"},
                    {"Two-dimensional Motion","Projectile Motion","ic_chapter_two_dimensional_motion"},
                    {"Isaac Newton's Laws of Motion","Friction, Free Body Diagrams","ic_chapter_newtons_laws"},
                    {"Work, Energy, and Power","Work, Energy, Power","ic_chapter_work_energy_power"},
                    {"Momentum and Impulse","Momentum and Impulse","ic_chapter_momentum_impulse"},
                    {"Uniform Circular Motion","Uniform Circular Motion, Centripetal and Centrifugal Forces, Rotational Motion","ic_chapter_uniform_circular_motion"}
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
                            "Definition, Distance and Displacement",
                            "ic_lesson_scalar_and_vector_quantities", "1", "0", "ihNZlp7iUHE", "audio_lesson_velocity"},
                    {"Velocity", "One-dimensional Motion",
                            "Definition, Speed and Velocity, Average Velocity, Instantaneous " +
                                    "Velocity",
                            "ic_lesson_velocity", "1", "1", "oRKxmXwLvUU", "audio_lesson_velocity"},
                    {"Acceleration", "One-dimensional Motion", "Definition, Acceleration, Average " +
                            "Acceleration, Instantaneous Acceleration",
                            "ic_lesson_acceleration", "1", "1", "FOkQszg1-j8", "audio_lesson_velocity"},
                    {"Free-fall", "One-dimensional Motion", "Definition, Free fall",
                            "ic_lesson_free_fall", "1", "1", "6wEEa8-RSqU", "audio_lesson_velocity"},
                    {"Projectile Motion", "Two-dimensional Motion", "Definition, " +
                            "Projectile Motion",
                            "ic_lesson_projectile_motion", "1", "1","rMVBc8cE5GU", "audio_lesson_velocity"},
                    {"Newton's Laws of Motion", "Isaac Newton's Laws of Motion", "Newton's Laws of Motion",
                            "ic_lesson_friction", "0", "0", "fmXFWi", "audio_lesson_velocity"}, //asdf
                    {"Friction", "Isaac Newton's Laws of Motion", "Definition, Two types of " +
                            "Friction",
                            "ic_lesson_friction", "1", "1", "fo_pmp5rtzo", "audio_lesson_velocity"},
                    {"Free-body Diagrams", "Isaac Newton's Laws of Motion", "Definition, Free " +
                            "Body Diagrams",
                            "ic_lesson_free_body_diagrams", "0", "0", "nDis6HbXxjg", "audio_lesson_velocity"},
                    {"Momentum and Impulse", "Momentum and Impulse", "Definition, Momentum, " +
                            "Impulse",
                            "ic_lesson_momentum_impulse", "1", "1", "XFhntPxow0U","audio_lesson_velocity"},
                    {"Law of Conservation of Energy", "Momentum and Impulse", "Definition, " +
                            "Conservation of Energy",
                            "ic_lesson_laws_of_conservation_of_energy", "0", "0", "PplaBASQ_3M", "audio_lesson_velocity"},
                    {"Work", "Work, Energy, and Power", "Definition, Work",
                            "ic_lesson_work", "1", "1", "fmXFWi", "audio_lesson_velocity"}, //asdf
                    {"Energy", "Work, Energy, and Power", "Definition, Kinetic Energy, " +
                            "Potential Energy, Total Mechanical Energy",
                            "ic_lesson_energy", "1", "1", "fmXFWi", "audio_lesson_velocity"}, //asdf
                    {"Power", "Work, Energy, and Power", "Definition, Average Power",
                            "ic_lesson_power", "1", "1", "fmXFWi", "audio_lesson_velocity"}, //asdf
                    {"Uniform Circular Motion", "Uniform Circular Motion", "Definition, " +
                            "Measurements of a Circle, Frequency, Angular Displacement, " +
                            "Length of Arc, Tangential Velocity, Angular Velocity, " +
                            "Centripetal Acceleration",
                            "ic_lesson_uniform_circular_motion", "1", "1", "bpFK2VCRHUs", "audio_lesson_velocity"},
                    {"Centripetal and Centrifugal Forces", "Uniform Circular Motion",
                            "Definition, Centripetal and Centrifugal Forces",
                            "ic_lesson_centripetal_and_centrifugal_force", "1", "1", "9s1IRJbL2Co", "audio_lesson_velocity"},
                    {"Rotational Motion", "Uniform Circular Motion", "Definition, Moment of " +
                            "Inertia, Torque, Angular Momentum",
                            "ic_lesson_rotational_motion", "1", "0", "fmXFWi", "audio_lesson_velocity"}
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
                    {"Scalar and Vector Values","Scalar and Vector Values Definition","" +
                            "<h2><b>Definition</b></h2>" +
                            "Scalar quantity:<br />" +
                            "\t•\thas a magnitude<br />" +
                            "\t•\tis one dimensional<br/><br/>" +
                            "Vector quantity:<br/>" +
                            "\t•\thas a magnitude and a direction<br/>" +
                            "\t•\tis two dimensional"},
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
                    {"Free-fall", "Free-fall Definition",
                            "<h2><b>Definition</b></h2>" +
                                    "Free fall:<br/>" +
                                    "\t•\trefers to the motion of an object where its motion is affected only by gravity<br/>" +
                                    "\t•\tacts only along the y-axis<br/><br/>" +
                                    "Acceleration due to gravity:\t<br/>" +
                                    "\t•\tis equal to 9.8 \\({m \\over s^2}\\)<br/>" +
                                    "\t•\tis constant regardless of the object’s mass<br/>"},
                    {"Free-fall", "Free-fall",
                            "<h2><b>Free fall</b></h2>|" +
                                    "An object is thrown with an initial velocity (\\(v_i\\)) along the y-axis. The position and speed of an object in free fall motion can be calculated from the " +
                                    "motion " +
                                    "equations.<br/><br/>" +
                                    "Velocity along the y-axis at any instant t:<br/>" +
                                    "$$v_f = v_i + g \\cdot t$$" +
                                    "The displacement along y-axis at any instant t:<br/>" +
                                    "$$d = {1 \\over 2}g  \\cdot t^2$$" +
                                    "In the case where an object is initially thrown upwards, the following formula can be used:<br/>" +
                                    "$$v_f^2 = v_i^2 + 2 \\cdot g \\cdot d$$" +
                                    "Where:<br/>" +
                                    "\\(v_i\\) = initial velocity<br/>" +
                                    "\\(v_f\\) = final velocity<br/>" +
                                    "\\(d\\) = vertical distance<br/>" +
                                    "\\(g\\) = acceleration due to gravity<br/>" +
                                    "\\(t\\) = time duration"},
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
                                    "\\(t\\) = time duration<br/><br/>" +
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
                                    "$$v = {\\Delta S \\over \\Delta t}$$" +
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
                                    "$$a_c = r \\cdot \\omega ^2$$"},
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
                    {"Rotational Motion", "Rotational Motion Definition",
                            "<p><h2><b>Definition</b></h2>" +
                                    "Rotational Motion:<br/>" +
                                    "\t•\tis a motion of an object in a circular path around a center (or point) of rotation<br/>"},
                    {"Rotational Motion", "Moment of Inertia",
                            "<h2><b>Moment of Inertia</b></h2>" +
                                    "The moment of inertia \\(I\\) is the measure of the object’s resistance to the change to its rotation. It is dependent to the object’s mass \\(m\\) and distance \\(r\\) of the mass further from the center of the rotational motion.<br/><br/>" +
                                    "Moment of inertia can be calculated using this formula:<br/>" +
                                    "$$I = m \\cdot r^2$$"},
                    {"Rotational Motion", "Torque",
                            "<h2><b>Torque</b></h2>" +
                                    "The torque \\(\\tau \\) is the twisting force \\(F\\) that tends to cause the rotation of an object which is at position \\(r\\) from its axis of rotation. " +
                                    "The position is perpendicular to the force.<br/><br/>" +
                                    "Torque can be calculated using this formula:<br/>" +
                                    "$$\\tau = r \\cdot F$$"},
                    {"Rotational Motion", "Angular Momentum",
                            "<h2><b>Angular Momentum</b></h2>" +
                                    "The angular momentum \\(L\\) is the quantity of rotation of a body. It is dependent on the moment of inertia \\(I\\) of the object and its angular velocity vector \\(\\omega \\).<br/><br/>" +
                                    "Angular momentum can be calculated using this formula:<br/>" +
                                    "$$L = I \\cdot \\omega$$"}
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
                    {"Distance and Displacement", "img_lesson_vector_and_scalar_quantities", "Figure 1: Vector and Scalar Quantities"},
                    {"Average Velocity", "img_lesson_velocity", "Figure 2: Velocity"},
                    {"Free-fall", "img_lesson_free_fall", "Figure 3: Free fall"},
                    {"Projectile Motion", "img_lesson_projectile_motion", "Figure 4: Projectile Motion"},
                    {"Friction Definition", "img_lesson_friction", "Figure 5: Friction"},
                    {"Free Body Diagrams", "img_lesson_free_body_diagram", "Figure 6: Free Body Diagrams"},
                    {"Work Definition", "img_lesson_work", "Figure 7: Work"},
                    {"Measurements of a Circle", "img_lesson_uniform_circular_motion_1", "Figure 8: Uniform Circular Motion"},
                    {"Angular Displacement", "img_lesson_uniform_circular_motion_2", "Figure 9: Angular Displacement, Length of Arc, and Tangential Velocity"},
                    {"Centripetal and Centrifugal Forces", "img_lesson_centripetal_and_centrifugal_forces", "Figure 10: Centripetal and Centrifugal Forces"}

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
                    {"Centripetal Acceleration", "Uniform Circular Motion", "$$a_c = r \\cdot \\omega ^2$$"},
                    {"Centripetal Force","Centripetal and Centrifugal Forces", "$$F = {m \\cdot v^2 \\over r} $$"},
                    {"Moment of Inertia", "Rotational Motion", "$$I = m \\cdot r^2$$"},
                    {"Torque","Rotational Motion", "$$\\tau = r \\cdot F$$"},
                    {"Angular Momentum", "Rotational Motion", "$$L = I \\cdot \\omega$$"}
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
                    {"Vertical Components", "Vertical Distance", "$${y} = {(dv_{yi} \\cdot {t}) + ({1 \\over 2} \\cdot g \\cdot ({t})^2)}$$", "(vi * t) + ((1 / 2) * g * (t)^2)","{y}", "y", "{{m}}",
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
                    {"Vertical Velocity at a Certain Time", "Vertical Velocity","$$v_y = {(g \\cdot t) + v_{yi}}$$", "vi + (g * t)", "v_y", "v", "{{m} \\over {s}}", "Speed"},
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
                    {"Work", "Angle", "$$\\theta = {cos^{-1}({W \\over {\\vec{F} \\cdot \\vec{x}}})}$$", "acos(W / (F * x))", "\\theta", "a", "^{{\\circ}}", "Angle"},
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
                    {"Centripetal Acceleration", "Centripetal Acceleration", "$$a_c = {{r} \\cdot (\\omega)^2}$$", "r * (w)^2", "a_c", "a", "{{m} \\over {s^2}}", "Acceleration"},
                    {"Centripetal Acceleration", "Angular Velocity", "$$\\omega = {\\sqrt{{a_c \\over {r}}}}$$", "sqrt(a / r)", "\\omega", "w", "{{rad} \\over {s}}", "Speed"},
                    {"Centripetal Acceleration", "Radius", "$${r} = {a_c \\over (\\omega)^2}$$", "a / (w)^2", "{r}", "r", "{{m}}", "Length"},
                    //
                    {"Centripetal Force", "Centripetal Force", "$$F = {(m \\cdot ({v})^2) \\over {r}}$$", "(m * (v)^2) / r", "F",  "F", "{{N}}", "Force"},
                    {"Centripetal Force", "Mass", "$$m = {(F \\cdot {r}) \\over ({v})^2}$$", "(F * r) / (v)^2", "m", "m", "{{kg}}", "Mass"},
                    {"Centripetal Force", "Tangential Velocity", "$${v} = {\\sqrt{{(F \\cdot {r}) \\over m}}}$$", "sqrt((F * r) / m)", "{v}", "v", "{{m} \\over {s}}", "Speed"},
                    {"Centripetal Force", "Radius", "$${r} = {(m \\cdot ({v})^2) \\over F}$$", "(m * (v)^2) / F", "{r}", "r", "{{m}}", "Length"},
                    //
                    {"Moment of Inertia", "Moment of Inertia", "$$I = {{m} \\cdot ({r})^2}$$", "m * (r)^2", "I", "I", "{{kg}{m^2}}", "Moment of Inertia"},
                    {"Moment of Inertia", "Mass", "$$m = {I \\over ({r})^2}$$", "I / (r)^2", "{m}", "m", "{{kg}}", "Mass"},
                    {"Moment of Inertia", "Distance","$${r} = {\\sqrt{{I \\over {m}}}}$$", "sqrt(I / m)", "{r}", "r", "{{m}}", "Length"},
                    //
                    {"Torque", "Torque", "$$\\tau = {F \\cdot {r}}$$", "r * F", "\\tau", "T", "{{N}}{m}}", "Torque"},
                    {"Torque", "Force", "$$F = {\\tau \\over {r}}$$", "T / r", "F", "F", "{{N}}", "Force"},
                    {"Torque", "Position",  "$${r} = {\\tau \\over F}$$", "T / F", "{r}", "r", "{{m}}", "Length"},
                    //
                    {"Angular Momentum", "Angular Momentum", "$$L = {I \\cdot \\omega}$$", "I * w", "L", "L", "{{kg}{m^3} \\over {s}}", "Angular Momentum"},
                    {"Angular Momentum", "Moment of Inertia", "$$I = {L \\over \\omega}$$", "L / w", "I", "I", "{{kg}{m^2}}", "Moment of Inertia"},
                    {"Angular Momentum", "Angular Velocity", "$$\\omega = {L \\over I}$$", "L / I", "\\omega", "w", "{{m} \\over {s}}", "Speed"}
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







