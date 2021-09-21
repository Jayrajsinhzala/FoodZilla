package com.example.foodzilla;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodzilla.databinding.ActivityAboutBinding;

public class About extends AppCompatActivity implements View.OnClickListener {
    private int salman;
    private int nikhil;
    private int vishwa;
    private int rishi;
    private int jayraj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ActivityAboutBinding binding = ActivityAboutBinding.inflate(getLayoutInflater());

        binding.aboutSalman.setOnClickListener(this);
        binding.aboutNikhil.setOnClickListener(this);
        binding.aboutVishwa.setOnClickListener(this);
        binding.aboutRishi.setOnClickListener(this);
        binding.aboutJayraj.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.aboutSalman:
                salman++;
                if (salman == 3) {
                    Toast.makeText(this, "Gracias", Toast.LENGTH_SHORT).show();
                }
                else if (salman == 5) {
                    Toast.makeText(this, "WTH STOP TAPPING", Toast.LENGTH_SHORT).show();
                }
                else if (salman >= 7) {
                    String url = "https://www.youtube.com/watch?v=4CI3lhyNKfo";
                    Uri uri = Uri.parse(url);
                    startActivity(new Intent(Intent.ACTION_VIEW, uri));
                }
                break;
            case R.id.aboutNikhil:
                nikhil++;
                if (nikhil == 3) {
                    Toast.makeText(this, "Sup Niggah", Toast.LENGTH_SHORT).show();
                }
                else if (nikhil == 5) {
                    Toast.makeText(this, "Stop Niggah", Toast.LENGTH_SHORT).show();
                }
                else if (nikhil >= 7) {
                    String url = "https://www.allrecipes.com/recipes/931/healthy-recipes/sugar-free/";
                    Uri uri = Uri.parse(url);
                    startActivity(new Intent(Intent.ACTION_VIEW, uri));
                }
                break;
            case R.id.aboutVishwa:
                vishwa++;
                if (vishwa == 3) {
                    Toast.makeText(this, "Haan, sure!", Toast.LENGTH_SHORT).show();
                }
                else if (vishwa == 5) {
                    Toast.makeText(this, "Shut upppp", Toast.LENGTH_SHORT).show();
                }
                else if (vishwa >= 7) {
                    String url = "https://en.wikipedia.org/wiki/Aloo";
                    Uri uri = Uri.parse(url);
                    startActivity(new Intent(Intent.ACTION_VIEW, uri));
                }
                break;
            case R.id.aboutRishi:
                rishi++;
                if (rishi == 3) {
                    Toast.makeText(this, "Haan, sure!", Toast.LENGTH_SHORT).show();
                }
                else if (rishi == 5) {
                    Toast.makeText(this, "Shut upppp", Toast.LENGTH_SHORT).show();
                }
                else if (rishi >= 7) {
                    String url = "https://en.wikipedia.org/wiki/Aloo";
                    Uri uri = Uri.parse(url);
                    startActivity(new Intent(Intent.ACTION_VIEW, uri));
                }
                break;
            case R.id.aboutJayraj:
                jayraj++;
                if (jayraj == 3) {
                    Toast.makeText(this, "I have to go to market", Toast.LENGTH_SHORT).show();
                }
                else if (jayraj == 5) {
                    Toast.makeText(this, "I'm in market", Toast.LENGTH_SHORT).show();
                }
                else if (jayraj >= 7) {
                    String url = "https://www.youtube.com/watch?v=i_s_iosCZfg";
                    Uri uri = Uri.parse(url);
                    startActivity(new Intent(Intent.ACTION_VIEW, uri));
                }
                break;
        }

    }
}