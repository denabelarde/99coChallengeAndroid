package com.ninetyninecochallenge.places;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.ninetyninecochallenge.places.dto.StoreDto;
import com.ninetyninecochallenge.places.helpers.MyLocation;

public class MainActivity extends BaseActivity {
	AlertDialog dialog;
	GoogleMap myMap;
	ValueAnimator vAnimator = new ValueAnimator();
	CameraPosition cameraPosition;
	SupportMapFragment supportMap;
	Circle circlemarker;
	MyLocation myLocation;
	String apiKey = "AIzaSyCkkNyh0sCgVyXMwvrsg4Gb1w7CxlH1RUg";
	String nextPageURL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?pagetoken=";
	String apiURL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=";
	String query = "&rankby=distance&types=food&key=";
	String nextPageQuery = "&key=";
	ArrayList<StoreDto> storeList = new ArrayList<StoreDto>();
	HashMap<String, StoreDto> storeMap = new HashMap<String, StoreDto>();
	String nextPageToken = "";
	String TAG_NEXTPAGE = "next_page_token";
	String TAG_RESULTS = "results";
	String TAG_GEOMETRY = "geometry";
	String TAG_LOCATION = "location";
	String TAG_NAME = "name";
	String TAG_PLACE_ID = "place_id";
	String TAG_LAT = "lat";
	String TAG_LNG = "lng";
	String TAG_ICON = "icon";
	RelativeLayout moreResultsBtn;
	int currentStorelistIndex = 0;

	public MainActivity() {
		super(R.string.favorites);

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.content_frame);

		moreResultsBtn = (RelativeLayout) findViewById(R.id.moreResultsBtn);
		moreResultsBtn.setVisibility(View.GONE);
		supportMap = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map23));

		myMap = supportMap.getMap();
		myMap.getUiSettings().setZoomControlsEnabled(false);
		myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

		showCurrentLocation();
	}

	public void showCurrentLocation() {
		Toast.makeText(this, "Retrieving current location....",
				Toast.LENGTH_LONG).show();
		myLocation = new MyLocation(MainActivity.this);
		if (myLocation.canGetLocation() == true) {
			myLocation.getLocation();
		} else {
			myLocation.showSettingsAlert();
		}
	}

	public void plotCurrentLocation(boolean hasLocation, double lat,
			double longhi) {
		if (hasLocation == true) {
			// this.lat = lat;
			// this.longhi = longhi;

			cameraPosition = new CameraPosition(new LatLng(lat, longhi), 15, 0,
					0);
			CameraUpdate cameraUpdate = CameraUpdateFactory
					.newCameraPosition(cameraPosition);
			myMap.animateCamera(cameraUpdate);

			myMap.addMarker(new MarkerOptions()
					.position(new LatLng(lat, longhi))
					.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_RED))
					.title("My Current Location"));

			// circlemarker = myMap.addCircle(new CircleOptions()
			// .strokeColor(Color.CYAN).radius(100)
			// .center(new LatLng(lat, longhi)));

			// vAnimator.setRepeatCount(ValueAnimator.INFINITE);
			// vAnimator.setRepeatMode(ValueAnimator.RESTART); /* PULSE */
			// vAnimator.setIntValues(0, 100);
			// vAnimator.setDuration(1300);
			// vAnimator.setEvaluator(new IntEvaluator());
			// vAnimator.setInterpolator(new
			// AccelerateDecelerateInterpolator());
			// vAnimator
			// .addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			// @Override
			// public void onAnimationUpdate(
			// ValueAnimator valueAnimator) {
			// float animatedFraction = valueAnimator
			// .getAnimatedFraction();
			// // Log.e("", "" + animatedFraction);
			//
			// circlemarker.setRadius(animatedFraction * 100);
			// // circlemarker.setStrokeWidth(500);
			// // circlemarker.setStrokeWidth(animatedFraction *
			// // 100);
			//
			// }
			// });
			// vAnimator.start();

			myMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

				@Override
				public void onInfoWindowClick(Marker arg0) {
					// TODO Auto-generated method stub

					System.out.println(arg0.getId() + " <--- MarkerID");

					if (!arg0.getTitle()
							.equalsIgnoreCase("My Current Location")) {
						if (!arg0.getId().isEmpty()) {
							Intent intent = new Intent(MainActivity.this,
									StoreDetails.class);
							intent.putExtra("place_id",
									storeMap.get(arg0.getId()).getPlace_id());
							intent.putExtra("storename",
									storeMap.get(arg0.getId()).getName());
							startActivity(intent);
							overridePendingTransition(R.anim.slide_in_up,
									R.anim.slide_in_up_exit);

						} else {
							Toast.makeText(getApplicationContext(),
									"No MarkerID", Toast.LENGTH_LONG).show();
						}
					}

				}
			});

			new getNearByShops(lat + "," + longhi).execute();

		} else {
			Toast.makeText(this, "Cannot retrieve current location",
					Toast.LENGTH_LONG).show();
		}

	}

	public void showNearYou(View view) {

		Intent intent = new Intent(MainActivity.this, StoreDetails.class);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_up_exit);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		// int id = item.getItemId();
		// if (id == R.id.action_settings) {
		// return true;
		// }

		// onBackPressed();
		return super.onOptionsItemSelected(item);
	}

	private class getNearByShops extends AsyncTask<String, Void, String> {

		JSONObject jsonObjectParent;
		JSONParser jParser = new JSONParser();
		JSONArray jsonObjectResults;
		String location;

		public getNearByShops(String location) {
			this.location = location;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			if (nextPageToken.isEmpty()) {
				Toast.makeText(MainActivity.this,
						"Retrieving nearest shops....", Toast.LENGTH_LONG)
						.show();
			} else {
				Toast.makeText(MainActivity.this, "Retrieving more shops....",
						Toast.LENGTH_LONG).show();
			}

			System.out.println("Started the asynctask");
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... placesURL) {
			// Simulates a background job.

			jsonObjectParent = new JSONObject();

			// Get 15 new listitems
			// if (jParser.checkServer(url)) {
			try {
				// Getting Array of Contacts

				// try {
				if (nextPageToken.isEmpty()) {
					jsonObjectParent = jParser.getJSONFromUrl2(apiURL
							+ location + query + apiKey);
				} else {
					jsonObjectParent = jParser.getJSONFromUrl2(nextPageURL
							+ nextPageToken + nextPageQuery + apiKey);
				}

				if (jParser.getResponseCode() == 200) {
					try {
						nextPageToken = jsonObjectParent
								.getString(TAG_NEXTPAGE);
					} catch (Exception e) {
						nextPageToken = "";
					}

					jsonObjectResults = jsonObjectParent
							.getJSONArray(TAG_RESULTS);

					for (int i = 0; i < jsonObjectResults.length(); i++) {
						JSONObject currentObject = jsonObjectResults
								.getJSONObject(i);
						StoreDto storeDto = new StoreDto();
						storeDto.setPlace_id(currentObject.getString(TAG_PLACE_ID));
						storeDto.setName(currentObject.getString(TAG_NAME));
						storeDto.setIcon(currentObject.getString(TAG_ICON));
						storeDto.setLat(currentObject
								.getJSONObject(TAG_GEOMETRY)
								.getJSONObject(TAG_LOCATION).getDouble(TAG_LAT));
						storeDto.setLonghi(currentObject
								.getJSONObject(TAG_GEOMETRY)
								.getJSONObject(TAG_LOCATION).getDouble(TAG_LNG));
						storeList.add(storeDto);
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
			Toast.makeText(MainActivity.this, "Plotting shops on map....",
					Toast.LENGTH_LONG).show();
			if (nextPageToken.isEmpty()) {
				moreResultsBtn.setVisibility(View.GONE);

			} else {
				moreResultsBtn.setVisibility(View.VISIBLE);
			}

			plotAllStores();
			super.onPostExecute(result);
		}
	}

	public void plotAllStores() {

		// for (StoreDto storeDto : storeList) {
		// new addMarkerAsync(storeDto).execute();
		// }

		System.out.print(currentStorelistIndex
				+ " <<<<<<<<< currentStorelistIndex");
		int y = currentStorelistIndex;
		for (int x = y; x < storeList.size(); x++) {
			new addMarkerAsync(storeList.get(x)).execute();
			currentStorelistIndex++;
		}
		currentStorelistIndex++;
	}

	class addMarkerAsync extends AsyncTask<Void, Void, Void> {
		Bitmap bmp;
		StoreDto storeDto;

		public addMarkerAsync(StoreDto storeDto) {
			this.storeDto = storeDto;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

		}

		@Override
		protected Void doInBackground(Void... params) {
			URL url;
			try {
				url = new URL(storeDto.getIcon());
				bmp = BitmapFactory.decodeStream(url.openConnection()
						.getInputStream());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			super.onPostExecute(result);

			String markerID = myMap.addMarker(
					new MarkerOptions()
							.position(
									new LatLng(storeDto.getLat(), storeDto
											.getLonghi()))
							.icon(BitmapDescriptorFactory.fromBitmap(bmp))
							.title(storeDto.getName())
							.snippet(
									storeDto.getLat() + ","
											+ storeDto.getLonghi())).getId();
			System.out.println(markerID + " <===MarkerID");
			storeMap.put(markerID, storeDto);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		getSlidingMenu().setMode(SlidingMenu.LEFT);
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);

		getSlidingMenu().setShadowDrawable(R.drawable.shadow);

		super.onResume();
	}

	public void reCalculateLocations(View view) {
		myMap.clear();
		nextPageToken = "";
		storeList = new ArrayList<StoreDto>();
		storeMap = new HashMap<String, StoreDto>();
		currentStorelistIndex = 0;
		showCurrentLocation();
	}

	public void showMoreResults(View view) {
		new getNearByShops("").execute();
	}
}
