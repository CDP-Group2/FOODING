package com.fooding.userapp;

import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Typeface;

import com.fooding.userapp.data.Food;
import com.fooding.userapp.data.User;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class FoodingApplication extends Application {
    private static FoodingApplication instance;
    private User user;
    private Food currentFood;
    private HashMap<String, String> recentSearch = new HashMap<String, String>();

    private SharedPreferences myPref;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static FoodingApplication getInstance() {
        return instance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Food getCurrentFood() {
        return currentFood;
    }

    public void setCurrentFood(Food currentFood) {
        this.currentFood = currentFood;
    }

    /*public ArrayList<String> getRecentSearch() {
        ArrayList<String> foodName = new ArrayList<String>();
        Set<String> keySet = recentSearch.keySet();
        Iterator<String> iterator = keySet.iterator();

        while(iterator.hasNext()) {
            String key = iterator.next();
            foodName.add(recentSearch.get(key));
        }

        return foodName;
    }*/

    public void setMyPref(SharedPreferences myPref) {
        this.myPref = myPref;
    }

    public SharedPreferences getMyPref() {
        return myPref;
    }

    public HashMap<String, String> getRecentSearch() {
        return recentSearch;
    }

    public void addRecentFood(String key, String name) {
        if(recentSearch.containsKey(key))
            return;

        if(recentSearch.size() >= 10) {
            Set<String> keySet = recentSearch.keySet();
            Iterator<String> iterator = keySet.iterator();

            String firstKey = iterator.next();
            recentSearch.remove(firstKey);
        }

        recentSearch.put(key, name);
    }
}
