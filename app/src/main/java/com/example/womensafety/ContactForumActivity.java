package com.example.womensafety;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

    public class ContactForumActivity extends AppCompatActivity {

        private EditText etContactName;
        private EditText etContactPhone;
        private Button btnSaveContact;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.contactforum);

            // Initialize views
            etContactName = findViewById(R.id.etContactName);
            etContactPhone = findViewById(R.id.etContactPhone);
            btnSaveContact = findViewById(R.id.btnSaveContact);

            btnSaveContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveContact(); // Implement the saveContact method
                }
            });
        }

        private void saveContact() {
            String contactName = etContactName.getText().toString();
            String contactPhone = etContactPhone.getText().toString();

            // Implement code to save the contact to the database (or other storage)

            // Optionally, you can navigate back to the Contact List Activity after saving
            finish();
        }
    }


