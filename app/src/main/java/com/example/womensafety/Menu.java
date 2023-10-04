package com.example.womensafety;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Menu extends AppCompatActivity {
DrawerLayout drawerLayout;
ImageView menu;
LinearLayout contacts,location,logout;
FloatingActionButton fab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        drawerLayout = findViewById(R.id.drawer);
        menu = findViewById(R.id.menu);
        contacts = findViewById(R.id.contact);
        location = findViewById(R.id.Location);

       FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show the contact input dialog
                showContactInputDialog();
            }
        });


        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawerLayout);
            }
        });

        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent intent = new Intent(Menu.this,ContactManage.class);
                startActivity(intent);
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(Menu.this,Location.class);
            }
        });

      /*  logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Menu.this,"Logout",Toast.LENGTH_SHORT).show();
                redirectActivity(Menu.this,Login.class);
            }
        });*/



    }

    private void showContactInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_contact_input, null);
        builder.setView(dialogView);

        final EditText edName = dialogView.findViewById(R.id.edtName);
        final EditText edPhnum = dialogView.findViewById(R.id.edtPhoneNumber);
        Button btnSave = dialogView.findViewById(R.id.btnSaveContact);

        final AlertDialog dialog = builder.create();



        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edName.getText().toString();
                String phone = edPhnum.getText().toString();

                if (name.isEmpty() || phone.isEmpty()) {
                    Toast.makeText(Menu.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Add the contact to the database
                    addContactToDatabase(name, phone);
                    Toast.makeText(Menu.this, "Contact saved", Toast.LENGTH_SHORT).show();
                    dialog.dismiss(); // Close the dialog after saving
                }
            }
        });

        dialog.show();
    }

    private void addContactToDatabase(String name, String phone) {
        ContactDbHelper dbHelper = new ContactDbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ContactDbHelper.COLUMN_NAME, name);
        values.put(ContactDbHelper.COLUMN_PHONE, phone);

        long newRowId = db.insert(ContactDbHelper.TABLE_CONTACTS, null, values);

        db.close();
    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }
    public static void closeDrawer(DrawerLayout drawerLayout) {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);

        }
    }
    public static void redirectActivity(Activity activity ,Class secondActivity){
        Intent intent = new Intent(activity,secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }
}

