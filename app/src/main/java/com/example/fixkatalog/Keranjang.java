package com.example.fixkatalog;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

public class Keranjang extends AppCompatActivity {

    ImageView history, cekOut;
    TextView lUserKeranjang;
    RecyclerView recycler;
    FirebaseRecyclerOptions<KeranjangClass> options;
    FirebaseRecyclerAdapter<KeranjangClass,KeranjangHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.keranjang);

        onRestart();
        onStart();

        lUserKeranjang = findViewById(R.id.lUserKeranjang);
        history = findViewById(R.id.history);
        cekOut = findViewById(R.id.cekOut);
        recycler=findViewById(R.id.recycler);

        recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recycler.setHasFixedSize(true);

        if(getIntent().getExtras()!=null){

            Bundle bundle = getIntent().getExtras();
            lUserKeranjang.setText(bundle.getString("lnama"));

        }else{

            lUserKeranjang.setText("Nama Tidak Tersedia");

        }

        load();
        total();

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Keranjang.this,KeranjangHalaman.class);
                String nama = lUserKeranjang.getText().toString();
                intent.putExtra("lnama", nama);
                startActivity(intent);

            }
        });

    }

    private void total(){
        ///

        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        String uid = fAuth.getCurrentUser().getUid();
        Query query = FirebaseDatabase.getInstance().getReference("konfirmasipembayaran")
                .orderByChild("kodekeranjang").equalTo(uid+"0");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TextView totalBayar = findViewById(R.id.totalBayar);
                if (dataSnapshot.exists())
                {

                    int sum = 0;
                    int sum12 = 0;
                    for(DataSnapshot ds : dataSnapshot.getChildren()){

                        Map<String,Object> map = (Map<String, Object>) ds.getValue();
                        Object hargabarang =  map.get("hargabarang");
                        Object hargapokok = map.get("hargapokok");
                        Object jumlah = map.get("jumlahbarang");

                        int pokok = Integer.parseInt(String.valueOf(hargapokok));
                        int barang = Integer.parseInt(String.valueOf(hargabarang));
                        int jValue = Integer.parseInt(String.valueOf(jumlah));
                        int sumi = barang * jValue ;
                        int sum1 = pokok * jValue ;

                        sum12 += sum1;
                        sum += sumi;
                        Log.d("Sum",String.valueOf(sum));
                        Log.d("Sum12",String.valueOf(sum12));
                        String jumlah1 = String.valueOf(sum);
                        String jumlahpokok = String.valueOf(sum12);

                        cekOut.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(Keranjang.this,KeranjangEdit.class);
                                String nama = lUserKeranjang.getText().toString();
                                intent.putExtra("lnama", nama);
                                intent.putExtra("jumlah", jumlah1);
                                intent.putExtra("jumlahpokok", jumlahpokok);
                                startActivity(intent);
                            }
                        });
                        String setTextView ;
                        String replace = String.valueOf(sum).replaceAll("[.]","");
                        if (!replace.isEmpty())
                        {
                            setTextView = formatRupiah(Double.parseDouble(replace));


                        }else
                        {

                            setTextView = "No data";
                        }

                        totalBayar.setText(setTextView);

                    }
                }else{
                    totalBayar.setText("Rp. 0");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ///////
    }

    private void load() {
        DatabaseReference Dataref= FirebaseDatabase.getInstance().getReference().child("konfirmasipembayaran");
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        String uid = fAuth.getCurrentUser().getUid();
        Query query= Dataref.orderByChild("kodekeranjang").equalTo(uid+"0");

        options=new FirebaseRecyclerOptions.Builder<KeranjangClass>().setQuery(query,KeranjangClass.class).build();
        adapter=new FirebaseRecyclerAdapter<KeranjangClass, KeranjangHolder> (options) {
            @Override
            protected void onBindViewHolder(@NonNull KeranjangHolder holder, final int position, @NonNull KeranjangClass model) {
                holder.kNama.setText(model.getNamabarang());
                holder.kUkuran.setText(model.getUkuranbarang());
                holder.kJumlah.setText(model.getJumlahbarang());

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

                holder.kHarga.setText(setTextView);

                holder.kDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
                        Dataref.child(getRef(position).getKey()).removeValue();
                        load();
                        total();
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
        recycler.setAdapter(adapter);
    }


    public void kembali(View view){
        Intent intent=new Intent(Keranjang.this,MainActivity.class);
        String nama = lUserKeranjang.getText().toString();
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
