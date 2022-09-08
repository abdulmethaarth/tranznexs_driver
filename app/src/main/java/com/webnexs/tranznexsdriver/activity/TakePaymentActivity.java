package com.webnexs.tranznexsdriver.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.webnexs.tranznexsdriver.Api;
import com.webnexs.tranznexsdriver.pojoClasses.BaseResponse;
import com.webnexs.tranznexsdriver.pojoClasses.CustomFontActivity;
import com.webnexs.tranznexsdriver.pojoClasses.RetrofitClient;
import com.webnexs.tranznexsdriver.R;
import com.webnexs.tranznexsdriver.pojoClasses.Constants;

import retrofit2.Call;
import retrofit2.Callback;

public class TakePaymentActivity extends CustomFontActivity {

	CustomFontActivity ctx = this;
	private int paymentMethod = 0;
	String fare,paymentString;
	String newPayment_type = "0";
	private String amount,TotalEarning,ride_id,payment_method;
	int i= 0;
	Dialog totalAmountDialog,SetUpPayment;
	TextView txt_total,prev_pymentType;
	RelativeLayout deb_crd_card_layout,payment_dialog_layout,layout_cash,change_done_layout;


	@SuppressLint("SetTextI18n")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_take_payment);

        SharedPreferences.Editor editor = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putBoolean(Constants.KEY_ONTRIP_PAYMENT,true);
        editor.apply();

		SharedPreferences prefs = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE);
		amount = prefs.getString(Constants.amount, "");
		ride_id = prefs.getString(Constants.ride_id, "");
		TotalEarning = prefs.getString(Constants.ToatlFareAmount, "");
		payment_method = prefs.getString(Constants.payment_method, "");
		((TextView) findViewById(R.id.fare)).setText("$"+amount);

		prev_pymentType = (TextView) findViewById(R.id.prev_pymentType);
		if(newPayment_type == "1"){
			paymentString = "Card";
			prev_pymentType.setText(paymentString);
		}
		else{
			paymentString = "Cash";
			prev_pymentType.setText(paymentString);
		}

		SetUpPayment = new Dialog(TakePaymentActivity.this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		SetUpPayment.setContentView(R.layout.payment_type_change);
		if (android.os.Build.VERSION.SDK_INT >= 21) {
			SetUpPayment.getWindow().setStatusBarColor(Color.TRANSPARENT);
		}

		SetUpPayment.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialogInterface) {

			}
		});

		deb_crd_card_layout = (RelativeLayout) SetUpPayment.findViewById(R.id.deb_crd_card_layout);
		payment_dialog_layout = (RelativeLayout) SetUpPayment.findViewById(R.id.payment_dialog_layout);
		layout_cash = (RelativeLayout) SetUpPayment.findViewById(R.id.layout_cash);
		change_done_layout = (RelativeLayout) SetUpPayment.findViewById(R.id.change_done_layout);
		deb_crd_card_layout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				deb_crd_card_layout.setBackgroundResource(R.drawable.btn_accent_circle_white_outlined);
				layout_cash.setBackgroundResource(0);
				newPayment_type = "1";
				change_done_layout.setVisibility(View.VISIBLE);
			}
		});
		layout_cash.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				layout_cash.setBackgroundResource(R.drawable.btn_accent_circle_white_outlined);
				deb_crd_card_layout.setBackgroundResource(0);
				newPayment_type = "0";
				change_done_layout.setVisibility(View.VISIBLE);
			}
		});

		payment_dialog_layout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SetUpPayment.cancel();
			}
		});
		change_done_layout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SetUpPayment.cancel();
				Api api = RetrofitClient.getRetrofitInstance().create(Api.class);
				Call<BaseResponse> userCall = api.changePayment(ride_id,newPayment_type);
				userCall.enqueue(new Callback<BaseResponse>() {
					@Override
					public void onResponse(Call<BaseResponse> call, retrofit2.Response<BaseResponse> response) {
						BaseResponse baseResponse = response.body();
						if (baseResponse.status.equalsIgnoreCase("true")) {
							SetUpPayment.cancel();
							if(newPayment_type == "1"){
								paymentString = "Card";
								newPayment_type = "1";
								prev_pymentType.setText(paymentString);
							}
							else{
								paymentString = "Cash";
								newPayment_type = "0";
								prev_pymentType.setText(paymentString);
							}

							Toast.makeText(TakePaymentActivity.this, baseResponse.getMessage(), Toast.LENGTH_LONG).show();
						} else {
							SetUpPayment.cancel();
							Toast.makeText(TakePaymentActivity.this, baseResponse.getMessage(), Toast.LENGTH_LONG).show();
						}
					}
					@Override
					public void onFailure(Call<BaseResponse> call, Throwable t) {
						SetUpPayment.cancel();
						Toast.makeText(TakePaymentActivity.this, "Check your internet", Toast.LENGTH_LONG).show();
					}
				});
			}
		});
		/*totalAmountDialog = new Dialog(TakePaymentActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
		totalAmountDialog.setContentView(R.layout.total_amount_dialog);


		txt_total=(TextView)totalAmountDialog.findViewById(R.id.txt_total);
		//Toast.makeText(this, "Total "+f, Toast.LENGTH_SHORT).show();
		txt_total.setText(TotalEarning);

		RelativeLayout layout_cancel = (RelativeLayout) totalAmountDialog.findViewById(R.id.layout_ok);
		layout_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SharedPreferences.Editor editor = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putBoolean(Constants.KEY_ONTRIP_PAYMENT,false);
			    editor.putString(Constants.amount, "0.00");
			    editor.apply();
				totalAmountDialog.cancel();

				Intent intent = new Intent(TakePaymentActivity.this, DriverHomeActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});
*/
}


	public void changepayment(View v){
		SetUpPayment.show();
	}

	public void done(View v) {
		Intent intent = new Intent(TakePaymentActivity.this, DriverHomeActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		/*final int rate = (int) ((RatingBar) findViewById(R.id.userRating)).getRating();
		if (rate == 0) {
			Toast.makeText(ctx, "Please give rating", Toast.LENGTH_LONG).show();
			return;

		}
		else{*/
			//totalAmountDialog.show();
	//	}
		/*Intent intent = new Intent(ctx, DriverHomeActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);*/

		/*final ProgressDialog pDialog = Util.showWorkingProgress(this, null, true);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response resp = Util.callAndParse(ctx, true, "driver/rate-user", "rating", rate, "payment_mode", paymentMethod, "card_resp", "1");
					if (!resp.error) {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Intent intent = new Intent(ctx, DriverHomeActivity.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
								startActivity(intent);
							}
						});
					}
				} catch (Exception ignored) {
					Util.noInternetDialog(ctx, false);
				}
				pDialog.dismiss();
			}
		}).start();*/
	}

	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this)
				.setTitle(getResources().getString(R.string.really_exit))
				.setMessage(getResources().getString(R.string.are_you_sure))
				.setNegativeButton(getResources().getString(R.string.cancel), null)
				.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface arg0, int arg1) {
						Intent a = new Intent(Intent.ACTION_MAIN);
						a.addCategory(Intent.CATEGORY_HOME);
						a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(a);
					}
				}).create().show();
	}
}
