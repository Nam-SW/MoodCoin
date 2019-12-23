package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    Button btn1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, SplashActivity.class);
        startActivity(intent);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        btn1 = (Button)findViewById(R.id.startbtn);
        String str = "";
        String sstr = "";
        ////////////////////////////////////////////////////////////////////////////////////////////////
        Log.d("ㅇㅇㅇ", "test1");
        try{
            BufferedReader br = new BufferedReader(new FileReader(getFilesDir()+"check.txt"));
//            while(((str = br.readLine()) != null)){
//                sstr += str +"\n";
//                Log.d("ㅇㅇㅇ", "test2" + sstr);
//            }
            str = br.readLine();
            br.close();
            Log.d("ㅇㅇㅇ", "test5" + str);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("ㅇㅇㅇ", "test4");
        //str = str.trim();
        Log.d("ㅇㅇㅇ", "test3");
        Log.d("0000", "아아아"+str);
        if(str.equals("O")){
            Log.d("ㅇㅇㅇ", "점프");
            nextjump();
            finish();
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    BufferedWriter bw = new BufferedWriter(new FileWriter(getFilesDir() + "check.txt", false));
                    bw.write("O");
                    bw.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
               nextjump();
            }
        });

    }

    private void nextjump() {
        Intent intent1 = new Intent(this, lobbyActivity.class);
        startActivity(intent1);
    }
}
