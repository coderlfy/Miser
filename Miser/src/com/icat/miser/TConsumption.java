package com.icat.miser;

public class TConsumption {
	public static final String TableName = "Consumption";
	
	static final String CREATE_Consumption_TABLE_SQL = "CREATE TABLE "
			+ TConsumption.TableName + "(" + TConsumptionColumns.ID
			+ " INTEGER PRIMARY KEY," + TConsumptionColumns.title
			+ " TEXT NOT NULL DEFAULT 0," + TConsumptionColumns.isConsumption
			+ " NUMERIC NOT NULL DEFAULT 0," + TConsumptionColumns.money
			+ " INTEGER NOT NULL DEFAULT 0" + TConsumptionColumns.startDate
			+ " INTEGER NOT NULL DEFAULT 0" + ")";

	public interface TConsumptionColumns {
		public static final String ID = "ID";
		public static final String title = "title";
		public static final String isConsumption = "isConsumption";
		public static final String money = "money";
		public static final String startDate = "startDate";
	}
}
