//for representing contact

package com.example.womensafety;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.LocationAvailability;

public class Contacts  {

    int id;
    String name;
    String phone_number;



//    creating constructor(overriding)

    public Contacts(int id,String name, String phone_number){
        this.id= id;
        this.name= name;
        this.phone_number=phone_number;
    }

    public Contacts(String name, String phone_number){
        this.name=name;
        this.phone_number= phone_number;
    }

    public Contacts(String contact1_, String s, double v, double v1) {
    }

    //    method for setting contact id
    public void SetId(int id){
        this.id=id;
    }

//    method for getting contact
    public int getId(){
        return id;
    }

//    method for setting  contact name

    public void SetName(String name){
        this.name=name;
    }

    //method for getting contact name

    public String getName(){
        return name;
    }

    //methods for  getting  contact phonenumber

    public void SetPhone_number(String phone_number){
        this.phone_number=phone_number;
    }

    public String getPhone_number(){
        return phone_number;
    }


    public String getPhoneNumber() {
        return phone_number;
    }

    public Location getLocation() {

        return null;
    }
}
