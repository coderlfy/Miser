package com.icat.miser.db;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.icat.miser.model.*;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

@SuppressLint("SimpleDateFormat")
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
				+ TableName, null);

		int strid = 0;
		if (cursor.moveToFirst())
			strid = cursor.getInt(0);
		Log.i("testAuto", strid + "");

		db.close(); // Closing database connection
		return strid;
	}

	// Updating single contact
	public int Update(ConsumptionModel consumption) {
		SQLiteDatabase db = this.mSqlite.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(TConsumptionColumns.title, consumption.getTitle());
		values.put(TConsumptionColumns.isConsumption,
				consumption.getIsConsumption());
		values.put(TConsumptionColumns.money, consumption.getMoney());
		values.put(TConsumptionColumns.startDate,
				consumption.getStartDateString());

		int keyid = consumption.getId();
		Log.d("updateid", String.valueOf(keyid));
		return db.update(TableName, values, TConsumptionColumns.ID + " = ?",
				new String[] { String.valueOf(keyid) });// Ö÷¼üÒªÐÞ¸Ä£¿£¿£¿
	}

	// Deleting single contact
	public void Delete(int id) {
		SQLiteDatabase db = this.mSqlite.getWritableDatabase();
		db.delete(TableName, TConsumptionColumns.ID + " = ?",
				new String[] { String.valueOf(id) });
		db.close();
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
							(new SimpleDateFormat("yyyy-MM-dd HH:mm"))
									.parse(cursor.getString(3)));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		cursor.close();
		db.close();

		return consumption;
	}

	// Getting All Contacts
	public ArrayList<ConsumptionModel> Get(int pageNumber, int pageSize,
			List<Integer> newIds) {
		ArrayList<ConsumptionModel> mConsumptions = null;
		try {

			mConsumptions = new ArrayList<ConsumptionModel>();
			String notids = "", sqlwhere = "";
			if (newIds != null) {
				for (int i : newIds) {
					notids += i + ",";
				}

				if (newIds.size() > 0)
					notids = notids.substring(0, notids.length() - 1);

				sqlwhere = " where " + TConsumptionColumns.ID + " not in ("
						+ notids + ")";
			}
			int start = (pageNumber <= 0) ? 0 : ((pageNumber - 1) * pageSize);
			String selectQuery = "select * from " + TableName + sqlwhere
					+ " order by " + TConsumptionColumns.startDate
					+ " desc limit " + pageSize + " offset " + start;

			// Select All Query
			// String selectQuery = "SELECT  * FROM " + TableName + " order by "
			// + TConsumptionColumns.startDate + " desc";

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
								.setIsConsumption(cursor.getInt(2) == 1)
								.setStartDate(
										(new SimpleDateFormat(
												"yyyy-MM-dd HH:mm"))
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

	// Getting contacts Count
	public int GetCount() {
		String countQuery = "SELECT  * FROM " + TableName;
		SQLiteDatabase db = this.mSqlite.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);

		int c = cursor.getCount();
		cursor.close();
		return c;
	}

	public ArrayList<ConsumptionMonthTotalModel> GetMonthTotal(int year) {
		ArrayList<ConsumptionMonthTotalModel> monthtotals = null;
		try {

			monthtotals = new ArrayList<ConsumptionMonthTotalModel>();

			String selectQuery = "select sum(" + TConsumptionColumns.money
					+ ") as totalmoney, strftime('%m',"
					+ TConsumptionColumns.startDate + ") as month,"
					+ TConsumptionColumns.isConsumption + " from " + TableName
					+ " group by strftime('%m',"
					+ TConsumptionColumns.startDate + "),"
					+ TConsumptionColumns.isConsumption
					+ " order by strftime('%m'," + TConsumptionColumns.startDate
					+ "), " + TConsumptionColumns.isConsumption;

			SQLiteDatabase db = this.mSqlite.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					ConsumptionMonthTotalModel monthtotal = new ConsumptionMonthTotalModel();

					monthtotal.setMoney(cursor.getDouble(0))
							.setMonthViewString(cursor.getString(1))
							.setIsConsumption(cursor.getInt(2) == 1);

					monthtotals.add(monthtotal);
				} while (cursor.moveToNext());
			}

			// return contact list
			cursor.close();
			db.close();
			return monthtotals;
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("all_contact", "" + e);
		}

		return monthtotals;
	}
}
