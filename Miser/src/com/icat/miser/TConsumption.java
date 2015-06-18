package com.icat.miser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TConsumption {
	public static final String TableName = "Consumption";

	static final String CREATE_Consumption_TABLE_SQL = "CREATE TABLE "
			+ TConsumption.TableName + "(" + TConsumptionColumns.ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT," + TConsumptionColumns.title
			+ " TEXT," + TConsumptionColumns.isConsumption + " NUMERIC,"
			+ TConsumptionColumns.money + " REAL,"
			+ TConsumptionColumns.startDate
			+ " DATETIME DEFAULT CURRENT_TIMESTAMP" + ")";

	public interface TConsumptionColumns {
		public static final String ID = "ID";
		public static final String title = "title";
		public static final String isConsumption = "isConsumption";
		public static final String money = "money";
		public static final String startDate = "startDate";
	}

	private SQLiteOpenHelper mSqlite = null;
	private ArrayList<ConsumptionModel> mConsumptions = null;

	public void setDBHelper(SQLiteOpenHelper sqlite) {
		this.mSqlite = sqlite;
	}

	public int Add(ConsumptionModel consumption) {
		SQLiteDatabase db = this.mSqlite.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(TConsumptionColumns.title, consumption.getTitle());

		values.put(TConsumptionColumns.isConsumption,
				consumption.getIsConsumption());
		values.put(TConsumptionColumns.money, consumption.getMoney());
		values.put(TConsumptionColumns.startDate,
				consumption.getStartDateString());

		// Inserting Row
		db.insert(TableName, null, values);

		Cursor cursor = db.rawQuery("select last_insert_rowid() from "
				+ this.TableName, null);

		int strid = 0;
		if (cursor.moveToFirst())
			strid = cursor.getInt(0);
		Log.i("testAuto", strid + "");

		db.close(); // Closing database connection
		return strid;
	}

	// Getting single contact
	ConsumptionModel Get(int id) {
		SQLiteDatabase db = this.mSqlite.getReadableDatabase();

		Cursor cursor = db.query(TableName, new String[] {
				TConsumptionColumns.title, TConsumptionColumns.isConsumption,
				TConsumptionColumns.money, TConsumptionColumns.startDate },
				TConsumptionColumns.ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		ConsumptionModel consumption = new ConsumptionModel();

		try {
			consumption
					.setTitle(cursor.getString(0))
					.setMoney(cursor.getDouble(2))
					.setIsConsumption(cursor.getInt(1) == 1)
					.setStartDate(
							(new SimpleDateFormat("yyyy-MM-dd")).parse(cursor
									.getString(3)));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		cursor.close();
		db.close();

		return consumption;
	}

	// Getting All Contacts
	public ArrayList<ConsumptionModel> Get() {
		try {
			if (mConsumptions != null)
				mConsumptions.clear();
			else
				mConsumptions = new ArrayList<ConsumptionModel>();

			// Select All Query
			String selectQuery = "SELECT  * FROM " + TableName + " order by "
					+ TConsumptionColumns.startDate + " desc";

			SQLiteDatabase db = this.mSqlite.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					try {
						ConsumptionModel consumption = new ConsumptionModel();

						consumption
								.setId(cursor.getInt(0))
								.setTitle(cursor.getString(1))
								.setMoney(cursor.getDouble(3))
								.setIsConsumption(cursor.getInt(2) != 1)
								.setStartDate(
										(new SimpleDateFormat("yyyy-MM-dd"))
												.parse(cursor.getString(4)));

						mConsumptions.add(consumption);

					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// Adding contact to list
				} while (cursor.moveToNext());
			}

			// return contact list
			cursor.close();
			db.close();
			return mConsumptions;
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("all_contact", "" + e);
		}

		return mConsumptions;
	}

	// Updating single contact
	public int Update(ConsumptionModel consumption) {
		SQLiteDatabase db = this.mSqlite.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(TConsumptionColumns.title, consumption.getTitle());
		values.put(TConsumptionColumns.isConsumption,
				consumption.getIsConsumption());
		values.put(TConsumptionColumns.money, consumption.getMoney());
		values.put(TConsumptionColumns.startDate, consumption.getStartDateString());

		// updating row
		return db.update(TableName, values, TConsumptionColumns.ID + " = ?",
				new String[] { String.valueOf(consumption.getId()) });// Ö÷¼üÒªÐÞ¸Ä£¿£¿£¿
	}

	// Deleting single contact
	public void Delete(int id) {
		SQLiteDatabase db = this.mSqlite.getWritableDatabase();
		db.delete(TableName, TConsumptionColumns.ID + " = ?",
				new String[] { String.valueOf(id) });
		db.close();
	}

	// Getting contacts Count
	public int GetCount() {
		String countQuery = "SELECT  * FROM " + TableName;
		SQLiteDatabase db = this.mSqlite.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}

}
