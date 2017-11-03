package com.fooding.userapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fooding.userapp.FoodingApplication;
import com.fooding.userapp.R;
import com.fooding.userapp.data.Food;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CameraActivity extends AppCompatActivity {
    @BindView(R.id.my_page)
    Button my_pagebutton;
    @BindView(R.id.filter) Button filterbutton;
    @BindView(R.id.title)
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);

        /****************
        *******************************
        ********************/
        //camera 찍어서 아래처럼 Food 저장 data-Food dir 참고

        FoodingApplication app = FoodingApplication.getInstance();
        Food food=new Food();
        String temp="오뚜기 케챱";
        food.setName(temp);
        app.setCurrentFood(food);

        //위처럼 food 정보 저장한다음 서버로 ㄲ 하는 작업 시
        //Food food = FoodingApplication.getInstance().getCurrentFood();
        //처럼 food 정보 가져올 수 있다

        my_pagebutton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent=new Intent(CameraActivity.this, MyPageActivity.class);
                //intent.putExtra("date",Integer.parseInt(date.getText().toString().replaceAll("[^0-9]", "")));
                startActivity(intent);
            }
        });

        filterbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CameraActivity.this, FilterActivity.class));
            }
        });
    }
}
