package com.ninetyninecochallenge.places;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class StoreDetails extends Activity {
	String place_id;
	String storename;
	String placeDetailsAPI = "https://maps.googleapis.com/maps/api/place/details/json?placeid=";

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
			setTitle(storename);
			((TextView) findViewById(R.id.placeid)).setText(place_id+" <---- PLACEID");
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.near_you, menu);
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
}
