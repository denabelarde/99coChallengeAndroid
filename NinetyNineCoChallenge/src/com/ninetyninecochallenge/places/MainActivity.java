package com.ninetyninecochallenge.places;

import java.net.URL;
import java.util.ArrayList;

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
	String apiURL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=";
	String query = "&rankby=distance&types=food&key=";
	ArrayList<StoreDto> storeList = new ArrayList<StoreDto>();
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

	public MainActivity() {
		super(R.string.favorites);

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.content_frame);
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

			supportMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map23));

			myMap = supportMap.getMap();
			myMap.getUiSettings().setZoomControlsEnabled(false);
			myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

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

					if (arg0.getTitle().equalsIgnoreCase("marker1")) {
						Intent i = new Intent(MainActivity.this,
								FirstMarker.class);
						i.putExtra("markertitle", arg0.getTitle().toString());
						startActivity(i);
					} else {
						Toast.makeText(getApplicationContext(),
								"No Such Activity to called upon",
								Toast.LENGTH_LONG).show();
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

		Intent intent = new Intent(MainActivity.this, NearYou.class);
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
			Toast.makeText(MainActivity.this, "Retrieving nearest shops....",
					Toast.LENGTH_LONG).show();
			System.out.println("Started the asynctask");
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... placesURL) {
			// Simulates a background job.

			jsonObjectParent = new JSONObject();
			storeList = new ArrayList<StoreDto>();

			// Get 15 new listitems
			// if (jParser.checkServer(url)) {
			try {
				// Getting Array of Contacts

				// try {
				jsonObjectParent = jParser.getJSONFromUrl2(apiURL + location
						+ query + apiKey);

				if (jParser.getResponseCode() == 200) {

					nextPageToken = jsonObjectParent.getString(TAG_NEXTPAGE);
					jsonObjectResults = jsonObjectParent
							.getJSONArray(TAG_RESULTS);

					for (int i = 0; i < jsonObjectResults.length(); i++) {
						JSONObject currentObject = jsonObjectResults
								.getJSONObject(i);
						StoreDto storeDto = new StoreDto();
						storeDto.setPlace_id(TAG_PLACE_ID);
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
			plotAllStores();
			super.onPostExecute(result);
		}
	}

	public void plotAllStores() {

		for (StoreDto storeDto : storeList) {
			new addMarkerAsync(storeDto).execute();
		}

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

			myMap.addMarker(new MarkerOptions()
					.position(
							new LatLng(storeDto.getLat(), storeDto.getLonghi()))
					.icon(BitmapDescriptorFactory.fromBitmap(bmp))
					.title(storeDto.getName()));
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
		showCurrentLocation();
	}
}
