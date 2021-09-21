package com.example.foodzilla;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodzilla.databinding.ActivitySignInBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class SignIn extends AppCompatActivity implements View.OnClickListener {
    private static final int RC_SIGN_IN = 0;
    private GoogleSignInClient mGoogleSignInClient;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySignInBinding binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sharedPreferences = getSharedPreferences("auth", MODE_PRIVATE);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        binding.sBtn.setOnClickListener(this);
        binding.btnSkip.setOnClickListener(this);
        binding.sBtn.setSize(SignInButton.SIZE_WIDE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.s_btn:
                signIn();
                break;
            case R.id.btnSkip:
                skipSignIn();
        }
    }

    private void skipSignIn() {
        Intent intent = new Intent(this, MainActivity.class);
        editor = sharedPreferences.edit();
        editor.putString("email", "guest");
        editor.apply();
        startActivity(intent);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount acct = task.getResult(ApiException.class);
            Intent dest = new Intent(this, MainActivity.class);
            if (acct != null) {
                editor = sharedPreferences.edit();
                editor.putString("email", acct.getEmail());
                editor.commit();
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                startActivity(dest);
            }
        } catch (ApiException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }
}