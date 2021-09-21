package com.example.foodzilla;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.foodzilla.databinding.ActivitySettingsBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class Settings extends AppCompatActivity implements View.OnClickListener{

    private GoogleSignInClient mGoogleSignInClient;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySettingsBinding binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.getWindow().setEnterTransition(new Slide(Gravity.END));

        sharedPreferences = getSharedPreferences("auth", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");
        if (!email.equals("guest")){
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            if (acct != null) {
                binding.googleUsername.setText(acct.getDisplayName());
                binding.userEmailid.setText(acct.getEmail());
                Glide.with(this).load(acct.getPhotoUrl()).into(binding.googleProfile);
            }
        }
        else {
            binding.loginStatus.setVisibility(View.VISIBLE);
            binding.logoutStatus.setVisibility(View.GONE);
        }
        binding.logoutStatus.setOnClickListener(this);
        binding.loginStatus.setOnClickListener(this);
        binding.cardAbout.setOnClickListener(this);
        binding.cardContactus.setOnClickListener(this);
        binding.cardFav.setOnClickListener(this);
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, task -> {
                    Intent intent = new Intent(Settings.this, SignIn.class);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove("email");
                    editor.apply();
                    Toast.makeText(Settings.this, "Signout successfully", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finishAffinity();
                });
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.logout_status:
                signOut();
                break;
            case R.id.login_status:
                intent.setClass(Settings.this, SignIn.class);
                startActivity(intent);
                break;
            case R.id.card_fav:
                intent.setClass(Settings.this, Exam.class);
                startActivity(intent);
                break;
            case R.id.card_about:
                intent.setClass(Settings.this, About.class);
                startActivity(intent);
                break;
            case R.id.card_contactus:
                // intent.setClass(Settings.this, SignIn.class);
                // Acitivity Contact
                break;
            default:
                // DO NOTHING
                break;
        }
    }
}