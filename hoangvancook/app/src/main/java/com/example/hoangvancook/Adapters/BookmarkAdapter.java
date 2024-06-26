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
import com.example.hoangvancook.Models.Recipe;
import com.example.hoangvancook.Models.RecipeBookmark;
import com.example.hoangvancook.R;
import com.squareup.picasso.Picasso;
import java.util.List;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.BookmarkViewHolder> {

    Context context;
    List<Recipe> recipes;
    List<RecipeBookmark> bookmarks;
    RecipeClickListener  recipeClickListener;

    public BookmarkAdapter(Context context, List<RecipeBookmark> bookmarks, RecipeClickListener recipeClickListener) {
        this.context = context;
        this.bookmarks = bookmarks;
        this.recipeClickListener = recipeClickListener;
    }

    @NonNull
    @Override
    public BookmarkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BookmarkViewHolder(LayoutInflater.from(context).inflate(R.layout.list_bookmark, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull BookmarkViewHolder holder, int position) {
        RecipeBookmark bookmark = bookmarks.get(position);

        holder.textView_title.setText(bookmarks.get(position).title);
        holder.textView_title.setSelected(true);
        holder.textView_serving.setText(bookmarks.get(position).servings + " ");
        holder.textView_time.setText(bookmarks.get(position).readyInMinutes + "'");
        Picasso.get().load(bookmarks.get(position).image)
                .placeholder(R.drawable.image_error)
                .error(R.drawable.image_error)
                .into(holder.imageView_food);
        holder.itemView.setOnClickListener(v -> recipeClickListener.onRecipeClick(String.valueOf(bookmark.getRecipeId())));
    }

    @Override
    public int getItemCount() {
        return bookmarks.size();
    }

    public void updateBookmarks(List<RecipeBookmark> newBookmarks) {
        bookmarks.clear();
        bookmarks.addAll(newBookmarks);
        notifyDataSetChanged();
    }
    public void removeBookmark(int position) {
        bookmarks.remove(position);
        notifyItemRemoved(position);
    }
    public RecipeBookmark getBookmarkAtPosition(int position) {
        return bookmarks.get(position);
    }

    public static class BookmarkViewHolder extends RecyclerView.ViewHolder {
        TextView textView_title, textView_serving, textView_time;
        ImageView imageView_food;
        CardView bookmark_list_container;

        public BookmarkViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_title = itemView.findViewById(R.id.textView_title);
            textView_serving = itemView.findViewById(R.id.textView_serving);
            textView_time = itemView.findViewById(R.id.textView_time);
            imageView_food = itemView.findViewById(R.id.imageView_food);
            bookmark_list_container = itemView.findViewById(R.id.bookmark_list_container);
        }
    }
}
