package com.fooding.userapp.activity;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fooding.userapp.FoodingApplication;
import com.fooding.userapp.R;
import com.fooding.userapp.data.Food;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NFCActivity extends AppCompatActivity {
//    @BindView(R.id.my_page) Button my_pagebutton;
    @BindView(R.id.filter) ImageButton filterbutton;
//    @BindView(R.id.Camera) Button camerabutton;
//    @BindView(R.id.viewrecipe) Button viewrecipebutton;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.camera) ImageButton cameraBtn;
    @BindView(R.id.recentlyViewed) ImageButton recentlyViewedBtn;
    @BindView(R.id.setting) ImageButton settingBtn;
    @BindView(R.id.nfc) ImageView nfc;
    @BindView(R.id.msg) TextView msg;

    NfcAdapter mNfc;
    PendingIntent pIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);
        ButterKnife.bind(this);

        /****************
         *******************************
         ********************/
        //camera 찍어서 아래처럼 Food 저장 data-Food dir 참고

        /*************************************************************************************************************/
        // font setting
        final FoodingApplication app = FoodingApplication.getInstance();
        SharedPreferences fontSP = app.getMyPref();

        final String pathT = fontSP.getString("titleFont", "none");
        Typeface font = Typeface.createFromAsset(getAssets(), pathT);
        title.setTypeface(font);
        final String pathK = fontSP.getString("koreanFont", "none");
        Typeface fontK = Typeface.createFromAsset(getAssets(), pathK);
        msg.setTypeface(fontK);
        /*************************************************************************************************************/

        /*************************************************************************************************************/
        // theme setting
        if (fontSP.getBoolean("theme", false)) { // dark theme
            // change background
            final View root = findViewById(R.id.NFCActivity).getRootView();
//            root.setBackgroundColor(Color.parseColor("#000000"));
            root.setBackgroundResource(R.drawable.dark_theme_background);

            // change text color
            title.setTextColor(Color.parseColor("#ffffff"));
            msg.setTextColor(getResources().getColor(R.color.myWhite));

            // change buttons
            filterbutton.setImageResource(R.mipmap.filter_white);
            cameraBtn.setImageResource(R.mipmap.camera_white);
            settingBtn.setImageResource(R.mipmap.settings_white);
            recentlyViewedBtn.setImageResource(R.mipmap.list_white);

            // change dividing lines
            View tmp = findViewById(R.id.title_bar);
            tmp.setBackgroundColor(Color.parseColor("#ffffff"));
            tmp = findViewById(R.id.menu_bar);
            tmp.setBackgroundColor(Color.parseColor("#ffffff"));

            // change image
            nfc.setImageResource(R.mipmap.noun_white);
        }
        /*************************************************************************************************************/

        if(fontSP.getBoolean("translation",false)) {
            msg.setText("PLEASE TOUCH THE NFC TAG\nON THE BACK OF YOUR DEVICE.");
        }

        final ImageView nfc_iv = (ImageView) findViewById(R.id.nfc);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha_anim);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        nfc_iv.startAnimation(anim);

        mNfc = NfcAdapter.getDefaultAdapter(this);
        if (mNfc == null) {
            // NFC 미지원단말
            Toast.makeText(getApplicationContext(), "No NFC on your Device", Toast.LENGTH_SHORT).show();
            finish();
        }
        Intent intent = new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pIntent = PendingIntent.getActivity(this, 0, intent, 0);


        //위처럼 food 정보 저장한다음 서버로 ㄲ 하는 작업 시
        //Food food = FoodingApplication.getInstance().getCurrentFood();
        //처럼 food 정보 가져올 수 있다

        /*my_pagebutton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent=new Intent(NFCActivity.this, MyPageActivity.class);
                //intent.putExtra("date",Integer.parseInt(date.getText().toString().replaceAll("[^0-9]", "")));
                startActivity(intent);
            }
        });*/

        filterbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NFCActivity.this, PopUpFilter.class));
                finish();
            }
        });

        /*camerabutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NFCActivity.this, CameraActivity.class));
                finish();
            }
        });*/

        /*viewrecipebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NFCActivity.this, ViewRecipeActivity.class));
                finish();
            }
        });*/

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NFCActivity.this, CameraActivity.class));
                finish();
            }
        });

        recentlyViewedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NFCActivity.this, recentlyViewedActivity.class));
                finish();
            }
        });

        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NFCActivity.this, SettingsActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mNfc!=null)
            mNfc.enableForegroundDispatch(this,pIntent,null,null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mNfc != null)
            mNfc.disableForegroundDispatch(this);
    }

        @Override
        protected void onNewIntent(Intent intent) {
            super.onNewIntent(intent);
            if (intent == null)
                return;
            Parcelable msg[] = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (msg != null) {
                String dMsg = setReadTagData((NdefMessage) msg[0]);
                Toast.makeText(this, dMsg, Toast.LENGTH_SHORT).show();
                Intent rintent = new Intent(NFCActivity.this,ViewRecipeActivity.class);
                rintent.putExtra("code", dMsg);
                startActivity(rintent);
                finish();
            }
        }

        public String setReadTagData(NdefMessage ndefmsg) {
            String strRec =null;
            if (ndefmsg == null) {
                return strRec;
            }
            NdefRecord[] records = ndefmsg.getRecords();
            for (NdefRecord rec : records) {
                byte[] payload = rec.getPayload();
                // 버퍼 데이터를 인코딩 변환
                strRec = byteDecoding(payload);
            }
            return strRec;
        }
        public String byteDecoding(byte[] buf) {
            String strText="";
            String textEncoding = ((buf[0] & 0200) == 0) ? "UTF-8" : "UTF-16";
            int langCodeLen = buf[0] & 0077;

            try {
                strText = new String(buf, langCodeLen + 1,
                        buf.length - langCodeLen - 1, textEncoding);
            } catch(Exception e) {
                Log.d("tag1", e.toString());
            }
            return strText;
        }
}
