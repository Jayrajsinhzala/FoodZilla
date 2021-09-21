package com.example.foodzilla;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity
public class Recipe {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo
    private String name;

    @ColumnInfo
    private String image;

    @SerializedName("is_veg")
    @ColumnInfo
    private Boolean isVeg;

    @ColumnInfo
    private String cuisine;

    @ColumnInfo
    private String course;

    @ColumnInfo
    private String taste;

    @ColumnInfo
    private int rating;

    @SerializedName("cooking_level")
    @ColumnInfo
    private String cookingLevel;

    @SerializedName("main_ingredients")
    @ColumnInfo
    private String mainIngredients;

    @SerializedName("prep_time")
    @ColumnInfo
    private int prepTime;

    @SerializedName("cook_time")
    @ColumnInfo
    private int cookTime;

    @Ignore
    private List<String> ingredients;

    @Ignore
    private List<String> steps;

    @ColumnInfo
    private int nutrition;

    @ColumnInfo
    private String ingredient;

    @ColumnInfo
    private String step;

    public Recipe(String name,
                  String image,
                  Boolean isVeg,
                  String cuisine,
                  String course,
                  String taste,
                  String cookingLevel,
                  String mainIngredients,
                  int prepTime,
                  int cookTime,
                  String ingredient,
                  String step,
                  int nutrition,
                  int rating) {
        this.name = name;
        this.image = image;
        this.isVeg = isVeg;
        this.cuisine = cuisine;
        this.course = course;
        this.taste = taste;
        this.cookingLevel = cookingLevel;
        this.mainIngredients = mainIngredients;
        this.prepTime = prepTime;
        this.cookTime = cookTime;
        this.ingredient = ingredient;
        this.step = step;
        this.nutrition = nutrition;
        this.rating = rating;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setVeg(Boolean veg) {
        isVeg = veg;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public void setTaste(String taste) {
        this.taste = taste;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setCookingLevel(String cookingLevel) {
        this.cookingLevel = cookingLevel;
    }

    public void setMainIngredients(String mainIngredients) {
        this.mainIngredients = mainIngredients;
    }

    public void setPrepTime(int prepTime) {
        this.prepTime = prepTime;
    }

    public void setCookTime(int cookTime) {
        this.cookTime = cookTime;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }

    public void setNutrition(int nutrition) {
        this.nutrition = nutrition;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public Boolean getIsVeg() {
        return isVeg;
    }

    public String getCuisine() {
        return cuisine;
    }

    public String getCourse() {
        return course;
    }

    public String getTaste() {
        return taste;
    }

    public int getRating() {
        return rating;
    }

    public String getCookingLevel() {
        return cookingLevel;
    }

    public String getMainIngredients() {
        return mainIngredients;
    }

    public int getPrepTime() {
        return prepTime;
    }

    public int getCookTime() {
        return cookTime;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public List<String> getSteps() {
        return steps;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public int getNutrition() {
        return nutrition;
    }

}
