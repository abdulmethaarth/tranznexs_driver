package com.webnexs.tranznexsdriver.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.webnexs.tranznexsdriver.pojoClasses.Constants;
import com.webnexs.tranznexsdriver.pojoClasses.ImageFilePath;
import com.webnexs.tranznexsdriver.R;

import net.gotev.uploadservice.MultipartUploadRequest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalDoc extends AppCompatActivity {

    CircleImageView driver_img, vehile_reg_img,govrnmntid_img,license_img;
    Button personal_doc_nxt;
    boolean connected = false;
    private static final int CAMERA_REQUEST = 1888;
    private static final int CAMERA_REQUEST_VEHICLE = 1080;
    private static final int CAMERA_REQUEST_LICENSE = 1313;
    private static final int CAMERA_REQUEST_GOV = 1999;
    Bitmap driverbitmap,licensebitmap,vehicleRegbitmap,govrnIdbitmap;
    String vehicleRegString,govrnIdString,driver_img_string,license_img_string;
    String driver_id,mobileno;
    TextView layout_open_camera,layout_open_gallery;
    Dialog OpenCameraDialog;
    RelativeLayout layout_open_cancel;
    private static final int MY_CAMERA_PERMISSION_CODES = 101;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static int RESULT_LOAD_IMAGE = 1;
    private static int RESULT_LOAD_IMAGE_LICENSE = 2;
    private static int RESULT_LOAD_IMAGE_VEHICLE = 3;
    private static int RESULT_LOAD_IMAGE_GOV = 0;
    private static final String IMAGE_DIRECTORY_NAME = "ABDUL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_doc);

        SharedPreferences prefs = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE);
        driver_id = prefs.getString(Constants.driver_id, "");
        mobileno  = prefs.getString(Constants.mobileno, "");

        driver_img = (CircleImageView)findViewById(R.id.driver_img);
        vehile_reg_img = (CircleImageView)findViewById(R.id.vehile_reg_img);
        govrnmntid_img = (CircleImageView)findViewById(R.id.govrnmntid_img);
        license_img = (CircleImageView)findViewById(R.id.license_img);
        personal_doc_nxt = (Button)findViewById(R.id.personal_doc_nxt);

        OpenCameraDialog = new Dialog(PersonalDoc.this, android.R.style.Theme_Translucent_NoTitleBar);
        OpenCameraDialog.setContentView(R.layout.camera_dialog_layout);
        layout_open_camera = (TextView) OpenCameraDialog.findViewById(R.id.layout_open_camera);
        layout_open_gallery = (TextView) OpenCameraDialog.findViewById(R.id.layout_open_gallery);
        layout_open_cancel = (RelativeLayout) OpenCameraDialog.findViewById(R.id.layout_open_cancel);

        layout_open_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenCameraDialog.cancel();
            }
        });


        driver_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permission();
                OpenCameraDialog.show();
                layout_open_camera.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.N)
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View v) {
                        OpenCameraDialog.cancel();
                        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODES);
                        } else {
                            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, CAMERA_REQUEST);

                        }
                    }
                });

                layout_open_gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OpenCameraDialog.cancel();
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(intent, RESULT_LOAD_IMAGE);
                    }
                });
            }
        });

        license_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permission();
                OpenCameraDialog.show();
                layout_open_camera.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View v) {
                        OpenCameraDialog.cancel();
                        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODES);
                        } else {
                            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, CAMERA_REQUEST_LICENSE);
                        }
                    }
                });
                layout_open_gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OpenCameraDialog.cancel();
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(intent, RESULT_LOAD_IMAGE_LICENSE);
                    }
                });
            }
        });

        vehile_reg_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenCameraDialog.show();
                layout_open_camera.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View v) {
                        OpenCameraDialog.cancel();
                        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODES);
                        } else {
                            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, CAMERA_REQUEST_VEHICLE);
                        }
                    }
                });
                layout_open_gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OpenCameraDialog.cancel();
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(intent, RESULT_LOAD_IMAGE_VEHICLE);
                    }
                });
            }
        });

        govrnmntid_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permission();
                OpenCameraDialog.show();
                layout_open_camera.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View v) {
                        OpenCameraDialog.cancel();
                        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODES);
                        } else {
                            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, CAMERA_REQUEST_GOV);
                        }
                    }
                });
                layout_open_gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OpenCameraDialog.cancel();
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(intent, RESULT_LOAD_IMAGE_GOV);
                    }
                });
            }
        });



        personal_doc_nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (driverbitmap == null ) {
                    Snackbar.make(findViewById(android.R.id.content), "Please provide your image", Snackbar.LENGTH_LONG).show();
                    return;
                }
                else  if ( licensebitmap == null ) {
                    Snackbar.make(findViewById(android.R.id.content), "Please provide license image", Snackbar.LENGTH_LONG).show();
                    return;
               }
               else if (vehicleRegbitmap == null ) {
                    Snackbar.make(findViewById(android.R.id.content), "Please provide your vehicle registration copy", Snackbar.LENGTH_LONG).show();
                    return;
               }else if ( govrnIdbitmap == null ) {
                    Snackbar.make(findViewById(android.R.id.content), "Please provide your any government proof", Snackbar.LENGTH_LONG).show();
                    return;
               }
               else{
                    personalDocUpload();
                }
            }
        });
    }

    /*@RequiresApi(api = Build.VERSION_CODES.N)
    private File createImageFile() throws IOException {
        // Create an image file name

    }
*/


    private void personalDocUpload() {
        String driverimg = String.valueOf(driver_img_string);
        String licenseimg = String.valueOf(license_img_string);
        String vehregimg = String.valueOf(vehicleRegString);
        String proofimg = String.valueOf(govrnIdString);
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;

            try {
                String uploadId = UUID.randomUUID().toString();
                //Creating a multi part request
                new MultipartUploadRequest(PersonalDoc.this, uploadId, Constants.REGISTER2)
                        .addFileToUpload(driverimg, "user_image") //Adding file
                        .addFileToUpload(licenseimg, "licence_image") //Adding file
                        .addFileToUpload(vehregimg, "vehicle_registration") //Adding file
                        .addFileToUpload(proofimg, "address_image") //Adding file
                        .addParameter("driver_id", driver_id)
                        .addParameter("phone_no", mobileno)
                        .setMaxRetries(3)
                        .startUpload();//Starting the upload
                startActivity(new Intent(PersonalDoc.this, AddVehicle.class));
                finish();
            } catch (FileNotFoundException f) {
                //   Common.showMkError(SignUpActivity.this, getResources().getString(R.string.check_internet));
                Toast.makeText(PersonalDoc.this, "check internet", Toast.LENGTH_SHORT).show();
            } catch (Exception exc) {
            }
        }
        else{
            connected = false;
            Snackbar.make(findViewById(android.R.id.content), "Check your internet connection.", Snackbar.LENGTH_LONG).show();
        }
    }

    private void permission() {
        ActivityCompat.requestPermissions(PersonalDoc.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                1);
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
                    Toast.makeText(PersonalDoc.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
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
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
        //Driver IMage
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            driverbitmap = (Bitmap) data.getExtras().get("data");
           // driver_img.setImageBitmap(driverbitmap);

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();

            try {
                String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpeg";
                File imageFile = new File(mPath);

                FileOutputStream outputStream = new FileOutputStream(imageFile);
                int quality = 100;
                driverbitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
                outputStream.flush();
                outputStream.close();
                String filePath = imageFile.getPath();
                Bitmap ssbitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                driverbitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                driver_img.setImageBitmap(ssbitmap);
                driver_img_string = filePath;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            try {
                driverbitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                Toast.makeText(PersonalDoc.this, "Driver Image Saved!", Toast.LENGTH_SHORT).show();
                driver_img.setImageBitmap(driverbitmap);
                driver_img_string = ImageFilePath.getPath(PersonalDoc.this, data.getData());

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(PersonalDoc.this, "Failed!", Toast.LENGTH_SHORT).show();
            }
        }

        // Driving License
        if (requestCode == CAMERA_REQUEST_LICENSE && resultCode == RESULT_OK) {
            licensebitmap = (Bitmap) data.getExtras().get("data");
            license_img.setImageBitmap(licensebitmap);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            try {
                String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpeg";
                File imageFile = new File(mPath);

                FileOutputStream outputStream = new FileOutputStream(imageFile);
                int quality = 100;
                licensebitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
                outputStream.flush();
                outputStream.close();
                String filePath = imageFile.getPath();
                Bitmap ssbitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                licensebitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                license_img.setImageBitmap(ssbitmap);
                license_img_string = filePath;

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        if (requestCode == RESULT_LOAD_IMAGE_LICENSE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();

            try {
                licensebitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                Toast.makeText(PersonalDoc.this, "License Image Saved!", Toast.LENGTH_SHORT).show();
                license_img.setImageBitmap(licensebitmap);
                license_img_string = ImageFilePath.getPath(PersonalDoc.this, data.getData());

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(PersonalDoc.this, "Failed!", Toast.LENGTH_SHORT).show();
            }
        }

        // VEHICLE REGISTRATION
        if (requestCode == CAMERA_REQUEST_VEHICLE && resultCode == RESULT_OK) {
            vehicleRegbitmap = (Bitmap) data.getExtras().get("data");
            vehile_reg_img.setImageBitmap(vehicleRegbitmap);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            try {
                String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpeg";
                File imageFile = new File(mPath);

                FileOutputStream outputStream = new FileOutputStream(imageFile);
                int quality = 100;
                vehicleRegbitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
                outputStream.flush();
                outputStream.close();
                String filePath = imageFile.getPath();
                Bitmap ssbitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                vehicleRegbitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                vehile_reg_img.setImageBitmap(ssbitmap);
                vehicleRegString = filePath;

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        if (requestCode == RESULT_LOAD_IMAGE_VEHICLE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();

            try {
                vehicleRegbitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                Toast.makeText(PersonalDoc.this, "Reg Image Saved!", Toast.LENGTH_SHORT).show();
                vehile_reg_img.setImageBitmap(vehicleRegbitmap);
                vehicleRegString = ImageFilePath.getPath(PersonalDoc.this, data.getData());

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(PersonalDoc.this, "Failed!", Toast.LENGTH_SHORT).show();
            }
        }

        // GOV ID
        if (requestCode == CAMERA_REQUEST_GOV && resultCode == RESULT_OK) {
            govrnIdbitmap = (Bitmap) data.getExtras().get("data");
            govrnmntid_img.setImageBitmap(govrnIdbitmap);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();

            try {
                String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpeg";
                File imageFile = new File(mPath);

                FileOutputStream outputStream = new FileOutputStream(imageFile);
                int quality = 100;
                govrnIdbitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
                outputStream.flush();
                outputStream.close();
                String filePath = imageFile.getPath();
                Bitmap ssbitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                govrnIdbitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                govrnmntid_img.setImageBitmap(ssbitmap);
                govrnIdString = filePath;

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        if (requestCode == RESULT_LOAD_IMAGE_GOV && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();

            try {
                govrnIdbitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                Toast.makeText(PersonalDoc.this, "Gov Id Image Saved!", Toast.LENGTH_SHORT).show();
                govrnmntid_img.setImageBitmap(govrnIdbitmap);
                govrnIdString = ImageFilePath.getPath(PersonalDoc.this, data.getData());

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(PersonalDoc.this, "Failed!", Toast.LENGTH_SHORT).show();
            }
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
