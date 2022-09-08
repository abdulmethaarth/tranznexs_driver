package com.webnexs.tranznexsdriver;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.webnexs.tranznexsdriver.activity.PastTripsActivity;
import com.webnexs.tranznexsdriver.pojoClasses.Constants;
import com.webnexs.tranznexsdriver.pojoClasses.DirectionObject;
import com.webnexs.tranznexsdriver.pojoClasses.GsonRequest;
import com.webnexs.tranznexsdriver.pojoClasses.Helper;
import com.webnexs.tranznexsdriver.pojoClasses.LegsObject;
import com.webnexs.tranznexsdriver.pojoClasses.PolylineObject;
import com.webnexs.tranznexsdriver.pojoClasses.RetrofitClient;
import com.webnexs.tranznexsdriver.pojoClasses.RouteObject;
import com.webnexs.tranznexsdriver.pojoClasses.SingleTripDetails;
import com.webnexs.tranznexsdriver.pojoClasses.StepsObject;
import com.webnexs.tranznexsdriver.pojoClasses.VolleySingleton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;

import static com.google.android.gms.maps.model.JointType.ROUND;

public class SingleTripDetailsActivity extends FragmentActivity implements OnMapReadyCallback {

    LinearLayout layoutFb,layoutMail,layoutWhatsApp;
    FloatingActionButton fabSettings;
    private LocationManager locManager;
    private boolean fabExpanded = false;
    String ride_id,driver_id;
    TextView date,booking_id,fare,paymentType,from_Location,to_Location,
            rider_name,tripFare,sub_total,total,total_payable;
    CircleImageView driverImg,stamp;
    //SimpleRatingBar userRating;
    private GoogleMap mMap;
    RelativeLayout layout_back_arrow;
    PolylineOptions lineOptions,graylineOptions;
    Polyline polyline,greyPolyLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_trip_details);


        ride_id = getIntent().getExtras().getString("ride_id");
        SharedPreferences pref = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE);
        driver_id = pref.getString(Constants.driver_id, "");
        singleRideDetails();

        date = (TextView)findViewById(R.id.date);
        booking_id = (TextView)findViewById(R.id.booking_id);
        fare = (TextView)findViewById(R.id.fare);
        paymentType = (TextView)findViewById(R.id.paymentType);
        from_Location = (TextView)findViewById(R.id.from_Location);
        to_Location = (TextView)findViewById(R.id.to_Location);
        rider_name = (TextView)findViewById(R.id.driver_name);
        tripFare = (TextView)findViewById(R.id.tripFare);
        sub_total = (TextView)findViewById(R.id.sub_total);
        total = (TextView)findViewById(R.id.total);
        total_payable = (TextView)findViewById(R.id.total_payable);
        driverImg = (CircleImageView)findViewById(R.id.driverImg);
        stamp = (CircleImageView)findViewById(R.id.stamp);
        layout_back_arrow = (RelativeLayout) findViewById(R.id.layout_back_arrow);
       // userRating = (SimpleRatingBar)findViewById(R.id.userRating);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        layout_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intnt = new Intent(SingleTripDetailsActivity.this, PastTripsActivity.class);
                startActivity(intnt);
            }
        });

        fabSettings = (FloatingActionButton) this.findViewById(R.id.fabSetting);
        layoutFb = (LinearLayout) this.findViewById(R.id.layoutFb);
        layoutFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        layoutMail = (LinearLayout) this.findViewById(R.id.layoutMail);
        layoutMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        layoutWhatsApp = (LinearLayout) this.findViewById(R.id.layoutWhatsApp);
        layoutWhatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        fabSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fabExpanded == true){
                    closeSubMenusFab();
                } else {
                    openSubMenusFab();
                }
            }
        });
    }

    private void singleRideDetails() {
        Api api = RetrofitClient.getRetrofitInstance().create(Api.class);
        Call<SingleTripDetails> userCall = api.getSingleTripDetail(driver_id,ride_id);
        userCall.enqueue(new Callback<SingleTripDetails>() {
            @Override
            public void onResponse(Call<SingleTripDetails> call, retrofit2.Response<SingleTripDetails> response) {

                SingleTripDetails baseResponse = response.body();
                if (baseResponse.status.equalsIgnoreCase("true")) {
                    SingleTripDetails.SingleTrip data = baseResponse.getDetails();
                    date.setText(data.getAccept_date());
                    booking_id.setText("Booking Id TRNZ_"+ride_id);
                    fare.setText("$"+data.getFare());
                    paymentType.setText(data.getPayment());
                    from_Location.setText(data.getPickup_loc());
                    to_Location.setText(data.getDrop_loc());
                    tripFare.setText("$"+data.getFare());
                    sub_total.setText("$"+data.getFare());
                    total.setText("$"+data.getFare());
                    total_payable.setText("$"+data.getFare());
                    rider_name.setText(data.getRidername());

                    // int rating=Integer.parseInt(data.getRating());
                  /*  userRating.setRating(3);
                    userRating.setEnabled(false);*/
                    Picasso.get()
                            .load(Uri.parse(data.getRider_image()))
                            .placeholder(R.drawable.user)
                            .into(driverImg);
                    Picasso.get()
                            .load(Uri.parse(data.getStatus_stamp()))
                            .placeholder(R.drawable.user)
                            .into(stamp);

                    String Plat = data.getPickup_lat();
                    String Plng = data.getPickup_lng();
                    String Dlat =data.getDrop_lat();
                    String Dlng = data.getDrop_lng();
                    double PickupLatitude=Double.parseDouble(Plat);
                    double PickupLongtude=Double.parseDouble(Plng);
                    double DropLatitude=Double.parseDouble(Dlat);
                    double DropLongtude=Double.parseDouble(Dlng);

                    LatLng Pickup = new LatLng(PickupLatitude,PickupLongtude);
                    mMap.addMarker(new MarkerOptions().position(Pickup).title("Your Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.black_location_pin)));
                    mMap.addMarker(new MarkerOptions().position(new LatLng(DropLatitude,DropLongtude)).title("Rider Location.").icon(BitmapDescriptorFactory.fromResource(R.drawable.black_location_pin)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(Pickup));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(Pickup)
                            .zoom(15)
                            .build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    String directionApiPath = Helper.getUrl(String.valueOf(PickupLatitude), String.valueOf(PickupLongtude),
                            String.valueOf(DropLatitude), String.valueOf(DropLongtude));
                    //Log.d(TAG, "Path " + directionApiPath);
                    getDirectionFromDirectionApiServer(directionApiPath);

                } else {
                    //Toast.makeText(DriverHomeActivity.this, "update_false", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SingleTripDetails> call, Throwable t) {
                Log.d("onFailure", t.toString());
                //Toast.makeText(DriverHomeActivity.this, "Check your internet.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openSubMenusFab() {
        layoutFb.setVisibility(View.VISIBLE);
        layoutMail.setVisibility(View.VISIBLE);
        layoutWhatsApp.setVisibility(View.VISIBLE);
        getWindow().setBackgroundDrawableResource(R.color.main_color_gray);
        fabSettings.setImageResource(R.drawable.fab_close_btn);
        fabExpanded = true;
    }

    private void closeSubMenusFab() {
        layoutFb.setVisibility(View.INVISIBLE);
        layoutMail.setVisibility(View.INVISIBLE);
        layoutWhatsApp.setVisibility(View.INVISIBLE);
        fabSettings.setImageResource(R.drawable.share_btn);
        fabExpanded = false;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
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

        mMap = googleMap;
    }

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
                    Toast.makeText(SingleTripDetailsActivity.this, "server error", Toast.LENGTH_SHORT).show();
                }
            };
        };
    }
    private void drawRouteOnMap(GoogleMap map, List<LatLng> positions){
        graylineOptions = new PolylineOptions();
        graylineOptions.color(Color.GRAY);
        graylineOptions.width(5);
        graylineOptions.startCap(new SquareCap());
        graylineOptions.endCap(new SquareCap());
        graylineOptions.jointType(ROUND);
        graylineOptions.addAll(positions);
        greyPolyLine = mMap.addPolyline(graylineOptions);

        lineOptions = new PolylineOptions();
        lineOptions.width(5);
        lineOptions.color(Color.BLACK).geodesic(true);
        lineOptions.startCap(new SquareCap());
        lineOptions.endCap(new SquareCap());
        lineOptions.jointType(ROUND);
        polyline = map.addPolyline(lineOptions);
        ValueAnimator polylineAnimator = ValueAnimator.ofInt(0, 100);
        polylineAnimator.setDuration(2000);
        polylineAnimator.setInterpolator(new LinearInterpolator());
        polylineAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                List<LatLng> points = greyPolyLine.getPoints();
                int percentValue = (int) valueAnimator.getAnimatedValue();
                int size = points.size();
                int newPoints = (int) (size * (percentValue / 100.0f));
                List<LatLng> p = points.subList(0, newPoints);
                polyline.setPoints(p);
            }

        });
        polylineAnimator.start();
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
    private void setRouteDistanceAndDuration(String distance, String duration){

    }
    private com.android.volley.Response.ErrorListener createRequestErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        };
    }

}
