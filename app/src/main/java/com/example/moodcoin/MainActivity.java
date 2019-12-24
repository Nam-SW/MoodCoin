package com.example.moodcoin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends Activity {
    Button btn1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn1 = (Button)findViewById(R.id.login);
        String str = "";
        String sstr = "";
        ////////////////////////////////////////////////////////////////////////////////////////////////
        Log.d("ㅇㅇㅇ", "test1");
        try{
            BufferedReader br = new BufferedReader(new FileReader(getFilesDir()+"check.txt"));
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
        Intent intent1 = new Intent(this, LobbyActivity.class);
        startActivity(intent1);
    }
}
