package com.example.moodcoin;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class LobbyActivity extends AppCompatActivity {

    String id;
    String today;
    Bundle bundle = new Bundle();
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FragmentMain fragmentMain = new FragmentMain();
    private FragmentCamera fragmentCamera = new FragmentCamera();
    private FragmentCall fragmentCall = new FragmentCall();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        id = getIntent().getStringExtra("id");
        today = getIntent().getStringExtra("today");
        mDB_ref = FirebaseDatabase.getInstance().getReference().child(id);

        id_str = (TextView) findViewById(R.id.id_input);
        eh = (TextView) findViewById(R.id.happy);
        efa = (TextView) findViewById(R.id.fireangry);
        eda = (TextView) findViewById(R.id.disappear);
        es = (TextView) findViewById(R.id.sad);
        ea = (TextView) findViewById(R.id.angry);

        bundle.putString("id", id);
        bundle.putString("today", today);

        fragmentMain.setArguments(bundle);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragmentMain).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new LobbyActivity.ItemSelectedListener());
    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            switch(menuItem.getItemId())
            {
                case R.id.searchItem:
                    transaction.replace(R.id.frameLayout, fragmentMain).commit();
                    break;
                case R.id.cameraItem:
                    transaction.replace(R.id.frameLayout, fragmentCamera).commitAllowingStateLoss();
                    break;
                case R.id.callItem:
                    transaction.replace(R.id.frameLayout, fragmentCall).commitAllowingStateLoss();
                    break;
            }
            return true;
        }
    }
}
