package com.example.hoangvancook.Models;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RecipeBookmarkDao {
    @Insert
    void insert(RecipeBookmark recipeBookmark);
    @Delete
    void delete(RecipeBookmark recipeBookmark);

    @Query("SELECT * FROM bookmarks")
    List<RecipeBookmark> getAllBookmarks();
    @Query("SELECT * FROM bookmarks WHERE recipeId = :recipeId")
    RecipeBookmark getBookmarkByRecipeId(int recipeId);
}
