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

public class PengeluaranLainnyaHalaman extends AppCompatActivity {

    ImageView fotoTambahPengeluaran;

    TextView lAdminPengeluaranLainnya;

    EditText searchPengeluaran;
    RecyclerView recyclerViewPengeluaran;
    FirebaseRecyclerOptions<PengeluaranLainnyaClass> options;
    FirebaseRecyclerAdapter<PengeluaranLainnyaClass,PengeluaranLainnyaHolder> adapter;
    DatabaseReference Dataref;

    ProgressBar barPengeluaran;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pengeluaran_lainnya_halaman);
        onRestart();
        onStart();
        Dataref= FirebaseDatabase.getInstance().getReference().child("pengeluaran");

        fotoTambahPengeluaran=findViewById(R.id.fotoTambahPengeluaran);
        lAdminPengeluaranLainnya=findViewById(R.id.lAdminPengeluaranLainnya);
        searchPengeluaran=findViewById(R.id.searchPengeluaran);
        recyclerViewPengeluaran=findViewById(R.id.recyclerViewPengeluaran);
        barPengeluaran=findViewById(R.id.barPengeluaran);

        barPengeluaran.setVisibility(View.VISIBLE);

        recyclerViewPengeluaran.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerViewPengeluaran.setHasFixedSize(true);
        fotoTambahPengeluaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PengeluaranLainnyaHalaman.this,PengeluaranLainnya.class);
                String nama = lAdminPengeluaranLainnya.getText().toString();
                intent.putExtra("lnama", nama);
                startActivity(intent);
            }
        });

        if(getIntent().getExtras()!=null){

            Bundle bundle = getIntent().getExtras();
            lAdminPengeluaranLainnya.setText(bundle.getString("lnama"));

        }else{

            lAdminPengeluaranLainnya.setText("Nama Tidak Tersedia");

        }

        LoadData("");

        searchPengeluaran.addTextChangedListener(new TextWatcher() {
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
        Query query=Dataref.orderByChild("tanggal").startAt(data).endAt(data+"\uf8ff");

        options=new FirebaseRecyclerOptions.Builder<PengeluaranLainnyaClass>().setQuery(query,PengeluaranLainnyaClass.class).build();
        adapter=new FirebaseRecyclerAdapter<PengeluaranLainnyaClass, PengeluaranLainnyaHolder> (options) {
            @Override
            protected void onBindViewHolder(@NonNull PengeluaranLainnyaHolder holder, final int position, @NonNull PengeluaranLainnyaClass model) {
                ///

                holder.textTanggalPengeluaran.setText(model.getTanggal());
                holder.textNamaAdmin.setText(model.getNamaadmin());
                Picasso.get().load(model.getImageUrl()).into(holder.fotoAdmin);

                String setTextView ;
                String replace = model.getTotalbiaya().replaceAll("[Rp. ]","");
                if (!replace.isEmpty())
                {
                    setTextView = formatRupiah(Double.parseDouble(replace));


                }else
                {

                    setTextView = "No data";
                }

                ///
                holder.textTotalBiaya.setText(setTextView);
                holder.v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(PengeluaranLainnyaHalaman.this,PengeluaranLainnyaEdit.class);
                        intent.putExtra("namaadmin",getRef(position).getKey());

                        String nama = lAdminPengeluaranLainnya.getText().toString();
                        intent.putExtra("lnama", nama);
                        startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public PengeluaranLainnyaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.pengeluaran_lainnya_list,parent,false);
                return new PengeluaranLainnyaHolder(v);
            }
        };
        adapter.startListening();
        recyclerViewPengeluaran.setAdapter(adapter);
        barPengeluaran.setVisibility(View.INVISIBLE);
    }

    public void kembali(View view){
        Intent intent=new Intent(PengeluaranLainnyaHalaman.this,MenuAdmin.class);
        String nama = lAdminPengeluaranLainnya.getText().toString();
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
