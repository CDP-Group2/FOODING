package com.fooding.userapp.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);
        ButterKnife.bind(this);

        /****************
         *******************************
         ********************/
        //camera 찍어서 아래처럼 Food 저장 data-Food dir 참고

        // font setting
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/BukhariScript-Regular.otf");
        title.setTypeface(font);

        final ImageView nfc_iv = (ImageView)findViewById(R.id.nfc);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha_anim);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        nfc_iv.startAnimation(anim);

        FoodingApplication app = FoodingApplication.getInstance();
        Food food=new Food();
        String temp="오뚜기 케챱";
        food.setName(temp);
        app.setCurrentFood(food);

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
                startActivity(new Intent(NFCActivity.this, FilterActivity.class));
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
    }
}
