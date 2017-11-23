package com.fooding.userapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fooding.userapp.FoodingApplication;
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
    @BindView(R.id.title) TextView title;
    @BindView(R.id.userListview) ListView userList;
    @BindView(R.id.removeBtn) ImageButton removeBtn;
    @BindView(R.id.Searchagain) ImageButton Searchagain;
    @BindView(R.id.setting) ImageButton settingBtn;
    @BindView(R.id.camera) ImageButton cameraBtn;
    @BindView(R.id.recentlyViewed) ImageButton recentlyViewedBtn;

    public ArrayAdapter adapter; //adapter intialize
    public Set<String> set; //set for preference
    Map<String, String> Filtermap = new HashMap<String, String>();

    Filter filter = new Filter(); //calling filter class

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popupfilter);
        ButterKnife.bind(this);

        /*************************************************************************************************************/
        // font setting
        final FoodingApplication app = FoodingApplication.getInstance();
        SharedPreferences fontSP = app.getMyPref();

        final String pathT = fontSP.getString("titleFont", "none");
        Typeface font = Typeface.createFromAsset(getAssets(), pathT);
        title.setTypeface(font);
        /*************************************************************************************************************/

        /*************************************************************************************************************/
        // theme setting
        if(fontSP.getBoolean("theme", false)) { // dark theme
            // change background
            final View root = findViewById(R.id.PopUpFilterActivity).getRootView();
//            root.setBackgroundColor(Color.parseColor("#000000"));
            root.setBackgroundResource(R.drawable.dark_theme_background);

            // change text color
            title.setTextColor(Color.parseColor("#ffffff"));

            // change buttons
            cameraBtn.setImageResource(R.mipmap.camera_white);
            settingBtn.setImageResource(R.mipmap.settings_white);
            recentlyViewedBtn.setImageResource(R.mipmap.list_white);
            Searchagain.setImageResource(R.mipmap.search_white);
            removeBtn.setImageResource(R.mipmap.dustbin_white);

            // change dividing lines
            View tmp = findViewById(R.id.title_bar);
            tmp.setBackgroundColor(Color.parseColor("#ffffff"));
            tmp = findViewById(R.id.menu_bar);
            tmp.setBackgroundColor(Color.parseColor("#ffffff"));

            // listview divider/separator
            /*userList.setDivider(new ColorDrawable(0xF0ECECEC));
            userList.setDividerHeight(1);*/
        }
        /*************************************************************************************************************/

        SharedPreferences myPref = getSharedPreferences("Mypref", MODE_PRIVATE);
        ArrayList<String> idSet;
        ArrayList<String> nameSet;
        if(myPref.getStringSet("userListkey",null) != null) {
            idSet = new ArrayList<>(myPref.getStringSet("userListkey", null));
            filter.setUserListId(idSet);
        }
        if(myPref.getStringSet("userList",null) != null) {
            nameSet = new ArrayList<>(myPref.getStringSet("userList", null));
            filter.setUserListName(nameSet);
        }

//        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, filter.getUserListName()) ;
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, filter.getUserListName()) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);

                final FoodingApplication app = FoodingApplication.getInstance();
                SharedPreferences myPref = app.getMyPref();

                final String pathT = myPref.getString("listViewFont", "fonts/NanumSquareRoundOTFR.otf");
                Typeface font = Typeface.createFromAsset(getAssets(), pathT);
                textView.setTypeface(font);

                final Integer fontSize = myPref.getInt("fontSize", 16);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, fontSize);

                if(myPref.getBoolean("theme", false)) { // dark theme
                    textView.setTextColor(Color.parseColor("#ffffff"));

                    // 선택된 항목 텍스트 색 변화 (바탕이 검은색이라 체크 항목이 안 보임)
                    SparseBooleanArray checked = userList.getCheckedItemPositions();
                    for(int i = 0; i < checked.size(); i++) {
                        int key = checked.keyAt(i);
                        boolean value = checked.get(key);
                        if(value && position == key)
                            textView.setTextColor(getResources().getColor(R.color.yellowAccent));
                    }
                }

                return view;
            }
        };
        userList.setAdapter(adapter);

        for(int i = 0; i< filter.getUserListId().size();i++){
            Filtermap.put(filter.getUserListName().get(i),filter.getUserListId().get(i));
        }
        //Toast.makeText(getApplicationContext(),"You selected "+Filtermap.toString(),Toast.LENGTH_SHORT).show();


        /*///////////////////////////Removing item/////////////////////////////////////
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
        ///////////////////////////////////////////////////////////////////*/

        userList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int selectedCount = 0;
                SparseBooleanArray sparse = userList.getCheckedItemPositions();
                final int length = filter.getUserListName().size();

                for(int j = 0; j < length; j++) {
                    if(sparse.valueAt(j)) {
                        Log.i("Selected Item", filter.getUserListName().get(i));

                        selectedCount++;
                    }
                }

                Log.i("# of Selected Items", Integer.toString(selectedCount));

                adapter.notifyDataSetChanged();
            }
        });

        removeBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                SparseBooleanArray checkedItems = userList.getCheckedItemPositions();
                int count = adapter.getCount();

                int selectedCount = 0;

                for(int i = count - 1; i >= 0; i--) {
                    if(checkedItems.get(i)) {
                        String removeName = userList.getItemAtPosition(i).toString();
                        String removeID = Filtermap.get(removeName);
                        filter.removeItemOnUserListId(removeID);
                        filter.removeItemOnUserListName(removeName);
                        Filtermap.remove(removeName);
                        /*Set<String> set1 = new HashSet<String>(filter.getUserListName());
                        Set<String> set2 = new HashSet<String>(filter.getUserListId());
                        SharedPreferences myPref = getSharedPreferences("Mypref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = myPref.edit();
                        editor.putStringSet("userList", set1);
                        editor.putStringSet("userListkey", set2);
                        editor.apply();*/

                        selectedCount++;
                    }
                }

                if(selectedCount == 0)
                    Toast.makeText(PopUpFilter.this, "0 Items Selected", Toast.LENGTH_SHORT).show();

                Set<String> set1 = new HashSet<String>(filter.getUserListName());
                Set<String> set2 = new HashSet<String>(filter.getUserListId());
                SharedPreferences myPref = getSharedPreferences("Mypref", MODE_PRIVATE);
                SharedPreferences.Editor editor = myPref.edit();
                editor.putStringSet("userList", set1);
                editor.putStringSet("userListkey", set2);
                editor.apply();

                userList.clearChoices();
                adapter.notifyDataSetChanged();
            }
        });

        cameraBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CameraActivity.class));
                finish();
            }
        });

        recentlyViewedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PopUpFilter.this, recentlyViewedActivity.class));
                finish();
            }
        });

        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PopUpFilter.this, SettingsActivity.class));
                finish();
            }
        });

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
                finish();
            }
        };
        Searchagain.setOnClickListener(Listen2Btn1);
    }

    /*@Override
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
    }*/
}
