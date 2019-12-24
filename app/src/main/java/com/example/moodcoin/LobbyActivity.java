package com.example.moodcoin;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class LobbyActivity extends Activity {

    String happy, fireangry, disappear, sad, angry;
    DatabaseReference mDB_ref ;
    String id;
    String today;
    TextView eh, efa, eda, es, ea;
    HashMap<String, Object> childUpdates = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        childUpdates = new HashMap<>();
        id = getIntent().getStringExtra("id");
        today = getIntent().getStringExtra("today");
        mDB_ref = FirebaseDatabase.getInstance().getReference().child(id);

        isExist();

        eh = (TextView) findViewById(R.id.happy);
        efa = (TextView) findViewById(R.id.fireangry);
        eda = (TextView) findViewById(R.id.disappear);
        es = (TextView) findViewById(R.id.sad);
        ea = (TextView) findViewById(R.id.angry);



        mDB_ref.addValueEventListener(Listener);

    }

    private void isExist() {

    }

    ValueEventListener Listener = new ValueEventListener() {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            try{
                happy = dataSnapshot.child(today).child("기쁨").getValue().toString();
                fireangry = dataSnapshot.child(today).child("분노").getValue().toString();
                disappear = dataSnapshot.child(today).child("불안").getValue().toString();
                sad = dataSnapshot.child(today).child("슬픔").getValue().toString();
                angry = dataSnapshot.child(today).child("짜증").getValue().toString();
                eh.setText(happy);
                efa.setText(fireangry);
                eda.setText(disappear);
                es.setText(sad);
                ea.setText(angry);
            }catch(Exception a){
                childUpdates.put(today +"/기쁨", 0);
                mDB_ref.updateChildren(childUpdates);
                childUpdates.put(today +"/분노", 0);
                mDB_ref.updateChildren(childUpdates);
                childUpdates.put(today +"/불안", 0);
                mDB_ref.updateChildren(childUpdates);
                childUpdates.put(today +"/슬픔", 0);
                mDB_ref.updateChildren(childUpdates);
                childUpdates.put(today +"/짜증", 0);
                mDB_ref.updateChildren(childUpdates);
                eh.setText("0");
                efa.setText("0");
                eda.setText("0");
                es.setText("0");
                ea.setText("0");
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(getApplicationContext(),"인터넷 연결을 확인해 주세요.",Toast.LENGTH_LONG).show();
        }

    };

}