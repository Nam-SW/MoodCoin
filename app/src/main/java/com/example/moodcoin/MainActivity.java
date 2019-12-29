package com.example.moodcoin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button login;
    String id, today;
    EditText ed_id;
    Date currentTime;
    String str = "";
    int cnt=0;
    SimpleDateFormat Format = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = (Button)findViewById(R.id.login);
        ed_id = (EditText)findViewById(R.id.id_input);

        //현재 날짜를 받아옴
        currentTime  = Calendar.getInstance().getTime();
        today = Format.format(currentTime);

        //파일 입출력으로 check.txt의 내용을 불러옴
        try{
            BufferedReader br = new BufferedReader(new FileReader(getFilesDir()+"check.txt"));
            str = br.readLine();
            br.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        //이전에 로그인 시, 로그인 창 없이 바로 LobbyActivity로 넘어감
        if(str.length()==18){
            cnt = 1;
            nextjump();
        }

        // 디코 ID 입력 후 로그인 버튼을 누를 시
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ed_id.getText().toString().length()<18 || ed_id.getText().toString().length()>18)
                    Toast.makeText(getApplicationContext(),"ID는 숫자 18자입니다.",Toast.LENGTH_LONG).show();
                else{
                    try{
                        id = ed_id.getText().toString();
                        BufferedWriter bw = new BufferedWriter(new FileWriter(getFilesDir() + "check.txt", false));
                        bw.write(id);
                        bw.close();
                        BufferedWriter sum = new BufferedWriter(new FileWriter(getFilesDir() + "/recent_sum.txt", false));
                        sum.write("0");
                        sum.close();
                        BufferedWriter set = new BufferedWriter(new FileWriter(getFilesDir() + "/recent_date.txt", false));
                        set.write("0");
                        set.close();
                        BufferedWriter ischanged = new BufferedWriter(new FileWriter(getFilesDir() + "/ischecked.txt", false));
                        ischanged.write("0");
                        ischanged.close();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    nextjump();
                }
            }
        });
    }

    //LobbyActivity로 보내주는 인스턴스
    private void nextjump() {
        Intent intent1 = new Intent(this, LobbyActivity.class);
        if(cnt == 1)
            intent1.putExtra("id", str);
        else
            intent1.putExtra("id", id);

        intent1.putExtra("today", today);
        startActivity(intent1);
        finish();
    }
}
