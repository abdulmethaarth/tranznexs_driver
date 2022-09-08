package com.webnexs.tranznexsdriver.activity;

import android.Manifest;
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
import android.widget.LinearLayout;
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

public class AddVehicleDocs extends AppCompatActivity {

    CircleImageView insurance_img,owner_img,puc_img,rc_img;
    Button veh_doc_nxt;
    LinearLayout bike_type_layout;
    String driver_id,mobileno;
    String rc_img_string,insurance_img_sting,owner_img_string,puc_img_string;
    boolean connected = false;
    Bitmap rcbitmap,insurbitmap,ownerbitmap,pucbitmap;
    private static final int MY_CAMERA_PERMISSION_CODES = 101;
    private static final int CAMERA_REQUEST_RC= 1888;
    private static final int CAMERA_REQUEST_INS = 1080;
    private static final int CAMERA_REQUEST_OWN= 1313;
    private static final int CAMERA_REQUEST_PUC = 1999;
    private static int RESULT_LOAD_IMAGE_RC = 1;
    private static int RESULT_LOAD_IMAGE_INS = 2;
    private static int RESULT_LOAD_IMAGE_OWN = 3;
    private static int RESULT_LOAD_IMAGE_PUC= 0;
    TextView layout_open_camera,layout_open_gallery;
    Dialog OpenCameraDialog;
    RelativeLayout layout_open_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle_docs);

        rc_img = (CircleImageView)findViewById(R.id.rc_img);
        insurance_img = (CircleImageView)findViewById(R.id.insurance_img);
        owner_img = (CircleImageView)findViewById(R.id.owner_img);
        puc_img = (CircleImageView)findViewById(R.id.puc_img);
        bike_type_layout = (LinearLayout)findViewById(R.id.bike_type_layout);
        veh_doc_nxt = (Button) findViewById(R.id.veh_doc_nxt);

        SharedPreferences prefs = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE);
        driver_id = prefs.getString(Constants.driver_id, "");
        mobileno  = prefs.getString(Constants.mobileno, "");

        OpenCameraDialog = new Dialog(AddVehicleDocs.this, android.R.style.Theme_Translucent_NoTitleBar);
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
        rc_img.setOnClickListener(new View.OnClickListener() {
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
                            startActivityForResult(cameraIntent, CAMERA_REQUEST_RC);
                        }
                    }
                });

                layout_open_gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OpenCameraDialog.cancel();
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(intent, RESULT_LOAD_IMAGE_RC);
                    }
                });
            }
        });
        insurance_img.setOnClickListener(new View.OnClickListener() {
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
                            startActivityForResult(cameraIntent, CAMERA_REQUEST_INS);
                        }
                    }
                });

                layout_open_gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OpenCameraDialog.cancel();
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(intent, RESULT_LOAD_IMAGE_INS);
                    }
                });
            }
        });
        //PUC IMAGE
        puc_img.setOnClickListener(new View.OnClickListener() {
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
                            startActivityForResult(cameraIntent, CAMERA_REQUEST_PUC);
                        }
                    }
                });

                layout_open_gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OpenCameraDialog.cancel();
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(intent, RESULT_LOAD_IMAGE_PUC);
                    }
                });
            }
        });

        owner_img.setOnClickListener(new View.OnClickListener() {
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
                            startActivityForResult(cameraIntent, CAMERA_REQUEST_OWN);
                        }
                    }
                });

                layout_open_gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OpenCameraDialog.cancel();
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(intent, RESULT_LOAD_IMAGE_OWN);
                    }
                });
            }
        });

        veh_doc_nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ( rcbitmap == null) {
                    Snackbar.make(findViewById(android.R.id.content), "Please provide your RC image", Snackbar.LENGTH_LONG).show();
                    return;
                }
                else if (insurbitmap == null) {
                    Snackbar.make(findViewById(android.R.id.content), "Please provide your Insurance copy", Snackbar.LENGTH_LONG).show();
                    return;
                }else if (ownerbitmap == null) {
                    Snackbar.make(findViewById(android.R.id.content), "Please provide your vehicle owner image", Snackbar.LENGTH_LONG).show();
                    return;
                }else if (pucbitmap == null) {
                    Snackbar.make(findViewById(android.R.id.content), "Please provide your PUC copy", Snackbar.LENGTH_LONG).show();
                    return;
                }
                else{
                    vehDocsUpload();
                }
            }
        });


    }

    private void vehDocsUpload() {
        String rc_book = String.valueOf(rc_img_string);
        String puc = String.valueOf(puc_img_string);
        String owner = String.valueOf(owner_img_string);
        String insurance = String.valueOf(insurance_img_sting);
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
            try {
                String uploadId = UUID.randomUUID().toString();
                //Creating a multi part request
                new MultipartUploadRequest(AddVehicleDocs.this, uploadId, Constants.REGISTER4)
                        .addFileToUpload(rc_book, "rc_image") //Adding file
                        .addFileToUpload(puc, "puc_image") //Adding file
                        .addFileToUpload(owner, "owner_certificate") //Adding file
                        .addFileToUpload(insurance, "insurance_image") //Adding file
                        .addParameter("driver_id", driver_id)
                        .addParameter("phone_no", mobileno)
                        .setMaxRetries(3)
                        .startUpload();//Starting the upload
                startActivity(new Intent(AddVehicleDocs.this, AddBankDetails.class));
                finish();
            } catch (FileNotFoundException f) {
                //   Common.showMkError(SignUpActivity.this, getResources().getString(R.string.check_internet));
                Toast.makeText(AddVehicleDocs.this, "check internet", Toast.LENGTH_SHORT).show();

            } catch (Exception exc) {

            }
        }
        else{
            connected = false;
            Snackbar.make(findViewById(android.R.id.content), "Check your internet connection.", Snackbar.LENGTH_LONG).show();
        }
    }

    private void permission() {
        ActivityCompat.requestPermissions(AddVehicleDocs.this,
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
                    Toast.makeText(AddVehicleDocs.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODES) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST_RC);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);


        //RC IMage
        if (requestCode == CAMERA_REQUEST_RC && resultCode == RESULT_OK) {
            rcbitmap = (Bitmap) data.getExtras().get("data");
            rc_img.setImageBitmap(rcbitmap);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            try {
                String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpeg";
                File imageFile = new File(mPath);

                FileOutputStream outputStream = new FileOutputStream(imageFile);
                int quality = 100;
                rcbitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
                outputStream.flush();
                outputStream.close();
                String filePath = imageFile.getPath();
                Bitmap ssbitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                rcbitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                rc_img.setImageBitmap(ssbitmap);
                rc_img_string = filePath;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == RESULT_LOAD_IMAGE_RC && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            try {
                rcbitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                Toast.makeText(AddVehicleDocs.this, "RC Image Saved!", Toast.LENGTH_SHORT).show();
                rc_img.setImageBitmap(rcbitmap);
                rc_img_string = ImageFilePath.getPath(AddVehicleDocs.this, data.getData());

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(AddVehicleDocs.this, "Failed!", Toast.LENGTH_SHORT).show();
            }
        }

        // INSURANCE IMAGE
        if (requestCode == CAMERA_REQUEST_INS && resultCode == RESULT_OK) {
            insurbitmap = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            try {
                String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpeg";
                File imageFile = new File(mPath);

                FileOutputStream outputStream = new FileOutputStream(imageFile);
                int quality = 100;
                insurbitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
                outputStream.flush();
                outputStream.close();
                String filePath = imageFile.getPath();
                Bitmap ssbitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                insurbitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                insurance_img.setImageBitmap(ssbitmap);
                insurance_img_sting = filePath;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == RESULT_LOAD_IMAGE_INS && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();

            try {
                insurbitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                Toast.makeText(AddVehicleDocs.this, "Insurance Image Saved!", Toast.LENGTH_SHORT).show();
                insurance_img.setImageBitmap(insurbitmap);
                insurance_img_sting = ImageFilePath.getPath(AddVehicleDocs.this, data.getData());

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(AddVehicleDocs.this, "Failed!", Toast.LENGTH_SHORT).show();
            }
        }

        // OWNER IMAGE
        if (requestCode == CAMERA_REQUEST_OWN && resultCode == RESULT_OK) {
            ownerbitmap= (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            try {
                String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpeg";
                File imageFile = new File(mPath);

                FileOutputStream outputStream = new FileOutputStream(imageFile);
                int quality = 100;
                ownerbitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
                outputStream.flush();
                outputStream.close();
                String filePath = imageFile.getPath();
                Bitmap ssbitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                ownerbitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                owner_img.setImageBitmap(ssbitmap);
                owner_img_string = filePath;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == RESULT_LOAD_IMAGE_OWN && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();

            try {
                ownerbitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                Toast.makeText(AddVehicleDocs.this, "Owner Image Saved!", Toast.LENGTH_SHORT).show();
                owner_img.setImageBitmap(ownerbitmap);
                owner_img_string = ImageFilePath.getPath(AddVehicleDocs.this, data.getData());

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(AddVehicleDocs.this, "Failed!", Toast.LENGTH_SHORT).show();
            }
        }

        // PUC IMage
        if (requestCode == CAMERA_REQUEST_PUC && resultCode == RESULT_OK) {
            pucbitmap = (Bitmap) data.getExtras().get("data");
            puc_img.setImageBitmap(pucbitmap);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            try {
                String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpeg";
                File imageFile = new File(mPath);

                FileOutputStream outputStream = new FileOutputStream(imageFile);
                int quality = 100;
                pucbitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
                outputStream.flush();
                outputStream.close();
                String filePath = imageFile.getPath();
                Bitmap ssbitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                pucbitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                puc_img.setImageBitmap(ssbitmap);
                puc_img_string = filePath;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == RESULT_LOAD_IMAGE_PUC && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();

            try {
                pucbitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                Toast.makeText(AddVehicleDocs.this, "Gov Id Image Saved!", Toast.LENGTH_SHORT).show();
                puc_img.setImageBitmap(pucbitmap);
                puc_img_string = ImageFilePath.getPath(AddVehicleDocs.this, data.getData());

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(AddVehicleDocs.this, "Failed!", Toast.LENGTH_SHORT).show();
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
