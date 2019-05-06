package com.example.myapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button potatoB = null;
    Button tomatoB = null;
    private int STORAGE_PERMISSION_CODE = 1;
    Intent loadImagActivity=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(MainActivity.this, "You have already granted this permission!",
                    Toast.LENGTH_SHORT).show();
        } else {
            requestStoragePermission();
        }

        potatoB = (Button) findViewById(R.id.Potato);
        tomatoB = (Button) findViewById(R.id.Tomato);

        potatoB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadImagActivity = new Intent(MainActivity.this,LoadImag.class);
                loadImagActivity.putExtra("message","getPotato");
                startActivity(loadImagActivity);


            }
        });


        tomatoB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadImagActivity = new Intent(MainActivity.this,LoadImag.class);
                loadImagActivity.putExtra("message","getTomato");
                startActivity(loadImagActivity);


            }
        });

    }


    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.INTERNET)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.INTERNET}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.INTERNET}, STORAGE_PERMISSION_CODE);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }



}
