package com.icat.miser;

import com.icat.miser.TConsumption.*;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MiserDBHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "Miser.db";
	private static final int DB_VERSION = 4;
	private static final String TAG = "MiserDBHelper";

	private static MiserDBHelper mInstance;

	public MiserDBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	public void createNoteTable(SQLiteDatabase db) {
		db.execSQL(TConsumption.CREATE_Consumption_TABLE_SQL);
		Log.d(TAG, "note table has been created");
	}

	/*
	 * private void createSystemFolder(SQLiteDatabase db) { ContentValues values
	 * = new ContentValues();
	 * 
	 * values.put(NoteColumns.ID, Notes.ID_CALL_RECORD_FOLDER);
	 * values.put(NoteColumns.TYPE, Notes.TYPE_SYSTEM); db.insert(TABLE.NOTE,
	 * null, values);
	 * 
	 * values.clear(); values.put(NoteColumns.ID, Notes.ID_ROOT_FOLDER);
	 * values.put(NoteColumns.TYPE, Notes.TYPE_SYSTEM); db.insert(TABLE.NOTE,
	 * null, values);
	 * 
	 * values.clear(); values.put(NoteColumns.ID, Notes.ID_TEMPARAY_FOLDER);
	 * values.put(NoteColumns.TYPE, Notes.TYPE_SYSTEM); db.insert(TABLE.NOTE,
	 * null, values);
	 * 
	 * values.clear(); values.put(NoteColumns.ID, Notes.ID_TRASH_FOLER);
	 * values.put(NoteColumns.TYPE, Notes.TYPE_SYSTEM); db.insert(TABLE.NOTE,
	 * null, values); }
	 * 
	 * public void createDataTable(SQLiteDatabase db) {
	 * db.execSQL(CREATE_Consumption_TABLE_SQL); Log.d(TAG,
	 * "data table has been created"); }
	 */

	static synchronized MiserDBHelper getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new MiserDBHelper(context);
		}
		return mInstance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createNoteTable(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

	/*
	 * @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int
	 * newVersion) { boolean reCreateTriggers = false; boolean skipV2 = false;
	 * 
	 * if (oldVersion == 1) { upgradeToV2(db); skipV2 = true; // this upgrade
	 * including the upgrade from v2 to v3 oldVersion++; }
	 * 
	 * if (oldVersion == 2 && !skipV2) { upgradeToV3(db); reCreateTriggers =
	 * true; oldVersion++; }
	 * 
	 * if (oldVersion == 3) { upgradeToV4(db); oldVersion++; }
	 * 
	 * if (reCreateTriggers) { reCreateNoteTableTriggers(db);
	 * reCreateDataTableTriggers(db); }
	 * 
	 * if (oldVersion != newVersion) { throw new IllegalStateException(
	 * "Upgrade notes database to version " + newVersion + "fails"); } }
	 */
}
