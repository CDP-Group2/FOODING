package com.fooding.userapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

//        final FoodingApplication app = FoodingApplication.getInstance();
        recentlyViewed = new ArrayList<String>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, recentlyViewed) {
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

        recipeList.setAdapter(adapter);
        recipeList.setOnItemClickListener(mItemClickListener);

        final HashMap<String, String> recentMap = app.getRecentSearch();
        final Set<String> keySet = recentMap.keySet();
        final Iterator<String> iterator = keySet.iterator();

        while(iterator.hasNext()) {
            String key = iterator.next();
            recentlyViewed.add(recentMap.get(key));
        }

        adapter.notifyDataSetChanged();

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
            String recipeName = (String)adapterView.getAdapter().getItem(position);
            String recipeId = "";

//            Toast.makeText(getApplicationContext(), recipeName, Toast.LENGTH_SHORT).show();

            final FoodingApplication app = FoodingApplication.getInstance();
            final HashMap<String, String> recentMap = app.getRecentSearch();
            final Set<String> keySet = recentMap.keySet();
            final Iterator<String> iterator = keySet.iterator();

            while(iterator.hasNext()) {
                String key = iterator.next();
                if(recentMap.get(key) == recipeName) {
                    recipeId = key;
                    break;
                }
            }

            if(recipeId.length() > 0) {
//                Toast.makeText(getApplicationContext(), recipeId, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(recentlyViewedActivity.this, ViewRecipeActivity.class);
                intent.putExtra("code", recipeId);
                startActivity(intent);
                finish();
            }
        }
    };
}
