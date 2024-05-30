package com.example.hoangvancook.Listeners;

import com.example.hoangvancook.Models.RandomRecipeApiResponse;
import com.example.hoangvancook.Models.SearchResponse;

public interface SearchResponseListener {
    void didFetch(SearchResponse response, String message);
    void didError(String message);

}