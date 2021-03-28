package com.example.fixkatalog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;

public class DataBarang extends AppCompatActivity {

    ImageView fotoTambahBarang;

    TextView lAdminDataBarang;

    EditText searchBarang;
    RecyclerView recyclerViewBarang;
    FirebaseRecyclerOptions<DataBarangClass> options;
    FirebaseRecyclerAdapter<DataBarangClass,DataBarangHolder> adapter;
    DatabaseReference Dataref;

    ProgressBar barDataBarang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_barang);
        onRestart();
        onStart();
        Dataref= FirebaseDatabase.getInstance().getReference().child("barang");
        searchBarang=findViewById(R.id.searchBarang);
        recyclerViewBarang=findViewById(R.id.recyclerViewBarang);
        fotoTambahBarang=findViewById(R.id.fotoTambahBarang);
        lAdminDataBarang=findViewById(R.id.lAdminDataBarang);
        barDataBarang=findViewById(R.id.barDataBarang);

        barDataBarang.setVisibility(View.VISIBLE);

        recyclerViewBarang.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerViewBarang.setHasFixedSize(true);
        fotoTambahBarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DataBarang.this,DataBarangTambah.class);
                String nama = lAdminDataBarang.getText().toString();
                intent.putExtra("lnama", nama);
                startActivity(intent);
            }
        });

        if(getIntent().getExtras()!=null){

            Bundle bundle = getIntent().getExtras();
            lAdminDataBarang.setText(bundle.getString("lnama"));

        }else{

            lAdminDataBarang.setText("Nama Tidak Tersedia");

        }

        LoadData("");

        searchBarang.addTextChangedListener(new TextWatcher() {
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
        Query query=Dataref.orderByChild("kodebarang").startAt(data).endAt(data+"\uf8ff");

        options=new FirebaseRecyclerOptions.Builder<DataBarangClass>().setQuery(query,DataBarangClass.class).build();
        adapter=new FirebaseRecyclerAdapter<DataBarangClass, DataBarangHolder> (options) {
            @Override
            protected void onBindViewHolder(@NonNull DataBarangHolder holder, final int position, @NonNull DataBarangClass model) {
                holder.textNamaBarangList.setText(model.getNamabarang());
                holder.textKodeBarangList.setText(model.getKodebarang());

                ///
                String setTextView ;
                String replace = model.getHargabarang().replaceAll("[Rp. ]","");
                if (!replace.isEmpty())
                {
                    setTextView = formatRupiah(Double.parseDouble(replace));


                }else
                {

                    setTextView = "No data";
                }

                ///

                holder.textHargaBarangList.setText(setTextView);
                Picasso.get().load(model.getImageUrl()).into(holder.fotoDataBarangList);

                holder.v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(DataBarang.this,DataBarangEdit.class);
                        intent.putExtra("kodebarang",getRef(position).getKey());

                        String nama = lAdminDataBarang.getText().toString();
                        intent.putExtra("lnama", nama);
                        startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public DataBarangHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.data_barang_list,parent,false);
                return new DataBarangHolder(v);
            }
        };
        adapter.startListening();
        recyclerViewBarang.setAdapter(adapter);
        barDataBarang.setVisibility(View.INVISIBLE);
    }

    public void kembali(View view){
        Intent intent=new Intent(DataBarang.this,MenuAdmin.class);
        String nama = lAdminDataBarang.getText().toString();
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
