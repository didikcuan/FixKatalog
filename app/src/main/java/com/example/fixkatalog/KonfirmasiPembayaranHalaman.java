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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;

public class KonfirmasiPembayaranHalaman extends AppCompatActivity {

    TextView lUserPembayaran;

    RadioGroup list;

    RecyclerView recyclerViewPembayaran;
    FirebaseRecyclerOptions<KonfirmasiPembayaranClass> options;
    FirebaseRecyclerAdapter<KonfirmasiPembayaranClass,KonfirmasiPembayaranHolder> adapter;
    DatabaseReference Dataref;

    ProgressBar barPembayaran;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.konfirmasi_pembayaran_halaman);
        onRestart();
        onStart();
        Dataref= FirebaseDatabase.getInstance().getReference().child("konfirmasipembayaran");

        recyclerViewPembayaran=findViewById(R.id.recyclerViewPembayaran);
        barPembayaran=findViewById(R.id.barPembayaran);
        lUserPembayaran=findViewById(R.id.lUserPembayaran);

        recyclerViewPembayaran.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerViewPembayaran.setHasFixedSize(true);

        if(getIntent().getExtras()!=null){

            Bundle bundle = getIntent().getExtras();
            lUserPembayaran.setText(bundle.getString("lnama"));

        }else{

            lUserPembayaran.setText("Nama Tidak Tersedia");

        }

        LoadData();


    }

    private void LoadData() {

        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        String userId = fAuth.getCurrentUser().getUid();

        Query query=Dataref.orderByChild("uid").startAt(userId).endAt(userId);

        options=new FirebaseRecyclerOptions.Builder<KonfirmasiPembayaranClass>().setQuery(query,KonfirmasiPembayaranClass.class).build();
        adapter=new FirebaseRecyclerAdapter<KonfirmasiPembayaranClass, KonfirmasiPembayaranHolder> (options) {
            @Override
            protected void onBindViewHolder(@NonNull KonfirmasiPembayaranHolder holder, final int position, @NonNull KonfirmasiPembayaranClass model) {
                holder.textNamaBarangPembayaran.setText(model.getNamabarang());
                String a;
                if (Integer.valueOf(model.getKonfirmasi()) == 1)
                {
                    a = "Sudah Dikonfirmasi";
                    holder.textTotalBarang.setText(a);
                }
                if (Integer.valueOf(model.getKonfirmasi()) == 0)
                {
                    a = "Belum Dikonfirmasi";
                    holder.textTotalBarang.setText(a);
                }
                if (Integer.valueOf(model.getKonfirmasi()) == 2)
                {
                    a = "Pembayaran Ditolak";
                    holder.textTotalBarang.setText(a);
                }

                ///
                String setTextView ;
                String replace = model.getJumlahbayar().replaceAll("[Rp. ]","");
                if (!replace.isEmpty())
                {
                    setTextView = formatRupiah(Double.parseDouble(replace));


                }else
                {

                    setTextView = "No data";
                }

                ///

                holder.textNamaPembeli.setText(setTextView);
                Picasso.get().load(model.getImageUrl()).into(holder.fotoKonfirmasiPembayaran);

                holder.v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(KonfirmasiPembayaranHalaman.this,KonfirmasiPembayaranEdit.class);
                        intent.putExtra("uid",getRef(position).getKey());
                        String nama = lUserPembayaran.getText().toString();
                        intent.putExtra("lnama", nama);
                        startActivity(intent);



                    }
                });

            }

            @NonNull
            @Override
            public KonfirmasiPembayaranHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.konfirmasi_pembayaran_list,parent,false);
                return new KonfirmasiPembayaranHolder(v);
            }
        };
        adapter.startListening();
        recyclerViewPembayaran.setAdapter(adapter);
        barPembayaran.setVisibility(View.INVISIBLE);
    }

    public void kembali(View view){
        Intent intent=new Intent(KonfirmasiPembayaranHalaman.this,MainActivity.class);
        String nama = lUserPembayaran.getText().toString();
        intent.putExtra("lnama", nama);
        startActivity(intent);
    }
    private  String formatRupiah(Double number){
        Locale localeID = new Locale("IND","ID");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(localeID);
        String formatrupiah = numberFormat.format(number);
        String[] split = formatrupiah.split(",");
        int length = split[0].length();
        return split[0].substring(0,2)+". "+split[0].substring(2,length);
    }

}
