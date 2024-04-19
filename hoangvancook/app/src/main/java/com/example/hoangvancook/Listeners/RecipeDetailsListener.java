package com.example.hoangvancook.Listeners;

import com.example.hoangvancook.Models.RecipeDetailsResponse;

public interface RecipeDetailsListener {
    void didFetch(RecipeDetailsResponse response, String message);
    void didError(String message);
}
