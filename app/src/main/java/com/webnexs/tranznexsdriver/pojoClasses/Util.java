package com.webnexs.tranznexsdriver.pojoClasses;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Address;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.webnexs.tranznexsdriver.CustomAlertDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.Locale;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Souvik Hazra on 03-02-2016.
 */
public class Util {
	//	public static final String serverLoc = "http://139.59.45.86/API/v1/";
	public static final String serverLoc = "http://webnexs.org:65526/driver/";
	public static final String mapsAPIKey = "AIzaSyArDrpO2VcEXtvkcc0r0GabkKUelC2mEMc";

	public static String execScript(Context ctx, String script, Object... params) throws Exception {
		String ret;
		OkHttpClient client = new OkHttpClient();
		Request.Builder req = new Request.Builder().url(serverLoc + script);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
		FormBody.Builder builder = new FormBody.Builder();
		if (params.length > 0) {
			for (int i = 0; i < params.length; i += 2) {
				builder.add(params[i].toString(), params[i + 1].toString());
			}
		}
		builder.add("user_id", prefs.getString("user_id", "-1"));
		req.addHeader("token", "48X7s3PzUUZC5DQDw6fNZR5");
		req.post(builder.build());
		ret = client.newCall(req.build()).execute().body().string();
		//Log.e(script, ret);
		//Log.e("user_id", prefs.getString("user_id", "-1"));
		return ret;
	}

	public static String executeURL(String url) throws Exception {
		OkHttpClient client = new OkHttpClient();
		Request.Builder req = new Request.Builder().url(url);

		okhttp3.Response resp = client.newCall(req.build()).execute();
		String ret = resp.body().string();
		resp.body().close();

		return ret;
	}

	public static void noInternetDialog(final Activity ctx, final boolean close) {
		ctx.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				CustomAlertDialog builder = new CustomAlertDialog(ctx);
				builder.setMessage("Failed to connect with server. Please check your internet connection.").setTitle("Connection Error");
				if (close) {
					builder.setCancelable(false).setPositiveButton("Close", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							ctx.finish();
						}
					});
				} else {
					builder.setPositiveButton("Close", null);
				}
				builder.create().show();
			}
		});
	}

	public static void somethingWrongDialog(final Activity ctx, final boolean close) {
		ctx.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				CustomAlertDialog builder = new CustomAlertDialog(ctx);
				builder.setMessage("Something went wrong. Please try again.").setTitle("Error");
				if (close) {
					builder.setCancelable(false).setPositiveButton("Close", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							ctx.finish();
						}
					});
				} else {
					builder.setPositiveButton("Close", null);
				}
				builder.create().show();
			}
		});
	}

	/*public static void noInternetImage(final Activity ctx) {
		ctx.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				ctx.findViewById(R.id.rootContent).setVisibility(View.GONE);
				ctx.findViewById(R.id.noInternet).setVisibility(View.VISIBLE);
			}
		});
	}*/

	public static ProgressDialog showWorkingProgress(final Context ctx, String msg, boolean show) {
		if (msg == null) {
			msg = "Working, please wait....";
		}
		ProgressDialog pDialog = new ProgressDialog(ctx);
		pDialog.setCancelable(false);
		pDialog.setMessage(msg);
		if (show) {
			pDialog.show();
		}
		return pDialog;
	}

	public static Response parseResponse(Activity ctx, String data, boolean showError) throws JSONException {
		Response resp = new Response(data);
		if (resp.error && showError) {
			Util.somethingWrongDialog(ctx, false);
		}
		return resp;
	}

	public static Response callAndParse(Activity ctx, boolean showError, String url, Object... params) throws Exception {
		String ret = execScript(ctx, url, params);
		return parseResponse(ctx, ret, showError);
	}

	public static void showSuspendDialog(final Activity ctx) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setMessage("Your account has been Suspended. You are no longer allowed to use Gookada Bike.").setTitle("Account Suspended").setCancelable(false).setPositiveButton("Close", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				ctx.finish();
			}
		}).create().show();
	}

	public static Address getAddrFromLatLng(double lat, double lng) throws Exception {
		String resp = executeURL("http://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + lng + "&KEY=" + URLEncoder.encode(mapsAPIKey, "UTF8"));
		Log.e("resp", resp);
		JSONObject obj = new JSONObject(resp);
		if (obj.getString("status").equals("OK")) {
			JSONArray result = obj.getJSONArray("results");
			if (result.length() > 0) {
				JSONObject res = result.getJSONObject(0);
				Address addr = new Address(Locale.ENGLISH);
				addr.setAddressLine(0, res.getString("formatted_address"));
				JSONArray addressComponents = res.getJSONArray("address_components");
				for (int i = 0; i < addressComponents.length(); i++) {
					obj = addressComponents.getJSONObject(i);
					JSONArray types = obj.getJSONArray("types");
					if (types.length() == 1 && types.getString(0).equals("postal_code")) {
						addr.setPostalCode(obj.getString("long_name"));
						break;
					}
				}
				return addr;
			}
		}
		Address addr = new Address(Locale.ENGLISH);
		addr.setAddressLine(0, "Unknown Address");
		addr.setPostalCode("000000");
		return addr;
	}
}
