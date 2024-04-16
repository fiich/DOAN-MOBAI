package com.example.hoangvancook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.hoangvancook.Adapters.RandomRecipeAdapter;
import com.example.hoangvancook.Listeners.RandomRecipeResponseListener;
import com.example.hoangvancook.Models.RandomRecipeApiResponse;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RequestManager manager;
    RandomRecipeAdapter randomRecipeAdapter;
    RecyclerView recyclerView;
    ChipGroup chipGroup;
    Chip chip;
    ImageView toggleFilterButton;
    HorizontalScrollView chipFilterScrollView;
    List<String> tags = new ArrayList<>();

    NestedScrollView nestedScrollView;




    public void loadData(){
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
        nestedScrollView = findViewById(R.id.nestedScrollView);



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
        Chip chip = (Chip) LayoutInflater.from(MainActivity.this).inflate(R.layout.chip_layout, null);
        chip.setText(text);
        chip.setClickable(true);
        chip.setCheckable(true); // If you want chips to be selectable
        chip.setPadding(8, 0, 8, 0); // Adjust padding as needed
        chipGroup.addView(chip);
    }


}