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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ContactManage  extends AppCompatActivity {

    private EditText edName;
    private EditText edPhnum;
    private Button btnsv, btnsw;
    private ListView listViewContacts;
    private List<Contacts> contacts;
    private ArrayAdapter<String> contactAdapter;

    public static List<String> getEmergencyContacts() {

        return null;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_contacts);

        edName = findViewById(R.id.add_name);
        edPhnum = findViewById(R.id.add_phn);
        btnsv = findViewById(R.id.btn_save);
        btnsw = findViewById(R.id.btn_showcon);
        listViewContacts = findViewById(R.id.listContact);

        //listing the contacts
        contacts = new ArrayList<>();
        contactAdapter = new ArrayAdapter<>(this, R.layout.data_item_row);
        listViewContacts.setAdapter(contactAdapter);

        // Set an OnClickListener for the "Show Contacts" button
        TableLayout tableLayout = new TableLayout(this);
        tableLayout.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.MATCH_PARENT
        ));
        btnsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Fetch and display saved contacts
                displaySavedContacts();
            }

            private void displaySavedContacts() {
                System.out.println("display saved contacts called");
                DatabaseHelper dbHelper = new DatabaseHelper(ContactManage.this);
                SQLiteDatabase db = dbHelper.getReadableDatabase();

                Cursor cursor = db.query(
                        DatabaseHelper.TABLE_CONTACTS,
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
                         @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
                         @SuppressLint("Range") String phone = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PHONE_NUMBER));
                        Log.d("ContactManage", " Name: " + name + ", Phone: " + phone);
                        System.out.println(name);
                        System.out.println(phone);
                        System.out.println("-----");
                        edName.setText(name);
                        // Create a Contacts object and add it to the list
                        savedContacts.add(new Contacts( name, phone));
                    } while (cursor.moveToNext());

                    cursor.close();
                }

                //listing data
                String[][] data = new String[savedContacts.size()][2];
                for(int i =0; i<savedContacts.size();i++){
                    data[i][0]=savedContacts.get(i).getName();
                    data[i][1]=savedContacts.get(i).getPhoneNumber();
                    System.out.println("getting");
                    System.out.println(data[i][0]);
                    System.out.println(data[i][1]);
                }


                // Iterate through the data and create rows and columns
                for (String[] row : data) {
                    TableRow tableRow = new TableRow(ContactManage.this);

                    for (String cell : row) {
                        TextView textView = new TextView(ContactManage.this);
                        textView.setText(cell);
                        textView.setPadding(16, 16, 16, 16);
                        tableRow.addView(textView);
                    }

                    tableLayout.addView(tableRow);
                }

                // Set the TableLayout as the content view
                setContentView(tableLayout);
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
                System.out.println("add contact to database 2");
                DatabaseHelper dbHelper = new DatabaseHelper(ContactManage.this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put(DatabaseHelper.COLUMN_NAME, name);
                values.put(DatabaseHelper.COLUMN_PHONE_NUMBER, phone);

                long newRowId = db.insert(DatabaseHelper.TABLE_CONTACTS, null, values);

                db.close();

            }

            private boolean contactsExists(String name) {
           for (Contacts contact: contacts){
               if(contact.getName().equalsIgnoreCase(name)){
                   return true;
               }

           }
                // Check the databaseSQLiteOpenHelper databaseHelper = null;
                DatabaseHelper dbHelper = new DatabaseHelper(ContactManage.this);
                SQLiteDatabase db = dbHelper.getReadableDatabase();

                Cursor cursor = db.query(
                        DatabaseHelper.TABLE_CONTACTS,
                        null,
                        DatabaseHelper.COLUMN_NAME + "=?",
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
