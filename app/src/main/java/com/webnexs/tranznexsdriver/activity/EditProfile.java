package com.webnexs.tranznexsdriver.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.webnexs.tranznexsdriver.pojoClasses.CircleTransform;
import com.webnexs.tranznexsdriver.pojoClasses.Constants;
import com.webnexs.tranznexsdriver.pojoClasses.ImageFilePath;
import com.squareup.picasso.Picasso;

import net.gotev.uploadservice.MultipartUploadRequest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.UUID;
import com.webnexs.tranznexsdriver.R;

import de.hdodenhof.circleimageview.CircleImageView;


public class EditProfile extends AppCompatActivity {

    EditText edit_fname, edit_lname, edit_email, edit_password, edit_country, edit_state, edit_city, edit_address, edit_mobile, edit_license, edit_pincode;
    String firstname,lastname,email_id,driver_id,address,mobileno,gender,country,state,city,pin_code,lisence_no,user_img_Camera,user_img_gallery,license_img_camera,license_img_gallery,licenseNo,mCurrentPhotoPath;
    Bitmap userbitmap,licensebitmap;
    boolean connected = false;
    CircleImageView user_img, licence_img;
    Dialog OpenCameraDialog;
    private static final int CAMERA_REQUEST = 1888;
    private static final int CAMERA_REQUESTS = 1889;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int MY_CAMERA_PERMISSION_CODES = 101;
    private static int RESULT_LOAD_IMAGE = 1;
    private static int RESULT_LOAD_IMAGES = 0;
    Button save;
    RelativeLayout edit,layout_back_arrow;
    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        ActivityCompat.requestPermissions(EditProfile.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                1);

        SharedPreferences prefs = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE);
        driver_id = prefs.getString(Constants.driver_id, "");
        mobileno  = prefs.getString(Constants.mobileno, "");
        firstname = prefs.getString(Constants.firstname, "");
        lastname = prefs.getString(Constants.lastname, "");
        email_id = prefs.getString(Constants.email_id, "");
        address = prefs.getString(Constants.address, "");
        String Driverimage = prefs.getString(Constants.imageDriver, "");

        Toast.makeText(this, "value "+driver_id, Toast.LENGTH_SHORT).show();

        edit_fname = (EditText) findViewById(R.id.fname);
        edit_fname.setText(firstname);
        edit_fname.setEnabled(false);

        edit_lname = (EditText) findViewById(R.id.lname);
        edit_lname.setText(lastname);
        edit_lname.setEnabled(false);

        edit_email = (EditText) findViewById(R.id.regEmail);
        edit_email.setText(email_id);
        edit_email.setEnabled(false);

        edit_mobile = (EditText) findViewById(R.id.mobNo);
        edit_mobile.setText(mobileno);
        edit_mobile.setEnabled(false);

        user_img = (CircleImageView) findViewById(R.id.user_img);
        Picasso.get()
                .load(Uri.parse(prefs.getString("imageDriver", "")))
                .placeholder(R.drawable.driver_icon)
                .transform(new CircleTransform())
                .into(user_img);

        save = (Button)findViewById(R.id.btn_save);
        edit = (RelativeLayout) findViewById(R.id.edit);
        layout_back_arrow = (RelativeLayout) findViewById(R.id.layout_back_arrow);
        layout_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfile.this, DriverHomeActivity.class));
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_email.setEnabled(true);
                edit_fname.setEnabled(true);
                edit_lname.setEnabled(true);
                edit_mobile.setEnabled(true);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userbitmap == null) {
                    Snackbar.make(findViewById(android.R.id.content), "Please change your image", Snackbar.LENGTH_LONG).show();
                    return;
                }/*
                else if (edit_fname.getText().toString().isEmpty()) {
                    Snackbar.make(findViewById(android.R.id.content), "Please enter first name", Snackbar.LENGTH_LONG).show();
                    return;
                }else if (edit_lname.getText().toString().isEmpty()) {
                    Snackbar.make(findViewById(android.R.id.content), "Please enter last name", Snackbar.LENGTH_LONG).show();
                    return;
                } else if (edit_email.getText().toString().isEmpty()) {
                    Snackbar.make(findViewById(android.R.id.content), "Please enter email", Snackbar.LENGTH_LONG).show();
                    return;
                }  else if (edit_country.getText().toString().isEmpty()) {
                    Snackbar.make(findViewById(android.R.id.content), "Please enter country name", Snackbar.LENGTH_LONG).show();
                    return;
                } else if (edit_state.getText().toString().isEmpty()) {
                    Snackbar.make(findViewById(android.R.id.content), "Please enter state", Snackbar.LENGTH_LONG).show();
                    return;
                } else if (edit_city.getText().toString().isEmpty()) {
                    Snackbar.make(findViewById(android.R.id.content), "Please enter city", Snackbar.LENGTH_LONG).show();
                    return;
                } else if (edit_address.getText().toString().isEmpty()) {
                    Snackbar.make(findViewById(android.R.id.content), "Please enter address", Snackbar.LENGTH_LONG).show();
                    return;
                } else if (edit_mobile.getText().toString().isEmpty()) {
                    Snackbar.make(findViewById(android.R.id.content), "Please enter active mobile number", Snackbar.LENGTH_LONG).show();
                    return;
                } else if (licensebitmap == null && license_img_gallery == null) {
                    Snackbar.make(findViewById(android.R.id.content), "Please provide current licence image", Snackbar.LENGTH_LONG).show();
                    return;
                }else if (edit_license.getText().toString().isEmpty()) {
                    Snackbar.make(findViewById(android.R.id.content), "Please enter license number", Snackbar.LENGTH_LONG).show();
                    return;
                }*/
                else {
                    updateProfile();
                }
            }
        });

        user_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenCameraDialog = new Dialog(EditProfile.this, android.R.style.Theme_Translucent_NoTitleBar);
                OpenCameraDialog.setContentView(R.layout.camera_dialog_layout);

                TextView layout_open_camera = (TextView) OpenCameraDialog.findViewById(R.id.layout_open_camera);
                layout_open_camera.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(View v) {

                        OpenCameraDialog.cancel();
                       /* Intent ci = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        mCapturedImageURI = getImageUri();
                        ci.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
                        startActivityForResult(ci, REQUEST_CAMERA);
*/
                        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                        } else {
                            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, CAMERA_REQUEST);
                        }
                    }
                });

                TextView layout_open_gallery = (TextView) OpenCameraDialog.findViewById(R.id.layout_open_gallery);
                layout_open_gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        OpenCameraDialog.cancel();
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(intent, RESULT_LOAD_IMAGE);
                    }
                });

                RelativeLayout layout_open_cancel = (RelativeLayout) OpenCameraDialog.findViewById(R.id.layout_open_cancel);
                layout_open_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OpenCameraDialog.cancel();
                    }
                });

                OpenCameraDialog.show();
            }
        });


    }



    private void updateProfile() {

        firstname = edit_fname.getText().toString();
        lastname = edit_lname.getText().toString();
        email_id = edit_email.getText().toString();
        mobileno = edit_mobile.getText().toString();
      //  lisence_no  = edit_license.getText().toString();


        pDialog = new ProgressDialog(EditProfile.this,
                R.style.Theme_AppCompat_DayNight_Dialog);
        pDialog.setIndeterminate(true);
        pDialog.setMessage("Profile updating");
        pDialog.setCancelable(false);
       /* pDialog.show();*/

        String image = String.valueOf(user_img_Camera);
        //String lisence_image = String.valueOf(license_img_camera);

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;

            try {
                String uploadId = UUID.randomUUID().toString();
                //Creating a multi part request
                new MultipartUploadRequest(this, uploadId, Constants.EDIT_PROFILE)
                        .addFileToUpload(image, "user_image") //Adding file
                        .addParameter("user_id", driver_id)
                        .addParameter("firstname", firstname)
                        .addParameter("lastname", lastname)
                        .addParameter("email_id", email_id)
                        .addParameter("driver_id", driver_id)
                        .addParameter("phone_no", mobileno)//Adding text parameter to the request
                        .setMaxRetries(3)
                        .startUpload();//Starting the upload

                pDialog.cancel();
                SharedPreferences prefs =getSharedPreferences(Constants.MY_PREFS_NAME,Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.clear();
                editor.apply();
                startActivity(new Intent(EditProfile.this, OTPActivity.class));
                Toast.makeText(this, "Only security purpose, if you changed your mobile number OTP verification is must.", Toast.LENGTH_LONG).show();
                finish();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(EditProfile.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }

    }

    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            userbitmap = (Bitmap) extras.get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            try {
                String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpeg";
                File imageFile = new File(mPath);

                FileOutputStream outputStream = new FileOutputStream(imageFile);
                int quality = 100;
                userbitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
                outputStream.flush();
                outputStream.close();
                String filePath = imageFile.getPath();
                Bitmap ssbitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                userbitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                user_img.setImageBitmap(ssbitmap);
                user_img_Camera = filePath;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            try {
                userbitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                //String path = saveImage(bitmap);
                Toast.makeText(EditProfile.this, "User Image Saved!", Toast.LENGTH_SHORT).show();
                user_img.setImageBitmap(userbitmap);
                user_img_Camera = ImageFilePath.getPath(EditProfile.this, data.getData());

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(EditProfile.this, "Failed!", Toast.LENGTH_SHORT).show();
            }
        }

    }

}
