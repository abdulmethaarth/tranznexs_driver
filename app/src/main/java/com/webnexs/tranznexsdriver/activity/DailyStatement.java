package com.webnexs.tranznexsdriver.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.webnexs.tranznexsdriver.pojoClasses.Constants;
import com.webnexs.tranznexsdriver.R;

public class DailyStatement extends AppCompatActivity {

    TextView fareAmount;
    String TotalEarning;
    Button closeBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_statement);

        SharedPreferences prefs = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE);
        TotalEarning = prefs.getString(Constants.ToatlFareAmount, "");

        closeBottom = (Button)findViewById(R.id.closeBottom);
        fareAmount = (TextView)findViewById(R.id.fareAmount);
        fareAmount.setText(TotalEarning);

        closeBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("selected", false);
                setResult(100, resultIntent);
                finish();
            }
        });
    }
}
