package com.fooding.userapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
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
import android.widget.Toast;

import com.fooding.userapp.APIService;
import com.fooding.userapp.FoodingApplication;
import com.fooding.userapp.R;
import com.fooding.userapp.data.Filter;
import com.fooding.userapp.data.Food;
import com.fooding.userapp.data.model.Ingredient;
import com.fooding.userapp.data.model.Nutrient;
import com.fooding.userapp.data.model.Recipe;
import com.fooding.userapp.data.model.SikdangReview;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ViewRecipeActivity extends AppCompatActivity {
//    @BindView(R.id.sendout) Button sendoutbutton;
    @BindView(R.id.title) TextView title;
    /*@BindView(R.id.otherRecipesTitle) TextView otherRecipesTitle;
    @BindView(R.id.otherRecipes) ListView viewOtherRecipe;*/
    @BindView(R.id.ingredients) ListView ingredientList;
    @BindView(R.id.filter) ImageButton filterBtn;
    @BindView(R.id.camera) ImageButton cameraBtn;
    @BindView(R.id.setting) ImageButton settingBtn;
    @BindView(R.id.recentlyViewed) ImageButton recentlyViewedBtn;
//    @BindView(R.id.viewNutrient) Button viewNutrient;
    @BindView(R.id.calorieText) TextView calorieText;
    @BindView(R.id.calorieValue) TextView calorieValue;
    @BindView(R.id.text1_1) TextView text1_1;
    @BindView(R.id.text1_2) TextView text1_2;
    @BindView(R.id.text2_1) TextView text2_1;
    @BindView(R.id.text2_2) TextView text2_2;
    @BindView(R.id.text3_1) TextView text3_1;
    @BindView(R.id.text3_2) TextView text3_2;
    @BindView(R.id.text4_1) TextView text4_1;
    @BindView(R.id.text4_2) TextView text4_2;
    @BindView(R.id.text5_1) TextView text5_1;
    @BindView(R.id.text5_2) TextView text5_2;
    @BindView(R.id.text6_1) TextView text6_1;
    @BindView(R.id.text6_2) TextView text6_2;
    @BindView(R.id.text7_1) TextView text7_1;
    @BindView(R.id.text7_2) TextView text7_2;
    @BindView(R.id.text8_1) TextView text8_1;
    @BindView(R.id.text8_2) TextView text8_2;
    @BindView(R.id.viewOtherRecipeBtn) Button viewOtherRecipeBtn;
    @BindView(R.id.noNutrientInfo) TextView noNutrientInfo;
    @BindView(R.id.nutrientInfo) LinearLayout nutrientInfo;

    public ArrayList<String> results;
    public ArrayAdapter adapterI;
    public ArrayList<String> resultsO;
    public ArrayAdapter adapterO;
    public String serialNumber;
    private int filtersize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);
        ButterKnife.bind(this);

        /*************************************************************************************************************/
        // font setting
        final FoodingApplication app = FoodingApplication.getInstance();
        final SharedPreferences fontSP = app.getMyPref();

        final String pathT = fontSP.getString("titleFont", "none");
        Typeface font = Typeface.createFromAsset(getAssets(), pathT);
        final String pathTK = fontSP.getString("titleFontk", "none");
        final Typeface fontTK = Typeface.createFromAsset(getAssets(), pathTK);
        title.setTypeface(font);

        final String pathK = fontSP.getString("koreanFont", "none");
        Typeface fontK = Typeface.createFromAsset(getAssets(), pathK);
        final String pathKB = fontSP.getString("boldKoreanFont", "none");
        Typeface fontKB = Typeface.createFromAsset(getAssets(), pathKB);
        calorieText.setTypeface(fontKB);
        calorieValue.setTypeface(fontKB);
        text1_1.setTypeface(fontK);
        text1_2.setTypeface(fontK);
        text2_1.setTypeface(fontK);
        text2_2.setTypeface(fontK);
        text3_1.setTypeface(fontK);
        text3_2.setTypeface(fontK);
        text4_1.setTypeface(fontK);
        text4_2.setTypeface(fontK);
        text5_1.setTypeface(fontK);
        text5_2.setTypeface(fontK);
        text6_1.setTypeface(fontK);
        text6_2.setTypeface(fontK);
        text7_1.setTypeface(fontK);
        text7_2.setTypeface(fontK);
        text8_1.setTypeface(fontK);
        text8_2.setTypeface(fontK);
        noNutrientInfo.setTypeface(fontKB);


        viewOtherRecipeBtn.setTypeface(fontKB);
//        otherRecipesTitle.setTypeface(font);
        /*************************************************************************************************************/

        /*************************************************************************************************************/
        // theme setting
        if(fontSP.getBoolean("theme", false)) { // dark theme
            // change background
            final View root = findViewById(R.id.ViewRecipeActivity).getRootView();
//            root.setBackgroundColor(Color.parseColor("#000000"));
            root.setBackgroundResource(R.drawable.dark_theme_background);

            // change text color
            title.setTextColor(Color.parseColor("#ffffff"));
//            otherRecipesTitle.setTextColor(Color.parseColor("#ffffff"));

            ((LinearLayout)findViewById(R.id.nutrientInfo))
                    .setBackground(getResources().getDrawable(R.drawable.border_white_rectangle));
            calorieText.setTextColor(getResources().getColor(R.color.myWhite));
            calorieValue.setTextColor(getResources().getColor(R.color.myWhite));
            text1_1.setTextColor(getResources().getColor(R.color.myWhite));
            text1_2.setTextColor(getResources().getColor(R.color.myWhite));
            text2_1.setTextColor(getResources().getColor(R.color.myWhite));
            text2_2.setTextColor(getResources().getColor(R.color.myWhite));
            text3_1.setTextColor(getResources().getColor(R.color.myWhite));
            text3_2.setTextColor(getResources().getColor(R.color.myWhite));
            text4_1.setTextColor(getResources().getColor(R.color.myWhite));
            text4_2.setTextColor(getResources().getColor(R.color.myWhite));
            text5_1.setTextColor(getResources().getColor(R.color.myWhite));
            text5_2.setTextColor(getResources().getColor(R.color.myWhite));
            text6_1.setTextColor(getResources().getColor(R.color.myWhite));
            text6_2.setTextColor(getResources().getColor(R.color.myWhite));
            text7_1.setTextColor(getResources().getColor(R.color.myWhite));
            text7_2.setTextColor(getResources().getColor(R.color.myWhite));
            text8_1.setTextColor(getResources().getColor(R.color.myWhite));
            text8_2.setTextColor(getResources().getColor(R.color.myWhite));

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

        if(fontSP.getBoolean("translation",false)) {
            viewOtherRecipeBtn.setText("View Other Recipes");
        }

        Retrofit retrofit;
        APIService apiService;

        retrofit = new Retrofit.Builder().baseUrl(APIService.API_URL).addConverterFactory(GsonConverterFactory.create()).build();
        apiService = retrofit.create(APIService.class);

//        final FoodingApplication app = FoodingApplication.getInstance();
        final Food food = new Food();
        final Map<String, String> ingredients = new LinkedHashMap<String, String>();
        final Map<String, String> resultsO_map = new LinkedHashMap<String, String>();

        // serialNumber를 CameraActivity로부터 전달받거나 food에 일련번호를 저장하는 변수 추가
        serialNumber = getIntent().getStringExtra("code");
        results = new ArrayList<String>();
        adapterI = new ArrayAdapter(this, android.R.layout.simple_list_item_1, results) {
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

                if(position < filtersize){  // change text color of filtered ingredient
                    //view.setBackgroundColor(getResources().getColor(R.color.transparent_Red));
                    textView.setTextColor(getResources().getColor(R.color.Red));
                }

                return view;
            }
        };
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
        ingredientList.setAdapter(adapterI);
//        viewOtherRecipe.setAdapter(adapterO);
        /*Food food = app.getCurrentFood();
        serialNumber = food.getSerialNumber();*/

        final Map<String,String> tempMap = new LinkedHashMap<String, String>();


        /////////////get id and name array list from preference//////////////////
        final SharedPreferences myPref = getSharedPreferences("Mypref", MODE_PRIVATE);
        ArrayList<String> idSet = new ArrayList<String>();
        final ArrayList<String> nameSet = new ArrayList<String>();
        final ArrayList<String> EnnameSet = new ArrayList<String>();
        if(myPref.getStringSet("userListkey", null) != null) {
            idSet.addAll(myPref.getStringSet("userListkey",null));
        }
        if(myPref.getStringSet("userList", null) != null) {
            nameSet.addAll(myPref.getStringSet("userList",null));
        }
        if(myPref.getStringSet("userListEn", null) != null) {
            EnnameSet.addAll(myPref.getStringSet("userListEn",null));
        }
        final Map<String, String> userfilterMap = new LinkedHashMap<String, String>();
        final Map<String, String> userfilterMapEn = new LinkedHashMap<String, String>();
        for(int i = 0; i< idSet.size();i++){
            userfilterMapEn.put(idSet.get(i),EnnameSet.get(i));
            userfilterMap.put(idSet.get(i),nameSet.get(i));
        }

        // 레시피 이름 서버로부터 받아오기
        Call<Recipe> comment_title = apiService.getRecipeInfo(serialNumber);
        comment_title.enqueue(new Callback<Recipe>() {
            @Override
            public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                if(response.isSuccessful()) {
                    String recipeName = response.body().getName();
                    title.setText(recipeName);
                    title.setSelected(true);
                    Pattern p = Pattern.compile("[^a-zA-Z0-9]");
                    boolean hasSpecialChar = p.matcher(recipeName).find();
                    if(hasSpecialChar) {
                        Log.i("title korean", "true");
                        title.setTypeface(fontTK);
                    }
                    food.setIdOfRecipe(response.body().getId());

                    SharedPreferences recentlyViewed = getSharedPreferences("recentlyViewed", MODE_PRIVATE);
                    ArrayList<String> recipe = new ArrayList<String>();
                    if(recentlyViewed.getStringSet("recipeList", null) != null)
                        recipe.addAll(recentlyViewed.getStringSet("recipeList", null));
                    Set<String> recipeSet = new HashSet<String>(recipe);

                    if(recipeSet.size() >= 10) {
                        Iterator<String> iterator = recipeSet.iterator();
                        iterator.next();
                        iterator.remove();
                    }
                    recipeSet.add(serialNumber+"@"+recipeName);

                    for(String str : recipeSet) {
                        Log.i("recipe", str);
                    }

                    SharedPreferences.Editor editor = recentlyViewed.edit();
                    editor.putStringSet("recipeList", recipeSet);
                    editor.apply();

                    Log.i("Get Recipe Info", recipeName);
                } else {
                    Log.i("Get Recipe Info", "Fail");
                }
            }

            @Override
            public void onFailure(Call<Recipe> call, Throwable t) {
                Log.i("Get Recipe Info", "On Failure");
                t.printStackTrace();
            }
        });


        //server call for ingredient
        Call<List<Ingredient>> comment = apiService.getIngredient(serialNumber);
        comment.enqueue(new Callback<List<Ingredient>>() {
            @Override
            public void onResponse(Call<List<Ingredient>> call, Response<List<Ingredient>> response) {
                if(response.isSuccessful()) {
                    results.clear();
                    ingredients.clear();

                    for(int i = 0; i < response.body().size(); i++) {
                        String temp=null;
                        if(fontSP.getBoolean("translation",false))
                            temp = response.body().get(i).getEn_name();
                        else if(temp==null)
                            temp = response.body().get(i).getName();
//                        Log.i("oname", temp);
                        results.add(temp);
                        ingredients.put(response.body().get(i).getId(), fontSP.getBoolean("translation",false)?response.body().get(i).getEn_name():response.body().get(i).getName());
                    }

                    if(response.body().size()!=0) adapterI.notifyDataSetChanged();

                    //////////filtering&compare userfilterList with dbList////////////
                    Set<String> dbIdSet = ingredients.keySet(); //get id set on db
                    //Toast.makeText(getApplicationContext(),""+dbIdSet.toString(),Toast.LENGTH_SHORT).show();
                    Set<String> userIdSet = userfilterMap.keySet();
                    Set<String> resultSet = new HashSet<>(dbIdSet);
                    resultSet.retainAll(userIdSet);
                    ArrayList<String> otherList = new ArrayList<>(dbIdSet);
                    ArrayList<String> filteredList = new ArrayList<>(resultSet);

                    //int filteredCount = 0;
                    for(int i = 0; i< filteredList.size();i++){
                        tempMap.put(filteredList.get(i),ingredients.get(filteredList.get(i)));
                        //filteredCount = filteredCount + i;
                    }
                    filtersize = filteredList.size();
                    for(int i = 0; i< otherList.size();i++){
                        tempMap.put(otherList.get(i),ingredients.get(otherList.get(i)));
                    }
                    //Toast.makeText(getApplicationContext(),""+tempMap.toString(),Toast.LENGTH_SHORT).show();
                    results.clear();
                    results.addAll(tempMap.values());
                    ingredients.clear();
                    ingredients.putAll(tempMap);
                    //text.setTextColor(getResources().getColor(R.color.Red));

                    //View nn = ingredientList.getChildAt(1);
                    //Toast.makeText(getApplicationContext(),""+ingredientList..toString(),Toast.LENGTH_SHORT).show();
                    ///////////////////////////////////////////////////////////////////////
                    food.setIngredient(ingredients);
                    app.setCurrentFood(food);

                } else {
                    Log.i("Get Ingredient", "Fail");
                }
            }

            @Override
            public void onFailure(Call<List<Ingredient>> call, Throwable t) {
                Log.i("Get Ingredient", "Fail");
                t.printStackTrace();
            }
        });
        //*******************************
        //server call for other recipes
        Call<List<Recipe>> comment1 = apiService.getRecipeEatable(idSet,serialNumber);
        if(!(idSet.isEmpty())){
//            Log.i("idset", idSet.get(0));
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

                    /*viewOtherRecipe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            final String chosenName = resultsO.get(position);
                            //Toast.makeText(getApplicationContext(),chosenName,Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ViewRecipeActivity.this,ViewRecipeActivity.class);
                            intent.putExtra("code", resultsO_map.get(chosenName));
                            startActivity(intent);
                            finish();
                        }
                    });*/
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

        //////////// get nutrient here ///////////////
        //we will recieve nutrient info as {담백질, 몇gram}, the last one {calorie, 몇}
        final ArrayList<String> NutrientName = new ArrayList<String>();
        NutrientName.add(0, "Calorie"); //calorie
        NutrientName.add(1, "Na"); //carb
        NutrientName.add(2, "Carbohydrate"); //
        NutrientName.add(3, "Sugar"); //
        NutrientName.add(4, "Protein"); //
        NutrientName.add(5, "Fat"); //
        NutrientName.add(6,"TransFat" );
        NutrientName.add(7, "FattyAcid");
        NutrientName.add(8, "Cholesterol");
        food.setNutrientName(NutrientName);

        if(fontSP.getBoolean("translation",false))
        {
            calorieText.setText(NutrientName.get(0));
            text1_1.setText(NutrientName.get(1));
            text2_1.setText(NutrientName.get(2));
            text3_1.setText(NutrientName.get(3));
            text4_1.setText(NutrientName.get(4));
            text5_1.setText(NutrientName.get(5));
            text6_1.setText(NutrientName.get(6));
            text7_1.setText(NutrientName.get(7));
            text8_1.setText(NutrientName.get(8));
        }

        final ArrayList<String> NutrientGram = new ArrayList<String>();
        Call<List<Nutrient>> comment2 = apiService.getNutrient(serialNumber);
        comment2.enqueue(new Callback<List<Nutrient>>() {
            @Override
            public void onResponse(Call<List<Nutrient>> call, Response<List<Nutrient>> response) {
                String calorie = new String();
                int calorieCompare = -1;

                if(response.isSuccessful()) {
                    NutrientGram.clear();
                    NutrientName.clear();

                    if(response.body().get(0).getCal() != null ) {
                        calorie = response.body().get(0).getCal();
                        Log.i("calorie", calorie);
                        try {
                            calorieCompare = Integer.parseInt(calorie.substring(0,1));

                            Log.i("calorie parse:",Integer.toString(calorieCompare));
                        } catch (Exception e) {
                            ;
                        }
                        if(calorieCompare == 0) {
                            Log.i("0 cal", "true!!!");
                            noNutrientInfo.setVisibility(View.VISIBLE);
                            nutrientInfo.setVisibility(View.INVISIBLE);
                        }
                        NutrientGram.add(0,  calorie);
                        NutrientGram.set(0,NutrientGram.get(0).substring(0,NutrientGram.get(0).indexOf(".")+2)+" kcal");

                        NutrientGram.add(1, response.body().get(0).getNa());
                        NutrientGram.set(1,NutrientGram.get(1).substring(0,NutrientGram.get(1).indexOf(".")+2)+" mg");

                        NutrientGram.add(2, response.body().get(0).getCarb());
                        NutrientGram.set(2,NutrientGram.get(2).substring(0,NutrientGram.get(2).indexOf(".")+2)+" g");

                        NutrientGram.add(3, response.body().get(0).getSugar());
                        NutrientGram.set(3,NutrientGram.get(3).substring(0,NutrientGram.get(3).indexOf(".")+2)+" g");

                        NutrientGram.add(4, response.body().get(0).getProtein());
                        NutrientGram.set(4,NutrientGram.get(4).substring(0,NutrientGram.get(4).indexOf(".")+2)+" g");

                        NutrientGram.add(5, response.body().get(0).getFat());
                        NutrientGram.set(5,NutrientGram.get(5).substring(0,NutrientGram.get(5).indexOf(".")+2)+" g");

                        NutrientGram.add(6, response.body().get(0).getTransFattyAcid());
                        NutrientGram.set(6,NutrientGram.get(6).substring(0,NutrientGram.get(6).indexOf(".")+2)+" mg");

                        NutrientGram.add(7, response.body().get(0).getFattyAcid());
                        NutrientGram.set(7,NutrientGram.get(7).substring(0,NutrientGram.get(7).indexOf(".")+2)+" mg");
                        //NutrientGram.add(8, response.body().get(0).getCholesterol() + " mg");
                        String test = response.body().get(0).getCholesterol();
                        Log.d("col", test);
                        NutrientGram.add(8, test.substring(0,test.indexOf(".")+2)+ " mg");
                    }
                    else{
                        String str[]={" kcal", " mg", " g", " g", " g", " g", " mg", " mg", " mg"};
                        for(int i = 0; i<9;i++){
                            NutrientGram.add(i,"0"+str[i]);
                        }
                        calorie = "0";
                    }
                    food.setNutrientGram(NutrientGram);

                    calorieValue.setText(NutrientGram.get(0));
                    // check calorie
                    if(fontSP.getBoolean("calorie", false) && fontSP.getString("myCalorie", null) != null) {
                        if(Integer.parseInt(fontSP.getString("myCalorie", null)) < Float.parseFloat(calorie))
                            calorieValue.setTextColor(getResources().getColor(R.color.Red));
                        else
                            calorieValue.setTextColor(getResources().getColor(R.color.myGreen));
                    }

                    text1_2.setText(NutrientGram.get(1));
                    text2_2.setText(NutrientGram.get(2));
                    text3_2.setText(NutrientGram.get(3));
                    text4_2.setText(NutrientGram.get(4));
                    text5_2.setText(NutrientGram.get(5));
                    text6_2.setText(NutrientGram.get(6));
                    text7_2.setText(NutrientGram.get(7));
                    text8_2.setText(NutrientGram.get(8));

                } else {
                    Log.i("Get Nutrient", "Fail");
                }
            }
            @Override
            public void onFailure(Call<List<Nutrient>> call, Throwable t) {
                Log.i("Get Nutrient", "Fail");
                t.printStackTrace();
            }
        });

        /////////////////////////////////////////////

        /*
        //////////////////get review////////////////////////
        final ArrayList<String> Review = new ArrayList<String>();
        Call<List<SikdangReview>> comment3 = apiService.getReview(food.getIdOfRecipe());
        comment3.enqueue(new Callback<List<SikdangReview>>() {
            @Override
            public void onResponse(Call<List<SikdangReview>> call, Response<List<SikdangReview>> response) {
                if(response.isSuccessful()) {
                    //clear all array list here

                    for(int i = 0; i < response.body().size(); i++) {
                        //add nutrient here
                        Review.add(response.body().get(i).getName());
                    }

                } else {
                    Log.i("Get review", "Fail");
                }
            }
            @Override
            public void onFailure(Call<List<SikdangReview>> call, Throwable t) {
                Log.i("Get review", "Fail");
                t.printStackTrace();
            }
        });
        ////////////////////////////////////////////////////
        */

        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewRecipeActivity.this, PopUpFilter.class));
                finish();
            }
        });

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewRecipeActivity.this, CameraActivity.class));
                finish();
            }
        });

        recentlyViewedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewRecipeActivity.this, recentlyViewedActivity.class));
                finish();
            }
        });

        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewRecipeActivity.this, SettingsActivity.class));
                finish();
            }
        });

        viewOtherRecipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewRecipeActivity.this, ViewOtherRecipes.class);
                intent.putExtra("code", serialNumber);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed(); // this can go before or after your stuff below
        // do your stuff when the back button is pressed
        Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        // super.onBackPressed(); calls finish(); for you
    }
}