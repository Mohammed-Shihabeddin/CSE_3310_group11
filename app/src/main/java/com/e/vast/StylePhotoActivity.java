package com.e.vast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class StylePhotoActivity extends AppCompatActivity {

    //Initialize variables
    ImageView image_view;
    Button bUpload;
    Button bCamera;
    TextView Next;
    Uri selectedImage = null;
    Bitmap selectedImageBM = null;
    Bitmap captureImage = null;
    private static final int PICK_IMAGE = 1;
    private static final int TAKE_IMAGE = 2;
    boolean camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_style_photo);

        //Assign Variable
        image_view = findViewById(R.id.image_view);
        bUpload = findViewById(R.id.bUpload);
        bCamera = findViewById(R.id.bCamera);
        Next = findViewById(R.id.Next);

        //When Open Camera button is clicked
        bCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Request Camera Permission
                if(ContextCompat.checkSelfPermission(StylePhotoActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(StylePhotoActivity.this,
                            new String[]{
                                    Manifest.permission.CAMERA
                            }, TAKE_IMAGE);
                } else{
                    //Open camera
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, TAKE_IMAGE);
                }
            }
        });

        //When Upload Image button is clicked
        bUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Request Photo gallery Permission
                if(ContextCompat.checkSelfPermission(StylePhotoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(StylePhotoActivity.this,
                            new String[]{
                                    Manifest.permission.READ_EXTERNAL_STORAGE
                            }, PICK_IMAGE);
                } else{
                    //open photo gallery
                    Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(gallery, PICK_IMAGE);
                }
            }
        });

        //When Next link is clicked
        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Checks that user uploaded an image
                if(captureImage != null || selectedImageBM != null){
                    Intent intent = new Intent(StylePhotoActivity.this, SelectStyleActivity.class);
                    if (camera) {
                        intent.putExtra("contentFilePath", createContentImageFile(captureImage));
                    } else {
                        intent.putExtra("contentFilePath", createContentImageFile(selectedImageBM));
                    }
                    startActivity(intent);
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(StylePhotoActivity.this);
                    builder.setMessage("Select Content Image first")
                            .setNegativeButton("Ok", null)
                            .create()
                            .show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PICK_IMAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //open photo gallery
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE);
            }else{
                Toast.makeText(this, "Permission not granted. Cannot open photo gallery", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == TAKE_IMAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Open camera
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, TAKE_IMAGE);
            }else{
                Toast.makeText(this, "Permission not granted. Cannot open camera", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //This method creates a File for the selected content image and returns the files path
    private String createContentImageFile (Bitmap contentImage){
        File fileDir = getApplicationContext().getFilesDir();
        File contentImageFile = new File (fileDir, "contentImage.jpg");
        OutputStream os;
        try {
            os = new FileOutputStream(contentImageFile);
            contentImage.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }
        return contentImageFile.getAbsolutePath();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_IMAGE){
            //Get capture image
            captureImage = (Bitmap) data.getExtras().get("data");
            //Set capture image to image_view
            image_view.setImageBitmap(captureImage);
            camera = true;
        } else if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null){
            selectedImage = data.getData();

            //converts Uri image to Bitmap
            try {
                selectedImageBM = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            image_view.setImageBitmap(selectedImageBM);
            camera = false;
        }

    }
}
