package com.example.womensafety;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContactListActivity  extends AppCompatActivity {
        private RecyclerView recyclerViewContacts;

        @SuppressLint("MissingInflatedId")
        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
                System.out.println("contact listt activiety on create");
                super.onCreate(savedInstanceState);
                setContentView(R.layout.contactist);

                //intializing recycle

                recyclerViewContacts = findViewById(R.id.recyclerViewContacts);
                recyclerViewContacts.setLayoutManager(new LinearLayoutManager(this));
                System.out.println("contact click listener");
                //connecting adapter
                ArrayList<Contacts> contactlist = null;
                ContactClickListener listener = null;
                ContactAdapter adapter = new ContactAdapter(this, null, null);
                recyclerViewContacts.setAdapter(adapter);

        }
}
