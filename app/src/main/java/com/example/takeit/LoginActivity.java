package com.example.takeit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Check if the user has previously logged in.
        if(ParseUser.getCurrentUser() != null)
        {
            // Move to home screen.
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        }

        EditText etUsername = findViewById(R.id.etLogin);
        EditText etPassword = findViewById(R.id.etPassword);
        CardView btnLogin = findViewById(R.id.cardView);
        TextView btnRegister = findViewById(R.id.btnRegister);

        // Log user into the application.
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                // Validating the log in data
                boolean validationError = false;

                StringBuilder validationErrorMessage = new StringBuilder("Please, enter ");
                if (username.isEmpty()) {
                    validationError = true;
                    validationErrorMessage.append("a username");
                }
                if (password.isEmpty()) {
                    if (validationError) {
                        validationErrorMessage.append(" and ");
                    }
                    validationError = true;
                    validationErrorMessage.append("a password");
                }
                validationErrorMessage.append(".");

                if (validationError) {
                    Toast.makeText(LoginActivity.this, validationErrorMessage.toString(), Toast.LENGTH_LONG).show();
                    return;
                }

                // If the fields are valid, login.
                loginUser(username, password);
            }
        });

        // Clicking to register a new account.
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });
    }

    // LOGIN FUNCTION
    private void loginUser (String username, String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with login", e);
                    Toast.makeText(LoginActivity.this,
                            "Invalid Username/Password. Please try again.", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Log them in if not null.
                homeActivity();

            }
        });
    }

    private void homeActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}