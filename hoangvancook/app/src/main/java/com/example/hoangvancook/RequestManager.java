package com.example.hoangvancook;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.hoangvancook.Listeners.InstructionsListener;
import com.example.hoangvancook.Listeners.RandomRecipeResponseListener;

import com.example.hoangvancook.Listeners.RecipeDetailsListener;
import com.example.hoangvancook.Listeners.SearchResponseListener;
import com.example.hoangvancook.Listeners.SimilarRecipesListener;
import com.example.hoangvancook.Models.InstructionsResponse;
import com.example.hoangvancook.Models.RandomRecipeApiResponse;
import com.example.hoangvancook.Models.RecipeDetailsResponse;
import com.example.hoangvancook.Models.SearchResponse;
import com.example.hoangvancook.Models.SimilarRecipeResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

import retrofit2.http.Path;
import retrofit2.http.Query;

public class RequestManager {
    Context context;
    Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.spoonacular.com").addConverterFactory(GsonConverterFactory.create()).build();

    public RequestManager(Context context) {

        this.context = context;
    }

    public void getRandomRecipes(RandomRecipeResponseListener listener, List<String> tags){
        CallRandomRecipes callRandomRecipes = retrofit.create(CallRandomRecipes.class);
        Call<RandomRecipeApiResponse> call = callRandomRecipes.callRandomRecipe(context.getString(R.string.api_key), "10", tags);
        call.enqueue(new Callback<RandomRecipeApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<RandomRecipeApiResponse> call, @NonNull Response<RandomRecipeApiResponse> response) {
                if(!response.isSuccessful()){
                    listener.didError(response.message());
                    return;
                }
                listener.didFetch(response.body(),response.message());
            }

            @Override
            public void onFailure(@NonNull Call<RandomRecipeApiResponse> call, @NonNull Throwable throwable) {
                listener.didError(throwable.getMessage());
            }
        });
    }

    public void getRecipeDetails(RecipeDetailsListener listener, int id){
        CallRecipeDetails callRecipeDetails = retrofit.create(CallRecipeDetails.class);
        Call<RecipeDetailsResponse> call = callRecipeDetails.callRecipeDetails(id, context.getString(R.string.api_key));
        call.enqueue(new Callback<RecipeDetailsResponse>() {
            @Override
            public void onResponse(@NonNull Call<RecipeDetailsResponse> call, @NonNull Response<RecipeDetailsResponse> response) {
                if(!response.isSuccessful()){
                    listener.didError(response.message());
                    return;
                }
                listener.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(@NonNull Call<RecipeDetailsResponse> call, @NonNull Throwable throwable) {
                listener.didError(throwable.getMessage());
            }
        });
    }
    public void getInstructions(InstructionsListener listener, int id){
        CallInstructions callInstructions = retrofit.create(CallInstructions.class);
        Call<List<InstructionsResponse>> call = callInstructions.callInstructions(id,context.getString(R.string.api_key));
        call.enqueue(new Callback<List<InstructionsResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<InstructionsResponse>> call, @NonNull Response<List<InstructionsResponse>> response) {
                if(!response.isSuccessful()){
                    listener.didError(response.message());
                    return;
                }
                listener.didFetch(response.body(), response.message());
            }
            @Override
            public void onFailure(@NonNull Call<List<InstructionsResponse>> call, @NonNull Throwable throwable) {
                listener.didError(throwable.getMessage());
            }
        });

    }
    public void searchFood (SearchResponseListener listener, String query) {
        Search searchFood = retrofit.create(Search.class);
        Call<SearchResponse> call = searchFood.SearchResponse("f6cc4597a30f46de9d5251182f968f32",query);
        call.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(@NonNull Call<SearchResponse> call, @NonNull Response<SearchResponse> response) {
                if(response.isSuccessful()){
                    listener.didFetch(response.body(),response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<SearchResponse> call, @NonNull Throwable throwable) {
                listener.didError(throwable.getMessage());
            }
        });
    }
    public void getSimilarRecipes(SimilarRecipesListener listener, int id){
        CallSimilarRecipes callSimilarRecipes = retrofit.create(CallSimilarRecipes.class);
        Call<List<SimilarRecipeResponse>> call =  callSimilarRecipes.callSimilarRecipe(id,"5",context.getString(R.string.api_key));
        call.enqueue(new Callback<List<SimilarRecipeResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<SimilarRecipeResponse>> call, @NonNull Response<List<SimilarRecipeResponse>> response) {
                if(!response.isSuccessful()){
                    listener.didError(response.message());
                    return;
                }
                listener.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(@NonNull Call<List<SimilarRecipeResponse>> call, @NonNull Throwable throwable) {
                listener.didError(throwable.getMessage());
            }
        });
    }
    private interface CallRandomRecipes{
        @GET("recipes/random")
        Call<RandomRecipeApiResponse> callRandomRecipe(
            @Query("apiKey") String apiKey,
            @Query("number") String number,
            @Query("tags") List<String> tags
        );
    }
    private interface CallRecipeDetails{
        @GET("recipes/{id}/information")
        Call<RecipeDetailsResponse> callRecipeDetails(
                @Path("id") int id,
                @Query("apiKey") String apiKey
        );
    }
    private interface CallInstructions{
        @GET("recipes/{id}/analyzedInstructions")
        Call<List<InstructionsResponse>> callInstructions(
            @Path("id") int id,
            @Query("apiKey") String apiKey
        );
    }
    private interface Search{
        @GET("recipes/complexSearch")
        Call<SearchResponse> SearchResponse(
                @Query("apiKey") String apiKey,
                @Query("query") String query

        );
    }
    private interface CallSimilarRecipes{
        @GET("recipes/{id}/similar")
        Call<List<SimilarRecipeResponse>> callSimilarRecipe(
                @Path("id") int id,
                @Query("number") String number,
                @Query("apiKey") String apiKey
        );
    }
}
