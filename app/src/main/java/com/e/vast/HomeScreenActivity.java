package com.e.vast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        Intent intent = getIntent();
        final String Fname = intent.getStringExtra("Fname");
        final String Mname = intent.getStringExtra("Mname");
        final String Lname = intent.getStringExtra("Lname");
        final String Email = intent.getStringExtra("Email");
        final String Password = intent.getStringExtra("Password");

        final Button bEditInfo = (Button) findViewById(R.id.bEditInfo);
        final Button bStylePhoto = (Button) findViewById(R.id.bStylePhoto);
        final Button bLogOut = (Button) findViewById(R.id.bLogOut);

        //when Edit Info button is clicked
        bEditInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreenActivity.this, EditInfoActivity.class);
                intent.putExtra("FName", Fname);
                intent.putExtra("MName", Mname);
                intent.putExtra("LName", Lname);
                intent.putExtra("Email", Email);
                intent.putExtra("Password", Password);

                HomeScreenActivity.this.startActivity(intent);
            }
        });

        //when Style Photo button is clicked
        bStylePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeScreenActivity.this.startActivity(new Intent(HomeScreenActivity.this, StylePhotoActivity.class));

            }
        });

        //when Log out button is clicked
        bLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeScreenActivity.this.startActivity(new Intent(HomeScreenActivity.this, MainActivity.class));
            }
        });
    }
}