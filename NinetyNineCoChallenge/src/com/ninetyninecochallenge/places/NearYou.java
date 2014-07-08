package com.ninetyninecochallenge.places;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.view.Menu;

public class NearYou extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_near_you);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.near_you, menu);
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
		NearYou.this.overridePendingTransition(0, R.anim.slide_out_down);
		super.onBackPressed();
	}
}
