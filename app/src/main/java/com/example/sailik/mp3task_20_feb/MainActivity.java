package com.example.sailik.mp3task_20_feb;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mDownloadBtn;
    private EditText mURLEditText;
    private MyWebRequestReceiver receiver;
    NotificationManager notificationManager;
    android.support.v4.app.NotificationCompat.Builder myNotification;
    private static final String TAG="MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDownloadBtn = (Button) findViewById(R.id.download_button);
        mURLEditText = (EditText) findViewById(R.id.URl_editText);

        mDownloadBtn.setOnClickListener(this);
        IntentFilter filter = new IntentFilter("response download");
        //filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new MyWebRequestReceiver();
        registerReceiver(receiver, filter);
        notificationManager =(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.download_button:
                String url_input = mURLEditText.getText().toString();
                String file_name = "file_1.mp3";
                if(url_input!=null){

                    String urlinputArray[] = url_input.split("\\.");
                    String extension = urlinputArray[urlinputArray.length-1];
                    if(extension.equals("mp3")) {

                        Intent intent = new Intent(this, MyQueue.class);
                        intent.putExtra("KEY_URL", url_input);
                        intent.putExtra("KEY_NAME", file_name);
                        startService(intent);
                    }
                    else{
                        Toast.makeText(MainActivity.this,"invalid input!!",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(MainActivity.this,"Enter URl. Nothing entered!!",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        unregisterReceiver(receiver);
    }
    
    public class MyWebRequestReceiver extends BroadcastReceiver {


        private static final int MY_NOTIFICATION_ID=1;

        @Override
        public void onReceive(Context context, Intent intent) {
            //Toast.makeText(getApplicationContext(),"broadcast",Toast.LENGTH_SHORT).show();



            String msg ="Download Complete!";

            Intent inte = new Intent(getApplicationContext(),MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0,inte,0);

            myNotification = new NotificationCompat.Builder(getApplicationContext());
            myNotification.setContentTitle("Mp3Task")
                          .setContentText(msg)
                          .setSmallIcon(R.drawable.img_alert)
                          .setTicker("Notification!")
                          .setContentIntent(pendingIntent);

            notificationManager.notify(MY_NOTIFICATION_ID, myNotification.build());





        }


    }



}
