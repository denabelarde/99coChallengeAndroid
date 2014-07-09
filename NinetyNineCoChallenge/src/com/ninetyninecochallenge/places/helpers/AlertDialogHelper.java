package com.ninetyninecochallenge.places.helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;

import com.ninetyninecochallenge.places.R;

public class AlertDialogHelper {

	public void alertMessage(String title, String message, Context context) {
		AlertDialog.Builder adb = new AlertDialog.Builder(context);
		adb.setTitle(title);
		adb.setMessage(message);
		adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Write your code here to execute after dialog
				// closed
				// Toast.makeText(getApplicationContext(),
				// "Login",
				// Toast.LENGTH_SHORT).show();
			}
		});

		adb.show();
	}

	public String getServerResponse(String message) {
		String result = "";
		if (message.contains("401")) {
			result = "Unauthorized User";
		} else if (message.contains("404")) {
			result = "Requested URL not found";
		} else if (message.contains("500")) {
			result = "Server Error";
		} else if (message.contains("204")) {
			result = "No Content";
		} else if (message.contains("301")) {
			result = "The URL has been moved permanently";
		} else {
			result = "Unknown Error Occured!";
		}
		return result;
	}

	public void alertNoticeMessage(String title, String message, Context context) {
		AlertDialog.Builder adb = new AlertDialog.Builder(context);
		adb.setTitle(title);
		adb.setIcon(R.drawable.warning);
		adb.setMessage(message);
		adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Write your code here to execute after dialog
				// closed
				// Toast.makeText(getApplicationContext(),
				// "Login",
				// Toast.LENGTH_SHORT).show();
			}
		});

		adb.show();
	}

	public void getResultStatusMessage(int resultStatus, Context context) {
		if (resultStatus == 2) {
			AlertDialog.Builder adb = new AlertDialog.Builder(context);
			adb.setTitle("ERROR!");
			adb.setMessage("INCORRECT PASSWORD!");
			adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// Write your code here to execute after dialog
					// closed
					// Toast.makeText(getApplicationContext(),
					// "Login",
					// Toast.LENGTH_SHORT).show();
				}
			});

			adb.show();

		} else if (resultStatus == 3) {
			AlertDialog.Builder adb = new AlertDialog.Builder(context);
			adb.setTitle("ERROR!");
			adb.setMessage("NO DEVICE ID!");
			adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// Write your code here to execute after dialog
					// closed
					// Toast.makeText(getApplicationContext(),
					// "Login",
					// Toast.LENGTH_SHORT).show();
				}
			});

			adb.show();

		} else if (resultStatus == 4) {
			AlertDialog.Builder adb = new AlertDialog.Builder(context);
			adb.setTitle("ERROR!");
			adb.setMessage("NO RECORD EXISTING!");
			adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// Write your code here to execute after dialog
					// closed
					// Toast.makeText(getApplicationContext(),
					// "Login",
					// Toast.LENGTH_SHORT).show();
				}
			});

			adb.show();
		} else if (resultStatus == 5) {
			AlertDialog.Builder adb = new AlertDialog.Builder(context);
			adb.setTitle("ERROR!");
			adb.setMessage("EMAIL EXISTING!");
			adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// Write your code here to execute after dialog
					// closed
					// Toast.makeText(getApplicationContext(),
					// "Login",
					// Toast.LENGTH_SHORT).show();
				}
			});

			adb.show();
		} else if (resultStatus == 6) {
			AlertDialog.Builder adb = new AlertDialog.Builder(context);
			adb.setTitle("ERROR!");
			adb.setMessage("REQUEST FAILED!");
			adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// Write your code here to execute after dialog
					// closed
					// Toast.makeText(getApplicationContext(),
					// "Login",
					// Toast.LENGTH_SHORT).show();
				}
			});

			adb.show();
		} else if (resultStatus == 7) {
			AlertDialog.Builder adb = new AlertDialog.Builder(context);
			adb.setTitle("ERROR!");
			adb.setMessage("USERNAME EXISTING!");
			adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// Write your code here to execute after dialog
					// closed
					// Toast.makeText(getApplicationContext(),
					// "Login",
					// Toast.LENGTH_SHORT).show();
				}
			});

			adb.show();
		} else if (resultStatus == 8) {
			AlertDialog.Builder adb = new AlertDialog.Builder(context);
			adb.setTitle("ERROR!");
			adb.setMessage("OLD PASSWORD INCORRECT!");
			adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// Write your code here to execute after dialog
					// closed
					// Toast.makeText(getApplicationContext(),
					// "Login",
					// Toast.LENGTH_SHORT).show();
				}
			});

			adb.show();
		} else if (resultStatus == 9) {
			AlertDialog.Builder adb = new AlertDialog.Builder(context);
			adb.setTitle("ERROR!");
			adb.setMessage("PASSWORD LENGTH MUST BE 8 CHARACTERS!");
			adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// Write your code here to execute after dialog
					// closed
					// Toast.makeText(getApplicationContext(),
					// "Login",
					// Toast.LENGTH_SHORT).show();
				}
			});

			adb.show();
		} else if (resultStatus == 11) {
			AlertDialog.Builder adb = new AlertDialog.Builder(context);
			adb.setTitle("ERROR!");
			adb.setMessage("WRONG CONFIRMATION CODE!");
			adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// Write your code here to execute after dialog
					// closed
					// Toast.makeText(getApplicationContext(),
					// "Login",
					// Toast.LENGTH_SHORT).show();
				}
			});

			adb.show();
		} else if (resultStatus == 12) {
			AlertDialog.Builder adb = new AlertDialog.Builder(context);
			adb.setTitle("ERROR!");
			adb.setMessage("Expired confirmation code! Please check your email for the new one!");
			adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// Write your code here to execute after dialog
					// closed
					// Toast.makeText(getApplicationContext(),
					// "Login",
					// Toast.LENGTH_SHORT).show();
				}
			});

			adb.show();
		} else if (resultStatus == 14) {
			AlertDialog.Builder adb = new AlertDialog.Builder(context);
			adb.setTitle("ERROR!");
			adb.setMessage("User is deactivated!");
			adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// Write your code here to execute after dialog
					// closed
					// Toast.makeText(getApplicationContext(),
					// "Login",
					// Toast.LENGTH_SHORT).show();
				}
			});

			adb.show();
		} else if (resultStatus == 16) {
			AlertDialog.Builder adb = new AlertDialog.Builder(context);
			adb.setTitle("ERROR!");
			adb.setMessage("New and Confirm Password does not match!");
			adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// Write your code here to execute after dialog
					// closed
					// Toast.makeText(getApplicationContext(),
					// "Login",
					// Toast.LENGTH_SHORT).show();
				}
			});

			adb.show();
		} else if (resultStatus == 17) {
			AlertDialog.Builder adb = new AlertDialog.Builder(context);
			adb.setTitle("ERROR!");
			adb.setMessage("Username must be at least 5 characters long!");
			adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// Write your code here to execute after dialog
					// closed
					// Toast.makeText(getApplicationContext(),
					// "Login",
					// Toast.LENGTH_SHORT).show();
				}
			});

			adb.show();
		} else if (resultStatus == 18) {
			AlertDialog.Builder adb = new AlertDialog.Builder(context);
			adb.setTitle("ERROR!");
			adb.setMessage("Username must not exceed 20 characters!");
			adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// Write your code here to execute after dialog
					// closed
					// Toast.makeText(getApplicationContext(),
					// "Login",
					// Toast.LENGTH_SHORT).show();
				}
			});

			adb.show();
		}
	}

	public boolean checkInternetConnection(Context context) {
		final ConnectivityManager conMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (conMgr.getActiveNetworkInfo() != null
				&& conMgr.getActiveNetworkInfo().isAvailable()
				&& conMgr.getActiveNetworkInfo().isConnected()) {
			return true;
		} else {
			return false;
		}
	}

}
