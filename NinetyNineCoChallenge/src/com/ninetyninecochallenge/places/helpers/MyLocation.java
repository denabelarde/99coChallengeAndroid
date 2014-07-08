package com.ninetyninecochallenge.places.helpers;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.ninetyninecochallenge.places.MainActivity;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;

public class MyLocation {
	Timer timer1;
	LocationManager lm;

	boolean gps_enabled = false;
	boolean network_enabled = false;
	Context mContext;

	// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
	// java.util.Locale.getDefault());

	public MyLocation(Context mContext) {
		this.mContext = mContext;
	}

	public void getLocation() {
		// I use LocationResult callback class to pass location value from
		// MyLocation to user code.

		if (lm == null)
			lm = (LocationManager) mContext
					.getSystemService(Context.LOCATION_SERVICE);

		// exceptions will be thrown if provider is not permitted.
		try {
			gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (Exception ex) {
		}
		try {
			network_enabled = lm
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (Exception ex) {
		}

		// don't start listeners if no provider is enabled
		if (!gps_enabled && !network_enabled) {
			System.out.println("Has No GPS provider!");

			updateAuditLogs(null);
		} else {
			if (gps_enabled) {

				lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
						locationListenerGps);
			}
			if (network_enabled) {

				lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
						0, locationListenerNetwork);
			}
			// new RegisterLocations().execute();
			timer1 = new Timer();
			timer1.schedule(new GetLastLocation(), 20000);
		}

	}

	class RegisterLocations extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			System.out.println("Asynctask started");
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub

			System.out.println("Has GPS provider!");
			System.out.println("Fetching gps locations");

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			System.out.println("Asynctask Ended");
			super.onPostExecute(result);
		}
	}

	LocationListener locationListenerGps = new LocationListener() {
		public void onLocationChanged(Location location) {
			timer1.cancel();

			updateAuditLogs(location);
			lm.removeUpdates(this);
			lm.removeUpdates(locationListenerNetwork);
		}

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};

	LocationListener locationListenerNetwork = new LocationListener() {
		public void onLocationChanged(Location location) {
			timer1.cancel();

			updateAuditLogs(location);
			lm.removeUpdates(this);
			lm.removeUpdates(locationListenerGps);
		}

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};

	class GetLastLocation extends TimerTask {
		@Override
		public void run() {
			lm.removeUpdates(locationListenerGps);
			lm.removeUpdates(locationListenerNetwork);

			Location net_loc = null, gps_loc = null;
			if (gps_enabled)
				gps_loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (network_enabled)
				net_loc = lm
						.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

			// if there are both values use the latest one
			if (gps_loc != null && net_loc != null) {
				if (gps_loc.getTime() > net_loc.getTime())

					updateAuditLogs(gps_loc);
				else

					updateAuditLogs(net_loc);
				return;
			}

			if (gps_loc != null) {

				updateAuditLogs(gps_loc);
				return;
			}

			if (net_loc != null) {

				updateAuditLogs(net_loc);
				return;

			}

			updateAuditLogs(null);
		}
	}

	public void updateAuditLogs(Location loc) {
		String location = "";
		if (loc != null) {
			location = loc.getLatitude() + "," + loc.getLongitude();
			((MainActivity) mContext).plotCurrentLocation(true,
					loc.getLatitude(), loc.getLongitude());
		} else {
			((MainActivity) mContext).plotCurrentLocation(false, 0, 0);
		}

		System.out
				.println(location
						+ " <----USER LOCATION NYA!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!$$$$");

	}

	public void showSettingsAlert() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

		// Setting Dialog Title
		alertDialog.setTitle("GPS Unavailable!");

		// Setting Dialog Message
		alertDialog
				.setMessage("GPS is not enabled. Please enable all providers.");

		// On pressing Settings button
		alertDialog.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(
								Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						mContext.startActivity(intent);
					}
				});

		// on pressing cancel button
		// alertDialog.setNegativeButton("Cancel",
		// new DialogInterface.OnClickListener() {
		// public void onClick(DialogInterface dialog, int which) {
		// dialog.cancel();
		// }
		// });
		// Showing Alert Message
		alertDialog.show();
	}

	public boolean canGetLocation() {
		boolean result = true;

		if (lm == null)
			lm = (LocationManager) mContext
					.getSystemService(Context.LOCATION_SERVICE);

		// exceptions will be thrown if provider is not permitted.
		try {
			gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (Exception ex) {

		}
		try {
			network_enabled = lm
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (Exception ex) {
		}
		if (gps_enabled == false || network_enabled == false) {
			result = false;
		} else {
			result = true;
		}

		return result;
	}
}