package com.example.moodcoin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button login;
    String id;
    EditText ed_id;
    Date currentTime;
    SimpleDateFormat Format = new SimpleDateFormat("yyyymmdd", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, SplashActivity.class);
        startActivity(intent);

        login = (Button)findViewById(R.id.login);
        ed_id = (EditText)findViewById(R.id.id_input);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ed_id.getText().toString().length()<18 || ed_id.getText().toString().length()>18)
                    Toast.makeText(getApplicationContext(),"ID는 숫자 18자입니다.",Toast.LENGTH_LONG).show();
                else{
                    id = ed_id.getText().toString();
                    currentTime  = Calendar.getInstance().getTime();
                    String today = Format.format(currentTime);
                    Intent intent = new Intent(MainActivity.this, LobbyActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("today", today);
                    startActivity(intent);
                }
            }
        });
    }
}

