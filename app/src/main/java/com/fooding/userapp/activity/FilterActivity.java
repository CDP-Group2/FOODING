package com.fooding.userapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.fooding.userapp.APIService;
import com.fooding.userapp.FoodingApplication;
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
    @BindView(R.id.addButton) ImageButton addBtn;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.recentlyViewed) ImageButton recentlyViewedBtn;
    @BindView(R.id.filter) ImageButton filterBtn;
    @BindView(R.id.camera) ImageButton cameraBtn;
//    @BindView(R.id.MylistBtn) Button MylistBtn;

    @BindView(R.id.JsonTextview) TextView debuggingView; //debugging purpose

    public ArrayList<String> IngridientId; //id list of ingridient
    public ArrayList<String> IngridientName; //list of ingridient name on user filter list .. user preferences
    public ArrayList<String> resultList; //list of ingridient name on user filter list .. user preferences
    public ArrayAdapter<String> adapter;

    Filter filter = new Filter(); //user filter
    private Map<String, String> dbIngridient = new LinkedHashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        ButterKnife.bind(this);

        /*************************************************************************************************************/
        // font setting
        final FoodingApplication app = FoodingApplication.getInstance();
        SharedPreferences fontSP = app.getMyPref();

        final String pathT = fontSP.getString("titleFont", "none");
        Typeface font = Typeface.createFromAsset(getAssets(), pathT);
        title.setTypeface(font);
        debuggingView.setTypeface(font);

        final String pathK = fontSP.getString("koreanFont", "none");
        Typeface fontK = Typeface.createFromAsset(getAssets(), pathK);
        searchText.setTypeface(fontK);
        /*************************************************************************************************************/

        IngridientId = new ArrayList<String>(); //initialization of ingridient id list
        IngridientName = new ArrayList<String>(); //initialization of ingridient name list
        resultList =  new ArrayList<String>(); //result list to show in listview

        resultList.addAll(dbIngridient.values());
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_single_choice, resultList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);

                final FoodingApplication app = FoodingApplication.getInstance();
                SharedPreferences fontSP = app.getMyPref();

                final String pathT = fontSP.getString("listViewFont", "none");
                Typeface font = Typeface.createFromAsset(getAssets(), pathT);
                textView.setTypeface(font);

                return view;
            }
        };
        resultListView.setAdapter(adapter);

        searchText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                // 엔터키 눌렀을 시 감지
                if((keyEvent.getAction() == keyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchText.getWindowToken(), 0);   // hide keyboard

                    return true;
                }
                return false;
            }
        });

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
                                //Toast.makeText(getApplicationContext(),id,Toast.LENGTH_SHORT).show();
                                String name = response.body().get(i).getName().toString();
                                dbIngridient.put(name,id); //adding all to map
                            }

                            resultList.addAll(dbIngridient.keySet());

                            for (int counter = 0; counter < IngridientName.size(); counter++) {
                                String temp = IngridientName.get(counter);
                                //Toast.makeText(getApplicationContext(), temp, Toast.LENGTH_LONG).show();
                                if(temp.contains(text)){
//                                    searchText.setTextColor(getResources().getColor(R.color.Red));
                                    debuggingView.setVisibility(View.INVISIBLE);
                                }
                                else{
//                                    searchText.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    resultListView.setVisibility(View.INVISIBLE);
                                    debuggingView.setVisibility(View.VISIBLE);
                                }
                            }

                            /*adapter.notifyDataSetChanged();

                            resultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                                    final String chosenName = resultList.get(i);
                                    final String chosenID = dbIngridient.get(chosenName);

                                    Toast.makeText(getApplicationContext(),chosenName+"\n"+chosenID,Toast.LENGTH_SHORT).show();

                                    View.OnClickListener Listen2Btn = new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            filter.addItem2UserListName(chosenName); //add string to userList
                                            filter.addItem2UserListId(chosenID);
                                            SharedPreferences myPref = getSharedPreferences("Mypref", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = myPref.edit();
                                            ArrayList<String> temp1 = new ArrayList<String>(filter.getUserListName());
                                            ArrayList<String> temp2 = new ArrayList<String>(filter.getUserListId());
                                            Set<String> set1 = new HashSet<String>(temp1);
                                            Set<String> set2 = new HashSet<String>(temp2);
                                            editor.putStringSet("userList",set1);
                                            editor.putStringSet("userListkey",set2);
                                            editor.apply();
                                            //adapter.notifyDataSetChanged(); //prevent same data
                                            startActivity(new Intent(FilterActivity.this, PopUpFilter.class));
                                        }

                                    };
                                    addBtn.setOnClickListener(Listen2Btn);
                                    resultListView.clearChoices();
                                    adapter.notifyDataSetChanged();
                                }
                            });*/

                            if(response.body().size()!=0)
                                adapter.notifyDataSetChanged();
                        } else {
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

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count, checked;

                count = adapter.getCount();

                if(count > 0) {
                    checked = resultListView.getCheckedItemPosition();

                    if(checked > -1 && checked < count) {
                        final String Name = resultList.get(checked).toString();
                        final String ID = dbIngridient.get(Name);

                        filter.addItem2UserListName(Name);
                        filter.addItem2UserListId(ID);

                        SharedPreferences myPref = getSharedPreferences("Mypref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = myPref.edit();
                        ArrayList<String> temp1 = new ArrayList<String>(filter.getUserListName());
                        ArrayList<String> temp2 = new ArrayList<String>(filter.getUserListId());
                        Set<String> set1 = new HashSet<String>(temp1);
                        Set<String> set2 = new HashSet<String>(temp2);
                        editor.putStringSet("userList",set1);
                        editor.putStringSet("userListkey",set2);
                        editor.apply();

                        startActivity(new Intent(FilterActivity.this, PopUpFilter.class));
                        finish();
                    }
                }
            }
        });

        recentlyViewedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FilterActivity.this, recentlyViewedActivity.class));
                finish();
            }
        });

        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FilterActivity.this, PopUpFilter.class));
                finish();
            }
        });

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FilterActivity.this, CameraActivity.class));
                finish();
            }
        });


        /*View.OnClickListener Listen2Btn1 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences mypref = getSharedPreferences("Mypref",MODE_PRIVATE);
                SharedPreferences.Editor editor = mypref.edit();
                ArrayList<String> temp1 = new ArrayList<>(mypref.getStringSet("userList",null));
                ArrayList<String> temp2 = new ArrayList<>(mypref.getStringSet("userListkey",null));
                if(temp1 == null && temp2 == null){
                    ArrayList<String> tempReset = new ArrayList<>();
                    Set<String> set = new HashSet<String>(tempReset);
                    editor.putStringSet("userList",set);
                    editor.putStringSet("userListkey",set);
                    editor.apply();
                    startActivity(new Intent(FilterActivity.this, PopUpFilter.class));
                }

                startActivity(new Intent(FilterActivity.this, PopUpFilter.class));
            }
        };
        MylistBtn.setOnClickListener(Listen2Btn1);*/
    }

    /*@Override
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
    }*/
}