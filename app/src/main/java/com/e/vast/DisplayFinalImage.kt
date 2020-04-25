package com.e.vast

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import kotlinx.coroutines.asCoroutineDispatcher
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.lang.Exception
import java.util.concurrent.Executors

class DisplayFinalImage : AppCompatActivity() {

    private lateinit var resultImageView: ImageView
    private lateinit var viewModel: MLExecutionViewModel
    private var selectedStyle: String = ""
    private var lastSavedFile = ""
    private lateinit var styleTransferModelExecutor: StyleTransferModelExecutor
    private val inferenceThread = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    private var useGPU = false
    private lateinit var bDelete:Button
    private lateinit var bSave:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_final_image)

        resultImageView = findViewById(R.id.finalImage)
        bDelete = findViewById(R.id.bDelete)
        bSave = findViewById(R.id.bSave)

        viewModel = ViewModelProviders.of(this)
                .get(MLExecutionViewModel::class.java)

        viewModel.styledBitmap.observe(
            this,
            Observer { resultImage ->
                if (resultImage != null) {
                    updateUIWithResults(resultImage)
                }
            }
        )
        styleTransferModelExecutor = StyleTransferModelExecutor(this@DisplayFinalImage, useGPU)

        val intent = getIntent()
        lastSavedFile = SelectStyleActivity.contentImageFilePath
        selectedStyle = intent.getStringExtra("styleImage")
        startRunningModel()

        bDelete.setOnClickListener {
            val intent = Intent(this, HomeScreenActivity::class.java)
            startActivity(intent)
        }

        bSave.setOnClickListener{
            // Check for write external storage permission
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                if (ContextCompat.checkSelfPermission(this@DisplayFinalImage, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        !=PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 100)
                } else{
                    saveImageToStorage()
                }
            } else{
                saveImageToStorage()
            }

            val intent = Intent(this, HomeScreenActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 100){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                saveImageToStorage()
            } else{
                Toast.makeText(this, "Permission not granted, image can not be saved", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //function to save image to photo gallery
    public fun saveImageToStorage(){
        val externalStorageState = Environment.getExternalStorageState()
        if (externalStorageState.equals(Environment.MEDIA_MOUNTED)){
            val storageDirectory = Environment.getExternalStorageDirectory().toString()
            val file = File(storageDirectory, String.format("stylizedImage_%d.jpg", System.currentTimeMillis()))
            try {
                val stream: OutputStream = FileOutputStream(file)
                var drawable = resultImageView.getDrawable() as BitmapDrawable
                var bitmap = (drawable as BitmapDrawable).bitmap
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                stream.flush()
                stream.close()
                Toast.makeText(this, "Image Saved successfully ${Uri.parse(file.absolutePath)}", Toast.LENGTH_SHORT).show()
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }


    private fun updateUIWithResults(modelExecutionResult: ModelExecutionResult) {
        setImageView(resultImageView, modelExecutionResult.styledImage)
    }

    //fun that sets the final rendered image to the image view
    private fun setImageView(imageView: ImageView, image: Bitmap) {
        Glide.with(baseContext)
                .load(image)
                .override(512, 512)
                .fitCenter()
                .into(imageView)
    }

    //starts running the pre-trained model
    private fun startRunningModel() {
         if (lastSavedFile.isNotEmpty() && selectedStyle.isNotEmpty()) {
            viewModel.onApplyStyle(
                    baseContext, lastSavedFile, selectedStyle, styleTransferModelExecutor,
                    inferenceThread
            )
          }
    }
}
