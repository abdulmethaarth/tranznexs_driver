package com.webnexs.tranznexsdriver.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.webnexs.tranznexsdriver.Api;
import com.webnexs.tranznexsdriver.R;
import com.webnexs.tranznexsdriver.pojoClasses.RetrofitClient;
import com.webnexs.tranznexsdriver.pojoClasses.Users;
import com.webnexs.tranznexsdriver.pojoClasses.Constants;
import com.victor.loading.rotate.RotateLoading;
import com.ybs.countrypicker.CountryPicker;
import com.ybs.countrypicker.CountryPickerListener;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTPActivity extends AppCompatActivity {

	SharedPreferences pref;
	Context ctx;
	Button  btnGenerateOTP,loginWithEmail;
	EditText etPhoneNumber /*etOTP,*/;
	String phoneNumber, otp,TxtViewCountryCode ;
	FirebaseAuth auth;
	LinearLayout otpPasswordLayout;
	PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
	private String verificationCode;
	RotateLoading cusRotateLoading;
	Dialog ProgressDialog;
	TextView edit_country_code;
	Api myApi;
	CountryPicker picker;
	RelativeLayout layout_back_arrow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_otp);
		/*super.initData();*/

		ProgressDialog = new Dialog(OTPActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
		ProgressDialog.setContentView(R.layout.custom_progress_dialog);
		ProgressDialog.setCancelable(false);
		cusRotateLoading = (RotateLoading)ProgressDialog.findViewById(R.id.rotateloading_register);
		//loginWithEmail = (TextView)findViewById(R.id.loginWithEmail);
		otpPasswordLayout = (LinearLayout)findViewById(R.id.otpPasswordLayout);
		final PinEntryEditText otpText = (PinEntryEditText) findViewById(R.id.otp);
		myApi = RetrofitClient.getRetrofitInstance().create(Api.class);

		btnGenerateOTP=(Button) findViewById(R.id.btn_generate_otp);
		loginWithEmail=(Button)findViewById(R.id.loginWithEmail);
		etPhoneNumber=(EditText)findViewById(R.id.et_phone_number);
		layout_back_arrow=(RelativeLayout) findViewById(R.id.layout_back_arrow);
		edit_country_code = (TextView) findViewById(R.id.country_code);
		TxtViewCountryCode = edit_country_code.getText().toString(); //gets you the contents of edit text

		layout_back_arrow.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(OTPActivity.this, LoginOptionActivity.class));
				finish();
			}
		});
		loginWithEmail.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(OTPActivity.this, EmailLogin.class));
			}
		});
		edit_country_code.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				picker = CountryPicker.newInstance("Select Country");  // dialog title
				picker.setListener(new CountryPickerListener() {
					@Override
					public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
						// Implement your code here
						Toast.makeText(OTPActivity.this, "code "+dialCode, Toast.LENGTH_SHORT).show();
						edit_country_code.setText(dialCode);
						picker.dismiss();
					}
				});
				picker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
			}
		});

		StartFirebaseLogin();
		btnGenerateOTP.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				phoneNumber = edit_country_code.getText().toString() + etPhoneNumber.getText().toString();
				//Toast.makeText(OTPActivity.this, "result : "+phoneNumber, Toast.LENGTH_SHORT).show();

				if(etPhoneNumber.getText().toString().isEmpty()){
					Toast.makeText(OTPActivity.this,"Please enter your Mobile number",Toast.LENGTH_SHORT).show();
				}
				else{
					Call<Users> call = myApi.getDriverdetails(etPhoneNumber.getText().toString());
					call.enqueue(new Callback<Users>() {
						@Override
						public void onResponse(Call<Users> call, Response<Users> response) {
							Users users = response.body();

							if (users.status.equalsIgnoreCase("true")) {
								Users.LoginUserDetails userData = users.getDriver_Details();
								//etPhoneNumber.setText(userData.getPhone_no());
								SharedPreferences.Editor editor = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE).edit();
								editor.putString(Constants.driver_id, userData.getDriver_id());
								editor.putString(Constants.firstname, userData.getFirstname());
								editor.putString(Constants.lastname, userData.getLastname());
								editor.putString(Constants.email_id, userData.getEmail_id());
								editor.putString(Constants.mobileno, userData.getPhone_no());
								editor.putString(Constants.address, userData.getAddress());
								//editor.putString(Constants.LicenseNo, userData.getLisence_Number());
								editor.putString(Constants.imageDriver, userData.getUser_image());
								editor.putString(Constants.biketypeName, userData.getModel());
								editor.putString(Constants.biketypeNUmber, userData.getCabtype());
								editor.putString(Constants.Bikeno, userData.getNumber_plate());
								editor.putBoolean(Constants.KEY_LOGGED_IN,true);
								editor.apply();

								new AlertDialog.Builder(OTPActivity.this)
										.setTitle(getResources().getString(R.string.edit_number))
										.setNegativeButton(getResources().getString(R.string.yes), null)
										.setPositiveButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface arg0, int arg1) {
												ProgressDialog.show();
												cusRotateLoading.start();
												PhoneAuthProvider.getInstance().verifyPhoneNumber(
														phoneNumber,                     // Phone number to verify
														60,                           // Timeout duration
														TimeUnit.SECONDS,                // Unit of timeout
														OTPActivity.this,        // Activity (for callback binding)
														mCallback);
											}
										}).create().show();
							}
							else if(users.status.equalsIgnoreCase("perdoc")){
								Users.LoginUserDetails userData = users.getDriver_Details();
								SharedPreferences.Editor editor = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE).edit();
								editor.putString(Constants.driver_id, userData.getDriver_id());
								editor.putString(Constants.mobileno, userData.getPhone_no());
								editor.apply();
								startActivity(new Intent(OTPActivity.this, PersonalDoc.class));
								finish();
							}
							else if(users.status.equalsIgnoreCase("cabdtls")){
								Users.LoginUserDetails userData = users.getDriver_Details();
								SharedPreferences.Editor editor = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE).edit();
								editor.putString(Constants.driver_id, userData.getDriver_id());
								editor.putString(Constants.mobileno, userData.getPhone_no());
								editor.apply();
								startActivity(new Intent(OTPActivity.this, AddVehicle.class));
								finish();
								Toast.makeText(OTPActivity.this, "Please re-enter your detials", Toast.LENGTH_SHORT).show();
							}
							else if (users.status.equalsIgnoreCase("cabdoc")){
								Users.LoginUserDetails userData = users.getDriver_Details();
								SharedPreferences.Editor editor = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE).edit();
								editor.putString(Constants.driver_id, userData.getDriver_id());
								editor.putString(Constants.mobileno, userData.getPhone_no());
								editor.apply();
								startActivity(new Intent(OTPActivity.this, AddVehicleDocs.class));
								finish();
								Toast.makeText(OTPActivity.this, "Please re-enter your detials", Toast.LENGTH_SHORT).show();
							}
							else if (users.status.equalsIgnoreCase("bankdtls")){
								Users.LoginUserDetails userData = users.getDriver_Details();
								SharedPreferences.Editor editor = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE).edit();
								editor.putString(Constants.driver_id, userData.getDriver_id());
								editor.putString(Constants.mobileno, userData.getPhone_no());
								editor.apply();
								startActivity(new Intent(OTPActivity.this, AddBankDetails.class));
								finish();
								Toast.makeText(OTPActivity.this, "Please re-enter your detials", Toast.LENGTH_SHORT).show();
							}
							else if (users.status.equalsIgnoreCase("pending")){
								Snackbar.make(findViewById(android.R.id.content),"Admin aproval Pending...", Snackbar.LENGTH_LONG).show();
								Toast.makeText(OTPActivity.this, "Admin aproval Pending...", Toast.LENGTH_SHORT).show();
							}else if (users.status.equalsIgnoreCase("blocked")){
								Snackbar.make(findViewById(android.R.id.content),"Your Acc has been blocked...", Snackbar.LENGTH_LONG).show();
								Toast.makeText(OTPActivity.this, "Your Acc has been blocked...", Toast.LENGTH_SHORT).show();
							}else {
								Snackbar.make(findViewById(android.R.id.content), "Your not register user.", Snackbar.LENGTH_LONG).show();
								Toast.makeText(OTPActivity.this, "Your not register user.", Toast.LENGTH_SHORT).show();
								startActivity(new Intent(OTPActivity.this, CreateProfile.class));
								finish();
							}
						}

						@Override
						public void onFailure(Call<Users> call, Throwable t) {
							Snackbar.make(findViewById(android.R.id.content),"Check your internet connection...", Snackbar.LENGTH_LONG).show();
							Toast.makeText(OTPActivity.this, "Check your internet connection...", Toast.LENGTH_SHORT).show();

						}
					});
				}

			}
		});
		//  }
       /* else{
            Intent i = new Intent(OneTimePasswordActivity.this, HomeActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            SharedPreferences shredpref=getSharedPreferences("OtpPhoneNumber", 0);
            SharedPreferences.Editor Edit=shredpref.edit();
            Edit.putString("phoneNumber",phoneNumber);
            Edit.commit();
            startActivity(i);

        }
*/

		otpText.requestFocus();
		otpText.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
			@Override
			public void onPinEntered(final CharSequence otp_pin) {
				if (otp_pin.length() == 6) {
					ProgressDialog.show();
					cusRotateLoading.start();
					//this only checking purpose so remove only this commend line
					otp = otpText.getText().toString();
					PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, otp);
					SigninWithPhone(credential);
				}
			}
		});




		ProgressDialog = new Dialog(OTPActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
		ProgressDialog.setContentView(R.layout.custom_progress_dialog);
		ProgressDialog.setCancelable(false);
		cusRotateLoading = (RotateLoading)ProgressDialog.findViewById(R.id.rotateloading_register);

	}

	private void SigninWithPhone(PhoneAuthCredential credential) {
		auth.signInWithCredential(credential)
				.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						if (task.isSuccessful()) {
							ProgressDialog.cancel();
							cusRotateLoading.stop();
							startActivity(new Intent(OTPActivity.this, DriverHomeActivity.class));
							finish();
							SharedPreferences.Editor editor = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE).edit();
							editor.putBoolean(Constants.KEY_LOGGED_IN,true);
							editor.apply();
						} else {
							ProgressDialog.cancel();
							cusRotateLoading.stop();
							Toast.makeText(OTPActivity.this,"Incorrect OTP",Toast.LENGTH_SHORT).show();
						}
					}
				});
	}


	private void StartFirebaseLogin() {
		auth = FirebaseAuth.getInstance();
		mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
			@Override
			public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
				Toast.makeText(OTPActivity.this,"verification completed",Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onVerificationFailed(FirebaseException e) {
				Toast.makeText(OTPActivity.this,"Check your mobile Number",Toast.LENGTH_SHORT).show();
				ProgressDialog.cancel();
				cusRotateLoading.stop();
			}
			@Override
			public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
				otpPasswordLayout.setVisibility(View.VISIBLE);
				ProgressDialog.cancel();
				cusRotateLoading.stop();
				super.onCodeSent(s, forceResendingToken);
				verificationCode = s;
				Toast.makeText(OTPActivity.this,"Code sent",Toast.LENGTH_SHORT).show();
			}
		};
	}

	@Override
	public void onBackPressed() {
		Intent a = new Intent(Intent.ACTION_MAIN);
		a.addCategory(Intent.CATEGORY_HOME);
		a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(a);

	}
}