package com.example.fixkatalog;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class KonfirmasiPembelianHalaman extends AppCompatActivity {

    TextView lAdminPembelian;

    RadioGroup listAdmin;

    RecyclerView recyclerViewPembelian;
    FirebaseRecyclerOptions<KonfirmasiPembelianClass> options;
    FirebaseRecyclerAdapter<KonfirmasiPembelianClass,KonfirmasiPembelianHolder> adapter;
    DatabaseReference Dataref;

    ProgressBar barPembelianAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.konfirmasi_pembelian_halaman);

        Dataref= FirebaseDatabase.getInstance().getReference().child("konfirmasipembayaran");

        recyclerViewPembelian=findViewById(R.id.recyclerViewPembelian);
        barPembelianAdmin=findViewById(R.id.barPembelianAdmin);
        lAdminPembelian=findViewById(R.id.lAdminPembelian);

        recyclerViewPembelian.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerViewPembelian.setHasFixedSize(true);

        if(getIntent().getExtras()!=null){

            Bundle bundle = getIntent().getExtras();
            lAdminPembelian.setText(bundle.getString("lnama"));

        }else{

            lAdminPembelian.setText("Nama Tidak Tersedia");

        }
        TextView a = findViewById(R.id.z);
        listAdmin = findViewById(R.id.listAdmin);
        listAdmin.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                switch (id){
                    case R.id.radioBelumAdmin:
                        a.setText("0");
                        break;
                    case R.id.radioSudahAdmin:
                        a.setText("1");
                        break;
                    case R.id.radioTolakAdmin:
                        a.setText("2");
                        break;
                }
            }
        });
        LoadData("");
        a.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString() != null)
                {
                    LoadData(s.toString());
                }else
                {
                    LoadData("");
                }
            }
        });

    }

    private void LoadData(String data) {


        Query query=Dataref.orderByChild("konfirmasi").startAt(data).endAt(data);

        options=new FirebaseRecyclerOptions.Builder<KonfirmasiPembelianClass>().setQuery(query,KonfirmasiPembelianClass.class).build();
        adapter=new FirebaseRecyclerAdapter<KonfirmasiPembelianClass, KonfirmasiPembelianHolder> (options) {
            @Override
            protected void onBindViewHolder(@NonNull KonfirmasiPembelianHolder holder, final int position, @NonNull KonfirmasiPembelianClass model) {
                holder.textNamaProduk.setText(model.getNamabarang());
                holder.textQTY.setText(model.getJumlahbarang());
                holder.textPembeli.setText(model.getNamapembeli());
                Picasso.get().load(model.getImageUrlBarang()).into(holder.fotoPembelianBarang);

                holder.v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(KonfirmasiPembelianHalaman.this,KonfirmasiPembelian.class);
                        intent.putExtra("uid",getRef(position).getKey());
                        String nama = lAdminPembelian.getText().toString();
                        intent.putExtra("lnama", nama);
                        startActivity(intent);



                    }
                });

            }

            @NonNull
            @Override
            public KonfirmasiPembelianHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.konfirmasi_pembelian_list,parent,false);
                return new KonfirmasiPembelianHolder(v);
            }
        };
        adapter.startListening();
        recyclerViewPembelian.setAdapter(adapter);
        barPembelianAdmin.setVisibility(View.INVISIBLE);
    }

    public void kembali(View view){
        Intent intent=new Intent(KonfirmasiPembelianHalaman.this,MenuAdmin.class);
        String nama = lAdminPembelian.getText().toString();
        intent.putExtra("lnama", nama);
        startActivity(intent);
    }

}
