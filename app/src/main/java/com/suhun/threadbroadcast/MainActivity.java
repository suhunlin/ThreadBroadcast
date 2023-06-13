package com.suhun.threadbroadcast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private String tag = MainActivity.class.getSimpleName();
    TextView showResult1;
    private MyReceiver myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showResult1 = findViewById(R.id.outputResult1);
    }

    @Override
    protected void onStart() {
        super.onStart();
        myReceiver = new MyReceiver();
        registerReceiver(myReceiver,new IntentFilter("thread1"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(myReceiver);
    }

    public void showThread1Result(View view){
        new Thread(){
            @Override
            public void run() {
                super.run();
                for(int i=1;i<=20;i++){
                    Log.d(tag, "+++++tag"+i);
                    String msg = ""+i;
                    Intent intent = new Intent("thread1");
                    intent.putExtra("count", msg);
                    sendBroadcast(intent);
                    try{
                        Thread.sleep(500);
                    }catch (Exception e){
                        System.out.println(e);
                    }
                }
            }
        }.start();
    }

    private class MyReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String result = intent.getStringExtra("count");
            showResult1.setText(result);
        }
    }
}