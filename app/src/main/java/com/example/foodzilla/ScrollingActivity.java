package com.example.foodzilla;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.foodzilla.databinding.ActivityScrollingBinding;
import com.example.foodzilla.databinding.ContentScrollingBinding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ScrollingActivity extends AppCompatActivity implements View.OnClickListener {

    private int localId;
    private String recipeName;
    private RecipeDatabase db;
    private RecipeDao dao;
    private ActivityScrollingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScrollingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        Intent intent = getIntent();
        recipeName = intent.getStringExtra("title");
        int id = intent.getIntExtra("id", 0);
        boolean isOffline = intent.getBooleanExtra("isOffline", false);

        getWindow().setEnterTransition(new Slide(Gravity.BOTTOM));

        binding.toolbarLayout.setTitle(recipeName);
        binding.fab.setOnClickListener(this);
        Bitmap image = intent.getParcelableExtra("image");
        String url = "https://foodzilla-salmannotkhan.vercel.app/";
        binding.imageAlt.setImageBitmap(image);
        db = Room.databaseBuilder(getApplicationContext(),
                RecipeDatabase.class,
                "database-name").allowMainThreadQueries().build();
        dao = db.recipeDao();
        localId = dao.getId(recipeName);
        if (localId != 0){
            binding.fab.setImageResource(R.drawable.favorite_filled);
        }
        if (!isOffline) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            FoodZillaAPI api = retrofit.create(FoodZillaAPI.class);
            Call<Recipe> respList = api.getRecipe(id);
            respList.enqueue(new Callback<Recipe>() {
                @Override
                public void onResponse(@NonNull Call<Recipe> call,@NonNull Response<Recipe> response) {
                    Recipe recipe = response.body();
                    if (recipe != null) {
                        Glide.with(ScrollingActivity.this).load(recipe.getImage()).centerCrop().into(binding.imageAlt);
                        List<String> ingredients =  recipe.getIngredients();
                        StringBuilder ingredientsString = new StringBuilder();
                        for (String ingredient : ingredients) {
                            ingredientsString.append("• ").append(ingredient).append("\n");
                        }
                        List<String> steps = recipe.getSteps();
                        StringBuilder stepsString = new StringBuilder();
                        for (String step : steps) {
                            stepsString.append("• ").append(step).append("\n\n");
                        }
                        binding.content.recipeIngredients.setText(ingredientsString.toString());
                        binding.content.recipeInstructions.setText(stepsString.toString());
                        binding.content.txtCook.setText(String.valueOf(recipe.getCookTime()));
                        binding.content.txtPrep.setText(String.valueOf(recipe.getPrepTime()));
                        binding.content.txtCalories.setText(String.valueOf(recipe.getNutrition()));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Recipe> call,@NonNull Throwable t) {
                    Toast.makeText(ScrollingActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                }
            });
            Call<List<Rating>> ratings = api.getRatings(id);
            ratings.enqueue(new Callback<List<Rating>>() {
                @Override
                public void onResponse(Call<List<Rating>> call, Response<List<Rating>> response) {
                    RatingAdapter myRatingAdapter = new RatingAdapter();
                    binding.content.reviews.recyclerView.setAdapter(myRatingAdapter);
                    myRatingAdapter.setRatingList(response.body());
                }

                @Override
                public void onFailure(Call<List<Rating>> call, Throwable t) {
                    Toast.makeText(ScrollingActivity.this, "Network error: Can't fetch ratings", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            Recipe recipe = dao.getById(id);
            File file = new File(getApplicationContext().getFilesDir(), recipe.getImage());
            byte[] bytes = new byte[(int) file.length()];
            try {
                getApplicationContext().openFileInput(recipe.getImage()).read(bytes);
                Glide.with(ScrollingActivity.this).load(bytes).centerCrop().into(binding.imageAlt);
            }
            catch (IOException e){
                e.printStackTrace();
            }
            binding.content.recipeIngredients.setText(recipe.getIngredient());
            binding.content.recipeInstructions.setText(recipe.getStep());
            binding.content.txtCook.setText(String.valueOf(recipe.getCookTime()));
            binding.content.txtPrep.setText(String.valueOf(recipe.getPrepTime()));
            binding.content.txtCalories.setText(String.valueOf(recipe.getNutrition()));
            binding.content.reviews.reviewSection.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        localId = dao.getId(recipeName);
        if (localId != 0 ) {
            Recipe recipe = dao.getById(localId);
            dao.delete(recipe);
            File file = new File(recipeName + ".jpg");
            file.delete();
            Toast.makeText(this, "Removed from favorite", Toast.LENGTH_SHORT).show();
            binding.fab.setImageResource(R.drawable.favorite_empty);
        }
        else {
            Bitmap bmp = ((BitmapDrawable) binding.imageAlt.getDrawable()).getBitmap();
            int bytes = bmp.getByteCount();

            ByteBuffer buffer = ByteBuffer.allocate(bytes);
            bmp.copyPixelsToBuffer(buffer);
            try (FileOutputStream fos = getApplicationContext().openFileOutput(recipeName + ".jpg", MODE_PRIVATE)) {
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Recipe recipe = new Recipe(
                    recipeName,
                    recipeName + ".jpg",
                    true,
                    "",
                    "",
                    "",
                    "",
                    "",
                    Integer.parseInt(binding.content.txtCook.getText().toString()),
                    Integer.parseInt(binding.content.txtPrep.getText().toString()),
                    binding.content.recipeIngredients.getText().toString(),
                    binding.content.recipeInstructions.getText().toString(),
                    Integer.parseInt(binding.content.txtCalories.getText().toString()),
                    (int)(Math.random()*(5-1+1)+1)

            );


            RecipeDao dao = db.recipeDao();
            dao.insertAll(recipe);
            Toast.makeText(this, "Added to favorite", Toast.LENGTH_SHORT).show();
            binding.fab.setImageResource(R.drawable.favorite_filled);

        }
    }
}