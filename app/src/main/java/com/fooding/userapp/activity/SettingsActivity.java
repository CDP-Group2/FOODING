package com.fooding.userapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fooding.userapp.FoodingApplication;
import com.fooding.userapp.R;
import com.github.channguyen.rsv.RangeSliderView;

import java.lang.reflect.Type;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends AppCompatActivity {
    private RangeSliderView textBoldness;
    private LinearLayout textBoldnessCaption;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.textBoldnessTitle) TextView textBoldnessTitle;
    @BindView(R.id.filter) ImageButton filterBtn;
    @BindView(R.id.camera) ImageButton cameraBtn;
    @BindView(R.id.recentlyViewed) ImageButton recentlyViewedBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        textBoldnessCaption = (LinearLayout)findViewById(R.id.textBoldnessCaption);

        /*************************************************************************************************************/
        // font setting
        final FoodingApplication app = FoodingApplication.getInstance();
        final SharedPreferences myPref = app.getMyPref();

        final String pathT = myPref.getString("titleFont", "none");
        Log.i("pathT", pathT);
        Typeface font = Typeface.createFromAsset(getAssets(), pathT);
        Log.i("foo", font.toString());
        title.setTypeface(font);

        final String pathK = myPref.getString("koreanFont", "none");
        Typeface fontK = Typeface.createFromAsset(getAssets(), pathK);
        TextView tv;
        for(int i = 0; i < textBoldnessCaption.getChildCount(); i++) {
            View view = textBoldnessCaption.getChildAt(i);
            if(view instanceof TextView) {
                tv = (TextView)view;
                tv.setTypeface(fontK);
            }
        }

        final String pathKB = myPref.getString("boldKoreanFont", "none");
        Typeface fontKB = Typeface.createFromAsset(getAssets(), pathKB);
        textBoldnessTitle.setTypeface(fontKB);
        /*************************************************************************************************************/

        textBoldness = (RangeSliderView)findViewById(R.id.textBoldness);
        final int userBoldness = myPref.getInt("fontBoldness", 1);
        textBoldness.setInitialIndex(userBoldness);

        textBoldness.setOnSlideListener(new RangeSliderView.OnSlideListener() {
            @Override
            public void onSlide(int index) {
                Log.i("On Slide Listener", Integer.toString(index));

                SharedPreferences.Editor editor = myPref.edit();
                editor.putInt("fontBoldness", index);

                switch (index) {
                    case 0:
                        editor.putString("listViewFont", "fonts/NanumSquareRoundOTFL.otf");
                        break;
                    case 1:
                        editor.putString("listViewFont", "fonts/NanumSquareRoundOTFR.otf");
                        break;
                    case 2:
                        editor.putString("listViewFont", "fonts/NanumSquareRoundOTFB.otf");
                        break;
                    case 3:
                        editor.putString("listViewFont", "fonts/NanumSquareRoundOTFEB.otf");
                        break;
                }

                editor.apply();
                /*final int temp = myPref.getInt("fontBoldness", 5);
                Toast.makeText(getApplicationContext(), Integer.toString(temp), Toast.LENGTH_SHORT).show();*/
            }
        });

        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingsActivity.this, PopUpFilter.class));
                finish();
            }
        });

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingsActivity.this, CameraActivity.class));
                finish();
            }
        });

        recentlyViewedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingsActivity.this, recentlyViewedActivity.class));
                finish();
            }
        });
    }
}
