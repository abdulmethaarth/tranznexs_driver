package com.webnexs.tranznexsdriver.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.webnexs.tranznexsdriver.Api;
import com.webnexs.tranznexsdriver.pojoClasses.Constants;
import com.webnexs.tranznexsdriver.pojoClasses.RetrofitClient;
import com.webnexs.tranznexsdriver.pojoClasses.Users;
import com.victor.loading.rotate.RotateLoading;
import com.webnexs.tranznexsdriver.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmailLogin extends AppCompatActivity {

    EditText edit_username;
    EditText edit_password;
    RelativeLayout layout_forgotPass,layout_back_arrow;
    TextView txt_change_password,txt_forgot_password;
    TextView txt_signin, txt_sign_in_logo;
    LinearLayout layout_login_main;
    Button layout_signin;
    Api myApi;
    Typeface OpenSans_Regular, OpenSans_Bold, regularRoboto, Roboto_Bold;

    Dialog ProgressDialog;
    RotateLoading cusRotateLoading;
    String email,password;

    //Error Alert
    RelativeLayout rlMainView;
    TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_login);

        myApi = RetrofitClient.getRetrofitInstance().create(Api.class);
       /* rlMainView = (RelativeLayout) findViewById(R.id.rlMainView);
        tvTitle = (TextView) findViewById(R.id.tvTitle);*/

        layout_login_main = (LinearLayout) findViewById(R.id.layout_login_main);
        edit_username = (EditText) findViewById(R.id.username);
        edit_password = (EditText) findViewById(R.id.password);
        layout_signin = (Button) findViewById(R.id.layout_signin);
      //  layout_forgotPass = (RelativeLayout) findViewById(R.id.layout_forgotPass);
        layout_back_arrow = (RelativeLayout) findViewById(R.id.layout_back_arrow);

        //txt_forgot_password = (TextView) findViewById(R.id.txt_forgot_password);
        txt_sign_in_logo = (TextView) findViewById(R.id.txt_sign_in_logo);

        ProgressDialog = new Dialog(EmailLogin.this, android.R.style.Theme_Translucent_NoTitleBar);
        ProgressDialog.setContentView(R.layout.custom_progress_dialog);
        ProgressDialog.setCancelable(false);
        cusRotateLoading = (RotateLoading) ProgressDialog.findViewById(R.id.rotateloading_register);

       /* OpenSans_Bold = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Bold_0.ttf");
        OpenSans_Regular = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular_0.ttf");
        regularRoboto = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        Roboto_Bold = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");*/

      /*  edit_username.setTypeface(OpenSans_Regular);
        edit_password.setTypeface(OpenSans_Regular);
        txt_forgot_password.setTypeface(OpenSans_Regular);
        txt_sign_in_logo.setTypeface(Roboto_Bold);*/

        layout_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EmailLogin.this, OTPActivity.class));
            }
        });

       /* layout_forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmailLogin.this, ForgotActivity.class);
                startActivity(intent);
            }
        });*/

        layout_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edit_username.getText().toString().isEmpty()) {

                    Toast.makeText(EmailLogin.this, "Please enter email", Toast.LENGTH_SHORT).show();

                    return;
                }
                else if(edit_username.getText().toString().trim().length() != 0 && !isValidEmail(edit_username.getText().toString().trim())){
                    Toast.makeText(EmailLogin.this, "Please enter valid email", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (edit_password.getText().toString().isEmpty()) {

                    Toast.makeText(EmailLogin.this, "Please enter password", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    email = edit_username.getText().toString();
                    password = edit_password.getText().toString();
                    ProgressDialog.show();
                    cusRotateLoading.start();
                    Call<Users> call = myApi.login(email,password);
                    call.enqueue(new Callback<Users>() {
                        @Override
                        public void onResponse(Call<Users> call, Response<Users> response) {
                            Users users = response.body();
                            if (users.status.equalsIgnoreCase("true")) {
                                ProgressDialog.cancel();
                                cusRotateLoading.stop();
                                Users.LoginUserDetails userData = users.getDriver_Details();
                                //etPhoneNumber.setText(userData.getPhone_no());
                                SharedPreferences.Editor editor = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE).edit();
                                editor.putString(Constants.driver_id, userData.getDriver_id());
                                editor.putString(Constants.firstname, userData.getFirstname());
                                editor.putString(Constants.lastname, userData.getLastname());
                                editor.putString(Constants.email_id, userData.getEmail_id());
                                editor.putString(Constants.mobileno, userData.getPhone_no());
                                editor.putString(Constants.address, userData.getAddress());
                                editor.putString(Constants.imageDriver, userData.getUser_image());
                                editor.putString(Constants.biketypeName, userData.getModel());
                                editor.putString(Constants.biketypeNUmber, userData.getCabtype());
                                editor.putString(Constants.Bikeno, userData.getNumber_plate());
                                editor.putBoolean(Constants.KEY_EMAIL_LOGGED_IN,true);
                                editor.apply();
                                startActivity(new Intent(EmailLogin.this, DriverHomeActivity.class));

                            } else if(users.status.equalsIgnoreCase("perdoc")){
                                Users.LoginUserDetails userData = users.getDriver_Details();
                                SharedPreferences.Editor editor = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE).edit();
                                editor.putString(Constants.driver_id, userData.getDriver_id());
                                editor.putString(Constants.mobileno, userData.getPhone_no());
                                editor.apply();
                                startActivity(new Intent(EmailLogin.this, PersonalDoc.class));
                                finish();
                            }
                            else if(users.status.equalsIgnoreCase("cabdtls")){
                                Users.LoginUserDetails userData = users.getDriver_Details();
                                SharedPreferences.Editor editor = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE).edit();
                                editor.putString(Constants.driver_id, userData.getDriver_id());
                                editor.putString(Constants.mobileno, userData.getPhone_no());
                                editor.apply();
                                startActivity(new Intent(EmailLogin.this, AddVehicle.class));
                                finish();
                                Toast.makeText(EmailLogin.this, "Please re-enter your detials", Toast.LENGTH_SHORT).show();
                            }
                            else if (users.status.equalsIgnoreCase("cabdoc")){
                                Users.LoginUserDetails userData = users.getDriver_Details();
                                SharedPreferences.Editor editor = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE).edit();
                                editor.putString(Constants.driver_id, userData.getDriver_id());
                                editor.putString(Constants.mobileno, userData.getPhone_no());
                                editor.apply();
                                startActivity(new Intent(EmailLogin.this, AddVehicleDocs.class));
                                finish();
                                Toast.makeText(EmailLogin.this, "Please re-enter your detials", Toast.LENGTH_SHORT).show();
                            }
                            else if (users.status.equalsIgnoreCase("bankdtls")){
                                Users.LoginUserDetails userData = users.getDriver_Details();
                                SharedPreferences.Editor editor = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE).edit();
                                editor.putString(Constants.driver_id, userData.getDriver_id());
                                editor.putString(Constants.mobileno, userData.getPhone_no());
                                editor.apply();
                                startActivity(new Intent(EmailLogin.this, AddBankDetails.class));
                                finish();
                                Toast.makeText(EmailLogin.this, "Please re-enter your detials", Toast.LENGTH_SHORT).show();
                            }
                            else if (users.status.equalsIgnoreCase("pending")){
                                ProgressDialog.cancel();
                                cusRotateLoading.stop();
                                Snackbar.make(findViewById(android.R.id.content),"Admin aproval Pending...", Snackbar.LENGTH_LONG).show();
                                Toast.makeText(EmailLogin.this, "Admin aproval Pending...", Toast.LENGTH_SHORT).show();
                            }else if (users.status.equalsIgnoreCase("blocked")){
                                ProgressDialog.cancel();
                                cusRotateLoading.stop();
                                Snackbar.make(findViewById(android.R.id.content),"Your Acc has been blocked...", Snackbar.LENGTH_LONG).show();
                                Toast.makeText(EmailLogin.this, "Your Acc has been blocked...", Toast.LENGTH_SHORT).show();
                            }else if (users.status.equalsIgnoreCase("wrong_pass")){
                                ProgressDialog.cancel();
                                cusRotateLoading.stop();
                                Snackbar.make(findViewById(android.R.id.content),"Wrong password", Snackbar.LENGTH_LONG).show();
                                Toast.makeText(EmailLogin.this, "Wrong password", Toast.LENGTH_SHORT).show();
                            }else {
                                Snackbar.make(findViewById(android.R.id.content), "Your not register user.", Snackbar.LENGTH_LONG).show();
                                Toast.makeText(EmailLogin.this, "Your not register user.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(EmailLogin.this, CreateProfile.class));
                                finish();
                            }


                            // loader(false);
                        }

                        @Override
                        public void onFailure(Call<Users> call, Throwable t) {
                            ProgressDialog.cancel();
                            cusRotateLoading.stop();
                            Toast.makeText(EmailLogin.this, "Sorry something went wrong...", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }


        });
    }

    @Override
    public void onBackPressed() {

      /*  if(slidingMenu.isMenuShowing()){
            slidingMenu.toggle();
        }else {*/
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
    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
