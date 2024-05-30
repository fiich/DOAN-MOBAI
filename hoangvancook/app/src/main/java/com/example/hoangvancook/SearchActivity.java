
package com.example.hoangvancook;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hoangvancook.Adapters.SearchAdapter;
import com.example.hoangvancook.Listeners.SearchResponseListener;
import com.example.hoangvancook.Models.SearchResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SearchActivity extends AppCompatActivity {
    private RecyclerView recyclerView_search_item;
    private Dialog dialog;
    public SearchView search ;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Addcontrol();

        Addevent();


        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.action_search);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.action_home) {
                Intent homeintent = new Intent(SearchActivity.this, HomeActivity.class);
                startActivity(homeintent);
                return true;

            } else if (itemId == R.id.action_bookmark) {
                Intent bookmarkintent = new Intent(SearchActivity.this, BookmarkActivity.class);
                startActivity(bookmarkintent);
                return true;
            }else if (itemId == R.id.action_profile) {
                Intent bookmarkintent = new Intent(SearchActivity.this, Profile.class);
                startActivity(bookmarkintent);
                return true;
            }
            return false;
        });
    }

    private void Addevent() {

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                tim_kiem_onSubmit(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                tim_kiem_onInput(newText);
                return true;
            }
        });
    }
    private void tim_kiem_onInput(String query) {
        RequestManager requestManager_search_item = new RequestManager(this);
        requestManager_search_item.searchFood(searchResponseListener, query, "");
    }
    private void tim_kiem_onSubmit(String query) {
        RequestManager requestManager_search_item = new RequestManager(this);
        requestManager_search_item.searchFood(searchResponseListener, query, "submit");
    }
    private void tim_kiem(String query) {
//
        RequestManager requestManager_search_item = new RequestManager(this);
        requestManager_search_item.searchFood(searchResponseListener,query," ");
    }

    private final SearchResponseListener searchResponseListener=new SearchResponseListener() {
        @Override
        public void didFetch(SearchResponse response, String message) {
            SearchAdapter searchAdapter = new SearchAdapter(SearchActivity.this, result -> {
                Intent intent = new Intent(SearchActivity.this, RecipeDetailsActivity.class);
                intent.putExtra("id", String.valueOf(result.getId()));
                startActivity(intent);

            }, response.getResults());
            recyclerView_search_item.setLayoutManager(new GridLayoutManager(SearchActivity.this, 2));
            recyclerView_search_item.setAdapter(searchAdapter);
        }

        @Override
        public void didError(String message) {

        }
    };

    private void Addcontrol() {
        search = findViewById(R.id.searchView);
        recyclerView_search_item = findViewById(R.id.recycler_search);
    }
}