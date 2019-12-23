package com.example.moodcoin;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LobbyActivity extends Activity {

    String happy, fireangry, disappear, sad, angry;
    private DatabaseReference mDB_ref;
    String id = getIntent().getStringExtra("id");
    
    String today = getIntent().getStringExtra("today");
    TextView eh, efa, eda, es, ea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        mDB_ref = FirebaseDatabase.getInstance().getReference().child(id).child(today);
        mDB_ref.addValueEventListener(Listener);
        eh = (TextView) findViewById(R.id.happy);
        efa = (TextView) findViewById(R.id.fireangry);
        eda = (TextView)findViewById(R.id.disappear);
        es = (TextView)findViewById(R.id.sad);
        ea = (TextView)findViewById(R.id.angry);
    }

    ValueEventListener Listener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            happy = dataSnapshot.child("기쁨").getValue().toString();
            fireangry = dataSnapshot.child("분노").getValue().toString();
            disappear = dataSnapshot.child("불안").getValue().toString();
            sad = dataSnapshot.child("슬픔").getValue().toString();
            angry = dataSnapshot.child("짜증").getValue().toString();
            eh.setText(happy);
            efa.setText(fireangry);
            eda.setText(disappear);
            es.setText(sad);
            ea.setText(angry);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {}

    };

}

