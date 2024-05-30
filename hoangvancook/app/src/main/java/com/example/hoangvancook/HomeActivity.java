package com.example.hoangvancook;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;

import android.content.Context;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;

import android.view.View;


import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hoangvancook.Adapters.RandomRecipeAdapter;
import com.example.hoangvancook.Listeners.RandomRecipeResponseListener;
import com.example.hoangvancook.Listeners.RecipeClickListener;
import com.example.hoangvancook.Listeners.RecipeLongClickListener;
import com.example.hoangvancook.Models.RandomRecipeApiResponse;
import com.example.hoangvancook.Models.Recipe;
import com.example.hoangvancook.Models.RecipeBookmark;
import com.example.hoangvancook.Models.RecipeDatabase;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {
    RequestManager manager;
    RandomRecipeAdapter randomRecipeAdapter;
    RecyclerView recyclerView;
    ChipGroup chipGroup;
    ImageView toggleFilterButton;
    HorizontalScrollView chipFilterScrollView;
    List<String> tags = new ArrayList<>();
    NestedScrollView nestedScrollView;
    Dialog dialog;
    BottomNavigationView bottomNavigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkInternetAndLoadData();
        findViews();

        String[] filterOptions = getResources().getStringArray(R.array.tags);

        for (String option : filterOptions) {
            addChip(option);
        }
        toggleFilterButton.setOnClickListener(v -> toggleFilterVisibility());

        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(() -> {
            checkInternetAndLoadData();
            pullToRefresh.setRefreshing(false);
        });

        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            tags.clear(); // Clear existing tags
            // Iterate over the checked chips and add their text to the tags list
            for (int i = 0; i < group.getChildCount(); i++) {
                Chip chip = (Chip) group.getChildAt(i);
                if (chip.isChecked()) {
                    tags.add(chip.getText().toString());
                }
            }
            // Call API to get random recipes with the selected tags
            checkInternetAndLoadData();
        });

        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.action_home);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.action_bookmark) {
                Intent bookmarkintent = new Intent(HomeActivity.this, BookmarkActivity.class);
                startActivity(bookmarkintent);
                return true;
            } else if (itemId == R.id.action_search) {
                Intent searchintent = new Intent(HomeActivity.this, SearchActivity.class);
                startActivity(searchintent);
                return true;
            } else if (itemId == R.id.action_profile) {
                Intent searchintent = new Intent(HomeActivity.this, Profile.class);
                startActivity(searchintent);
                return true;
            }
            return false;
        });
    }
    public void checkInternetAndLoadData() {
        if (isInternetConnected()) {
            loadData();
        } else {
            showError("No internet connection!");
        }
    }
    public boolean isInternetConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
    public void showError(String errorMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_error,null);
        builder.setView(dialogView);
        builder.setMessage(errorMessage);
        builder.setPositiveButton("Wi-Fi Settings", (dialog, which) -> {
            Intent wifiSettingsIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
            startActivity(wifiSettingsIntent);
        });
        builder.setNegativeButton("Retry", (dialog, which) -> checkInternetAndLoadData());
        builder.create();
        builder.show();
    }

    public void loadData(){
        dialog = new Dialog(this);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_loading);
        dialog.create();
        dialog.show();
        manager = new RequestManager(this);
        manager.getRandomRecipes(randomRecipeResponseListener,tags);
    }
    public void findViews(){
        chipGroup = findViewById(R.id.chipGroup);
        toggleFilterButton = findViewById(R.id.toggleFilterButton);
        chipFilterScrollView = findViewById(R.id.chipFilterScrollView);
        nestedScrollView = findViewById(R.id.nestedScrollView);
    }
    private final RandomRecipeResponseListener randomRecipeResponseListener = new RandomRecipeResponseListener() {
        @Override
        public void didFetch(RandomRecipeApiResponse response, String message) {
            dialog.dismiss();
            recyclerView = findViewById(R.id.recycler_random);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(HomeActivity.this, 2));
            randomRecipeAdapter = new RandomRecipeAdapter(HomeActivity.this, response.recipes, recipeClickListener,recipeLongClickListener);
            recyclerView.setAdapter(randomRecipeAdapter);
        }
        @Override
        public void didError(String message) {
            Toast.makeText(HomeActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    };
    private void toggleFilterVisibility() {

        if (chipFilterScrollView.getVisibility() == View.VISIBLE) {
            chipFilterScrollView.setVisibility(View.GONE);
            nestedScrollView.scrollTo(0, 0);
        } else {
            chipFilterScrollView.setVisibility(View.VISIBLE);
            nestedScrollView.scrollTo(0, nestedScrollView.getChildAt(0).getHeight());
        }
    }
    private void addChip(String text) {
        @SuppressLint("InflateParams") Chip chip = (Chip) LayoutInflater.from(HomeActivity.this).inflate(R.layout.chip_layout, null);
        chip.setText(text);
        chip.setClickable(true);
        chip.setCheckable(true); // If you want chips to be selectable
        chip.setPadding(8, 0, 8, 0); // Adjust padding as needed
        chipGroup.addView(chip);
    }

    private  final RecipeClickListener recipeClickListener = id -> startActivity(new Intent(HomeActivity.this, RecipeDetailsActivity.class)
            .putExtra("id", id));
    private final RecipeLongClickListener recipeLongClickListener = this::showBookmarkDialog;

    private void showBookmarkDialog(final Recipe recipe) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add to Bookmark");
        builder.setMessage("Do you want to add this recipe to your bookmarks?");
        builder.setPositiveButton("Add", (dialog, which) -> {
            addToBookmarks(recipe);
            Toast.makeText(HomeActivity.this, "Recipe added to bookmarks", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }
    public void addToBookmarks(Recipe recipe) {
        new Thread(() -> {
            RecipeBookmark existingBookmark = RecipeDatabase.getInstance(HomeActivity.this)
                    .recipeBookmarkDao()
                    .getBookmarkByRecipeId(recipe.id);

            if (existingBookmark == null) {
                RecipeBookmark recipeBookmark = new RecipeBookmark(
                        recipe.id,
                        recipe.title,
                        recipe.image,
                        recipe.servings,
                        recipe.readyInMinutes
                );
                RecipeDatabase.getInstance(HomeActivity.this)
                        .recipeBookmarkDao()
                        .insert(recipeBookmark);
                runOnUiThread(() -> Toast.makeText(HomeActivity.this, "Recipe added to bookmarks", Toast.LENGTH_SHORT).show());
            } else {
                runOnUiThread(() -> Toast.makeText(HomeActivity.this, "Recipe already bookmarked", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }


}