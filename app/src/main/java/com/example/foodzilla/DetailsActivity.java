//package com.example.foodzilla;
//
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import android.transition.Slide;
//import android.view.Gravity;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.room.Room;
//
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.android.volley.toolbox.Volley;
//import com.bumptech.glide.Glide;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//
//import java.lang.reflect.Array;
//import java.util.Arrays;
//
//public class DetailsActivity extends AppCompatActivity implements View.OnClickListener {
//    ImageView imageMain;
//    TextView textTitle;
//    TextView textIngredients;
//    TextView textInstructions;
//    TextView textPrep;
//    TextView textCook;
//    TextView textKcal;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_details);
//        imageMain = findViewById(R.id.imageView);
//        textTitle = findViewById(R.id.recipe_title);
//        textInstructions = findViewById(R.id.recipe_instructions);
//        textIngredients = findViewById(R.id.recipe_ingredients);
//        textCook = findViewById(R.id.txt_cook);
//        textPrep = findViewById(R.id.txt_prep);
//        textKcal = findViewById(R.id.txt_calories);
//        this.getWindow().setEnterTransition(new Slide(Gravity.BOTTOM));
//        Intent currIntent = getIntent();
//        int id = currIntent.getIntExtra("id", 0);
//        boolean isOffline = currIntent.getBooleanExtra("isOffline", false);
//        Bitmap image = currIntent.getParcelableExtra("image");
//        String url = "https://foodzilla-salmannotkhan.vercel.app/recipe/" + id;
//        imageMain.setImageBitmap(image);
//        if (!isOffline) {
//            RequestQueue requestQueue = Volley.newRequestQueue(DetailsActivity.this);
//            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
//                try {
//                    textTitle.setText(response.getString("name"));
//                    Glide.with(DetailsActivity.this).load(response.getString("image")).centerCrop().into(imageMain);
//                    JSONArray ingredients = (JSONArray) response.getJSONArray("ingredients");
//                    StringBuilder ingredientsString = new StringBuilder();
//                    for (int i = 0; i < ingredients.length(); i++) {
//                        ingredientsString.append("• ").append(ingredients.get(i)).append("\n");
//                    }
//                    textIngredients.setText(ingredientsString.toString());
//                    JSONArray steps = (JSONArray) response.getJSONArray("steps");
//                    StringBuilder stepsString = new StringBuilder();
//                    for (int i = 0; i < steps.length(); i++) {
//                        stepsString.append("• ").append(steps.get(i)).append("\n\n");
//                    }
//                    textInstructions.setText(stepsString.toString());
//                    textCook.setText(String.valueOf(response.getInt("cook_time")));
//                    textPrep.setText(String.valueOf(response.getInt("prep_time")));
//                    textKcal.setText(String.valueOf(response.getInt("nutrition")));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }, error -> Toast.makeText(DetailsActivity.this, error.toString(), Toast.LENGTH_SHORT).show());
//            requestQueue.add(jsonObjectRequest);
//        }
//        else {
//            RecipeDatabase db = Room.databaseBuilder(getApplicationContext(),
//                    RecipeDatabase.class,
//                    "database-name").allowMainThreadQueries().build();
//            RecipeDao dao = db.recipeDao();
//            Recipe recipe = dao.getById(id);
//            textTitle.setText(recipe.getName());
//            Glide.with(DetailsActivity.this).load(recipe.getName()).centerCrop().into(imageMain);
////            textIngredients.setText(recipe.getIngredients());
////            textInstructions.setText(recipe.getSteps());
//            textCook.setText(String.valueOf(recipe.getCookTime()));
//            textPrep.setText(String.valueOf(recipe.getPrepTime()));
//            textKcal.setText(String.valueOf(recipe.getNutrition()));
//        }
//    }
//
//    @Override
//    public void onClick(View v) {
//        RecipeDatabase db = Room.databaseBuilder(getApplicationContext(),
//                RecipeDatabase.class,
//                "database-name").allowMainThreadQueries().build();
//        Recipe recipe = new Recipe(
//                textTitle.getText().toString(),
//                "",
//                true,
//                "",
//                "",
//                "",
//                "",
//                "",
//                Integer.parseInt(textCook.getText().toString()),
//                Integer.parseInt(textPrep.getText().toString()),
//                textIngredients.getText().toString(),
//                textInstructions.getText().toString(),
//                Integer.parseInt(textKcal.getText().toString()));
//        RecipeDao dao = db.recipeDao();
//        dao.insertAll(recipe);
//        Toast.makeText(this, "Added to favorite", Toast.LENGTH_SHORT).show();
//    }
//}