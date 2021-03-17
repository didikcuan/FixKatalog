package com.example.fixkatalog;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

public class BarangMasuk extends AppCompatActivity {

    ImageView fotoTambahBarangMasuk;

    TextView lAdminDataBarangMasuk;

    EditText searchBarangMasuk;
    RecyclerView recyclerViewBarangMasuk;
    FirebaseRecyclerOptions<BarangMasukClass> options;
    FirebaseRecyclerAdapter<BarangMasukClass,BarangMasukHolder> adapter;
    DatabaseReference Dataref;

    ProgressBar barBarangMasuk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barang_masuk);

        Dataref= FirebaseDatabase.getInstance().getReference().child("barangmasuk");
        searchBarangMasuk=findViewById(R.id.searchBarangMasuk);
        recyclerViewBarangMasuk=findViewById(R.id.recyclerViewBarangMasuk);
        fotoTambahBarangMasuk=findViewById(R.id.fotoTambahBarangMasuk);
        lAdminDataBarangMasuk=findViewById(R.id.lAdminDataBarangMasuk);
        barBarangMasuk=findViewById(R.id.barBarangMasuk);

        barBarangMasuk.setVisibility(View.VISIBLE);

        recyclerViewBarangMasuk.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerViewBarangMasuk.setHasFixedSize(true);
        fotoTambahBarangMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(BarangMasuk.this,BarangMasukTambah.class);
                String nama = lAdminDataBarangMasuk.getText().toString();
                intent.putExtra("lnama", nama);
                startActivity(intent);
            }
        });

        if(getIntent().getExtras()!=null){

            Bundle bundle = getIntent().getExtras();
            lAdminDataBarangMasuk.setText(bundle.getString("lnama"));

        }else{

            lAdminDataBarangMasuk.setText("Nama Tidak Tersedia");

        }

        LoadData("");

        searchBarangMasuk.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString()!=null)
                {
                    LoadData(s.toString());
                }
                else
                {
                    LoadData("");
                }

            }
        });


    }

    private void LoadData(String data) {
        Query query=Dataref.orderByChild("kodebarangmasuk").startAt(data).endAt(data+"\uf8ff");

        options=new FirebaseRecyclerOptions.Builder<BarangMasukClass>().setQuery(query,BarangMasukClass.class).build();
        adapter=new FirebaseRecyclerAdapter<BarangMasukClass, BarangMasukHolder> (options) {
            @Override
            protected void onBindViewHolder(@NonNull BarangMasukHolder holder, final int position, @NonNull BarangMasukClass model) {
                holder.textKodeBarangMasukList.setText(model.getKodebarangmasuk());
                holder.textNamaBarangMasukList.setText(model.getNamabarang());


                ///
                String setTextView ;
                String replace = model.getHargabarangmasuk().replaceAll("[Rp. ]","");
                if (!replace.isEmpty())
                {
                    setTextView = formatRupiah(Double.parseDouble(replace));


                }else
                {

                    setTextView = "No data";
                }

                ///




                holder.textHargaBarangMasukList.setText(setTextView);
                holder.textJumlahBarangList.setText(model.getJumlahbarang());
                holder.edit.setText("Kode Barang Masuk : ");
                Picasso.get().load(model.getImageUrl()).into(holder.fotoDataBarangMasukList);

                holder.v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(BarangMasuk.this,BarangMasukEdit.class);
                        intent.putExtra("kodebarangmasuk",getRef(position).getKey());
                        String nama = lAdminDataBarangMasuk.getText().toString();
                        intent.putExtra("lnama", nama);
                        startActivity(intent);



                    }
                });

            }

            @NonNull
            @Override
            public BarangMasukHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.barang_masuk_list,parent,false);
                return new BarangMasukHolder(v);
            }
        };
        adapter.startListening();
        recyclerViewBarangMasuk.setAdapter(adapter);
        barBarangMasuk.setVisibility(View.INVISIBLE);
    }

    public void kembali(View view){
        Intent intent=new Intent(BarangMasuk.this,MenuAdmin.class);
        String nama = lAdminDataBarangMasuk.getText().toString();
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
