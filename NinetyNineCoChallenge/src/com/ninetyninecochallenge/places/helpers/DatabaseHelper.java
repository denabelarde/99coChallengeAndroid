package com.ninetyninecochallenge.places.helpers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class DatabaseHelper extends SQLiteOpenHelper {
 
	
	// The Android's default system path of your application database.
	static final int DB_VERSION = 1;
	static final String DB_NAME = "99cochallenge";
	// private SQLiteDatabase mDatabase;
	private final Context mContext;

	/**
	 * Constructor Takes and keeps a reference of the passed context in order to
	 * access to the application assets and resources.
	 * 
	 * @param context
	 */

	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		// TODO Auto-generated constructor stub
		this.mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		System.out.println("create database");
		executeSQLScript(db, "99cochallenge.sql");

	}

	private void executeSQLScript(SQLiteDatabase database, String dbname) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte buf[] = new byte[1024];
		int len;
		AssetManager assetManager = mContext.getAssets();
		InputStream inputStream = null;

		try {
			inputStream = assetManager.open(dbname);
			// System.out.println("Error");
			while ((len = inputStream.read(buf)) != -1) {
				outputStream.write(buf, 0, len);
			}
			outputStream.close();
			inputStream.close();

			String[] createScript = outputStream.toString().split(";");
			for (int i = 0; i < createScript.length; i++) {
				String sqlStatement = createScript[i].trim();
				// TODO You may want to parse out comments here
				if (sqlStatement.length() > 0) {
					database.execSQL(sqlStatement + ";");
				}
			}
		} catch (IOException e) {
			// TODO Handle Script Failed to Load
			System.out.println("Failed To load");
		} catch (SQLException e) {
			// TODO Handle Script Failed to Execute
			System.out.println("Failed To Execute");
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		System.out.println("upgrade database");
		if (newVersion > oldVersion) {
			System.out.println("New Version Database detected!");
			switch (oldVersion) {
			case 1:
				// executeSQLScript(db, "update_v2.sql");
			case 2:
				// executeSQLScript(db, "update_v3.sql");
			}
		}
	}

	// Add your public helper methods to access and get content from the
	// database.
	// You could return cursors by doing "return myDataBase.query(....)" so it'd
	// be easy
	// to you to create adapters for your views.
	public Cursor query(String table, String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy) {
		SQLiteDatabase mDatabase = getReadableDatabase();
		Cursor cursor = mDatabase.query(table, columns, selection,
				selectionArgs, groupBy, having, orderBy);

		return cursor;
	}

	public Cursor rawQuery(String sql, String[] selectionArgs) {
		SQLiteDatabase mDatabase = getReadableDatabase();
		Cursor cursor = mDatabase.rawQuery(sql, selectionArgs);
		return cursor;
	}

	public long insert(String table, ContentValues values) {
		SQLiteDatabase mDatabase = getWritableDatabase();
		long rowid = mDatabase.insert(table, null, values);
		mDatabase.close();
		return rowid;
	}

	public void batchInsert(String sql, ArrayList<String[]> values) {
		SQLiteDatabase db = getWritableDatabase();
		db.beginTransaction();

		SQLiteStatement stmt = db.compileStatement(sql);
		for (int i = 0; i < values.size(); i++) {
			String[] record = values.get(i);

			for (int ii = 0; ii < record.length; ii++) {
				stmt.bindString(ii + 1, record[ii]);
			}

			stmt.execute();
			stmt.clearBindings();
		}

		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();
	}

	public int update(String table, ContentValues values, String whereClause,
			String[] whereArgs) {
		SQLiteDatabase mDatabase = getWritableDatabase();
		int rows = mDatabase.update(table, values, whereClause, whereArgs);
		mDatabase.close();
		return rows;
	}

	public int delete(String table, String whereClause, String[] whereArgs) {
		SQLiteDatabase mDatabase = getWritableDatabase();
		int rows = mDatabase.delete(table, whereClause, whereArgs);
		mDatabase.close();
		return rows;
	}
}