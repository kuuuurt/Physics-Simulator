package com.ps.physicssimulator.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.ComponentName;
import com.ps.physicssimulator.data.DataContract.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class DataProviderInstrumentedTest {
    private Context mContext;
    private DBHelper dbHelper;
    private SQLiteDatabase db;


    @Before
    public void setup(){
        mContext = InstrumentationRegistry.getTargetContext();
        dbHelper = new DBHelper(mContext);
        db = dbHelper.getWritableDatabase();
    }

    @Test
    public void testProviderRegistry() {
        PackageManager pm = mContext.getPackageManager();
        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                DataProvider.class.getName());
        try {
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);
            assertEquals("DataProvider registered with authority: " + providerInfo.authority +
                    " instead of authority: " + DataContract.CONTENT_AUTHORITY,
                    providerInfo.authority, DataContract.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            assertTrue("DataProvider not registered at " + mContext.getPackageName(), false);
        }
    }

    @Test
    public void testGetType(){
        String type = mContext.getContentResolver().getType(LessonEntry.CONTENT_URI);
        assertEquals("Uri should return CONTENT_TYPE",
                LessonEntry.CONTENT_TYPE, type);

        String testVar = "Name";
        type = mContext.getContentResolver().getType(LessonEntry.buildLessonTitle(testVar));
        assertEquals("Uri should return CONTENT_ITEM_TYPE",
                LessonEntry.CONTENT_ITEM_TYPE, type);

        type = mContext.getContentResolver().getType(ChapterEntry.CONTENT_URI);
        assertEquals("Uri should return CONTENT_TYPE",
                ChapterEntry.CONTENT_TYPE, type);

        type = mContext.getContentResolver().getType(ChapterEntry.buildChapterName(testVar));
        assertEquals("Uri should return CONTENT_ITEM_TYPE",
                ChapterEntry.CONTENT_ITEM_TYPE, type);

        type = mContext.getContentResolver().getType(ConstantEntry.CONTENT_URI);
        assertEquals("Uri should return CONTENT_TYPE",
                ConstantEntry.CONTENT_TYPE, type);

        type = mContext.getContentResolver().getType(ConstantEntry.buildConstantName(testVar));
        assertEquals("Uri should return CONTENT_ITEM_TYPE",
                ConstantEntry.CONTENT_ITEM_TYPE, type);

    }
}
