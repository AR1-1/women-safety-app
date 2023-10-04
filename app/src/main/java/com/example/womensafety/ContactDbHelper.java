package com.example.womensafety;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ContactDbHelper extends SQLiteOpenHelper {
    private static final  String DATABASE_NAME ="contacts_db";
    private static  final int DATABASE_VERSION =2;
    public static final String TABLE_CONTACTS ="contacts";
    public static final String COLUMN_NAME ="name";
    public static final String COLUMN_Id ="id";
    public static final String COLUMN_PHONE ="phone";



public ContactDbHelper(Context context){
    super(context,DATABASE_NAME,null,DATABASE_VERSION);

}

    @Override
    public void onCreate(SQLiteDatabase db) {
      String createTable ="CREATE TABLE " + TABLE_CONTACTS + "(id INTEGER PRIMARY KEY, name TEXT ,phone TEXT)";
      db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_CONTACTS);
        onCreate(db);
    }
    public void insertData(int id,String name,String Phone){
    SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id",id);
        contentValues.put("name",name);
        contentValues.put("phone",Phone);

        db.insert("contacts",null,contentValues);
        db.close();

    }
    public Cursor selectData(){
    SQLiteDatabase db = this.getReadableDatabase();
    String query =" SELECT *FROM contacts";
    Cursor cursor = db.rawQuery(query,null);
    return cursor;
    }

    public void updateData(String id ,String name,String phone){
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put(COLUMN_NAME,name);
    contentValues.put(COLUMN_PHONE,phone);
    contentValues.put(COLUMN_Id,id);

    //row update
        String whereClause = COLUMN_Id + "=?";
        String[] whereArgs ={id};
        db.update(TABLE_CONTACTS,contentValues,whereClause,whereArgs);
        db.close();

    }
}
