package com.example.womensafety;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity {
    EditText username, password, age, gmail, address;
    Button Register;

    DbHelperReg db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        username = findViewById(R.id.username);
        age = findViewById(R.id.userage);
        address = findViewById(R.id.useraddr);
        gmail = findViewById(R.id.usergmail);
        password = findViewById(R.id.userpass);
        Register = findViewById(R.id.rg_submit);

        db = new DbHelperReg(this);

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString().trim();
                String ag = age.getText().toString().trim();
                String addr = address.getText().toString().trim();
                String mail = gmail.getText().toString().trim();
                String pass = password.getText().toString();

                if (user.isEmpty() || pass.isEmpty() || ag.isEmpty() || addr.isEmpty() || mail.isEmpty()) {
                    Toast.makeText(Register.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    if (!db.checkusername(user)) {
                        boolean insert = db.insertdata(user, pass, ag, addr, mail);
                        if (insert) {
                            Toast.makeText(Register.this, "Registered successful", Toast.LENGTH_SHORT).show();
                            // Redirect or perform other actions here
                            finish();
                        } else {
                            Toast.makeText(Register.this, "Registration failed", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Register.this, "Username already exists", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
