package com.webnexs.tranznexsdriver.pojoClasses;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import com.webnexs.tranznexsdriver.R;

public class AlarmReceiver extends BroadcastReceiver {
    public static MediaPlayer mediaPlayertune;
    public static CountDownTimer waitTimer;

    @Override
    public void onReceive(Context context, Intent intent) {
        mediaPlayertune = MediaPlayer.create(context, R.raw.beep);
        waitTimer = new CountDownTimer(500000, 1000) {

            public void onTick(long millisUntilFinished) {
                mediaPlayertune.start();
            }
            public void onFinish() {
                mediaPlayertune.stop();
            }
        }.start();

        Intent i = new Intent();
        i.setClassName("com.s2labs.ntcabdriver", "com.s2labs.ntcabdriver.activity.Payment_from_company");
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);


    }
}
