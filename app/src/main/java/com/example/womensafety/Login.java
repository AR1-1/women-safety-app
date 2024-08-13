package com.example.womensafety;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    EditText username, password;
    Button submit;
    DbHelperReg db;
    SharedPreferences sharedPreferences;

    //checking whether the user is loggined or not



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        db = new DbHelperReg(this); // Initializing the instance of db helper
        sharedPreferences = getSharedPreferences("userPrefs", MODE_PRIVATE);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        submit = findViewById(R.id.btn_submit);

        TextView clickableText = findViewById(R.id.goreg);
        clickableText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
            }
        });

        if (isUserLoggedIn()) {
            startMainMenu();
            return; // No need to show the login screen
        }

        // Action for login button
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString();
                String pass = password.getText().toString();

                if (user.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(Login.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    boolean checkUserPass = db.checkusername(user);
                    if (checkUserPass) {
                        // Mark the user as logged in using SharedPreferences
                        setLoggedIn(true);

                        Toast.makeText(Login.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                        startMainMenu();
                        // Hide the password characters
                        password.setVisibility(View.INVISIBLE);
                        Toast.makeText(Login.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Login.this, Menu.class);
                        startActivity(intent);
                        finish(); // Closing the login activity
                    }
                else {

                        Toast.makeText(Login.this, "Invalid username or password", Toast.LENGTH_SHORT).show();

                        
                    } 
                }
            }
        });
    }

    private boolean isUserLoggedIn() {
        return sharedPreferences.getBoolean("loggedIn", false);

    }




    private void startMainMenu() {
        Intent intent = new Intent(Login.this, Menu.class);
        startActivity(intent);
        finish(); // Close the login activity

    }

    private void setLoggedIn(boolean loggedIn) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("loggedIn", loggedIn);
        editor.apply();
    }
}
