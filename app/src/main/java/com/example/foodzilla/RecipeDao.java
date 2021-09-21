package com.example.foodzilla;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RecipeDao {
    @Query("SELECT * from Recipe")
    List<Recipe> getAll();

    @Query("SELECT * from Recipe where id = :id")
    Recipe getById(int id);

    @Query("SELECT id FROM Recipe where name = :name")
    int getId(String name);

    @Query("DELETE FROM Recipe")
    void deleteAll();

    @Insert
    void insertAll(Recipe... recipes);

    @Delete
    void delete(Recipe recipe);

    @Query(("SELECT * from Recipe where rating between :min and :max"))
    List<Recipe> getRange(int min,int max);
}
