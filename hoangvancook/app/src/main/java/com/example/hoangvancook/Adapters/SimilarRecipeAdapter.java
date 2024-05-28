package com.example.hoangvancook.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hoangvancook.Listeners.RecipeClickListener;
import com.example.hoangvancook.Models.SimilarRecipeResponse;
import com.example.hoangvancook.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SimilarRecipeAdapter extends RecyclerView.Adapter<SimilarRecipeViewHolder>{
    Context context;
    List<SimilarRecipeResponse> list;
    RecipeClickListener listener;

    public SimilarRecipeAdapter(Context context, List<SimilarRecipeResponse> list, RecipeClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SimilarRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SimilarRecipeViewHolder(LayoutInflater.from(context).inflate(R.layout.list_similar_recipe, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SimilarRecipeViewHolder holder, int position) {
        holder.textView_similar_recipe.setText(list.get(position).title);
        holder.textView_similar_recipe.setSelected(true);
        holder.textView_similar_recipe_serving.setText(String.valueOf(list.get(position).servings));
        holder.textView_similar_recipe_time.setText(String.valueOf(list.get(position).readyInMinutes));
        Picasso.get().load("https://spoonacular.com/recipeimages/"+list.get(position).id+"-556x370."+list.get(position).imageType)
                .placeholder(R.drawable.image_error)
                .error(R.drawable.image_error)
                .into(holder.imageView_similar_recipe);

        holder.similar_recipe_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRecipeClick(String.valueOf(list.get(holder.getAdapterPosition()).id));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
class SimilarRecipeViewHolder extends RecyclerView.ViewHolder{

    CardView similar_recipe_container;
    TextView textView_similar_recipe,textView_similar_recipe_serving,textView_similar_recipe_time;
    ImageView imageView_similar_recipe;
    public SimilarRecipeViewHolder(@NonNull View itemView) {
        super(itemView);
        similar_recipe_container = itemView.findViewById(R.id.similar_recipe_container);
        textView_similar_recipe = itemView.findViewById(R.id.textView_similar_recipe);
        textView_similar_recipe_serving = itemView.findViewById(R.id.textView_similar_recipe_serving);
        textView_similar_recipe_time = itemView.findViewById(R.id.textView_similar_recipe_time);
        imageView_similar_recipe = itemView.findViewById(R.id.imageView_similar_recipe);
    }
}