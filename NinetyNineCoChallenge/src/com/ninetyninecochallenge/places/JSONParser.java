package com.ninetyninecochallenge.places;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;

import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;

import android.util.Log;

public class JSONParser {

	static InputStream is = null;
	static JSONObject jObj = null;
	static String json = "";
	public AlertDialog alertDialog;
	HttpResponse httpResponse;
	List<NameValuePair> nameValuePairs;

	// constructor
	public JSONParser() {
		// alertDialog = new AlertDialog.Builder(null).create();
	}

	public void setNameValuePairs(List<NameValuePair> nameValuePairs) {
		this.nameValuePairs = nameValuePairs;
	}

	public JSONObject getJSONFromUrl(String url) {
		System.out.println(url);
		// Making HTTP request
		try {
			// defaultHttpClient

			HttpParams httpParameters = new BasicHttpParams();

			// Set the timeout in milliseconds until a connection is
			// established.
			// The default value is zero, that means the timeout is not used.

			int timeoutConnection = 10000;
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					timeoutConnection);

			// Set the default socket timeout (SO_TIMEOUT)
			// in milliseconds which is the timeout for waiting for data.

			int timeoutSocket = 30000;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

			// DefaultHttpClient httpClient = new
			// DefaultHttpClient(httpParameters);
			// HttpPost httpPost = new HttpPost(url);
			//
			// httpResponse = httpClient.execute(httpPost);
			// System.out.println(httpResponse.getStatusLine().getStatusCode()
			// + " <---Statuscode");
			//
			// HttpEntity httpEntity = httpResponse.getEntity();
			// is = httpEntity.getContent();

			HttpClient httpClient = new DefaultHttpClient();

			HttpPost httppost = new HttpPost(url);
			// HttpGet httppost = new HttpGet(url
			// + "username/denver/password/admin/deviceid/adfas");
			//
			// String auth = new String(Base64.encode(
			// ("masterCard" + ":" + "1234").getBytes(), Base64.URL_SAFE
			// | Base64.NO_WRAP));
			// httppost.addHeader("Authorization", "Basic " + auth);

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			httpResponse = httpClient.execute(httppost);

			is = httpResponse.getEntity().getContent();

			// System.out.println(httpEntity.getContent().toString());
			// System.out
			// .println(response.getEntity().getContentLength() + "awts");
			// System.out.println("Pumasok ang Inputstream");

		} catch (ConnectTimeoutException e) {

			// alertDialog.setTitle("ERROR!!");
			// alertDialog.setMessage("Connection timeout please try again!!");
			// alertDialog.setIcon(R.drawable.field_err_icon);
			// alertDialog.setButton("OK", new DialogInterface.OnClickListener()
			// {
			// public void onClick(DialogInterface dialog, int which) {
			// // Write your code here to execute after
			// // dialog closed
			// // Toast.makeText(getApplicationContext(),
			// // "Login",
			// // Toast.LENGTH_SHORT).show();
			// }
			// });
			// alertDialog.show();

			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			System.out.println(sb.toString() + " <---Json String");
			json = sb.toString();
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		// try parse the string to a JSON object
		try {
			jObj = new JSONObject(json);
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}

		// return JSON String
		return jObj;

	}

	public JSONObject getJSONFromUrl2(String url) {
		System.out.println(url);
		// Making HTTP request
		try {
			// defaultHttpClient

			HttpParams httpParameters = new BasicHttpParams();

			// Set the timeout in milliseconds until a connection is
			// established.
			// The default value is zero, that means the timeout is not used.

			int timeoutConnection = 10000;
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					timeoutConnection);

			// Set the default socket timeout (SO_TIMEOUT)
			// in milliseconds which is the timeout for waiting for data.

			int timeoutSocket = 30000;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

			// DefaultHttpClient httpClient = new
			// DefaultHttpClient(httpParameters);
			// HttpPost httpPost = new HttpPost(url);
			//
			// httpResponse = httpClient.execute(httpPost);
			// System.out.println(httpResponse.getStatusLine().getStatusCode()
			// + " <---Statuscode");
			//
			// HttpEntity httpEntity = httpResponse.getEntity();
			// is = httpEntity.getContent();

			HttpClient httpClient = new DefaultHttpClient();

			HttpGet httppost = new HttpGet(url);
			// HttpGet httppost = new HttpGet(url
			// + "username/denver/password/admin/deviceid/adfas");
			//
			// String auth = new String(Base64.encode(
			// ("masterCard" + ":" + "1234").getBytes(), Base64.URL_SAFE
			// | Base64.NO_WRAP));
			// httppost.addHeader("Authorization", "Basic " + auth);

			httpResponse = httpClient.execute(httppost);

			is = httpResponse.getEntity().getContent();

			// System.out.println(httpEntity.getContent().toString());
			// System.out
			// .println(response.getEntity().getContentLength() + "awts");
			// System.out.println("Pumasok ang Inputstream");

		} catch (ConnectTimeoutException e) {

			// alertDialog.setTitle("ERROR!!");
			// alertDialog.setMessage("Connection timeout please try again!!");
			// alertDialog.setIcon(R.drawable.field_err_icon);
			// alertDialog.setButton("OK", new DialogInterface.OnClickListener()
			// {
			// public void onClick(DialogInterface dialog, int which) {
			// // Write your code here to execute after
			// // dialog closed
			// // Toast.makeText(getApplicationContext(),
			// // "Login",
			// // Toast.LENGTH_SHORT).show();
			// }
			// });
			// alertDialog.show();

			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			System.out.println(sb.toString() + " <---Json String");
			json = sb.toString();
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		// try parse the string to a JSON object
		try {
			jObj = new JSONObject(json);
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}

		// return JSON String
		return jObj;

	}

	public Boolean checkServer(String url) {
		boolean isOK = false;
		try {
			URL url2 = new URL(url);
			HttpURLConnection urlcon = (HttpURLConnection) url2
					.openConnection();
			urlcon.connect();
			if (urlcon.getResponseCode() == 200) {
				// InputStream in = new BufferedInputStream(
				// urlcon.getInputStream());
				// String serverStatus = readStream(in); // assuming that
				// // "http://yourserverurl/yourstatusmethod"
				// // returns OK or ERROR
				// // depending on your
				// // server status check
				// isOK = (serverStatus.equalsIgnoreCase("OK"));
				isOK = true;
			} else {
				isOK = false;
			}

			urlcon.disconnect();

		} catch (MalformedURLException e1) {
			isOK = false;
			e1.printStackTrace();
		} catch (IOException e) {
			isOK = false;
			e.printStackTrace();
		}

		return isOK;
	}

	public static String readStream(InputStream in) throws IOException {
		StringBuffer out = new StringBuffer();
		byte[] b = new byte[4096];
		for (int n; (n = in.read(b)) != -1;) {
			out.append(new String(b, 0, n));
		}
		return out.toString();
	}

	public String getResponse() {
		String result = "Error retrieving data";
		// if (httpResponse.getStatusLine().getStatusCode() == 200) {
		// result = "Connection successful";
		// } else

		if (httpResponse != null) {
			if (httpResponse.getStatusLine().getStatusCode() == 404) {
				result = "Requested URL not found";
			} else if (httpResponse.getStatusLine().getStatusCode() == 500) {
				result = "Server Error";
			} else if (httpResponse.getStatusLine().getStatusCode() == 204) {
				result = "No Content";
			} else if (httpResponse.getStatusLine().getStatusCode() == 301) {
				result = "The URL has been moved permanently";
			} else if (httpResponse.getStatusLine().getStatusCode() == 401) {
				result = "Unauthorized User";
			}
		}

		return result;
	}

	public int getResponseCode() {
		int i = 0;
		System.out.println("Dumadaan sa GetresponseCOde");
		if (httpResponse != null) {
			i = httpResponse.getStatusLine().getStatusCode();
		}

		return i;
	}
}
