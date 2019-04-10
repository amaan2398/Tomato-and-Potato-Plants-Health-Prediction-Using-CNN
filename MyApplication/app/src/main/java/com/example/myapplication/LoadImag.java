package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class LoadImag  extends AppCompatActivity {

    public static final int RESULT_LOAD_IMAGE = 1;
    public static final int CAMERA_REQUEST=2;


    public Button loadB = null;
    public Button cameraB = null;
    public Button submitB =null;


    public Intent intent=null;
    String pictureImagePath=null;
    Uri outputFileUri;
    String message=null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_imag);

        intent=getIntent();

        loadB = findViewById(R.id.LoadImage);
        cameraB = findViewById(R.id.Camera);
        submitB = findViewById(R.id.Submit);

        loadB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        cameraB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = timeStamp + ".jpg";
                File storageDir = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES);
                pictureImagePath = storageDir.getAbsolutePath() +"/planDis" +"/" + imageFileName;
                File file = new File(pictureImagePath);
                outputFileUri = FileProvider.getUriForFile(LoadImag.this, LoadImag.this.getApplicationContext().getPackageName()+".my.package.name.provider", file);
                Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE );
                intent1.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent1.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                startActivityForResult(intent1,CAMERA_REQUEST);
            }
        });

        submitB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intent!=null) {
                    message = intent.getStringExtra("message");
                }
                Intent loadImagActivity = new Intent(LoadImag.this,ResultView.class);
                loadImagActivity.putExtra("ImageP",pictureImagePath);
                loadImagActivity.putExtra("buttonm",message);
                startActivity(loadImagActivity);
            }
        });

    }

    @SuppressLint("CutPasteId")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAMERA_REQUEST){
            File imgFile = new  File(pictureImagePath);
            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                ImageView myImage = findViewById(R.id.imageView);
                myImage.setImageBitmap(myBitmap);

            }

        }

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            int columnIndex;
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = null;
            if (selectedImage != null) {
                cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            }
            if (cursor != null) {
                cursor.moveToFirst();
                columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                pictureImagePath= cursor.getString(columnIndex);
                cursor.close();
            }
            ImageView imageView = findViewById(R.id.imageView);
            imageView.setImageURI(selectedImage);

        }

    }




























}
