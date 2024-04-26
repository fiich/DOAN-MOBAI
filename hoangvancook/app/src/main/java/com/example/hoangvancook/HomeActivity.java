package com.example.hoangvancook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hoangvancook.Adapters.RandomRecipeAdapter;
import com.example.hoangvancook.Listeners.RandomRecipeResponseListener;
import com.example.hoangvancook.Listeners.RecipeClickListener;
import com.example.hoangvancook.Models.RandomRecipeApiResponse;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    RequestManager manager;
    RandomRecipeAdapter randomRecipeAdapter;
    RecyclerView recyclerView;
    ChipGroup chipGroup;
    ImageView toggleFilterButton,imageView_bookmark;
    HorizontalScrollView chipFilterScrollView;
    List<String> tags = new ArrayList<>();
    NestedScrollView nestedScrollView;
    ProgressDialog dialog;



    public void checkInternetAndLoadData() {
        if (isInternetConnected()) {
            loadData();
        } else {
            // Show error message indicating no internet connection
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
        builder.setTitle("NO INTERNET CONNECTION");
        builder.setMessage(errorMessage);
        builder.setPositiveButton("OK", null);
        builder.show();
    }
    public void loadData(){
        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading...");
        dialog.show();
        manager = new RequestManager(this);
        manager.getRandomRecipes(randomRecipeResponseListener,tags);
    }
    public void findViews(){
        chipGroup = findViewById(R.id.chipGroup);
        toggleFilterButton = findViewById(R.id.toggleFilterButton);
        chipFilterScrollView = findViewById(R.id.chipFilterScrollView);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        imageView_bookmark = findViewById(R.id.imageView_bookmark);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkInternetAndLoadData();
        findViews();

        String[] filterOptions = getResources().getStringArray(R.array.tags);
        // Add chips for each item in the string array
        for (String option : filterOptions) {
            addChip(option);
        }
        toggleFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFilterVisibility();
            }
        });

        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                checkInternetAndLoadData();
                pullToRefresh.setRefreshing(false);
            }
        });

        chipGroup.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
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
            }
        });

    }
    private final RandomRecipeResponseListener randomRecipeResponseListener = new RandomRecipeResponseListener() {
        @Override
        public void didFetch(RandomRecipeApiResponse response, String message) {
            dialog.dismiss();
            recyclerView = findViewById(R.id.recycler_random);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(HomeActivity.this, 2));

            randomRecipeAdapter = new RandomRecipeAdapter(HomeActivity.this, response.recipes, recipeClickListener);
            recyclerView.setAdapter(randomRecipeAdapter);
        }
        @Override
        public void didError(String message) {
            Toast.makeText(HomeActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    };
    private void toggleFilterVisibility() {
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);

        if (chipFilterScrollView.getVisibility() == View.VISIBLE) {
            chipFilterScrollView.setVisibility(View.GONE);
            chipFilterScrollView.startAnimation(fadeOut);
            nestedScrollView.scrollTo(0, 0);
        } else {
            chipFilterScrollView.setVisibility(View.VISIBLE);
            chipFilterScrollView.startAnimation(fadeIn);
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

    private  final RecipeClickListener recipeClickListener = new RecipeClickListener() {
        @Override
        public void onRecipeClick(String id) {
            startActivity(new Intent(HomeActivity.this, RecipeDetailsActivity.class)
                    .putExtra("id", id));
        }
    };

}