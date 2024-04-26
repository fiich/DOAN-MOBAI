package com.example.hoangvancook;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.hoangvancook.Adapters.IngredientsAdapter;
import com.example.hoangvancook.Adapters.InstructionsAdapter;
import com.example.hoangvancook.Listeners.InstructionsListener;
import com.example.hoangvancook.Listeners.RecipeDetailsListener;
import com.example.hoangvancook.Models.InstructionsResponse;
import com.example.hoangvancook.Models.RecipeDetailsResponse;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

public class RecipeDetailsActivity extends AppCompatActivity {
    int id;
    TextView textView_meal_name, textView_meal_source, textView_meal_summary;
    ImageView imageView_meal_image;
    RecyclerView recycler_meal_ingredients, recycler_meal_equipments, recycler_meal_instructions;
    RequestManager manager;
    ProgressDialog dialog;
    IngredientsAdapter ingredientsAdapter;
    InstructionsAdapter instructionsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        findViews();
        int id;
        id = getIntent().getIntExtra("id", 0);

//        id = Integer.parseInt(Objects.requireNonNull(getIntent().getStringExtra("id")));
        manager = new RequestManager(this);
        manager.getRecipeDetails(recipeDetailsListener, id);
        manager.getInstructions(instructionsListener,id);


        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading Details...");
        dialog.show();
    }

    private void findViews() {
        textView_meal_name = findViewById(R.id.textView_meal_name);
        textView_meal_source = findViewById(R.id.textView_meal_source);
        textView_meal_summary = findViewById(R.id.textView_meal_summary);
        imageView_meal_image = findViewById(R.id.imageView_meal_image);
        recycler_meal_ingredients = findViewById(R.id.recycler_meal_ingredients);
        recycler_meal_instructions = findViewById(R.id.recycler_meal_instructions);

    }


    private final RecipeDetailsListener recipeDetailsListener = new RecipeDetailsListener() {
        @Override
        public void didFetch(RecipeDetailsResponse response, String message) {
            dialog.dismiss();
            textView_meal_name.setText(response.title);
            textView_meal_source.setText(response.sourceName);
            textView_meal_summary.setText(response.summary.replaceAll("\\<.*?\\>", ""));
            Picasso.get().load(response.image).into(imageView_meal_image);

            recycler_meal_ingredients.setHasFixedSize(true);
            recycler_meal_ingredients.setLayoutManager(new LinearLayoutManager(RecipeDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false));



            ingredientsAdapter = new IngredientsAdapter(RecipeDetailsActivity.this, response.extendedIngredients);
            recycler_meal_ingredients.setAdapter(ingredientsAdapter);




        }
        @Override
        public void didError(String message) {
            Toast.makeText(RecipeDetailsActivity.this, message, Toast.LENGTH_SHORT ).show();
        }
    };
    private final InstructionsListener instructionsListener = new InstructionsListener() {
        @Override
        public void didFetch(List<InstructionsResponse> response, String message) {
            recycler_meal_instructions.setHasFixedSize(true);
            recycler_meal_instructions.setLayoutManager(new LinearLayoutManager(RecipeDetailsActivity.this,LinearLayoutManager.VERTICAL,false));
            instructionsAdapter = new InstructionsAdapter(RecipeDetailsActivity.this,response);
            recycler_meal_instructions.setAdapter(instructionsAdapter);

        }

        @Override
        public void didError(String message) {
            Toast.makeText(RecipeDetailsActivity.this, message, Toast.LENGTH_SHORT ).show();
        }
    };
}
