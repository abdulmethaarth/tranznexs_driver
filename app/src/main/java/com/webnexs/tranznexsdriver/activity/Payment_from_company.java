package com.webnexs.tranznexsdriver.activity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.PowerManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.webnexs.tranznexsdriver.CustomAlertDialog;
import com.webnexs.tranznexsdriver.R;
import com.webnexs.tranznexsdriver.pojoClasses.AlarmReceiver;
import com.webnexs.tranznexsdriver.pojoClasses.Constants;

public class Payment_from_company extends AppCompatActivity {

    TextView paidForCompany,snoozing,rideCost,percentage,pay_for_company;

    private PowerManager mPowerManager;
    private PowerManager.WakeLock mWakeLock;
    String ToatlOnline;
    String TotalEarning = "100";
    @Override
    protected  void onStart(){
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_from_company);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen

        SharedPreferences prefs = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE);
        TotalEarning = prefs.getString(Constants.ToatlFareAmount, "");
        ToatlOnline = prefs.getString(Constants.OnlineHours, "");


        paidForCompany = (TextView)findViewById(R.id.paidForCompany);
        snoozing = (TextView)findViewById(R.id.snoozing);
        percentage = (TextView)findViewById(R.id.percentage);
        pay_for_company = (TextView)findViewById(R.id.pay_for_company);

        rideCost = (TextView)findViewById(R.id.rideCost);
        rideCost.setText(TotalEarning);
        percentage.setText("0.0");
        issueNotification();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        paidForCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //alarm cancel
               AlarmManager am = (AlarmManager) Payment_from_company.this.getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(Payment_from_company.this, AlarmReceiver.class);
               PendingIntent sender = PendingIntent.getBroadcast(Payment_from_company.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                am.cancel(sender);
                new CustomAlertDialog(Payment_from_company.this)
                        .setMessage("You sent a Payment succesfully,  Thank you.")
                        .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlarmReceiver.mediaPlayertune.stop();
                                        AlarmReceiver.waitTimer.cancel();
                                       Intent intent = new Intent(Payment_from_company.this, DriverHomeActivity.class);
                                       startActivity(intent);
                                    }
                                }).start();
                            }
                        }).create().show();
            }
        });

        snoozing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmReceiver.mediaPlayertune.stop();
                AlarmReceiver.waitTimer.cancel();
               // DriverHomeActivity.am.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),1000 * 60 * 1, sender);
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);
            }
        });
    }

    private void issueNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            makeNotificationChannel("CHANNEL_1", "Example channel", NotificationManager.IMPORTANCE_DEFAULT);
        }
        NotificationCompat.Builder notification =
                new NotificationCompat.Builder(this, "CHANNEL_1");
        notification
                .setSmallIcon(R.mipmap.ic_launcher) // can use any other icon
                .setContentTitle("Paying for company!")
                .setContentText("pay must")
                .setDefaults(Notification.DEFAULT_ALL) // must requires VIBRATE permission
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo))
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("You have must paying some percentage amount from total  earned amount. \n"))
                .setNumber(1); // this shows a number in the notification dots

        NotificationManager notificationManager =
                (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        assert notificationManager != null;
        notificationManager.notify(1, notification.build());
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    void makeNotificationChannel(String id, String name, int importance)
    {
        NotificationChannel channel = new NotificationChannel(id, name, importance);
        channel.setShowBadge(true); // set false to disable badges, Oreo exclusive

        NotificationManager notificationManager =
                (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        assert notificationManager != null;
        notificationManager.createNotificationChannel(channel);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.really_exit))
                .setMessage(getResources().getString(R.string.are_you_sure))
                .setNegativeButton(getResources().getString(R.string.no), null)
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {

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
