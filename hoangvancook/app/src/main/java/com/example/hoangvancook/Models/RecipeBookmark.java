package com.example.hoangvancook.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "bookmarks")
public class RecipeBookmark {
    @PrimaryKey(autoGenerate = true)
    public int id;
    
    public int recipeId;
    public String title;
    public String image;

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int servings;
    public int readyInMinutes;

    public RecipeBookmark(int recipeId, String title, String image, int servings, int readyInMinutes) {
        this.recipeId = recipeId;
        this.title = title;
        this.image = image;
        this.servings = servings;
        this.readyInMinutes = readyInMinutes;
    }
}

