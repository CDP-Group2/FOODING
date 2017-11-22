package com.fooding.userapp;

import com.fooding.userapp.data.model.Ingredient;
import com.fooding.userapp.data.model.Recipe;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {
    public static final String API_URL = "http://poerty.co.kr/fooding/";

    // 음식 key 가지고 해당 음식 재료 정보 가져오기
    @GET("getIngredient.php")
    Call<List<Ingredient>>getIngredient(@Query("key") String key);

    // ingredient key로 ingredient 정보 가져오기
    @GET("Ingredientsid.php")
    Call<Ingredient>getIngredientInfo(@Query("key") String key);

    // 레시피 이름 받아오기
    @GET("getRecipeName.php")
    Call<Recipe>getRecipeInfo(@Query("recipeID") String recipeID);

    // 사업자번호로 해당 사업자 레시피 리스트 가져오기 (사업자용 어플리케이션)
    // 레시피번호를 전송하면, 해당 레시피번호의 사업자번호와 같은 사업자번호를 갖는 모든 레시피 리스트 가져오기 (사용자용 어플리케이션)
    @GET("getRecipe.php")
    Call<List<Recipe>>getRecipe(@Query("recipeID") String recipeID);

    //auto complete search
    @GET("searchIngredient.php")
    Call<List<Ingredient>>searchIngredient(@Query("searchText") String searchText);

    //필터와 레시피아이디로 해당 사업자 음식중 먹을 수 있는것 가져오기
    @GET("getRecipeEatable.php")
    Call<List<Recipe>>getRecipeEatable(@Query("filterList[]") ArrayList<String> filterList ,@Query("recipeID") String recipeID);
}
