package com.example.moodcoin;

import android.graphics.Color;
import android.os.Bundle;
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
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class FragmentMain extends Fragment {

    HashMap<String, Object> childUpdates = null;
    DatabaseReference mDB_ref ;
    String id;
    String today;
    TextView eh, efa, eda, es, ea;
    String happy, fireangry, disappear, sad, angry;
    Float _happy, _fireangry, _disappear, _sad, _angry;
    PieChart pieChart;
    ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        childUpdates = new HashMap<>();
        Bundle bundle = this.getArguments();
        id = bundle.getString("id");
        today = bundle.getString("today");

        View v = inflater.inflate(R.layout.fragment_main, container, false);
        mDB_ref = FirebaseDatabase.getInstance().getReference();

        eh = (TextView) v.findViewById(R.id.happy);
        efa = (TextView) v.findViewById(R.id.fireangry);
        eda = (TextView) v.findViewById(R.id.disappear);
        es = (TextView) v.findViewById(R.id.sad);
        ea = (TextView) v.findViewById(R.id.angry);

        mDB_ref.addValueEventListener(Listener);

        pieChart = (PieChart)v.findViewById(R.id.piechart);

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5,10,5,5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(false);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);

        number();

        Description description = new Description();
        description.setText("세계 국가"); //라벨
        description.setTextSize(15);
        pieChart.setDescription(description);

        pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic); //애니메이션

        PieDataSet dataSet = new PieDataSet(yValues,"Countries");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        PieData data = new PieData((dataSet));
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.YELLOW);

        pieChart.setData(data);

        return v;
    }

    private void number() {
        _happy = Float.parseFloat(eh.getText().toString());
        _fireangry = Float.parseFloat(efa.getText().toString());
        _angry = Float.parseFloat(ea.getText().toString());
        _disappear = Float.parseFloat(eda.getText().toString());
        _sad = Float.parseFloat(es.getText().toString());
        yValues.add(new PieEntry(_happy,"행복"));
        yValues.add(new PieEntry(_fireangry,"분노"));
        yValues.add(new PieEntry(_disappear,"불안"));
        yValues.add(new PieEntry(_sad,"슬픔"));
        yValues.add(new PieEntry(_angry,"짜증"));
    }

    ValueEventListener Listener = new ValueEventListener() {

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
                    happy = dataSnapshot.child(id).child(today).child("기쁨").getValue().toString();
                    fireangry = dataSnapshot.child(id).child(today).child("분노").getValue().toString();
                    disappear = dataSnapshot.child(id).child(today).child("불안").getValue().toString();
                    sad = dataSnapshot.child(id).child(today).child("슬픔").getValue().toString();
                    angry = dataSnapshot.child(id).child(today).child("짜증").getValue().toString();
                }catch (Exception a){
                    make_id_toFB();
                    make_today_toFB();
                    happy = dataSnapshot.child(id).child(today).child("기쁨").getValue().toString();
                    fireangry = dataSnapshot.child(id).child(today).child("분노").getValue().toString();
                    disappear = dataSnapshot.child(id).child(today).child("불안").getValue().toString();
                    sad = dataSnapshot.child(id).child(today).child("슬픔").getValue().toString();
                    angry = dataSnapshot.child(id).child(today).child("짜증").getValue().toString();
                }
            }
            _happy = Float.parseFloat(happy);
            _angry = Float.parseFloat(angry);
            _disappear = Float.parseFloat(disappear);
            _fireangry = Float.parseFloat(fireangry);
            _sad = Float.parseFloat(sad);
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



}
