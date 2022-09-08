package com.webnexs.tranznexsdriver.pojoClasses;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

@SuppressWarnings("MissingPermission")
public class BackService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
	public BackService() {
	}

	static Response resp;
	private int lastStatus = 0;
	private LatLng endLatLng = null;

	private static final int SYNC_INTERVAL = 2800;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		lbm = LocalBroadcastManager.getInstance(this);
		buildGoogleApiClient();
		/*new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					long now = System.currentTimeMillis();
					if (lat != 0) {
						try {
							String r = Util.execScript(BackService.this, "driver", "latitude", lat, "longitude", lng, "speed", speed, "no_data", false);
							resp = new Response(r);
							if (resp.status == ReturnCodes.DRIVER_OFFLINE) {
								stopSelf();
								return;
							}
							if (lastStatus != resp.status && resp.status == ReturnCodes.NEW_RIDE) {
								if (!DriverHomeActivity.running) {
									Intent i = new Intent(BackService.this, DriverHomeActivity.class);
									i.putExtra("new_booking", true);
									i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
									startActivity(i);

									endLatLng = null;
								}
							}
							if (resp.status == ReturnCodes.EXISTING_ON_RIDE) {
								LatLng latLng = new LatLng(resp.getJObjData().getDouble("end_lat"), resp.getJObjData().getDouble("end_lng"));
								if (endLatLng == null) {
									endLatLng = latLng;
								} else {
									if (latLng.longitude != endLatLng.longitude || latLng.latitude != endLatLng.latitude) {
										Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
										Ringtone rt = RingtoneManager.getRingtone(getApplicationContext(), notification);
										rt.play();
										Intent i = new Intent(BackService.this, DriverHomeActivity.class);
										i.putExtra(DESTINATION_UPDATE, true);
										i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
										startActivity(i);
										endLatLng = latLng;
									}
								}
							} else if (resp.status == ReturnCodes.RIDE_CANCELED_BY_USER) {
								*//*if (DriverHomeActivity.newBookingRing != null) {
									DriverHomeActivity.newBookingRing.stop();
									DriverHomeActivity.newBookingRing.release();
									DriverHomeActivity.newBookingRing = null;
								}*//*
							}
							Intent i = new Intent(NEW_RESPONSE);
							lbm.sendBroadcast(i);
							lastStatus = resp.status;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					try {
						TimeUnit.MILLISECONDS.sleep(SYNC_INTERVAL - System.currentTimeMillis() + now);
					} catch (InterruptedException ignored) {
					}
				}
			}
		}).start();*/
	}

	public static double lat = 0, lng = 0;
	static float speed = 0;
	final static String MAP_UPDATE = "map_update";
	final static String NEW_RESPONSE = "new_response";
	final static String DESTINATION_UPDATE = "destination_update";

	public GoogleApiClient mGoogleApiClient;

	protected synchronized void buildGoogleApiClient() {
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API)
				.build();
		mGoogleApiClient.connect();
	}

	@Override
	public void onConnected(@Nullable Bundle bundle) {
		LocationRequest mLocationRequest = new LocationRequest();
		mLocationRequest.setInterval(500);
		mLocationRequest.setFastestInterval(500);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
	}

	@Override
	public void onConnectionSuspended(int i) {

	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

	}

	@Override
	public void onDestroy() {
		//LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
		super.onDestroy();
	}

	public boolean firstFix = true;

	static boolean noDataMode = false;
	public LocalBroadcastManager lbm;

	@Override
	public void onLocationChanged(final Location location) {
		lat = location.getLatitude();
		lng = location.getLongitude();
		speed = location.getSpeed();

		if (firstFix) {
			Intent i = new Intent(MAP_UPDATE);
			lbm.sendBroadcast(i);
			firstFix = false;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}
}
