package com.example.womensafety;

import androidx.appcompat.app.AppCompatActivity;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

    public class Emergency extends Activity {

        private static final int PICK_CONTACT_REQUEST = 1;
        private TextView emergencyContactTextView;
        private Button selectContactButton;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.emergencycontact);

            emergencyContactTextView = findViewById(R.id.emergencyContactTextView);
            selectContactButton = findViewById(R.id. selectContactButton);

            selectContactButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Start the contact picker activity
                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(intent, PICK_CONTACT_REQUEST);
                }
            });
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == PICK_CONTACT_REQUEST && resultCode == RESULT_OK) {
                // Get the selected contact's information
                String contactName = "";
                String contactPhoneNumber = "";

                // Extract contact data from the returned Intent
                // You may need to handle multiple data types depending on the selected contact
                // Here, we assume that the user selected a contact with phone data
                Bundle extras = data.getExtras();
                if (extras != null) {
                    String contactId = extras.getString(ContactsContract.Contacts._ID);
                    contactName = extras.getString(ContactsContract.Contacts.DISPLAY_NAME);

                    // Retrieve the contact's phone number
                    contactPhoneNumber = retrieveContactPhoneNumber(contactId);
                }

                // Store the selected contact as an emergency contact
                // You can save it in a local database or SharedPreferences
                saveEmergencyContact(contactName, contactPhoneNumber);

                // Display the emergency contact
                emergencyContactTextView.setText("Emergency Contact:\nName: " + contactName + "\nPhone: " + contactPhoneNumber);
            }
        }

        private String retrieveContactPhoneNumber(String contactId) {

            return "+1234567890";
        }

        private void saveEmergencyContact(String name, String phoneNumber) {

        }
    }


