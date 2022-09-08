package com.webnexs.tranznexsdriver.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.webnexs.tranznexsdriver.Api;
import com.webnexs.tranznexsdriver.CreateResponse;
import com.webnexs.tranznexsdriver.pojoClasses.RetrofitClient;
import com.webnexs.tranznexsdriver.R;
import com.webnexs.tranznexsdriver.pojoClasses.Constants;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateProfile extends AppCompatActivity {

    int value;
    boolean connected = false;

    DatePickerDialog picker;
    Button crt_prof_nxt;
    EditText edit_fname, edit_lname, edit_email, edit_password,edit_address, edit_mobile;
    private ProgressDialog pDialog;
    RadioGroup radioGroup;
    RadioButton male,female;
    Api myApi;
    RelativeLayout layout_back_arrow;
    TextView already_login,edit_dob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        ActivityCompat.requestPermissions(CreateProfile.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                1);

        myApi = RetrofitClient.getRetrofitInstance().create(Api.class);

        layout_back_arrow = (RelativeLayout)findViewById(R.id.layout_back_arrow);
        edit_fname = (EditText) findViewById(R.id.fname);
        edit_lname = (EditText) findViewById(R.id.lname);
        edit_email = (EditText) findViewById(R.id.regEmail);
        edit_password = (EditText) findViewById(R.id.password);
        //licence_img = (CircleImageView) findViewById(R.id.license_img);
        edit_address = (EditText) findViewById(R.id.address);
        edit_mobile = (EditText) findViewById(R.id.mobNo);
        crt_prof_nxt = (Button) findViewById(R.id.crt_prof_nxt);
        radioGroup=(RadioGroup)findViewById(R.id.radiogrup);
        male = (RadioButton)findViewById(R.id.male);
        female = (RadioButton)findViewById(R.id.female);
        already_login = (TextView)findViewById(R.id.already_login);
        edit_dob = (TextView)findViewById(R.id.edit_dob);


        already_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateProfile.this, OTPActivity.class));
            }
        });

        layout_back_arrow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(CreateProfile.this, EmailLogin.class));
                    }
                });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.male:
                        value = 1;
                        Toast.makeText(getBaseContext(), "Male", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.female:
                        value = 2;
                        Toast.makeText(getBaseContext(), "Female", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.other:
                        value = 3;
                        Toast.makeText(getBaseContext(), "Other", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        crt_prof_nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if (edit_fname.getText().toString().isEmpty()) {
                    Snackbar.make(findViewById(android.R.id.content), "Please enter first name", Snackbar.LENGTH_LONG).show();
                    return;
                }else if (edit_lname.getText().toString().isEmpty()) {
                    Snackbar.make(findViewById(android.R.id.content), "Please enter last name", Snackbar.LENGTH_LONG).show();
                    return;
                } else if (edit_email.getText().toString().isEmpty()) {
                    Snackbar.make(findViewById(android.R.id.content), "Please enter email", Snackbar.LENGTH_LONG).show();
                    return;
                } else if(edit_email.getText().toString().trim().length() != 0 && !isValidEmail(edit_email.getText().toString().trim())){
                     Snackbar.make(findViewById(android.R.id.content), "Please enter valid email", Snackbar.LENGTH_LONG).show();
                     return;
                } else if (edit_password.getText().toString().isEmpty()) {
                    Snackbar.make(findViewById(android.R.id.content), "Please enter password", Snackbar.LENGTH_LONG).show();
                    return;
                }else if (edit_address.getText().toString().isEmpty()) {
                    Snackbar.make(findViewById(android.R.id.content), "Please enter address", Snackbar.LENGTH_LONG).show();
                    return;
                } else if (edit_mobile.getText().toString().isEmpty()) {
                    Snackbar.make(findViewById(android.R.id.content), "Please enter mobile number", Snackbar.LENGTH_LONG).show();
                    return;
                }else if (edit_dob.getText().toString().isEmpty()) {
                     Snackbar.make(findViewById(android.R.id.content), "Please enter dob", Snackbar.LENGTH_LONG).show();
                     return;
                }else if (radioGroup.getCheckedRadioButtonId() == -1) {
                    Snackbar.make(findViewById(android.R.id.content), "please select gender", Snackbar.LENGTH_LONG).show();
                    return;
                }
                 else {
                     createProfile();
                 }
            }
        });

        edit_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int pYear = c.get(Calendar.YEAR);
                int pMonth = c.get(Calendar.MONTH);
                int pDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateProfile.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                edit_dob.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, pYear, pMonth, pDay);
                datePickerDialog.show();
            }
        });
    }


    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    private void createProfile() {
        String firstname  = edit_fname.getText().toString();
        String lastname = edit_lname.getText().toString();
        String email_id = edit_email.getText().toString();
        String password = edit_password.getText().toString();
        String address = edit_address.getText().toString();
        String mobileno = edit_mobile.getText().toString();
        String gender = Integer.toString(value);
        String dob = edit_dob.getText().toString();

        pDialog = new ProgressDialog(CreateProfile.this,
                R.style.Theme_AppCompat_DayNight_Dialog);
        pDialog.setIndeterminate(true);
        pDialog.setMessage("Creating Account...");
        pDialog.show();

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;

            Call<CreateResponse> call = myApi.createprofile(firstname,lastname,email_id,password,mobileno,address,dob,gender);
            call.enqueue(new Callback<CreateResponse>() {
                @Override
                public void onResponse(Call<CreateResponse> call, Response<CreateResponse> response) {
                    CreateResponse users = response.body();

                    if (users.status.equalsIgnoreCase("true")) {
                        CreateResponse.CreatingResponse userData = users.getCreatingResponse();
                        pDialog.cancel();
                        SharedPreferences.Editor editor = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putString(Constants.driver_id, userData.getDriver_id());
                        editor.putString(Constants.mobileno, userData.getPhone_no());
                        editor.apply();
                        startActivity(new Intent(CreateProfile.this, PersonalDoc.class));
                        finish();

                    } else {
                        Snackbar.make(findViewById(android.R.id.content), users.getMessage(), Snackbar.LENGTH_LONG).show();
                        pDialog.cancel();
                    }
                }

                @Override
                public void onFailure(Call<CreateResponse> call, Throwable t) {
                    Snackbar.make(findViewById(android.R.id.content), "Check your internet.", Snackbar.LENGTH_LONG).show();
                    pDialog.cancel();
                }
            });
        }
        else{
            connected = false;
            pDialog.cancel();
            Snackbar.make(findViewById(android.R.id.content), "Check your internet connection.", Snackbar.LENGTH_LONG).show();
        }
    }

/*

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


       *//* if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            driverbitmap = (Bitmap) extras.get("data");
            driver_img.setImageBitmap(driverbitmap);

            Uri tempUri = getImageUri(getApplicationContext(), driverbitmap);
            File user_im = new File(getRealPathFromURI(tempUri));
            driver_img_string = String.valueOf(user_im);
        }
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            licensebitmap = (Bitmap) data.getExtras().get("data");
            license_img.setImageBitmap(licensebitmap);

            Uri tempUri = getImageUri(getApplicationContext(), licensebitmap);
            File license_im = new File(getRealPathFromURI(tempUri));
            license_img_string = String.valueOf(license_im);
        }
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            panbitmap = (Bitmap) data.getExtras().get("data");
            pan_img.setImageBitmap(panbitmap);

            Uri tempUri = getImageUri(getApplicationContext(), panbitmap);
            File pan_im = new File(getRealPathFromURI(tempUri));
            pan_img_string = String.valueOf(pan_im);
        }
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            pssbkbitmap = (Bitmap) data.getExtras().get("data");
            bnkpass_img.setImageBitmap(pssbkbitmap);

            Uri tempUri = getImageUri(getApplicationContext(), pssbkbitmap);
            File pass_im = new File(getRealPathFromURI(tempUri));
            pass_img_string = String.valueOf(pass_im);
        }
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            rcbitmap = (Bitmap) data.getExtras().get("data");
            rc_img.setImageBitmap(rcbitmap);

            Uri tempUri = getImageUri(getApplicationContext(), rcbitmap);
            File rc_img = new File(getRealPathFromURI(tempUri));
            rc_img_string = String.valueOf(rc_img);
        }
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            ownerbitmap = (Bitmap) data.getExtras().get("data");
            owner_img.setImageBitmap(ownerbitmap);

            Uri tempUri = getImageUri(getApplicationContext(), ownerbitmap);
            File owner = new File(getRealPathFromURI(tempUri));
            owner_img_string = String.valueOf(owner);
        }
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            pucbitmap = (Bitmap) data.getExtras().get("data");
            puc_img.setImageBitmap(pucbitmap);

            Uri tempUri = getImageUri(getApplicationContext(), pucbitmap);
            File puc = new File(getRealPathFromURI(tempUri));
            puc_img_string = String.valueOf(puc);
        }
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            insurbitmap = (Bitmap) data.getExtras().get("data");
            insurance_img.setImageBitmap(insurbitmap);

            Uri tempUri = getImageUri(getApplicationContext(), insurbitmap);
            File insurance = new File(getRealPathFromURI(tempUri));
            insurance_img_sting = String.valueOf(insurance);
        }
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            govrnIdbitmap = (Bitmap) data.getExtras().get("data");
            govrnmntid_img.setImageBitmap(govrnIdbitmap);

            Uri tempUri = getImageUri(getApplicationContext(), govrnIdbitmap);
            File govId = new File(getRealPathFromURI(tempUri));
            govrnIdString = String.valueOf(govId);
        }
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            vehicleRegbitmap = (Bitmap) data.getExtras().get("data");
            vehile_reg_img.setImageBitmap(vehicleRegbitmap);

            Uri tempUri = getImageUri(getApplicationContext(), vehicleRegbitmap);
            File regVeh = new File(getRealPathFromURI(tempUri));
            vehicleRegString = String.valueOf(regVeh);
        }*//*




    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }*/
}