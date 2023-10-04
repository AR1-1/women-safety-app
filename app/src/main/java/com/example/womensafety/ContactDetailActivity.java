package com.example.womensafety;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ContactDetailActivity extends AppCompatActivity {
    private TextView tvContactName, tvContactPhone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_details);

        tvContactName = findViewById(R.id.tvContactName);
        tvContactPhone = findViewById(R.id.tvContactPhone);

        //retriving contact details
        String contactName ="Anisha Rijal";
        String contactPhone ="9855040244";

        //setting contact details in the txtview
        tvContactName.setText(contactName);
        tvContactPhone.setText(contactPhone);


        //passing data to activity
        Intent intent = new Intent(this, ContactDetailActivity.class);
        intent.putExtra("contactName", contactName);
        intent.putExtra("contactPhone", contactPhone);
        startActivity(intent);

    }
}
