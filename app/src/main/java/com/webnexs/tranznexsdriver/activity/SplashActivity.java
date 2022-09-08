package com.webnexs.tranznexsdriver.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.webnexs.tranznexsdriver.pojoClasses.CustomFontActivity;
import com.wang.avi.AVLoadingIndicatorView;
import com.webnexs.tranznexsdriver.R;
import com.webnexs.tranznexsdriver.pojoClasses.Constants;

public class SplashActivity extends CustomFontActivity {

	static boolean running = false;
    AVLoadingIndicatorView avi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

	}
	@Override
	protected void onStart() {
		super.onStart();
		LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
		boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);

        avi = (AVLoadingIndicatorView)findViewById(R.id.avi);
        avi.show();

		if (!enabled){
			Intent viewIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivity(viewIntent);
		}
		else if (ContextCompat.checkSelfPermission(SplashActivity.this,
				Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

			// Check if enabled and if not send user to the GPS settings

			if (ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this,
					Manifest.permission.ACCESS_FINE_LOCATION)) {

				ActivityCompat.requestPermissions(SplashActivity.this,
						new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
			}

			else{
				ActivityCompat.requestPermissions(SplashActivity.this,
						new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
				/* GetActivity();*/
			}

		}else{
			getStarted();
		}
		running = true;
	}

	public void getStarted() {

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				SharedPreferences prefs = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE);
				if(prefs.getBoolean(Constants.KEY_LOGGED_IN, false)){
					startActivity(new Intent(SplashActivity.this, DriverHomeActivity.class));
					finish();
					if(prefs.getBoolean(Constants.KEY_ONRIDE, false)){
						startActivity(new Intent(SplashActivity.this, DriverHomeActivity.class));
						finish();
					}else{
						startActivity(new Intent(SplashActivity.this, DriverHomeActivity.class));
						finish();
					}if(prefs.getBoolean(Constants.KEY_ONTRIP_PAYMENT, false)){
						startActivity(new Intent(SplashActivity.this, TakePaymentActivity.class));
						finish();
					}else{
						startActivity(new Intent(SplashActivity.this, DriverHomeActivity.class));
						finish();
					}
					if(prefs.getBoolean(Constants.KEY_ONLINE_IN, false)){
						startActivity(new Intent(SplashActivity.this, GoOnlineDriverActivity.class));
						finish();
					} else {
						startActivity(new Intent(SplashActivity.this, DriverHomeActivity.class));
						finish();
					}
				}else if(prefs.getBoolean(Constants.KEY_EMAIL_LOGGED_IN, false)){
                    startActivity(new Intent(SplashActivity.this, DriverHomeActivity.class));
                    finish();
                }
                else {
                    startActivity(new Intent(SplashActivity.this, LoginOptionActivity.class));
                    finish();
                }
			}
		},1000);
	}
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions,
										   int[] grantResults){
		switch (requestCode){
			case 1: {
				if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
					if (ContextCompat.checkSelfPermission(SplashActivity.this,
							Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
					//	Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
						getStarted();

					}
				}else{
					//Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
					if (ContextCompat.checkSelfPermission(SplashActivity.this,
							Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
						if (ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this,
								Manifest.permission.ACCESS_FINE_LOCATION)){
							ActivityCompat.requestPermissions(SplashActivity.this,
									new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
						}else{
							ActivityCompat.requestPermissions(SplashActivity.this,
									new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
						}
					}
				}
				return;
			}
		}
	}

}
