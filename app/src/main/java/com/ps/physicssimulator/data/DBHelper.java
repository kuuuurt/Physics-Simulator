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
                            "Definition, Distance and Displacement",
                            "<p><h2><b>Definition</b></h2>\n" +
                                    "Scalar quantity:\n" +
                                    "•\thas a magnitude\n" +
                                    "•\tis one dimensional\n" +
                                    "Vector quantity:\n" +
                                    "•\thas a magnitude and a direction\n" +
                                    "•\tis two dimensional\n" +
                                    "<h2><b>Distance and Displacement</b></h2>\n" +
                                    "Distance:\n" +
                                    "•\tis a scalar quantity\n" +
                                    "•\tmeasures the interval between two points that is measured along the actual path that was made that connects them\n" +
                                    "Displacement:\n" +
                                    "•\tis a vector quantity\n" +
                                    "•\tmeasures the interval between two points along the shortest path that connects them \n" +
                                    "//<Insert diagram>\n" +
                                    "The SI unit used for distance and displacement is meter (m).\n" +
                                    "Displacement can be calculated using this formula:\n" +
                                    "//<Insert formula>\n" +
                                    "Where:\n" +
                                    "<b>x1</b> = initial position of the object\n" +
                                    "<b>x2</b> = final position of the object\n</p>",
                            "",
                            "com.ps.physicssimulator.lessons.ValuesFragment",
                            "com.ps.physicssimulator.calculator.ValuesFragment"},
                    {"Velocity", "One-dimensional Motion",
                            "Definition, Speed and Velocity, Average Velocity, Instantaneous " +
                                    "Velocity",
                            "<p><h2><b>Definition</b></h2>\n" +
                                    "Speed:\n" +
                                    "•\tshows the rate at which an object is able to move\n" +
                                    "Velocity:\n" +
                                    "•\tshows the rate at which an object is able to move in a given direction\n" +
                                    "The SI unit used for speed and velocity is meter per second (m/s).\n" +
                                    "<h2><b>Speed and Velocity</b></h2>\n" +
                                    "Speed can be calculated by dividing distance over time:\n" +
                                    "//<Insert formula>\n" +
                                    "Velocity can be calculated by dividing displacement over time:\n" +
                                    "//<Insert formula>\n" +
                                    "<h2><b>Average Velocity</b></h2>\n" +
                                    "Average velocity is the ratio of total displacement (Δx) taken over time interval (Δt)\n" +
                                    "//<Insert diagram>\n" +
                                    "The average velocity from when the object starts to move up to the time when the object stops can be described as:\n" +
                                    "//<Insert formula>\n" +
                                    "Where:\n" +
                                    "<b>Δx</b> = change in velocity\n" +
                                    "<b>Δt</b> = change in time\n" +
                                    "<b>x1</b> = initial position of the object\n" +
                                    "<b>x2</b> = final position of the object\n" +
                                    "<b>t1</b> = time when the object was at position x1\n" +
                                    "<b>t2</b> = time when the object was at position x2\n" +
                                    "<h2><b>Instantaneous Velocity</b></h2>\n" +
                                    "Instantaneous velocity is the measure of the velocity of an object at a particular moment.\n</p>",
                            "",
                            "com.ps.physicssimulator.lessons.VelocityFragment",
                            "com.ps.physicssimulator.calculator.VelocityFragment"},
                    {"Acceleration", "One-dimensional Motion", "Definition, Acceleration, Average " +
                            "Acceleration, Instantaneous Acceleration",
                            "<p><h2><b>Definition</b></h2>\n" +
                                    "Acceleration:\n" +
                                    "•\tis the rate of change of velocity with respect to time\n" +
                                    "•\tcan be positive (speeding up) or negative (slowing down)\n" +
                                    "The SI unit used for acceleration is meter per second squared (m/s²).\n" +
                                    "<h2><b>Acceleration</b></h2>\n" +
                                    "Acceleration is the ratio of total velocity change (Δv) taken over time interval (Δt).\n" +
                                    "//<Insert formula>\n" +
                                    "<h2><b>Average Acceleration</b></h2>\n" +
                                    "Average acceleration can be calculated using this formula:\n" +
                                    "//<Insert formula>\n" +
                                    "Where:\n" +
                                    "<b>v1</b> = initial velocity of the object\n" +
                                    "<b>v2</b> = final velocity of the object\n" +
                                    "<b>t1</b> = time when the object had velocity x1\n" +
                                    "<b>t2</b> = time when the object had velocity x2\n" +
                                    "<h2><b>Instantaneous Acceleration</b></h2>\n" +
                                    "Instantaneous acceleration is the change of velocity at infinitesimal (very small) time interval.\n</p>",
                            "", "", ""},
                    {"Free-fall", "One-dimensional Motion", "Definition, Free fall",
                            "<p><h2><b>Definition</b></h2>\n" +
                                    "Free fall:\n" +
                                    "•\trefers to the motion of an object where its motion is affected only by gravity\n" +
                                    "•\tacts only along the y-axis\n" +
                                    "Acceleration due to gravity:\t\n" +
                                    "•\tis equal to 9.8 m/s²\n" +
                                    "•\tis constant regardless of the object’s mass\n" +
                                    "<h2><b>Free fall</b></h2>\n" +
                                    "//<Insert diagram>\n" +
                                    "An object is thrown with an initial velocity u along the y-axis. The position and speed of an object in free fall motion can be calculated from the motion equations.\n" +
                                    "Velocity along the y-axis at any instant t:\n" +
                                    "//<Insert formula>\n" +
                                    "The displacement along y-axis at any instant t:\n" +
                                    "//<Insert formula>\n</p>",
                            "", "", ""},
                    {"Projectile Motion", "Two-dimensional Motion", "Definition, " +
                            "Projectile Motion",
                            "<p><h2><b>Definition</b></h2>\n" +
                                    "Projectile:\n" +
                                    "•\tis an object thrown with an initial velocity in a vertical plane\n" +
                                    "•\tmoves in two dimensions\n" +
                                    "•\tacts under the action of gravity alone without being propelled\n" +
                                    "Projectile Motion:\n" +
                                    "•\tis the motion done by the projectile\n" +
                                    "Trajectory:\n" +
                                    "•\tIs the path passed by the projectile\n" +
                                    "<h2><b>Projectile Motion</b></h2>\n" +
                                    "Projectile motion is a two dimensional motion. Any two dimensional motion case can be split up into two cases of one dimensional motion.\n" +
                                    "An important reminder is that the motion along the x-axis does not affect the motion along the y-axis. It also applies in vice versa. Each motion along each axis is independent of each other.\n" +
                                    "//<Insert diagram>\n" +
                                    "Projectile motion formula is given by the following:\n" +
                                    "Horizontal distance (m):\n" +
                                    "//<Insert formula>\n" +
                                    "Horizontal velocity (m/s):\n" +
                                    "//<Insert formula>\n" +
                                    "Vertical distance (m):\n" +
                                    "//<Insert formula>\n" +
                                    "Vertical velocity (m/s):\n" +
                                    "//<Insert formula>\n" +
                                    "Where:\n" +
                                    "<b>vx</b> = velocity of the object along x-axis\n" +
                                    "<b>vx1</b> = initial velocity of the object along x-axis\n" +
                                    "<b>vy</b> = velocity of the object along y-axis\n" +
                                    "<b>vy1</b> = initial velocity of the object along y-axis\n" +
                                    "<b>g</b> = acceleration due to gravity \n" +
                                    "<b>t</b> = time duration\n" +
                                    "Formulas related to trajectory motion is given by the following:\n" +
                                    "Time of flight (s):\n" +
                                    "//<Insert formula>\n" +
                                    "Maximum height reached (m):\n" +
                                    "//<Insert formula>\n" +
                                    "Horizontal range (m):\n" +
                                    "//<Insert formula>\n" +
                                    "Where:\n" +
                                    "<b>v1</b> = initial velocity of the object\n" +
                                    "<b>sin θ</b> = component along y-axis\n" +
                                    "<b>cos θ</b> = component along x-axis\n</p>",
                            "", "", ""},
                    {"Friction", "Isaac Newton's Laws of Motion", "Definition, Two types of " +
                            "Friction",
                            "<p><h2><b>Definition</b></h2>\n" +
                                    "Friction Force:\n" +
                                    "•\tis the force that is exerted by a surface as an object moves across it\n" +
                                    "//<Insert Diagram>\n" +
                                    "When an object is being pushed across a surface, the surface offers resistance to the movement of the object.\n" +
                                    "The friction force is opposite to the direction of the motion of the object.\n" +
                                    "<h2><b>Two types of Friction</b></h2>\n" +
                                    "1. Static Friction:\n" +
                                    "•\tis the friction between multiple solid objects that are not moving relative to each other\n" +
                                    "2. Kinetic Friction:\n" +
                                    "•\tis the friction between multiple solid objects that are moving relative to each other\n" +
                                    "The friction force is dependent on two factors:\n" +
                                    "•\tthe material of the objects that are in contact\n" +
                                    "•\tthe force that pushes both surfaces together\n" +
                                    "The following is the equation that summarizes the topic:\n" +
                                    "//<Insert formula>\n" +
                                    "Where:\n" +
                                    "<b>Ff</b> = frictional force \n" +
                                    "<b>μ</b> = coefficient of friction\n" +
                                    "<b>Fn</b> = normal force\n</p>",
                            "", "", ""},
                    {"Free-body Diagrams", "Isaac Newton's Laws of Motion", "Definition, Free " +
                            "Body Diagrams",
                            "<p><h2><b>Definition</b></h2>\n" +
                                    "Free Body Diagrams:\n" +
                                    "•\talso known as “Force Diagram”\n" +
                                    "•\ta graphical illustration used to visualize the applied forces, movements and resulting reaction on a body in a steady state condition\n" +
                                    "•\tshows all the forces acting on an object or a “body” that is singled out from or “freed” from a group of objects\n" +
                                    "<h2><b>Free Body Diagrams</b></h2>\n" +
                                    "There are four types of forces typically used in free body diagrams. These are the forces used.\n" +
                                    "1. Gravity (G) - its force is directed towards the ground (downwards).\n" +
                                    "2. Normal (N) - its force is directed perpendicular to the object’s surface.\n" +
                                    "3. Tension (T) - its force is directed along a string, rope, chain or anything an object is attached to.\n" +
                                    "4. Friction (fr) - its force’s direction opposes the relative motion of the object.\n" +
                                    "\n" +
                                    "Drawing Free Body Diagrams\n" +
                                    "\n" +
                                    "There are a few rules we should follow when drawing a free body diagram.\n" +
                                    "1.\tAlways draw the forces from the center of the object.\n" +
                                    "2.\tThe stronger the force is, the longer the arrow is.\n" +
                                    "3.\tThe arrow should point in the direction for force is acting.\n" +
                                    "4.\tLabel the forces acting with letters/symbols.\n" +
                                    "\n" +
                                    "//<Insert diagram>\n" +
                                    "\n" +
                                    "Here are a few hints when working with free body diagrams: \n" +
                                    "•\tThe force of gravity always points straight down. \n" +
                                    "•\tThe normal force will always push straight up from a surface.\n" +
                                    "•\tIf an object is moving in one direction, friction is acting in the opposite direction.\n" +
                                    "•\tThink about whether the forces on opposite sides are balanced or not. If they are, the two arrows should be about the same length. \n</p>",
                            "", "", ""},
                    {"Momentum and Impulse", "Momentum and Impulse", "Definition, Momentum, " +
                            "Impulse",
                            "<p><h2><b>Definition</b></h2>\n" +
                                    "Momentum:\n" +
                                    "•\tis defined as “mass in motion”\n" +
                                    "•\tdepends upon the variables mass and velocity\n" +
                                    "•\tis a vector quantity\n" +
                                    "<h2><b>Momentum</b></h2>\n" +
                                    "In terms of an equation, the momentum of an object is equal to the mass of the object times the velocity of the object. \n" +
                                    "<b><i>Momentum = mass * velocity</i></b>\n" +
                                    "In physics, the symbol for the quantity momentum is the lower case p. Thus, the above equation can be rewritten as:\n" +
                                    "//<Insert formula>\n" +
                                    "where:\n" +
                                    "<b>m</b> = mass of the object\n" +
                                    "<b>v</b> = velocity of the object\n" +
                                    "The equation illustrates that momentum is:\n" +
                                    "•\tdirectly proportional to an object's mass \n" +
                                    "•\tdirectly proportional to the object's velocity\n" +
                                    "Momentum is also a vector quantity. The momentum of an object then is fully described by both magnitude and direction.\n" +
                                    "<h2><b>Impulse</b></h2>\n" +
                                    "Impulse is known as quantity force multiplied by time. \n" +
                                    "And since the quantity m•Δv is the change in momentum. The equation really says that the Impulse is equal to Change in Momentum.\n" +
                                    "The physics of collisions are governed by the laws of momentum. The first law is the equation known as the impulse-momentum change equation. The law can be expressed this way:\n" +
                                    "//<Insert formula>\n" +
                                    "In a collision:\n" +
                                    "•\tobjects experience an impulse\n" +
                                    "•\tthe impulse causes and is equal to the change in momentum\n</p>",
                            "", "", ""},
                    {"Law of Conservation of Energy", "Momentum and Impulse", "Definition, " +
                            "Conservation of Energy",
                            "<p><h2><b>Definition</b></h2>\n" +
                                    "Law of Conservation of Energy\n" +
                                    "•\tstates that the total energy of an isolated system remains constant\n" +
                                    "•\tIt implies that energy can neither be created nor destroyed, but can be change from one form to another\n" +
                                    "•\tthe change in energy of an object due to a transformation is equal to the work done on the object or by the object for that transformation\n" +
                                    "<h2><b>Conservation of Energy</b></h2>\n" +
                                    "The different types of energy are the potential energy, kinetic energy, and the total mechanical energy. These types of energy will further be discussed in a different part (Energy) of this application.\n" +
                                    "The basic formula for the conservation of energy is:\n" +
                                    "//<Insert formula>\n" +
                                    "The formula can be more explicitly written as Conservation of Energy Equation depending on the context. \n" +
                                    "•\tAn object when dropped from a height transforms its potential energy into kinetic energy.\n" +
                                    "Mathematically it can be expressed as a Conservation of Energy Equation as follows:\n" +
                                    "//<Insert formula>\n" +
                                    "Where:\n" +
                                    "<b>m</b> = mass of the object\n" +
                                    "<b>v</b> = final velocity after falling from a height of <b>h</b>\n" +
                                    "<b>g</b> = acceleration due to gravity\n" +
                                    "\n</p>",
                            "", "", ""},
                    {"Work", "Work, Energy, and Power", "Definition, Work",
                            "<p><h2><b>Definition</b></h2>\n" +
                                    "Work:\n" +
                                    "•\tis the measure of energy being transferred occurring when an object is moved over a distance\n" +
                                    "//<Insert diagram>\n" +
                                    "The SI unit used for work is Joule (J). Joule’s base units is kg • m2/s2.\n" +
                                    "<h2><b>Work</b></h2>\n" +
                                    "A force is doing work when it acts on an object which displaces it from the point of application.\n" +
                                    "Constant work can be calculated using this formula:\n" +
                                    "//<Insert formula>\n" +
                                    "Where:\n" +
                                    "<b>→F</b> = force vector\n" +
                                    "<b>→x</b> = position vector\n</p>",
                            "", "", ""},
                    {"Energy", "Work, Energy, and Power", "Definition, Kinetic Energy, " +
                            "Potential Energy, Total Mechanical Energy",
                            "<p></p>",
                            "", "<h2><b>Definition</b></h2>\n" +
                            "Energy:\n" +
                            "•\tis the capacity of performing work\n" +
                            "•\tmay exist in various forms (potential, kinetic, electric, chemical etc.)\n" +
                            "The SI unit for energy is the same with work, Joule (J).\n" +
                            "<h2><b>Kinetic Energy</b></h2>\n" +
                            "Kinetic energy is an energy that is possessed by means of its motion.\n" +
                            "Kinetic energy can be calculated using this formula:\n" +
                            "//<Insert formula>\n" +
                            "Where:\n" +
                            "<b>m</b> = mass of the object\n" +
                            "<b>v</b> = velocity of the object\n" +
                            "<h2><b>Potential Energy</b></h2>\n" +
                            "Potential energy is an energy that is possessed by means of its position relative to others.\n" +
                            "Gravitational potential energy can be calculated using this formula:\n" +
                            "//<Insert formula>\n" +
                            "Where:\n" +
                            "<b>h</b> = height of the object above the ground\n" +
                            "<b>g</b> = acceleration due to gravity\n" +
                            "Spring potential energy can be calculated using this formula:\n" +
                            "//<Insert formula>\n" +
                            "Where:\n" +
                            "<b>k</b> = spring constant measured in Newton per meter\n" +
                            "<b>x</b> = amount spring is displaced from initial point\n" +
                            "<h2><b>Total Mechanical Energy</b></h2>\n" +
                            "Total mechanical energy is the sum of the kinetic and potential energy of a conservative system:\n" +
                            "//<Insert formula>\n", ""},
                    {"Power", "Work, Energy, and Power", "Definition, Average Power, " +
                            "Instantaneous Power",
                            "<p><h2><b>Definition</b></h2>\n" +
                                    "Power:\n" +
                                    "•\tis the rate of doing work\n" +
                                    "•\thas no direction\n" +
                                    "•\tis a scalar quantity\n" +
                                    "The SI unit for power is Watt (W), or Joule per second (J/s).\n" +
                                    "<h2><b>Average Power</b></h2>\n" +
                                    "Average power, or simple “power”, is the average amount of work done converted per unit of time.\n" +
                                    "Average power can be calculated by using this formula:\n" +
                                    "//<Insert formula>\n" +
                                    "Where:\n" +
                                    "<b>ΔW</b> = amount of work performed\n" +
                                    "<b>Δt</b> = time duration\n" +
                                    "<h2><b>Instantaneous Power</b></h2>\n" +
                                    "Instantaneous power is the limiting value of the average power as the time interval approaches to zero.\n" +
                                    "Average power can be calculated by using this formula:\n" +
                                    "//<Insert formula>\n" +
                                    "Where:\n" +
                                    "<b>W</b> = work done\n" +
                                    "<b>t</b> = time duration\n" +
                                    "<b>F</b> = force applied on the object\n" +
                                    "<b>x</b> = path made by the object\n" +
                                    "<b>a</b> = angle between the force and the position vectors\n" +
                                    "<b>v</b> = velocity of the object\n</p>",
                            "", "", ""},
                    {"Uniform Circular Motion", "Uniform Circular Motion", "Definition, " +
                            "Measurements of a Circle, Frequency, Angular Displacement, " +
                            "Length of Arc, Tangential Velocity, Angular Velocity, " +
                            "Centripetal Acceleration",
                            "<p><h2><b>Definition</b></h2>\n" +
                                    "Uniform Circular Motion:\n" +
                                    "•\tis the motion of an object traveling at a constant speed on a path that is circular\n" +
                                    "<h2><b>Measurements of a Circle</b></h2>\n" +
                                    "The arc of a circle is a portion of the circumference.\n" +
                                    "The length of an arc is the length of its portion of the circumference.\n" +
                                    "The radian is the standard unit of angular measure. When it is drawn as a central angle, it subtends an arc whose length is equal to the length of the radius of the circle.\n" +
                                    "//<Insert diagram>\n" +
                                    "The relationship between the degrees and radians is:\n" +
                                    "//<Insert formula>\n" +
                                    "<h2><b>Frequency</b></h2>\n" +
                                    "The frequency (f) is the number of revolutions completed per time unit.\n" +
                                    "The SI unit used for frequency is hertz (Hz). Hertz’ base units is 1/s.\n" +
                                    "Another unit of measure for frequency is revolutions per minute (RPM). 60 RPM is equal to 1 hertz.\n" +
                                    "Period (T):\n" +
                                    "•\tis the time an object takes to travel one revolution around the circle.\n" +
                                    "The SI unit used for period is second (s). This is based on the reciprocal value of frequency where T = 1/f.\n" +
                                    "<h2><b>Angular Displacement</b></h2>\n" +
                                    "//<Insert diagram>\n" +
                                    "The angular displacement (Δθ) is the angle traveled by the object while moving from point B to C around the circular path.\n" +
                                    "<h2><b>Length of Arc</b></h2>\n" +
                                    "The length of arc (ΔS) is directly proportional to the angular displacement subtended traced at the center of circle by the ends of the arc.\n" +
                                    "Length of arc can be calculated using this formula:\n" +
                                    "//<Insert formula>\n" +
                                    "<h2><b>Tangential Velocity</b></h2>\n" +
                                    "The tangential velocity (v) is the velocity measured at any tangential point in a circle.\n" +
                                    "Tangential velocity can be calculated using this formula:\n" +
                                    "//<Insert formula>\n" +
                                    "The velocity around the circle can be calculated using this formula:\n" +
                                    "//<Insert formula>\n" +
                                    "<h2><b>Angular Velocity</b></h2>\n" +
                                    "The angular velocity (ω) is rate of change of angular position of a body that is rotating.\n" +
                                    "The SI unit used for angular velocity is radian per second (rad/s).\n" +
                                    "Angular velocity can be calculated using this formula:\n" +
                                    "//<Insert formula>\n" +
                                    "Since angular velocity is a vector quantity, it is defined as positive when the motion in the circle is in counter-clockwise, and is defined as negative when the motion in the circle is in clockwise.\n" +
                                    "The relationship between the linear velocity and angular velocity is:\n" +
                                    "//<Insert formula>\n" +
                                    "<h2><b>Centripetal Acceleration</b></h2>\n" +
                                    "The centripetal acceleration is the rate of change of tangential velocity.\n" +
                                    "The centripetal acceleration is always pointing towards the center of the circle in motion.\n" +
                                    "Centripetal acceleration can be calculated using this formula:\n" +
                                    "//<Insert formula>\n</p>",
                            "", "", ""},
                    {"Centripetal and Centrifugal Forces", "Uniform Circular Motion",
                            "Definition, Centripetal and Centrifugal Forces",
                            "<p><h2><b>Definition</b></h2>\n" +
                                    "Centripetal Force:\n" +
                                    "•\tis a force that acts on an object that is moving in a circular path and is directed towards the center where the object is moving\n" +
                                    "Centrifugal Force:\n" +
                                    "•\tis the opposing reaction force of the centripetal force\n" +
                                    "•\tis a force acting outwards of an object that is moving in a circular path\n" +
                                    "<h2><b>Centripetal and Centrifugal Forces</b></h2>\n" +
                                    "According the Newton’s second law motion, where there is an acceleration (centripetal acceleration), there is a force (centripetal force).\n" +
                                    "//<Insert diagram>\n" +
                                    "Magnitude of the centripetal force can be calculated using this formula:\n" +
                                    "//<Insert formula>\n" +
                                    "Where:\n" +
                                    "<b>m</b> = mass of the object\n" +
                                    "<b>v</b> = tangential velocity of the object\n" +
                                    "<b>r</b> = radius of curvature cause by the force\n" +
                                    "Newton’s third law of motion states that every action has an equal and opposite reaction. Therefore, in this case, there must be an equal and opposite reaction force to the centripetal force which is called the centrifugal force.\n</p>",
                            "", "", ""},
                    {"Rotational Motion", "Uniform Circular Motion", "Definition, Moment of " +
                            "Inertia, Torque, Angular Momentum",
                            "<p><h2><b>Definition</b></h2>\n" +
                                    "Rotational Motion:\n" +
                                    "•\tis a motion of an object in a circular path around a center (or point) of rotation\n" +
                                    "<h2><b>Moment of Inertia</b></h2>\n" +
                                    "The moment of inertia (I) is the measure of the object’s resistance to the change to its rotation. It is dependent to the object’s mass (m) and distance (r) of the mass further from the center of the rotational motion.\n" +
                                    "Moment of inertia can be calculated using this formula:\n" +
                                    "//<Insert formula>\n" +
                                    "<h2><b>Torque</b></h2>\n" +
                                    "The torque (τ) is the twisting force (F) that tends to cause the rotation of an object which is at position (r) from its axis of rotation. \n" +
                                    "Torque can be calculated using this formula:\n" +
                                    "//<Insert formula>\n" +
                                    "<h2><b>Angular Momentum</b></h2>\n" +
                                    "The angular momentum (L) is the quantity of rotation of a body. It is dependent on the moment of inertia (I) of the object and its angular velocity vector (ω).\n" +
                                    "Angular momentum can be calculated using this formula:\n" +
                                    "//<Insert formula>\n</p>",
                            "", "", ""}
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
                    {"Free-fall Velocity", "Acceleration due to Gravity", "$$g = {v - u \\over t}$$", "v - (u / t)", "g", "\\(m \\over s^2\\)"},
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







