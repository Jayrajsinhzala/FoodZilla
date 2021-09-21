package com.example.foodzilla;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.foodzilla.databinding.ActivityFavouriteBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class Favourite extends AppCompatActivity implements View.OnClickListener {
    boolean flag = true;
    RecipeDao dao;
    RecipeAdapter myRecipeAdapter;
    List<Recipe> recipeList;
    ActivityFavouriteBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavouriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        myRecipeAdapter = new RecipeAdapter(Favourite.this);

        binding.recyclerFav.setAdapter(myRecipeAdapter);


        Intent intent = getIntent();
        int min = intent.getIntExtra("Min",0);
        int max = intent.getIntExtra("Max",0);

        Toast.makeText(this, ""+min, Toast.LENGTH_SHORT).show();

        RecipeDatabase db = Room.databaseBuilder(getApplicationContext(), RecipeDatabase.class, "database-name").allowMainThreadQueries().build();
        dao = db.recipeDao();


        recipeList = dao.getRange(min,max);
        myRecipeAdapter.setCards(recipeList);
        binding.btnDelateAll.setOnClickListener(this);
        if (recipeList.size() == 0) {
            binding.btnDelateAll.setVisibility(View.INVISIBLE);
        }
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
                super.onSelectedChanged(viewHolder, actionState);
                if (actionState == ItemTouchHelper.ACTION_STATE_IDLE) {
                    Toast.makeText(Favourite.this, "Swipe to remove from favourite", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getLayoutPosition();
                int id = viewHolder.itemView.getId();
                Recipe recipe = recipeList.remove(position);
                myRecipeAdapter.setCards(recipeList);
                myRecipeAdapter.notifyItemRemoved(position);
                setFlag(true);
                if (recipeList.size() == 0) {
                    binding.btnDelateAll.setVisibility(View.INVISIBLE);
                }
                Snackbar snackbar = Snackbar.make(binding.getRoot(), "Removed from Favourite", Snackbar.LENGTH_LONG);
                snackbar.setAction("Undo", view -> {
                    int pos = position;
                    if (position == recipeList.size()) {
                        pos = recipeList.size();
                        recipeList.add(recipe);
                    } else {
                        recipeList.add(pos, recipe);
                    }
                    if (recipeList.size() != 0) {
                        binding.btnDelateAll.setVisibility(View.VISIBLE);
                    }
                    myRecipeAdapter.setCards(recipeList);
                    myRecipeAdapter.notifyItemInserted(pos);
                    setFlag(false);
                });
                snackbar.addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);
                        if (flag) {
                            dao.delete(recipe);
                        }
                    }
                });
                snackbar.setActionTextColor(Color.BLUE);
                snackbar.show();
            }
        });
        itemTouchHelper.attachToRecyclerView(binding.recyclerFav);
    }






    void setFlag(boolean flag) {
        this.flag = flag;
    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure? All saved favourites will be removed.");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            myRecipeAdapter.setCards(new ArrayList<>());
            myRecipeAdapter.notifyItemRangeRemoved(0, recipeList.size());
            binding.btnDelateAll.setVisibility(View.INVISIBLE);
            dao.deleteAll();
        });
        builder.setNegativeButton("No", (dialog, which) -> { });
        AlertDialog alertDialog = builder.create();
        alertDialog.setTitle("Confirmation");
        alertDialog.show();
    }
}