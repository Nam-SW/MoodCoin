package com.example.moodcoin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

import java.util.HashMap;

public class FragmentFriends extends Fragment {
    DatabaseReference mDB_ref;
    TextView friends_count, friends_table;
    HashMap<String, Object> childUpdates = null;
    Button add_friend, delete_friend;
    String id;
    String today;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        childUpdates = new HashMap<>();
        Bundle bundle = this.getArguments();
        id = bundle.getString("id");
        today = bundle.getString("today");

        View v = inflater.inflate(R.layout.fragment_friends, container, false);

        friends_count = (TextView) v.findViewById(R.id.friends_count);
        friends_table = (TextView) v.findViewById(R.id.friends_table);
        add_friend = (Button) v.findViewById(R.id.add_friend);
        delete_friend = (Button) v.findViewById(R.id.delete_friend);

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
            }catch(Exception e){
                childUpdates.put(id + "/friends/name_list","");
                mDB_ref.updateChildren(childUpdates);
                friends_table.setText("");
            }
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
                            mDB_ref.child(id).child("friends").child("name_list").setValue(table + "\n" + name_val + " 님");
                            mDB_ref.child(id).child("friends").child(name_val).setValue(id_val);
                            friends_table.setText(table + "\n" + name_val + " 님");
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
                                table = table.replace("\n" + name_val + " 님","");
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
