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


    /////for arraylist of id and name//////////////
    private static ArrayList<String> userListName = new ArrayList<String>();
    private static ArrayList<String> userListId = new ArrayList<String>();
    private static ArrayList<String> userListEnName = new ArrayList<String>();

    /////ingredient array list name functions//////////////////
    public void setUserListName(ArrayList<String> userListName) {
        Filter.userListName = userListName;
    }

    public ArrayList<String> getUserListName() {
        return userListName;
    }

    public void addItem2UserListName(String temp){
        if(userListName.contains(temp)!=true){
            userListName.add(temp);
        }
    }
    public void removeItemOnUserListName(String temp){
        userListName.remove(temp);
    }
    public void removeItemOnUserListNamebyIndex(int i) {userListName.remove(i);}
    /////////////////////////////////////////
    /////ingredient array list eng name functions//////////////////
    public void setUserListEnName(ArrayList<String> userListEnName) {
        Filter.userListEnName = userListEnName;
    }

    public ArrayList<String> getUserListEnName() {
        return userListEnName;
    }

    public void addItem2UserListEnName(String temp){
        if(userListEnName.contains(temp)!=true){
            userListEnName.add(temp);
        }
    }
    public void removeItemOnUserListEnName(String temp){
        userListEnName.remove(temp);
    }
    public void removeItemOnUserListEnNamebyIndex(int i) {userListEnName.remove(i);}


    ///////ingredient array list id functions//////////////////////
    public void setUserListId(ArrayList<String> userListId) {
        Filter.userListId = userListId;
    }

    public ArrayList<String> getUserListId() {
        return userListId;
    }

    public void addItem2UserListId(String temp){
        if(userListId.contains(temp)!=true){
            userListId.add(temp);
        }
    }

    public void removeItemOnUserListId(String temp){
        userListId.remove(temp);
    }
    /////////////////////////////////////////////////////////////
}
