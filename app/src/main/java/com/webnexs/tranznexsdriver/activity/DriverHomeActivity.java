package com.webnexs.tranznexsdriver.activity;

import android.Manifest;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.webnexs.tranznexsdriver.Api;
import com.webnexs.tranznexsdriver.CustomAlertDialog;
import com.webnexs.tranznexsdriver.pojoClasses.EndTripResponse;
import com.webnexs.tranznexsdriver.pojoClasses.PolylineObject;
import com.webnexs.tranznexsdriver.pojoClasses.Response;
import com.webnexs.tranznexsdriver.pojoClasses.RetrofitClient;
import com.webnexs.tranznexsdriver.pojoClasses.RouteObject;
import com.webnexs.tranznexsdriver.pojoClasses.StepsObject;
import com.webnexs.tranznexsdriver.pojoClasses.User;
import com.webnexs.tranznexsdriver.pojoClasses.Util;
import com.webnexs.tranznexsdriver.pojoClasses.AlarmReceiver;
import com.webnexs.tranznexsdriver.pojoClasses.BackService;
import com.webnexs.tranznexsdriver.pojoClasses.BaseResponse;
import com.webnexs.tranznexsdriver.pojoClasses.CircleTransform;
import com.webnexs.tranznexsdriver.pojoClasses.Constants;
import com.webnexs.tranznexsdriver.pojoClasses.CustomFontFragmentActivity;
import com.webnexs.tranznexsdriver.pojoClasses.DirectionObject;
import com.webnexs.tranznexsdriver.pojoClasses.GsonRequest;
import com.webnexs.tranznexsdriver.pojoClasses.Helper;
import com.webnexs.tranznexsdriver.pojoClasses.LegsObject;
import com.webnexs.tranznexsdriver.pojoClasses.VolleySingleton;
import com.squareup.picasso.Picasso;
import com.victor.loading.rotate.RotateLoading;

import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import co.intentservice.chatui.ChatView;
import co.intentservice.chatui.models.ChatMessage;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;

import com.wang.avi.AVLoadingIndicatorView;
import com.webnexs.tranznexsdriver.R;
import retrofit2.Callback;

@SuppressWarnings("MissingPermission")
@SuppressLint("SetTextI18n")
public class DriverHomeActivity extends CustomFontFragmentActivity implements OnMapReadyCallback, LocationListener {

	public static String APP_TAG;
	private Circle lastUserCircle;
	private static int animation = 1000;
	private ValueAnimator lastPulseAnimator;
	GoogleMap mMap;
	private MapStyleOptions mapStyle;
	Marker mPositionMarker;
	PolylineOptions options;
	String Hourly = "Hourly";

	private LocationManager locManager;

	private DriverHomeActivity ctx = this;

	private DrawerLayout drawer;

	private Response resp = null;

	static boolean running = false;

	private float acceptedFare = 0;

	// chat variables
	ChatView chatView;
	AlertDialog chatDialog;
	View chatViewRoot;
	TextView cancelTrip_reason,endLocation,locationName;
	ImageView header_title,side_bar;
	LinearLayout layout_complete ,rideRequest,finding_trips,opertunity_box,
			online_box,accept_options,layout_accept_cancel,layout_arrived_cancel;
	Button header_EndTrip_text;
	RelativeLayout otp_ConfirmRoot,accept_ride,cancel_ride,layout_arrived,cancel_acceptRide;
	String sideMenuUserName,driver_id,latitude,longitude,lisenceNo,usermobileno,UserImg,UserPickuplng,UserPickuplat;
	double lat,lng,UserpicLat,UserpicLng,UserdrpLat,UserdrpLng;
	PinEntryEditText otpText;
	final String TAG = "GPS";
	private final static int ALL_PERMISSIONS_RESULT = 101;
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
	LocationManager locationManager;
	Location loc;
	ArrayList<String> permissions = new ArrayList<>();
	ArrayList<String> permissionsToRequest;
	ArrayList<String> permissionsRejected = new ArrayList<>();
	boolean isGPS = false;
	boolean isNetwork = false;
	boolean canGetLocation = true;
	private Boolean exit = false;
	Api myApi;
	//userresponse
	String UserMobileNumber,RidingUserId,Drop_loc_name,Pickup_loc_name,ride_id,Fare,otp,distance,
			bike_type_number,bike_type_name,RideType,TotoalEarning,OnlineHours,cancellation_Resson;
	MediaPlayer mediaPlayer ,mediaPlayer2;
	private Object LatLng;
	Timer timerAsync,timerAsync2,timerAsyncOnline,drivertracking ;
	int interval= 30 * 1000;
	int onlineInterval= 3 * 1000;
	int driverSearching= 5 * 1000;
	int driverTRackingInterval= 10 * 1000;
	int randomOtp;
	TimerTask timerTaskAsync;
	CountDownTimer countDownTimer;
	TextView counttime;
	private static int TIME_OUT_ = 100;
	public static AlarmManager am;
	public static Calendar cal;
	public static PendingIntent sender;
	String start_ride_time;
	RotateLoading cusRotateLoading;
	Dialog ProgressDialog,cancel_reason_dialog,custom_dialog;
	AVLoadingIndicatorView avi;
	//Dialog text view & Edittext
	TextView rider_not_there,wrong_address,rude_call,bad_location,personal_issues,yes_cancel,txt_no,done,cancel_iconText;
	//EditText edt_personal_issues;
	LinearLayout edt_personal_issue_layout;
	String DropLat,Droplng;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_driver_home);

		avi = (AVLoadingIndicatorView)findViewById(R.id.avi);
		avi.show();

		SharedPreferences prefs = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE);
		start_ride_time = prefs.getString(Constants.START_TIMING, "");
		driver_id = prefs.getString(Constants.driver_id, "");
		lisenceNo = prefs.getString(Constants.LicenseNo, "");
		bike_type_name = prefs.getString(Constants.biketypeName, "");
		usermobileno = prefs.getString(Constants.usermobileno, "");
		bike_type_number = prefs.getString(Constants.biketypeNUmber, "");
		ride_id = prefs.getString(Constants.ride_id, "");
		RidingUserId = prefs.getString(Constants.rider_id, "");
		latitude = prefs.getString(Constants.latitude, "");
		longitude = prefs.getString(Constants.longitude, "");
		UserImg = prefs.getString(Constants.userImg, "");
		UserPickuplat = prefs.getString(Constants.UserPickup_lat, "");
		UserPickuplng = prefs.getString(Constants.UserPickup_lng, "");
		DropLat = prefs.getString(Constants.Drop_lat, "");
		Droplng = prefs.getString(Constants.Drop_lng, "");
		otp = prefs.getString(Constants.Ride_otp, "");
		Pickup_loc_name = prefs.getString(Constants.Pickup_loc, "");
		Drop_loc_name = prefs.getString(Constants.Drop_loc, "");
		counttime=findViewById(R.id.counttime);
		//image = prefs.getString(Constants.image, "");

		sideMenuUserName = prefs.getString(Constants.firstname, "");//"No name defined" is the default value.
		//location
		locationManager = (LocationManager) getSystemService(Service.LOCATION_SERVICE);
		isGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		isNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

		permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
		permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
		permissionsToRequest = findUnAskedPermissions(permissions);

		if (!isGPS && !isNetwork) {
			Log.d(TAG, "Connection off");
			// showSettingsAlert();
			// getLastLocation();
		} else {
			Log.d(TAG, "Connection on");
			// check permissions
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
				if (permissionsToRequest.size() > 0) {
					requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]),
							ALL_PERMISSIONS_RESULT);
					Log.d(TAG, "Permission requests");
					canGetLocation = false;
				}
			}
			getLocation();
		}

		/*TextView callRider = (TextView)findViewById(R.id.callRider);
		callRider.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setData(Uri.parse("tel:"+usermobileno));
				startActivity(callIntent);
			}
		});*/
		//location
		//Toast.makeText(this, "ans"+lat+" "+lng, Toast.LENGTH_SHORT).show();
		// Obtain the SupportMapFragment and get notified when the map is ready to be used.
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);

		locManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		online_box = (LinearLayout)findViewById(R.id.online_box);
		//fndng_strt_layout = (LinearLayout)findViewById(R.id.fndng_strt_layout);
		//accept_options = (LinearLayout)findViewById(R.id.accept_options);
		accept_ride = (RelativeLayout)findViewById(R.id.accept_ride);
		cancel_ride = (RelativeLayout)findViewById(R.id.cancel_ride);
		layout_accept_cancel = (LinearLayout)findViewById(R.id.layout_accept_cancel);
		layout_arrived = (RelativeLayout)findViewById(R.id.layout_arrived);
		cancel_acceptRide = (RelativeLayout)findViewById(R.id.cancel_acceptRide);
		layout_arrived_cancel = (LinearLayout)findViewById(R.id.layout_arrived_cancel);
		opertunity_box = (LinearLayout)findViewById(R.id.opertunity_box);
		finding_trips = (LinearLayout)findViewById(R.id.finding_trips);
		header_EndTrip_text = (Button) findViewById(R.id.header_EndTrip_text);
		header_title = (ImageView)findViewById(R.id.header_title);
		locationName = findViewById(R.id.locationName);
		endLocation = findViewById(R.id.endLocation);
		side_bar = (ImageView)findViewById(R.id.side_bar);
		layout_complete = (LinearLayout)findViewById(R.id.layout_complete);
		otp_ConfirmRoot = (RelativeLayout)findViewById(R.id.otp_ConfirmRoot);
		otpText = (PinEntryEditText) findViewById(R.id.startTripOTP);
		rideRequest = (LinearLayout)findViewById(R.id.rideRequest);
		//newBookingRoot = (LinearLayout)findViewById(R.id.newBookingRoot);
		mediaPlayer = MediaPlayer.create(this, R.raw.new_booking);
		mediaPlayer2 = MediaPlayer.create(this, R.raw.beep);
		drawer = findViewById(R.id.drawer_layout);
		//cancelTrip_reason = (TextView)findViewById(R.id.cancelTrip_reason);

		//Dialog id's
		custom_dialog = new Dialog(DriverHomeActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
		custom_dialog.setContentView(R.layout.custom_dialog);
		yes_cancel = (TextView) custom_dialog.findViewById(R.id.yes_cancel);
		txt_no = (TextView) custom_dialog.findViewById(R.id.no);
		cancel_reason_dialog = new Dialog(DriverHomeActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
		cancel_reason_dialog.setContentView(R.layout.cancel_reason_dialog);
		rider_not_there = (TextView) cancel_reason_dialog.findViewById(R.id.rider_not_ther);
		wrong_address = (TextView) cancel_reason_dialog.findViewById(R.id.wrong_address);
		rude_call = (TextView) cancel_reason_dialog.findViewById(R.id.rude_call);
		bad_location = (TextView) cancel_reason_dialog.findViewById(R.id.bad_location);
		personal_issues = (TextView) cancel_reason_dialog.findViewById(R.id.personal_issues);
		edt_personal_issue_layout = (LinearLayout) cancel_reason_dialog.findViewById(R.id.edt_personal_issue_layout);

		cancel_iconText = (TextView) cancel_reason_dialog.findViewById(R.id.cancel_iconText);
		done = (TextView) cancel_reason_dialog.findViewById(R.id.done);

		rider_not_there.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				rider_not_there.setBackgroundResource(R.drawable.btn_accent_circle_white_outlined);
				wrong_address.setBackgroundResource(0);
				rude_call.setBackgroundResource(0);
				bad_location.setBackgroundResource(0);
				edt_personal_issue_layout.setVisibility(View.GONE);
				done.setVisibility(View.VISIBLE);
				cancellation_Resson = rider_not_there.getText().toString();
			}
		});


		if(prefs.getBoolean(Constants.ONRIDE,false)){
			acceptedRidedetails();
		}

		wrong_address.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				wrong_address.setBackgroundResource(R.drawable.btn_accent_circle_white_outlined);
				rude_call.setBackgroundResource(0);
				bad_location.setBackgroundResource(0);
				rider_not_there.setBackgroundResource(0);
				edt_personal_issue_layout.setVisibility(View.GONE);
				done.setVisibility(View.VISIBLE);
				cancellation_Resson = wrong_address.getText().toString();
			}
		});

		rude_call.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				rude_call.setBackgroundResource(R.drawable.btn_accent_circle_white_outlined);
				bad_location.setBackgroundResource(0);
				rider_not_there.setBackgroundResource(0);
				wrong_address.setBackgroundResource(0);
				edt_personal_issue_layout.setVisibility(View.GONE);
				done.setVisibility(View.VISIBLE);
				cancellation_Resson = rude_call.getText().toString();
			}
		});

		bad_location.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				bad_location.setBackgroundResource(R.drawable.btn_accent_circle_white_outlined);
				rider_not_there.setBackgroundResource(0);
				wrong_address.setBackgroundResource(0);
				rude_call.setBackgroundResource(0);
				edt_personal_issue_layout.setVisibility(View.GONE);
				done.setVisibility(View.VISIBLE);
				cancellation_Resson = bad_location.getText().toString();
			}
		});


		personal_issues.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				edt_personal_issue_layout.setVisibility(View.VISIBLE);
				bad_location.setBackgroundResource(0);
				rider_not_there.setBackgroundResource(0);
				wrong_address.setBackgroundResource(0);
				rude_call.setBackgroundResource(0);
				done.setVisibility(View.VISIBLE);
				cancellation_Resson = personal_issues.getText().toString();
			}
		});
		done.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ProgressDialog.show();
				cusRotateLoading.start();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = new Date();
				String CurrentTime = formatter.format(date);
				Api api = RetrofitClient.getRetrofitInstance().create(Api.class);
				Call<BaseResponse> userCall = api.cancelAcceptRide(driver_id,ride_id,RidingUserId,cancellation_Resson,CurrentTime);
				userCall.enqueue(new Callback<BaseResponse>() {
					@Override
					public void onResponse(Call<BaseResponse> call, retrofit2.Response<BaseResponse> response) {

						BaseResponse baseResponse = response.body();

						if (baseResponse.status.equalsIgnoreCase("true")) {
							mMap.clear();
							timerAsync2.cancel();
						/*	timerAsync.cancel();*/
							interval = 0;
							drivertracking.cancel();
							new CustomAlertDialog(ctx).setMessage(baseResponse.getMessage()).setPositiveButton("CLOSE", null).create().show();
							Intent intent = new Intent(ctx, DriverHomeActivity.class);
							startActivity(intent);
							onStart();

						} else {
							//	Toast.makeText(DriverHomeActivity.this, "online_false", Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void onFailure(Call<BaseResponse> call, Throwable t) {
						Log.d("onFailure", t.toString());
						Toast.makeText(DriverHomeActivity.this, "Check internet.", Toast.LENGTH_SHORT).show();
						ProgressDialog.cancel();
						cusRotateLoading.stop();
					}
				});
				/*edt_personal_issue_layout.setVisibility(View.GONE);
				bad_location.setBackgroundResource(0);
				rider_not_there.setBackgroundResource(0);
				wrong_address.setBackgroundResource(0);
				rude_call.setBackgroundResource(0);
				cancel_reason_dialog.cancel();*/
			}
		});


		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				startBottomToTopAnimation(online_box);
				online_box.setVisibility(View.VISIBLE);
				startBottomToTopAnimation(opertunity_box);
				opertunity_box.setVisibility(View.VISIBLE);
				startBottomToTopAnimation(finding_trips);
				finding_trips.setVisibility(View.VISIBLE);
			}
		},animation);

		ProgressDialog = new Dialog(DriverHomeActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
		ProgressDialog.setContentView(R.layout.custom_progress_dialog);
		ProgressDialog.setCancelable(false);
		cusRotateLoading = (RotateLoading)ProgressDialog.findViewById(R.id.rotateloading_register);
		myApi = RetrofitClient.getRetrofitInstance().create(Api.class);

		//RideType = prefs.getString(Constants.RideType, "");


		otpText.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
			@Override
			public void onPinEntered(final CharSequence otp_pin) {
				if (otp_pin.length() == 4) {
					ProgressDialog.show();
					cusRotateLoading.start();
					if (otp_pin.toString().equals(otp)) {
						otpConfirmStrt();
						/*Call<BaseResponse> call = myApi.otpVerification(otp);
						call.enqueue(new Callback<BaseResponse>() {
							@Override
							public void onResponse(Call<BaseResponse> call, retrofit2.Response<BaseResponse> response) {
								BaseResponse users = response.body();

								if (users.status.equalsIgnoreCase("true")) {
									otpConfirmStrt();
								}
								else {

								}
							}
							@Override
							public void onFailure(Call<BaseResponse> call, Throwable t) {
								Toast.makeText(DriverHomeActivity.this, "Check your internet connection...", Toast.LENGTH_SHORT).show();

							}
						});*/
					}
					else {
						Toast.makeText(DriverHomeActivity.this, "Incorrect otp...", Toast.LENGTH_SHORT).show();
						ProgressDialog.cancel();
						cusRotateLoading.stop();
					}



				}
			}
		});
		/*try {
			Util.getAddrFromLatLng(lat,lng);
			TextView fromLocation = (TextView)findViewById(R.id.pickupLocation);
			fromLocation.setText((CharSequence) Util.getAddrFromLatLng(lat,lng));
		} catch (Exception e) {
			e.printStackTrace();
		}*/

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				reCenter();
			}
		}, 100);

		timerAsync2 = new Timer();
		timerTaskAsync = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override public void run() {
						NewUserBooking();
						//	Toast.makeText(DriverHomeActivity.this, "Searching..", Toast.LENGTH_SHORT).show();
					}
				});
			}
		};  timerAsync2.schedule(timerTaskAsync, 0,driverSearching);

		timerAsyncOnline = new Timer();
		timerTaskAsync = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override public void run() {
						OnlineState();
					}
				});
			}
		};  timerAsyncOnline.schedule(timerTaskAsync, 0,onlineInterval);




		header_title.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//	newBookingRoot.setVisibility(View.VISIBLE);
			}
		});
		//CancelRide = (SwipeButton)findViewById(R.id.cancelRide);

		/*CancelRide.setOnStateChangeListener(new OnStateChangeListener() {
			@Override
			public void onStateChange(boolean active) {
				if (active) {

				}
			}
		});*/

		header_EndTrip_text.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				layout_complete.setVisibility(View.VISIBLE);
				endLocation.setVisibility(View.VISIBLE);
				locationName.setVisibility(View.GONE);
			}
		});

		FloatingActionButton reCenter = (FloatingActionButton) findViewById(R.id.reCenter);
		reCenter.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				reCenter();
			}
		});

	/*	Target viewTarget = new ViewTarget(R.id.reCenter, this);  // Add the control you need to focus by the ShowcaseView
		new ShowcaseView.Builder(this)
				.setTarget(viewTarget)
				.setContentTitle("Welcome")       // Add your string file (title_single_shot) in values/strings.xml
				.setContentText("Hello every one wellcome to all") // Add your string file (R_strings_desc_single_shot) in values/strings.xml
				.singleShot(42)
				.hideOnTouchOutside()
				.setStyle(R.color.colorPrimary)
				.build();*/




		ImageView img = (ImageView)findViewById(R.id.left_menu_userImg);
		Picasso.get()
				.load(Uri.parse(prefs.getString("imageDriver", "")))
				.placeholder(R.drawable.user)
				.transform(new CircleTransform())
				.into(img);
		img.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent (DriverHomeActivity.this, EditProfile.class);
				startActivity(intent);
			}
		});


		ListView drawerList = findViewById(R.id.drawer);
		ArrayAdapter<String> drawerAd = new ArrayAdapter<>(this, R.layout.listview_item_text_white);
		drawerAd.add("Your Trips");
		drawerAd.add("Wallet");
		drawerAd.add("Go Offline");
		drawerAd.add("Earning");
		drawerAd.add("Setting");
		drawerAd.add("Support");
		drawerAd.add("About");
		drawerAd.add("Logout");
		drawerList.setAdapter(drawerAd);

		//getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (position == 0) {
					startActivity(new Intent(ctx, PastTripsActivity.class));
				} else if (position == 1) {
					/*Intent intent = new Intent(ctx, WalletActivity.class);
					 *//*intent.putExtra("lat", BackService.lat);
					intent.putExtra("lng", BackService.lng);*//*
					startActivity(intent);*/
				} else if (position == 2) {
					new CustomAlertDialog(ctx).setMessage("Are you sure to go offline now?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							//final ProgressDialog pDialog = Util.showWorkingProgress(ctx, null, true);
							new Thread(new Runnable() {
								@Override
								public void run() {
									offline();
								}
							}).start();
						}
					}).setNegativeButton("No", null).create().show();
				}
				else if(position == 3){
					//startActivity(new Intent(ctx, DailyStatement.class));
				}
				else if (position == 7) {

					android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(DriverHomeActivity.this);
					alertDialogBuilder.setTitle("Logout");
					alertDialogBuilder.setMessage("Are you sure you want to log out ?");
					alertDialogBuilder.setPositiveButton("YES",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									File cache = getCacheDir();
									File appDir = new File(cache.getParent());
									if(appDir.exists()){
										String[] children = appDir.list();
										for(String s : children){
											if(!s.equals("lib")){
												deleteDir(new File(appDir, s));
												Log.i("TAG", "File /data/data/APP_PACKAGE/" + s +" DELETED");
											}
										}
									}
									SharedPreferences prefs =getSharedPreferences(Constants.MY_PREFS_NAME,Context.MODE_PRIVATE);
									SharedPreferences.Editor editor = prefs.edit();
									editor.clear();
									editor.apply();
									interval=0;
									onlineInterval=0;
									driverSearching=0;
									driverTRackingInterval=0;
									timerAsync2.cancel();
									LogOutoffline();
									//		timerAsync.cancel();
									//	timerAsyncOnline.cancel();
									//	drivertracking.cancel();

								}
							});

					alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});

					android.app.AlertDialog alertDialog = alertDialogBuilder.create();
					alertDialog.show();
				}
			}
		});

		/*SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);*/
		((TextView) findViewById(R.id.myName)).setText(sideMenuUserName);
		((TextView) findViewById(R.id.myId)).setText("Trnz-"+driver_id);

		latitude = String.valueOf(lat);
		longitude = String.valueOf(lng);

		layout_complete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				new CustomAlertDialog(ctx).setMessage("Are you sure to Complete your trip?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//final ProgressDialog pDialog = Util.showWorkingProgress(ctx, null, true);
						new Thread(new Runnable() {
							@Override
							public void run() {
								endTrip();
							}
						}).start();
					}
				}).setNegativeButton("No", null).create().show();
			}
		});


		accept_ride.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AcceptRide();
			}
		});

		cancel_ride.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				custom_dialog.show();
			}

		});


		yes_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cancelTrip();
			}
		});


		txt_no.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				custom_dialog.dismiss();
			}
		});


		cancel_iconText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cancel_reason_dialog.dismiss();
			}
		});
		cancel_acceptRide.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cancel_reason_dialog.show();
			}
		});
		layout_arrived.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ProgressDialog.show();
				cusRotateLoading.start();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = new Date();
				String CurrentTime = formatter.format(date);
				Api api = RetrofitClient.getRetrofitInstance().create(Api.class);
				Call<BaseResponse> userCall = api.arrived(driver_id,CurrentTime,ride_id);
				userCall.enqueue(new Callback<BaseResponse>() {
					@Override
					public void onResponse(Call<BaseResponse> call, retrofit2.Response<BaseResponse> response) {

						BaseResponse baseResponse = response.body();

						if (baseResponse.status.equalsIgnoreCase("true")) {
							ProgressDialog.cancel();
							interval = 0;
							cusRotateLoading.stop();
							otp_ConfirmRoot.setVisibility(View.VISIBLE);
							layout_arrived_cancel.setVisibility(View.GONE);

						} else {
							//	Toast.makeText(DriverHomeActivity.this, "online_false", Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void onFailure(Call<BaseResponse> call, Throwable t) {
						Log.d("onFailure", t.toString());
						Toast.makeText(DriverHomeActivity.this, "Check internet.", Toast.LENGTH_SHORT).show();
						ProgressDialog.cancel();
						cusRotateLoading.stop();
					}
				});
			}
		});
	}

	private void acceptedRidedetails() {

		Api myApi = RetrofitClient.getRetrofitInstance().create(Api.class);
		Call<User> call = myApi.getAcceptedDtls(driver_id,ride_id,RidingUserId);
		call.enqueue(new Callback<User>() {
			@Override
			public void onResponse(Call<User> call, retrofit2.Response<User> response) {
				User user = response.body();

				if (user.status.equalsIgnoreCase("true")) {
					User.UserDetails userData = user.getUserDetails();

					rideRequest.setVisibility(View.VISIBLE);
					counttime.setVisibility(View.GONE);
					startBottomToTopAnimation(rideRequest);
					finding_trips = (LinearLayout)findViewById(R.id.finding_trips);
					finding_trips.setVisibility(View.GONE);
					layout_arrived_cancel.setVisibility(View.VISIBLE);
					startBottomToTopAnimation(layout_arrived_cancel);
					side_bar.setVisibility(View.GONE);
					drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
					layout_accept_cancel.setVisibility(View.GONE);
					cancel_acceptRide.setVisibility(View.GONE);

					SharedPreferences.Editor editor = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE).edit();
					editor.putString(Constants.amount, userData.getFare());
					editor.putString(Constants.RideType, userData.getRide_type());
					editor.putString(Constants.usermobileno, userData.getPhone_no());
					editor.putString(Constants.ride_id, userData.getRide_id());
					editor.putString(Constants.userImg, userData.getUser_image());
					editor.putString(Constants.UserPickup_lat, userData.getPickup_lat());
					editor.putString(Constants.UserPickup_lng, userData.getPickup_lng());
					editor.putString(Constants.Ride_otp, userData.getRide_otp());
					editor.putString(Constants.Pickup_loc, userData.getPickup_loc());
					editor.putString(Constants.Drop_loc, userData.getDrop_loc());
					editor.putString(Constants.Drop_lat, userData.getDrop_lat());
					editor.putString(Constants.Drop_lng, userData.getDrop_lng());
					editor.apply();
					((TextView) findViewById(R.id.pickUpUserName)).setText(userData.getName());

					locationName.setText(userData.getPickup_loc());
					endLocation.setText(userData.getDrop_loc());
					//((TextView) findViewById(R.id.riding_user_name)).setText(userData.getFirstname());
					RidingUserId = userData.getRider_id();
					Pickup_loc_name = userData.getPickup_loc();
					Drop_loc_name = userData.getDrop_loc();
					Fare = userData.getFare();
					ride_id = userData.getRide_id();
					otp = userData.getRide_otp();
					UserImg = userData.getUser_image();

					UserpicLat =Double.parseDouble(userData.getPickup_lat());
					UserpicLng =Double.parseDouble(userData.getPickup_lng());
					UserdrpLat =Double.parseDouble(userData.getDrop_lat());
					UserdrpLng =Double.parseDouble(userData.getDrop_lng());
					UserMobileNumber = userData.getPhone_no();

					CircleImageView riding_user_img = (CircleImageView)findViewById(R.id.riding_user_img);
					Picasso.get()
							.load(Uri.parse(userData.getUser_image()))
							.placeholder(R.drawable.user)
							.transform(new CircleTransform())
							.into(riding_user_img);
					reCenter();
					mMap.clear();
					LatLng Pickup = new LatLng(lat,lng);
					mMap.addMarker(new MarkerOptions().position(Pickup).title("Your Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.black_location_pin)));
					mMap.addMarker(new MarkerOptions().position(new LatLng(UserpicLat,UserpicLng)).title("Rider Location.").icon(BitmapDescriptorFactory.fromResource(R.drawable.black_location_pin)));
					mMap.moveCamera(CameraUpdateFactory.newLatLng(Pickup));
					mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
					CameraPosition cameraPosition = new CameraPosition.Builder()
							.target(Pickup)
							.zoom(7)
							.build();
					String directionApiPath = Helper.getUrl(String.valueOf(lat), String.valueOf(lng),
							String.valueOf(UserpicLat), String.valueOf(UserpicLng));
					//Log.d(TAG, "Path " + directionApiPath);
					getDirectionFromDirectionApiServer(directionApiPath);

				}
				else if (user.status.equalsIgnoreCase("arrived")) {
					User.UserDetails userData = user.getUserDetails();

					counttime.setVisibility(View.GONE);
					rideRequest.setVisibility(View.VISIBLE);
					startBottomToTopAnimation(rideRequest);
					finding_trips = (LinearLayout)findViewById(R.id.finding_trips);
					finding_trips.setVisibility(View.GONE);
					side_bar.setVisibility(View.GONE);
					drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
					layout_accept_cancel.setVisibility(View.GONE);
					cancel_acceptRide.setVisibility(View.GONE);
					otp_ConfirmRoot.setVisibility(View.VISIBLE);

					((TextView) findViewById(R.id.pickUpUserName)).setText(userData.getName());

					locationName.setText(userData.getPickup_loc());
					endLocation.setText(userData.getDrop_loc());
					//((TextView) findViewById(R.id.riding_user_name)).setText(userData.getFirstname());

					Fare = userData.getFare();
					otp = userData.getRide_otp();

					UserpicLat =Double.parseDouble(userData.getPickup_lat());
					UserpicLng =Double.parseDouble(userData.getPickup_lng());
					UserdrpLat =Double.parseDouble(userData.getDrop_lat());
					UserdrpLng =Double.parseDouble(userData.getDrop_lng());
					UserMobileNumber = userData.getPhone_no();

					CircleImageView riding_user_img = (CircleImageView)findViewById(R.id.riding_user_img);
					Picasso.get()
							.load(Uri.parse(userData.getUser_image()))
							.placeholder(R.drawable.user)
							.transform(new CircleTransform())
							.into(riding_user_img);
					reCenter();
					mMap.clear();
					LatLng Pickup = new LatLng(lat,lng);
					mMap.addMarker(new MarkerOptions().position(Pickup).title("Your Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.black_location_pin)));
					mMap.addMarker(new MarkerOptions().position(new LatLng(UserpicLat,UserpicLng)).title("Rider Location.").icon(BitmapDescriptorFactory.fromResource(R.drawable.black_location_pin)));
					mMap.moveCamera(CameraUpdateFactory.newLatLng(Pickup));
					mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
					CameraPosition cameraPosition = new CameraPosition.Builder()
							.target(Pickup)
							.zoom(7)
							.build();
					String directionApiPath = Helper.getUrl(String.valueOf(lat), String.valueOf(lng),
							String.valueOf(UserpicLat), String.valueOf(UserpicLng));
					//Log.d(TAG, "Path " + directionApiPath);
					getDirectionFromDirectionApiServer(directionApiPath);

				}else if (user.status.equalsIgnoreCase("started")) {
					User.UserDetails userData = user.getUserDetails();

					counttime.setVisibility(View.GONE);
					rideRequest.setVisibility(View.VISIBLE);
					startBottomToTopAnimation(rideRequest);
					finding_trips = (LinearLayout)findViewById(R.id.finding_trips);
					finding_trips.setVisibility(View.GONE);
					side_bar.setVisibility(View.GONE);
					drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
					layout_accept_cancel.setVisibility(View.GONE);
					cancel_acceptRide.setVisibility(View.GONE);

					((TextView) findViewById(R.id.pickUpUserName)).setText(userData.getName());

					locationName.setText(userData.getPickup_loc());
					endLocation.setText(userData.getDrop_loc());
					//((TextView) findViewById(R.id.riding_user_name)).setText(userData.getFirstname());

					Fare = userData.getFare();
					otp = userData.getRide_otp();

					UserpicLat =Double.parseDouble(userData.getPickup_lat());
					UserpicLng =Double.parseDouble(userData.getPickup_lng());
					UserdrpLat =Double.parseDouble(userData.getDrop_lat());
					UserdrpLng =Double.parseDouble(userData.getDrop_lng());
					UserMobileNumber = userData.getPhone_no();

					CircleImageView riding_user_img = (CircleImageView)findViewById(R.id.riding_user_img);
					Picasso.get()
							.load(Uri.parse(userData.getUser_image()))
							.placeholder(R.drawable.user)
							.transform(new CircleTransform())
							.into(riding_user_img);
					reCenter();
					mMap.clear();
					header_title.setVisibility(View.GONE);
					header_EndTrip_text.setVisibility(View.VISIBLE);
					mMap.clear();
					LatLng Pickup = new LatLng(UserpicLat,UserpicLng);
					mMap.addMarker(new MarkerOptions().position(Pickup).title("Your Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.black_location_pin)));
					mMap.addMarker(new MarkerOptions().position(new LatLng(UserdrpLat,UserdrpLng)).title("Rider Location.").icon(BitmapDescriptorFactory.fromResource(R.drawable.black_location_pin)));
					mMap.moveCamera(CameraUpdateFactory.newLatLng(Pickup));
					mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
					CameraPosition cameraPosition = new CameraPosition.Builder()
							.target(Pickup)
							.zoom(15)
							.build();
					String directionApiPath = Helper.getUrl(String.valueOf(UserpicLat), String.valueOf(UserpicLng),
							String.valueOf(UserdrpLat), String.valueOf(UserdrpLng));
					//Log.d(TAG, "Path " + directionApiPath);
					getDirectionFromDirectionApiServer(directionApiPath);

				}else {
						Toast.makeText(DriverHomeActivity.this, "Could't get details.", Toast.LENGTH_SHORT).show();
				}
			}
			@Override
			public void onFailure(Call<User> call, Throwable t) {
				Toast.makeText(DriverHomeActivity.this, "Check your internet", Toast.LENGTH_SHORT).show();
				//Util.noInternetDialog(ctx, false);

			}
		});
	}

	private void AcceptRide() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String CurrentTime = formatter.format(date);

		Api api = RetrofitClient.getRetrofitInstance().create(Api.class);
		Call<BaseResponse> userCall = api.iHaveArriveToSendDetails(driver_id,RidingUserId,ride_id,latitude,longitude,CurrentTime);
		userCall.enqueue(new Callback<BaseResponse>() {
			@Override
			public void onResponse(Call<BaseResponse> call, retrofit2.Response<BaseResponse> response) {

				BaseResponse baseResponse = response.body();

				if (baseResponse.status.equalsIgnoreCase("true")) {
					countDownTimer.cancel();
					counttime.setVisibility(View.GONE);
					side_bar.setVisibility(View.GONE);
					drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
					SharedPreferences.Editor editor = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE).edit();
					editor.putBoolean(Constants.ONRIDE,true);
					editor.apply();
					//stopAudio();
					mediaPlayer.stop();
					/*mediaPlayer.reset();*/
					mediaPlayer = null;
					//otpConfirmStrt();
					//accept_options.setVisibility(View.VISIBLE);
					//startBottomToTopAnimation(accept_options);
					layout_accept_cancel.setVisibility(View.GONE);
					layout_arrived_cancel.setVisibility(View.VISIBLE);
					startBottomToTopAnimation(layout_arrived_cancel);
					reCenter();
					timerAsync2.cancel();

					drivertracking = new Timer();
					timerTaskAsync = new TimerTask() {
						@Override
						public void run() {
							runOnUiThread(new Runnable() {
								@Override public void run() {
									DriverTracking();
								}
							});
						}
					};  drivertracking.schedule(timerTaskAsync, 0,driverTRackingInterval);

					new CustomAlertDialog(ctx).setMessage("Once you reached at rider then swipe to start your journy").setPositiveButton("OK",null).create().show();


				} else if (baseResponse.status.equalsIgnoreCase("accepted")){
					if(mediaPlayer.isPlaying()){
						mediaPlayer.stop();
						mediaPlayer = null;
					}
					countDownTimer.cancel();
					SharedPreferences.Editor editor = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE).edit();
					editor.putBoolean(Constants.ONRIDE,false);
					editor.apply();
					Toast.makeText(DriverHomeActivity.this, baseResponse.getMessage(), Toast.LENGTH_SHORT).show();
					new CustomAlertDialog(ctx).setMessage(baseResponse.getMessage()).setPositiveButton("OK",null).create().show();
					Intent intent = new Intent(ctx, DriverHomeActivity.class);
					startActivity(intent);
					timerAsync2.cancel();
					onStart();

				} else if (baseResponse.status.equalsIgnoreCase("user_cancel")){
					mediaPlayer.stop();
					mediaPlayer = null;
					countDownTimer.cancel();
					timerAsync2.cancel();
					Toast.makeText(DriverHomeActivity.this, baseResponse.getMessage(), Toast.LENGTH_SHORT).show();
					SharedPreferences.Editor editor = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE).edit();
					editor.putBoolean(Constants.ONRIDE,false);
					editor.apply();
					new CustomAlertDialog(ctx).setMessage(baseResponse.getMessage()).setPositiveButton("OK",null).create().show();
					Intent intent = new Intent(ctx, DriverHomeActivity.class);
					startActivity(intent);
					onStart();

				}
				else {
					//new CustomAlertDialog(ctx).setMessage("User cancelled this ride").setPositiveButton("CLOSE", null).create().show();
				}
			}

			@Override
			public void onFailure(Call<BaseResponse> call, Throwable t) {
				Log.d("onFailure", t.toString());
				//Util.noInternetDialog(ctx, false);
				Toast.makeText(DriverHomeActivity.this, "Server error please try after some times.", Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void startBottomToTopAnimation(View view) {
		view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.bottom_to_top));
	}

	public static boolean deleteDir(File dir) {
		if (dir != null && dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}

		return dir.delete();
	}


	private void otpConfirmStrt() {
		ProgressDialog.show();
		cusRotateLoading.start();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String CurrentTime = formatter.format(date);

		Call<BaseResponse> call = myApi.startRide(RidingUserId,ride_id,CurrentTime,latitude,longitude,otp);
		call.enqueue(new Callback<BaseResponse>() {
			@Override
			public void onResponse(Call<BaseResponse> call, retrofit2.Response<BaseResponse> response) {
				BaseResponse user = response.body();

				if (user.status.equalsIgnoreCase("true")) {

					SharedPreferences.Editor editor = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE).edit();
					editor.putBoolean(Constants.ONSTART,true);
					editor.apply();

					ProgressDialog.cancel();
					cusRotateLoading.stop();
					new CustomAlertDialog(ctx).setMessage("Ride started.").setPositiveButton("OK", null).create().show();
					timerAsync2.cancel();
					driverTRackingInterval = 0;
					locationName.setText(Drop_loc_name);
					otp_ConfirmRoot.setVisibility(View.GONE);
					header_title.setVisibility(View.GONE);
					header_EndTrip_text.setVisibility(View.VISIBLE);
					mMap.clear();
					LatLng Pickup = new LatLng(UserpicLat,UserpicLng);
					mMap.addMarker(new MarkerOptions().position(Pickup).title("Your Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.black_location_pin)));
					mMap.addMarker(new MarkerOptions().position(new LatLng(UserdrpLat,UserdrpLng)).title("Rider Location.").icon(BitmapDescriptorFactory.fromResource(R.drawable.black_location_pin)));
					mMap.moveCamera(CameraUpdateFactory.newLatLng(Pickup));
					mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
					CameraPosition cameraPosition = new CameraPosition.Builder()
							.target(Pickup)
							.zoom(15)
							.build();
					String directionApiPath = Helper.getUrl(String.valueOf(UserpicLat), String.valueOf(UserpicLng),
							String.valueOf(UserdrpLat), String.valueOf(UserdrpLng));
					//Log.d(TAG, "Path " + directionApiPath);
					getDirectionFromDirectionApiServer(directionApiPath);

				} else {
					//Toast.makeText(DriverHomeActivity.this, "Start_trip_false.", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Call<BaseResponse> call, Throwable t) {
				Toast.makeText(DriverHomeActivity.this, "Server error Check your internet connection.", Toast.LENGTH_SHORT).show();
				//Util.noInternetDialog(ctx, false);
				ProgressDialog.cancel();
				cusRotateLoading.stop();
			}
		});

	}
	private void NewUserBooking() {

		Call<User> call = myApi.getUsers(driver_id,bike_type_number,latitude,longitude);
		call.enqueue(new Callback<User>() {
			@Override
			public void onResponse(Call<User> call, retrofit2.Response<User> response) {
				User user = response.body();

				if (user.status.equalsIgnoreCase("true")) {
					User.UserDetails userData = user.getUserDetails();

					mediaPlayer.start();

					rideRequest.setVisibility(View.VISIBLE);
					startBottomToTopAnimation(rideRequest);
					finding_trips.setVisibility(View.GONE);
					timerAsync2.cancel();

					SharedPreferences.Editor editor = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE).edit();
					editor.putString(Constants.amount, userData.getFare());
					editor.putString(Constants.RideType, userData.getRide_type());
					editor.putString(Constants.usermobileno, userData.getPhone_no());
					editor.putString(Constants.ride_id, userData.getRide_id());
					editor.putString(Constants.userImg, userData.getUser_image());
					editor.putString(Constants.UserPickup_lat, userData.getPickup_lat());
					editor.putString(Constants.UserPickup_lng, userData.getPickup_lng());
					editor.putString(Constants.Drop_lat, userData.getDrop_lat());
					editor.putString(Constants.Drop_lng, userData.getDrop_lng());
					editor.putString(Constants.Ride_otp, userData.getRide_otp());
					editor.putString(Constants.Pickup_loc, userData.getPickup_loc());
					editor.putString(Constants.Drop_loc, userData.getDrop_loc());
					editor.putString(Constants.rider_id, userData.getRider_id());
					editor.apply();
					((TextView) findViewById(R.id.pickUpUserName)).setText(userData.getName());

					locationName.setText(userData.getPickup_loc());
					endLocation.setText(userData.getDrop_loc());
					//((TextView) findViewById(R.id.riding_user_name)).setText(userData.getFirstname());
					RidingUserId = userData.getRider_id();
					Pickup_loc_name = userData.getPickup_loc();
					Drop_loc_name = userData.getDrop_loc();
					Fare = userData.getFare();
					ride_id = userData.getRide_id();
					otp = userData.getRide_otp();
					UserImg = userData.getUser_image();

					UserpicLat =Double.parseDouble(userData.getPickup_lat());
					UserpicLng =Double.parseDouble(userData.getPickup_lng());
					UserdrpLat =Double.parseDouble(userData.getDrop_lat());
					UserdrpLng =Double.parseDouble(userData.getDrop_lng());
					UserMobileNumber = userData.getPhone_no();

					CircleImageView riding_user_img = (CircleImageView)findViewById(R.id.riding_user_img);
					Picasso.get()
							.load(Uri.parse(userData.getUser_image()))
							.placeholder(R.drawable.user)
							.transform(new CircleTransform())
							.into(riding_user_img);
					reCenter();
					mMap.clear();
					LatLng Pickup = new LatLng(lat,lng);
					mMap.addMarker(new MarkerOptions().position(Pickup).title("Your Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.black_location_pin)));
					mMap.addMarker(new MarkerOptions().position(new LatLng(UserpicLat,UserpicLng)).title("Rider Location.").icon(BitmapDescriptorFactory.fromResource(R.drawable.black_location_pin)));
					mMap.moveCamera(CameraUpdateFactory.newLatLng(Pickup));
					mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
					CameraPosition cameraPosition = new CameraPosition.Builder()
							.target(Pickup)
							.zoom(7)
							.build();
					String directionApiPath = Helper.getUrl(String.valueOf(lat), String.valueOf(lng),
							String.valueOf(UserpicLat), String.valueOf(UserpicLng));
					//Log.d(TAG, "Path " + directionApiPath);
					getDirectionFromDirectionApiServer(directionApiPath);

					timerAsync = new Timer();
					timerTaskAsync = new TimerTask() {
						@Override
						public void run() {
							runOnUiThread(new Runnable() {
								@Override public void run() {
									Usercancel();
									//	Toast.makeText(DriverHomeActivity.this, "repeat after each 1mins", Toast.LENGTH_SHORT).show();
								}
							});
						}
					};  timerAsync.schedule(timerTaskAsync, 0, interval);


					counttime.setVisibility(View.VISIBLE);
					countDownTimer= new CountDownTimer(40000,1000) {
						@Override
						public void onTick(long millisUntilFinished) {

							NumberFormat f = new DecimalFormat("00");
							long hour = (millisUntilFinished / 3600000) % 24;
							long min = (millisUntilFinished / 60000) % 60;
							long sec = (millisUntilFinished / 1000) % 60;
							counttime.setText(f.format(min) + ":" + f.format(sec));
							// counter++;
						}
						@Override
						public void onFinish() {
							counttime.setText("00:00");
							counttime.setVisibility(View.GONE);
							autoCancelTrip();
						}
					}.start();

					//Toast.makeText(DriverHomeActivity.this, "New user has ride booked.", Toast.LENGTH_SHORT).show();

				} else {
					//	Toast.makeText(DriverHomeActivity.this, "Could't get rider.", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Call<User> call, Throwable t) {
				//Toast.makeText(DriverHomeActivity.this, "Check your internet", Toast.LENGTH_SHORT).show();
				//Util.noInternetDialog(ctx, false);

			}
		});
	}
	private void Usercancel() {
		Call<BaseResponse> call = myApi.userCancelledRide(ride_id);
		call.enqueue(new Callback<BaseResponse>() {
			@Override
			public void onResponse(Call<BaseResponse> call, retrofit2.Response<BaseResponse> response) {
				BaseResponse user = response.body();

				if (user.status.equalsIgnoreCase("true")) {
					Toast.makeText(DriverHomeActivity.this, "User Ride cancelled", Toast.LENGTH_SHORT).show();
					timerAsync.cancel();

					if(mediaPlayer.isPlaying()){
						mediaPlayer.stop();
						mediaPlayer = null;
					}
					mediaPlayer = null;
					otp_ConfirmRoot.setVisibility(View.GONE);
					otp_ConfirmRoot.setVisibility(View.GONE);
					mMap.clear();

				} else {
				}
			}

			@Override
			public void onFailure(Call<BaseResponse> call, Throwable t) {
				Toast.makeText(DriverHomeActivity.this, "Server error Check your internet connection.", Toast.LENGTH_SHORT).show();
				//Util.noInternetDialog(ctx, false);
			}
		});
	}

	private void cancelTrip() {
		ProgressDialog.show();
		cusRotateLoading.start();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String CurrentTime = formatter.format(date);
		Call<BaseResponse> call = myApi.cancelRide(driver_id,ride_id,RidingUserId,CurrentTime);
		call.enqueue(new Callback<BaseResponse>() {
			@Override
			public void onResponse(Call<BaseResponse> call, retrofit2.Response<BaseResponse> response) {
				BaseResponse user = response.body();

				if (user.status.equalsIgnoreCase("true")) {
					ProgressDialog.cancel();
					cusRotateLoading.stop();
					SharedPreferences.Editor editor = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE).edit();
					editor.putBoolean(Constants.KEY_ONRIDE,false);
					editor.apply();
					timerAsync.cancel();
					countDownTimer.cancel();
					rideRequest.setVisibility(View.GONE);
					mMap.clear();
					mediaPlayer.stop();
					mediaPlayer = null;
					new CustomAlertDialog(ctx).setMessage(user.getMessage()).setPositiveButton("CLOSE", null).create().show();
					Intent intent = new Intent(ctx, DriverHomeActivity.class);
					startActivity(intent);
					onStart();

				}else if (user.status.equalsIgnoreCase("user_cancel")){
					timerAsync.cancel();
					countDownTimer.cancel();
					rideRequest.setVisibility(View.GONE);
					mMap.clear();
					mediaPlayer.stop();
					mediaPlayer = null;
					new CustomAlertDialog(ctx).setMessage(user.getMessage()).setPositiveButton("CLOSE", null).create().show();
					Intent intent = new Intent(ctx, DriverHomeActivity.class);
					startActivity(intent);
					onStart();
				}
				else {
					//	Toast.makeText(DriverHomeActivity.this, "Cancel_trip_false.", Toast.LENGTH_SHORT).show();
				}
			}
			@Override
			public void onFailure(Call<BaseResponse> call, Throwable t) {
				Toast.makeText(DriverHomeActivity.this, "Server error Check your internet connection.", Toast.LENGTH_SHORT).show();
				//Util.noInternetDialog(ctx, false);
				ProgressDialog.cancel();
				cusRotateLoading.stop();
			}
		});
	}


	private void endTrip() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String CurrentTime = formatter.format(date);

		SharedPreferences prefs = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE);
		RideType = prefs.getString(Constants.RideType, "");

		/*String drop_lat=Double.toString(UserdrpLat);
		String drop_lng=Double.toString(UserdrpLng);

		String pic_lat=Double.toString(UserpicLng);
		String pic_lng=Double.toString(UserpicLng);*/

		Api api = RetrofitClient.getRetrofitInstance().create(Api.class);
		Call<EndTripResponse> call = api.endTrip(RideType,driver_id,ride_id,latitude,longitude,CurrentTime);
		call.enqueue(new Callback<EndTripResponse>() {
			@Override
			public void onResponse(Call<EndTripResponse> call, retrofit2.Response<EndTripResponse> response) {
				EndTripResponse users = response.body();
				if (users.status.equalsIgnoreCase("true")) {

					EndTripResponse.EndTrip data = users.getCloseData();
					//	Toast.makeText(DriverHomeActivity.this, "endTrip success", Toast.LENGTH_SHORT).show();
					layout_complete.setVisibility(View.GONE);
					TotoalEarning ="0" ;
					timerAsyncOnline.cancel();
					SharedPreferences.Editor editor = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE).edit();
					editor.putBoolean(Constants.ONSTART,false);
					editor.putBoolean(Constants.ONRIDE,false);
					editor.putString(Constants.amount, data.getTrip_amount());
					editor.putString(Constants.ToatlFareAmount, data.getTotalEarning());
					editor.putString(Constants.OnlineHours, data.getTrip_hour());
					editor.apply();
					//amoutPayAlarm();
					startActivity(new Intent(DriverHomeActivity.this, TakePaymentActivity.class));
					interval = 0;
					driverSearching = 0;
					timerAsyncOnline.cancel();
					driverTRackingInterval = 0;

				} else {

					//	Toast.makeText(DriverHomeActivity.this, "endTrip false", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Call<EndTripResponse> call, Throwable t) {
				Log.d("onFailure", t.toString());

				//	Toast.makeText(DriverHomeActivity.this, "Server error Check your internet connection.", Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void amoutPayAlarm() {
		am = (AlarmManager) DriverHomeActivity.this.getSystemService(Context.ALARM_SERVICE);
		cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 20);
		cal.set(Calendar.MINUTE, 0);
		cal.add(Calendar.SECOND, 0);
		Intent intent = new Intent(DriverHomeActivity.this, AlarmReceiver.class);
		sender = PendingIntent.getBroadcast(DriverHomeActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		am.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),1000 * 60 * 5, sender);
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
		}
		return true;
	}
	private boolean canAskPermission() {
		return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
	}

	private void offline() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String CurrentTime = formatter.format(date);
		Api api = RetrofitClient.getRetrofitInstance().create(Api.class);
		Call<BaseResponse> userCall = api.goOffline(driver_id,CurrentTime);

		userCall.enqueue(new Callback<BaseResponse>() {
			@Override
			public void onResponse(Call<BaseResponse> call, retrofit2.Response<BaseResponse> response) {

				BaseResponse baseResponse = response.body();

				if (baseResponse.status.equalsIgnoreCase("true")) {
					//	Toast.makeText(getBaseContext(), "Now you are offline", Toast.LENGTH_LONG).show();
					timerAsyncOnline.cancel();
					onlineInterval = 0;
					timerAsync2.cancel();
					timerAsyncOnline.purge();
					timerTaskAsync.cancel();
					startActivity(new Intent(DriverHomeActivity.this, GoOnlineDriverActivity.class));
					finish();
				} else {

					new CustomAlertDialog(ctx).setMessage("You are not allowed to go offline now.").setPositiveButton("CLOSE", null).create().show();
				}
			}

			@Override
			public void onFailure(Call<BaseResponse> call, Throwable t) {
				Log.d("onFailure", t.toString());
				Util.noInternetDialog(ctx, false);
			}
		});
	}

	private void LogOutoffline() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String CurrentTime = formatter.format(date);
		Api api = RetrofitClient.getRetrofitInstance().create(Api.class);
		Call<BaseResponse> userCall = api.goOffline(driver_id,CurrentTime);

		userCall.enqueue(new Callback<BaseResponse>() {
			@Override
			public void onResponse(Call<BaseResponse> call, retrofit2.Response<BaseResponse> response) {

				BaseResponse baseResponse = response.body();

				if (baseResponse.status.equalsIgnoreCase("true")) {
					//	Toast.makeText(getBaseContext(), "Now you are offline", Toast.LENGTH_LONG).show();
					timerAsyncOnline.cancel();
					timerAsync2.cancel();
					timerAsyncOnline.purge();
					timerTaskAsync.cancel();
					startActivity(new Intent(DriverHomeActivity.this, SplashActivity.class));
					finish();
				} else {

					new CustomAlertDialog(ctx).setMessage("Logout currently not allowed.").setPositiveButton("CLOSE", null).create().show();
				}
			}

			@Override
			public void onFailure(Call<BaseResponse> call, Throwable t) {
				Log.d("onFailure", t.toString());
				Util.noInternetDialog(ctx, false);
				Toast.makeText(DriverHomeActivity.this, "Check your internet", Toast.LENGTH_SHORT).show();
			}
		});
	}

	BroadcastReceiver chatReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String msg = intent.getStringExtra("msg");
			chatView.addMessage(new ChatMessage(msg, System.currentTimeMillis(), ChatMessage.Type.RECEIVED));
			chatDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
			try {
				acceptedFare = Float.parseFloat(msg);
			} catch (Exception ignored) {
			}
			if (msg.equals("PRICE ACCEPTED.")) {
				chatDialog.getButton(AlertDialog.BUTTON_POSITIVE).performClick();
			}
		}
	};

	int dp20 = 0;

	@Override
	public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;
		mapStyle = MapStyleOptions.loadRawResourceStyle(this,R.raw.style_json_silver);
		mMap.setMapStyle(mapStyle);
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}
		this.mMap.getUiSettings().setMyLocationButtonEnabled(false);
		this.mMap.setMyLocationEnabled(true);

		dp20 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
		//mMap.setPadding(dp20, dp20, dp20, dp20);

		startService(new Intent(this, BackService.class));
//
//		IntentFilter iFilter = new IntentFilter();
//		iFilter.addAction(BackService.MAP_UPDATE);
//		iFilter.addAction(BackService.NEW_RESPONSE);
//		lbm.registerReceiver(broadcastReceiver, iFilter);

		if (BackService.lat != 0) {
			mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(BackService.lat, BackService.lng), 16));
		}

		/*if (resp != null) {
			handleResponse();
		}*/
	}

	private LocalBroadcastManager lbm = null;

	@Override
	protected void onStart() {
		super.onStart();

		if (checkPermissions(false)) {
			if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				Toast.makeText(ctx, "Please enable your GPS", Toast.LENGTH_LONG).show();
				Intent viewIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivity(viewIntent);
				//reCenter();
			}
		}else{
			OnlineState();
			getLocation();
			reCenter();
			NewUserBooking();
			SharedPreferences pr = PreferenceManager.getDefaultSharedPreferences(this);
			if (!pr.getBoolean("firstTime", false)) {
				// <---- run your one time code here
				finish();
				startActivity(getIntent());

				// mark first time has ran.
				SharedPreferences.Editor editor = pr.edit();
				editor.putBoolean("firstTime", true);
				editor.commit();
			}
		}
		running = true;
		/*if (mMap != null) {
			IntentFilter iFilter = new IntentFilter();
			iFilter.addAction(BackService.MAP_UPDATE);
			iFilter.addAction(BackService.NEW_RESPONSE);
			lbm.registerReceiver(broadcastReceiver, iFilter);
		}*/
        /*int interval= 60 * 1000;
        timerAsync = new Timer();
        TimerTask timerTaskAsync = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override public void run() {
                        OnlineState();
                       // NewUserBooking();
                        Toast.makeText(DriverHomeActivity.this, "repeat after each 1mins", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };  timerAsync.schedule(timerTaskAsync, 0, interval);*/
		/*final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {

			}
		}, 10000);*/
	}
	private static final String[] permission = {
			Manifest.permission.WRITE_EXTERNAL_STORAGE,
			Manifest.permission.READ_EXTERNAL_STORAGE,
			Manifest.permission.ACCESS_COARSE_LOCATION,
			Manifest.permission.ACCESS_FINE_LOCATION,
			Manifest.permission.CAMERA,
			Manifest.permission.ACCESS_NETWORK_STATE,
			Manifest.permission.ACCESS_WIFI_STATE,

	};
	private boolean checkPermissions(boolean checked) {
		if (checked) {
			ActivityCompat.requestPermissions(this, permission, 100);
		} else {
			for (String permiss : permission) {
				if (ActivityCompat.checkSelfPermission(this, permiss) != PackageManager.PERMISSION_GRANTED) {
					ActivityCompat.requestPermissions(this, permission, 100);
					return false;
				}
			}
		}
		return true;
	}
	private void OnlineState() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String CurrentTime = formatter.format(date);
		Api api = RetrofitClient.getRetrofitInstance().create(Api.class);
		Call<BaseResponse> userCall = api.goOnline(driver_id,bike_type_number,latitude,longitude,CurrentTime);
		userCall.enqueue(new Callback<BaseResponse>() {
			@Override
			public void onResponse(Call<BaseResponse> call, retrofit2.Response<BaseResponse> response) {

				BaseResponse baseResponse = response.body();

				if (baseResponse.status.equalsIgnoreCase("true")) {
					//Toast.makeText(DriverHomeActivity.this, "You now in online.", Toast.LENGTH_SHORT).show();

				} else {
					//	Toast.makeText(DriverHomeActivity.this, "online_false", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Call<BaseResponse> call, Throwable t) {
				Log.d("onFailure", t.toString());
				//Toast.makeText(DriverHomeActivity.this, "Check internet.", Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void DriverTracking() {
		Api api = RetrofitClient.getRetrofitInstance().create(Api.class);
		Call<BaseResponse> userCall = api.bikeTracking(driver_id,latitude,longitude,ride_id);
		userCall.enqueue(new Callback<BaseResponse>() {
			@Override
			public void onResponse(Call<BaseResponse> call, retrofit2.Response<BaseResponse> response) {

				BaseResponse baseResponse = response.body();

				if (baseResponse.status.equalsIgnoreCase("true")) {

					//Toast.makeText(DriverHomeActivity.this, "updated.", Toast.LENGTH_SHORT).show();

				} else {
					//Toast.makeText(DriverHomeActivity.this, "update_false", Toast.LENGTH_SHORT).show();
				}
			}
			@Override
			public void onFailure(Call<BaseResponse> call, Throwable t) {
				Log.d("onFailure", t.toString());
				//Toast.makeText(DriverHomeActivity.this, "Check your internet.", Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void autoCancelTrip() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String CurrentTime = formatter.format(date);
		Call<BaseResponse> call = myApi.auto_cancel(RidingUserId,driver_id,ride_id,CurrentTime);
		call.enqueue(new Callback<BaseResponse>() {
			@Override
			public void onResponse(Call<BaseResponse> call, retrofit2.Response<BaseResponse> response) {
				BaseResponse user = response.body();

				if (user.status.equalsIgnoreCase("true")) {
					timerTaskAsync.cancel();
					mediaPlayer.stop();
					mediaPlayer = null;
					new CustomAlertDialog(ctx).setMessage(user.getMessage()).setPositiveButton("CLOSE", null).create().show();
					Toast.makeText(DriverHomeActivity.this, user.getMessage(), Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(ctx, DriverHomeActivity.class);
					startActivity(intent);
					onStart();

				}else if (user.status.equalsIgnoreCase("user_cancel")) {
					timerTaskAsync.cancel();
					Toast.makeText(DriverHomeActivity.this, user.getMessage(), Toast.LENGTH_LONG).show();
					new CustomAlertDialog(ctx).setMessage(user.getMessage()).setPositiveButton("CLOSE", null).create().show();
					Intent intent = new Intent(ctx, DriverHomeActivity.class);
					startActivity(intent);
					onStart();
				}else
				{
					//	Toast.makeText(DriverHomeActivity.this, "Cancel_trip_false.", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Call<BaseResponse> call, Throwable t) {
				Toast.makeText(DriverHomeActivity.this, "Server error Check your internet connection.", Toast.LENGTH_SHORT).show();
			}
		});
	}



	@Override
	protected void onStop() {
		/*if (lbm != null) {
			lbm.unregisterReceiver(broadcastReceiver);
		}*/
		running = false;
		super.onStop();
	}

	public void reCenter() {

		if (mMap == null) {
			return;
		}

		if (bookingData != null) {
			LatLngBounds.Builder builder = new LatLngBounds.Builder();
			builder.include(new LatLng(BackService.lat, BackService.lng));
			builder.include(destMarker.getPosition());
			if (polyline != null) {
				List<LatLng> lats = polyline.getPoints();
				for (LatLng lat : lats) {
					builder.include(lat);
				}
			}
			LatLngBounds bounds = builder.build();
			CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, dp20);
			mMap.animateCamera(cu);
		} else {
			CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(new LatLng(BackService.lat, BackService.lng), 16);
			mMap.animateCamera(cu);
		}
	}

	JSONObject bookingData = null;
	private Marker destMarker = null;

	private void setDestMarker(double lat, double lng, int icon) {
		if (destMarker != null) {
			destMarker.remove();
			destMarker = null;
		}
		destMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).icon(BitmapDescriptorFactory.fromResource(icon)).anchor(.5f, .5f));

		reCenter();
	}

	private void removeDestMarker() {
		if (destMarker != null) {
			destMarker.remove();
			destMarker = null;
		}
	}
//Command the code is testing purpose.....


	private void getDirectionFromDirectionApiServer(String url){
		GsonRequest<DirectionObject> serverRequest = new GsonRequest<DirectionObject>(
				Request.Method.GET,
				url,
				DirectionObject.class,
				createRequestSuccessListener(),
				createRequestErrorListener());
		serverRequest.setRetryPolicy(new DefaultRetryPolicy(
				Helper.MY_SOCKET_TIMEOUT_MS,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(serverRequest);
	}
	private com.android.volley.Response.ErrorListener createRequestErrorListener() {
		return new com.android.volley.Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				error.printStackTrace();
			}
		};
	}
	private com.android.volley.Response.Listener<DirectionObject> createRequestSuccessListener() {
		return new com.android.volley.Response.Listener<DirectionObject>() {
			@Override
			public void onResponse(DirectionObject response) {
               /* try {
                    Log.d("JSON Response", response.toString());*/
				if(response.getStatus().equalsIgnoreCase("OK")){
					List<LatLng> mDirections = getDirectionPolylines(response.getRoutes());
					drawRouteOnMap(mMap, mDirections);


				}
				else{
					//Toast.makeText(HomeActivity.this, "server error", Toast.LENGTH_SHORT).show();
				}

			};
		};
	}
	private void drawRouteOnMap(GoogleMap map, List<LatLng> positions){
		options = new PolylineOptions().width(5).color(Color.BLACK).geodesic(true);
		options.addAll(positions);
		Polyline polyline = map.addPolyline(options);
	}
	private void setRouteDistanceAndDuration(String distance, String duration){
		/* DistanceTextView.setText(distance);*/
	}
	private List<LatLng> getDirectionPolylines(List<RouteObject> routes){
		List<LatLng> directionList = new ArrayList<LatLng>();
		for(RouteObject route : routes){
			List<LegsObject> legs = route.getLegs();
			for(LegsObject leg : legs){
				String routeDistance = leg.getDistance().getText();
				String routeDuration = leg.getDuration().getText();
				setRouteDistanceAndDuration(routeDistance, routeDuration);
				List<StepsObject> steps = leg.getSteps();
				for(StepsObject step : steps){
					PolylineObject polyline = step.getPolyline();
					String points = polyline.getPoints();
					List<LatLng> singlePolyline = decodePoly(points);
					for (LatLng direction : singlePolyline){
						directionList.add(direction);
					}
				}
			}
		}
		return directionList;
	}
	private List<LatLng> decodePoly(String encoded) {
		List<LatLng> poly = new ArrayList<>();
		int index = 0, len = encoded.length();
		int lat = 0, lng = 0;
		while (index < len) {
			int b, shift = 0, result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lat += dlat;
			shift = 0;
			result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lng += dlng;
			LatLng p = new LatLng((((double) lat / 1E5)),
					(((double) lng / 1E5)));
			poly.add(p);
		}
		return poly;
	}

	public void toggleDrawer(View v) {
		drawer.openDrawer(GravityCompat.START);
	}

	private Polyline polyline;
	private List<LatLng> tripPath = null;

	private void createPolyLine() {
		if (polyline != null) {
			if (tripPath != null) {
				polyline.setPoints(tripPath);
				LatLngBounds.Builder builder = new LatLngBounds.Builder();
				for (LatLng p : tripPath) {
					builder.include(new LatLng(p.latitude, p.longitude));
				}
				LatLngBounds bounds = builder.build();
				CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, dp20);
				mMap.animateCamera(cu);
			}
			return;
		}
		if (tripPath != null) {
			PolylineOptions ops = new PolylineOptions();
			LatLngBounds.Builder builder = new LatLngBounds.Builder();
			for (LatLng p : tripPath) {
				ops.add(new LatLng(p.latitude, p.longitude));
				builder.include(new LatLng(p.latitude, p.longitude));
			}
			ops.color(getResources().getColor(R.color.textPrimary));
			ops.width(7);
			polyline = mMap.addPolyline(ops);
			LatLngBounds bounds = builder.build();
			CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, dp20);
			mMap.animateCamera(cu);
		}
	}

	private void destroyPolyLine() {
		if (polyline != null) {
			polyline.remove();
			polyline = null;
		}
	}

	public void calling(View v) {
		Toast.makeText(ctx, "Your call will be initiated in a few moments.", Toast.LENGTH_LONG).show();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent i = new Intent(Intent.ACTION_DIAL);
				i.setData(Uri.parse("tel:"+UserMobileNumber));
				startActivity(i);
			}
		}, 100);
	}

	//my example location code
	@Override
	public void onLocationChanged(Location location) {
		Log.d(TAG, "onLocationChanged");
		updateUI(location);

		/*if (location == null)
			return;

		if (mPositionMarker == null) {

			mPositionMarker = mMap.addMarker(new MarkerOptions()
					.flat(true)
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.car))
					.anchor(0.5f, 0.5f)
					.position(
							new LatLng(location.getLatitude(), location
									.getLongitude())));
		}

		animateMarker(mPositionMarker, location); // Helper method for smooth
		// animation

		mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(location
				.getLatitude(), location.getLongitude())));

		LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
		CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
		mMap.animateCamera(cameraUpdate);
		locationManager.removeUpdates(this);*/

	}

	private void animateMarker(final Marker marker, final Location location) {
		final Handler handler = new Handler();
		final long start = SystemClock.uptimeMillis();
		final LatLng startLatLng = marker.getPosition();
		final double startRotation = marker.getRotation();
		final long duration = 500;

		final Interpolator interpolator = new LinearInterpolator();

		handler.post(new Runnable() {
			@Override
			public void run() {
				long elapsed = SystemClock.uptimeMillis() - start;
				float t = interpolator.getInterpolation((float) elapsed
						/ duration);

				double lng = t * location.getLongitude() + (1 - t)
						* startLatLng.longitude;
				double lat = t * location.getLatitude() + (1 - t)
						* startLatLng.latitude;

				float rotation = (float) (t * location.getBearing() + (1 - t)
						* startRotation);

				marker.setPosition(new LatLng(lat, lng));
				marker.setRotation(rotation);

				if (t < 1.0) {
					// Post again 16ms later.
					handler.postDelayed(this, 16);
				}
			}
		});
	}

	@Override
	public void onStatusChanged(String s, int i, Bundle bundle) {
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
					loc.setLatitude(0.0);
					loc.setLongitude(0.0);
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
	private void stopAudio() {
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
			/*mediaPlayer.reset();*/
			mediaPlayer = null;
		}
	}
	@Override
	public void onBackPressed() {
		new android.app.AlertDialog.Builder(this)
				.setTitle(getResources().getString(R.string.really_exit))
				.setMessage(getResources().getString(R.string.are_you_sure))
				.setNegativeButton(getResources().getString(R.string.cancel), null)
				.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface arg0, int arg1) {
						//  HomeActivity.super.onBackPressed();
						Intent a = new Intent(Intent.ACTION_MAIN);
						a.addCategory(Intent.CATEGORY_HOME);
						a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(a);
					}
				}).create().show();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
