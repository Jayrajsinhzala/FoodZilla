package com.example.foodzilla;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.MyRecipeViewHolder> implements View.OnClickListener {
    private List<Recipe> recipeList = new ArrayList<>();
    private final Context context;

    public RecipeAdapter(Context context) {
        this.context = context;
    }
    @NonNull
    @Override
    public MyRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_card, parent, false);
        return new MyRecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecipeViewHolder holder, int position) {
        holder.textName.setText(recipeList.get(position).getName());
        String isVeg;
        if (recipeList.get(position).getIsVeg()) {
            isVeg = "Veg";
        }
        else {
            isVeg = "Non-Veg";
        }
        holder.textIsVeg.setText(isVeg);
        holder.textId.setText(String.valueOf(recipeList.get(position).getId()));
        if (context.getClass().equals(Favourite.class)) {
            File file = new File(context.getFilesDir(), recipeList.get(position).getImage());
            byte[] bytes = new byte[(int) file.length()];
            try {
                context.openFileInput(recipeList.get(position).getImage()).read(bytes);
                Glide.with(context).load(bytes).into(holder.imageView);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            Glide.with(context).load(recipeList.get(position).getImage()).centerCrop().into(holder.imageView);
        }
        holder.itemView.setId(recipeList.get(position).getId());
        holder.itemView.setOnClickListener(RecipeAdapter.this);
    }


    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public void setCards(List<Recipe> recipeList) {
        this.recipeList = recipeList;
        if (this.context.getClass().equals(MainActivity.class)){
            notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        MyRecipeViewHolder holder = new MyRecipeViewHolder(v);
//        ScrollingActivity
//        =================
        Intent intent = new Intent(context, ScrollingActivity.class);
//        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra("id", Integer.parseInt(holder.textId.getText().toString()));
        intent.putExtra("title", holder.textName.getText());
        if (this.context.getClass().equals(Favourite.class)) {
            intent.putExtra("isOffline", true);
        }
        holder.imageView.buildDrawingCache(true);
        Bitmap bmp = holder.imageView.getDrawingCache(true);
        intent.putExtra("image", bmp);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context, holder.imageView, "imageRecipe");
        context.startActivity(intent, options.toBundle());
    }

    public static class MyRecipeViewHolder extends RecyclerView.ViewHolder{
        public final TextView textName;
        public final TextView textId;
        public final TextView textIsVeg;
        public final ImageView imageView;

        public MyRecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageRecipeThumb);
            textId = itemView.findViewById(R.id.textId);
            textName = itemView.findViewById(R.id.textRecipeTitle);
            textIsVeg = itemView.findViewById(R.id.textIsVeg);
        }
    }

}
