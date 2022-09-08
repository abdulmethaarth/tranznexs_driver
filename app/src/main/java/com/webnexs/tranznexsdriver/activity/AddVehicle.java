package com.webnexs.tranznexsdriver.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.webnexs.tranznexsdriver.Api;
import com.webnexs.tranznexsdriver.CabType;
import com.webnexs.tranznexsdriver.CabTypes_list;
import com.webnexs.tranznexsdriver.CabsTypeAdapter;
import com.webnexs.tranznexsdriver.CreateResponse;
import com.webnexs.tranznexsdriver.pojoClasses.Constants;
import com.webnexs.tranznexsdriver.pojoClasses.RetrofitClient;
import com.victor.loading.rotate.RotateLoading;
import com.webnexs.tranznexsdriver.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddVehicle extends AppCompatActivity {

    TextView service_type;
    Button add_veh_nxt;
    EditText brand,vehicle_model,number_plate,vehicle_color;
    private ArrayList<CabType> cabTypes;
    private CabsTypeAdapter goCabAdapter;
    RecyclerView cabtype;
    LinearLayout bike_type_layout;
    RotateLoading cusRotateLoading;
    Dialog ProgressDialog;
    Api myApi;
    String driver_id,mobileno;
    String cab_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);

        ProgressDialog = new Dialog(AddVehicle.this, android.R.style.Theme_Translucent_NoTitleBar);
        ProgressDialog.setContentView(R.layout.custom_progress_dialog);
        ProgressDialog.setCancelable(false);
        cusRotateLoading = (RotateLoading)ProgressDialog.findViewById(R.id.rotateloading_register);
        myApi = RetrofitClient.getRetrofitInstance().create(Api.class);

        SharedPreferences prefs = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE);
        driver_id = prefs.getString(Constants.driver_id, "");
        mobileno  = prefs.getString(Constants.mobileno, "");

        service_type = (TextView)findViewById(R.id.service_type);
        brand = (EditText)findViewById(R.id.brand);
        vehicle_model = (EditText)findViewById(R.id.vehicle_model);
        number_plate = (EditText)findViewById(R.id.number_plate);
        vehicle_color = (EditText)findViewById(R.id.vehicle_color);
        add_veh_nxt = (Button)findViewById(R.id.add_veh_nxt);
        bike_type_layout = (LinearLayout) findViewById(R.id.bike_type_layout);
        cabTypeService();

        service_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bike_type_layout.setVisibility(View.VISIBLE);
            }
        });


        add_veh_nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (service_type.getText().toString().isEmpty()) {
                    Snackbar.make(findViewById(android.R.id.content), "Please select your service type", Snackbar.LENGTH_LONG).show();
                    return;
                }else if (vehicle_model.getText().toString().isEmpty()) {
                    Snackbar.make(findViewById(android.R.id.content), "Please enter your model", Snackbar.LENGTH_LONG).show();
                    return;
                }else if (brand.getText().toString().isEmpty()) {
                    Snackbar.make(findViewById(android.R.id.content), "Please enter your brand", Snackbar.LENGTH_LONG).show();
                    return;
                }else if (number_plate.getText().toString().isEmpty()) {
                    Snackbar.make(findViewById(android.R.id.content), "Please enter your vehicle number", Snackbar.LENGTH_LONG).show();
                    return;
                }else if (vehicle_color.getText().toString().isEmpty()) {
                    Snackbar.make(findViewById(android.R.id.content), "Please enter your vehicle color", Snackbar.LENGTH_LONG).show();
                    return;
                }
                else{
                    addVehicleDetails();
                }
            }
        });
    }


    private void addVehicleDetails() {
        String service = cab_id;
        String brandname= brand.getText().toString();
        String model = vehicle_model.getText().toString();
        String number = number_plate.getText().toString();
        String color = vehicle_color.getText().toString();

        Call<CreateResponse> call = myApi.uploadvehicle(driver_id,mobileno,service,brandname,model,number,color);
        call.enqueue(new Callback<CreateResponse>() {
            @Override
            public void onResponse(Call<CreateResponse> call, Response<CreateResponse> response) {
                CreateResponse users = response.body();

                if (users.status.equalsIgnoreCase("true")) {
                    CreateResponse.CreatingResponse userData = users.getCreatingResponse();
                    ProgressDialog.cancel();
                    cusRotateLoading.stop();
                    //etPhoneNumber.setText(userData.getPhone_no());
                    SharedPreferences.Editor editor = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putString(Constants.driver_id, userData.getDriver_id());
                    editor.putString(Constants.mobileno, userData.getPhone_no());
                    editor.apply();
                    startActivity(new Intent(AddVehicle.this, AddVehicleDocs.class));
                    finish();

                } else {
                    Snackbar.make(findViewById(android.R.id.content), users.getMessage(), Snackbar.LENGTH_LONG).show();
                    ProgressDialog.cancel();
                    cusRotateLoading.stop();
                }
            }

            @Override
            public void onFailure(Call<CreateResponse> call, Throwable t) {
                Snackbar.make(findViewById(android.R.id.content), "Check your internet.", Snackbar.LENGTH_LONG).show();
                ProgressDialog.cancel();
                cusRotateLoading.stop();
            }
        });
    }


    private void cabTypeService() {
        Api api = RetrofitClient.getApiService();
        Call<CabTypes_list> call = api.getCabDetails();

        call.enqueue(new Callback<CabTypes_list>() {
            @Override
            public void onResponse(Call<CabTypes_list> call, retrofit2.Response<CabTypes_list> response) {
                if (response.isSuccessful()) {
                    cabTypes = response.body().getCabTypes();
                    cabtype = (RecyclerView) findViewById(R.id.biketypes);
                    goCabAdapter = new CabsTypeAdapter(cabTypes);
                    RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(getApplicationContext());
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AddVehicle.this, LinearLayoutManager.VERTICAL, false);
                    //  GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),3);
                    cabtype.setLayoutManager(linearLayoutManager);
                    cabtype.setItemAnimator(new DefaultItemAnimator());
                    cabtype.setAdapter(goCabAdapter);

                    cabtype.addOnItemTouchListener(new RecyclerTouchListener(AddVehicle.this,
                            cabtype, new ClickListener() {
                        @Override
                        public void onClick(View view, final int position) {
                            cab_id = cabTypes.get(position).getId();
                            String model = cabTypes.get(position).getName();
                            service_type.setText(model);
                            bike_type_layout.setVisibility(View.GONE);

                        }
                        @Override
                        public void onLongClick(View view, int position) {
                        }
                    }));
                }
            }

            @Override
            public void onFailure(Call<CabTypes_list> call, Throwable t) {
                Toast.makeText(AddVehicle.this, "Server connection failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Recycler view clicking functions
    public static interface ClickListener{
        public void onClick(View view,int position);
        public void onLongClick(View view,int position);
    }

    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{
        private ClickListener clicklistener;
        private GestureDetector gestureDetector;
        public RecyclerTouchListener(Context context, final RecyclerView recycleView, final ClickListener clicklistener){
            this.clicklistener=clicklistener;
            gestureDetector=new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
                @Override
                public void onLongPress(MotionEvent e) {
                    View child=recycleView.findChildViewUnder(e.getX(),e.getY());
                    if(child!=null && clicklistener!=null){
                        clicklistener.onLongClick(child,recycleView.getChildAdapterPosition(child));
                    }
                }
            });
        }
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child=rv.findChildViewUnder(e.getX(),e.getY());
            if(child!=null && clicklistener!=null && gestureDetector.onTouchEvent(e)){
                clicklistener.onClick(child,rv.getChildAdapterPosition(child));
            }
            return false;
        }
        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }
        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
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
}
