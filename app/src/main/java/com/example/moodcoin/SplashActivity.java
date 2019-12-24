package com.example.moodcoin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                jumpnext();
                finish();
            }
        }, 2000);


    }

    private void jumpnext() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
