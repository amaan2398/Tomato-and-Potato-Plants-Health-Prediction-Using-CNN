package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultView extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_view);
        String buttonm="";
        Intent intent = getIntent();
        if (intent!=null) {
            buttonm = intent.getStringExtra("buttonm");
        }
        TextView textView=(TextView)findViewById(R.id.prediction);
        textView.setText("Image is UPLOADING & Processing please wait...");
        textView.setTextSize(20);
        ImageView imageView=(ImageView) findViewById(R.id.imageView4);
        ServerImageSend serverImageSend=new ServerImageSend(intent,textView,imageView, buttonm);
        serverImageSend.execute();




    }
}
