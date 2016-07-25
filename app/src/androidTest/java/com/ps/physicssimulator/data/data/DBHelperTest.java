package com.ps.physicssimulator.data.data;


import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.content.Context;
import android.support.test.runner.AndroidJUnit4;

import com.ps.physicssimulator.data.DBHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Created by qwerasdf on 7/21/16.
 */
@RunWith(AndroidJUnit4.class)
public class DBHelperTest {

    private DBHelper dbHelper;
    private Context mContext;

    @Before
    public void setUp(){
        mContext = InstrumentationRegistry.getTargetContext();
        dbHelper = new DBHelper(mContext);
        mContext.deleteDatabase(DBHelper.DATABASE_NAME);
    }

    @Test
    public void testCreateDB(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        assertTrue("Database not created!", db.isOpen());
        db.close();
    }

    @Test
    public void testDeleteDB(){
        mContext.deleteDatabase(DBHelper.DATABASE_NAME);
    }

}
