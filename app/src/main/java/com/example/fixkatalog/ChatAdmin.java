package com.example.fixkatalog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ChatAdmin extends AppCompatActivity {
    TextView lAdminChatRoom;
    ListView listRoom;
    EditText namaRoom;
    ImageView sendRoom;
    String name;
    DatabaseReference root = FirebaseDatabase.getInstance().getReference().child("chat");

    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_rooms = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_admin);

        lAdminChatRoom=findViewById(R.id.lAdminChatRoom);
        listRoom=findViewById(R.id.listRoom);
        namaRoom=findViewById(R.id.namaRoom);
        sendRoom=findViewById(R.id.sendRoom);


        if(getIntent().getExtras()!=null){

            Bundle bundle = getIntent().getExtras();
            lAdminChatRoom.setText(bundle.getString("lnama"));

        }else{

            lAdminChatRoom.setText("Nama Tidak Tersedia");

        }

        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list_of_rooms);
        listRoom.setAdapter(arrayAdapter);


        name = lAdminChatRoom.getText().toString();


        sendRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String,Object> map = new HashMap<String,Object>();
                map.put(namaRoom.getText().toString(),"");
                root.updateChildren(map);
                namaRoom.setText(new String(""));

            }
        });
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Set<String> set = new HashSet<String>();
                Iterator i = dataSnapshot.getChildren().iterator();
                while ( i.hasNext())
                {
                    set.add(((DataSnapshot)i.next()).getKey());
                }
                list_of_rooms.clear();
                list_of_rooms.addAll(set);

                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listRoom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent I = new Intent(getApplicationContext(),ChatAdminUser.class);
                I.putExtra("room",((TextView)view).getText().toString());
                I.putExtra("user",name);
                String nama = lAdminChatRoom.getText().toString();
                I.putExtra("lnama", nama);
                startActivity(I);
            }
        });

    }






    public void kembali(View view){
        Intent intent=new Intent(ChatAdmin.this,MenuAdmin.class);
        String nama = lAdminChatRoom.getText().toString();
        intent.putExtra("lnama", nama);
        startActivity(intent);
    }

    public void chat(View view){
        Intent intent=new Intent(ChatAdmin.this,ChatAdmin.class);
        String nama = lAdminChatRoom.getText().toString();
        intent.putExtra("lnama", nama);
        startActivity(intent);
    }

}