package com.ice.bunchbead.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    // Firebase variable
    private FirebaseAuth mAuth;

    // View variable
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private View loginProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Init Layout
        setContentView(R.layout.activity_login);

        // Init View variable
        emailEditText = findViewById(R.id.inputLoginEmail);
        passwordEditText = findViewById(R.id.inputLoginPassword);
        loginButton = findViewById(R.id.loginButton);
        loginProgress = findViewById(R.id.loginProgress);

        // Listen button click
        loginButton.setOnClickListener(v -> processLogin());

        // Init Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    private void processLogin() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        if (email.isEmpty() || password.isEmpty()) {
            // Send Error
            Toast.makeText(this, "Email dan password tidak boleh kosong", Toast.LENGTH_SHORT).show();
        } else {
            emailEditText.setEnabled(false);
            passwordEditText.setEnabled(false);
            loginButton.setVisibility(View.GONE);
            loginProgress.setVisibility(View.VISIBLE);
            Context context = this;
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            successLogin();
                        } else {
                            // Send Error
                            emailEditText.setEnabled(true);
                            passwordEditText.setEnabled(true);
                            loginButton.setVisibility(View.VISIBLE);
                            loginProgress.setVisibility(View.GONE);
                            Toast.makeText(context, "Login gagal, periksa email dan password anda", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void successLogin() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
