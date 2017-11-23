package com.fooding.userapp.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.fooding.userapp.FoodingApplication;
import com.fooding.userapp.R;
import com.fooding.userapp.data.Food;
import com.google.zxing.ResultPoint;

import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CameraActivity extends AppCompatActivity {
    private DecoratedBarcodeView barcodeView;
    @BindView(R.id.filter) ImageButton filterbutton;
    @BindView(R.id.NFC) ImageButton nfcbutton;
    @BindView(R.id.fooding) TextView title;
    @BindView(R.id.recentlyViewed) ImageButton recentlyViewedBtn;
    @BindView(R.id.setting) ImageButton settingBtn;

    private String lastText;

    //callback when barcode scanned

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);

        /*************************************************************************************************************/
        // font setting
        final FoodingApplication app = FoodingApplication.getInstance();
        SharedPreferences fontSP = app.getMyPref();
        // Toast.makeText(getApplicationContext(), fontSP.getString("titleFont", "none"), Toast.LENGTH_SHORT).show();
        final String path = fontSP.getString("titleFont", "none");
        // Toast.makeText(getApplicationContext(), path, Toast.LENGTH_SHORT).show();

        Typeface font = Typeface.createFromAsset(getAssets(), path);
        title.setTypeface(font);
        /*************************************************************************************************************/

        /*************************************************************************************************************/
        // theme setting
        if(fontSP.getBoolean("theme", false)) { // dark theme
            // change background
            final View root = findViewById(R.id.cameraActivity).getRootView();
//            root.setBackgroundColor(Color.parseColor("#000000"));
            root.setBackgroundResource(R.drawable.dark_theme_background);

            // change text color
            title.setTextColor(Color.parseColor("#ffffff"));

            // change buttons
            filterbutton.setImageResource(R.mipmap.filter_white);
            nfcbutton.setImageResource(R.mipmap.nfc_white);
            recentlyViewedBtn.setImageResource(R.mipmap.list_white);
            settingBtn.setImageResource(R.mipmap.settings_white);
        }
        /*************************************************************************************************************/

        //set barcode instant****************
        // camera permission for marshmellow
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // Should we show an explanation?
                if (this.shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    // No explanation needed, we can request the permission.
                    this.requestPermissions(
                            new String[]{Manifest.permission.CAMERA},
                            1);
                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }
        }
        barcodeView = (DecoratedBarcodeView) findViewById(R.id.barcode_scanner);
        barcodeView.setStatusText("");
        barcodeView.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                if(result.getText() == null || result.getText().equals(lastText)) {
                    // Prevent duplicate scans
                    return;
                }

                lastText = result.getText();
                barcodeView.setStatusText(result.getText());

                // QR코드에 담긴 일련번호 ViewRecipeActivity로 보내줘야 함
                Intent intent = new Intent(CameraActivity.this,ViewRecipeActivity.class);
                intent.putExtra("code", lastText);
                startActivity(intent);
                finish();
            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {
            }
        });
        //****************
        //camera 찍어서 아래처럼 Food 저장 data-Food dir 참고

//        FoodingApplication app = FoodingApplication.getInstance();
        Food food=new Food();
        String temp="오뚜기 케챱";
        food.setName(temp);
        Map<String, String> ttt=new LinkedHashMap<String, String>();
        ttt.put("a123","ketchap1");
        ttt.put("b123","ketchap2");
        ttt.put("c123","ketchap3");
        food.setIngredient(ttt);
        app.setCurrentFood(food);

        //위처럼 food 정보 저장한다음 서버로 ㄲ 하는 작업 시
        //Food food = FoodingApplication.getInstance().getCurrentFood();
        //처럼 food 정보 가져올 수 있다

        filterbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CameraActivity.this, PopUpFilter.class));
                finish();
            }
        });

        nfcbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CameraActivity.this, NFCActivity.class));
                finish();
            }
        });

        recentlyViewedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CameraActivity.this, recentlyViewedActivity.class));
                finish();
            }
        });

        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CameraActivity.this, SettingsActivity.class));
                finish();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        resumeScanner();
    }

    protected void resumeScanner() {
        if (!barcodeView.isActivated())
            barcodeView.resume();
    }

    protected void pauseScanner() {
        barcodeView.pause();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseScanner();
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
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
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
