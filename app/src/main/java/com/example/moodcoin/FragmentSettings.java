package com.example.moodcoin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class FragmentSettings extends Fragment {
    String id, today;
    Button logout;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        id = bundle.getString("id");
        today = bundle.getString("today");
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        logout = (Button)v.findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    BufferedWriter bw = new BufferedWriter(new FileWriter( getActivity().getFilesDir() + "check.txt", false));
                    bw.write("0");
                    bw.close();
                    BufferedWriter sum = new BufferedWriter(new FileWriter(getActivity().getFilesDir() + "/recent_sum.txt", false));
                    sum.write("0");
                    sum.close();
                    BufferedWriter set = new BufferedWriter(new FileWriter(getActivity().getFilesDir() + "/recent_date.txt", false));
                    set.write("0");
                    set.close();
                    BufferedWriter sendprice_br = new BufferedWriter(new FileWriter(getActivity().getFilesDir() + "recent_send_price.txt", false));
                    sendprice_br.write("0");
                    sendprice_br.close();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        return v;
    }
}
