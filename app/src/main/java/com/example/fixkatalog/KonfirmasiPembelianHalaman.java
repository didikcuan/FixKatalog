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

import java.text.NumberFormat;
import java.util.Locale;

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
        onRestart();
        onStart();
        Dataref= FirebaseDatabase.getInstance().getReference().child("tampilkeranjang");

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
                        a.setText("1");
                        break;
                    case R.id.radioSudahAdmin:
                        a.setText("2");
                        break;
                    case R.id.radioTolakAdmin:
                        a.setText("3");
                        break;
                }
            }
        });
        LoadData("1");
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

        Query query=Dataref.orderByChild("status").equalTo(data);

        options=new FirebaseRecyclerOptions.Builder<KonfirmasiPembelianClass>().setQuery(query,KonfirmasiPembelianClass.class).build();
        adapter=new FirebaseRecyclerAdapter<KonfirmasiPembelianClass, KonfirmasiPembelianHolder> (options) {
            @Override
            protected void onBindViewHolder(@NonNull KonfirmasiPembelianHolder holder, final int position, @NonNull KonfirmasiPembelianClass model) {
                holder.textNo.setText(model.getKey());
                String key = model.getKodekeranjang();
                if (Integer.valueOf(model.getStatus()) == 1){
                    holder.textStatus.setText("Belum Dikonfirmasi");
                }
                if (Integer.valueOf(model.getStatus()) == 2){
                    holder.textStatus.setText("Dikonfirmasi");
                }
                if (Integer.valueOf(model.getStatus()) == 3){
                    holder.textStatus.setText("Ditolak");
                }

                holder.textPembeli.setText(model.getNamapembeli());

                Picasso.get().load(model.getImageUrlPembeli()).into(holder.fotoPembelianBarang);

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
                holder.textJumlahHarga.setText(setTextView);

                holder.v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(KonfirmasiPembelianHalaman.this,KonfirmasiPembelian.class);
                        intent.putExtra("key_temp",model.getKey());
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
    private  String formatRupiah(Double number){
        Locale localeID = new Locale("IND","ID");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(localeID);
        String formatrupiah = numberFormat.format(number);
        String[] split = formatrupiah.split(",");
        int length = split[0].length();
        return split[0].substring(0,2)+". "+split[0].substring(2,length);
    }

}
