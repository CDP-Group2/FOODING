package com.fooding.userapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
//        SharedPreferences myPref = app.getMyPref();

        final SharedPreferences myPref = getSharedPreferences("settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = myPref.edit();

        /*boolean firstTime = myPref.getBoolean("isFirst", false);
        if(firstTime == false) {    // 어플리케이션 최초 실행
            Toast.makeText(getApplicationContext(), "최초 실행", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "최초 실행 ㄴㄴ", Toast.LENGTH_SHORT).show();
        }*/

        myPref.getString("firstTime", "");
        if(myPref.getString("firstTime", "").isEmpty()) {   // 어플리케이션 설치 후 최초 실행
//            Toast.makeText(getApplicationContext(), "최초 실행", Toast.LENGTH_SHORT).show();

            editor.putString("titleFont", "fonts/BukhariScript-Regular.otf");
            editor.putString("titleFontk", "fonts/NanumSquareRoundOTFEB.otf");
            editor.putString("koreanFont", "fonts/NanumSquareRoundOTFR.otf");
            editor.putString("boldKoreanFont", "fonts/NanumSquareRoundOTFB.otf");
            editor.putString("listViewFont", "fonts/NanumSquareRoundOTFR.otf");
            editor.putInt("fontBoldness", 1);
            editor.putInt("fontSize", 16);
            editor.putBoolean("theme", false);
            editor.putBoolean("translation", false);
            editor.putBoolean("calorie", false);
            editor.putString("myCalorie", null);
            editor.apply();

        } else {
//            Toast.makeText(getApplicationContext(), "최초 실행 아님", Toast.LENGTH_SHORT).show();
            ;
        }

        editor.putString("firstTime", "exist");
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