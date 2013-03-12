//  16:17   10.03.2013 
package com.example.android_test_assignment.model.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.example.android_test_assignment.model.PointData;

import java.util.ArrayList;
import java.util.List;


public class PointsDAO {

	private SQLiteDatabase db;
	private PointsTableSQLiteHelper dbHelper;
	private String[] allColumns = {PointsTableSQLiteHelper.COLUMN_ID, PointsTableSQLiteHelper.COLUMN_X_COORDINATE, PointsTableSQLiteHelper.COLUMN_Y_COORDINATE};


	public PointsDAO(Context context) {
		dbHelper = new PointsTableSQLiteHelper(context);
	}

	public void open() throws SQLException {
		db = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public PointData createPoint(int x, int y) {
		ContentValues values = new ContentValues();
		values.put(PointsTableSQLiteHelper.COLUMN_X_COORDINATE, x);
		values.put(PointsTableSQLiteHelper.COLUMN_Y_COORDINATE, y);
		long insertId = db.insert(PointsTableSQLiteHelper.TABLE_POINTS, null, values);
		Cursor cursor = db.query(PointsTableSQLiteHelper.TABLE_POINTS, allColumns, PointsTableSQLiteHelper.COLUMN_ID + " = " + insertId,
				null, null, null, null);
		cursor.moveToFirst();
		PointData newPoint = cursorToPoint(cursor);
		cursor.close();
		return newPoint;
	}

	private PointData cursorToPoint(Cursor cursor) {
		PointData point = new PointData();
		point.id = cursor.getLong(0);
		point.x = cursor.getInt(1);
		point.y = cursor.getInt(2);
		return point;
	}

	public void updatePoint(PointData point, int x, int y) {
		ContentValues values = new ContentValues();
		values.put(PointsTableSQLiteHelper.COLUMN_X_COORDINATE, x);
		values.put(PointsTableSQLiteHelper.COLUMN_Y_COORDINATE, y);
		db.update(PointsTableSQLiteHelper.TABLE_POINTS, values, PointsTableSQLiteHelper.COLUMN_ID + " = " + point.id, null);
	}

	public void deletePoint(PointData point) {
		db.delete(PointsTableSQLiteHelper.TABLE_POINTS, PointsTableSQLiteHelper.COLUMN_ID + " = " + point.id, null);
	}

	public void deleteAllPoints() {
		db.delete(PointsTableSQLiteHelper.TABLE_POINTS, null, null);
	}

	public List<PointData> getAllPoints() {
		List<PointData> points = new ArrayList<PointData>();
		Cursor cursor = db.query(PointsTableSQLiteHelper.TABLE_POINTS, allColumns, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			PointData point = cursorToPoint(cursor);
			points.add(point);
			cursor.moveToNext();
		}
		cursor.close();
		return points;
	}


}
