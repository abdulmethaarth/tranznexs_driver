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
import android.widget.EditText;
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

public class AddBankDetails extends AppCompatActivity {

    CircleImageView bnkpass_img,pan_img;
    EditText bank_name,holder_name,acc_no,ifsc_code;
    Button bank_dtls_nxt ;
    boolean connected = false;
    String mobileno,driver_id;
    String pan_img_string,pass_img_string;
    Bitmap panbitmap,pssbkbitmap;
    private static final int CAMERA_REQUEST_PASS= 1888;
    private static final int CAMERA_REQUEST_PAN = 1080;
    private static final int MY_CAMERA_PERMISSION_CODES = 101;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static int RESULT_LOAD_IMAGE_PASS = 1;
    private static int RESULT_LOAD_IMAGE_PAN = 0;
    TextView layout_open_camera,layout_open_gallery;
    Dialog OpenCameraDialog;
    RelativeLayout layout_open_cancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bank_details);

        SharedPreferences prefs = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE);
        driver_id = prefs.getString(Constants.driver_id, "");
        mobileno  = prefs.getString(Constants.mobileno, "");

        pan_img = (CircleImageView)findViewById(R.id.pan_img);
        bnkpass_img = (CircleImageView)findViewById(R.id.bnkpass_img);
        bank_name = (EditText)findViewById(R.id.bank_name);
        holder_name = (EditText)findViewById(R.id.holder_name);
        acc_no = (EditText)findViewById(R.id.acc_no);
        ifsc_code = (EditText)findViewById(R.id.ifsc_code);
        bank_dtls_nxt = (Button)findViewById(R.id.bank_dtls_nxt);

        OpenCameraDialog = new Dialog(AddBankDetails.this, android.R.style.Theme_Translucent_NoTitleBar);
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

        pan_img.setOnClickListener(new View.OnClickListener() {
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
                            if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                                startActivityForResult(cameraIntent, CAMERA_REQUEST_PAN);
                            }
                        }
                    }
                });

                layout_open_gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OpenCameraDialog.cancel();
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(intent, RESULT_LOAD_IMAGE_PAN);
                    }
                });
            }
        });
        bnkpass_img.setOnClickListener(new View.OnClickListener() {
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
                            startActivityForResult(cameraIntent, CAMERA_REQUEST_PASS);
                        }
                    }
                });

                layout_open_gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OpenCameraDialog.cancel();
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(intent, RESULT_LOAD_IMAGE_PASS);
                    }
                });
            }
        });

        bank_dtls_nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bank_name.getText().toString().isEmpty()) {
                    Snackbar.make(findViewById(android.R.id.content), "Please enter Bank name", Snackbar.LENGTH_LONG).show();
                    return;
                }else if (holder_name.getText().toString().isEmpty()) {
                    Snackbar.make(findViewById(android.R.id.content), "Please enter acc holder name", Snackbar.LENGTH_LONG).show();
                    return;
                } else if (acc_no.getText().toString().isEmpty()) {
                    Snackbar.make(findViewById(android.R.id.content), "Please enter acc no", Snackbar.LENGTH_LONG).show();
                    return;
                } else if (ifsc_code.getText().toString().isEmpty()) {
                    Snackbar.make(findViewById(android.R.id.content), "Please enter IFSC code", Snackbar.LENGTH_LONG).show();
                    return;
                }else if (pssbkbitmap == null) {
                    Snackbar.make(findViewById(android.R.id.content), "Please provide passbook copy", Snackbar.LENGTH_LONG).show();
                    return;
                }else if (panbitmap== null) {
                    Snackbar.make(findViewById(android.R.id.content), "Please provide PAN card copy", Snackbar.LENGTH_LONG).show();
                    return;
                }else{
                    bankDetailsUpload();
                }

            }
        });
    }

    private void bankDetailsUpload() {
        String pancard = String.valueOf(pan_img_string);
        String passbook = String.valueOf(pass_img_string);
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;

            try {

                String uploadId = UUID.randomUUID().toString();
                //Creating a multi part request
                new MultipartUploadRequest(AddBankDetails.this, uploadId, Constants.REGISTER5)
                        .addFileToUpload(passbook, "passbook_image") //Adding file
                        .addFileToUpload(pancard, "pancard_image") //Adding file
                        //.addFileToUpload(lisence_image, "lisence_image") //Adding file
                        .addParameter("driver_id", driver_id)
                        .addParameter("bank_name", bank_name.getText().toString())
                        .addParameter("account_holder", holder_name.getText().toString())
                        .addParameter("accountno", acc_no.getText().toString())
                        .addParameter("phone_no", mobileno)
                        .addParameter("ifsc_code", ifsc_code.getText().toString())
                        .setMaxRetries(3)
                        .startUpload();//Starting the upload
                startActivity(new Intent(AddBankDetails.this, OTPActivity.class));
            } catch (FileNotFoundException f) {
                //   Common.showMkError(SignUpActivity.this, getResources().getString(R.string.check_internet));
                Toast.makeText(AddBankDetails.this, "check internet", Toast.LENGTH_SHORT).show();
            } catch (Exception exc) {
            }
        }
        else{
            connected = false;
            Snackbar.make(findViewById(android.R.id.content), "Check your internet connection.", Snackbar.LENGTH_LONG).show();
        }
    }


    private void permission() {
        ActivityCompat.requestPermissions(AddBankDetails.this,
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
                    Toast.makeText(AddBankDetails.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
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
                startActivityForResult(cameraIntent, CAMERA_REQUEST_PAN);
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

        //PN IMAGE
        if (requestCode == CAMERA_REQUEST_PAN && resultCode == RESULT_OK) {
            panbitmap = (Bitmap)data.getExtras().get("data");

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            try {
                String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpeg";
                File imageFile = new File(mPath);

                FileOutputStream outputStream = new FileOutputStream(imageFile);
                int quality = 100;
                panbitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
                outputStream.flush();
                outputStream.close();
                String filePath = imageFile.getPath();
                Bitmap ssbitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                panbitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                pan_img.setImageBitmap(ssbitmap);
                pan_img_string = filePath;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == RESULT_LOAD_IMAGE_PAN && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();

            try {
                panbitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                Toast.makeText(AddBankDetails.this, "pan_img Image Saved!", Toast.LENGTH_SHORT).show();
                pan_img.setImageBitmap(panbitmap);
                pan_img_string = ImageFilePath.getPath(AddBankDetails.this, data.getData());

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(AddBankDetails.this, "Failed!", Toast.LENGTH_SHORT).show();
            }
        }

        //PASS BOOK IMAGE
        if (requestCode == CAMERA_REQUEST_PASS && resultCode == RESULT_OK) {
            pssbkbitmap = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            try {
                String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpeg";
                File imageFile = new File(mPath);

                FileOutputStream outputStream = new FileOutputStream(imageFile);
                int quality = 100;
                pssbkbitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
                outputStream.flush();
                outputStream.close();
                String filePath = imageFile.getPath();
                Bitmap ssbitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                pssbkbitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                bnkpass_img.setImageBitmap(ssbitmap);
                pass_img_string = filePath;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == RESULT_LOAD_IMAGE_PASS && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();

            try {
                pssbkbitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                Toast.makeText(AddBankDetails.this, "passbook_img Image Saved!", Toast.LENGTH_SHORT).show();
                bnkpass_img.setImageBitmap(pssbkbitmap);
                pass_img_string = ImageFilePath.getPath(AddBankDetails.this, data.getData());

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(AddBankDetails.this, "Failed!", Toast.LENGTH_SHORT).show();
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
