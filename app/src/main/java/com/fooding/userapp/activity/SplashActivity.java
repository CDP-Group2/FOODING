package com.fooding.userapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.fooding.userapp.FoodingApplication;
import com.fooding.userapp.R;

public class SplashActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        SharedPreferences myPref = getSharedPreferences("settings", MODE_PRIVATE);
        final FoodingApplication app = FoodingApplication.getInstance();
        SharedPreferences myPref = app.getMyPref();

        myPref = getSharedPreferences("settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = myPref.edit();
        editor.putString("titleFont", "fonts/BukhariScript-Regular.otf");
        editor.putString("koreanFont", "fonts/NanumSquareRoundOTFR.otf");
        editor.putString("listViewFont", "fonts/NanumSquareRoundOTFR.otf");
        editor.apply();

        app.setMyPref(myPref);

//        Toast.makeText(getApplicationContext(), myPref.getString("titleFont", "none"), Toast.LENGTH_SHORT).show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, CameraActivity.class));
                finish();
            }
        },1000);
    }
}