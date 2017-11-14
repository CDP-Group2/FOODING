package com.fooding.userapp.data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class Filter {
    //////for map type//////
    private Map<String, String> Ingredient = new LinkedHashMap<String, String>();

    public Map<String, String> getIngredient() {
        return Ingredient;
    }

    public void setIngredient(Map<String, String> nIngredients) {
        this.Ingredient.putAll(nIngredients);
    }

    public void addIngredient(String key, String name){
        this.Ingredient.put(key,name);
    }
    /////////////////////////


    /////for arraylist//////////////
    private static ArrayList<String> userList = new ArrayList<String>();

    public void setUserList(ArrayList<String> userList) {
        Filter.userList = userList;
    }

    public ArrayList<String> getUserList() {
        return userList;
    }

    public void addItem2UserList(String temp){
        userList.add(temp);
    }

    public void removeItemOnUserList(String temp){
        userList.remove(temp);
    }
    //////////////////////////////

    /////for getting preference/////////
    //function to add and remove from pref
    /////////////////////////////////////

    /////for filtering preference with normal data/////////
    ///////////////////////////////////////////////////////
}
