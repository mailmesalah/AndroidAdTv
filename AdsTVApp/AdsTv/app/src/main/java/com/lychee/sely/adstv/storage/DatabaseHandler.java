package com.lychee.sely.adstv.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.lychee.sely.adstv.storage.tables.CustomAd;
import com.lychee.sely.adstv.storage.tables.CustomDetailAd;
import com.lychee.sely.adstv.storage.tables.ImageAd;
import com.lychee.sely.adstv.storage.tables.ScheduledAd;
import com.lychee.sely.adstv.storage.tables.ScheduledAdProgram;
import com.lychee.sely.adstv.storage.tables.StackedAd;
import com.lychee.sely.adstv.storage.tables.StackedAdProgram;
import com.lychee.sely.adstv.storage.tables.VideoAd;

/**
 * Created by Sely on 06-Jan-17.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables

	private static DatabaseHandler database;

	private DatabaseHandler(Context context) {
		super(context, "Storage", null, 8);
	}

	public static synchronized DatabaseHandler getInstance(Context context) {

		// Use the application context, which will ensure that you
		// don't accidentally leak an Activity's context.
		if (database == null) {
			database = new DatabaseHandler(context.getApplicationContext());
		}
		return database;
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
        //Table creation
        StackedAdProgram.onCreate(db);
        StackedAd.onCreate(db);
        ImageAd.onCreate(db);
        VideoAd.onCreate(db);
        CustomAd.onCreate(db);
        CustomDetailAd.onCreate(db);
        ScheduledAd.onCreate(db);
        ScheduledAdProgram.onCreate(db);
        Log.d("Database : ", "Database and Table Created");
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Upgrade Tables
        StackedAdProgram.onUpgrade(db,oldVersion,newVersion);
        StackedAd.onUpgrade(db,oldVersion,newVersion);
        ImageAd.onUpgrade(db,oldVersion,newVersion);
        VideoAd.onUpgrade(db,oldVersion,newVersion);
        CustomAd.onUpgrade(db,oldVersion,newVersion);
        CustomDetailAd.onUpgrade(db,oldVersion,newVersion);
        ScheduledAd.onUpgrade(db,oldVersion,newVersion);
        ScheduledAdProgram.onUpgrade(db,oldVersion,newVersion);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */


    //StackedAdProgram
	public synchronized Cursor getStackedAdPrograms(String selection, String[] selectionArgs){
        SQLiteDatabase rdb = database.getReadableDatabase();

        return rdb.query("StackedAdProgram", new String[] { "_ID", "ProgramName", "Repeat" }, selection, selectionArgs, null, null, null);
    }

    public synchronized long addStackedAdProgram(ContentValues values){
        long id = database.getWritableDatabase().insert("StackedAdProgram", "", values);
        return id;
    }

    public synchronized int updateStackedAdProgram(ContentValues values, String selection, String[] selectionArgs){
        long id = database.getWritableDatabase().update("StackedAdProgram",values,selection,selectionArgs);
        if(id <=0 ) {
            return (int)id;
        }
        return (int)id;
    }

    public synchronized int deleteStackedAdProgram(String selection, String[] selectionArgs){
        long id = database.getWritableDatabase().delete("StackedAdProgram",selection,selectionArgs);
        if(id <=0 ) {
            return (int)id;
        }
        return (int) id;
    }

    //ScheduledAdItem
    public synchronized Cursor getStackedAds(String selection, String[] selectionArgs, String sortOrder){
        SQLiteDatabase rdb = database.getReadableDatabase();
        return rdb.query("StackedAd", new String[] { "_ID", "SerialNo", "ProgramCode", "AdType", "AdCode", "AdName", "AdPath", "AdTimePeriod" }, selection, selectionArgs, null, null, sortOrder);
    }

    public synchronized long addStackedAd(ContentValues values){
        long id = database.getWritableDatabase().insert("StackedAd", "", values);
        return id;
    }

    public synchronized int updateStackedAd(ContentValues values, String selection, String[] selectionArgs){
        long id = database.getWritableDatabase().update("StackedAd",values,selection,selectionArgs);
        if(id <=0 ) {
            return (int)id;
        }
        return (int)id;
    }

    public synchronized int deleteStackedAd(String selection, String[] selectionArgs){
        long id = database.getWritableDatabase().delete("StackedAd",selection,selectionArgs);
        if(id <=0 ) {
            return (int)id;
        }
        return (int) id;
    }

    //ImageAd
    public synchronized Cursor getImageAds(String selection, String[] selectionArgs, String sortOrder){
        SQLiteDatabase rdb = database.getReadableDatabase();
        return rdb.query("ImageAd", new String[] { "_ID", "AdName", "AdPath" }, selection, selectionArgs, null, null, sortOrder);
    }

    public synchronized long addImageAd(ContentValues values){
        long id = database.getWritableDatabase().insert("ImageAd", "", values);
        return id;
    }

    public synchronized int updateImageAd(ContentValues values, String selection, String[] selectionArgs){
        long id = database.getWritableDatabase().update("ImageAd",values,selection,selectionArgs);
        if(id <=0 ) {
            return (int)id;
        }
        return (int)id;
    }

    public synchronized int deleteImageAd(String selection, String[] selectionArgs){
        long id = database.getWritableDatabase().delete("ImageAd",selection,selectionArgs);
        if(id <=0 ) {
            return (int)id;
        }
        return (int) id;
    }


    //VideoAd
    public synchronized Cursor getVideoAds(String selection, String[] selectionArgs, String sortOrder){
        SQLiteDatabase rdb = database.getReadableDatabase();
        return rdb.query("VideoAd", new String[] { "_ID", "AdName", "AdPath" }, selection, selectionArgs, null, null, sortOrder);
    }

    public synchronized long addVideoAd(ContentValues values){
        long id = database.getWritableDatabase().insert("VideoAd", "", values);
        return id;
    }

    public synchronized int updateVideoAd(ContentValues values, String selection, String[] selectionArgs){
        long id = database.getWritableDatabase().update("VideoAd",values,selection,selectionArgs);
        if(id <=0 ) {
            return (int)id;
        }
        return (int)id;
    }

    public synchronized int deleteVideoAd(String selection, String[] selectionArgs){
        long id = database.getWritableDatabase().delete("VideoAd",selection,selectionArgs);
        if(id <=0 ) {
            return (int)id;
        }
        return (int) id;
    }

    //ScheduledAdProgram
    public synchronized Cursor getScheduledAdPrograms(String selection, String[] selectionArgs){
        SQLiteDatabase rdb = database.getReadableDatabase();

        return rdb.query("ScheduledAdProgram", new String[] { "_ID", "ProgramName", "Repeat", "RepeatType" }, selection, selectionArgs, null, null, null);
    }

    public synchronized long addScheduledAdProgram(ContentValues values){
        long id = database.getWritableDatabase().insert("ScheduledAdProgram", "", values);
        return id;
    }

    public synchronized int updateScheduledAdProgram(ContentValues values, String selection, String[] selectionArgs){
        long id = database.getWritableDatabase().update("ScheduledAdProgram",values,selection,selectionArgs);
        if(id <=0 ) {
            return (int)id;
        }
        return (int)id;
    }

    public synchronized int deleteScheduledAdProgram(String selection, String[] selectionArgs){
        long id = database.getWritableDatabase().delete("ScheduledAdProgram",selection,selectionArgs);
        if(id <=0 ) {
            return (int)id;
        }
        return (int) id;
    }

    //ScheduledAdItem
    public synchronized Cursor getScheduledAds(String selection, String[] selectionArgs, String sortOrder){
        SQLiteDatabase rdb = database.getReadableDatabase();
        return rdb.query("ScheduledAd", new String[] { "_ID", "ProgramCode", "AdType", "AdCode", "AdName", "AdPath", "AdDateTime" }, selection, selectionArgs, null, null, sortOrder);
    }

    public synchronized long addScheduledAd(ContentValues values){
        long id = database.getWritableDatabase().insert("ScheduledAd", "", values);
        return id;
    }

    public synchronized int updateScheduledAd(ContentValues values, String selection, String[] selectionArgs){
        long id = database.getWritableDatabase().update("ScheduledAd",values,selection,selectionArgs);
        if(id <=0 ) {
            return (int)id;
        }
        return (int)id;
    }

    public synchronized int deleteScheduledAd(String selection, String[] selectionArgs){
        long id = database.getWritableDatabase().delete("ScheduledAd",selection,selectionArgs);
        if(id <=0 ) {
            return (int)id;
        }
        return (int) id;
    }


    //Custom Ads 27-May-17
    public Cursor getCustomAds(String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase rdb = database.getReadableDatabase();
        return rdb.query("CustomAd", new String[] { "_ID", "AdName" }, selection, selectionArgs, null, null, sortOrder);
    }

    public long addCustomAd(ContentValues values) {
        return database.getWritableDatabase().insert("CustomAd", "", values);
    }

    public int deleteCustomAd(String selection, String[] selectionArgs) {
        long id = database.getWritableDatabase().delete("CustomAd",selection,selectionArgs);
        if(id <=0 ) {
            return (int)id;
        }
        return (int) id;
        //Delete details from where this is called
    }

    public int updateCustomAd(ContentValues values, String selection, String[] selectionArgs) {
        long id = database.getWritableDatabase().update("CustomAd",values,selection,selectionArgs);
        if(id <=0 ) {
            return (int)id;
        }
        return (int)id;
    }

    public Cursor getCustomDetailAds(String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase rdb = database.getReadableDatabase();
        return rdb.query("CustomDetailAd", new String[] { "_ID", "AdId", "X", "Y", "Width", "Height", "ZOrder", "AdType", "AdName", "AdPath" }, selection, selectionArgs, null, null, sortOrder);
    }

    public long addCustomDetailAd(ContentValues values) {
        return database.getWritableDatabase().insert("CustomDetailAd", "", values);
    }

    public int deleteCustomDetailAd(String selection, String[] selectionArgs) {
        long id = database.getWritableDatabase().delete("CustomDetailAd",selection,selectionArgs);
        if(id <=0 ) {
            return (int)id;
        }
        return (int) id;
    }

    public int updateCustomDetailAd(ContentValues values, String selection, String[] selectionArgs) {
        long id = database.getWritableDatabase().update("CustomDetailAd",values,selection,selectionArgs);
        if(id <=0 ) {
            return (int)id;
        }
        return (int)id;
    }
}