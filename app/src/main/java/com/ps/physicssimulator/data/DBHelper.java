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
                DataContract.LessonEntry.COLUMN_HAS_CALCULATOR + " INTEGER NOT NULL," +
                DataContract.LessonEntry.COLUMN_HAS_SIMULATION + " INTEGER NOT NULL," +
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
                DataContract.VariableEntry.COLUMN_SYMBOL + " TEXT, " +
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
                    {"Momentum and Impulse","Momentum and Impulse","ic_chapter_momentum_impulse"},
                    {"Work, Energy, and Power","Work, Energy, Power","ic_chapter_work_energy_power"},
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
                            "<p><h2><b>Definition</b></h2><br/>" +
                                    "Scalar quantity:<br />" +
                                    "•\thas a magnitude<br />" +
                                    "•\tis one dimensional<br/>" +
                                    "Vector quantity:<br/>" +
                                    "•\thas a magnitude and a direction<br/>" +
                                    "•\tis two dimensional<br/>" +
                                    "<h2><b>Distance and Displacement</b></h2><br/>" +
                                    "Distance:<br/>" +
                                    "•\tis a scalar quantity<br/>" +
                                    "•\tmeasures the interval between two points that is measured along the actual path that was made that connects them<br/>" +
                                    "Displacement:<br/>" +
                                    "•\tis a vector quantity<br/>" +
                                    "•\tmeasures the interval between two points along the shortest path that connects them <br/>" +
                                    "//<Insert diagram><br/>" +
                                    "The SI unit used for distance and displacement is meters\\($m\\).<br/>" +
                                    "Displacement can be calculated using this formula:<br/>" +
                                    "$$d = x_f - x_i$$<br/>" +
                                    "Where:<br/>" +
                                    "\\(x_i\\) = initial position of the object<br/>" +
                                    "\\(x_f\\) = final position of the object<br/></p>",
                            "",
                            "1",
                            "0"},
                    {"Velocity", "One-dimensional Motion",
                            "Definition, Speed and Velocity, Average Velocity, Instantaneous " +
                                    "Velocity",
                            "<p><h2><b>Definition</b></h2><br/>" +
                                    "Speed:<br/>" +
                                    "•\tshows the rate at which an object is able to move<br/>" +
                                    "Velocity:<br/>" +
                                    "•\tshows the rate at which an object is able to move in a given direction<br/>" +
                                    "The SI unit used for speed and velocity is meter per second (m/s).<br/>" +
                                    "<h2><b>Speed and Velocity</b></h2><br/>" +
                                    "Speed can be calculated by dividing distance over time:<br/>" +
                                    "//<Insert formula><br/>" +
                                    "Velocity can be calculated by dividing displacement over time:<br/>" +
                                    "//<Insert formula><br/>" +
                                    "<h2><b>Average Velocity</b></h2><br/>" +
                                    "Average velocity is the ratio of total displacement (Δx) taken over time interval (Δt)<br/>" +
                                    "//<Insert diagram><br/>" +
                                    "The average velocity from when the object starts to move up to the time when the object stops can be described as:<br/>" +
                                    "//<Insert formula><br/>" +
                                    "Where:<br/>" +
                                    "<b>Δx</b> = change in velocity<br/>" +
                                    "<b>Δt</b> = change in time<br/>" +
                                    "<b>x1</b> = initial position of the object<br/>" +
                                    "<b>x2</b> = final position of the object<br/>" +
                                    "<b>t1</b> = time when the object was at position x1<br/>" +
                                    "<b>t2</b> = time when the object was at position x2<br/>" +
                                    "<h2><b>Instantaneous Velocity</b></h2><br/>" +
                                    "Instantaneous velocity is the measure of the velocity of an object at a particular moment.<br/></p>",
                            "",
                            "1",
                            "1"},
                    {"Acceleration", "One-dimensional Motion", "Definition, Acceleration, Average " +
                            "Acceleration, Instantaneous Acceleration",
                            "<p><h2><b>Definition</b></h2><br/>" +
                                    "Acceleration:<br/>" +
                                    "•\tis the rate of change of velocity with respect to time<br/>" +
                                    "•\tcan be positive (speeding up) or negative (slowing down)<br/>" +
                                    "The SI unit used for acceleration is meter per second squared (m/s²).<br/>" +
                                    "<h2><b>Acceleration</b></h2><br/>" +
                                    "Acceleration is the ratio of total velocity change (Δv) taken over time interval (Δt).<br/>" +
                                    "//<Insert formula><br/>" +
                                    "<h2><b>Average Acceleration</b></h2><br/>" +
                                    "Average acceleration can be calculated using this formula:<br/>" +
                                    "//<Insert formula><br/>" +
                                    "Where:<br/>" +
                                    "<b>v1</b> = initial velocity of the object<br/>" +
                                    "<b>v2</b> = final velocity of the object<br/>" +
                                    "<b>t1</b> = time when the object had velocity x1<br/>" +
                                    "<b>t2</b> = time when the object had velocity x2<br/>" +
                                    "<h2><b>Instantaneous Acceleration</b></h2><br/>" +
                                    "Instantaneous acceleration is the change of velocity at infinitesimal (very small) time interval.<br/></p>",
                            "", "1", "1"},
                    {"Free-fall", "One-dimensional Motion", "Definition, Free fall",
                            "<p><h2><b>Definition</b></h2><br/>" +
                                    "Free fall:<br/>" +
                                    "•\trefers to the motion of an object where its motion is affected only by gravity<br/>" +
                                    "•\tacts only along the y-axis<br/>" +
                                    "Acceleration due to gravity:\t<br/>" +
                                    "•\tis equal to 9.8 m/s²<br/>" +
                                    "•\tis constant regardless of the object’s mass<br/>" +
                                    "<h2><b>Free fall</b></h2><br/>" +
                                    "//<Insert diagram><br/>" +
                                    "An object is thrown with an initial velocity u along the y-axis. The position and speed of an object in free fall motion can be calculated from the motion equations.<br/>" +
                                    "Velocity along the y-axis at any instant t:<br/>" +
                                    "//<Insert formula><br/>" +
                                    "The displacement along y-axis at any instant t:<br/>" +
                                    "//<Insert formula><br/></p>",
                            "", "1", "1"},
                    {"Projectile Motion", "Two-dimensional Motion", "Definition, " +
                            "Projectile Motion",
                            "<p><h2><b>Definition</b></h2><br/>" +
                                    "Projectile:<br/>" +
                                    "•\tis an object thrown with an initial velocity in a vertical plane<br/>" +
                                    "•\tmoves in two dimensions<br/>" +
                                    "•\tacts under the action of gravity alone without being propelled<br/>" +
                                    "Projectile Motion:<br/>" +
                                    "•\tis the motion done by the projectile<br/>" +
                                    "Trajectory:<br/>" +
                                    "•\tIs the path passed by the projectile<br/>" +
                                    "<h2><b>Projectile Motion</b></h2><br/>" +
                                    "Projectile motion is a two dimensional motion. Any two dimensional motion case can be split up into two cases of one dimensional motion.<br/>" +
                                    "An important reminder is that the motion along the x-axis does not affect the motion along the y-axis. It also applies in vice versa. Each motion along each axis is independent of each other.<br/>" +
                                    "//<Insert diagram><br/>" +
                                    "Projectile motion formula is given by the following:<br/>" +
                                    "Horizontal distance (m):<br/>" +
                                    "//<Insert formula><br/>" +
                                    "Horizontal velocity (m/s):<br/>" +
                                    "//<Insert formula><br/>" +
                                    "Vertical distance (m):<br/>" +
                                    "//<Insert formula><br/>" +
                                    "Vertical velocity (m/s):<br/>" +
                                    "//<Insert formula><br/>" +
                                    "Where:<br/>" +
                                    "<b>vx</b> = velocity of the object along x-axis<br/>" +
                                    "<b>vx1</b> = initial velocity of the object along x-axis<br/>" +
                                    "<b>vy</b> = velocity of the object along y-axis<br/>" +
                                    "<b>vy1</b> = initial velocity of the object along y-axis<br/>" +
                                    "<b>g</b> = acceleration due to gravity <br/>" +
                                    "<b>t</b> = time duration<br/>" +
                                    "Formulas related to trajectory motion is given by the following:<br/>" +
                                    "Time of flight (s):<br/>" +
                                    "//<Insert formula><br/>" +
                                    "Maximum height reached (m):<br/>" +
                                    "//<Insert formula><br/>" +
                                    "Horizontal range (m):<br/>" +
                                    "//<Insert formula><br/>" +
                                    "Where:<br/>" +
                                    "<b>v1</b> = initial velocity of the object<br/>" +
                                    "<b>sin θ</b> = component along y-axis<br/>" +
                                    "<b>cos θ</b> = component along x-axis<br/></p>",
                            "", "1", "1"},
                    {"Friction", "Isaac Newton's Laws of Motion", "Definition, Two types of " +
                            "Friction",
                            "<p><h2><b>Definition</b></h2><br/>" +
                                    "Friction Force:<br/>" +
                                    "•\tis the force that is exerted by a surface as an object moves across it<br/>" +
                                    "//<Insert Diagram><br/>" +
                                    "When an object is being pushed across a surface, the surface offers resistance to the movement of the object.<br/>" +
                                    "The friction force is opposite to the direction of the motion of the object.<br/>" +
                                    "<h2><b>Two types of Friction</b></h2><br/>" +
                                    "1. Static Friction:<br/>" +
                                    "•\tis the friction between multiple solid objects that are not moving relative to each other<br/>" +
                                    "2. Kinetic Friction:<br/>" +
                                    "•\tis the friction between multiple solid objects that are moving relative to each other<br/>" +
                                    "The friction force is dependent on two factors:<br/>" +
                                    "•\tthe material of the objects that are in contact<br/>" +
                                    "•\tthe force that pushes both surfaces together<br/>" +
                                    "The following is the equation that summarizes the topic:<br/>" +
                                    "//<Insert formula><br/>" +
                                    "Where:<br/>" +
                                    "<b>Ff</b> = frictional force <br/>" +
                                    "<b>μ</b> = coefficient of friction<br/>" +
                                    "<b>Fn</b> = normal force<br/></p>",
                            "", "1", "1"},
                    {"Free-body Diagrams", "Isaac Newton's Laws of Motion", "Definition, Free " +
                            "Body Diagrams",
                            "<p><h2><b>Definition</b></h2><br/>" +
                                    "Free Body Diagrams:<br/>" +
                                    "•\talso known as “Force Diagram”<br/>" +
                                    "•\ta graphical illustration used to visualize the applied forces, movements and resulting reaction on a body in a steady state condition<br/>" +
                                    "•\tshows all the forces acting on an object or a “body” that is singled out from or “freed” from a group of objects<br/>" +
                                    "<h2><b>Free Body Diagrams</b></h2><br/>" +
                                    "There are four types of forces typically used in free body diagrams. These are the forces used.<br/>" +
                                    "1. Gravity (G) - its force is directed towards the ground (downwards).<br/>" +
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
                                    "//<Insert diagram><br/>" +
                                    "<br/>" +
                                    "Here are a few hints when working with free body diagrams: <br/>" +
                                    "•\tThe force of gravity always points straight down. <br/>" +
                                    "•\tThe normal force will always push straight up from a surface.<br/>" +
                                    "•\tIf an object is moving in one direction, friction is acting in the opposite direction.<br/>" +
                                    "•\tThink about whether the forces on opposite sides are balanced or not. If they are, the two arrows should be about the same length. <br/></p>",
                            "", "1", "1"},
                    {"Momentum and Impulse", "Momentum and Impulse", "Definition, Momentum, " +
                            "Impulse",
                            "<p><h2><b>Definition</b></h2><br/>" +
                                    "Momentum:<br/>" +
                                    "•\tis defined as “mass in motion”<br/>" +
                                    "•\tdepends upon the variables mass and velocity<br/>" +
                                    "•\tis a vector quantity<br/>" +
                                    "<h2><b>Momentum</b></h2><br/>" +
                                    "In terms of an equation, the momentum of an object is equal to the mass of the object times the velocity of the object. <br/>" +
                                    "<b><i>Momentum = mass * velocity</i></b><br/>" +
                                    "In physics, the symbol for the quantity momentum is the lower case p. Thus, the above equation can be rewritten as:<br/>" +
                                    "//<Insert formula><br/>" +
                                    "where:<br/>" +
                                    "<b>m</b> = mass of the object<br/>" +
                                    "<b>v</b> = velocity of the object<br/>" +
                                    "The equation illustrates that momentum is:<br/>" +
                                    "•\tdirectly proportional to an object's mass <br/>" +
                                    "•\tdirectly proportional to the object's velocity<br/>" +
                                    "Momentum is also a vector quantity. The momentum of an object then is fully described by both magnitude and direction.<br/>" +
                                    "<h2><b>Impulse</b></h2><br/>" +
                                    "Impulse is known as quantity force multiplied by time. <br/>" +
                                    "And since the quantity m•Δv is the change in momentum. The equation really says that the Impulse is equal to Change in Momentum.<br/>" +
                                    "The physics of collisions are governed by the laws of momentum. The first law is the equation known as the impulse-momentum change equation. The law can be expressed this way:<br/>" +
                                    "//<Insert formula><br/>" +
                                    "In a collision:<br/>" +
                                    "•\tobjects experience an impulse<br/>" +
                                    "•\tthe impulse causes and is equal to the change in momentum<br/></p>",
                            "", "1", "1"},
                    {"Law of Conservation of Energy", "Momentum and Impulse", "Definition, " +
                            "Conservation of Energy",
                            "<p><h2><b>Definition</b></h2><br/>" +
                                    "Law of Conservation of Energy<br/>" +
                                    "•\tstates that the total energy of an isolated system remains constant<br/>" +
                                    "•\tIt implies that energy can neither be created nor destroyed, but can be change from one form to another<br/>" +
                                    "•\tthe change in energy of an object due to a transformation is equal to the work done on the object or by the object for that transformation<br/>" +
                                    "<h2><b>Conservation of Energy</b></h2><br/>" +
                                    "The different types of energy are the potential energy, kinetic energy, and the total mechanical energy. These types of energy will further be discussed in a different part (Energy) of this application.<br/>" +
                                    "The basic formula for the conservation of energy is:<br/>" +
                                    "//<Insert formula><br/>" +
                                    "The formula can be more explicitly written as Conservation of Energy Equation depending on the context. <br/>" +
                                    "•\tAn object when dropped from a height transforms its potential energy into kinetic energy.<br/>" +
                                    "Mathematically it can be expressed as a Conservation of Energy Equation as follows:<br/>" +
                                    "//<Insert formula><br/>" +
                                    "Where:<br/>" +
                                    "<b>m</b> = mass of the object<br/>" +
                                    "<b>v</b> = final velocity after falling from a height of <b>h</b><br/>" +
                                    "<b>g</b> = acceleration due to gravity<br/>" +
                                    "<br/></p>",
                            "", "0", "0"},
                    {"Work", "Work, Energy, and Power", "Definition, Work",
                            "<p><h2><b>Definition</b></h2><br/>" +
                                    "Work:<br/>" +
                                    "•\tis the measure of energy being transferred occurring when an object is moved over a distance<br/>" +
                                    "//<Insert diagram><br/>" +
                                    "The SI unit used for work is Joule (J). Joule’s base units is kg • m2/s2.<br/>" +
                                    "<h2><b>Work</b></h2><br/>" +
                                    "A force is doing work when it acts on an object which displaces it from the point of application.<br/>" +
                                    "Constant work can be calculated using this formula:<br/>" +
                                    "//<Insert formula><br/>" +
                                    "Where:<br/>" +
                                    "<b>→F</b> = force vector<br/>" +
                                    "<b>→x</b> = position vector<br/></p>",
                            "", "1", "1"},
                    {"Energy", "Work, Energy, and Power", "Definition, Kinetic Energy, " +
                            "Potential Energy, Total Mechanical Energy",
                            "<h2><b>Definition</b></h2><br/>" +
                            "Energy:<br/>" +
                            "•\tis the capacity of performing work<br/>" +
                            "•\tmay exist in various forms (potential, kinetic, electric, chemical etc.)<br/>" +
                            "The SI unit for energy is the same with work, Joule (J).<br/>" +
                            "<h2><b>Kinetic Energy</b></h2><br/>" +
                            "Kinetic energy is an energy that is possessed by means of its motion.<br/>" +
                            "Kinetic energy can be calculated using this formula:<br/>" +
                            "//<Insert formula><br/>" +
                            "Where:<br/>" +
                            "<b>m</b> = mass of the object<br/>" +
                            "<b>v</b> = velocity of the object<br/>" +
                            "<h2><b>Potential Energy</b></h2><br/>" +
                            "Potential energy is an energy that is possessed by means of its position relative to others.<br/>" +
                            "Gravitational potential energy can be calculated using this formula:<br/>" +
                            "//<Insert formula><br/>" +
                            "Where:<br/>" +
                            "<b>h</b> = height of the object above the ground<br/>" +
                            "<b>g</b> = acceleration due to gravity<br/>" +
                            "Spring potential energy can be calculated using this formula:<br/>" +
                            "//<Insert formula><br/>" +
                            "Where:<br/>" +
                            "<b>k</b> = spring constant measured in Newton per meter<br/>" +
                            "<b>x</b> = amount spring is displaced from initial point<br/>" +
                            "<h2><b>Total Mechanical Energy</b></h2><br/>" +
                            "Total mechanical energy is the sum of the kinetic and potential energy of a conservative system:<br/>" +
                            "//<Insert formula><br/>",
                            "", "1", "1"},
                    {"Power", "Work, Energy, and Power", "Definition, Average Power, " +
                            "Instantaneous Power",
                            "<p><h2><b>Definition</b></h2><br/>" +
                                    "Power:<br/>" +
                                    "•\tis the rate of doing work<br/>" +
                                    "•\thas no direction<br/>" +
                                    "•\tis a scalar quantity<br/>" +
                                    "The SI unit for power is Watt (W), or Joule per second (J/s).<br/>" +
                                    "<h2><b>Average Power</b></h2><br/>" +
                                    "Average power, or simple “power”, is the average amount of work done converted per unit of time.<br/>" +
                                    "Average power can be calculated by using this formula:<br/>" +
                                    "//<Insert formula><br/>" +
                                    "Where:<br/>" +
                                    "<b>ΔW</b> = amount of work performed<br/>" +
                                    "<b>Δt</b> = time duration<br/>" +
                                    "<h2><b>Instantaneous Power</b></h2><br/>" +
                                    "Instantaneous power is the limiting value of the average power as the time interval approaches to zero.<br/>" +
                                    "Average power can be calculated by using this formula:<br/>" +
                                    "//<Insert formula><br/>" +
                                    "Where:<br/>" +
                                    "<b>W</b> = work done<br/>" +
                                    "<b>t</b> = time duration<br/>" +
                                    "<b>F</b> = force applied on the object<br/>" +
                                    "<b>x</b> = path made by the object<br/>" +
                                    "<b>a</b> = angle between the force and the position vectors<br/>" +
                                    "<b>v</b> = velocity of the object<br/></p>",
                            "", "1", "1"},
                    {"Uniform Circular Motion", "Uniform Circular Motion", "Definition, " +
                            "Measurements of a Circle, Frequency, Angular Displacement, " +
                            "Length of Arc, Tangential Velocity, Angular Velocity, " +
                            "Centripetal Acceleration",
                            "<p><h2><b>Definition</b></h2><br/>" +
                                    "Uniform Circular Motion:<br/>" +
                                    "•\tis the motion of an object traveling at a constant speed on a path that is circular<br/>" +
                                    "<h2><b>Measurements of a Circle</b></h2><br/>" +
                                    "The arc of a circle is a portion of the circumference.<br/>" +
                                    "The length of an arc is the length of its portion of the circumference.<br/>" +
                                    "The radian is the standard unit of angular measure. When it is drawn as a central angle, it subtends an arc whose length is equal to the length of the radius of the circle.<br/>" +
                                    "//<Insert diagram><br/>" +
                                    "The relationship between the degrees and radians is:<br/>" +
                                    "//<Insert formula><br/>" +
                                    "<h2><b>Frequency</b></h2><br/>" +
                                    "The frequency (f) is the number of revolutions completed per time unit.<br/>" +
                                    "The SI unit used for frequency is hertz (Hz). Hertz’ base units is 1/s.<br/>" +
                                    "Another unit of measure for frequency is revolutions per minute (RPM). 60 RPM is equal to 1 hertz.<br/>" +
                                    "Period (T):<br/>" +
                                    "•\tis the time an object takes to travel one revolution around the circle.<br/>" +
                                    "The SI unit used for period is second (s). This is based on the reciprocal value of frequency where T = 1/f.<br/>" +
                                    "<h2><b>Angular Displacement</b></h2><br/>" +
                                    "//<Insert diagram><br/>" +
                                    "The angular displacement (Δθ) is the angle traveled by the object while moving from point B to C around the circular path.<br/>" +
                                    "<h2><b>Length of Arc</b></h2><br/>" +
                                    "The length of arc (ΔS) is directly proportional to the angular displacement subtended traced at the center of circle by the ends of the arc.<br/>" +
                                    "Length of arc can be calculated using this formula:<br/>" +
                                    "//<Insert formula><br/>" +
                                    "<h2><b>Tangential Velocity</b></h2><br/>" +
                                    "The tangential velocity (v) is the velocity measured at any tangential point in a circle.<br/>" +
                                    "Tangential velocity can be calculated using this formula:<br/>" +
                                    "//<Insert formula><br/>" +
                                    "The velocity around the circle can be calculated using this formula:<br/>" +
                                    "//<Insert formula><br/>" +
                                    "<h2><b>Angular Velocity</b></h2><br/>" +
                                    "The angular velocity (ω) is rate of change of angular position of a body that is rotating.<br/>" +
                                    "The SI unit used for angular velocity is radian per second (rad/s).<br/>" +
                                    "Angular velocity can be calculated using this formula:<br/>" +
                                    "//<Insert formula><br/>" +
                                    "Since angular velocity is a vector quantity, it is defined as positive when the motion in the circle is in counter-clockwise, and is defined as negative when the motion in the circle is in clockwise.<br/>" +
                                    "The relationship between the linear velocity and angular velocity is:<br/>" +
                                    "//<Insert formula><br/>" +
                                    "<h2><b>Centripetal Acceleration</b></h2><br/>" +
                                    "The centripetal acceleration is the rate of change of tangential velocity.<br/>" +
                                    "The centripetal acceleration is always pointing towards the center of the circle in motion.<br/>" +
                                    "Centripetal acceleration can be calculated using this formula:<br/>" +
                                    "//<Insert formula><br/></p>",
                            "", "1", "1"},
                    {"Centripetal and Centrifugal Forces", "Uniform Circular Motion",
                            "Definition, Centripetal and Centrifugal Forces",
                            "<p><h2><b>Definition</b></h2><br/>" +
                                    "Centripetal Force:<br/>" +
                                    "•\tis a force that acts on an object that is moving in a circular path and is directed towards the center where the object is moving<br/>" +
                                    "Centrifugal Force:<br/>" +
                                    "•\tis the opposing reaction force of the centripetal force<br/>" +
                                    "•\tis a force acting outwards of an object that is moving in a circular path<br/>" +
                                    "<h2><b>Centripetal and Centrifugal Forces</b></h2><br/>" +
                                    "According the Newton’s second law motion, where there is an acceleration (centripetal acceleration), there is a force (centripetal force).<br/>" +
                                    "//<Insert diagram><br/>" +
                                    "Magnitude of the centripetal force can be calculated using this formula:<br/>" +
                                    "//<Insert formula><br/>" +
                                    "Where:<br/>" +
                                    "<b>m</b> = mass of the object<br/>" +
                                    "<b>v</b> = tangential velocity of the object<br/>" +
                                    "<b>r</b> = radius of curvature cause by the force<br/>" +
                                    "Newton’s third law of motion states that every action has an equal and opposite reaction. Therefore, in this case, there must be an equal and opposite reaction force to the centripetal force which is called the centrifugal force.<br/></p>",
                            "", "1", "0"},
                    {"Rotational Motion", "Uniform Circular Motion", "Definition, Moment of " +
                            "Inertia, Torque, Angular Momentum",
                            "<p><h2><b>Definition</b></h2><br/>" +
                                    "Rotational Motion:<br/>" +
                                    "•\tis a motion of an object in a circular path around a center (or point) of rotation<br/>" +
                                    "<h2><b>Moment of Inertia</b></h2><br/>" +
                                    "The moment of inertia (I) is the measure of the object’s resistance to the change to its rotation. It is dependent to the object’s mass (m) and distance (r) of the mass further from the center of the rotational motion.<br/>" +
                                    "Moment of inertia can be calculated using this formula:<br/>" +
                                    "//<Insert formula><br/>" +
                                    "<h2><b>Torque</b></h2><br/>" +
                                    "The torque (τ) is the twisting force (F) that tends to cause the rotation of an object which is at position (r) from its axis of rotation. <br/>" +
                                    "Torque can be calculated using this formula:<br/>" +
                                    "//<Insert formula><br/>" +
                                    "<h2><b>Angular Momentum</b></h2><br/>" +
                                    "The angular momentum (L) is the quantity of rotation of a body. It is dependent on the moment of inertia (I) of the object and its angular velocity vector (ω).<br/>" +
                                    "Angular momentum can be calculated using this formula:<br/>" +
                                    "//<Insert formula><br/></p>",
                            "", "1", "0"}
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
                values.put(DataContract.LessonEntry.COLUMN_TITLE, s[0]);
                values.put(DataContract.LessonEntry.COLUMN_CHAPTER_KEY,
                        c.getLong(c.getColumnIndex(DataContract.ChapterEntry._ID)));
                values.put(DataContract.LessonEntry.COLUMN_DESCRIPTION, s[2]);
                values.put(DataContract.LessonEntry.COLUMN_CONTENT, s[3]);
                values.put(DataContract.LessonEntry.COLUMN_LOGO, s[4]);
                values.put(DataContract.LessonEntry.COLUMN_HAS_CALCULATOR, s[5]);
                values.put(DataContract.LessonEntry.COLUMN_HAS_SIMULATION, s[6]);

                c.close();

                database.insert(DataContract.LessonEntry.TABLE_NAME, null, values);
            }
        }
    }

    private void initSections(SQLiteDatabase database){
        if(database.isOpen()){
            String[][] sections = {
                    {"Scalar and Vector Values","Scalar and Vector Values Definition","" +
                            "<p><h2><b>Definition</b></h2><br/>" +
                            "Scalar quantity:<br />" +
                            "\t•\thas a magnitude<br />" +
                            "\t•\tis one dimensional<br/>|" +
                            "Vector quantity:<br/>" +
                            "\t•\thas a magnitude and a direction<br/>" +
                            "\t•\tis two dimensional<br/></p>"},
                    {"Scalar and Vector Values","Distance and Displacement",
                            "<h2><b>Distance and Displacement</b></h2><br/>" +
                            "Distance:<br/>" +
                            "•\tis a scalar quantity<br/>" +
                            "•\tmeasures the interval between two points that is measured along the actual path that was made that connects them<br/>" +
                            "Displacement:<br/>" +
                            "•\tis a vector quantity<br/>" +
                            "•\tmeasures the interval between two points along the shortest path that connects them <br/>"},
                    {"Scalar and Vector Values","Scalar and Vector Values Formula",
                            "The SI unit used for distance and displacement is meters\\($m\\).<br/>" +
                            "Displacement can be calculated using this formula:<br/>" +
                            "$$d = x_f - x_i$$<br/>" +
                            "Where:<br/>" +
                            "\\(x_i\\) = initial position of the object<br/>" +
                            "\\(x_f\\) = final position of the object<br/></p>"}
            };

            for(String[] s: sections){
                Cursor c = database.query(
                        DataContract.LessonEntry.TABLE_NAME,
                        new String[]{DataContract.LessonEntry._ID},
                        DataContract.LessonEntry.COLUMN_TITLE + " = ?",
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
                    {"Scalar and Vector Values Definition", "ic_chapter_momentum_impulse", "Figure 1: asdfasdf"},
                    {"Scalar and Vector Values Definition", "ic_chapter_two_dimensional_motion", "Figure 2: qwerqwer"}
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
                    {"Speed", "Velocity", "$$s = {d \\over t}$$", ""},
                    {"Velocity", "Velocity", "$$v = {x_f - x_i \\over t}$$", ""},
                    {"Average Velocity", "Velocity", "$$v_{av} = {x_f - x_i \\over t_f - t_i}$$", ""},
                    {"Acceleration", "Acceleration", "$$a = {v_f - v_i \\over t}$$", ""},
                    {"Average Acceleration","Acceleration", "$$a_{av} = {v_f - v_i \\over t_f - t_i}$$", ""},
                    {"Free-fall Velocity","Free-fall", "$$v_y = u + g \\cdot t$$", ""},
                    {"Free-fall Displacement", "Free-fall", "$$y = {1 \\over 2}g  \\cdot t^2$$", ""},
                    {"Horizontal Distance", "Projectile Motion", "$$x = v_{x}t$$", ""},
                    {"Horizontal Velocity", "Projectile Motion", "$$v_x = v_{xi}$$", ""},
                    {"Vertical Distance",  "Projectile Motion", "$$y = v_{yi}t - {1 \\over 2}g \\cdot t^2$$", ""},
                    {"Vertical Velocity",  "Projectile Motion", "$$v_y = v_{yi} - g \\cdot t$$", ""},
                    {"Time of Flight", "Projectile Motion", "$$t = {2v_{1}sin \\Theta \\over g}$$", ""},
                    {"Maximum Height Reached", "Projectile Motion", "$$H = {v_{1}^{2}sin^{2} \\Theta \\over 2g}$$", ""},
                    {"Horizontal Range", "Projectile Motion", "$$R = {v_{1}^{2}sin2 \\Theta \\over g}$$", ""},
                    {"Friction", "Friction", "$$F_f = \\mu \\cdot F_n$$", ""},
                    {"Momentum", "Momentum and Impulse", "$$p = m \\cdot v$$", ""},
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
                    {"Moment of Inertia", "Rotational Motion", "$$I = m \\cdot r^2$$", ""},
                    {"Torque","Rotational Motion", "$$\\tau = r \\cdot F$$", ""},
                    {"Angular Momentum", "Rotational Motion", "$$L = I \\cdot \\omega$$", ""}
            };

            for(String[] s: formulas){
                Cursor c = database.query(
                        DataContract.LessonEntry.TABLE_NAME,
                        new String[]{DataContract.LessonEntry._ID},
                        DataContract.LessonEntry.COLUMN_TITLE + " = ?",
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
                    {"Acceleration due to Gravity", "Free-fall Velocity"}
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
                    {"Displacement", "Displacement", "$$d = {x_f - x_i}$$", "xf - xi", "d", "{{m}}"},
                    {"Displacement", "Initial Velocity","$$x_i= {x_f - d}$$", "xf - d", "xi", "{{m}}"},
                    {"Displacement", "Final Velocity", "$$x_f= {x_i + d}$$", "xi + d", "xf", "{{m}}"},

                    {"Speed", "Speed", "$$s = {d \\over t}$$", "d / t", "s", "{{m} \\over {s}}"},
                    {"Speed", "Distance", "$$d = {s \\cdot t}$$", "s * t", "d", "{{m}}"},
                    {"Speed", "Time", "$$t = {d \\over s}$$", "d / s", "t", "{{s}}"},

                    {"Velocity", "Velocity", "$$v = {x_f - x_i \\over t}$$", "(xf - xi) / t", "a", "{{m} \\over {s}}"},
                    {"Velocity", "Initial Position", "$$x_i = x_f - vt$$", "xf - (a * t)", "xi", "{{m}}"},
                    {"Velocity", "Final Position", "$$x_f = vt + x_i$$", "(a * t) + xi", "xf", "{{m}}"},
                    {"Velocity", "Time", "$$t = {x_f - x_i \\over v}$$", "(xf - xi) / a", "t", "{{s}}"},

                    {"Average Velocity", "Average Velocity", "$$v_{av} = {x_f - x_i \\over t_f - t_i}$$", "(xf - xi) / (tf - ti)", "v", "{{m} \\over {s}}"},
                    {"Average Velocity", "Initial Position", "$$x_i = x_f - v_{av}(t_f - t_i)$$", "xf - (v * (tf - ti))", "xi", "{{m}}"},
                    {"Average Velocity", "Final Position",  "$$x_f = v_{av}(t_f - t_i) + x_i$$", "(v * (tf - ti)) + xi", "xf", "{{m}}"},
                    {"Average Velocity", "Initial Time","$$t_i = t_f - (x_f - x_i \\over v_{av})$$", "tf - ((xf - xi) / v)", "ti", "{{s}}"},
                    {"Average Velocity", "Final Time", "$$t_f = (x_f - x_i \\over v_{av}) + t_i$$", "((xf - xi) / v) + ti", "tf", "{{s}}"},

                    {"Acceleration", "Acceleration", "$$a = {v_f - v_i \\over t}$$", "(vf - vi) / t", "a", "{{m} \\over {s^2}}"},
                    {"Acceleration", "Initial Velocity", "$$v_i = v_f - at$$", "vf - (a * t)", "vi", "{{m} \\over {s}}"},
                    {"Acceleration", "Final Velocity", "$$v_f = at + v_i$$", "(a * t) + vi", "vf", "{{m} \\over {s}}"},
                    {"Acceleration", "Time Interval", "$$t = {v_f - v_i \\over a}$$", "(vf - vi) / a", "t", "{{s}}"},

                    {"Average Acceleration", "Average Acceleration", "$$a_{av} = {v_f - v_i \\over t_f - t_i}$$", "(vf - vi) / (tf - ti)", "a", "{{m \\over s^2}}"},
                    {"Average Acceleration", "Initial Velocity", "$$v_i = v_f - a_{av}(t_f - t_i)$$", "vf - (a * (tf - ti))", "vi", "{{m \\over s}}"},
                    {"Average Acceleration", "Final Velocity", "$$v_f = a_{av}(t_f - t_i) + v_i$$", "(a * (tf - ti)) + vi", "vf", "{{m \\over s}}"},
                    {"Average Acceleration", "Initial Time", "$$t_i = t_f - (v_f - v_i \\over a_{av})$$", "tf - ((vf - vi) / a)", "ti", "{{s}}"},
                    {"Average Acceleration", "Final Time", "$$$$t_f = (v_f - v_i \\over a_{av}) + t_i$$", "((vf - vi) / a) + ti", "tf", "{{s}}"},

                    {"Free-fall Velocity", "Velocity", "$$v_y = u + g \\cdot t$$", "u + (g * t)", "v", "{m \\over s}"},
                    {"Free-fall Velocity", "Initial Velocity", "$$u = v - g \\cdot t$$", "v - (g / t)", "u", "{m \\over s}"},
                    {"Free-fall Velocity", "Acceleration due to Gravity", "$$g = {v - u \\over t}$$", "v - (u / t)", "g", "{m \\over s^2}", "Acceleration due to Gravity"},
                    {"Free-fall Velocity", "Time", "$$t = {v - u \\over g}$$", "v - (u / g)", "t", "{s}"},

                    {"Free-fall Displacement", "Displacement", "$$y = {1 \\over 2}g  \\cdot {t^2}$$", "(g / 2) * t ^ 2", "y", "{m}"},
                    {"Free-fall Displacement", "Acceleration due to Gravity", "$$g = 2{y \\over t^2}$$", "2 * (y / t^2)", "g", "{m \\over s^2}"},
                    {"Free-fall Displacement", "Time", "$$t = \\sqrt{2{y \\over g}}$$", "sqrt(2 * (y / g))", "t", "{s}"},

                    {"Horizontal Distance", "Horizontal Distance","$$x = v_{x}t$$", "v * t", "x", "{m}"},
                    {"Horizontal Distance", "Velocity along the x-axis","$$v_x = {x \\over t}$$", "x / t", "v", "{m \\over s}"},
                    {"Horizontal Distance", "Time","$$t = { x \\over v_x}$$", "x / v", "t", "{s}"},

                    {"Horizontal Velocity", "Velocity along the x-axis", "$$v_x = v_{xi}$$", "vi", "v", "{m \\over s}"},
                    {"Horizontal Velocity", "Initial Velocity along the x-axis", "$$$$", "v", "vi", "{m \\over s}"},

                    {"Vertical Distance", "Vertical Distance", "$$y = v_{yi}t - {1 \\over 2}g \\cdot t^2$$", "", "y", "{m}"},
                    {"Vertical Distance", "Initial Velocity along the y-axis", "$$TEXT NA NAKA CAPS$$", "", "vi", "{m \\over s}"},
                    {"Vertical Distance", "Time","$$PARA MAKITA NATIN$$", "", "t", "{s}"},
                    {"Vertical Distance", "Acceleration due to Gravity","$$IF EVER$$", "", "g", "{m \\over s^2}"},

                    {"Vertical Velocity", "Vertical Velocity","$$v_y = v_{yi} - g \\cdot t$$", "vi - (g * t)", "v", "{m \\over s}"},
                    {"Vertical Velocity", "Initial Velocity along the y-axis", "$$v_{yi} = v_y + g \\cdot t$$", "v + (g * t)", "vi", "{m \\over s}"},
                    {"Vertical Velocity", "Time","$$g = {v_{yi} - v_y \\over t}$$", "(vi - v) / g", "t", "{s}"},
                    {"Vertical Velocity", "Acceleration due to Gravity","$$t = {v_{yi} - v_y \\over g}$$", "(vi - v) / t", "g", "{m \\over s^2}"},

                    {"Time of Flight", "Time of Flight","$$t = {2v_{1}sin \\Theta \\over g}$$", "((2 * v) * sin(a)) / g", "t", "{s}"},
                    {"Time of Flight", "Initial Velocity","$$g = {2v_{1}sin \\Theta \\over t}$$", "((2 * v) * sin(a)) / t", "v", "{m \\over s}"},
                    {"Time of Flight", "Angle of Trajectory","\\Theta = sin^{-1} {gt \\ 2v}", "asin((g * t) / (2 * v))", "a", "{\\Theta }"},
                    {"Time of Flight", "Acceleration due to Gravity","$$$$v_{yi} = {gt \\over 2sin \\Theta}$$$$", "(t * g) / (2 * sin(a))", "g", "{m \\over s^2}"},

                    {"Maximum Height Reached", "Maximum Height Reached","$$H = {v_{1}^{2}sin^{2} \\Theta \\over 2g}$$", "(v^2 * sin(a) * sin(a)) / 2 * g", "H", "{m}"},
                    {"Maximum Height Reached", "Initial Velocity",  "$$v_{1} = \\sqrt{2gH \\over sin^2 \\Theta}$$", "sqrt((2 * g *H) / (sin(a) * sin(a)))", "v", "{m \\over s}"},
                    {"Maximum Height Reached", "Angle of Trajectory", "$$\\Theta = sin^{2-1}{2gH \\over v_{1}^{2}}$$", "asin(sqrt((2 * g * H) / (v^2)))", "a", "{\\Theta }"},
                    {"Maximum Height Reached", "Acceleration due to Gravity", "$$g = {v_{1}^{2}sin^{2} \\Theta \\over 2H}$$", "(v^2 * sin(a) * sin(a)) / 2 * H", "g", "{m \\over s^2}"},

                    {"Horizontal Range", "Horizontal Range","$$R = {v_{1}^{2}sin2 \\Theta \\over g}$$", "(v^2 * sin(2 * a)) / g", "R", "{m}"},
                    {"Horizontal Range", "Initial Velocity","$$v_{1} = \\sqrt{gR \\over sin2 \\Theta}$$", "sqrt((g * R) / (sin(2 * a)))", "v", "{m \\over s}"},
                    {"Horizontal Range", "Time of Flight", "$$\\Theta = sin^{-1}{gR \\over 2v_{1}^{2}}$$", "asin((g * R) / (2 * v))", "a", "{\\Theta }"},
                    {"Horizontal Range", "Acceleration due to Gravity", "$$g = {v_{1}^{2}sin2 \\Theta \\over R$$", "(v^2 * sin(2 * a)) / R", "g", "{m \\over s^2}"},

                    {"Friction", "Frictional Force", "$$F_f = \\mu \\cdot F_n$$", "m * N", "F", "{N}"},
                    {"Friction", "Coefficient of Friction", "$$\\mu = {F_f \\over F_n}$$", "F / N", "m", "{}"},
                    {"Friction", "Normal Force", "$$F_n = {F_f \\over \\mu}$$", "f / m", "N", "{N}"},

                    {"Momentum", "Momentum","$$p = m \\cdot v$$", "m * v", "p", "{kg \\cdot m \\over s}"},
                    {"Momentum", "Mass", "$$m = {p \\over v}$$", "p / v", "m", "{kg}"},
                    {"Momentum", "Velocity", "$$v = {p \\over m}$$", "p / m", "v", "{m \\over s}"},

                    {"Work", "Work", "$$W = \\vec{F} \\cdot \\vec{x}$$", "F * x", "W", "{J}"},
                    {"Work", "Force", "$$\\vec{F} = {W \\over \\vec{x}}$$", "W / x", "F", "{N}"},
                    {"Work", "Distance", "$$\\vec{x} = {W \\over \\vec{F}}$$", "W / F", "x", "{m}"},

                    {"Kinetic Energy", "Kinetic Energy", "$$KE = {mv^2 \\over 2}$$", "(m * v^2) / 2", "KE", "{J}"},
                    {"Kinetic Energy", "Mass", "$$m = {2KE \\over m}$$", "(2 * KE) / v^2", "m", "{kg}"},
                    {"Kinetic Energy", "Velocity", "$$v = \\sqrt{2KE \\ over m}$$", "sqrt((2 * KE) / m)", "v", "{m \\over s}"},

                    {"Gravitational Potential Energy", "Potential Energy",  "$$PE_g = m \\cdot g \\cdot h$$", "m * (a * h)", "PE", "{J}"},
                    {"Gravitational Potential Energy", "Mass", "$$m = {PE_g \\over g \\cdot h}$$", "PE / (a * h)", "m", "{kg}"}, //add to lesson *missing from lesson
                    {"Gravitational Potential Energy", "Height", "$$h = {PE_g \\over m \\cdot g}$$", "PE / (m * a)", "h", "{m}"},
                    {"Gravitational Potential Energy", "Acceleration due to Gravity", "$$g = {PE_g \\over m \\cdot h}$$", "PE / (m * h)", "a", "{{m} \\over {s^2}}", "Acceleration due to Gravity"},




                    {"Spring Potential Energy", "Potential Energy", "$$PE_{sp} = {1 \\over 2}k \\cdot \\Delta x^2$$", "(k / 2) * x^2", "PE", "{J}"},
                    {"Spring Potential Energy", "Spring Constant", "$$k = {2PE \\over \\Delta x^2}$$", "(2 * PE) / x^2", "k", "{N}"},
                    {"Spring Potential Energy", "Spring Displacement", "$$\\Delta x = \\sqrt{2PE \\over k}$$", "sqrt((2 * PE) / k)", "x", "{m}"},

                    {"Total Mechanical Energy", "Total Mechanical Energy", "$$TME = KE + PE$$", "KE + PE", "TME", "{J}"},
                    {"Total Mechanical Energy", "Kinetic Energy", "$$KE = TME - PE$$", "TME - PE", "KE", "{J}"},
                    {"Total Mechanical Energy", "Potential Energy", "$$PE = TME - KE$$", "TME - KE", "PE", "{J}"},

                    {"Average Power", "Average Power", "$$P_{av} = {\\Delta W \\over \\Delta t}$$", "W / t", "P", "{W}"},
                    {"Average Power", "Amount of Work done", "$$\\Delta W = P_{av} \\cdot \\Delta t$$", "P * t", "W", "{J}"},
                    {"Average Power", "Time", "$$\\Delta t = {\\Delta W \\over P_{av}}$$", "W / P", "t", "{s}"},

                    {"Instantaneous Power", "Instantaneous Power",  "$$P = F \\cdot cosa \\cdot v$$", "F * cos(a) * v", "P", "{W}"},
                    {"Instantaneous Power", "Force",  "$$F = {P \\over cosa \\cdot v}$$", "P / (cos(a) * v)", "P", "{N}"},
                    {"Instantaneous Power", "Angle", "$$a = cos^{-1}{p \\over F \\cdot v}$$", "a = acos(P / (F * v))", "a", "{\\Theta }"},
                    {"Instantaneous Power", "Velocity", "$$v = {P \\over F \\cdot cosa}$$", "P / (F * cos(a))", "v", "{m \\over s}"},

                    {"Length of Arc", "Length of Arc", "$$\\Delta S = r \\cdot \\Delta \\Theta$$", "r * a", "s", "{m}"},
                    {"Length of Arc", "Radius", "$$r = (\\Delta S \\over \\Delta \\Theta)$$", "s / a", "r", "{m}"},
                    {"Length of Arc", "Angle",  "$$\\Delta \\Theta = (\\Delta S \\over r)$$", "s / r", "a", "{\\Theta }"},

                    {"Tangential Velocity", "Tangential Velocity", "$$v = {\\Delta S \\over \\Delta t}$$", "s / t", "v", "{m \\over s}"},
                    {"Tangential Velocity", "Length of Arc","$$\\Delta S = v \\cdot \\Delta t$$", "v * t", "s", "{\\Theta }"},
                    {"Tangential Velocity", "Time",  "$$\\Delta t = {\\Delta S \\over v}$$", "s / v", "t", "{s}"},

                    {"Velocity around a Circle", "Velocity around a Circle", "$$v = 2 \\cdot \\pi \\cdot {r \\over t}$$", "", "v", "{m \\over s}"},
                    {"Velocity around a Circle", "Radius", "$$r = {v \\cdot t \\over 2 \\pi}$$", "", "r", "{m}"},
                    {"Velocity around a Circle", "Time", "$$t = 2 \\cdot \\pi \\cdot {r \\over v}$$", "", "t", "{s}"},

                    {"Angular Velocity", "Angular Velocity", "$$\\omega = {\\Delta \\Theta \\over \\Delta t}$$", "a / s", "v", "{\\omega }"},
                    {"Angular Velocity", "Angel", "$$\\Delta \\Theta = {\\omega \\cdot \\Delta t}$$", "a * t", "a", "{\\Theta }"},
                    {"Angular Velocity", "Time", "$$\\Delta t = {\\Delta \\Theta \\over \\omega}$$", "a / v", "t", "{s}"},

                    {"Centripetal Acceleration", "Centripetal Acceleration", "$$a_c = r \\cdot \\omega ^2$$", "r * w^2", "a", "{m \\over s^2}"},
                    {"Centripetal Acceleration", "Angular Velocity", "$$\\omega = \\sqrt{a_c \\over r}$$", "sqrt(a / r)", "w", "{\\omega}"},
                    {"Centripetal Acceleration", "Angle", "$$r = {a_c \\over \\omega ^2}$$", "a / w^2", "r", "{\\Theta }"},

                    {"Centripetal Force", "Centripetal Force", "$$F = {m \\cdot v^2 \\over r}$$", "(m * v^2) / r", "F", "{N}"},
                    {"Centripetal Force", "Mass", "$$m = {F \\cdot r \\over v^2}$$", "(F * r) / v^2", "m", "{kg}"},
                    {"Centripetal Force", "Tangential Velocity", "$$v = \\sqrt{F \\cdot r \\over m}$$", "sqrt((F * r) / m)", "v", "{m \\over s}"},
                    {"Centripetal Force", "Radius","$$r = {m \\cdot v^2 \\over F}$$", "((m * v)^2) / F", "r", "{m }"},

                    {"Moment of Inertia", "Moment of Inertia", "$$I = m \\cdot r^2$$", "m * r^2", "I", "{kg \\cdot m^2}"},
                    {"Moment of Inertia", "Mass", "$$m = {I \\over r^2}$$", "I / r^2", "m", "{kg}"},
                    {"Moment of Inertia", "Distance","$$r = \\sqrt{I \\over m}$$", "sqrt(I / m)", "r", "{m}"},

                    {"Torque", "Torque", "$$\\tau = r \\cdot F$$", "r * F", "T", "{N \\cdot m}"},
                    {"Torque", "Force", "$$F = {\\tau \\over r}$$", "T / r", "F", "{N}"},
                    {"Torque", "Position",  "$$r = {\\tau \\over F}$$", "T / F", "r", "{m}"},

                    {"Angular Momentum", "Angular Momentum", "$$L = I \\cdot \\omega$$", "I * w", "L", "{kg-m^2 \\over s}"},
                    {"Angular Momentum", "Moment of Inertia", "$$I = {L \\over \\omega}$$", "L / w", "I", "{kg \\cdot m^2}"},
                    {"Angular Momentum", "Angular Velocity", "$$\\omega = {L \\over I}$$", "L / I", "w", "{\\omega}"}
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
                values.put(DataContract.VariableEntry.COLUMN_SYMBOL, s[4]);
                values.put(DataContract.VariableEntry.COLUMN_UNIT, s[5]);
                try{
                    c = database.query(
                            DataContract.ConstantEntry.TABLE_NAME,
                            new String[]{DataContract.ConstantEntry._ID},
                            DataContract.ConstantEntry.COLUMN_NAME + " = ?",
                            new String[]{s[6]},
                            null,
                            null,
                            null
                    );
                    c.moveToFirst();
                    values.put(DataContract.VariableEntry.COLUMN_CONSTANT_KEY,
                            c.getLong(c.getColumnIndex(DataContract.ConstantEntry._ID)));
                } catch (Exception ex){
                    values.put(DataContract.VariableEntry.COLUMN_CONSTANT_KEY, -1);
                }
                c.close();
                database.insert(DataContract.VariableEntry.TABLE_NAME, null, values);
            }
        }
    }
}







