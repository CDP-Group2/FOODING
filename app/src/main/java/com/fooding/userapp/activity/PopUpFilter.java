package com.fooding.userapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.fooding.userapp.R;
import com.fooding.userapp.data.Filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PopUpFilter extends AppCompatActivity {
    @BindView(R.id.userListview) ListView userList;
    @BindView(R.id.removeBtn) Button removeBtn;
    @BindView(R.id.Searchagain) Button Searchagain;

    public ArrayAdapter adapter; //adapter intialize
    public Set<String> set; //set for preference
    Map<String, String> Filtermap = new HashMap<String, String>();

    Filter filter = new Filter(); //calling filter class

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popupfilter);
        ButterKnife.bind(this);

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, filter.getUserListName()) ;
        userList.setAdapter(adapter);

        for(int i = 0; i< filter.getUserListId().size();i++){
            Filtermap.put(filter.getUserListName().get(i),filter.getUserListId().get(i));
        }
        //Toast.makeText(getApplicationContext(),"You selected "+Filtermap.toString(),Toast.LENGTH_SHORT).show();


        ///////////////////////////Removing item/////////////////////////////////////
        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                final String tempStr = userList.getItemAtPosition(i).toString(); //selected String
                //Toast.makeText(getApplicationContext(),"You selected "+tempStr,Toast.LENGTH_SHORT).show();
                View.OnClickListener Listen2Btn = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(tempStr!=null){
                            String Idremove = Filtermap.get(tempStr);
                            filter.removeItemOnUserListId(Idremove);
                            //Toast.makeText(getApplicationContext(),"You selected "+filter.getUserListId().toString(),Toast.LENGTH_SHORT).show();
                            filter.removeItemOnUserListName(tempStr);
                            Filtermap.remove(tempStr); //remove id and name
                            Toast.makeText(getApplicationContext(),"You selected "+filter.getUserListName().toString()+"\n"+filter.getUserListId().toString(),Toast.LENGTH_SHORT).show();
                            Set<String> set1 = new HashSet<String>(filter.getUserListName());
                            Set<String> set2 = new HashSet<String>(filter.getUserListId());
                            SharedPreferences myPref = getSharedPreferences("Mypref", MODE_PRIVATE);
                            SharedPreferences.Editor editor = myPref.edit();
                            editor.putStringSet("userList",set1);
                            editor.putStringSet("userListkey",set2);
                            editor.apply();
                            adapter.notifyDataSetChanged();
                        }
                    }
                };
                removeBtn.setOnClickListener(Listen2Btn);
                userList.clearChoices();
            }
        });
        ///////////////////////////////////////////////////////////////////
        View.OnClickListener Listen2Btn1 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FilterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                // clear your SharedPreferences
                if(filter.getUserListName().isEmpty()){
                    //Set<String> set = new HashSet<String>(filter.getUserListName());
                    SharedPreferences myPref = getSharedPreferences("Mypref", MODE_PRIVATE);
                    SharedPreferences.Editor editor = myPref.edit();
                    ArrayList<String> tempArray = new ArrayList<>();
                    Set<String> setTemp = new HashSet<String>(tempArray);
                    editor.putStringSet("userList",setTemp);
                    editor.putStringSet("userListkey",setTemp);
                    editor.apply();
                }
                startActivity(intent);
            }
        };
        Searchagain.setOnClickListener(Listen2Btn1);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed(); // this can go before or after your stuff below
        // do your stuff when the back button is pressed
        Intent intent = new Intent(getApplicationContext(), FilterActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // super.onBackPressed(); calls finish(); for you

        // clear your SharedPreferences
        if(filter.getUserListName().isEmpty()){
            SharedPreferences myPref = getSharedPreferences("Mypref", MODE_PRIVATE);
            SharedPreferences.Editor editor = myPref.edit();
            ArrayList<String> tempArray = new ArrayList<>();
            Set<String> setTemp = new HashSet<String>(tempArray);
            editor.putStringSet("userList",setTemp);
            editor.putStringSet("userListkey",setTemp);
            editor.apply();
        }

        startActivity(intent);
    }
}
