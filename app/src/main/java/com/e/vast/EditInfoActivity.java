package com.e.vast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class EditInfoActivity extends AppCompatActivity {

    EditText etFName;
    EditText etMName;
    EditText etLName;
    EditText etPassword;
    Button bSave;
    String Email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);

        etFName = (EditText) findViewById(R.id.etFName);
        etMName = (EditText) findViewById(R.id.etMName);
        etLName = (EditText) findViewById(R.id.etLName);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bSave = (Button) findViewById(R.id.bSave);

        Intent getintent = getIntent();
        Email = getintent.getStringExtra("Email");

        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String FName2 = etFName.getText().toString();
                final String MName2 = etMName.getText().toString();
                final String LName2 = etLName.getText().toString();
                final String Password2 = etPassword.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success){
                                Intent intent = new Intent(EditInfoActivity.this, HomeScreenActivity.class);
                                EditInfoActivity.this.startActivity(intent);
                            }else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(EditInfoActivity.this);
                                builder.setMessage("Changes Failed")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                EditInfoRequest editInfoRequest = new EditInfoRequest(FName2, MName2, LName2, Email, Password2, responseListener);
                RequestQueue queue = Volley.newRequestQueue(EditInfoActivity.this);
                queue.add(editInfoRequest);
            }
        });

    }

   public void onResume(){
        super.onResume();
        Log.d("Email", Email);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success){
                        String Fname = jsonResponse.getString("Fname");
                        String Mname = jsonResponse.getString("Mname");
                        String Lname = jsonResponse.getString("Lname");
                        String Password = jsonResponse.getString("Password");

                        etFName.setText(Fname);
                        etMName.setText(Mname);
                        etLName.setText(Lname);
                        etPassword.setText(Password);

                    } else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditInfoActivity.this);
                        builder.setMessage("Get info Failed")
                               .setNegativeButton("Retry", null)
                               .create()
                               .show();
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        };

        GetInfo getInfo = new GetInfo(Email, responseListener);
        RequestQueue queue2 = Volley.newRequestQueue(EditInfoActivity.this);
        queue2.add(getInfo);
    }


}
