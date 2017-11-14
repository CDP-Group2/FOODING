package com.fooding.userapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.fooding.userapp.APIService;
import com.fooding.userapp.R;
import com.fooding.userapp.data.Filter;
import com.fooding.userapp.data.model.Ingredient;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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

public class FilterActivity extends AppCompatActivity {
    @BindView(R.id.resultList) ListView resultListView;
    @BindView(R.id.searchText) EditText searchText;
    @BindView(R.id.addButton) Button addBtn;
    @BindView(R.id.MylistBtn) Button MylistBtn;

    @BindView(R.id.JsonTextview) TextView debuggingView; //debugging purpose

    public ArrayList<String> IngridientId; //id list of ingridient
    public ArrayList<String> IngridientName; //list of ingridient name on user filter list .. user preferences
    public ArrayList<String> resultList; //list of ingridient name on user filter list .. user preferences
    public ArrayAdapter<String> adapter;

    //Map x = HashMap;

    Filter filter = new Filter(); //user filter
    private Map<String, String> dbIngridient = new LinkedHashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        ButterKnife.bind(this);

        IngridientId = new ArrayList<String>(); //initialization of ingridient id list
        IngridientName = new ArrayList<String>(); //initialization of ingridient name list

        /*
        dbIngridient.put("1","one");
        dbIngridient.put("2","two");
        */

        resultList =  new ArrayList<String>(); //result list to show in listview

        resultList.addAll(dbIngridient.values());
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, resultList) ;
        resultListView.setAdapter(adapter);


        TextWatcher watcher = new TextWatcher()
        {
            @Override
            public void afterTextChanged(Editable s) {
                //텍스트 변경 후 발생할 이벤트를 작성.
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                //텍스트의 길이가 변경되었을 경우 발생할 이벤트를 작성.
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, final int count)
            {
                Retrofit retrofit;
                APIService apiService;

                retrofit = new Retrofit.Builder().baseUrl(APIService.API_URL).addConverterFactory(GsonConverterFactory.create()).build();


                apiService = retrofit.create(APIService.class);

                final String text = searchText.getText().toString();
                final Call<List<Ingredient>> comment = apiService.searchIngredient(text);

                comment.enqueue(new Callback<List<Ingredient>>() {
                    @Override
                    public void onResponse(Call<List<Ingredient>> call, Response<List<Ingredient>> response) {
                        if(response.isSuccessful()){
                            ///reset list///
                            IngridientId.clear();
                            IngridientName.clear();
                            dbIngridient.clear();
                            resultList.clear();
                            ///////////////
                            resultListView.setVisibility(View.VISIBLE);


                            for(int i=0;i<response.body().size();i++){
                                IngridientId.add(response.body().get(i).getId()); //get id list of ingridient
                                IngridientName.add(response.body().get(i).getName()); //get name list of the ingridient
                                String id = response.body().get(i).getId();
                                String name = response.body().get(i).getName().toString();
                                dbIngridient.put(name,id); //adding all to map
                            }

                            resultList.addAll(dbIngridient.keySet());


                            ///debugging view////
                            //debuggingView.setText("Id matched: " + (Integer.toString(resultList.size())));


                            for (int counter = 0; counter < IngridientName.size(); counter++) {
                                String temp = IngridientName.get(counter);
                                //Toast.makeText(getApplicationContext(), temp, Toast.LENGTH_LONG).show();
                                if(temp.contains(text)){
                                    searchText.setTextColor(getResources().getColor(R.color.Red));
                                }
                                else{
                                    searchText.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    resultListView.setVisibility(View.INVISIBLE);
                                }
                            }

                            adapter.notifyDataSetChanged();

                            resultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                                    final String chosenName = (String) resultListView.getItemAtPosition(i).toString();
                                    //final String chosenId = (String) dbIngridient.get(chosenName).toString();
                                    Toast.makeText(getApplicationContext(),chosenName+"\n",Toast.LENGTH_SHORT).show();
                                    //final int chosenId = 823;
                                    //if(IngridientName.contains(temp)){

                                    //}
                                    //Toast.makeText(getApplicationContext(),"You selected: id["+String.valueOf(ChosenId)+"]"+"["+chosenName+"]",Toast.LENGTH_SHORT).show();


                                    View.OnClickListener Listen2Btn = new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            filter.addItem2UserList(chosenName); //add string to userList
                                            SharedPreferences myPref = getSharedPreferences("Mypref", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = myPref.edit();
                                            ArrayList<String> temp1 = new ArrayList<String>(filter.getUserList());
                                            Set<String> set = new HashSet<String>(temp1);
                                           ///Set<String> set1 = new HashSet<String>(new ArrString.valueOf(chosenId));
                                            editor.putStringSet("userList",set);
                                           // editor.putStringSet("userListkey",set1);
                                            editor.apply();
                                            startActivity(new Intent(FilterActivity.this, PopUpFilter.class));
                                        }

                                    };
                                    addBtn.setOnClickListener(Listen2Btn);
                                    resultListView.clearChoices();
                                    adapter.notifyDataSetChanged();
                                }
                            });

                            if(response.body().size()!=0); adapter.notifyDataSetChanged();
                        }
                        else{
                            Log.i("Test1", "fail");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Ingredient>> call, Throwable t) {
                        Log.i("Test1", "onfailure");
                        t.printStackTrace();
                    }


                });

                //텍스트가 변경될때마다 발생할 이벤트를 작성.
                if (searchText.isFocusable())
                {
                    //mXMLBuyCount EditText 가 포커스 되어있을 경우에만 실행됩니다.
                }
            }
        };

        searchText.addTextChangedListener(watcher);


        View.OnClickListener Listen2Btn1 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences mypref = getSharedPreferences("Mypref",MODE_PRIVATE);
                SharedPreferences.Editor editor = mypref.edit();
                ArrayList<String> temp = new ArrayList<>(mypref.getStringSet("userList",null));
                if(temp == null){
                    ArrayList<String> tempnew = new ArrayList<>();
                    Set<String> set = new HashSet<String>(tempnew);
                    editor.putStringSet("userList",set);
                    editor.apply();
                    startActivity(new Intent(FilterActivity.this, PopUpFilter.class));
                }

                startActivity(new Intent(FilterActivity.this, PopUpFilter.class));
            }
        };
        MylistBtn.setOnClickListener(Listen2Btn1);
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