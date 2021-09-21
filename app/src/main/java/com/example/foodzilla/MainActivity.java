package com.example.foodzilla;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodzilla.databinding.ActivityMainBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static androidx.recyclerview.widget.RecyclerView.INVISIBLE;
import static androidx.recyclerview.widget.RecyclerView.OnScrollListener;
import static androidx.recyclerview.widget.RecyclerView.VISIBLE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private ActivityMainBinding binding;
    private Map<String, Object> filterOptions = new HashMap<>();
    private List<Recipe> recipeList;
    private RecipeAdapter myRecipeAdapter;
    private FoodZillaAPI api;
    boolean ended;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences = getSharedPreferences("auth", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");
        recipeList = new ArrayList<>();
        myRecipeAdapter = new RecipeAdapter(MainActivity.this);
        binding.recyclerMain.setAdapter(myRecipeAdapter);
        filterOptions.put("last_id", 0);
        binding.imageProfile.setOnClickListener(this);
        if (email.equals("")) {
            Intent intent = new Intent(this, SignIn.class);
            startActivity(intent);
            finish();
        }

        else if (!email.equals("guest")) {
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
            if (account != null) {
                Glide.with(this).load(account.getPhotoUrl()).into(binding.imageProfile);
            }
        }

        String url = "https://foodzilla-salmannotkhan.vercel.app/";

        binding.isVeg.setOnClickListener(this);
        binding.cuisine.setOnClickListener(this);
        binding.taste.setOnClickListener(this);
        binding.course.setOnClickListener(this);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(FoodZillaAPI.class);

        loadData();

        binding.btnClearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.textSearch.setText("");
                filterOptions.remove("query");
                binding.btnClearSearch.setVisibility(View.GONE);
                loadData();
            }
        });
        binding.textSearch.setOnKeyListener((v, keyCode, event) -> {
            binding.textSearch.setText(binding.textSearch.getText().toString().trim());
            if (binding.textSearch.getText().toString().length() > 0){
                filterOptions.put("query", binding.textSearch.getText().toString());
                binding.textSearch.clearFocus();
                binding.btnClearSearch.setVisibility(VISIBLE);
            }
            else {
                filterOptions.remove("query");
                binding.btnClearSearch.setVisibility(View.GONE);
            }
            loadData();
            return true;
        });
        binding.recyclerMain.addOnScrollListener(new OnScrollListener () {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                boolean scrollable = recyclerView.canScrollVertically(10);
                if (!scrollable && recipeList.size() != 0 && !ended) {
                    binding.textLoading.setVisibility(VISIBLE);
                    Call<List<Recipe>> respList = api.getAllRecipes(filterOptions);
                    respList.enqueue(new Callback<List<Recipe>>() {
                        @Override
                        public void onResponse(@NonNull Call<List<Recipe>> call,@NonNull Response<List<Recipe>> response) {
                            handleResponse(response);
                            binding.textLoading.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(@NonNull Call<List<Recipe>> call,@NonNull Throwable t) {
                            Toast.makeText(MainActivity.this, "Request Failed.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }}
        );
    }

    public void handleResponse(Response<List<Recipe>> response) {
        List<Recipe> respList = response.body();
        if (respList != null) {
            recipeList.addAll(respList);
            if (recipeList.size() == 0) {
                binding.offlineView.setVisibility(VISIBLE);
                binding.textOfflineHint.setText("No Recipe found try changing filters");
                binding.BtnOfflineFav.setVisibility(INVISIBLE);
            }
            else {
                binding.offlineView.setVisibility(View.GONE);
                binding.BtnOfflineFav.setVisibility(VISIBLE);
                myRecipeAdapter.setCards(recipeList);
                int last_id = recipeList.get(recipeList.size() - 1).getId();
                filterOptions.put("last_id", last_id);
            }
            if (respList.size() < 10) {
                ended = true;
            }
        }
        else {
            Toast.makeText(MainActivity.this, "No more data found", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onClick(View v) {
        Intent intent;
        boolean[] previousState;
        AlertDialog.Builder builder;
        switch (v.getId()) {
            case R.id.imageProfile:
                intent = new Intent(MainActivity.this, Settings.class);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, binding.imageProfile, "profile");
                startActivity(intent, options.toBundle());
                break;
            case R.id.BtnOfflineFav:
                intent = new Intent(MainActivity.this, Favourite.class);
                startActivity(intent);
                break;
            case R.id.is_veg:
                if (filterOptions.containsKey("is_veg")){
                    if ((Boolean) filterOptions.get("is_veg")) {
                        binding.isVeg.setText("Non-Vegetarian");
                        binding.isVeg.setCheckedIconTint(ColorStateList.valueOf(Color.RED));
                        binding.isVeg.setChecked(true);
                        filterOptions.put("is_veg", false);
                    }
                    else {
                        filterOptions.remove("is_veg");
                        binding.isVeg.setText("Vegetarian");
                    }
                }
                else {
                    binding.isVeg.setCheckedIconTint(ColorStateList.valueOf(Color.GREEN));
                    filterOptions.put("is_veg", true);
                }
                loadData();
                break;
            case R.id.cuisine:
                String[] cuisines = {
                        "Bengali",
                        "Other Cuisines",
                        "Tamil Nadu,  Kerala",
                        "Gujarati, Indian, Fusion",
                        "Fusion, American",
                        "Mangalorean, Indian",
                        "Indian, Maharashtrian",
                        "fusion",
                        "Rajasthani",
                        "American",
                        "Korean, Fusion",
                        "Punjabi, Indian, Fusion",
                        "German Cuisine",
                        "Other",
                        "Kashmiri",
                        "Punjabi, Indian",
                        "Maharashtrian, Indian, Fusion",
                        "Indian, Gujarati",
                        "indian",
                        "Middle Eastern",
                        "Indo Chinese, Indo-Chinese",
                        "Korean",
                        "Punjabi",
                        "Indo-Mexican",
                        "Oriental, Fusion",
                        "Indian, Parsi",
                        "Madhya Pradesh",
                        "Persian",
                        "Lebanese",
                        "Chinese",
                        "Indian, Rajasthani",
                        "Creole",
                        "Indian"
                };

                List<String> selectedCuisine = new ArrayList<>();
                binding.cuisine.setChecked(false);
                String selectedCuisineString = (String) filterOptions.get("cuisine");
                previousState = new boolean[]{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false};
                if (selectedCuisineString != null){
                    binding.cuisine.setChecked(true);
                    List<String> previouslySelected = Arrays.asList(selectedCuisineString.split("\\|"));
                    for(int i = 0; i < cuisines.length; i++) {
                        previousState[i] = previouslySelected.contains(cuisines[i]);
                    }
                    selectedCuisine.addAll(previouslySelected);
                }
                builder = new AlertDialog.Builder(this);
                builder.setTitle("Select Cuisine:");
                builder.setMultiChoiceItems(cuisines,previousState, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked)
                        {
                            selectedCuisine.add(cuisines[which]);
                        }
                        else
                        {
                            selectedCuisine.remove(cuisines[which]);
                        }
                    }
                });
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StringBuilder selectedCuisine2 = new StringBuilder();
                        for (String cuisine : selectedCuisine){
                            if (selectedCuisine.indexOf(cuisine) != 0){
                                selectedCuisine2.append("|");
                            }
                            selectedCuisine2.append(cuisine);
                        }
                        if (!selectedCuisine2.toString().equals("")) {
                            filterOptions.put("cuisine", selectedCuisine2.toString());
                            binding.taste.setChecked(true);
                        }
                        else {
                            filterOptions.remove("cuisine");
                            binding.taste.setChecked(false);
                        }
                        loadData();
                    }
                });
                builder.show();
                break;
            case R.id.taste:
                final String[] tastes = {
                    "Sweet and Sour",
                    "Sour",
//                  TODO : Take a look in Sweet & Sour AND "SELECT TASTE"
                    "Sweet & Sour",
                    "Sweet",
                    "Tangy",
                    "Mild",
                    "Spicy"};
                //"Select Taste
                List<String> selectedTaste = new ArrayList<>();
                binding.taste.setChecked(false);
                String selectedTasteString = (String) filterOptions.get("taste");
                previousState = new boolean[]{false, false, false, false, false, false, false};
                if (selectedTasteString != null){
                    binding.taste.setChecked(true);
                    List<String> previouslySelected = Arrays.asList(selectedTasteString.split("\\|"));
                    for(int i = 0; i < tastes.length; i++) {
                        previousState[i] = previouslySelected.contains(tastes[i]);
                    }
                    selectedTaste.addAll(previouslySelected);
                }
                builder = new AlertDialog.Builder(this);
                builder.setTitle("Select Taste:");
                builder.setMultiChoiceItems(tastes, previousState,
                        (dialog, which, isChecked) -> {
                            if (isChecked) {
                                selectedTaste.add(tastes[which]);
                            } else selectedTaste.remove(tastes[which]);
                        });

                builder.setPositiveButton("Ok", (dialogInterface, i) -> {
                    StringBuilder selectedTasteString1 = new StringBuilder();
                    for (String taste: selectedTaste){
                        if (selectedTaste.indexOf(taste) != 0) {
                            selectedTasteString1.append("|");
                        }
                        selectedTasteString1.append(taste);
                    }
                    if (!selectedTasteString1.toString().equals("")) {
                        filterOptions.put("taste", selectedTasteString1.toString());
                        binding.taste.setChecked(true);
                    }
                    else {
                        filterOptions.remove("taste");
                        binding.taste.setChecked(false);
                    }
                    loadData();
                });
                builder.show();
                break;
            case R.id.course:
                final String[] courses = {
                        "Desserts",
                        "Beverages",
                        "Rice",
                        "Noodles and Pastas",
                        "Salads",
                        "Main Course Seafood",
                        "Main Course Vegetarian",
                        "Main Course Mutton",
                        "Garnishes",
                        "Main Course Egg",
                        "Main Course Chicken",
                        "Dips",
                        "Raitas",
                        "Mithais",
                        "Snacks and Starters",
                        "Powders",
                        "Pickles, Jams and Chutneys",
                        "Gravies, Sauces and Stocks",
                        "Dals and Kadhis",
                        "Main Course Vegetarian",
                        "Main Course Seafood",
                        "Snacks and Starters",
                        "Pickles, Jams and Chutneys",
                        "Main Course Chicken",
                        "Breads",
                        "Soups",
                        "Rice"
                };

                List<String> selectedCourse = new ArrayList<>();
                binding.course.setChecked(false);
                String selectedCourseString = (String) filterOptions.get("course");
                previousState = new boolean[]{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false};
                if (selectedCourseString != null){
                    binding.course.setChecked(true);
                    List<String> previouslySelected = Arrays.asList(selectedCourseString.split("\\|"));
                    for(int i = 0; i < courses.length; i++) {
                        previousState[i] = previouslySelected.contains(courses[i]);
                    }
                    selectedCourse.addAll(previouslySelected);
                }
                builder = new AlertDialog.Builder(this);
                builder.setTitle("Select Course:");
                builder.setMultiChoiceItems(courses,previousState, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked)
                        {
                            selectedCourse.add(courses[which]);
                        }
                        else
                        {
                            selectedCourse.remove(courses[which]);
                        }
                    }
                });
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StringBuilder selectedCourse2 = new StringBuilder();
                        for (String course : selectedCourse){
                            if (selectedCourse.indexOf(course) != 0){
                                selectedCourse2.append("|");
                            }
                            selectedCourse2.append(course);
                        }
                        if (!selectedCourse2.toString().equals("")) {
                            filterOptions.put("course", selectedCourse2.toString());
                            binding.taste.setChecked(true);
                        }
                        else {
                            filterOptions.remove("course");
                            binding.taste.setChecked(false);
                        }
                        loadData();
                    }
                });
                builder.show();

                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }

    private void loadData() {
        recipeList.clear();
        ended = false;
        myRecipeAdapter.setCards(recipeList);
        filterOptions.put("last_id", 0);
        Call<List<Recipe>> respList = api.getAllRecipes(filterOptions);
        respList.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse( Call<List<Recipe>> call, Response<List<Recipe>> response) {
                handleResponse(response);
            }

            @Override
            public void onFailure(@NonNull Call<List<Recipe>> call,@NonNull Throwable t) {
                binding.offlineView.setVisibility(VISIBLE);
                binding.BtnOfflineFav.setOnClickListener(MainActivity.this);
                Toast.makeText(MainActivity.this, "Request Failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}