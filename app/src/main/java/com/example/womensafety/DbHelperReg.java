package com.example.womensafety;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelperReg extends SQLiteOpenHelper {

    public static final String Database_Name = "Women";

    public DbHelperReg(Context context) {
        super(context, "Women", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table users(username text primary key, age text, address text, gmail text, password text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists users");
        onCreate(db);
    }

    public boolean insertdata(String username, String password, String age, String gmail, String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("age", age);
        contentValues.put("gmail", gmail);
        contentValues.put("address", address);
        contentValues.put("password", password);

    /*    long result = db.insert("users", null, contentValues);
        db.close(); // Close the database connection

        return result != -1;*/
        db.beginTransaction();
        try {
            long result = db.insert("users", null, contentValues);
            db.setTransactionSuccessful();
            return result != -1;
        } catch (Exception e) {
            // Handle any exceptions here
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close(); // Close the database connection
        }
        return false;
    }



    public boolean checkusername(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from users where username=?", new String[]{username});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }
}
