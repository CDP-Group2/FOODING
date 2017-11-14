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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fooding.userapp.APIService;
import com.fooding.userapp.FoodingApplication;
import com.fooding.userapp.R;
import com.fooding.userapp.data.Food;
import com.fooding.userapp.data.model.Ingredient;

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
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.myFilteredList) TextView filterList;

    String serialNumber;
    public ArrayList<String> results;


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

        /*Food food = app.getCurrentFood();
        serialNumber = food.getSerialNumber();*/

        /////////////get id and name array list from preference//////////////////
        SharedPreferences myPref = getSharedPreferences("Mypref", MODE_PRIVATE);
        ArrayList<String> idSet = new ArrayList<>(myPref.getStringSet("userListkey",null));
        ArrayList<String> nameSet = new ArrayList<>(myPref.getStringSet("userList",null));

        Map<String, String> userfilterMap = new LinkedHashMap<String, String>();
        for(int i = 0; i< idSet.size();i++){
            userfilterMap.put(idSet.get(i),nameSet.get(i));
        }
        ////////////////////////////////////////////////////////////////////////////

        ////////////temporary db map from QRcode//////////////////
        final Map<String, String> dbMap=new HashMap<String, String>();
        dbMap.put("40","ketchap2");
        dbMap.put("30","ketchap3");
        dbMap.put("1","ketchap1");
        dbMap.put("6","ketchap1");
        dbMap.put("4","ketchap1");
        dbMap.put("7","ketchap1");
        dbMap.put("10","ketchap1");
        dbMap.put("2","ketchap1");
        dbMap.put("3","ketchap1");
        ///////////////////////////////////////////////

        Call<List<Ingredient>> comment = apiService.getIngredient(serialNumber);
        comment.enqueue(new Callback<List<Ingredient>>() {
            @Override
            public void onResponse(Call<List<Ingredient>> call, Response<List<Ingredient>> response) {
                if(response.isSuccessful()) {
                    results.clear();
                    ingredients.clear();

                    for(int i = 0; i < response.body().size(); i++) {
                        results.add(response.body().get(i).getName());
                        ingredients.put(response.body().get(i).getId(), response.body().get(i).getName());
                        /*when data can be parse from server add this:
                        dbMap.put(response.body().get(i).getId(),response.body().get(i).getName());
                        */
                    }

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

        //////////filtering&compare userfilterList with dbList////////////
        Map<String,String> tempMap = new LinkedHashMap<String, String>();
        Set<String> dbIdSet = dbMap.keySet(); //get id set on db
        Set<String> userIdSet = userfilterMap.keySet();
        Set<String> resultSet = new HashSet<>(dbIdSet);
        resultSet.retainAll(userIdSet);
        ArrayList<String> otherList = new ArrayList<>(dbIdSet);
        ArrayList<String> filteredList = new ArrayList<>(resultSet);

        int higlightedSize = 0;
        for(int i = 0; i< filteredList.size();i++){
            tempMap.put(filteredList.get(i),dbMap.get(filteredList.get(i)));
            higlightedSize = higlightedSize + dbMap.get(filteredList.get(i)).length();
        }
        Toast.makeText(getApplicationContext(),""+higlightedSize,Toast.LENGTH_SHORT).show();
        for(int i = 0; i< otherList.size();i++){
            tempMap.put(otherList.get(i),dbMap.get(otherList.get(i)));
        }

        String all = String.valueOf(tempMap.values());
        filterList.setText(all);

        String text = filterList.getText().toString();
        if(filteredList.size() != 0){
            SpannableString spannableString = new SpannableString(text);
            spannableString.setSpan(new ForegroundColorSpan(Color.RED), 1 , higlightedSize+(filteredList.size()*2), 0);
            filterList.setText(spannableString);
        }
        ////////////////////////////////////////////////////////////////////////

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