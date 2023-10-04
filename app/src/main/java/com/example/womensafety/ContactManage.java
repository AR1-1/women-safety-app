//for managing contact

package com.example.womensafety;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ContactManage  extends AppCompatActivity {

    private EditText edName;
    private EditText edPhnum;
    private Button btnsv, btnsw;
    private ListView listViewConatcts;
    private List<Contacts> contacts;
    private ArrayAdapter<String> contactAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_contacts);

        edName = findViewById(R.id.add_name);
        edPhnum = findViewById(R.id.add_phn);
        btnsv = findViewById(R.id.btn_save);
        btnsw = findViewById(R.id.btn_showcon);
        listViewConatcts = findViewById(R.id.listContact);

        //listing the contacts
        contacts = new ArrayList<>();
        contactAdapter = new ArrayAdapter<>(this, R.layout.data_item_row);
        listViewConatcts.setAdapter(contactAdapter);

        // Set an OnClickListener for the "Show Contacts" button
        btnsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Fetch and display saved contacts
                displaySavedContacts();
            }

            private void displaySavedContacts() {
                ContactDbHelper dbHelper = new ContactDbHelper(ContactManage.this);
                SQLiteDatabase db = dbHelper.getReadableDatabase();

                Cursor cursor = db.query(
                        ContactDbHelper.TABLE_CONTACTS,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                );

                // Create a list to store the retrieved contacts
                List<Contacts> savedContacts = new ArrayList<>();

                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        // Retrieve data from the cursor
                         @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(ContactDbHelper.COLUMN_NAME));
                         @SuppressLint("Range") String phone = cursor.getString(cursor.getColumnIndex(ContactDbHelper.COLUMN_PHONE));
                        Log.d("ContactManage", " Name: " + name + ", Phone: " + phone);

                        // Create a Contacts object and add it to the list
                        savedContacts.add(new Contacts( name, phone));
                    } while (cursor.moveToNext());

                    cursor.close();
                }

                db.close();



            }
        });


        btnsv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edName.getText().toString();
                String phone =edPhnum.getText().toString();

                //checking the fileds
                if(name.isEmpty() || phone.isEmpty()){
                    Toast.makeText(ContactManage.this,"Please fill the empty fields",Toast.LENGTH_SHORT).show();
                return;
                }

                //checking if contact already exists
                if(contactsExists(name)){
                    Toast.makeText(ContactManage.this,"Conatct already exists",Toast.LENGTH_SHORT).show();
                }
                    else{
                        addContactsToDatabase(name,phone);
                    Toast.makeText(ContactManage.this,"Conatct saved",Toast.LENGTH_SHORT).show();

                }
                Contacts contact = new Contacts(name,phone);
                contacts.add(contact);

                contactAdapter.add(contact.getName());
                contactAdapter.notifyDataSetChanged();

                edName.getText().clear();
                edPhnum.getText().clear();

            }

            private void addContactsToDatabase(String name, String phone) {
                //initializing databasehelper
                ContactDbHelper dbHelper = new ContactDbHelper(ContactManage.this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put(ContactDbHelper.COLUMN_NAME, name);
                values.put(ContactDbHelper.COLUMN_PHONE, phone);

                long newRowId = db.insert(ContactDbHelper.TABLE_CONTACTS, null, values);

                db.close();

            }

            private boolean contactsExists(String name) {
           for (Contacts contact: contacts){
               if(contact.getName().equalsIgnoreCase(name)){
                   return true;
               }

           }
                // Check the databaseSQLiteOpenHelper databaseHelper = null;
                ContactDbHelper dbHelper = new ContactDbHelper(ContactManage.this);
                SQLiteDatabase db = dbHelper.getReadableDatabase();

                Cursor cursor = db.query(
                        ContactDbHelper.TABLE_CONTACTS,
                        null,
                        ContactDbHelper.COLUMN_NAME + "=?",
                        new String[]{name},
                        null,
                        null,
                        null
                );

                boolean exists = cursor != null && cursor.getCount() > 0;

                if (cursor != null) {
                    Log.d("ContactManage", "Cursor count: " + cursor.getCount());
                    cursor.close();
                }

                db.close();

                return exists;

            }
        });



    }

}
