package com.ninetyninecochallenge.places;

import java.util.Date;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.ninetyninecochallenge.places.dto.StoreDetailsDto;
import com.ninetyninecochallenge.places.dto.StoreDto;
import com.ninetyninecochallenge.places.models.FavoriteStoreModel;

public class StoreDetails extends Activity {
	String place_id;
	String storename;
	double lat;
	double lng;
	String icon;
	String query = "&key=";
	String placeDetailsAPI = "https://maps.googleapis.com/maps/api/place/details/json?placeid=";
	String TAG_RESULT = "result";
	String TAG_NAME = "name";
	String TAG_ADDRESS = "formatted_address";
	String TAG_NUMBER = "international_phone_number";
	String TAG_VICINITY = "vicinity";
	String TAG_URL = "url";
	String TAG_WEBSITE = "website";
	String TAG_RATING = "rating";
	StoreDetailsDto storeDetailsDto = new StoreDetailsDto();
	TextView contentpage_description;
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_details);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			place_id = extras.getString("place_id");
			storename = extras.getString("storename");
			lat = extras.getDouble("latitude");
			lng = extras.getDouble("longhitude");
			icon = extras.getString("icon");
			setTitle(storename);
			// ((TextView) findViewById(R.id.placeid)).setText(place_id
			// + " <---- PLACEID");
		}

		contentpage_description = (TextView) findViewById(R.id.contentpage_description);
		new getStoreDetails().execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.store_details_menu, menu);
		if (FavoriteStoreModel.isStoreFavorite(StoreDetails.this, place_id) == true) {
			menu.getItem(0).setIcon(R.drawable.ic_action_favorite_dark);
			menu.getItem(1).setTitle("Remove from favorites");
		} else {
			menu.getItem(0).setIcon(R.drawable.ic_action_favorite);
			menu.getItem(1).setTitle("Add to favorites");
		}

		return true;
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onNavigateUp() {
		// TODO Auto-generated method stub
		onBackPressed();
		return super.onNavigateUp();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
		StoreDetails.this.overridePendingTransition(0, R.anim.slide_out_down);
		super.onBackPressed();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {

		/*
		 * Checkin/Checkout Button codes
		 */
		case R.id.add_to_favorites:
			invokeFavoritesActions();
			return true;
		case R.id.overflow_add_to_favorites:
			invokeFavoritesActions();
			return true;

		default:
			onBackPressed();
		}

		return super.onOptionsItemSelected(item);
	}

	public void invokeFavoritesActions() {
		if (FavoriteStoreModel.isStoreFavorite(this, place_id) == true) {

			AlertDialog.Builder adb = new AlertDialog.Builder(StoreDetails.this);
			adb.setTitle("Remove From Favorites");
			adb.setIcon(R.drawable.warning);
			adb.setMessage("Are you sure you want to remove " + storename
					+ " from favorites?");
			adb.setNegativeButton("No", null);
			adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int arg1) { //
					FavoriteStoreModel.deleteSingleFavoriteStore(
							StoreDetails.this, place_id);
					invalidateOptionsMenu();
				}

			});

			adb.show();
		} else {
			AlertDialog.Builder adb = new AlertDialog.Builder(StoreDetails.this);
			adb.setTitle("Add To Favorites");
			adb.setIcon(R.drawable.warning);
			adb.setMessage("Are you sure you want to add " + storename
					+ " into favorites?");
			adb.setNegativeButton("No", null);
			adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int arg1) { //
					StoreDto storeDto = new StoreDto();
					storeDto.setPlace_id(place_id);
					storeDto.setName(storename);
					storeDto.setLat(lat);
					storeDto.setLonghi(lng);
					storeDto.setIcon(icon);
					storeDto.setAddress(storeDetailsDto.getStoreAddress());
					storeDto.setDatecreated(new Date().toString());
					FavoriteStoreModel.insertFavoriteStore(StoreDetails.this,
							storeDto);
					invalidateOptionsMenu();
				}

			});

			adb.show();
		}
	}

	private class getStoreDetails extends AsyncTask<String, Void, String> {

		JSONObject jsonObjectParent;
		JSONParser jParser = new JSONParser();
		JSONObject jsonObjectResult;

		public getStoreDetails() {

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			progressDialog = ProgressDialog.show(StoreDetails.this,
					"Please wait...", "Loading store details...");

			progressDialog.setCancelable(false);
			System.out.println("Started the asynctask");
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... placesURL) {
			// Simulates a background job.

			jsonObjectParent = new JSONObject();
			try {
				// Getting Array of Contacts

				// try {

				jsonObjectParent = jParser.getJSONFromUrl2(placeDetailsAPI
						+ place_id + query
						+ getResources().getString(R.string.apikey));

				if (jParser.getResponseCode() == 200) {

					jsonObjectResult = jsonObjectParent
							.getJSONObject(TAG_RESULT);
					try {
						storeDetailsDto.setStoreName(jsonObjectResult
								.getString(TAG_NAME));
					} catch (Exception e) {
						storeDetailsDto.setStoreName("");
					}

					try {
						storeDetailsDto.setStoreAddress(jsonObjectResult
								.getString(TAG_ADDRESS));
					} catch (Exception e) {
						storeDetailsDto.setStoreAddress("");
					}

					try {
						storeDetailsDto.setPhoneNumber(jsonObjectResult
								.getString(TAG_NUMBER));
					} catch (Exception e) {
						storeDetailsDto.setPhoneNumber("");
					}

					try {
						storeDetailsDto.setRating(jsonObjectResult
								.getDouble(TAG_RATING));
					} catch (Exception e) {
						storeDetailsDto.setRating(0);
					}

					try {
						storeDetailsDto.setUrl(jsonObjectResult
								.getString(TAG_URL));
					} catch (Exception e) {
						storeDetailsDto.setUrl("");
					}
					try {
						storeDetailsDto.setVicinity(jsonObjectResult
								.getString(TAG_VICINITY));
					} catch (Exception e) {
						storeDetailsDto.setVicinity("");
					}

					try {
						storeDetailsDto.setWebsite(jsonObjectResult
								.getString(TAG_WEBSITE));
					} catch (Exception e) {
						storeDetailsDto.setWebsite("");
					}

				}

				// looping through All Contacts

			} catch (Exception e) {
				e.printStackTrace();
			}

			return "";
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			System.out.println("OnPostExecute...");
			contentpage_description.setText("Store Name: " + storename
					+ "\n");
			if (jParser.getResponseCode() == 200) {
//				contentpage_description.setText("");
				
				contentpage_description.append("Address: "
						+ storeDetailsDto.getStoreAddress() + "\n");
				contentpage_description.append("Phone Number: "
						+ storeDetailsDto.getPhoneNumber() + "\n");
				contentpage_description.append("Vicinity: "
						+ storeDetailsDto.getVicinity() + "\n");
				contentpage_description.append("URL: "
						+ storeDetailsDto.getUrl() + "\n");
				contentpage_description.append("Website: "
						+ storeDetailsDto.getWebsite() + "\n");
				contentpage_description.append("Rating: "
						+ storeDetailsDto.getRating() + "\n");
				super.onPostExecute(result);
			} else {
				contentpage_description.append("No store details loaded! Please try again.");
			}
			progressDialog.dismiss();
		}
	}

}
