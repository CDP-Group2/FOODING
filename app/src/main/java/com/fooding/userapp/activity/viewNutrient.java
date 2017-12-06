package com.fooding.userapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.fooding.userapp.FoodingApplication;
import com.fooding.userapp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ajidmasterz on 12/6/2017.
 */

public class viewNutrient extends AppCompatActivity{
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.calorieText)
    TextView calorieText;
    @BindView(R.id.ingredients) ListView ingredientList;
    @BindView(R.id.filter)
    ImageButton filterBtn;
    @BindView(R.id.camera) ImageButton cameraBtn;
    @BindView(R.id.setting) ImageButton settingBtn;
    @BindView(R.id.recentlyViewed) ImageButton recentlyViewedBtn;
    @BindView(R.id.viewRecipe)
    Button viewRecipe;

    ArrayAdapter nutrientAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewnutrient);
        ButterKnife.bind(this);

        final FoodingApplication app = FoodingApplication.getInstance();
        String serialNumber = getIntent().getStringExtra("recipeName");
        ArrayList<String> nutrientInfo = getIntent().getStringArrayListExtra("gram");
        ArrayList<String> nutrientName = getIntent().getStringArrayListExtra("Nutrientname");

        ArrayList<String> outputNutrient = new ArrayList<>();
        outputNutrient.add(nutrientName.get(1) + ": " + nutrientInfo.get(1)+ "g");
        outputNutrient.add(nutrientName.get(2) + ": " +  nutrientInfo.get(2)+ "g");
        outputNutrient.add(nutrientName.get(3) + ": " +  nutrientInfo.get(3)+ "g");
        outputNutrient.add(nutrientName.get(4) + ": " +  nutrientInfo.get(4)+ "g");
        outputNutrient.add(nutrientName.get(5) + ": " +  nutrientInfo.get(5)+ "g");
        outputNutrient.add(nutrientName.get(6) + ": " +  nutrientInfo.get(6)+ "g");
        outputNutrient.add(nutrientName.get(7) + ": " +  nutrientInfo.get(7)+ "g");
        outputNutrient.add(nutrientName.get(8) + ": " +  nutrientInfo.get(8)+ "g");

        nutrientAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, outputNutrient);
        ingredientList.setAdapter(nutrientAdapter);

        title.setText(serialNumber);

        calorieText.setText(nutrientName.get(0) + ": " + nutrientInfo.get(0));



        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(viewNutrient.this, PopUpFilter.class));
                finish();
            }
        });

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(viewNutrient.this, CameraActivity.class));
                finish();
            }
        });

        recentlyViewedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(viewNutrient.this, recentlyViewedActivity.class));
                finish();
            }
        });

        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(viewNutrient.this, SettingsActivity.class));
                finish();
            }
        });
        viewRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(viewNutrient.this,ViewRecipeActivity.class);
                String serialNum = getIntent().getStringExtra("code");
                intent.putExtra("code",serialNum);
                startActivity(intent);
                finish();
            }
        });
    }

}
