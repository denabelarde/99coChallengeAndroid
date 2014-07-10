package com.ninetyninecochallenge.places;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.ninetyninecochallenge.places.dto.StoreDto;
import com.ninetyninecochallenge.places.helpers.AlertDialogHelper;
import com.ninetyninecochallenge.places.helpers.DirectionsJSONParser;
import com.ninetyninecochallenge.places.helpers.MyLocation;

public class MainActivity extends BaseActivity {
	AlertDialog dialog;
	GoogleMap myMap;
	ValueAnimator vAnimator = new ValueAnimator();
	CameraPosition cameraPosition;
	SupportMapFragment supportMap;
	Circle circlemarker;
	MyLocation myLocation;
	// String apiKey;
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
	AlertDialogHelper alertDialogHelper;
	LatLng userLocation;
	private ProgressDialog progressDialog;
	boolean stillPloting = false;
	Dialog selectTravelMode;

	public MainActivity() {
		super(R.string.favorites);

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.content_frame);
		getActionBar().setIcon(R.drawable.ic_action_favorite_dark);
		moreResultsBtn = (RelativeLayout) findViewById(R.id.moreResultsBtn);
		moreResultsBtn.setVisibility(View.GONE);
		supportMap = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map23));

		myMap = supportMap.getMap();
		myMap.getUiSettings().setZoomControlsEnabled(false);
		myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		alertDialogHelper = new AlertDialogHelper();
		showCurrentLocation();
	}

	public void showCurrentLocation() {

		if (checkInternetConnection() == true) {
			// Toast.makeText(this, "Retrieving current location....",
			// Toast.LENGTH_LONG).show();
			myLocation = new MyLocation(MainActivity.this);
			if (myLocation.canGetLocation() == true) {
				myMap.clear();
				nextPageToken = "";
				storeList = new ArrayList<StoreDto>();
				storeMap = new HashMap<String, StoreDto>();
				currentStorelistIndex = 0;
				progressDialog = ProgressDialog.show(
						MainActivity.this,
						getResources().getString(R.string.please_wait),
						getResources().getString(
								R.string.retrieving_current_location));

				progressDialog.setCancelable(false);
				myLocation.getLocation();

			} else {
				myLocation.showSettingsAlert();
			}
		} else {
			alertDialogHelper.alertMessage(
					getResources().getString(R.string.nointernet_string),
					getResources().getString(R.string.nointernet_description),
					MainActivity.this);
		}

	}

	public void plotCurrentLocation(boolean hasLocation, double lat,
			double longhi) {
		if (hasLocation == true) {
			// this.lat = lat;
			// this.longhi = longhi;
			userLocation = new LatLng(lat, longhi);
			cameraPosition = new CameraPosition(new LatLng(lat, longhi), 15, 0,
					0);
			CameraUpdate cameraUpdate = CameraUpdateFactory
					.newCameraPosition(cameraPosition);
			myMap.animateCamera(cameraUpdate);

			myMap.addMarker(new MarkerOptions()
					.position(new LatLng(lat, longhi))
					.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_RED))
					.title(getResources()
							.getString(R.string.mycurrent_location)));

			circlemarker = myMap.addCircle(new CircleOptions()
					.strokeColor(Color.CYAN).radius(500)
					.center(new LatLng(lat, longhi)));

			vAnimator.setRepeatCount(ValueAnimator.INFINITE);
			vAnimator.setRepeatMode(ValueAnimator.RESTART); /* PULSE */
			vAnimator.setIntValues(0, 600);
			vAnimator.setDuration(1300);
			vAnimator.setEvaluator(new IntEvaluator());
			vAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
			vAnimator
					.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
						@Override
						public void onAnimationUpdate(
								ValueAnimator valueAnimator) {
							float animatedFraction = valueAnimator
									.getAnimatedFraction();
							// Log.e("", "" + animatedFraction);

							circlemarker.setRadius(animatedFraction * 500);
							// circlemarker.setStrokeWidth(500);
							// circlemarker.setStrokeWidth(animatedFraction *
							// 100);

						}
					});
			vAnimator.start();

			myMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

				@Override
				public void onInfoWindowClick(Marker arg0) {
					// TODO Auto-generated method stub

					System.out.println(arg0.getId() + " <--- MarkerID");

					if (!arg0.getTitle().equalsIgnoreCase(
							getResources().getString(
									R.string.mycurrent_location))) {
						if (!arg0.getId().isEmpty()) {
							Intent intent = new Intent(MainActivity.this,
									StoreDetails.class);
							intent.putExtra("place_id",
									storeMap.get(arg0.getId()).getPlace_id());
							intent.putExtra("storename",
									storeMap.get(arg0.getId()).getName());
							intent.putExtra("latitude",
									storeMap.get(arg0.getId()).getLat());
							intent.putExtra("longhitude",
									storeMap.get(arg0.getId()).getLonghi());
							intent.putExtra("icon", storeMap.get(arg0.getId())
									.getIcon());
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
		if (nextPageToken.isEmpty()) {
			menu.getItem(1).setVisible(false);
		} else {
			menu.getItem(1).setVisible(true);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (id == R.id.refresh_location) {

			showCurrentLocation();

			return true;
		} else if (id == R.id.load_more_results) {

			if (checkInternetConnection() == true) {
				new getNearByShops("").execute();
			} else {
				alertDialogHelper.alertMessage(
						getResources().getString(R.string.nointernet_string),
						getResources().getString(
								R.string.nointernet_description),
						MainActivity.this);
			}

			return true;
		}

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

				progressDialog.setMessage(getResources().getString(
						R.string.retrieving_nearest_stores));
			} else {

				progressDialog = ProgressDialog.show(
						MainActivity.this,
						getResources().getString(R.string.please_wait),
						getResources().getString(
								R.string.retrieving_more_stores));

				progressDialog.setCancelable(false);
			}

			System.out.println("Started the asynctask");
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... placesURL) {
			// Simulates a background job.

			jsonObjectParent = new JSONObject();

			try {

				if (nextPageToken.isEmpty()) {
					jsonObjectParent = jParser.getJSONFromUrl2(apiURL
							+ location + query
							+ getResources().getString(R.string.apikey));
				} else {
					jsonObjectParent = jParser.getJSONFromUrl2(nextPageURL
							+ nextPageToken + nextPageQuery
							+ getResources().getString(R.string.apikey));
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
						storeDto.setPlace_id(currentObject
								.getString(TAG_PLACE_ID));
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

			} catch (Exception e) {
				e.printStackTrace();
			}

			return "";
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			// Toast.makeText(MainActivity.this, "Plotting shops on map....",
			// Toast.LENGTH_LONG).show();

			if (jParser.getResponseCode() == 200) {
				progressDialog.setMessage(getResources().getString(
						R.string.plottingstores_on_map));
				if (nextPageToken.isEmpty()) {
					moreResultsBtn.setVisibility(View.GONE);

				} else {
					moreResultsBtn.setVisibility(View.VISIBLE);

				}
				invalidateOptionsMenu();
				plotAllStores();
			} else {
				progressDialog.dismiss();
				alertDialogHelper.alertMessage(
						getResources().getString(R.string.error),
						getResources().getString(
								R.string.error_retrieving_stores),
						MainActivity.this);
			}

			super.onPostExecute(result);
		}
	}

	public void plotAllStores() {

		System.out.print(currentStorelistIndex
				+ " <<<<<<<<< currentStorelistIndex");

		new addMultipleStoreMarkersAsync().execute();
	}

	class addMultipleStoreMarkersAsync extends AsyncTask<Void, Void, Void> {
		Bitmap bmp;
		ArrayList<Bitmap> iconBitmapList;

		public addMultipleStoreMarkersAsync() {

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

		}

		@Override
		protected Void doInBackground(Void... params) {
			iconBitmapList = new ArrayList<Bitmap>();

			int y = currentStorelistIndex;
			for (int x = y; x < storeList.size(); x++) {
				// new addMarkerAsync(storeList.get(x)).execute();

				URL url;
				try {
					url = new URL(storeList.get(x).getIcon());
					bmp = BitmapFactory.decodeStream(url.openConnection()
							.getInputStream());
					iconBitmapList.add(bmp);
				} catch (Exception e) {
					e.printStackTrace();
					iconBitmapList.add(null);
				}
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			super.onPostExecute(result);
			int y = currentStorelistIndex;
			int bitmapCounter = 0;
			for (int x = y; x < storeList.size(); x++) {
				// new addMarkerAsync(storeList.get(x)).execute();
				String markerID = "";
				if (iconBitmapList.get(bitmapCounter) != null) {
					markerID = myMap.addMarker(
							new MarkerOptions()
									.position(
											new LatLng(storeList.get(x)
													.getLat(), storeList.get(x)
													.getLonghi()))
									.icon(BitmapDescriptorFactory
											.fromBitmap(bmp))
									.title(storeList.get(x).getName())
									.snippet(
											storeList.get(x).getLat()
													+ ","
													+ storeList.get(x)
															.getLonghi()))
							.getId();

				} else {
					markerID = myMap
							.addMarker(
									new MarkerOptions()
											.position(
													new LatLng(
															storeList.get(x)
																	.getLat(),
															storeList
																	.get(x)
																	.getLonghi()))
											.icon(BitmapDescriptorFactory
													.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
											.title(storeList.get(x).getName())
											.snippet(
													storeList.get(x).getLat()
															+ ","
															+ storeList
																	.get(x)
																	.getLonghi()))
							.getId();

				}
				System.out.println(markerID + " <===MarkerID");
				if (!markerID.isEmpty()) {
					storeMap.put(markerID, storeList.get(x));
				}
				bitmapCounter++;
				currentStorelistIndex++;
			}
			currentStorelistIndex++;
			progressDialog.dismiss();
		}
	}

	class addSingleStoreMarker extends AsyncTask<Void, Void, Void> {
		Bitmap bmp;
		StoreDto storeDto;

		public addSingleStoreMarker(StoreDto storeDto) {
			this.storeDto = storeDto;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			progressDialog = ProgressDialog.show(
					MainActivity.this,
					getResources().getString(R.string.please_wait),
					getResources().getString(
							R.string.creating_markers_waypoints));

			progressDialog.setCancelable(false);
			super.onPreExecute();

		}

		@Override
		protected Void doInBackground(Void... params) {

			// new addMarkerAsync(storeList.get(x)).execute();

			URL url;
			try {
				url = new URL(storeDto.getIcon());
				bmp = BitmapFactory.decodeStream(url.openConnection()
						.getInputStream());

			} catch (Exception e) {
				e.printStackTrace();
				bmp = null;
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			super.onPostExecute(result);
			String markerID = "";
			if (bmp != null) {
				markerID = myMap.addMarker(
						new MarkerOptions()
								.position(
										new LatLng(storeDto.getLat(), storeDto
												.getLonghi()))
								.icon(BitmapDescriptorFactory.fromBitmap(bmp))
								.title(storeDto.getName())
								.snippet(
										storeDto.getLat() + ","
												+ storeDto.getLonghi()))
						.getId();

			} else {

				markerID = myMap
						.addMarker(
								new MarkerOptions()
										.position(
												new LatLng(storeDto.getLat(),
														storeDto.getLonghi()))
										.icon(BitmapDescriptorFactory
												.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
										.title(storeDto.getName())
										.snippet(
												storeDto.getLat() + ","
														+ storeDto.getLonghi()))
						.getId();
			}

			System.out.println(markerID + " <===MarkerID");
			if (!markerID.isEmpty()) {
				storeMap.put(markerID, storeDto);
			}

			progressDialog.dismiss();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		getSlidingMenu().setMode(SlidingMenu.LEFT);
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);

		getSlidingMenu().setShadowDrawable(R.drawable.shadow);

		getSlidingMenu().setSecondaryMenu(R.layout.menu_frame_two);
		getSlidingMenu().setSecondaryShadowDrawable(R.drawable.shadowright);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame_two, new FavoriteListFragment())
				.commit();
		super.onResume();
	}

	public void reCalculateLocations(View view) {

		showCurrentLocation();

	}

	public void showMoreResults(View view) {
		if (checkInternetConnection() == true) {
			new getNearByShops("").execute();
		} else {
			alertDialogHelper.alertMessage(
					getResources().getString(R.string.nointernet_string),
					getResources().getString(R.string.nointernet_description),
					MainActivity.this);
		}

	}

	public boolean checkInternetConnection() {
		final ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
		// should check null because in air plan mode it will be null
		if (netInfo != null && netInfo.isConnected()) {
			return true;
		} else {
			return false;
		}

	}

	// WAYPOINT CREATION METHODS OR CODES
	// public void plotUserLocationAndFavoriteStore(LatLng dest, StoreDto
	// storeDto) {
	// if (checkInternetConnection() == true) {
	//
	// } else {
	// alertDialogHelper.alertMessage(
	// getResources().getString(R.string.nointernet_string),
	// getResources().getString(R.string.nointernet_description),
	// MainActivity.this);
	// }
	//
	// }

	public void plotUserLocationAndFavoriteStore(final LatLng dest,
			final StoreDto storeDto) {

		if (checkInternetConnection() == true) {
			LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
			final ArrayList<String> travelModeList = new ArrayList<String>();
			travelModeList.add(getResources().getString(R.string.walking));
			travelModeList.add(getResources().getString(R.string.driving));
//			travelModeList.add(getResources().getString(R.string.bicycling));
//			travelModeList.add(getResources().getString(R.string.transit));
			ArrayAdapter<String> listadapter = new ArrayAdapter<String>(this,
					R.layout.list_item, travelModeList);
			View customView = inflater.inflate(R.layout.waypointmode_dialog,
					null);

			ListView modeList = (ListView) customView
					.findViewById(R.id.numberlist);
			modeList.setAdapter(listadapter);
			modeList.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> myAdapter, View myView,
						int myItemInt, long mylng) {
					myMap.clear();
					nextPageToken = "";
					storeList = new ArrayList<StoreDto>();
					storeMap = new HashMap<String, StoreDto>();
					currentStorelistIndex = 0;
					getSlidingMenu().toggle();
					moreResultsBtn.setVisibility(View.GONE);

					storeList.add(storeDto);
					cameraPosition = new CameraPosition(new LatLng(
							userLocation.latitude, userLocation.longitude), 15,
							0, 0);
					CameraUpdate cameraUpdate = CameraUpdateFactory
							.newCameraPosition(cameraPosition);
					myMap.animateCamera(cameraUpdate);

					myMap.addMarker(new MarkerOptions()
							.position(
									new LatLng(userLocation.latitude,
											userLocation.longitude))
							.icon(BitmapDescriptorFactory
									.defaultMarker(BitmapDescriptorFactory.HUE_RED))
							.title("My Current Location"));

					new addSingleStoreMarker(storeDto).execute();

					// Getting URL to the Google Directions API
					String url = getDirectionsUrl(userLocation, dest,
							travelModeList.get(myItemInt).toLowerCase());

					DownloadTask downloadTask = new DownloadTask();

					// Start downloading json data from Google Directions API
					downloadTask.execute(url);
					selectTravelMode.dismiss();
				}
			});

			// Build the dialog
			selectTravelMode = new Dialog(this);
			// selectModelDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			selectTravelMode.setContentView(customView);
			selectTravelMode.getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
			selectTravelMode.setTitle("SELECT MODE OF TRAVEL");

			selectTravelMode.show();
		} else {
			alertDialogHelper.alertMessage(
					getResources().getString(R.string.nointernet_string),
					getResources().getString(R.string.nointernet_description),
					MainActivity.this);
		}

	}

	// Fetches data from url passed
	private class DownloadTask extends AsyncTask<String, Void, String> {

		// Downloading data in non-ui thread
		@Override
		protected String doInBackground(String... url) {

			// For storing data from web service
			String data = "";

			try {
				// Fetching the data from web service
				data = downloadUrl(url[0]);
			} catch (Exception e) {
				Log.d("Background Task", e.toString());
			}
			return data;
		}

		// Executes in UI thread, after the execution of
		// doInBackground()
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			ParserTask parserTask = new ParserTask();

			// Invokes the thread for parsing the JSON data
			parserTask.execute(result);

		}
	}

	/** A method to download json data from url */
	private String downloadUrl(String strUrl) throws IOException {
		String data = "";
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(strUrl);

			// Creating an http connection to communicate with url
			urlConnection = (HttpURLConnection) url.openConnection();

			// Connecting to url
			urlConnection.connect();

			// Reading data from url
			iStream = urlConnection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					iStream));

			StringBuffer sb = new StringBuffer();

			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			data = sb.toString();

			br.close();

		} catch (Exception e) {
			Log.d("Exception while downloading url", e.toString());
		} finally {
			iStream.close();
			urlConnection.disconnect();
		}
		return data;
	}

	/** A class to parse the Google Places in JSON format */
	private class ParserTask extends
			AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

		// Parsing the data in non-ui thread
		@Override
		protected List<List<HashMap<String, String>>> doInBackground(
				String... jsonData) {

			JSONObject jObject;
			List<List<HashMap<String, String>>> routes = null;

			try {
				jObject = new JSONObject(jsonData[0]);
				DirectionsJSONParser parser = new DirectionsJSONParser();

				// Starts parsing data
				routes = parser.parse(jObject);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return routes;
		}

		// Executes in UI thread, after the parsing process
		@Override
		protected void onPostExecute(List<List<HashMap<String, String>>> result) {

			try {
				ArrayList<LatLng> points = null;
				PolylineOptions lineOptions = null;

				// Traversing through all the routes
				for (int i = 0; i < result.size(); i++) {
					points = new ArrayList<LatLng>();
					lineOptions = new PolylineOptions();

					// Fetching i-th route
					List<HashMap<String, String>> path = result.get(i);

					// Fetching all the points in i-th route
					for (int j = 0; j < path.size(); j++) {
						HashMap<String, String> point = path.get(j);

						double lat = Double.parseDouble(point.get("lat"));
						double lng = Double.parseDouble(point.get("lng"));
						LatLng position = new LatLng(lat, lng);

						points.add(position);
					}

					// Adding all the points in the route to LineOptions
					lineOptions.addAll(points);
					lineOptions.width(2);
					lineOptions.color(Color.RED);
				}

				// Drawing polyline in the Google Map for the i-th route
				myMap.addPolyline(lineOptions);
			} catch (Exception e) {
				alertDialogHelper.alertMessage(
						getResources().getString(R.string.error),
						getResources().getString(R.string.waypoint_error),
						MainActivity.this);
			}

			progressDialog.dismiss();
		}
	}

	private String getDirectionsUrl(LatLng origin, LatLng dest,
			String travelmode) {

		// Origin of route
		String str_origin = "origin=" + origin.latitude + ","
				+ origin.longitude;

		// Destination of route
		String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

		// Sensor enabled
		String sensor = "sensor=false";

		// Waypoints
		String waypoints = "";
		// for (int i = 2; i < markerPoints.size(); i++) {
		// LatLng point = (LatLng) markerPoints.get(i);
		// if (i == 2)
		waypoints = "waypoints=";
		waypoints += origin.latitude + "," + origin.longitude + "|";
		// }
		waypoints += dest.latitude + "," + dest.longitude + "|";
		// Building the parameters to the web service
		String parameters = str_origin + "&" + str_dest + "&" + sensor + "&"
				+ waypoints;

		// Output format
		String output = "json";
		String modeOfTravel = "&mode=";
		// Building the url to the web service
		String url = "https://maps.googleapis.com/maps/api/directions/"
				+ output + "?" + parameters + modeOfTravel + travelmode;

		return url;
	}

}
