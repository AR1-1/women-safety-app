package com.example.womensafety;

import static android.provider.ContactsContract.CommonDataKinds.Phone.*;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Emergency extends Activity {

    private static final int PICK_CONTACT_REQUEST = 1;
    private static final int CONTEXT_MENU_ADD_TO_EMERGENCY = 101;

    private TextView emergencyContactTextView;
    private Button selectContactButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emergencycontact);

        emergencyContactTextView = findViewById(R.id.tvSelectedContact);
        selectContactButton = findViewById(R.id.btnSelectContact);

        selectContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check and request permissions if necessary
                // ...

                // Start the contact picker activity
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT_REQUEST);
            }
        });

        // Register the TextView for the context menu
        registerForContextMenu(emergencyContactTextView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_CONTACT_REQUEST && resultCode == RESULT_OK) {
            // Get the selected contact's information
            String contactName = "";
            String contactPhoneNumber = "";

            // Extract contact data from the returned Intent
            Uri contactUri = data.getData();
            if (contactUri != null) {
                Cursor cursor = getContentResolver().query(contactUri, null, null, null, null);
                if (cursor != null) {
                    try {
                        if (cursor.moveToFirst()) {
                            contactName = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));

                            // Retrieve the contact's phone number
                            contactPhoneNumber = retrieveContactPhoneNumber(cursor);
                        }
                    } finally {
                        cursor.close();
                    }
                }
            }

            // Store the selected contact as an emergency contact
            saveEmergencyContact(contactName, contactPhoneNumber);

            // Display the emergency contact
            emergencyContactTextView.append("\n\nEmergency Contact:\nName: " + contactName + "\nPhone: " + contactPhoneNumber);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_add_to_emergency_contacts) {
            // Retrieve the selected contact details from the TextView
            String selectedContactDetails = emergencyContactTextView.getText().toString();

            // Extract the contact name and phone number (modify as per your text format)
            String contactName = extractContactName(selectedContactDetails);
            String contactPhoneNumber = extractContactPhoneNumber(selectedContactDetails);

            // Add the contact to the emergency list
            saveEmergencyContact(contactName, contactPhoneNumber);

            // Display a message or perform any other action
            Toast.makeText(this, "Contact added to Emergency Contacts", Toast.LENGTH_SHORT).show();

            return true;
        }
        return super.onContextItemSelected(item);
    }

    private String retrieveContactPhoneNumber(Cursor cursor) {
        String contactPhoneNumber = "";

        try {
            String contactId = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

            if (contactId != null && !contactId.isEmpty()) {
                Cursor phoneCursor = getContentResolver().query(
                        CONTENT_URI,
                        null,
                        CONTACT_ID + " = ?",
                        new String[]{contactId},
                        null
                );

                if (phoneCursor != null) {
                    try {
                        if (phoneCursor.moveToFirst())
                            contactPhoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                    } finally {
                        phoneCursor.close();
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace(); // Log the exception for debugging
        }

        return contactPhoneNumber;
    }

    private String extractContactName(String contactDetails) {
        // Logic to extract the contact name from the details
        // Modify this according to your text format
        return "SampleName";
    }

    private String extractContactPhoneNumber(String contactDetails) {
        // Logic to extract the contact phone number from the details
        // Modify this according to your text format
        return "SamplePhoneNumber";
    }

    private void saveEmergencyContact(String name, String phoneNumber) {
        // Use SharedPreferences for simplicity (you might want to use a database for a more robust solution)
        SharedPreferences.Editor editor = getSharedPreferences("EmergencyContacts", MODE_PRIVATE).edit();
        editor.putString("Name", name);
        editor.putString("PhoneNumber", phoneNumber);
        editor.apply();
    }
}
