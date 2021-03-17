package com.example.fixkatalog;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ChatAdminUser extends AppCompatActivity {
    TextView lAdminChatUser, chat;
    EditText kolomChat;
    ImageView sendChat;
    String user, room;
    DatabaseReference root;
    String temp_key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_admin_user);

        lAdminChatUser=findViewById(R.id.lAdminChatUser);
        chat=findViewById(R.id.chat);
        kolomChat=findViewById(R.id.kolomChat);
        sendChat=findViewById(R.id.sendChat);

        user = getIntent().getExtras().get("user").toString();
        room = getIntent().getExtras().get("room").toString();

        setTitle("Room - "+room);

        root = FirebaseDatabase.getInstance().getReference().child("chat").child(room);


        if(getIntent().getExtras()!=null){

            Bundle bundle = getIntent().getExtras();
            lAdminChatUser.setText(bundle.getString("lnama"));

        }else{

            lAdminChatUser.setText("Nama Tidak Tersedia");

        }

        sendChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(kolomChat.getText().toString())) {
                    kolomChat.setError("Tolong isi Dulu Pesannya");
                    return;
                }

                Map<String,Object> map = new HashMap<String, Object>();
                temp_key = root.push().getKey();
                root.updateChildren(map);

                DatabaseReference message_root = root.child(temp_key);
                Map<String,Object> map2 = new HashMap<String, Object>();
                map2.put("nama", user);
                map2.put("pesan",kolomChat.getText().toString());

                message_root.updateChildren(map2);
                kolomChat.setText(new String(""));

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

            chat.append(chat_msg + " : "+chat_user_name +"\n");


        }
    }


    public void kembali(View view){
        Intent intent=new Intent(ChatAdminUser.this, MenuAdmin.class);
        String nama = lAdminChatUser.getText().toString();
        intent.putExtra("lnama", nama);
        startActivity(intent);
    }

    public void chat(View view){
        Intent intent=new Intent(ChatAdminUser.this, ChatAdmin.class);
        String nama = lAdminChatUser.getText().toString();
        intent.putExtra("lnama", nama);
        startActivity(intent);
    }

}