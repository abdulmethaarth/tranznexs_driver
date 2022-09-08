package com.webnexs.tranznexsdriver.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.webnexs.tranznexsdriver.Api;
import com.webnexs.tranznexsdriver.SingleTripDetailsActivity;
import com.webnexs.tranznexsdriver.pojoClasses.CustomFontActivity;
import com.webnexs.tranznexsdriver.pojoClasses.OvertripDetailsList;
import com.webnexs.tranznexsdriver.R;
import com.webnexs.tranznexsdriver.pojoClasses.RetrofitClient;
import com.webnexs.tranznexsdriver.pojoClasses.TripDetails;
import com.webnexs.tranznexsdriver.adapter.TripDetailsAdapter;
import com.webnexs.tranznexsdriver.pojoClasses.Constants;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PastTripsActivity extends CustomFontActivity {

	private CustomFontActivity ctx = this;
	private ArrayList<TripDetails> tripDetailsList;
	private RecyclerView recyclerView;
	private TripDetailsAdapter eAdapter;
	Dialog ProgressDialog;
	TextView noRecords;
	RotateLoading cusRotateLoading;
	String driver_id,ride_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_past_trips);

		noRecords = (TextView)findViewById(R.id.noRecords);
		ProgressDialog = new Dialog(PastTripsActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
		ProgressDialog.setContentView(R.layout.custom_progress_dialog);
		ProgressDialog.setCancelable(false);
		cusRotateLoading = (RotateLoading)ProgressDialog.findViewById(R.id.rotateloading_register);
		ProgressDialog.show();
		cusRotateLoading.start();
		Api api = RetrofitClient.getApiService();


		SharedPreferences prefs = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE);
		driver_id = prefs.getString(Constants.driver_id, "");
		ride_id = prefs.getString(Constants.ride_id, "");

		Call<OvertripDetailsList> call = api.getTripDetails(driver_id);

		call.enqueue(new Callback<OvertripDetailsList>() {
			@Override
			public void onResponse(Call<OvertripDetailsList> call, Response<OvertripDetailsList> response) {
				OvertripDetailsList users = response.body();

				if (users.status.equalsIgnoreCase("true")) {
					ProgressDialog.cancel();
					cusRotateLoading.stop();
					tripDetailsList = response.body().getTripDetails();
					recyclerView = (RecyclerView) findViewById(R.id.recycle_all_trip);
					eAdapter = new TripDetailsAdapter(tripDetailsList);
					RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(getApplicationContext());
					LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PastTripsActivity.this, LinearLayoutManager.VERTICAL, false);
					recyclerView.setLayoutManager(linearLayoutManager);
					recyclerView.setItemAnimator(new DefaultItemAnimator());
					recyclerView.setAdapter(eAdapter);
					recyclerView.addOnItemTouchListener(new PastTripsActivity.RecyclerTouchListener(PastTripsActivity.this,
							recyclerView, new PastTripsActivity.ClickListener() {
						@Override
						public void onClick(View view, final int position) {
							ride_id = tripDetailsList.get(position).getRide_id();
							Intent intent = new Intent(PastTripsActivity.this, SingleTripDetailsActivity.class);
							intent.putExtra("ride_id",ride_id);
							startActivity(intent);
						}
						@Override
						public void onLongClick(View view, int position) {
						}
					}));
				}
				else{
					ProgressDialog.cancel();
					cusRotateLoading.stop();
					noRecords.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onFailure(Call<OvertripDetailsList> call, Throwable t) {
				ProgressDialog.cancel();
				cusRotateLoading.stop();
				noRecords.setVisibility(View.VISIBLE);
				Snackbar.make(findViewById(android.R.id.content), "Check your internet.", Snackbar.LENGTH_LONG).show();
			}
		});
	}

	public static interface ClickListener{
		public void onClick(View view,int position);
		public void onLongClick(View view,int position);
	}


	class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{
		private PastTripsActivity.ClickListener clicklistener;
		private GestureDetector gestureDetector;
		public RecyclerTouchListener(Context context, final RecyclerView recycleView, final PastTripsActivity.ClickListener clicklistener){
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
}
