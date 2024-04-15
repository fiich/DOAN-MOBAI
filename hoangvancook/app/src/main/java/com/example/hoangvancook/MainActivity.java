package com.example.hoangvancook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hoangvancook.Adapters.RandomRecipeAdapter;
import com.example.hoangvancook.Listeners.RandomRecipeResponseListener;
import com.example.hoangvancook.Models.RandomRecipeApiResponse;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ProgressDialog dialog;
    RequestManager manager;
    RandomRecipeAdapter randomRecipeAdapter;
    RecyclerView recyclerView;
    ChipGroup chipGroup;
    Chip chip;
    ImageView toggleFilterButton;
    HorizontalScrollView chipFilterScrollView;
    List<String> tags = new ArrayList<>();



    public void loadData(){

        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading...");
        manager = new RequestManager(this);
        manager.getRandomRecipes(randomRecipeResponseListener,tags);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadData();

        chipGroup = findViewById(R.id.chipGroup);
        toggleFilterButton = findViewById(R.id.toggleFilterButton);
        chipFilterScrollView = findViewById(R.id.chipFilterScrollView);


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
                loadData();
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
                manager.getRandomRecipes(randomRecipeResponseListener,tags);

            }
        });
    }
    private final RandomRecipeResponseListener randomRecipeResponseListener = new RandomRecipeResponseListener() {
        @Override
        public void didFetch(RandomRecipeApiResponse response, String message) {
            recyclerView = findViewById(R.id.recycler_random);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 1));

            randomRecipeAdapter = new RandomRecipeAdapter(MainActivity.this, response.recipes);
            recyclerView.setAdapter(randomRecipeAdapter);
        }
        @Override
        public void didError(String message) {
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    };
    private void toggleFilterVisibility() {
        if (chipFilterScrollView.getVisibility() == View.VISIBLE) {
            chipFilterScrollView.setVisibility(View.GONE);
        } else {
            chipFilterScrollView.setVisibility(View.VISIBLE);
        }
    }
    private void addChip(String text) {
        Chip chip = (Chip) LayoutInflater.from(MainActivity.this).inflate(R.layout.chip_layout, null);
        chip.setText(text);
        chip.setClickable(true);
        chip.setCheckable(true); // If you want chips to be selectable
        chip.setPadding(8, 0, 8, 0); // Adjust padding as needed
        chipGroup.addView(chip);
    }


}