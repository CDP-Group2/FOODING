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
import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PopUpFilter extends AppCompatActivity {
    @BindView(R.id.userListview) ListView userList;
    @BindView(R.id.removeBtn) Button removeBtn;
    @BindView(R.id.Searchagain) Button Searchagain;

    public ArrayAdapter adapter; //adapter intialize
    public Set<String> set; //set for preference
    public ArrayList<String> myFilterList; //user filter list

    Filter filter = new Filter(); //calling filter class

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popupfilter);
        ButterKnife.bind(this);

        ///////////////////checking if there is a user list/////////////////////////
        SharedPreferences myPref = getSharedPreferences("Mypref", MODE_PRIVATE);
        set = myPref.getStringSet("userList",null);

        if(set == null){
            myFilterList = new ArrayList<String>();

        }
        else{
            myFilterList = new ArrayList<String>(set);
        }

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, myFilterList) ;
        userList.setAdapter(adapter);
        /////////////////////////////////////////////////////////////////////////////
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
                            filter.removeItemOnUserList(tempStr);
                            adapter.notifyDataSetChanged();
                            filter.setUserList(myFilterList);
                            //Toast.makeText(getApplicationContext(),"You selected "+filter.getUserList().toString(),Toast.LENGTH_SHORT).show();
                            Set<String> set = new HashSet<String>(filter.getUserList());
                            SharedPreferences myPref = getSharedPreferences("Mypref", MODE_PRIVATE);
                            SharedPreferences.Editor editor = myPref.edit();
                            editor.putStringSet("userList",set);
                            editor.apply();
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
                if(myFilterList.isEmpty()){
                    Set<String> set = new HashSet<String>(filter.getUserList());
                    SharedPreferences myPref = getSharedPreferences("Mypref", MODE_PRIVATE);
                    SharedPreferences.Editor editor = myPref.edit();
                    ArrayList<String> tempnew = new ArrayList<>();
                    Set<String> set1 = new HashSet<String>(tempnew);
                    editor.putStringSet("userList",set1);
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
        if(myFilterList.isEmpty()){
            Set<String> set = new HashSet<String>(filter.getUserList());
            SharedPreferences myPref = getSharedPreferences("Mypref", MODE_PRIVATE);
            SharedPreferences.Editor editor = myPref.edit();
            ArrayList<String> tempnew = new ArrayList<>();
            Set<String> set1 = new HashSet<String>(tempnew);
            editor.putStringSet("userList",set1);
            editor.apply();
        }

        startActivity(intent);
    }
}
