package com.fooding.userapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.util.Range;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
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
    private RangeSliderView textSize;
    private LinearLayout textBoldnessCaption;
    private LinearLayout textSizeCaption;
    @BindView(R.id.translationCaption) TextView translationCaption;
    @BindView(R.id.themeCaption) TextView themeCaption;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.textBoldnessTitle) TextView textBoldnessTitle;
    @BindView(R.id.textSizeTitle) TextView textSizeTitle;
    @BindView(R.id.etcTitle) TextView etcTitle;
    @BindView(R.id.filter) ImageButton filterBtn;
    @BindView(R.id.camera) ImageButton cameraBtn;
    @BindView(R.id.recentlyViewed) ImageButton recentlyViewedBtn;
    @BindView(R.id.calorieText) TextView calorieText;
    @BindView(R.id.calorieValue) EditText calorieValue;
    @BindView(R.id.kcal) TextView kcal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        textBoldnessCaption = (LinearLayout)findViewById(R.id.textBoldnessCaption);
        textSizeCaption = (LinearLayout)findViewById(R.id.textSizeCaption);

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
        for(int i = 0; i < textSizeCaption.getChildCount(); i++) {
            View view = textSizeCaption.getChildAt(i);
            if(view instanceof TextView) {
                tv = (TextView)view;
                tv.setTypeface(fontK);
            }
        }
        translationCaption.setTypeface(fontK);
        themeCaption.setTypeface(fontK);
        kcal.setTypeface(fontK);

        final String pathKB = myPref.getString("boldKoreanFont", "none");
        Typeface fontKB = Typeface.createFromAsset(getAssets(), pathKB);
        textBoldnessTitle.setTypeface(fontKB);
        textSizeTitle.setTypeface(fontKB);
        etcTitle.setTypeface(fontKB);
        calorieText.setTypeface(fontKB);
        calorieValue.setTypeface(fontKB);
        /*************************************************************************************************************/

        /*************************************************************************************************************/
        // theme setting
        if(myPref.getBoolean("theme", false)) { // dark theme
            // change background
            final View root = findViewById(R.id.settingsActivity).getRootView();
//            root.setBackgroundColor(Color.parseColor("#000000"));
            root.setBackgroundResource(R.drawable.dark_theme_background);

            // change text color
            title.setTextColor(Color.parseColor("#ffffff"));
            for(int i = 0; i < textBoldnessCaption.getChildCount(); i++) {
                View view = textBoldnessCaption.getChildAt(i);
                if(view instanceof TextView) {
                    tv = (TextView)view;
                    tv.setTextColor(Color.parseColor("#ffffff"));
                }
            }
            for(int i = 0; i < textSizeCaption.getChildCount(); i++) {
                View view = textSizeCaption.getChildAt(i);
                if(view instanceof TextView) {
                    tv = (TextView)view;
                    tv.setTextColor(Color.parseColor("#ffffff"));
                }
            }
            translationCaption.setTextColor(Color.parseColor("#ffffff"));
            themeCaption.setTextColor(Color.parseColor("#ffffff"));
            textBoldnessTitle.setTextColor(Color.parseColor("#ffffff"));
            textSizeTitle.setTextColor(Color.parseColor("#ffffff"));
            etcTitle.setTextColor(Color.parseColor("#ffffff"));
            calorieText.setTextColor(getResources().getColor(R.color.myWhite));
            calorieValue.setTextColor(getResources().getColor(R.color.myWhite));
            kcal.setTextColor(getResources().getColor(R.color.myWhite));

            // change buttons
            filterBtn.setImageResource(R.mipmap.filter_white);
            cameraBtn.setImageResource(R.mipmap.camera_white);
            recentlyViewedBtn.setImageResource(R.mipmap.list_white);

            // change dividing lines
            View tmp = findViewById(R.id.title_bar);
            tmp.setBackgroundColor(Color.parseColor("#ffffff"));
            tmp = findViewById(R.id.menu_bar);
            tmp.setBackgroundColor(Color.parseColor("#ffffff"));
            tmp = findViewById(R.id.tmp1);
            tmp.setBackgroundColor(Color.parseColor("#ececec"));
            tmp = findViewById(R.id.tmp2);
            tmp.setBackgroundColor(Color.parseColor("#ececec"));
            tmp = findViewById(R.id.tmp3);
            tmp.setBackgroundColor(Color.parseColor("#ececec"));
        }
        /*************************************************************************************************************/

        calorieValue.setText(myPref.getString("myCalorie", null));

        textBoldness = (RangeSliderView)findViewById(R.id.textBoldness);
        final int userBoldness = myPref.getInt("fontBoldness", 1);
        textBoldness.setInitialIndex(userBoldness);

        textBoldness.setOnSlideListener(new RangeSliderView.OnSlideListener() {
            @Override
            public void onSlide(int index) {
                Log.i("Font Boldness", Integer.toString(index));

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

        textSize = (RangeSliderView)findViewById(R.id.textSize);
        final int userSize = myPref.getInt("fontSize", 16);
        textSize.setInitialIndex((userSize - 12) / 2);

        textSize.setOnSlideListener(new RangeSliderView.OnSlideListener() {
            @Override
            public void onSlide(int index) {
                Log.i("Text Size", Integer.toString(index));

                SharedPreferences.Editor editor = myPref.edit();
                editor.putInt("fontSize", index * 2 + 12);
                editor.apply();

                /*final int temp = myPref.getInt("fontSize", 5);
                Toast.makeText(getApplicationContext(), Integer.toString(temp), Toast.LENGTH_SHORT).show();*/
            }
        });

        SwitchCompat themeSwitch = (SwitchCompat)findViewById(R.id.themeSwitch);
        themeSwitch.setChecked(myPref.getBoolean("theme", false));
        int[][] states = new int[][] {
                new int[] {-android.R.attr.state_checked},
                new int[] {android.R.attr.state_checked},
        };
        int[] trackColors = new int[] {
                getResources().getColor(R.color.gray),
                getResources().getColor(R.color.yellowAccentAlpha),
        };
        themeSwitch.setTrackTintList(new ColorStateList(states, trackColors));

        themeSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences.Editor editor = myPref.edit();

                editor.putBoolean("theme", b);
                editor.apply();

                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);

//                Toast.makeText(getApplicationContext(), Boolean.toString(myPref.getBoolean("theme", false)), Toast.LENGTH_SHORT).show();
            }
        });

        final SwitchCompat translationSwitch = (SwitchCompat) findViewById(R.id.translationSwitch);
        translationSwitch.setChecked(myPref.getBoolean("translation", false));
        translationSwitch.setTrackTintList(new ColorStateList(states, trackColors));

        translationSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences.Editor editor = myPref.edit();

                editor.putBoolean("translation", b);
                editor.apply();
            }
        });

        final SwitchCompat calorieSwitch = (SwitchCompat) findViewById(R.id.calorieSwitch);
        calorieSwitch.setChecked(myPref.getBoolean("calorie", false));
        calorieSwitch.setTrackTintList(new ColorStateList(states, trackColors));

        calorieSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences.Editor editor = myPref.edit();

                editor.putBoolean("calorie", b);

                if(!b) {
                    calorieValue.setText(null);
                    editor.putString("myCalorie", null);
                }

                editor.apply();
            }
        });

        calorieValue.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                calorieValue.clearFocus();
                InputMethodManager mInputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

                Log.i("my Calorie", calorieValue.getText().toString());

                SharedPreferences.Editor editor = myPref.edit();
                editor.putString("myCalorie", calorieValue.getText().toString());
                editor.apply();

                mInputMethodManager.hideSoftInputFromWindow(calorieValue.getWindowToken(), 0);
                return false;
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
