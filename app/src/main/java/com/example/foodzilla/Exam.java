package com.example.foodzilla;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.foodzilla.databinding.ActivityExamBinding;
import com.example.foodzilla.databinding.ActivityMainBinding;

import java.util.List;

public class Exam extends AppCompatActivity implements View.OnClickListener{
    private ActivityExamBinding binding;
    List<Recipe> ratings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityExamBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        binding.btnSearch.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        int min = (int) binding.ratingBarMin.getRating();
        int max = binding.ratingBarMax.getNumStars();

        Toast.makeText(this, ""+ min, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,Favourite.class);
        intent.putExtra("Min",min);
        intent.putExtra("Max", max);
        startActivity(intent);
    }
}