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
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fooding.userapp.FoodingApplication;
import com.fooding.userapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class recentlyViewedActivity extends AppCompatActivity {

    @BindView(R.id.title) TextView title;
    @BindView(R.id.filter) ImageButton filterBtn;
    @BindView(R.id.camera) ImageButton cameraBtn;
    @BindView(R.id.recipeList) ListView recipeList;
    @BindView(R.id.setting) ImageButton settingBtn;

    public ArrayList<String> recentlyViewed;
    public ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recently_viewed);
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
            final View root = findViewById(R.id.RecentlyViewedActivity).getRootView();
//            root.setBackgroundColor(Color.parseColor("#000000"));
            root.setBackgroundResource(R.drawable.dark_theme_background);

            // change text color
            title.setTextColor(Color.parseColor("#ffffff"));

            // change buttons
            filterBtn.setImageResource(R.mipmap.filter_white);
            cameraBtn.setImageResource(R.mipmap.camera_white);
            settingBtn.setImageResource(R.mipmap.settings_white);

            // change dividing lines
            View tmp = findViewById(R.id.title_bar);
            tmp.setBackgroundColor(Color.parseColor("#ffffff"));
            tmp = findViewById(R.id.menu_bar);
            tmp.setBackgroundColor(Color.parseColor("#ffffff"));

            // listview divider/separator
            /*recipeList.setDivider(new ColorDrawable(0xF0ECECEC));
            recipeList.setDividerHeight(1);*/
        }
        /*************************************************************************************************************/

//        final FoodingApplication app = FoodingApplication.getInstance();
        recentlyViewed = new ArrayList<String>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, recentlyViewed) {
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
                }

                return view;
            }
        };

        recipeList.setAdapter(adapter);
        recipeList.setOnItemClickListener(mItemClickListener);

//        final HashMap<String, String> recentMap = app.getRecentSearch();
        Map<String, String> recentMap = new HashMap<String, String>();

        SharedPreferences myPref = getSharedPreferences("recentlyViewed", MODE_PRIVATE);
        ArrayList<String> recipe = new ArrayList<String>();
        if(myPref.getStringSet("recipeList", null) != null)
            recipe.addAll(myPref.getStringSet("recipeList", null));

        Log.i("recipe size", Integer.toString(recipe.size()));

        if(recipe.size() > 0) {
            for(int i = 0; i < recipe.size(); i++) {
                String[] str = recipe.get(i).split("@");
                recentMap.put(str[0], str[1]);
                Log.i("id / name", str[0] + " / " + str[1]);
                recentlyViewed.add(str[1]);
            }

            adapter.notifyDataSetChanged();
            app.setRecentSearch(recentMap);
        }

        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(recentlyViewedActivity.this, PopUpFilter.class));
                finish();
            }
        });

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(recentlyViewedActivity.this, CameraActivity.class));
                finish();
            }
        });

        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(recentlyViewedActivity.this, SettingsActivity.class));
                finish();
            }
        });
    }

    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l_position) {
//            String recipeName = (String)adapterView.getAdapter().getItem(position);
            String recipeId = "";

            SharedPreferences myPref = getSharedPreferences("recentlyViewed", MODE_PRIVATE);
            ArrayList<String> recipe = new ArrayList<String>();
            if(myPref.getStringSet("recipeList", null) != null)
                recipe.addAll(myPref.getStringSet("recipeList", null));

            if(recipe.size() > 0) {
                String[] str = recipe.get(position).split("@");
                Log.i("clicked recipe ID", str[0]);
                Log.i("clicked recipe name", str[1]);
                recipeId = str[0];
            }

            if(recipeId.length() > 0) {
                Log.i("ViewRecipeId로 전달하는 ID", recipeId);
                Intent intent = new Intent(recentlyViewedActivity.this, ViewRecipeActivity.class);
                intent.putExtra("code", recipeId);
                startActivity(intent);
                finish();
            }
        }
    };
}
