package com.fooding.userapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fooding.userapp.APIService;
import com.fooding.userapp.FoodingApplication;
import com.fooding.userapp.R;
import com.fooding.userapp.data.Food;
import com.fooding.userapp.data.model.Recipe;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ViewOtherRecipes extends AppCompatActivity {
    @BindView(R.id.title) TextView title;
    @BindView(R.id.filter) ImageButton filterBtn;
    @BindView(R.id.camera) ImageButton cameraBtn;
    @BindView(R.id.setting) ImageButton settingBtn;
    @BindView(R.id.recentlyViewed) ImageButton recentlyViewedBtn;
    @BindView(R.id.otherRecipes) ListView otherRecipes;
    @BindView(R.id.back) Button backBtn;

    public ArrayList<String> resultsO;
    public ArrayAdapter adapterO;
    public String serialNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_other_recipes);
        ButterKnife.bind(this);

        /*************************************************************************************************************/
        // font setting
        final FoodingApplication app = FoodingApplication.getInstance();
        SharedPreferences fontSP = app.getMyPref();

        final String pathT = fontSP.getString("titleFont", "fonts/BukhariScript-Regular.otf");
        final Typeface font = Typeface.createFromAsset(getAssets(), pathT);
        title.setTypeface(font);
        final String pathTK = fontSP.getString("titleFontk", "none");
        final Typeface fontTK = Typeface.createFromAsset(getAssets(), pathTK);

        final String pathK = fontSP.getString("koreanFont", "none");
        Typeface fontK = Typeface.createFromAsset(getAssets(), pathK);
        final String pathKB = fontSP.getString("boldKoreanFont", "none");
        Typeface fontKB = Typeface.createFromAsset(getAssets(), pathKB);
        backBtn.setTypeface(fontKB);
        /*************************************************************************************************************/

        /*************************************************************************************************************/
        // theme setting
        if(fontSP.getBoolean("theme", false)) { // dark theme
            // change background
            final View root = findViewById(R.id.viewOtherRecipeActivity).getRootView();
//            root.setBackgroundColor(Color.parseColor("#000000"));
            root.setBackgroundResource(R.drawable.dark_theme_background);

            // change text color
            title.setTextColor(Color.parseColor("#ffffff"));

            // change buttons
            filterBtn.setImageResource(R.mipmap.filter_white);
            cameraBtn.setImageResource(R.mipmap.camera_white);
            settingBtn.setImageResource(R.mipmap.settings_white);
            recentlyViewedBtn.setImageResource(R.mipmap.list_white);

            // change dividing lines
            View tmp = findViewById(R.id.title_bar);
            tmp.setBackgroundColor(Color.parseColor("#ffffff"));
            tmp = findViewById(R.id.menu_bar);
            tmp.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        /*************************************************************************************************************/

        Retrofit retrofit;
        APIService apiService;

        retrofit = new Retrofit.Builder().baseUrl(APIService.API_URL).addConverterFactory(GsonConverterFactory.create()).build();
        apiService = retrofit.create(APIService.class);

        final Food food = new Food();
        final Map<String, String> resultsO_map = new LinkedHashMap<String, String>();

        serialNumber = getIntent().getStringExtra("code");

        resultsO = new ArrayList<String>();
        adapterO = new ArrayAdapter(this, android.R.layout.simple_list_item_1, resultsO) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);

                textView.setTextColor(getResources().getColor(R.color.myBlack));

                final FoodingApplication app = FoodingApplication.getInstance();
                SharedPreferences myPref = app.getMyPref();

                final String pathT = myPref.getString("listViewFont", "fonts/NanumSquareRoundOTFR.otf");
                Typeface font = Typeface.createFromAsset(getAssets(), pathT);
                textView.setTypeface(font);

                final Integer fontSize = myPref.getInt("fontSize", 16);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, fontSize);

                if(myPref.getBoolean("theme", false)) { // dark theme
                    textView.setTextColor(Color.parseColor("#ffffff"));
                }

                return view;
            }
        };
        otherRecipes.setAdapter(adapterO);

        /////////////get id and name array list from preference//////////////////
        final SharedPreferences myPref = getSharedPreferences("Mypref", MODE_PRIVATE);
        ArrayList<String> idSet = new ArrayList<String>();
        final ArrayList<String> nameSet = new ArrayList<String>();
        if(myPref.getStringSet("userListkey", null) != null) {
            idSet.addAll(myPref.getStringSet("userListkey",null));
        }
        if(myPref.getStringSet("userList", null) != null) {
            nameSet.addAll(myPref.getStringSet("userList",null));
        }
        final Map<String, String> userfilterMap = new LinkedHashMap<String, String>();
        for(int i = 0; i< idSet.size();i++){
            userfilterMap.put(idSet.get(i),nameSet.get(i));
        }

        //server call for other recipes
        Call<List<Recipe>> comment1 = apiService.getRecipeEatable(idSet,serialNumber);
        if(!(idSet.isEmpty())){
            Log.i("idset", idSet.get(0));
        }
        comment1.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if(response.isSuccessful()) {
                    resultsO.clear();
                    resultsO_map.clear();

                    for(int i = 0; i < response.body().size(); i++) {
                        Recipe temp = response.body().get(i);
                        resultsO.add(temp.getName());
                        resultsO_map.put(temp.getName(),temp.getId());
                    }

                    otherRecipes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            final String chosenName = resultsO.get(position);
                            //Toast.makeText(getApplicationContext(),chosenName,Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ViewOtherRecipes.this,ViewRecipeActivity.class);
                            intent.putExtra("code", resultsO_map.get(chosenName));
                            startActivity(intent);
                            finish();
                        }
                    });

                    if(response.body().size()!=0) adapterO.notifyDataSetChanged();
                } else {
                    Log.i("Get Recipe", "Fail");
                }
            }
            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.i("Get Recipe", "Fail");
                t.printStackTrace();
            }
        });

        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewOtherRecipes.this, PopUpFilter.class));
                finish();
            }
        });

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewOtherRecipes.this, CameraActivity.class));
                finish();
            }
        });

        recentlyViewedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewOtherRecipes.this, recentlyViewedActivity.class));
                finish();
            }
        });

        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewOtherRecipes.this, SettingsActivity.class));
                finish();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewOtherRecipes.this,ViewRecipeActivity.class);
                intent.putExtra("code", serialNumber);
                startActivity(intent);
                finish();
            }
        });
    }
}
