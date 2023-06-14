package com.suhun.threadbroadcast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private String tag = MainActivity.class.getSimpleName();
    private TextView showResult1, showResult2;
    private EditText userInput;
    private MyReceiver myReceiver;
    private UIHandler uiHandlerhandler = new UIHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showResult1 = findViewById(R.id.outputResult1);
        showResult2 = findViewById(R.id.outputResult2);
        userInput = findViewById(R.id.threadName);
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

    public void showThread1Result2(View view){
        String threadName = userInput.getText().toString();
        if(!threadName.equals("")){
            Log.d(tag, "++++thread name:"+ threadName);
            RunAddCounter runAddCounter = new RunAddCounter(threadName);
            runAddCounter.start();
        }
    }

    private class UIHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what==0){
                Bundle getMsgData = msg.getData();
                String result = getMsgData.getString("counter");
                showResult2.setText(result);
            }
        }
    }

    private class MyReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String result = intent.getStringExtra("count");
            showResult1.setText(result);
        }
    }

    private class RunAddCounter extends Thread{
        private String name;

        public RunAddCounter(String name){
            this.name = name;
        }

        @Override
        public void run() {
            super.run();
            for(int i=0; i<30;i++){
                Log.d(tag, "++++thread:" + this.name + i);
                String data = this.name + i;
                Message message = new Message();
                message.what = 0;
                Bundle bundle = new Bundle();
                bundle.putString("counter", data);
                message.setData(bundle);
                uiHandlerhandler.sendMessage(message);
                try{
                    Thread.sleep(500);
                }catch (Exception e){
                    System.out.println(e);
                }
            }
        }
    }
}