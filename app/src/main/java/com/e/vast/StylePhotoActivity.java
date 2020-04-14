package com.e.vast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class StylePhotoActivity extends AppCompatActivity {

    //Initialize variables
    ImageView image_view;
    Button bUpload;
    Button bCamera;
    Uri selectedImage;
    private static final int PICK_IMAGE = 1;
    private static final int TAKE_IMAGE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_style_photo);

        //Assign Variable
        image_view = findViewById(R.id.image_view);
        bUpload = findViewById(R.id.bUpload);
        bCamera = findViewById(R.id.bCamera);

        //Request Camera Permission
        if(ContextCompat.checkSelfPermission(StylePhotoActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(StylePhotoActivity.this,
                    new String[]{
                            Manifest.permission.CAMERA
                    }, 100);
        }

        //When Open Camera button is clicked
        bCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open camera
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, TAKE_IMAGE);
            }
        });

        bUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_IMAGE){
            //Get capture image
            Bitmap captureImage = (Bitmap) data.getExtras().get("data");
            //Set capture image to image_view
            image_view.setImageBitmap(captureImage);
        } else if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null){
            selectedImage = data.getData();
            image_view.setImageURI(selectedImage);
        }

    }
}
