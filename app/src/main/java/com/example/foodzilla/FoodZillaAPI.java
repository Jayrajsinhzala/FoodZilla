package com.example.foodzilla;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface FoodZillaAPI {
    @GET("/recipes")
    Call<List<Recipe>> getAllRecipes(@QueryMap Map<String, Object> options);

    @GET("/recipe/{id}")
    Call<Recipe> getRecipe(@Path("id") int id);

    @GET("/recipe/{id}/ratings")
    Call<List<Rating>> getRatings(@Path("id") int id);

    @POST("/recipe/{id}/rate")
    Call<Rating> setRatings(@Path("id") int id, @QueryMap Map<String, String> options);
}
