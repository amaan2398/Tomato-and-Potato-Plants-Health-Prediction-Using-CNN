package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ServerImageSend extends AsyncTask<Void,Void,String> {

    private static final String API_KEY = "YOUR API KEY";
    private static final String LINE_FEED = "\r\n";

    Intent intent;
    TextView textView;
    ImageView imageView;
    String printView="";
    String buttonm ="";

    ServerImageSend(Intent intent,TextView textView ,ImageView imageView ,String buttonm){
        this.textView=textView;
        this.intent=intent;
        this.imageView=imageView;
        this.buttonm =buttonm;
        Log.d("what",buttonm);
    }




    protected String doInBackground(Void... voids) {
        String charset = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            charset = StandardCharsets.UTF_8.name();
        }

        // Your image file to upload
        String button=intent.getStringExtra("button");
        File uploadFile = new File(intent.getStringExtra("ImageP"));
        String requestURL = "..."+buttonm;//in place of ... use own ip or domain which ure hosting ure server

        // Creates a unique boundary based on time stamp
        String boundary = String.valueOf(System.currentTimeMillis());

        // Setup http connection
        URL url = null;
        try {
            url = new URL(requestURL);

            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setUseCaches(false);
            httpConn.setDoOutput(true); // POST method
            httpConn.setRequestProperty("User-Agent", "API Java example");
            httpConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            httpConn.setRequestProperty("pic", API_KEY);

            // Setup OutputStream and PrintWriter
            OutputStream outputStream = httpConn.getOutputStream();
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, charset), true);

            // Add image file to POST request
            String fieldName = "pic";
            String fileName = uploadFile.getName();
            writer.append("--").append(boundary).append(LINE_FEED);
            writer.append("Content-Disposition: form-data; name=\"").append(fieldName).append("\"; filename=\"").append(fileName).append("\"").append(LINE_FEED);
            writer.append(LINE_FEED);
            writer.flush();

            FileInputStream inputStream = new FileInputStream(uploadFile);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
            inputStream.close();

            writer.append(LINE_FEED);
            writer.flush();

            // Send POST request and get response
            List<String> response = new ArrayList<>();
            writer.append(LINE_FEED).flush();
            writer.append("--").append(boundary).append("--").append(LINE_FEED);
            writer.close();

            // Check server status message
            int status = httpConn.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        httpConn.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    response.add(line);
                }
                reader.close();
                httpConn.disconnect();
            } else {
                throw new IOException("Server returned non-OK status: " + status);
            }



            // Read response
            for (String line : response) {
                printView += line;
            }




        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return printView;
    }

    @Override
    protected void onPostExecute(String s) {
        textView.setText(s);
        if(s.contains("Unhealthy")){
            textView.setTextColor(Color.RED);
        }
        else if(s.contains("Healthy")){
            textView.setTextColor(Color.GREEN);
        }
        textView.setTextSize(35);
        File imgFile = new  File(intent.getStringExtra("ImageP"));
        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            //Bitmap bitmap = Bitmap.createScaledBitmap(myBitmap,20,20,true);
            imageView.setImageBitmap(myBitmap);

        }
    }
}
