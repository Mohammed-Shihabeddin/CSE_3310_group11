package com.e.vast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class HomeScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        Intent intent = getIntent();
        String Fname = intent.getStringExtra("Fname");
        String Mname = intent.getStringExtra("Mname");
        String Lname = intent.getStringExtra("Lname");
        String Email = intent.getStringExtra("Email");
        String Password = intent.getStringExtra("Password");
    }
}
//test