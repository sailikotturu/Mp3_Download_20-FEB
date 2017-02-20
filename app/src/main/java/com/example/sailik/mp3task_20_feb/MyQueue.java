package com.example.sailik.mp3task_20_feb;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by saili.k on 20-02-2017.
 */

public class MyQueue extends IntentService {

    private static final String TAG="MyQueue";


    public MyQueue() {
        super("MyService");
    }

    @Override
    public void onCreate(){
        super.onCreate();
        Log.d(TAG,"onCreate()");

    }
    @Override
    protected void onHandleIntent(Intent intent) {
        String mp3_url = intent.getStringExtra("KEY_URL");
        String fileName = intent.getStringExtra("KEY_NAME");
        int totalSize=0;
        Log.d(TAG,"onhandleintent before try");
        try {
            URL url = new URL(mp3_url);
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.connect();
            //if(urlConnection.getResponseCode()!= HttpURLConnection.HTTP_OK){
//            Intent broadcastIntent = new Intent();
//            broadcastIntent.setAction("response download");
//
//            broadcastIntent.putExtra("status", "url invalid");
//
//            sendBroadcast(broadcastIntent);
            //}
            //else {
            Log.d(TAG, "onhandleintent after try after connect");

            File SDCardRoot = Environment.getExternalStorageDirectory();
            File file = new File(SDCardRoot, fileName);
            if (file.exists()) {
                    Log.d(TAG, "onhandleintent in if block");
                    file.delete();
                    file = new File(SDCardRoot, fileName);
            }

            FileOutputStream fileOutput = new FileOutputStream(file);
            InputStream inputStream = urlConnection.getInputStream();

                //this is the total size of the file which we are downloading
            totalSize = urlConnection.getContentLength();
            byte[] buffer = new byte[1024];
            int bufferLength = 0;
            while ((bufferLength = inputStream.read(buffer)) > 0) {
                    fileOutput.write(buffer, 0, bufferLength);
            }
            fileOutput.close();

                Log.d(TAG, "onhandleintent after close");

                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("response download");

                broadcastIntent.putExtra("status", "done");

                sendBroadcast(broadcastIntent);



        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
