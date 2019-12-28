package com.example.moodcoin;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class FragmentMain extends Fragment {

    HashMap<String, Object> childUpdates = null;
    DatabaseReference mDB_ref;
    String getsum = "";
    String id;
    String today;
    TextView eh, efa, eda, es, ea, lobby_title, most, when, allfeel, imana;
    String happy, fireangry, disappear, sad, angry;
    static Float _happy =0f, _fireangry=0f, _disappear=0f, _sad=0f, _angry=0f;
    PieChart pieChart;
    ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();
    int cnt=0;
    SimpleDateFormat hour = new SimpleDateFormat("HH");
    SimpleDateFormat min = new SimpleDateFormat("mm");

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        childUpdates = new HashMap<>();
        Bundle bundle = this.getArguments();
        id = bundle.getString("id");
        today = bundle.getString("today");

        View v = inflater.inflate(R.layout.fragment_main, container, false);
        mDB_ref = FirebaseDatabase.getInstance().getReference();

        mDB_ref.addListenerForSingleValueEvent(Listener);

        eh = (TextView) v.findViewById(R.id.happy);
        efa = (TextView) v.findViewById(R.id.fireangry);
        eda = (TextView) v.findViewById(R.id.disappear);
        es = (TextView) v.findViewById(R.id.sad);
        ea = (TextView) v.findViewById(R.id.angry);

        imana = (TextView)v.findViewById(R.id.imana);
        lobby_title = (TextView)v.findViewById(R.id.lobby_title);
        most = (TextView)v.findViewById(R.id.most);
        when = (TextView)v.findViewById(R.id.when);
        allfeel = (TextView)v.findViewById(R.id.allfeel);

        pieChart = (PieChart)v.findViewById(R.id.piechart);

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
            try{
                happy = dataSnapshot.child(id).child(today).child("기쁨").getValue().toString();
                fireangry = dataSnapshot.child(id).child(today).child("분노").getValue().toString();
                disappear = dataSnapshot.child(id).child(today).child("불안").getValue().toString();
                sad = dataSnapshot.child(id).child(today).child("슬픔").getValue().toString();
                angry = dataSnapshot.child(id).child(today).child("짜증").getValue().toString();
                eh.setText(happy);
                efa.setText(fireangry);
                eda.setText(disappear);
                es.setText(sad);
                ea.setText(angry);
            }catch (Exception e){
                try{
                    make_today_toFB();
                }catch (Exception a){
                    make_id_toFB();
                    make_today_toFB();
                }
            }finally {
                _happy = Float.parseFloat(eh.getText().toString());
                _fireangry = Float.parseFloat(efa.getText().toString());
                _angry = Float.parseFloat(ea.getText().toString());
                _disappear = Float.parseFloat(eda.getText().toString());
                _sad = Float.parseFloat(es.getText().toString());
                setChart();

                try {
                    setPriceInfo();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                pieDataRefresh();
            }
            gethighest();
            //set_title();

        }

        private void setPriceInfo() throws IOException {
            String h = hour.format(new Date());
            String m = min.format(new Date());
            int hh = Integer.parseInt(h);
            int mm = Integer.parseInt(m);
            if((hh >= 20 || (hh==19 && mm>=30))){ //7:30이 지난 경우, 오늘 송금 금액과 기분을 띄운다.
                try{
                    if(!getRecentDay().equals(today)){
                        try{
                            setRecentSum(gethighest());
                            setRecentDay();
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }catch (Exception a){
                    try{
                        setRecentSum(gethighest());
                        setRecentDay();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                when.setText("오늘은 대체적으로");
                getRecentDay();
                allfeel.setText(getRecentSum());
            }else{// 7:30이 지나지 않은 경우, 어제의 데이터를 띄운다.
                try {
                    when.setText(getRecentDay().substring(4,6) + "월" + getRecentDay().substring(6,8) + "일에는 대체적으로");
                    allfeel.setText(getRecentSum());
                }catch (Exception e){
                    when.setText("아직 이전");
                    imana.setText("데이터가 없네요");
                    allfeel.setText("");
                }
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
            lobby_title.setText(title[cnt]);
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
            eh.setText("0");
            efa.setText("0");
            eda.setText("0");
            es.setText("0");
            ea.setText("0");
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

}