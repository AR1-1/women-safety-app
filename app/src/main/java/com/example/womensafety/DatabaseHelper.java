package com.example.womensafety;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database details
    private static final String DATABASE_NAME = "contacts_database";
    private static final int DATABASE_VERSION = 1;

    // Table and column names
    public static final String TABLE_CONTACTS = "contacts";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
  public static final String COLUMN_PHONE_NUMBER = "phone_number";

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create table query
    private static final String CREATE_TABLE_CONTACTS =
            "CREATE TABLE " + TABLE_CONTACTS + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_NAME + " TEXT," +
                    COLUMN_PHONE_NUMBER + " TEXT)";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CONTACTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(db);
    }

    // Insert a contact
    public long insertContact(Contacts contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, contact.getName());
        values.put(COLUMN_PHONE_NUMBER, contact.getPhone_number());
        long id = db.insert(TABLE_CONTACTS, null, values);
        db.close();
        return id;
    }

    // Get all emergency contacts
    public List<Contacts> getEmergencyContacts() {
        List<Contacts> contacts = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query to retrieve all contacts
        Cursor cursor = db.query(
                TABLE_CONTACTS,
                new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_PHONE_NUMBER},
                null,
                null,
                null,
                null,
                null);

        // Iterate through the cursor and add contacts to the list
        if (cursor != null && cursor.getCount() >0) {

            cursor.moveToFirst();
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
                String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE_NUMBER));

                Contacts contact = new Contacts(id, name, phoneNumber);
                contacts.add(contact);
            } while (cursor.moveToNext());

            // Close the cursor
            cursor.close();
        }

        // Close the database
        db.close();

        return contacts;
    }


    public List<Contacts> getAllContacts() {
        List<Contacts> contacts = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query to retrieve all contacts
        Cursor cursor = db.query(
                TABLE_CONTACTS,
                new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_PHONE_NUMBER},
                null,
                null,
                null,
                null,
                null);

        // Iterate through the cursor and add contacts to the list
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
                String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE_NUMBER));
                Contacts contact = new Contacts(id, name, phoneNumber);
                contacts.add(contact);
            } while (cursor.moveToNext());

            // Close the cursor
            cursor.close();
        }

        // Close the database
        db.close();

        return contacts;
    }

}
