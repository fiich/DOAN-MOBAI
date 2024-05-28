package com.example.hoangvancook.Adapters;

import android.annotation.SuppressLint;
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
import com.example.hoangvancook.Listeners.RecipeLongClickListener;
import com.example.hoangvancook.Models.Recipe;
import com.example.hoangvancook.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RandomRecipeAdapter extends RecyclerView.Adapter<RandomRecipeViewHolder>{
    Context context;
    List<Recipe> list;
    RecipeClickListener clickListener;
    RecipeLongClickListener longClickListener;

    public RandomRecipeAdapter(Context context, List<Recipe> list, RecipeClickListener clickListener, RecipeLongClickListener longClickListener) {
        this.context = context;
        this.list = list;
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;
    }

    @NonNull
    @Override
    public RandomRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RandomRecipeViewHolder(LayoutInflater.from(context).inflate(R.layout.list_random_recipe, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RandomRecipeViewHolder holder, int position) {
        holder.textView_title.setText(list.get(position).title);
        holder.textView_title.setSelected(true);
        holder.textView_serving.setText(list.get(position).servings + " ");
        holder.textView_time.setText(list.get(position).readyInMinutes + "'");
        Picasso.get().load(list.get(position).image)
                .placeholder(R.drawable.image_error)
                .error(R.drawable.image_error)
                .into(holder.imageView_food);


        holder.random_list_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onRecipeClick(String.valueOf(list.get(holder.getAdapterPosition()).id));
            }
        });
        holder.random_list_container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                longClickListener.onRecipeLongClick(list.get(holder.getAdapterPosition()));
                return true;
            }
        });
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
}
class RandomRecipeViewHolder extends RecyclerView.ViewHolder{
    CardView random_list_container;
    TextView textView_title, textView_serving, textView_time;
    ImageView imageView_food;
    public RandomRecipeViewHolder(@NonNull View itemView) {
        super(itemView);
        random_list_container = itemView.findViewById(R.id.random_list_container);
        textView_title = itemView.findViewById(R.id.textView_title);
        textView_serving = itemView.findViewById(R.id.textView_serving);
        textView_time = itemView.findViewById(R.id.textView_time);
        imageView_food = itemView.findViewById(R.id.imageView_food);
    }
}

