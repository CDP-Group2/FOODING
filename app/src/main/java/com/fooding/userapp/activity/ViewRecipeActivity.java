package com.fooding.userapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fooding.userapp.APIService;
import com.fooding.userapp.FoodingApplication;
import com.fooding.userapp.R;
import com.fooding.userapp.data.Filter;
import com.fooding.userapp.data.Food;
import com.fooding.userapp.data.model.Ingredient;
import com.fooding.userapp.data.model.Recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ViewRecipeActivity extends AppCompatActivity {
    @BindView(R.id.sendout) Button sendoutbutton;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.otherRecipes) ListView viewOtherRecipe;
    @BindView(R.id.ingredients) ListView ingredientList;

    public ArrayList<String> results;
    public ArrayList<String> results1;
    public ArrayAdapter adapterI;
    public ArrayAdapter adapterO;
    public String serialNumber;
    public String serialNumber1;
    private Map<String,String> results1map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);
        ButterKnife.bind(this);

        Retrofit retrofit;
        APIService apiService;

        retrofit = new Retrofit.Builder().baseUrl(APIService.API_URL).addConverterFactory(GsonConverterFactory.create()).build();
        apiService = retrofit.create(APIService.class);

        final FoodingApplication app = FoodingApplication.getInstance();
        final Food food = new Food();
        final Map<String, String> ingredients = new LinkedHashMap<String, String>();
        // serialNumber를 CameraActivity로부터 전달받거나 food에 일련번호를 저장하는 변수 추가
        serialNumber = getIntent().getStringExtra("code");
        results = new ArrayList<String>();
        results1 = new ArrayList<String>();

        //adapterI.setsize(2);
        /*Food food = app.getCurrentFood();
        serialNumber = food.getSerialNumber();*/

        final Map<String,String> tempMap = new LinkedHashMap<String, String>();

        /////////////get id and name array list from preference//////////////////
        SharedPreferences myPref = getSharedPreferences("Mypref", MODE_PRIVATE);
        ArrayList<String> idSet = new ArrayList<>(myPref.getStringSet("userListkey",null));
        ArrayList<String> nameSet = new ArrayList<>(myPref.getStringSet("userList",null));
        final Map<String, String> userfilterMap = new LinkedHashMap<String, String>();
        for(int i = 0; i< idSet.size();i++){
            userfilterMap.put(idSet.get(i),nameSet.get(i));
        }

        Call<List<Ingredient>> comment = apiService.getIngredient(serialNumber);
        comment.enqueue(new Callback<List<Ingredient>>() {
            @Override
            public void onResponse(Call<List<Ingredient>> call, Response<List<Ingredient>> response) {
                if(response.isSuccessful()) {
                    results.clear();
                    ingredients.clear();

                    for(int i = 0; i < response.body().size(); i++) {
                        String temp = response.body().get(i).getName();
                        Log.i("oname", temp);
                        results.add(temp);
                        ingredients.put(response.body().get(i).getId(), response.body().get(i).getName());
                    }

                    //////////filtering&compare userfilterList with dbList////////////
                    Set<String> dbIdSet = ingredients.keySet(); //get id set on db
                    //Toast.makeText(getApplicationContext(),""+dbIdSet.toString(),Toast.LENGTH_SHORT).show();
                    Set<String> userIdSet = userfilterMap.keySet();
                    Set<String> resultSet = new HashSet<>(dbIdSet);
                    resultSet.retainAll(userIdSet);
                    ArrayList<String> otherList = new ArrayList<>(dbIdSet);
                    ArrayList<String> filteredList = new ArrayList<>(resultSet);

                    int size = 0;
                    for(int i = 0; i< filteredList.size();i++){
                        tempMap.put(filteredList.get(i),ingredients.get(filteredList.get(i)));
                        size = size + 1;
                    }
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

                    final int sizetemp = size;
                    adapterI = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, results){
                        @Override
                        public View getView(int position,View convertView, ViewGroup parent) {
                            View row = super.getView(position,convertView,parent);
                            if(position<sizetemp)row.setBackgroundColor(getResources().getColor(R.color.Red));
                            else{
                                row.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                            }
                            return row;
                        }
                    };
                    ingredientList.setAdapter(adapterI);

                    if(response.body().size()!=0) adapterI.notifyDataSetChanged();


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

        results1map = new LinkedHashMap<String,String>();
        adapterO = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, results1){
            @Override
            public View getView(int position,View convertView, ViewGroup parent) {
                View row = super.getView(position,convertView,parent);
                row.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                return row;
            }
        };
        viewOtherRecipe.setAdapter(adapterO);


        Call<List<Recipe>> comment1 = apiService.getRecipeEatable(idSet,serialNumber);
        if(!(idSet.isEmpty())){
            Log.i("idset", idSet.get(0));
        }
        comment1.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if(response.isSuccessful()) {
                    results1.clear();

                    for(int i = 0; i < response.body().size(); i++) {
                        Recipe temp = response.body().get(i);
                        //Log.i("rname", temp);
                        results1.add(temp.getName());
                        results1map.put(temp.getName(),temp.getId());
                    }

                    viewOtherRecipe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            final String chosenName = results1.get(position);
                            //Toast.makeText(getApplicationContext(),chosenName,Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ViewRecipeActivity.this,ViewRecipeActivity.class);
                            intent.putExtra("code", results1map.get(chosenName));
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

        sendoutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewRecipeActivity.this, SendOutQRActivity.class));
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