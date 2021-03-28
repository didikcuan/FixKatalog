package com.example.fixkatalog;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

public class Dummy extends AppCompatActivity {

    RecyclerView recycler1;
    FirebaseRecyclerOptions<KeranjangClass> options;
    FirebaseRecyclerAdapter<KeranjangClass,KeranjangHolder> adapter;
    String nama,key,uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dummy);

        onRestart();
        onStart();
        recycler1 = findViewById(R.id.recycler1);
        recycler1.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recycler1.setHasFixedSize(true);

        if(getIntent().getExtras()!=null){

            Bundle bundle = getIntent().getExtras();
            nama = bundle.getString("lnama");
            key = bundle.getString("key");
            uid = bundle.getString("uid");

        }

        load();



    }


    private void load() {
        DatabaseReference Dataref= FirebaseDatabase.getInstance().getReference().child("konfirmasipembayaran");
        Query query= Dataref.orderByChild("kodekeranjang").equalTo(uid+"1"+key);

        options=new FirebaseRecyclerOptions.Builder<KeranjangClass>().setQuery(query,KeranjangClass.class).build();
        adapter=new FirebaseRecyclerAdapter<KeranjangClass, KeranjangHolder> (options) {
            @Override
            protected void onBindViewHolder(@NonNull KeranjangHolder holder, final int position, @NonNull KeranjangClass model) {
                holder.kNama.setText(model.getNamabarang());
                holder.kUkuran.setText(model.getUkuranbarang());
                holder.kJumlah.setText(model.getJumlahbarang());
                holder.kHarga.setText(model.getHargabarang());

                DatabaseReference tampil = FirebaseDatabase.getInstance().getReference().child("tampil");
                tampil.child("1"+model.getKodebarang()+model.getUkuranbarang()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                        Integer itu= Integer.valueOf(dataSnapshot1.child("jumlahbarang").getValue().toString());
                        String hasil = String.valueOf(itu+Integer.valueOf(model.getJumlahbarang()));
                        dataSnapshot1.getRef().child("jumlahbarang").setValue(hasil);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @NonNull
            @Override
            public KeranjangHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.keranjang_list,parent,false);
                return new KeranjangHolder(v);
            }
        };
        adapter.startListening();
        recycler1.setAdapter(adapter);
        Intent intent=new Intent(Dummy.this,MenuAdmin.class);
        intent.putExtra("lnama", nama);
        startActivity(intent);
    }
    
}
