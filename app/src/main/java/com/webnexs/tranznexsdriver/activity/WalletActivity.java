package com.webnexs.tranznexsdriver.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.webnexs.tranznexsdriver.R;
import com.webnexs.tranznexsdriver.pojoClasses.Response;
import com.webnexs.tranznexsdriver.pojoClasses.Util;

import org.json.JSONObject;

public class WalletActivity extends AppCompatActivity {

	AppCompatActivity ctx;
	String upiId, amount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wallet);

		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle("");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		//((TextView) findViewById(R.id.activityTitle)).setText("Payment");

		ctx = this;

		final ProgressDialog pDialog = Util.showWorkingProgress(ctx, null, true);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response resp = Util.callAndParse(ctx, true, "driver/payment-id");
					if (!resp.error) {
						JSONObject jobj = resp.getJObjData();
						upiId = jobj.getString("upi_id");
						final String balance = jobj.getString("balance");
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								((TextView) findViewById(R.id.walletMoney)).setText("₹" + balance);
							}
						});
					}
				} catch (Exception e) {
					e.printStackTrace();
				//	Util.noInternetDialog(ctx, true);     //this commend only me so remove that
				}
				pDialog.dismiss();
			}
		}).start();
	}

	public void proceed(View v) {
//this commend and try code is only testing purpose...
		Toast.makeText(ctx, "BHIM app is not installed. Please install BHIM app", Toast.LENGTH_LONG).show();
		try {
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=in.org.npci.upiapp")));
		} catch (android.content.ActivityNotFoundException anfe) {
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=in.org.npci.upiapp")));
		}
		/*amount = ((EditText) findViewById(R.id.amount)).getText().toString().trim();
		if (amount.length() > 0) {
			try {
				Uri uri = Uri.parse("upi://pay?pa=" + upiId + "&pn=" + getResources().getString(R.string.app_title).replace(" ", "%20") + "&tn=Add%20money%20to%20wallet&am=" + amount + "&cu=INR");
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				intent.setPackage("in.org.npci.upiapp");
				startActivityForResult(intent, 1000);
			} catch (Exception e){
				e.printStackTrace();
				Toast.makeText(ctx, "BHIM app is not installed. Please install BHIM app", Toast.LENGTH_LONG).show();
				try {
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=in.org.npci.upiapp")));
				} catch (android.content.ActivityNotFoundException anfe) {
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=in.org.npci.upiapp")));
				}
			}
		}*/
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1000) {
			if (resultCode == RESULT_CANCELED) {
				Toast.makeText(ctx, "Payment cancelled", Toast.LENGTH_LONG).show();
			} else {
				if (data.getStringExtra("response").toLowerCase().contains("success")) {
					final ProgressDialog pDialog = Util.showWorkingProgress(ctx, null, true);
					new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								Util.execScript(ctx, "driver/add-money", "amount", amount);
							} catch (Exception e) {
								e.printStackTrace();
							}
							pDialog.dismiss();
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									Toast.makeText(ctx, "Payment successful", Toast.LENGTH_LONG).show();
									startActivity(getIntent());
									finish();
								}
							});
						}
					}).start();
				} else {
					Toast.makeText(ctx, "Payment failed", Toast.LENGTH_LONG).show();
				}
			}
		}
	}
}
