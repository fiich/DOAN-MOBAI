package com.example.hoangvancook;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hoangvancook.Adapters.BookmarkAdapter;
import com.example.hoangvancook.Listeners.RecipeClickListener;
import com.example.hoangvancook.Models.RecipeBookmark;
import com.example.hoangvancook.Models.RecipeDatabase;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Collections;
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
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        loadBookmarks();

        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.action_bookmark);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
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
        });

    }
    private void loadBookmarks() {
        new Thread(() -> {
            List<RecipeBookmark> bookmarks = RecipeDatabase.getInstance(BookmarkActivity.this).recipeBookmarkDao().getAllBookmarks();
            runOnUiThread(() -> {
                if (bookmarkAdapter == null) {
                    bookmarkAdapter = new BookmarkAdapter(BookmarkActivity.this, bookmarks, recipeClickListener);
                    Collections.reverse(bookmarks);
                    recyclerView.setAdapter(bookmarkAdapter);
                } else {
                    bookmarkAdapter.updateBookmarks(bookmarks);
                }
            });
        }).start();
    }

    private final RecipeClickListener recipeClickListener = id -> {
        Intent intent = new Intent(BookmarkActivity.this, RecipeDetailsActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    };
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            RecipeBookmark bookmark = bookmarkAdapter.getBookmarkAtPosition(position);
            new Thread(() -> RecipeDatabase.getInstance(BookmarkActivity.this).recipeBookmarkDao().delete(bookmark)).start();
            bookmarkAdapter.removeBookmark(position);
            Toast.makeText(BookmarkActivity.this, "Removed from bookmark", Toast.LENGTH_SHORT).show();
        }
    };
}
