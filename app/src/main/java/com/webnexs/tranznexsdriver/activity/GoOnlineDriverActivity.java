package com.webnexs.tranznexsdriver.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.webnexs.tranznexsdriver.Api;
import com.webnexs.tranznexsdriver.CustomAlertDialog;
import com.webnexs.tranznexsdriver.pojoClasses.CustomFontActivity;
import com.webnexs.tranznexsdriver.pojoClasses.RetrofitClient;
import com.webnexs.tranznexsdriver.pojoClasses.Util;
import com.webnexs.tranznexsdriver.pojoClasses.BaseResponse;
import com.webnexs.tranznexsdriver.pojoClasses.Constants;
import com.webnexs.tranznexsdriver.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;

@SuppressWarnings("MissingPermission")
public class GoOnlineDriverActivity extends CustomFontActivity implements /*QRCodeReaderView.OnQRCodeReadListener,*/ GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

	final String TAG = "GPS";
	//private QRCodeReaderView myDecoderView;
	private CustomFontActivity ctx = this;

	private LocationManager locManager;

	double lat, lng ;
	Location loc;
	LocationManager locationManager;
	private Boolean exit = false;
	private ProgressDialog pDialog;
	String latitude,longitude,driver_id,bike_typeNUmber;
	private final static int ALL_PERMISSIONS_RESULT = 101;
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
	boolean isGPS = false;
	boolean isNetwork = false;
	boolean canGetLocation = true;
	ArrayList<String> permissions = new ArrayList<>();
	ArrayList<String> permissionsToRequest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_go_online_driver);

		SharedPreferences.Editor editor = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE).edit();
		editor.putBoolean(Constants.KEY_ONLINE_IN,true);
		editor.apply();

		locationManager = (LocationManager) getSystemService(Service.LOCATION_SERVICE);
		isGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		isNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);


		permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
		permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
		permissionsToRequest = findUnAskedPermissions(permissions);

		if (!isGPS && !isNetwork) {
			Log.d(TAG, "Connection off");

		} else {
			Log.d(TAG, "Connection on");
			// check permissions
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				if (permissionsToRequest.size() > 0) {
					requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]),
							ALL_PERMISSIONS_RESULT);
					Log.d(TAG, "Permission requests");
					canGetLocation = false;
				}
			}


			getLocation();
		}

		SharedPreferences prefs = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE);
		driver_id = prefs.getString(Constants.driver_id, "");
		bike_typeNUmber = prefs.getString(Constants.biketypeNUmber, "");

		Button goOnline = (Button)findViewById(R.id.goOnline);
		goOnline.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				goOnlineMethod();
			}
		});
		locManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		pDialog = new ProgressDialog(this);
		pDialog.setMessage("Fetching your current location. Please wait....");
		pDialog.setCancelable(true);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				pDialog.cancel();
			}
		},1000);
	}


	private ArrayList findUnAskedPermissions(ArrayList<String> wanted) {
		ArrayList result = new ArrayList();

		for (String perm : wanted) {
			if (!hasPermission(perm)) {
				result.add(perm);
			}
		}

		return result;
	}
	private boolean hasPermission(String permission) {
		if (canAskPermission()) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
					return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
				}
			}
		}SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
		Date date = new Date();
		String CurrentTime = formatter.format(date);
		return true;
	}
	private boolean canAskPermission() {
		return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
	}


	private void goOnlineMethod() {
		latitude = String.valueOf(lat);
		longitude = String.valueOf(lng);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String CurrentTime = formatter.format(date);

		Api api = RetrofitClient.getRetrofitInstance().create(Api.class);
		Call<BaseResponse> userCall = api.goOnline(driver_id,bike_typeNUmber,latitude,longitude,CurrentTime);
		userCall.enqueue(new Callback<BaseResponse>() {
							 @Override
							 public void onResponse(Call<BaseResponse> call, retrofit2.Response<BaseResponse> response) {
								 BaseResponse baseResponse = response.body();
								 if (baseResponse.status.equalsIgnoreCase("true")) {
									 SharedPreferences.Editor editor = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE).edit();
									 editor.putString(Constants.latitude, latitude);
									 editor.putString(Constants.longitude, latitude);
									 editor.putBoolean(Constants.KEY_ONLINE_IN,false);
									 editor.apply();
									 Toast.makeText(getBaseContext(), "Now you are online", Toast.LENGTH_LONG).show();
									 startActivity(new Intent(GoOnlineDriverActivity.this, DriverHomeActivity.class));
									 finish();

								 } else {
									 new CustomAlertDialog(ctx).setMessage("You are not allowed to go online now.").setPositiveButton("CLOSE", null).create().show();
								 }
							 }

							 @Override
							 public void onFailure(Call<BaseResponse> call, Throwable t) {
								 Log.d("onFailure", t.toString());
								 Util.noInternetDialog(ctx, false);
							 }
						 });

	}
	@Override
	protected void onStart() {
		super.onStart();

		if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Toast.makeText(ctx, "Please enable your GPS", Toast.LENGTH_LONG).show();
			Intent viewIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivity(viewIntent);
		} else {
			buildGoogleApiClient();
			if (!pDialog.isShowing()) {
				pDialog.show();
			}
		}
	}
	private void getLocation() {
		try {
			if (canGetLocation) {
				Log.d(TAG, "Can get location");
				if (isGPS) {
					// from GPS
					Log.d(TAG, "GPS on");
					locationManager.requestLocationUpdates(
							LocationManager.GPS_PROVIDER,
							MIN_TIME_BW_UPDATES,
							MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

					if (locationManager != null) {
						loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
						if (loc != null)
							updateUI(loc);
					}
				} else if (isNetwork) {
					// from Network Provider
					Log.d(TAG, "NETWORK_PROVIDER on");
					locationManager.requestLocationUpdates(
							LocationManager.NETWORK_PROVIDER,
							MIN_TIME_BW_UPDATES,
							MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

					if (locationManager != null) {
						loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if (loc != null)
							updateUI(loc);
					}
				} else {
					loc.setLatitude(0);
					loc.setLongitude(0);
					updateUI(loc);
				}
			} else {
				Log.d(TAG, "Can't get location");
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}
	private void updateUI(Location loc) {
		Log.d(TAG, "updateUI");
		lat = loc.getLatitude();
		lng = loc.getLongitude();
		// tvTime.setText(DateFormat.getTimeInstance().format(loc.getTime()));
	}
	private GoogleApiClient mGoogleApiClient;

	protected synchronized void buildGoogleApiClient() {
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API)
				.build();
		mGoogleApiClient.connect();
	}

	boolean readOnce = false;

	@Override
	protected void onResume() {
		super.onResume();
		if (readOnce) {
			return;
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		Log.d(TAG, "onLocationChanged");
		updateUI(location);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public void onProviderEnabled(String s) {
		getLocation();
	}

	@Override
	public void onProviderDisabled(String s) {
		if (locationManager != null) {
			locationManager.removeUpdates(this);
		}

	}

	LocationRequest mLocationRequest;

	@Override
	public void onConnected(@Nullable Bundle bundle) {
		mLocationRequest = new LocationRequest();
		mLocationRequest.setInterval(1000);
		mLocationRequest.setFastestInterval(1000);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		//LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

		FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
		mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
			@Override
			public void onSuccess(Location location) {
				// Got last known location. In some rare situations this can be null.
				if (location != null) {
					onLocationChanged(location);
				}
			}
		});
	}

	@Override
	public void onConnectionSuspended(int i) {

	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

	}
	@Override
	public void onBackPressed() {


		// this.finish();
		if (exit) {
			finish(); // finish activity
		} else {
			Toast.makeText(this, "Press Back again to Home.",
					Toast.LENGTH_SHORT).show();
			Intent a = new Intent(Intent.ACTION_MAIN);
			a.addCategory(Intent.CATEGORY_HOME);
			a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(a);
		}
	}
}
