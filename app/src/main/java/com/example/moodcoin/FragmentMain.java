package com.example.moodcoin;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class FragmentMain extends Fragment {

    HashMap<String, Object> childUpdates = null;
    DatabaseReference mDB_ref;
    LinearLayout no, yes, most_things_view, price_window, rec_h, rec_fa, rec_d, rec_s, rec_a, recommandation, when_box;
    LinearLayout h1, h2, h3, fa1, fa2, fa3, d1, d2, d3, s1, s2, s3, a1, a2, a3;
    String id;
    String today;
    TextView lobby_title, most, when, allfeel, imana, when_income, price, tv1;
    String happy, fireangry, disappear, sad, angry;
    static Float _happy =0f, _fireangry=0f, _disappear=0f, _sad=0f, _angry=0f;
    PieChart pieChart;
    ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();
    int cnt=0;
    SimpleDateFormat hour = new SimpleDateFormat("HH");
    SimpleDateFormat min = new SimpleDateFormat("mm");
    SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");
    int flagnum = 1;
    String addprice = " ";

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        childUpdates = new HashMap<>();
        Bundle bundle = this.getArguments();
        id = bundle.getString("id");
        today = bundle.getString("today");
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        mDB_ref = FirebaseDatabase.getInstance().getReference();

        final SwipeRefreshLayout swipeRefreshLayout = v.findViewById(R.id.swipe_refresh_layout);
        mDB_ref.addListenerForSingleValueEvent(Listener);

        no = (LinearLayout)v.findViewById(R.id.no_info);
        yes = (LinearLayout)v.findViewById(R.id.yes_info);
        most_things_view = (LinearLayout)v.findViewById(R.id.most_things_view);
        price_window = (LinearLayout)v.findViewById(R.id.price_window);
        rec_a = (LinearLayout)v.findViewById(R.id.rec_a);
        rec_fa = (LinearLayout)v.findViewById(R.id.rec_fa);
        rec_d = (LinearLayout)v.findViewById(R.id.rec_d);
        rec_s = (LinearLayout)v.findViewById(R.id.rec_s);
        rec_h = (LinearLayout)v.findViewById(R.id.rec_h);
        recommandation = (LinearLayout)v.findViewById(R.id.recommendation);
        when_box = (LinearLayout)v.findViewById(R.id.when_box);

        h1 = (LinearLayout)v.findViewById(R.id.h1);
        h2 = (LinearLayout)v.findViewById(R.id.h2);
        h3 = (LinearLayout)v.findViewById(R.id.h3);
        fa1 = (LinearLayout)v.findViewById(R.id.fa1);
        fa2 = (LinearLayout)v.findViewById(R.id.fa2);
        fa3 = (LinearLayout)v.findViewById(R.id.fa3);
        d1 = (LinearLayout)v.findViewById(R.id.d1);
        d2 = (LinearLayout)v.findViewById(R.id.d2);
        d3 = (LinearLayout)v.findViewById(R.id.d3);
        s1 = (LinearLayout)v.findViewById(R.id.s1);
        s2 = (LinearLayout)v.findViewById(R.id.s2);
        s3 = (LinearLayout)v.findViewById(R.id.s3);
        a1 = (LinearLayout)v.findViewById(R.id.a1);
        a2 = (LinearLayout)v.findViewById(R.id.a2);
        a3 = (LinearLayout)v.findViewById(R.id.a3);
        tv1 = (TextView)v.findViewById(R.id.tv1);

        h1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.parse("https://www.youtube.com/watch?v=cmv37FtCwj8");
                intent.setData(uri);
                startActivity(intent);
            }
        });
        h2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.parse("https://www.youtube.com/watch?v=7anO9e8ozP8");
                intent.setData(uri);
                startActivity(intent);
            }
        });
        h3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.parse("https://www.youtube.com/watch?v=bwzjR-hvj9Q");
                intent.setData(uri);
                startActivity(intent);
            }
        });
        fa1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.parse("https://www.youtube.com/watch?v=mLLtYA-VcAA");
                intent.setData(uri);
                startActivity(intent);
            }
        });
        fa2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.parse("https://www.youtube.com/watch?v=HUHC9tYz8ik");
                intent.setData(uri);
                startActivity(intent);
            }
        });
        fa3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.parse("https://www.youtube.com/watch?v=MWTcdrtdpcY");
                intent.setData(uri);
                startActivity(intent);
            }
        });
        d1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.parse("https://www.youtube.com/watch?v=MVr_AVI8LSw");
                intent.setData(uri);
                startActivity(intent);
            }
        });
        d2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.parse("https://www.youtube.com/watch?v=LHUAmHYOXFM");
                intent.setData(uri);
                startActivity(intent);
            }
        });
        d3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.parse("https://www.youtube.com/watch?v=IC1Zv4CXnAE");
                intent.setData(uri);
                startActivity(intent);
            }
        });
        s1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.parse("https://www.youtube.com/watch?v=gBRi6aZJGj4");
                intent.setData(uri);
                startActivity(intent);
            }
        });
        s2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.parse("https://www.youtube.com/watch?v=sF9sv9lT83A");
                intent.setData(uri);
                startActivity(intent);
            }
        });
        s3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.parse("https://www.youtube.com/watch?v=QLtqTGjQLCk");
                intent.setData(uri);
                startActivity(intent);
            }
        });
        a1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.parse("https://www.youtube.com/watch?v=J5uapKonaG0");
                intent.setData(uri);
                startActivity(intent);
            }
        });
        a2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.parse("https://www.youtube.com/watch?v=D-rB9IhRQTM&t=2s");
                intent.setData(uri);
                startActivity(intent);
            }
        });
        a3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.parse("https://www.youtube.com/watch?v=g7MlANTSPmM");
                intent.setData(uri);
                startActivity(intent);
            }
        });

        when_income = (TextView)v.findViewById(R.id.when_income);
        imana = (TextView)v.findViewById(R.id.imana);
        lobby_title = (TextView)v.findViewById(R.id.lobby_title);
        most = (TextView)v.findViewById(R.id.most);
        when = (TextView)v.findViewById(R.id.when);
        allfeel = (TextView)v.findViewById(R.id.allfeel);
        price = (TextView)v.findViewById(R.id.price);

        pieChart = (PieChart)v.findViewById(R.id.piechart);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("asd", "스와이프 접속");
                mDB_ref.addListenerForSingleValueEvent(Listener);
                flagnum = 1;
                new JSONTask().execute("http://10.120.72.146:3000/mywallet");
                if(flagnum == 2){
                    new JSONTask().execute("http://10.120.72.146:3000/statemoney");
                }
                swipeRefreshLayout.setRefreshing((false));
            }
        });

        return v;
    }

    public void pieDataRefresh(){
        PieDataSet dataSet = new PieDataSet(yValues,"");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        PieData data = new PieData((dataSet));
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.YELLOW);

        pieChart.setData(data);
    }


    ValueEventListener Listener = new ValueEventListener() {
        int cnt = 0;
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            price_window.setVisibility(View.VISIBLE);

            String today_changed = date.format(new Date());
            int int_today_changed = Integer.parseInt(today_changed);
            if(int_today_changed > Integer.parseInt(today))
                today = today_changed;
            try{
                happy = dataSnapshot.child(id).child(today).child("기쁨").getValue().toString();
                fireangry = dataSnapshot.child(id).child(today).child("분노").getValue().toString();
                disappear = dataSnapshot.child(id).child(today).child("불안").getValue().toString();
                sad = dataSnapshot.child(id).child(today).child("슬픔").getValue().toString();
                angry = dataSnapshot.child(id).child(today).child("짜증").getValue().toString();
            }catch (Exception e){
                try{
                    make_today_toFB();
                }catch (Exception a){
                    make_id_toFB();
                    make_today_toFB();
                }
            }finally {
                _happy = Float.parseFloat(happy);
                _fireangry = Float.parseFloat(fireangry);
                _angry = Float.parseFloat(angry);
                _disappear = Float.parseFloat(disappear);
                _sad = Float.parseFloat(sad);
                setChart();
                try {
                    setPriceInfo();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                pieDataRefresh();
            }
            gethighest();
        }

        private void setPriceInfo() throws IOException {
            String send_price;
            String h = hour.format(new Date());
            String m = min.format(new Date());
            int hh = Integer.parseInt(h);
            int mm = Integer.parseInt(m);
            if((hh >= 20 || (hh==19 && mm>=30))){ //7:30이 지난 경우, 오늘 송금 금액과 기분을 띄운다.
                if(!getRecentDay().equals(today)){
                    if(!gethighest().equals("X")){ //앱을 실행할 때가 7:30이 지났고 현재 SUM값이 X가 아닐 경우
                        try{
                            setRecentSum(gethighest());
                            setRecentDay();
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                        String got = gethighest();
                        String got_arrayed[] = got.split(" ");
                        String selected = got_arrayed[1+(int)(Math.random()*(got_arrayed.length-1))];
                        switch (selected){
                            case "기쁨": send_price = "700"; break;
                            case "분노": send_price = "400"; break;
                            case "슬픔": send_price = "200"; break;
                            case "불안": send_price = "300"; break;
                            case "짜증": send_price = "500"; break;
                            default: send_price = "0"; break;
                        }
                        BufferedWriter sendprice_br = new BufferedWriter(new FileWriter(getActivity().getFilesDir() + "recent_send_price.txt", false));
                        sendprice_br.write(send_price);
                        sendprice_br.close();
                    }
                }
                if(getRecentDay().equals("0")){ //오늘과 이전 데이터가 없을 경우
                    no_prev_data();
                }else if(getRecentDay().equals(today)){ //오늘 데이터가 있을 경우
                    when_box.setVisibility(View.VISIBLE);
                    when.setText("오늘은 대체적으로");
                    allfeel.setText(getRecentSum());
                    imana.setText("(이)가 많아 보였어요");
                    when_income.setText("오늘 입금된 금액");
                    reported_price();
                }else if(!getRecentDay().equals("0")){ //오늘은 없어도 이전 데이터가 있을 경우
                    show_prev_data();
                }
            }else{ // 7:30이 지나지 않은 경우, 어제의 데이터를 띄운다.
                try {
                    if(getRecentSum().equals("0")){ //이전 데이터가 없을 경우
                        no_prev_data();
                    }else{ //이전 데이터가 있을 경우
                        show_prev_data();
                    }
                }catch (Exception e){
                    no_prev_data();
                }
            }
        }

        private void show_prev_data(){
            when_box.setVisibility(View.VISIBLE);
            when.setText(getRecentDay().substring(4,6) + "월" + getRecentDay().substring(6,8) + "일에는 대체적으로");
            allfeel.setText(getRecentSum());
            imana.setText("(이)가 많아 보였어요");
            when_income.setText("어제 입금된 금액");
            reported_price();
        }

        private void no_prev_data(){
            when_box.setVisibility(View.GONE);
            when.setText("아직 이전");
            imana.setText("데이터가 없네요");
            allfeel.setText("");
        }

        private void reported_price(){
            try{
                //성범
                BufferedReader br = new BufferedReader(new FileReader(getActivity().getFilesDir()+"recent_send_price.txt"));
                String get_price = br.readLine();
                br.close();
                price.setText(get_price);

                addprice = get_price;
                flagnum = 1;
                Log.d("dd", "flagnum1됬다");

                new JSONTask().execute("http://10.120.72.146:3000/mywallet");
                Log.d("마마", "메오오오" + flagnum);
                if(flagnum == 2) {
                    new JSONTask().execute("http://10.120.72.146:3000/statemoney");
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void setChart(){
            yValues =new ArrayList<PieEntry>();
            pieChart.setUsePercentValues(true);
            pieChart.getDescription().setEnabled(false);
            pieChart.setExtraOffsets(5,10,5,5);
            pieChart.setDragDecelerationFrictionCoef(0.95f);
            pieChart.setDrawHoleEnabled(false);
            pieChart.setHoleColor(Color.WHITE);
            pieChart.setTransparentCircleRadius(61f);
            Description description = new Description();
            description.setText(""); //라벨
            description.setTextSize(15);
            pieChart.setDescription(description);
            pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic); //애니메이션
            if (_happy  != 0f) yValues.add(new PieEntry(_happy,"기쁨"));
            if (_fireangry != 0f) yValues.add(new PieEntry(_fireangry,"분노"));
            if (_disappear != 0f) yValues.add(new PieEntry(_disappear,"불안"));
            if (_sad != 0f) yValues.add(new PieEntry(_sad,"슬픔"));
            if (_angry != 0f) yValues.add(new PieEntry(_angry,"짜증"));
            Log.d("TESTPIE", "onDataChange: "+yValues);
        }

        private String gethighest(){
            int [] arr = {Integer.parseInt(happy), Integer.parseInt(fireangry), Integer.parseInt(disappear), Integer.parseInt(sad), Integer.parseInt(angry)};
            String[] title = {
                    "오늘이 세상 행복한 날이었던 이유는\n오늘 세상 이쁜 미소를 보여줬던\n당신 덕분이에요!",
                    "이런 부당하고 뼈아픈 상황에도\n꿋꿋이 살아가는 당신에게\n진심으로 존경스러운 마음이에요!",
                    "초조하고 많이 떨리는 상황이라면,\n당신을 진심으로 응원해 온\n소중한 사람들을 떠올려 보기로 해요!",
                    "가녀린 꽃가지 같은 당신,\n가끔 한번 소리내어 울어도 좋아요.\n당신의 속마음에도 휴식이 필요하니까요..!",
                    "충분히 화낼 만 하고 억장이 무뎌질 순간에도\n최선을 다해 하루하루 살아가는\n당신은 존경스러운 사람이에요!"
            };
            String[] most_arr = {"기쁨", "분노", "불안", "슬픔", "짜증"};
            String[] most_arr_sum = {"","","","",""};
            String sum = "";
            int max=0, cnt=0, i, z=0;
            for(i=0; i < 5; i++){
                if(max<arr[i]){
                    max = arr[i];
                    cnt = i;
                }
            }
            most_arr_sum[z] = most_arr[cnt];
            z++;
            for(int j =0; j < 5; j++){
                if(j!=cnt){
                    if(arr[j]==arr[cnt]){
                        most_arr_sum[z] = most_arr[j];
                        z++;
                    }
                }
            }
            for(int k=0; k<most_arr_sum.length; k++){
                if(!most_arr_sum[k].equals(""))
                    sum = sum +" " + most_arr_sum[k];
            }
            rec_h.setVisibility(View.GONE);
            rec_fa.setVisibility(View.GONE);
            rec_d.setVisibility(View.GONE);
            rec_s.setVisibility(View.GONE);
            rec_a.setVisibility(View.GONE);
            if(max == 0){
                no.setVisibility(View.VISIBLE);
                yes.setVisibility(View.GONE);
                recommandation.setVisibility(View.GONE);
                sum = "X";
                lobby_title.setText("내일이면 소멸되는 남은 당신의 소중한 하루를\n부디 후회없이 보내길 바래요..!");
            }else{
                yes.setVisibility(View.VISIBLE);
                no.setVisibility(View.GONE);
                recommandation.setVisibility(View.VISIBLE);
                lobby_title.setText(title[cnt]);
                switch (most_arr[cnt]){
                    case "기쁨" : rec_h.setVisibility(View.VISIBLE); break;
                    case "분노" : rec_fa.setVisibility(View.VISIBLE); break;
                    case "불안" : rec_d.setVisibility(View.VISIBLE); break;
                    case "슬픔" : rec_s.setVisibility(View.VISIBLE); break;
                    case "짜증" : rec_a.setVisibility(View.VISIBLE); break;
                }
            }
            most.setText(sum);
            return sum;
        }



        private void make_id_toFB() {
            childUpdates.put(id,null);
            mDB_ref.updateChildren(childUpdates);
        }

        private void make_today_toFB() {
            childUpdates.put(id + "/" + today +"/기쁨", 0);
            mDB_ref.updateChildren(childUpdates);
            childUpdates.put(id + "/" + today +"/분노", 0);
            mDB_ref.updateChildren(childUpdates);
            childUpdates.put(id + "/" + today +"/불안", 0);
            mDB_ref.updateChildren(childUpdates);
            childUpdates.put(id + "/" + today +"/슬픔", 0);
            mDB_ref.updateChildren(childUpdates);
            childUpdates.put(id + "/" + today +"/짜증", 0);
            mDB_ref.updateChildren(childUpdates);
            happy = "0";
            fireangry = "0";
            disappear = "0";
            sad = "0";
            angry = "0";
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(getActivity() ,"DB 점검중입니다.",Toast.LENGTH_LONG).show();
        }

    };

    private String getRecentDay(){
        String day = null;
        try {
            BufferedReader get = new BufferedReader(new FileReader(getActivity().getFilesDir()+"/recent_date.txt"));
            day = get.readLine();
            get.close();
        }catch (Exception e){
            String testStr = "";
            File savefile = new File(getActivity().getFilesDir()+"/recent_date.txt");
            try{ FileOutputStream fos = new FileOutputStream(savefile);
                fos.write(testStr.getBytes()); fos.close();}
            catch(IOException a){}
            day = "";
        }

        return day;
    }

    private void setRecentDay(){
        try {
            BufferedWriter set = new BufferedWriter(new FileWriter(getActivity().getFilesDir() + "/recent_date.txt", false));
            set.write(today);
            set.close();
        }catch (Exception e){
            String testStr = "";
            File savefile = new File(getActivity().getFilesDir()+"/recent_date.txt");
            try{ FileOutputStream fos = new FileOutputStream(savefile);
                fos.write(testStr.getBytes()); fos.close();}
            catch(IOException a){}
        }
    }

    private String getRecentSum(){
        String yesterday_sum = null;
        try{
            BufferedReader sum = new BufferedReader(new FileReader(getActivity().getFilesDir()+"/recent_sum.txt"));
            yesterday_sum = sum.readLine();
            sum.close();
        }catch (Exception e){
            String testStr = "";
            File savefile = new File(getActivity().getFilesDir()+"/recent_sum.txt");
            try{ FileOutputStream fos = new FileOutputStream(savefile);
                fos.write(testStr.getBytes()); fos.close();}
            catch(IOException a){}
            yesterday_sum = "";
        }
        return yesterday_sum;
    }

    private void setRecentSum(String getsum){
        try {
            BufferedWriter sum = new BufferedWriter(new FileWriter(getActivity().getFilesDir() + "/recent_sum.txt", false));
            sum.write(getsum);
            sum.close();
        }catch (Exception e){
        }
    }

    public class JSONTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject jsonObject = new JSONObject();
                if(flagnum == 1){
                    jsonObject.accumulate("sendperson", id);
                    Log.d("확인", "flag1 접속");
                }else if(flagnum == 2){
                    jsonObject.accumulate("sendperson", id);
                    jsonObject.accumulate("addprice", addprice);
                    Log.d("확인", "flag2 접속");
                }
                HttpURLConnection con = null;
                BufferedReader reader = null;
                Log.d("asdsad", jsonObject.toString());
                Log.d("flagnum", "flagnum" +  flagnum);
                try{
                    URL url = new URL("http://10.120.72.146:3000/mywallet"); //주소
                    if(flagnum == 2) {
                        url = new URL("http://10.120.72.146:3000/statemoney");
                    }
                    //URL url = new URL(urls[0]);
                    //연결을 함
                    con = (HttpURLConnection) url.openConnection();

                    con.setRequestMethod("POST");//POST방식으로 보냄
                    con.setRequestProperty("Cache-Control", "no-cache");//캐시 설정
                    con.setRequestProperty("Content-Type", "application/json");//application JSON 형식으로 전송


                    con.setRequestProperty("Accept", "text/html");//서버에 response 데이터를 html로 받음
                    con.setDoOutput(true);//Outstream으로 post 데이터를 넘겨주겠다는 의미
                    con.setDoInput(true);//Inputstream으로 서버로부터 응답을 받겠다는 의미
                    con.connect();

                    //서버로 보내기위해서 스트림 만듬
                    OutputStream outStream = con.getOutputStream();
                    //버퍼를 생성하고 넣음
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                    writer.write(jsonObject.toString());
                    writer.flush();
                    writer.close();//버퍼를 받아줌

                    //서버로 부터 데이터를 받음
                    InputStream stream = con.getInputStream();

                    reader = new BufferedReader(new InputStreamReader(stream));

                    StringBuffer buffer = new StringBuffer();

                    String line = "";
                    while((line = reader.readLine()) != null){
                        buffer.append(line);
                    }

                    return buffer.toString();//서버로 부터 받은 값을 리턴해줌 아마 OK!!가 들어올것임

                } catch (MalformedURLException e){
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if(con != null){
                        con.disconnect();
                    }
                    try {
                        if(reader != null){
                            reader.close();//버퍼를 닫아줌
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(flagnum == 1){
                tv1.setText(result);
                flagnum = 2;

                if(!getRecentDay().equals(today))
                    new JSONTask().execute("http://10.120.72.146:3000/statemoney");

            } else if(flagnum == 2){
                tv1.setText(result);
            }
        }
    }
}
