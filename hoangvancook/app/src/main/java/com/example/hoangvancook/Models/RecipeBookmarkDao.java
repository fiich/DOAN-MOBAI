package com.example.hoangvancook.Models;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RecipeBookmarkDao {
    @Insert
    void insert(RecipeBookmark recipeBookmark);

    @Query("SELECT * FROM bookmarks")
    List<RecipeBookmark> getAllBookmarks();
}
