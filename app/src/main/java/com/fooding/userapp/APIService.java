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
    Call<List<Ingredient>>getIngredient(@Query("recipeID") String key);

    // 음식 key로 해당 음식 재료의 이름 가져오기
    @GET("getIngredientInfo.php")
    Call<Ingredient>getIngredientInfo(@Query("recipeID") String key);

    // 사업자번호로 해당 사업자 레시피 리스트 가져오기
    @GET("getRecipe.php")
    Call<List<Recipe>>getRecipe(@Query("recipeID") String companyID);

    //auto complete search
    @GET("searchIngredient.php")
    Call<List<Ingredient>>searchIngredient(@Query("searchText") String searchText);
}
