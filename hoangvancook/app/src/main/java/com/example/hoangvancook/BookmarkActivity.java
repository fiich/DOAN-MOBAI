package com.example.hoangvancook;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hoangvancook.Adapters.BookmarkAdapter;
import com.example.hoangvancook.Models.RecipeBookmark;
import com.example.hoangvancook.Models.RecipeDatabase;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class BookmarkActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    BookmarkAdapter bookmarkAdapter;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        recyclerView = findViewById(R.id.recycler_bookmark);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        loadBookmarks();

        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.action_bookmark);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.action_home) {
                    Intent homeintent = new Intent(BookmarkActivity.this, HomeActivity.class);
                    startActivity(homeintent);
                    return true;
                } else if (itemId == R.id.action_search) {
                    Intent searchintent = new Intent(BookmarkActivity.this, SearchActivity.class);
                    startActivity(searchintent);
                    return true;
                }
                return false;
            }
        });
    }
    private void loadBookmarks() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<RecipeBookmark> bookmarks = RecipeDatabase.getInstance(BookmarkActivity.this).recipeBookmarkDao().getAllBookmarks();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bookmarkAdapter = new BookmarkAdapter(BookmarkActivity.this, bookmarks);
                        recyclerView.setAdapter(bookmarkAdapter);
                    }
                });
            }
        }).start();
    }
}
