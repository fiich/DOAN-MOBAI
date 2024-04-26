package com.example.hoangvancook;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hoangvancook.Adapters.SearchAdapter;
import com.example.hoangvancook.Listeners.Clickitem;
import com.example.hoangvancook.Listeners.SearchResponseListener;
import com.example.hoangvancook.Models.Result;
import com.example.hoangvancook.Models.SearchResponse;
import com.example.hoangvancook.R;

public class SearchAcivity extends AppCompatActivity {
private RequestManager requestManager_search_item;
private RecyclerView recyclerView_search_item;
private SearchAdapter searchAdapter;

    public SearchView search ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Addcontrol();

        Addevent();

    }

    private void Addevent() {

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                tim_kiem(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void tim_kiem(String query) {
//
        requestManager_search_item = new RequestManager(this);
        requestManager_search_item.searchFood(searchResponseListener,query);


    }

    private final SearchResponseListener searchResponseListener=new SearchResponseListener() {
        @Override
        public void didFetch(SearchResponse response, String message) {
            searchAdapter = new SearchAdapter(SearchAcivity.this, new Clickitem<Result>() {
                @Override
                public void onclick(Result result) {
                    Intent intent= new Intent(SearchAcivity.this, RecipeDetailsActivity.class);
                    intent.putExtra("id",result.getId());
                    startActivity(intent);

                }
            }, response.getResults());
            recyclerView_search_item.setLayoutManager(new GridLayoutManager(SearchAcivity.this, 1));
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
