package com.ninetyninecochallenge.places.models;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.ninetyninecochallenge.places.dto.StoreDto;
import com.ninetyninecochallenge.places.helpers.DatabaseHelper;

public class StoreModel {
	public static int count(Context context) {
		DatabaseHelper dbhelper = new DatabaseHelper(context);
		String[] columns = { "count(_id)" };
		int reportcount = 0;

		Cursor report = dbhelper.query("favoritestores", columns, null, null,
				null, null, null);

		if (report.moveToFirst()) {
			reportcount = report.getInt(0);
		}
		report.close();
		dbhelper.close();

		return reportcount;
	}

	public static ArrayList<StoreDto> getAllFavorites(Context context) {
		DatabaseHelper dbhelper = new DatabaseHelper(context);

		Cursor store = dbhelper.query("favoritestores", null, null, null, null,
				null, "datecreated ASC");

		ArrayList<StoreDto> storeList = new ArrayList<StoreDto>();

		if (store.moveToFirst()) {
			do {

				StoreDto storeDto = new StoreDto();
				storeDto.set_id(store.getInt(0));
				storeDto.setPlace_id(store.getString(1));
				storeDto.setName(store.getString(2));
				storeDto.setLat(Double.parseDouble(store.getString(3)));
				storeDto.setLonghi(Double.parseDouble(store.getString(4)));
				storeDto.setIcon(store.getString(5));
				storeDto.setDatecreated(store.getString(6));
				storeList.add(storeDto);
			} while (store.moveToNext());

		}

		store.close();
		dbhelper.close();
		return storeList;
	}

	public static long insertFavoriteStore(Context context, StoreDto storeDto) {
		DatabaseHelper dbhelper = new DatabaseHelper(context);

		ContentValues values = new ContentValues();

		values.put("place_id", storeDto.getPlace_id());
		values.put("name", storeDto.getName());
		values.put("lat", storeDto.getLat());
		values.put("lng", storeDto.getLonghi());
		values.put("icon", storeDto.getIcon());
		values.put("datecreated", storeDto.getDatecreated());
		return dbhelper.insert("favoritestores", values);
	}

	public static void updateUserStore(Context context, long storeid) {
		DatabaseHelper dbhelper = new DatabaseHelper(context);

		ContentValues values = new ContentValues();
		System.out.println(storeid + " storeid sa updateuserstore");
		values.put("storeid", storeid);

		try {
			dbhelper.update("users", values, null, null);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void deletAllFavorites(Context context) {

		DatabaseHelper dbhelper = new DatabaseHelper(context);
		System.out.println(dbhelper.delete("favoritestores", null, null)
				+ " <--- favoritestores Deleted!");

	}

	public static void deletSingleUser(Context context, int _id) {

		DatabaseHelper dbhelper = new DatabaseHelper(context);
		System.out.println(dbhelper.delete("favoritestores", "_id=?",
				new String[] { _id + "" }) + " <--- favorite store Deleted!");

	}
	// public static int countByGreaterDate(Context context, String date) {
	// DatabaseHelper dbhelper = new DatabaseHelper(context);
	// String[] columns = { "count(_id)" };
	// int reportcount = 0;
	//
	// Cursor report = dbhelper.query("users", columns, "datelogin>?",
	// new String[] { date }, null, null, null);
	//
	// if (report.moveToFirst()) {
	// reportcount = report.getInt(0);
	// }
	// report.close();
	// dbhelper.close();
	//
	// return reportcount;
	// }
}
