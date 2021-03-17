package com.example.fixkatalog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ChatUser extends AppCompatActivity {
    TextView lChatUser, chatUser;
    EditText kolomChatUser;
    ImageView sendChatUser;
    String user, room;
    DatabaseReference root;
    String temp_key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_user);

        lChatUser=findViewById(R.id.lChatUser);
        chatUser=findViewById(R.id.chatUser);
        kolomChatUser=findViewById(R.id.kolomChatUser);
        sendChatUser=findViewById(R.id.sendChatUser);

        DatabaseReference root1 = FirebaseDatabase.getInstance().getReference().child("chat");




        if(getIntent().getExtras()!=null){

            Bundle bundle = getIntent().getExtras();
            lChatUser.setText(bundle.getString("lnama"));
            user = lChatUser.getText().toString();
            room = user;
        }else{

            lChatUser.setText("Nama Tidak Tersedia");

        }

        root1.child(user).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        setTitle("Room - "+room);
        root = FirebaseDatabase.getInstance().getReference().child("chat").child(room);

        sendChatUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String,Object> map = new HashMap<String, Object>();
                temp_key = root.push().getKey();
                root.updateChildren(map);

                DatabaseReference message_root = root.child(temp_key);
                Map<String,Object> map2 = new HashMap<String, Object>();
                map2.put("nama", user);
                map2.put("pesan",kolomChatUser.getText().toString());

                message_root.updateChildren(map2);

                kolomChatUser.setText(new String(""));

            }
        });

        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                append_chat_conversatin(dataSnapshot);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                append_chat_conversatin(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    private String chat_msg, chat_user_name;

    private void append_chat_conversatin(DataSnapshot dataSnapshot) {
        Iterator i = dataSnapshot.getChildren().iterator();
        while (i.hasNext())
        {
            chat_msg = (String) ((DataSnapshot)i.next()).getValue();
            chat_user_name = (String) ((DataSnapshot)i.next()).getValue();

            chatUser.append(chat_msg + " : "+chat_user_name +"\n");


        }
    }


    public void kembali(View view){
        Intent intent=new Intent(ChatUser.this, MainActivity.class);
        String nama = lChatUser.getText().toString();
        intent.putExtra("lnama", nama);
        startActivity(intent);
    }

    public void chat(View view){
        Intent intent=new Intent(ChatUser.this, ChatUser.class);
        String nama = lChatUser.getText().toString();
        intent.putExtra("lnama", nama);
        startActivity(intent);
    }

}