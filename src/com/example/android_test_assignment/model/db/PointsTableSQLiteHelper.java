//  16:03   10.03.2013 
package com.example.android_test_assignment.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PointsTableSQLiteHelper extends SQLiteOpenHelper{

	public static final String TABLE_POINTS = "points";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_X_COORDINATE = "x_coordinate";
	public static final String COLUMN_Y_COORDINATE = "y_coordinate";

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "points.db";
	private static final String DATABASE_CREATE = "create table " + TABLE_POINTS + "("
			+ COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_X_COORDINATE + " integer not null, "
			+ COLUMN_Y_COORDINATE + " integer not null);";


	public PointsTableSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_POINTS);
		onCreate(db);
	}
}
