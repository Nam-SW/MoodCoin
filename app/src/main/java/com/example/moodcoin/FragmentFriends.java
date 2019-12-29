package com.example.moodcoin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public class FragmentFriends extends Fragment {
    DatabaseReference mDB_ref;
    TextView friends_count, friends_table, help_suggestion;
    HashMap<String, Object> childUpdates = null;
    Button add_friend, delete_friend;
    LinearLayout help_window;
    String id;
    String today;
    String happy, fireangry, disappear, sad, angry;
    SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        childUpdates = new HashMap<>();
        Bundle bundle = this.getArguments();
        id = bundle.getString("id");
        today = bundle.getString("today");

        View v = inflater.inflate(R.layout.fragment_friends, container, false);

        friends_count = (TextView) v.findViewById(R.id.friends_count);
        friends_table = (TextView) v.findViewById(R.id.friends_table);
        help_suggestion = (TextView) v.findViewById(R.id.help_suggestion);
        add_friend = (Button) v.findViewById(R.id.add_friend);
        delete_friend = (Button) v.findViewById(R.id.delete_friend);
        help_window = (LinearLayout) v.findViewById(R.id.help_window);

        mDB_ref = FirebaseDatabase.getInstance().getReference();
        mDB_ref.addListenerForSingleValueEvent(Listener);

        add_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_friend_show();
            }
        });

        delete_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete_friend_show();
            }
        });

        return v;
    }

    ValueEventListener Listener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            String value = "";
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
            }



            try{
                value = dataSnapshot.child(id).child("friends").child("count").getValue().toString();
                friends_count.setText(value + "명의 친구가\n연결되어 있어요");
            }catch (Exception e){
                childUpdates.put(id + "/friends/count", 0);
                mDB_ref.updateChildren(childUpdates);
                friends_count.setText("0명의 친구가\n연결되어 있어요");
            }
            try{
                value = dataSnapshot.child(id).child("friends").child("name_list").getValue().toString();
                friends_table.setText(value);
                String help_roulette[] = value.split("\n");
                int len = help_roulette.length;
                String roulette_survived[] = new String[len];
                int z = 0;
                for(int i = 0; i < help_roulette.length ; i++){
                    Log.d("ㅇㅇ",help_roulette[i]);
                    String mydb_getid = dataSnapshot.child(id).child("friends").child(help_roulette[i]).getValue().toString();
                    try{
                        int[] moods_f = {
                                Integer.parseInt(dataSnapshot.child(mydb_getid).child(today).child("기쁨").getValue().toString()),
                                Integer.parseInt(dataSnapshot.child(mydb_getid).child(today).child("분노").getValue().toString()),
                                Integer.parseInt(dataSnapshot.child(mydb_getid).child(today).child("불안").getValue().toString()),
                                Integer.parseInt(dataSnapshot.child(mydb_getid).child(today).child("슬픔").getValue().toString()),
                                Integer.parseInt(dataSnapshot.child(mydb_getid).child(today).child("짜증").getValue().toString())
                        };
                        String[] most_arr = {"기쁨", "분노", "불안", "슬픔", "짜증"};
                        int max=0, cnt=0;
                        for(int j = 0; j < 5; j++){
                            if(max < moods_f[j]){
                                max = moods_f[j];
                                cnt = j;
                            }
                        }
                        if(!most_arr[cnt].equals("기쁨") && cnt != 0){
                            roulette_survived[z] = help_roulette[i];
                            Log.d("생존자",roulette_survived[z]);
                            z++;
                        }
                    }catch (Exception e){}
                }
                //Log.d("생존자",roulette_survived[(int)(Math.random()*(roulette_survived.length - 1))]);
            }catch(Exception e){
                childUpdates.put(id + "/friends/name_list","");
                mDB_ref.updateChildren(childUpdates);
                friends_table.setText("");
            }

            if(gethighest().trim().equals("기쁨")){
                help_window.setVisibility(View.VISIBLE);
            }else{
                help_window.setVisibility(View.GONE);
            }

        }



        private String gethighest(){
            int [] arr = {Integer.parseInt(happy), Integer.parseInt(fireangry), Integer.parseInt(disappear), Integer.parseInt(sad), Integer.parseInt(angry)};
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
            if(max == 0){
                sum = "X";
            }
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



    void add_friend_show(){
        final LinearLayout linear = (LinearLayout)View.inflate(getActivity(), R.layout.dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("친구 추가/수정");
        builder.setMessage("친구의 이름과 ID를 차례대로 입력하세요.");
        builder.setView(linear);
        builder.setPositiveButton("추가",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        EditText id_inputed = (EditText)linear.findViewById(R.id.id);
                        EditText name_inputed = (EditText)linear.findViewById(R.id.name);
                        String name_val = name_inputed.getText().toString().trim();
                        String id_val = id_inputed.getText().toString().trim();
                        if(id_val.equals("") || name_val.equals("")){
                            Toast.makeText(getActivity(),"id와 이름을 모두 입력해주세요",Toast.LENGTH_LONG).show();
                            add_friend_show();
                            return;
                        }else if(id_val.length()!=18){
                            Toast.makeText(getActivity(),"id는 숫자 18자입니다.",Toast.LENGTH_LONG).show();
                            add_friend_show();
                            return;
                        }else{
                            String table = friends_table.getText().toString();
                            mDB_ref.child(id).child("friends").child("name_list").setValue(table + "\n" + name_val);
                            mDB_ref.child(id).child("friends").child(name_val).setValue(id_val);
                            friends_table.setText(table + "\n" + name_val);
                            int count = Integer.parseInt(friends_count.getText().toString().replaceAll("[^0-9]","")) + 1;
                            friends_count.setText(Integer.toString(count) + "명의 친구가\n연결되어 있어요");
                            mDB_ref.child(id).child("friends").child("count").setValue(Integer.toString(count));
                            Toast.makeText(getActivity(),name_val + " 님이 친구가 되었습니다.",Toast.LENGTH_LONG).show();
                        }
                    }
                });
        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) { }
                });
        builder.show();
    }

    void delete_friend_show(){
        final EditText del_name = new EditText(getActivity());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("친구 삭제");
        builder.setMessage("삭제할 친구의 이름을 입력하세요.");
        builder.setView(del_name);
        builder.setPositiveButton("삭제",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String name_val = del_name.getText().toString().trim();
                        if(name_val.equals("")){
                            Toast.makeText(getActivity(),"이름을 입력해주세요",Toast.LENGTH_LONG).show();
                            delete_friend_show();
                            return;
                        }else{
                            if(friends_table.getText().toString().contains(name_val)){
                                String table = friends_table.getText().toString();
                                int count = Integer.parseInt(friends_count.getText().toString().replaceAll("[^0-9]","")) - 1;
                                friends_count.setText(Integer.toString(count) + "명의 친구가\n연결되어 있어요");
                                mDB_ref.child(id).child("friends").child("count").setValue(Integer.toString(count));
                                table = table.replace("\n" + name_val,"");
                                mDB_ref.child(id).child("friends").child("name_list").setValue(table);
                                friends_table.setText(table);
                                mDB_ref.child(id).child("friends").child(name_val).removeValue();
                                Toast.makeText(getActivity() ,name_val + " 님이 친구 목록에서 삭제되었습니다.",Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(getActivity() ,"해당 이름을 가진 친구는 존재하지 않습니다.",Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) { }
                });
        builder.show();
    }


}
